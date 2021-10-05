package parser;

import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import napi.configurate.yaml.lang.Language;

import java.util.Collection;
import java.util.Map;

public class TestScheduleService implements ScheduleService {

    private Language lang;
    private Map<NamespacedKey, Schedule> studentSchedule;
    private Schedule teacherSchedule;
    private Schedule consultSchedule;

    @Override
    public Language getLang() {
        return lang;
    }

    @Override
    public Schedule getStudentSchedule(NamespacedKey key) {
        return studentSchedule.get(key);
    }

    @Override
    public Schedule getTeacherSchedule() {
        return teacherSchedule;
    }

    @Override
    public Schedule getConsultSchedule() {
        return consultSchedule;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    public void setStudentSchedule(Map<NamespacedKey, Schedule> studentSchedule) {
        this.studentSchedule = studentSchedule;
    }

    public void setTeacherSchedule(Schedule teacherSchedule) {
        this.teacherSchedule = teacherSchedule;
    }

    public void setConsultSchedule(Schedule consultSchedule) {
        this.consultSchedule = consultSchedule;
    }

    @Override
    public Collection<Schedule> reloadStudentSchedule() {
        return null;
    }

    @Override
    public boolean reloadTeacherSchedule() {
        return false;
    }

    @Override
    public boolean reloadConsultSchedule() {
        return false;
    }
}
