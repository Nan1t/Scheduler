package edu.zieit.scheduler.render;

import com.aspose.cells.*;
import edu.zieit.scheduler.api.render.RenderException;
import edu.zieit.scheduler.api.render.SheetRenderer;
import edu.zieit.scheduler.api.render.DocRenderOptions;
import org.apache.poi.ss.usermodel.Sheet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class AsposeRenderer extends SheetRenderer {

    private final ImageOrPrintOptions asposeOptions;

    public AsposeRenderer() {
        super();
        asposeOptions = convertOptions();
    }

    public AsposeRenderer(DocRenderOptions options) {
        super(options);
        asposeOptions = convertOptions();
    }

    @Override
    public BufferedImage[] render(org.apache.poi.ss.usermodel.Workbook book) throws RenderException {
        Workbook workbook = toAsposeWorkbook(book);
        var images = new BufferedImage[workbook.getWorksheets().getCount()];
        int i = 0;

        for (Object sheet : workbook.getWorksheets()) {
            Worksheet worksheet = (Worksheet) sheet;
            images[i] = render(worksheet);
            i++;
        }

        return images;
    }

    @Override
    public BufferedImage render(Sheet sheet) throws RenderException {
        Workbook workbook = toAsposeWorkbook(sheet.getWorkbook());
        Worksheet worksheet = workbook.getWorksheets().get(sheet.getWorkbook().getSheetIndex(sheet));
        return render(worksheet);
    }

    private BufferedImage render(Worksheet worksheet) throws RenderException {
        try {
            SheetRender render = new SheetRender(worksheet, asposeOptions);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            render.toImage(0, output);
            return ImageIO.read(new ByteArrayInputStream(output.toByteArray()));
        } catch (Exception e) {
            throw new RenderException(e);
        }
    }

    private Workbook toAsposeWorkbook(org.apache.poi.ss.usermodel.Workbook book) throws RenderException {
        var out = new ByteArrayOutputStream();

        try {
            book.write(out);
        } catch (IOException e) {
            throw new RenderException(e);
        }

        try {
            return new Workbook(new ByteArrayInputStream(out.toByteArray()));
        } catch (Exception e) {
            throw new RenderException(e);
        }
    }

    private ImageOrPrintOptions convertOptions() {
        DocRenderOptions renderOptions = getOptions();
        int format = switch (renderOptions.format()) {
            case JPEG -> ImageType.JPEG;
            case PNG -> ImageType.PNG;
            case GIF -> ImageType.GIF;
        };

        ImageOrPrintOptions asposeOptions = new ImageOrPrintOptions();
        asposeOptions.setHorizontalResolution(renderOptions.dpi());
        asposeOptions.setVerticalResolution(renderOptions.dpi());
        asposeOptions.setQuality(100);
        asposeOptions.setOnePagePerSheet(true);
        asposeOptions.setOutputBlankPageWhenNothingToPrint(true);
        asposeOptions.setImageType(format);
        asposeOptions.setCellAutoFit(true);

        return asposeOptions;
    }
}
