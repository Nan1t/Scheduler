package edu.zieit.scheduler.services;

import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.render.SheetRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleLoader;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.api.util.HashUtil;
import edu.zieit.scheduler.config.ScheduleConfig;
import edu.zieit.scheduler.persistence.ScheduleHash;
import edu.zieit.scheduler.persistence.dao.ScheduleHashesDao;
import edu.zieit.scheduler.render.AsposeRenderer;
import edu.zieit.scheduler.schedule.consult.ConsultScheduleLoader;
import edu.zieit.scheduler.schedule.students.StudentScheduleInfo;
import edu.zieit.scheduler.schedule.students.StudentScheduleLoader;
import edu.zieit.scheduler.schedule.teacher.TeacherScheduleLoader;
import napi.configurate.yaml.lang.Language;

import java.util.*;

public class ScheduleServiceImpl implements ScheduleService {

    private final Language lang;
    private final ScheduleConfig config;
    private final ScheduleHashesDao hashesDao;

    private final ScheduleLoader studentsLoader;
    private final ScheduleLoader teachersLoader;
    private final ScheduleLoader consultLoader;

    private final Map<NamespacedKey, Schedule> studentsSchedule;
    private Schedule teachersSchedule;
    private Schedule consultSchedule;

    public ScheduleServiceImpl(Language lang, ScheduleConfig config, ScheduleHashesDao hashesDao) {
        this.lang = lang;
        this.config = config;
        this.hashesDao = hashesDao;

        SheetRenderer renderer = new AsposeRenderer(config.getRenderOptions());

        this.studentsLoader = new StudentScheduleLoader(renderer);
        this.teachersLoader = new TeacherScheduleLoader(renderer);
        this.consultLoader = new ConsultScheduleLoader(renderer);

        this.studentsSchedule = new HashMap<>();
    }

    @Override
    public Language getLang() {
        return lang;
    }

    @Override
    public Schedule getStudentSchedule(NamespacedKey key) {
        return studentsSchedule.get(key);
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

            if (studentsSchedule.isEmpty() || oldHash == null || !oldHash.getHash().equals(newHash)) {
                for (Schedule schedule : studentsLoader.load(info)) {
                    studentsSchedule.put(schedule.getKey(), schedule);
                    updated.add(schedule);
                }

                saveHash(info.getId(), newHash);
            }
        }

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
                return true;
            }
        }

        return false;
    }

    private void saveHash(String fileName, String hash) {
        hashesDao.save(new ScheduleHash(fileName, hash));
    }
}
