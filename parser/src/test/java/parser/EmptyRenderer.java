package parser;

import edu.zieit.scheduler.api.render.RenderException;
import edu.zieit.scheduler.api.render.SheetRenderer;
import org.apache.poi.ss.usermodel.Sheet;

import java.awt.image.BufferedImage;

public class EmptyRenderer extends SheetRenderer {
    
    @Override
    public BufferedImage render(Sheet sheet) throws RenderException {
        return null;
    }
}