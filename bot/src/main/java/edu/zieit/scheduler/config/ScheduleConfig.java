package edu.zieit.scheduler.config;

import com.google.common.reflect.TypeToken;
import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.api.render.DocRenderOptions;
import edu.zieit.scheduler.schedule.consult.ConsultScheduleInfo;
import edu.zieit.scheduler.schedule.course.CourseScheduleInfo;
import edu.zieit.scheduler.schedule.teacher.TeacherScheduleInfo;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class ScheduleConfig extends AbstractConfig {

    private long checkRate;
    private Collection<String> compAuds;
    private final Map<String, Integer> dayIndexes = new HashMap<>();
    private TeacherScheduleInfo teachers;
    private ConsultScheduleInfo consult;
    private Collection<CourseScheduleInfo> courses;
    private DocRenderOptions renderOptions;

    public ScheduleConfig(Path rootDir) {
        super(rootDir, "/schedule.yml", Map.of(
                SheetPoint.class, new SheetPoint.Serializer(),
                TeacherScheduleInfo.class, new TeacherScheduleInfo.Serializer(),
                ConsultScheduleInfo.class, new ConsultScheduleInfo.Serializer(),
                CourseScheduleInfo.class, new CourseScheduleInfo.Serializer()
        ));
    }

    @Override
    protected void load() throws ObjectMappingException {
        checkRate = conf.getNode("check_rate").getLong();
        compAuds = conf.getNode("comp_auds").getList(TypeToken.of(String.class));
        teachers = conf.getNode("teachers").getValue(TypeToken.of(TeacherScheduleInfo.class));
        consult = conf.getNode("consult").getValue(TypeToken.of(ConsultScheduleInfo.class));
        courses = conf.getNode("courses").getList(TypeToken.of(CourseScheduleInfo.class));

        DocRenderOptions.Format renderFormat = DocRenderOptions.Format
                .valueOf(conf.getNode("render", "format").getString("JPEG"));
        int renderDpi = conf.getNode("render", "dpi").getInt(150);

        renderOptions = new DocRenderOptions(renderFormat, renderDpi);

        for (var entry : conf.getNode("day_indexes").getChildrenMap().entrySet()) {
            String day = entry.getKey().toString();
            int index = entry.getValue().getInt();
            dayIndexes.put(day, index);
        }
    }

    public long getCheckRate() {
        return checkRate;
    }

    public Collection<String> getCompAuds() {
        return compAuds;
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
}
