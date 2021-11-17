package edu.zieit.scheduler.api.render;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * Abstract class for renderers
 * Renderers able to get document as input and render it to image.
 */
public abstract class SheetRenderer {

    private final DocRenderOptions options;

    public SheetRenderer() {
        this.options = DocRenderOptions.DEFAULTS;
    }

    public SheetRenderer(DocRenderOptions options) {
        this.options = options;
    }

    public DocRenderOptions getOptions() {
        return options;
    }

    /**
     * Render every sheet of workbook
     * @param book Workbook object
     * @return Array of rendered sheets.
     * Array element index equivalents sheet number. Counting from zero
     */
    public BufferedImage[] render(final Workbook book) throws RenderException {
        var images = new BufferedImage[book.getNumberOfSheets()];
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
    public abstract BufferedImage render(final Sheet sheet) throws RenderException;

    /**
     * Render single sheet of workbook and return as input stream
     * @param sheet Sheet object
     * @return Rendered image
     */
    public abstract InputStream renderStream(final Sheet sheet) throws RenderException;

}
