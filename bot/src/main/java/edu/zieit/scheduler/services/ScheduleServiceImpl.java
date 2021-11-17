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
import edu.zieit.scheduler.schedule.students.StudentScheduleInfo;
import edu.zieit.scheduler.schedule.students.StudentScheduleLoader;
import edu.zieit.scheduler.schedule.teacher.TeacherScheduleLoader;
import napi.configurate.yaml.lang.Language;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public final class ScheduleServiceImpl implements ScheduleService {

    private static final Logger logger = LogManager.getLogger(ScheduleServiceImpl.class);

    private final Language lang;
    private final ScheduleConfig config;
    private final HashesDao hashesDao;

    private final SheetRenderer renderer;

    private final ScheduleLoader studentsLoader;
    private final ScheduleLoader teachersLoader;
    private final ScheduleLoader consultLoader;

    private final Map<NamespacedKey, Schedule> studentsSchedule;
    private Schedule teachersSchedule;
    private Schedule consultSchedule;

    private boolean firstLoad;

    public ScheduleServiceImpl(Language lang, ScheduleConfig config, HashesDao hashesDao) {
        this.lang = lang;
        this.config = config;
        this.hashesDao = hashesDao;

        this.renderer = new AsposeRenderer(config.getRenderOptions());

        this.studentsLoader = new StudentScheduleLoader(renderer);
        this.teachersLoader = new TeacherScheduleLoader(renderer);
        this.consultLoader = new ConsultScheduleLoader(renderer);

        this.studentsSchedule = new HashMap<>();
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
    public Schedule getStudentSchedule(NamespacedKey key) {
        return studentsSchedule.get(key);
    }

    @Override
    public Collection<Schedule> getStudentsSchedule() {
        return Collections.unmodifiableCollection(studentsSchedule.values());
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
    public Collection<Schedule> reloadStudentSchedule() {
        List<Schedule> updated = new LinkedList<>();

        for (StudentScheduleInfo info : config.getStudents()) {
            ScheduleHash oldHash = hashesDao.find(info.getId());
            String newHash = HashUtil.getHash(info.getUrl());

            if (firstLoad || oldHash == null || !oldHash.getHash().equals(newHash)) {
                for (Schedule schedule : studentsLoader.load(info)) {
                    studentsSchedule.put(schedule.getKey(), schedule);
                    updated.add(schedule);
                    logger.info("Loaded schedule '{}'", schedule.getKey());
                }

                saveHash(info.getId(), newHash);
                continue;
            }

            logger.info("Skipping schedule {}", info.getId());
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
