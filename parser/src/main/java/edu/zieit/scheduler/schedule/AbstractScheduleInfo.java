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
        this(url, FileUtil.getName(url.toString()).toLowerCase());
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractScheduleInfo) {
            AbstractScheduleInfo info = (AbstractScheduleInfo) obj;
            return this.url.equals(info.url) && this.id.equals(info.id);
        }
        return false;
    }
}
