package edu.zieit.scheduler.api.schedule;

import java.net.URL;

/**
 * Schedule basic info
 */
public class ScheduleInfo {

    private final URL url;
    private final int sheetIndex;

    public ScheduleInfo(URL url, int sheetIndex) {
        this.url = url;
        this.sheetIndex = sheetIndex;
    }

    /**
     * Get schedule url
     * @return Schedule url
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Get sheet index of workbook
     * @return Workbook's sheet index
     */
    public int getSheetIndex() {
        return sheetIndex;
    }
}
