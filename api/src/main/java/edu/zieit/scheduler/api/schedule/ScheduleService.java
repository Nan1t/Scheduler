package edu.zieit.scheduler.api.schedule;

import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.render.SheetRenderer;
import napi.configurate.yaml.lang.Language;

import java.util.Collection;
import java.util.Optional;

public interface ScheduleService {

    Language getLang();

    /**
     * Get renderer of workbook's sheet
     * @return Current renderer
     */
    SheetRenderer renderer();

    /**
     * Get all loaded courses schedule
     * @return Collection of schedule
     */
    Collection<Schedule> getCoursesSchedule();

    /**
     * Get students schedule by key
     * @param key Unique schedule key
     * @return Schedule instance or null if not found
     */
    Schedule getCourseSchedule(NamespacedKey key);

    /**
     * Get collection of all groups parsed from courses schedule
     * @return Collections of group names
     */
    Collection<String> getGroups();

    /**
     * Get course schedule by group name
     * @param group Group name
     * @return Found schedule or Optional.EMPTY if not found
     */
    Optional<Schedule> getCourseByGroup(String group);

    /**
     * Get collection of all classrooms parsed from courses schedule
     * @return Collection of classrooms
     */
    Collection<String> getClassrooms();

    /**
     * Get classroom schedule
     * @param classroom Classroom name
     * @return Schedule or Optional.EMPTY if not found
     */
    Optional<Schedule> getClassroomSchedule(String classroom);

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
     * @param initial Is this initial (first loading).
     *                On first load hashes equality will be ignored.
     * @return Collection of schedules which has been reloaded
     */
    Collection<Schedule> reloadCourseSchedule(boolean initial);

    /**
     * Reload and reparse teachers schedule
     * @param initial Is this initial (first loading).
     *                On first load hashes equality will be ignored.
     * @return true if schedule reloaded or false otherwise
     */
    boolean reloadTeacherSchedule(boolean initial);

    /**
     * Reload and reparse consultations schedule
     * @param initial Is this initial (first loading).
     *                On first load hashes equality will be ignored.
     * @return true if schedule reloaded or false otherwise
     */
    boolean reloadConsultSchedule(boolean initial);

    /**
     * Reload all schedules
     */
    default void reloadAll() {
        reloadCourseSchedule(true);
        reloadTeacherSchedule(true);
        reloadConsultSchedule(true);
    }
}
