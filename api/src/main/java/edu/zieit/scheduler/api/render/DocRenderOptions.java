package edu.zieit.scheduler.api.render;

public record DocRenderOptions(Format format, int dpi) {

    public static final DocRenderOptions DEFAULTS = new DocRenderOptions(Format.JPEG, 300);

    public enum Format {
        JPEG,
        PNG,
        GIF
    }

}
