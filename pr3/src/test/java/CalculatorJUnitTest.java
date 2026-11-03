import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульні тести JUnit 5 для класу Calculator.
 */
class CalculatorJUnitTest {

    private final Calculator calculator = new Calculator();

    @Test
    void testBasicOperations() throws InvalidInputException {
        assertEquals(15.0, calculator.add(10.5, 4.5), 1e-9);
        assertEquals(6.0, calculator.subtract(10.5, 4.5), 1e-9);
        assertEquals(12.0, calculator.multiply(3.0, 4.0), 1e-9);
        assertEquals(5.0, calculator.divide(15.0, 3.0), 1e-9);
        assertEquals(4.0, calculator.sqrt(16.0), 1e-9);
    }

    @Test
    void testDivisionByZeroThrowsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(10.0, 0.0));
    }

    @Test
    void testInvalidStringInputThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> Main.parseInput("рядок_замість_числа"));
    }

    @Test
    void testNegativeSqrtThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> calculator.sqrt(-9.0));
    }

    @Test
    void testNaNInputThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> calculator.add(Double.NaN, 1.0));
    }
}
