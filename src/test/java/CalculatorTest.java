
import org.junit.Test;
import static org.junit.Assert.*;

public class CalculatorTest {
    @Test public void testSomeLibraryMethod() {
        Calculator c = new Calculator();
     

        assertEquals((Integer) 2, c.calculate("2"));
        assertEquals((Integer) 5, c.calculate("let(a, 5, a)"));
        assertEquals((Integer) 3, c.calculate("add(1, 2"));
        
        assertEquals((Integer) 3, c.calculate("add(1, 2)"));
        assertEquals((Integer) 7, c.calculate("add(1, mult(2, 3))"));
        assertEquals((Integer) 12, c.calculate("mult(add(2, 2), div(9, 3))"));
        assertEquals((Integer) 10, c.calculate("let(a, 5, add(a, a))"));
        assertEquals((Integer) 55, c.calculate("let(a, 5, let(b, mult(a, 10), add(b, a)))"));
        assertEquals((Integer) 40, c.calculate("let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))"));
        
    }
}
