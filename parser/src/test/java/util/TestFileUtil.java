package util;

import edu.zieit.scheduler.util.FileUtil;
import org.junit.jupiter.api.Test;

public class TestFileUtil {

    @Test
    public void testFileName() {
        String fullName = "path/to/file.xls";
        String name = FileUtil.getName(fullName);
        assert name.equals("file");
    }

}
