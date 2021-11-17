package edu.zieit.scheduler.util;

import edu.zieit.scheduler.api.schedule.ScheduleService;

public final class FilenameUtil {

    private FilenameUtil() {}

    /**
     * Get filename based on current renderer's format
     * @param service Service to get render options
     * @param name Base file name without extension
     * @return File name with extension
     */
    public static String getNameWithExt(ScheduleService service, String name) {
        return name + "." + service.renderer().getOptions()
                .format()
                .toString()
                .toLowerCase();
    }

}
