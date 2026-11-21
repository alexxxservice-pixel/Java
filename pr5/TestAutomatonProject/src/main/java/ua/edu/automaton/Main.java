package ua.edu.automaton;

/**
 * Демонстрація роботи скінченного автомата для розпізнавання слова "TEST".
 */
public class Main {
    public static void main(String[] args) {
        TestAutomaton naiveAutomaton = new TestAutomaton(false);
        TestAutomaton fixedAutomaton = new TestAutomaton(true);

        String[] standardExamples = {
                "abcTESTabc",
                "abcTES",
                "abcTE",
                "abcT",
                "abcdef",
                ""
        };

        System.out.println("=== 1. Базові приклади роботи автомата ===");
        for (String input : standardExamples) {
            System.out.printf("Рядок: %-14s -> Стан: %s%n", "\"" + input + "\"", fixedAutomaton.process(input));
        }

        String[] overlappingExamples = {
                "TTEST",
                "TTTTEST",
                "TETEST",
                "TTETTEST",
                "TETTTEST",
                "abcTTESTxyz",
                "abcTETESTxyz"
        };

        System.out.println("\n=== 3*. Рядки зі словом \"TEST\", на яких базова схема помиляється ===");
        System.out.printf("%-16s | %-20s | %-20s%n", "Вхідний рядок", "Базовий автомат (1)", "Виправлений (3*)");
        System.out.println("-----------------------------------------------------------------");
        for (String input : overlappingExamples) {
            String naiveResult = naiveAutomaton.process(input);
            String fixedResult = fixedAutomaton.process(input);
            System.out.printf("%-16s | %-20s | %-20s%n",
                    "\"" + input + "\"", naiveResult, fixedResult);
        }
    }
}
