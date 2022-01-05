import edu.zieit.scheduler.api.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PersonTest {

    @Test
    public void testTeacher() {
        String t1 = "Прізвище І.Б.";
        String t2 = "Прізвище І. Б.";
        String t3 = "Прізвище І.Б";
        String t4 = "Прізвище І. Б";
        String t5 = "ПрізвищеІ.Б.";
        String t6 = "ПрізвищеІ. Б.";
        String t7 = "ПрізвищеІ. Б";

        Assertions.assertNotEquals(null, Person.teacher(t1));
        Assertions.assertNotEquals(null, Person.teacher(t2));
        Assertions.assertNotEquals(null, Person.teacher(t3));
        Assertions.assertNotEquals(null, Person.teacher(t4));
        Assertions.assertNotEquals(null, Person.teacher(t5));
        Assertions.assertNotEquals(null, Person.teacher(t6));
        Assertions.assertNotEquals(null, Person.teacher(t7));
    }

}
