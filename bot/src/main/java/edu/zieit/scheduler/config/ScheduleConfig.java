package edu.zieit.scheduler.config;

import com.google.common.reflect.TypeToken;
import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.api.render.DocRenderOptions;
import edu.zieit.scheduler.schedule.consult.ConsultScheduleInfo;
import edu.zieit.scheduler.schedule.students.StudentScheduleInfo;
import edu.zieit.scheduler.schedule.teacher.TeacherScheduleInfo;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

public final class ScheduleConfig extends AbstractConfig {

    private long checkRate;
    private TeacherScheduleInfo teachers;
    private ConsultScheduleInfo consult;
    private Collection<StudentScheduleInfo> students;
    private DocRenderOptions renderOptions;

    public ScheduleConfig(Path rootDir) {
        super(rootDir, "/schedule.yml", Map.of(
                SheetPoint.class, new SheetPoint.Serializer(),
                TeacherScheduleInfo.class, new TeacherScheduleInfo.Serializer(),
                ConsultScheduleInfo.class, new ConsultScheduleInfo.Serializer(),
                StudentScheduleInfo.class, new StudentScheduleInfo.Serializer()
        ));
    }

    @Override
    protected void load() throws ObjectMappingException {
        checkRate = conf.getNode("check_rate").getInt();
        teachers = conf.getNode("teachers").getValue(TypeToken.of(TeacherScheduleInfo.class));
        consult = conf.getNode("consult").getValue(TypeToken.of(ConsultScheduleInfo.class));
        students = conf.getNode("students").getList(TypeToken.of(StudentScheduleInfo.class));

        DocRenderOptions.Format renderFormat = DocRenderOptions.Format
                .valueOf(conf.getNode("render", "format").getString("JPEG"));
        int renderDpi = conf.getNode("render", "dpi").getInt(300);

        renderOptions = new DocRenderOptions(renderFormat, renderDpi);
    }

    public long getCheckRate() {
        return checkRate;
    }

    public TeacherScheduleInfo getTeachers() {
        return teachers;
    }

    public ConsultScheduleInfo getConsult() {
        return consult;
    }

    public Collection<StudentScheduleInfo> getStudents() {
        return students;
    }

    public DocRenderOptions getRenderOptions() {
        return renderOptions;
    }
}
