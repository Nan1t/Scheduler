import edu.zieit.scheduler.api.Regexs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClassroomTest {

    @Test
    public void test() {
        String cr1 = "ауд.007";
        String cr2 = "ауд. 007";
        String cr3 = "ауд007";
        String cr4 = "007";

        Assertions.assertTrue(Regexs.CLASSROOM.matcher(cr1).matches());
        Assertions.assertTrue(Regexs.CLASSROOM.matcher(cr2).matches());
        Assertions.assertTrue(Regexs.CLASSROOM.matcher(cr3).matches());
        Assertions.assertTrue(Regexs.CLASSROOM.matcher(cr4).matches());
    }

}
