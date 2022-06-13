package edu.zieit.scheduler.server.entity;

import java.util.Collection;
import java.util.Map;

public class BasicProperties {

    private long checkRate;
    private Collection<String> compAuds;
    private Map<String, Integer> dayIndexes;

    public long getCheckRate() {
        return checkRate;
    }

    public void setCheckRate(long checkRate) {
        this.checkRate = checkRate;
    }

    public Collection<String> getCompAuds() {
        return compAuds;
    }

    public void setCompAuds(Collection<String> compAuds) {
        this.compAuds = compAuds;
    }

    public Map<String, Integer> getDayIndexes() {
        return dayIndexes;
    }

    public void setDayIndexes(Map<String, Integer> dayIndexes) {
        this.dayIndexes = dayIndexes;
    }
}
