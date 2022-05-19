package edu.zieit.scheduler.services;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.render.SheetRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleLoader;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.api.util.HashUtil;
import edu.zieit.scheduler.config.ScheduleConfig;
import edu.zieit.scheduler.persistence.ScheduleHash;
import edu.zieit.scheduler.persistence.dao.HashesDao;
import edu.zieit.scheduler.render.AsposeRenderer;
import edu.zieit.scheduler.schedule.TimeTable;
import edu.zieit.scheduler.schedule.classroom.ClassroomSchedule;
import edu.zieit.scheduler.schedule.consult.ConsultScheduleLoader;
import edu.zieit.scheduler.schedule.course.*;
import edu.zieit.scheduler.schedule.teacher.TeacherScheduleLoader;
import napi.configurate.yaml.lang.Language;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public final class ScheduleServiceImpl implements ScheduleService {

    private static final Logger logger = LogManager.getLogger(ScheduleServiceImpl.class);

    private final Language lang;
    private final ScheduleConfig config;
    private final HashesDao hashesDao;

    private final SheetRenderer renderer;

    private final ScheduleLoader coursesLoader;
    private final ScheduleLoader teachersLoader;
    private final ScheduleLoader consultLoader;

    private final Map<NamespacedKey, Schedule> coursesSchedule;
    private final Map<String, Schedule> courseByGroup;
    private final Map<String, ClassroomSchedule> classroomSchedule;

    private Schedule teachersSchedule;
    private Schedule consultSchedule;

    private List<String> groups;
    private List<String> classrooms;

    @Inject
    public ScheduleServiceImpl(Language lang, ScheduleConfig config, HashesDao hashesDao) {
        this.lang = lang;
        this.config = config;
        this.hashesDao = hashesDao;

        this.renderer = new AsposeRenderer(config.getRenderOptions());

        this.coursesLoader = new CourseScheduleLoader(renderer);
        this.teachersLoader = new TeacherScheduleLoader(renderer);
        this.consultLoader = new ConsultScheduleLoader(renderer);

        this.coursesSchedule = new HashMap<>();
        this.courseByGroup = new HashMap<>();
        this.classroomSchedule = new HashMap<>();

        this.groups = new LinkedList<>();
        this.classrooms = new LinkedList<>();
    }

    @Override
    public Language getLang() {
        return lang;
    }

    @Override
    public SheetRenderer renderer() {
        return renderer;
    }

    @Override
    public Schedule getCourseSchedule(NamespacedKey key) {
        return coursesSchedule.get(key);
    }

    @Override
    public Collection<Schedule> getCoursesSchedule() {
        return Collections.unmodifiableCollection(coursesSchedule.values());
    }

    @Override
    public Collection<String> getGroups() {
        return groups;
    }

    @Override
    public Collection<String> getClassrooms() {
        return classrooms;
    }

    @Override
    public Optional<Schedule> getCourseByGroup(String group) {
        return Optional.ofNullable(courseByGroup.get(group));
    }

    @Override
    public Optional<Schedule> getClassroomSchedule(String classroom) {
        return Optional.ofNullable(classroomSchedule.get(classroom));
    }

    @Override
    public Schedule getTeacherSchedule() {
        return teachersSchedule;
    }

    @Override
    public Schedule getConsultSchedule() {
        return consultSchedule;
    }

    @Override
    public Collection<Schedule> reloadCourseSchedule(boolean initial) {
        List<Schedule> updated = new LinkedList<>();
        Set<String> groups = new HashSet<>();

        for (CourseScheduleInfo info : config.getCourses()) {
            String newHash = HashUtil.getHash(info.getUrl());

            if (newHash != null) {
                ScheduleHash oldHash = hashesDao.find(info.getId());

                if (initial || !newHash.equals(oldHash.getHash())) {
                    saveHash(info.getId(), newHash);

                    try {
                        for (Schedule schedule : coursesLoader.load(info)) {
                            CourseSchedule course = (CourseSchedule) schedule;

                            coursesSchedule.put(schedule.getKey(), schedule);
                            updated.add(schedule);

                            for (String group : course.getGroupNames()) {
                                courseByGroup.put(group, course);
                                groups.add(group);
                            }

                            logger.info("Loaded schedule '{}'", course.getKey());
                        }
                    } catch (Exception e) {
                        logger.error("Course schedule '{}' cannot be loaded: {}", info.getId(), e.getMessage());
                    }
                }
            }
        }

        if (!groups.isEmpty()) {
            this.groups = groups.stream()
                    .sorted(Comparator.naturalOrder())
                    .collect(Collectors.toList());
        }

        if (!updated.isEmpty()) {
            logger.info("Building classroom schedule ...");
            buildClassroomSchedule();
            logger.info("Classroom schedule has been built");
        }

        return updated;
    }

    @Override
    public boolean reloadTeacherSchedule(boolean initial) {
        String newHash = HashUtil.getHash(config.getTeachers().getUrl());

        if (newHash != null) {
            ScheduleHash oldHash = hashesDao.find(config.getTeachers().getId());

            if (initial || !newHash.equals(oldHash.getHash())) {
                saveHash(config.getTeachers().getId(), newHash);

                try {
                    Optional<Schedule> opt = teachersLoader.loadSingle(config.getTeachers());

                    if (opt.isPresent()) {
                        teachersSchedule = opt.get();
                        logger.info("Loaded teachers schedule");
                        return true;
                    }
                } catch (Exception e) {
                    logger.error("Teacher schedule cannot be loaded", e);
                }
            }
        }

        return false;
    }

    @Override
    public boolean reloadConsultSchedule(boolean initial) {
        String newHash = HashUtil.getHash(config.getConsult().getUrl());

        if (newHash != null) {
            ScheduleHash oldHash = hashesDao.find(config.getConsult().getId());

            if (initial || !newHash.equals(oldHash.getHash())) {
                saveHash(config.getConsult().getId(), newHash);

                try {
                    Optional<Schedule> opt = consultLoader.loadSingle(config.getConsult());

                    if (opt.isPresent()) {
                        consultSchedule = opt.get();
                        logger.info("Loaded consultations schedule");
                        return true;
                    }
                } catch (Exception e) {
                    logger.error("Consultations schedule cannot be loaded", e);
                }
            }
        }

        return false;
    }

    private void buildClassroomSchedule() {
        classroomSchedule.clear();

        Set<String> classrooms = new HashSet<>();

        for (Schedule schedule : coursesSchedule.values()) {
            CourseSchedule course = (CourseSchedule) schedule;

            for (CourseDay day : course.getDays()) {
                int dayIndex = TimeTable.getDayIndex(day.getName());

                for (var classes : day.getClasses().entrySet()) {
                    for (CourseClass cl : classes.getValue()) {
                        if (!cl.getClassroom().isEmpty()) {
                            ClassroomSchedule days = classroomSchedule.computeIfAbsent(cl.getClassroom(),
                                    cr -> new ClassroomSchedule());

                            CourseDay cday = days.getDays().computeIfAbsent(dayIndex, i -> {
                                CourseDay courseDay = new CourseDay();
                                courseDay.setName(day.getName());
                                courseDay.setDate(day.getDate());
                                return courseDay;
                            });

                            cday.addClass(cl.getIndex(), cl);

                            classrooms.add(cl.getClassroom());
                        }
                    }
                }
            }
        }

        this.classrooms = classrooms.stream()
                .sorted(Comparator.naturalOrder())
                .toList();
    }

    private void saveHash(String fileName, String hash) {
        hashesDao.save(new ScheduleHash(fileName, hash));
    }
}
