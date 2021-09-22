package edu.zieit.scheduler.api.render;

public class RenderException extends RuntimeException {

    public RenderException(String message) {
        super(message);
    }

    public RenderException(Throwable cause) {
        super(cause);
    }
}
