import edu.zieit.scheduler.api.render.DocumentRenderer;
import edu.zieit.scheduler.render.AwtRenderer;
import edu.zieit.scheduler.util.SizeUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestRenderer {

    @Test
    public void testRender() throws IOException  {
        Path file = Paths.get("C:\\Users\\nanit\\Downloads\\Financial Sample.xlsx");
        Path imgFile = Paths.get("E:\\IdeaProjects\\Scheduler\\build\\test.jpg");
        Workbook workbook = new XSSFWorkbook(Files.newInputStream(file));
        Sheet sheet = workbook.getSheetAt(0);
        DocumentRenderer renderer = new AwtRenderer();

        try {
            BufferedImage img = renderer.render(sheet);
            Files.deleteIfExists(imgFile);
            Files.createFile(imgFile);
            ImageIO.write(img, "jpg", imgFile.toFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSizes() {
        for (int pt = 6; pt < 23; pt++) {
            System.out.println("Pt: " + pt +"; Px: " + SizeUtil.pointsToPixels(pt));
        }
    }

}
