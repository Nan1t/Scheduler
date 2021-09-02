package edu.zieit.scheduler.schedule;

import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.util.FileUtil;

import java.net.URL;

public abstract class AbstractScheduleInfo implements ScheduleInfo {

    private final URL url;
    private final String id;

    public AbstractScheduleInfo(URL url, String id) {
        this.url = url;
        this.id = id;
    }

    public AbstractScheduleInfo(URL url) {
        this(url, FileUtil.getName(url.toString()));
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public String getId() {
        return id;
    }
}
