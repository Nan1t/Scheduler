package edu.zieit.scheduler.api.schedule;

import edu.zieit.scheduler.api.NamespacedKey;
import napi.configurate.yaml.lang.Language;

import java.util.Collection;

public interface ScheduleService {

    Language getLang();

    /**
     * Get students schedule by key
     * @param key Unique schedule key
     * @return Schedule instance or null if not found
     */
    Schedule getStudentSchedule(NamespacedKey key);

    /**
     * Get teachers schedule
     * @return Teachers schedule instance
     */
    Schedule getTeacherSchedule();

    /**
     * Get consultations schedule
     * @return Consultations schedule instance
     */
    Schedule getConsultSchedule();

    /**
     * Reload and reparse all updated students schedules
     * @return Collection of schedules which has been reloaded
     */
    Collection<Schedule> reloadStudentSchedule();

    /**
     * Reload and reparse teachers schedule
     * @return true if schedule reloaded or false otherwise
     */
    boolean reloadTeacherSchedule();

    /**
     * Reload and reparse consultations schedule
     * @return true if schedule reloaded or false otherwise
     */
    boolean reloadConsultSchedule();

    /**
     * Reload all schedules
     */
    default void reloadAll() {
        reloadStudentSchedule();
        reloadTeacherSchedule();
        reloadConsultSchedule();
    }
}
