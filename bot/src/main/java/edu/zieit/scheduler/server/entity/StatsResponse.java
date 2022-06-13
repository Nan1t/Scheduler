package edu.zieit.scheduler.server.entity;

public record StatsResponse(
        long users,
        long subsTeacher,
        long subsConsult,
        long subsCourse,
        long subsGroup,
        long subsPoints
) { }