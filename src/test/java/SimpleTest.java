import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleTest {

    @Test
    public void testAddition() {
        int result = 2 + 3;
        // Sprawdź, czy wynik dodawania dwóch liczb jest równy 5
        assertEquals(5, result, "Expected result should be 5");
    }
}