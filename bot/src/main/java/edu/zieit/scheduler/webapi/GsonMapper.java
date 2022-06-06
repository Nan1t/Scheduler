package edu.zieit.scheduler.webapi;

import com.google.gson.Gson;
import io.javalin.plugin.json.JsonMapper;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class GsonMapper implements JsonMapper {

    private static final Gson gson = new Gson();

    @NotNull
    @Override
    public String toJsonString(@NotNull Object obj) {
        return gson.toJson(obj);
    }

    @NotNull
    @Override
    public InputStream toJsonStream(@NotNull Object obj) {
        byte[] bytes = gson.toJson(obj).getBytes(StandardCharsets.UTF_8);
        return new ByteArrayInputStream(bytes);
    }

    @NotNull
    @Override
    public <T> T fromJsonString(@NotNull String json, @NotNull Class<T> targetClass) {
        return gson.fromJson(json, targetClass);
    }

    @NotNull
    @Override
    public <T> T fromJsonStream(@NotNull InputStream json, @NotNull Class<T> targetClass) {
        return gson.fromJson(new InputStreamReader(json), targetClass);
    }
}
