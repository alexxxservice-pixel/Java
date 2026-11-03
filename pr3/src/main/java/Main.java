import java.util.Scanner;

/**
 * Головний клас для взаємодії з користувачем.
 */
public class Main {

    public static double parseInput(String input) throws InvalidInputException {
        try {
            return Double.parseDouble(input.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Помилка вводу: '" + input + "' не є числом.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Calculator calculator = new Calculator();

        System.out.println("=== Калькулятор ===");
        System.out.println("Доступні операції: +, -, *, /, sqrt (квадратний корінь)");

        try {
            System.out.print("Введіть перше число: ");
            double num1 = parseInput(scanner.nextLine());

            System.out.print("Введіть операцію (+, -, *, /, sqrt): ");
            String operation = scanner.nextLine().trim();

            double result;
            if (operation.equalsIgnoreCase("sqrt")) {
                result = calculator.sqrt(num1);
                System.out.println("Результат: sqrt(" + num1 + ") = " + result);
            } else {
                System.out.print("Введіть друге число: ");
                double num2 = parseInput(scanner.nextLine());

                switch (operation) {
                    case "+":
                        result = calculator.add(num1, num2);
                        break;
                    case "-":
                        result = calculator.subtract(num1, num2);
                        break;
                    case "*":
                        result = calculator.multiply(num1, num2);
                        break;
                    case "/":
                        result = calculator.divide(num1, num2);
                        break;
                    default:
                        throw new InvalidInputException("Невідома операція: '" + operation + "'. Доступні: +, -, *, /, sqrt.");
                }
                System.out.println("Результат: " + num1 + " " + operation + " " + num2 + " = " + result);
            }

        } catch (ArithmeticException e) {
            System.out.println("Арифметична помилка: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.out.println("Помилка вхідних даних: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Непередбачена помилка: " + e.getMessage());
        } finally {
            System.out.println("Завершення обробки запиту.");
            scanner.close();
        }
    }
}
