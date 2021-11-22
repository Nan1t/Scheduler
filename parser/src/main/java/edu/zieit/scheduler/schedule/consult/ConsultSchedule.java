package edu.zieit.scheduler.schedule.consult;

import com.google.common.base.Preconditions;
import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.render.SheetRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.schedule.AbstractSchedule;
import edu.zieit.scheduler.schedule.AbstractScheduleBuilder;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;

public class ConsultSchedule extends AbstractSchedule {

    private final ConsultScheduleInfo info;
    private final String title;
    private final Map<Person, List<ConsultDay>> weekMap;

    public ConsultSchedule(ConsultScheduleInfo info, Sheet sheet, SheetRenderer renderer,
                           String title, Map<Person, List<ConsultDay>> weekMap) {
        super(info, sheet, renderer);
        this.info = info;
        this.title = title;
        this.weekMap = weekMap;
    }

    public String getTitle() {
        return title;
    }

    public Collection<ConsultDay> getWeek(Person teacher) {
        for (var entry : weekMap.entrySet()) {
            if (entry.getKey().isSimilar(teacher))
                return entry.getValue();
        }
        return Collections.emptyList();
        //return weekMap.getOrDefault(teacher, Collections.emptyList());
    }

    @Override
    public ScheduleRenderer getPersonalRenderer(Object data, ScheduleService manager) {
        return new ConsultScheduleRenderer(this, (Person) data, manager);
    }

    @Override
    public ConsultScheduleInfo getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "ConsultSchedule{" +
                "title='" + title + '\'' +
                ", weekMap=" + weekMap +
                '}';
    }

    public static Builder builder(Sheet sheet, SheetRenderer renderer) {
        return new Builder(sheet, renderer);
    }

    public static class Builder extends AbstractScheduleBuilder {

        private final Map<Person, List<ConsultDay>> weekMap;
        private ConsultScheduleInfo info;
        private String title;

        public Builder(Sheet sheet, SheetRenderer renderer) {
            super(sheet, renderer);
            this.weekMap = new HashMap<>();
        }

        public Builder info(ConsultScheduleInfo info) {
            this.info = info;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder withWeek(Person teacher, List<ConsultDay> week) {
            if (teacher != null)
                this.weekMap.put(teacher, week);
            return this;
        }

        public ConsultSchedule build() {
            Preconditions.checkNotNull(info);
            Preconditions.checkNotNull(title);
            return new ConsultSchedule(info, getSheet(), getRenderer(), title, weekMap);
        }

    }

}
