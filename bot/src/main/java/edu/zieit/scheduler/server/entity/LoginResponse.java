package edu.zieit.scheduler.server.entity;

public record LoginResponse(String accessToken, boolean isAdmin) {
}
