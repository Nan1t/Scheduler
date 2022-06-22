package edu.zieit.scheduler.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.api.config.AbstractConfig;
import edu.zieit.scheduler.api.render.DocRenderOptions;
import edu.zieit.scheduler.schedule.consult.ConsultScheduleInfo;
import edu.zieit.scheduler.schedule.course.CourseScheduleInfo;
import edu.zieit.scheduler.schedule.teacher.TeacherScheduleInfo;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import java.nio.file.Path;
import java.util.*;

public final class ScheduleConfig extends AbstractConfig {

    private long checkRate;
    private Collection<String> compAud;
    private Map<String, Integer> dayIndexes = new HashMap<>();
    private TeacherScheduleInfo teachers;
    private ConsultScheduleInfo consult;
    private Collection<CourseScheduleInfo> courses;
    private DocRenderOptions renderOptions;

    @Inject
    public ScheduleConfig(@Named("appDir") Path rootDir) {
        super(rootDir, "/schedule.yml");
    }

    @Override
    protected void load() throws SerializationException {
        checkRate = conf.node("check_rate").getLong();
        compAud = conf.node("comp_auds").getList(String.class);
        teachers = conf.node("teachers").get(TeacherScheduleInfo.class);
        consult = conf.node("consult").get(ConsultScheduleInfo.class);
        courses = conf.node("courses").getList(CourseScheduleInfo.class);

        DocRenderOptions.Format renderFormat = DocRenderOptions.Format
                .valueOf(conf.node("render", "format").getString("JPEG"));
        int renderDpi = conf.node("render", "dpi").getInt(150);

        renderOptions = new DocRenderOptions(renderFormat, renderDpi);

        for (var entry : conf.node("day_indexes").childrenMap().entrySet()) {
            String day = entry.getKey().toString();
            int index = entry.getValue().getInt();
            dayIndexes.put(day, index);
        }
    }

    @Override
    protected void serializers(TypeSerializerCollection.Builder build) {
        build.register(SheetPoint.class, new SheetPoint.Serializer());
        build.register(TeacherScheduleInfo.class, new TeacherScheduleInfo.Serializer());
        build.register(ConsultScheduleInfo.class, new ConsultScheduleInfo.Serializer());
        build.register(CourseScheduleInfo.class, new CourseScheduleInfo.Serializer());
    }

    public long getCheckRate() {
        return checkRate;
    }

    public Collection<String> getCompAud() {
        return compAud;
    }

    public Map<String, Integer> getDayIndexes() {
        return dayIndexes;
    }

    public TeacherScheduleInfo getTeachers() {
        return teachers;
    }

    public ConsultScheduleInfo getConsult() {
        return consult;
    }

    public Collection<CourseScheduleInfo> getCourses() {
        return courses;
    }

    public DocRenderOptions getRenderOptions() {
        return renderOptions;
    }

    public void setCheckRate(long checkRate) {
        this.checkRate = checkRate;

        try {
            conf.node("check_rate").set(checkRate);
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    public void setCompAud(Collection<String> compAud) {
        this.compAud = compAud;

        try {
            conf.node("comp_auds").set(compAud);
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    public void setDayIndexes(Map<String, Integer> dayIndexes) {
        this.dayIndexes = dayIndexes;

        try {
            conf.node("day_indexes").set(dayIndexes);
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    public void setTeachers(TeacherScheduleInfo teachers) {
        this.teachers = teachers;

        try {
            conf.node("teachers").set(teachers);
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    public void setConsult(ConsultScheduleInfo consult) {
        this.consult = consult;

        try {
            conf.node("consult").set(consult);
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    public void setCourses(Collection<CourseScheduleInfo> courses) {
        this.courses = courses;

        try {
            conf.node("courses").setList(CourseScheduleInfo.class, courses.stream().toList());
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    public void setRenderOptions(DocRenderOptions renderOptions) {
        this.renderOptions = renderOptions;

        try {
            conf.node("render", "format").set(renderOptions.format().toString());
            conf.node("render", "dpi").set(renderOptions.dpi());
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }
}
