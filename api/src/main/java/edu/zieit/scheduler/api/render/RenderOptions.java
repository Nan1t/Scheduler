package edu.zieit.scheduler.api.render;

public class RenderOptions {

    public static final RenderOptions DEFAULTS = new RenderOptions(Format.JPEG, 300);

    private final Format format;
    private final int dpi;

    public RenderOptions(Format format, int dpi) {
        this.format = format;
        this.dpi = dpi;
    }

    public Format getFormat() {
        return format;
    }

    public int getDpi() {
        return dpi;
    }

    public enum Format {
        JPEG,
        PNG,
        GIF
    }

}
