package edu.zieit.scheduler.render;

import com.aspose.cells.*;
import edu.zieit.scheduler.api.render.DocumentRenderException;
import edu.zieit.scheduler.api.render.DocumentRenderer;
import edu.zieit.scheduler.api.render.RenderOptions;
import org.apache.poi.ss.usermodel.Sheet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class AsposeRenderer extends DocumentRenderer {

    private final ImageOrPrintOptions asposeOptions;

    public AsposeRenderer() {
        super();
        asposeOptions = convertOptions();
    }

    public AsposeRenderer(RenderOptions options) {
        super(options);
        asposeOptions = convertOptions();
    }

    @Override
    public BufferedImage[] render(org.apache.poi.ss.usermodel.Workbook book) throws DocumentRenderException {
        Workbook workbook = toAsposeWorkbook(book);
        BufferedImage[] images = new BufferedImage[workbook.getWorksheets().getCount()];
        int i = 0;

        for (Object sheet : workbook.getWorksheets()) {
            Worksheet worksheet = (Worksheet) sheet;
            images[i] = render(worksheet);
            i++;
        }

        return images;
    }

    @Override
    public BufferedImage render(Sheet sheet) throws DocumentRenderException {
        Workbook workbook = toAsposeWorkbook(sheet.getWorkbook());
        Worksheet worksheet = workbook.getWorksheets().get(sheet.getWorkbook().getSheetIndex(sheet));
        return render(worksheet);
    }

    private BufferedImage render(Worksheet worksheet) throws DocumentRenderException {
        try {
            SheetRender render = new SheetRender(worksheet, asposeOptions);
            float[] size = render.getPageSize(0);
            BufferedImage image = new BufferedImage((int) size[0], (int) size[1], BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            render.toImage(0, graphics);
            return image;
        } catch (Exception e) {
            throw new DocumentRenderException(e);
        }
    }

    private Workbook toAsposeWorkbook(org.apache.poi.ss.usermodel.Workbook book) throws DocumentRenderException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            book.write(out);
        } catch (IOException e) {
            throw new DocumentRenderException(e);
        }

        try {
            return new Workbook(new ByteArrayInputStream(out.toByteArray()));
        } catch (Exception e) {
            throw new DocumentRenderException(e);
        }
    }

    private ImageOrPrintOptions convertOptions() {
        RenderOptions renderOptions = getOptions();
        int format = ImageType.UNKNOWN;

        switch (renderOptions.getFormat()) {
            case JPEG:
                format = ImageType.JPEG;
                break;
            case PNG:
                format = ImageType.PNG;
                break;
            case GIF:
                format = ImageType.GIF;
                break;
        }

        ImageOrPrintOptions asposeOptions = new ImageOrPrintOptions();
        asposeOptions.setHorizontalResolution(renderOptions.getDpi());
        asposeOptions.setVerticalResolution(renderOptions.getDpi());
        asposeOptions.setQuality(100);
        asposeOptions.setOnePagePerSheet(true);
        asposeOptions.setOutputBlankPageWhenNothingToPrint(true);
        asposeOptions.setImageType(format);

        return asposeOptions;
    }
}