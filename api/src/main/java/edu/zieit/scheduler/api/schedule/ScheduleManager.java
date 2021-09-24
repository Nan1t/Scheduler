package edu.zieit.scheduler.api.schedule;

import edu.zieit.scheduler.api.NamespaceKey;
import napi.configurate.yaml.lang.Language;

public interface ScheduleManager {

    Language getLang();

    Schedule getStudentSchedule(NamespaceKey key);

    Schedule getTeacherSchedule();

    Schedule getConsultSchedule();

}
