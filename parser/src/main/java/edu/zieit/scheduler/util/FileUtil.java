package edu.zieit.scheduler.util;

public final class FileUtil {

    private FileUtil() { }

    public static String getName(String fullName) {
        return getName(fullName, "/");
    }

    /**
     * Get file name without extension
     * @param fullName Full name of the file
     * @param separator Path separator
     * @return File name without extension
     */
    public static String getName(String fullName, String separator) {
        String[] arr = fullName.split(separator);
        String name = arr[arr.length - 1];
        return name.substring(0, name.indexOf('.'));
    }

}
