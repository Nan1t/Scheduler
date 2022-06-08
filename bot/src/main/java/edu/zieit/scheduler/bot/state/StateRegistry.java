package edu.zieit.scheduler.bot.state;

import edu.zieit.scheduler.bot.states.StateDenyAll;
import edu.zieit.scheduler.bot.states.StateHelp;
import edu.zieit.scheduler.bot.states.aud.StateAudList;
import edu.zieit.scheduler.bot.states.aud.StateAudShow;
import edu.zieit.scheduler.bot.states.consult.*;
import edu.zieit.scheduler.bot.states.course.StateCourse;
import edu.zieit.scheduler.bot.states.course.StateCourseDeny;
import edu.zieit.scheduler.bot.states.course.StateCourseList;
import edu.zieit.scheduler.bot.states.course.StateCourseShow;
import edu.zieit.scheduler.bot.states.group.StateGroup;
import edu.zieit.scheduler.bot.states.group.StateGroupDeny;
import edu.zieit.scheduler.bot.states.group.StateGroupList;
import edu.zieit.scheduler.bot.states.group.StateGroupShow;
import edu.zieit.scheduler.bot.states.points.StatePoints;
import edu.zieit.scheduler.bot.states.points.StatePointsDeny;
import edu.zieit.scheduler.bot.states.teacher.*;
import napi.configurate.yaml.lang.Language;

import java.util.HashMap;
import java.util.Map;

public final class StateRegistry {

    private final Language lang;
    private final Map<String, State> baseStates = new HashMap<>();

    private StateRegistry(Language lang) {
        this.lang = lang;
        init();
    }

    public State getBaseState(String cmd) {
        return baseStates.get(cmd.toLowerCase());
    }

    public void registerState(State state, String... aliases) {
        for (String alias : aliases) {
            baseStates.put(alias.toLowerCase(), state);
        }
    }

    private void init() {
        registerState(new StateHelp(lang), "help", "start");

        registerState(new StateTeacherList(lang, new StateTeacherShow(true)), "teachersub", "teachersubscribe");
        registerState(new StateTeacherList(lang, new StateTeacherShow(false)), "teachershow");
        registerState(new StateTeacherDeny(), "teacherdeny");
        registerState(new StateTeacher(), "teacher");
        //registerState(new StateToggleNotices(), "notices");

        registerState(new StateCourseList(lang, new StateCourseShow(true)), "coursesub", "studentsubscribe");
        registerState(new StateCourseList(lang, new StateCourseShow(false)), "courseshow");
        registerState(new StateCourseDeny(), "coursedeny");
        registerState(new StateCourse(), "course");

        registerState(new StateGroupList(lang, new StateGroupShow(true)), "groupsub");
        registerState(new StateGroupList(lang, new StateGroupShow(false)), "groupshow");
        registerState(new StateGroupDeny(), "groupdeny");
        registerState(new StateGroup(), "group");

        registerState(new StateConsultList(lang, new StateConsultShow(true)), "consultsub");
        registerState(new StateConsultList(lang, new StateConsultShow(false)), "consultshow");
        registerState(new StateConsultDeny(), "consultdeny");
        registerState(new StateConsultAll(), "consall");
        registerState(new StateConsult(), "consult");

        registerState(new StateAudList(lang, new StateAudShow()), "aud");

        registerState(new StateDenyAll(), "denyall");

        registerState(new StatePoints(), "points");
        registerState(new StatePointsDeny(), "pointsdeny");
    }
}
