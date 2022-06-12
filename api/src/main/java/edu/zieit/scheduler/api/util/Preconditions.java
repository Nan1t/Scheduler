package edu.zieit.scheduler.api.util;

public final class Preconditions {

    private Preconditions() {}

    public static void checkNotNull(Object obj) {
        checkNotNull(obj, "Argument is null");
    }

    public static void checkNotNull(Object obj, String msg) {
        if (obj == null) {
            throw new IllegalArgumentException(msg);
        }
    }

}
