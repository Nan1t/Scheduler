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
    private final Map<String, Integer> dayIndexes = new HashMap<>();
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
        courses = new LinkedList<>();

        for (var elem : conf.node("courses").childrenList()) {
            courses.add(elem.get(CourseScheduleInfo.class));
        }

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
    protected TypeSerializerCollection serializers() {
        return TypeSerializerCollection.builder()
                .register(SheetPoint.class, new SheetPoint.Serializer())
                .register(TeacherScheduleInfo.class, new TeacherScheduleInfo.Serializer())
                .register(ConsultScheduleInfo.class, new ConsultScheduleInfo.Serializer())
                .register(CourseScheduleInfo.class, new CourseScheduleInfo.Serializer())
                .build();
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
    }

    public void setCompAud(Collection<String> compAud) {
        this.compAud = compAud;
    }

    public void setTeachers(TeacherScheduleInfo teachers) {
        this.teachers = teachers;
    }

    public void setConsult(ConsultScheduleInfo consult) {
        this.consult = consult;
    }

    public void setCourses(Collection<CourseScheduleInfo> courses) {
        this.courses = courses;
    }

    public void setRenderOptions(DocRenderOptions renderOptions) {
        this.renderOptions = renderOptions;
    }
}
