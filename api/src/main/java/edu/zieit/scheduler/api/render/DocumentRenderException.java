package edu.zieit.scheduler.api.render;

public class DocumentRenderException extends RuntimeException {

    public DocumentRenderException(String message) {
        super(message);
    }

    public DocumentRenderException(Throwable cause) {
        super(cause);
    }
}
