/**
 * Автономний клас для тестування функціональності Calculator та обробки виключень
 * (можна запускати через метод main без сторонніх бібліотек).
 */
public class CalculatorTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        Calculator calc = new Calculator();

        System.out.println("Запуск тестів Calculator...\n");

        // 1. Тести базових арифметичних операцій
        testBasicOperations(calc);

        // 2. Тест ділення на нуль (ArithmeticException)
        testDivisionByZero(calc);

        // 3. Тест невірних вхідних даних (рядки замість чисел)
        testInvalidStringInput();

        // 4. Тести випадків, що викликають власне виключення (InvalidInputException)
        testCustomExceptionSqrtNegative(calc);
        testCustomExceptionNaN(calc);

        System.out.println("\n=== Підсумок тестування ===");
        System.out.println("Успішно: " + passed + ", Помилок: " + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void testBasicOperations(Calculator calc) {
        try {
            checkEquals(calc.add(10.5, 4.5), 15.0, "Помилка додавання");
            checkEquals(calc.subtract(10.5, 4.5), 6.0, "Помилка віднімання");
            checkEquals(calc.multiply(3.0, 4.0), 12.0, "Помилка множення");
            checkEquals(calc.divide(15.0, 3.0), 5.0, "Помилка ділення");
            checkEquals(calc.sqrt(16.0), 4.0, "Помилка sqrt");
            pass("Базові арифметичні операції (+, -, *, /, sqrt)");
        } catch (Throwable e) {
            fail("Базові арифметичні операції", e.getMessage());
        }
    }

    private static void testDivisionByZero(Calculator calc) {
        try {
            calc.divide(10.0, 0.0);
            fail("Ділення на нуль", "Очікувалось виключення ArithmeticException, але воно не було кинуте.");
        } catch (ArithmeticException e) {
            pass("Ділення на нуль викликає ArithmeticException (" + e.getMessage() + ")");
        } catch (Exception e) {
            fail("Ділення на нуль", "Отримано інше виключення: " + e);
        }
    }

    private static void testInvalidStringInput() {
        try {
            Main.parseInput("abc123");
            fail("Невірні вхідні дані (рядок замість числа)", "Очікувалось виключення InvalidInputException.");
        } catch (InvalidInputException e) {
            pass("Невірні вхідні дані (рядок замість числа) викликають InvalidInputException (" + e.getMessage() + ")");
        } catch (Exception e) {
            fail("Невірні вхідні дані (рядок замість числа)", "Отримано інше виключення: " + e);
        }
    }

    private static void testCustomExceptionSqrtNegative(Calculator calc) {
        try {
            calc.sqrt(-25.0);
            fail("Квадратний корінь з від'ємного числа", "Очікувалось виключення InvalidInputException.");
        } catch (InvalidInputException e) {
            pass("Квадратний корінь з від'ємного числа викликає InvalidInputException (" + e.getMessage() + ")");
        } catch (Exception e) {
            fail("Квадратний корінь з від'ємного числа", "Отримано інше виключення: " + e);
        }
    }

    private static void testCustomExceptionNaN(Calculator calc) {
        try {
            calc.add(Double.NaN, 5.0);
            fail("Передача NaN у метод калькулятора", "Очікувалось виключення InvalidInputException.");
        } catch (InvalidInputException e) {
            pass("Передача некоректного значення (NaN) викликає InvalidInputException (" + e.getMessage() + ")");
        } catch (Exception e) {
            fail("Передача NaN у метод калькулятора", "Отримано інше виключення: " + e);
        }
    }

    private static void checkEquals(double actual, double expected, String message) {
        if (Math.abs(actual - expected) >= 1e-9) {
            throw new RuntimeException(message + ": очікувалось " + expected + ", отримано " + actual);
        }
    }

    private static void pass(String testName) {
        passed++;
        System.out.println("[PASS] " + testName);
    }

    private static void fail(String testName, String reason) {
        failed++;
        System.out.println("[FAIL] " + testName + " -> " + reason);
    }
}
