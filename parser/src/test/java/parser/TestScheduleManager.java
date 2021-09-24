package parser;

import edu.zieit.scheduler.api.NamespaceKey;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleManager;
import napi.configurate.yaml.lang.Language;

import java.util.Map;

public class TestScheduleManager implements ScheduleManager {

    private Language lang;
    private Map<NamespaceKey, Schedule> studentSchedule;
    private Schedule teacherSchedule;
    private Schedule consultSchedule;

    @Override
    public Language getLang() {
        return lang;
    }

    @Override
    public Schedule getStudentSchedule(NamespaceKey key) {
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

    public void setStudentSchedule(Map<NamespaceKey, Schedule> studentSchedule) {
        this.studentSchedule = studentSchedule;
    }

    public void setTeacherSchedule(Schedule teacherSchedule) {
        this.teacherSchedule = teacherSchedule;
    }

    public void setConsultSchedule(Schedule consultSchedule) {
        this.consultSchedule = consultSchedule;
    }
}
