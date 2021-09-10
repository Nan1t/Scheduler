package edu.zieit.scheduler.api.render;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.awt.image.BufferedImage;

/**
 * Interface for renderers
 * Renderers able to get document as input and render it to image.
 */
public abstract class DocumentRenderer {

    private final RenderOptions options;

    public DocumentRenderer() {
        this.options = RenderOptions.DEFAULTS;
    }

    public DocumentRenderer(RenderOptions options) {
        this.options = options;
    }

    public RenderOptions getOptions() {
        return options;
    }

    /**
     * Render every sheet of workbook
     * @param book Workbook object
     * @return Array of rendered sheets.
     * Array element index equivalents sheet number. Counting from zero
     */
    public BufferedImage[] render(final Workbook book) throws DocumentRenderException {
        BufferedImage[] images = new BufferedImage[book.getNumberOfSheets()];
        int index = 0;

        for (Sheet sheet : book) {
            images[index] = render(sheet);
            index++;
        }

        return images;
    }

    /**
     * Render single sheet of workbook
     * @param sheet Sheet object
     * @return Rendered image
     */
    public abstract BufferedImage render(final Sheet sheet) throws DocumentRenderException;

}
