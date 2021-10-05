package edu.zieit.scheduler.api.schedule;

import edu.zieit.scheduler.api.NamespacedKey;
import napi.configurate.yaml.lang.Language;

public interface ScheduleManager {

    Language getLang();

    Schedule getStudentSchedule(NamespacedKey key);

    Schedule getTeacherSchedule();

    Schedule getConsultSchedule();

}
