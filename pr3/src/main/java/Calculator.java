/**
 * Клас Calculator для виконання базових арифметичних операцій та обчислення квадратного кореня.
 */
public class Calculator {

    /**
     * Перевіряє коректність вхідних чисел (чи не є вони NaN або нескінченністю).
     */
    private void validateNumbers(double... numbers) throws InvalidInputException {
        for (double num : numbers) {
            if (Double.isNaN(num) || Double.isInfinite(num)) {
                throw new InvalidInputException("Помилка: вхідне значення не є коректним скінченним числом.");
            }
        }
    }

    public double add(double a, double b) throws InvalidInputException {
        validateNumbers(a, b);
        return a + b;
    }

    public double subtract(double a, double b) throws InvalidInputException {
        validateNumbers(a, b);
        return a - b;
    }

    public double multiply(double a, double b) throws InvalidInputException {
        validateNumbers(a, b);
        return a * b;
    }

    public double divide(double a, double b) throws InvalidInputException, ArithmeticException {
        validateNumbers(a, b);
        if (b == 0.0) {
            throw new ArithmeticException("Помилка: ділення на нуль неможливе!");
        }
        return a / b;
    }

    public double sqrt(double a) throws InvalidInputException {
        validateNumbers(a);
        if (a < 0) {
            throw new InvalidInputException("Помилка: неможливо обчислити квадратний корінь з від'ємного числа (" + a + ").");
        }
        return Math.sqrt(a);
    }
}
