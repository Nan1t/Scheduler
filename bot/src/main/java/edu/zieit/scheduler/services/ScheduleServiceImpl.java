package edu.zieit.scheduler.services;

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
import edu.zieit.scheduler.schedule.consult.ConsultScheduleLoader;
import edu.zieit.scheduler.schedule.course.CourseSchedule;
import edu.zieit.scheduler.schedule.course.CourseScheduleInfo;
import edu.zieit.scheduler.schedule.course.CourseScheduleLoader;
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
    private List<String> groups;

    private Schedule teachersSchedule;
    private Schedule consultSchedule;

    private boolean firstLoad;

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
        this.groups = new LinkedList<>();
        this.firstLoad = true;
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
    public Optional<Schedule> getCourseByGroup(String group) {
        return Optional.ofNullable(courseByGroup.get(group.toLowerCase()));
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
    public Collection<Schedule> reloadCourseSchedule() {
        List<Schedule> updated = new LinkedList<>();

        Set<String> groups = new HashSet<>();

        for (CourseScheduleInfo info : config.getCourses()) {
            ScheduleHash oldHash = hashesDao.find(info.getId());
            String newHash = HashUtil.getHash(info.getUrl());

            if (firstLoad || oldHash == null || !oldHash.getHash().equals(newHash)) {
                for (Schedule schedule : coursesLoader.load(info)) {
                    coursesSchedule.put(schedule.getKey(), schedule);
                    updated.add(schedule);

                    if (schedule instanceof CourseSchedule course) {
                        for (String group : course.getGroupNames()) {
                            courseByGroup.put(group.toLowerCase(), schedule);
                            groups.add(group);
                        }
                    }

                    logger.info("Loaded schedule '{}'", schedule.getKey());
                }

                saveHash(info.getId(), newHash);
            }
        }

        if (!groups.isEmpty()) {
            this.groups = groups.stream()
                    .sorted(Comparator.naturalOrder())
                    .collect(Collectors.toList());
        }

        firstLoad = false;

        return updated;
    }

    @Override
    public boolean reloadTeacherSchedule() {
        ScheduleHash oldHash = hashesDao.find(config.getTeachers().getId());
        String newHash = HashUtil.getHash(config.getTeachers().getUrl());

        if (teachersSchedule == null || oldHash == null || !oldHash.getHash().equals(newHash)) {
            Optional<Schedule> opt = teachersLoader.loadSingle(config.getTeachers());

            if (opt.isPresent()) {
                teachersSchedule = opt.get();
                saveHash(config.getTeachers().getId(), newHash);
                logger.info("Loaded teachers schedule");
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean reloadConsultSchedule() {
        ScheduleHash oldHash = hashesDao.find(config.getConsult().getId());
        String newHash = HashUtil.getHash(config.getConsult().getUrl());

        if (consultSchedule == null || oldHash == null || !oldHash.getHash().equals(newHash)) {
            Optional<Schedule> opt = consultLoader.loadSingle(config.getConsult());

            if (opt.isPresent()) {
                consultSchedule = opt.get();
                saveHash(config.getConsult().getId(), newHash);
                logger.info("Loaded consultations schedule");
                return true;
            }
        }

        return false;
    }

    private void saveHash(String fileName, String hash) {
        hashesDao.save(new ScheduleHash(fileName, hash));
    }
}
