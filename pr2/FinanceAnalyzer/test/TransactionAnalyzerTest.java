import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class TransactionAnalyzerTest {

    // ---------- Задача 2.2 ----------
    @Test
    public void testCalculateTotalBalance() {
        // Створення тестових даних
        Transaction transaction1 = new Transaction("2023-01-01", 100.0, "Дохід");
        Transaction transaction2 = new Transaction("2023-01-02", -50.0, "Витрата");
        Transaction transaction3 = new Transaction("2023-01-03", 150.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);

        // Створення екземпляру TransactionAnalyzer з тестовими даними
        TransactionAnalyzer analyzer = new TransactionAnalyzer(transactions);

        // Виклик методу, який потрібно протестувати
        double result = analyzer.calculateTotalBalance();

        // Перевірка результату
        Assertions.assertEquals(200.0, result, "Розрахунок загального балансу неправильний");
    }

    // ---------- Задача 3.2 ----------
    @Test
    public void testCountTransactionsByMonth() {
        // Підготовка тестових даних
        Transaction transaction1 = new Transaction("01-02-2023", 50.0, "Дохід");
        Transaction transaction2 = new Transaction("15-02-2023", -20.0, "Витрата");
        Transaction transaction3 = new Transaction("05-03-2023", 100.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);

        // Створення екземпляру TransactionAnalyzer з тестовими даними
        TransactionAnalyzer analyzer = new TransactionAnalyzer(transactions);

        int countFeb = analyzer.countTransactionsByMonth("02-2023");
        int countMar = analyzer.countTransactionsByMonth("03-2023");

        // Перевірка результатів
        Assertions.assertEquals(2, countFeb, "Кількість транзакцій за лютий неправильна");
        Assertions.assertEquals(1, countMar, "Кількість транзакцій за березень неправильна");
    }

    // ---------- Самостійна робота 1.2: тест на 10 найбільших витрат ----------
    @Test
    public void testFindTopExpenses() {
        List<Transaction> transactions = new ArrayList<>();
        // 12 витрат: -100, -200, ..., -1200
        for (int i = 1; i <= 12; i++) {
            transactions.add(new Transaction(String.format("%02d-03-2023", i), -100.0 * i, "Витрата" + i));
        }
        // доходи не повинні потрапляти до списку витрат
        transactions.add(new Transaction("20-03-2023", 99999.0, "Зарплата"));

        TransactionAnalyzer analyzer = new TransactionAnalyzer(transactions);
        List<Transaction> top = analyzer.findTopExpenses();

        Assertions.assertEquals(10, top.size(), "Має бути рівно 10 витрат");
        Assertions.assertEquals(-1200.0, top.get(0).getAmount(), "Перша - найбільша витрата");
        Assertions.assertEquals(-300.0, top.get(9).getAmount(), "Десята - -300");
        Assertions.assertTrue(top.stream().allMatch(t -> t.getAmount() < 0), "Лише витрати");
        for (int i = 1; i < top.size(); i++) {
            Assertions.assertTrue(top.get(i - 1).getAmount() <= top.get(i).getAmount(),
                    "Список має бути відсортований від найбільшої витрати");
        }
    }

    @Test
    public void testFindTopExpensesWhenFewerThanTen() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("01-01-2024", -10.0, "А"),
                new Transaction("02-01-2024", 500.0, "Дохід"));
        Assertions.assertEquals(1, new TransactionAnalyzer(transactions).findTopExpenses().size());
    }

    // ---------- Самостійна робота 2.1: найбільша / найменша витрата за період ----------
    @Test
    public void testLargestAndSmallestExpenseInPeriod() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("05-12-2023", -450.0, "Сільпо"),
                new Transaction("07-12-2023", -120.0, "Аптека"),
                new Transaction("14-12-2023", -3200.0, "Оренда квартири"),
                new Transaction("10-12-2023", 8000.0, "Зарплата"),
                new Transaction("05-01-2024", -9000.0, "Поза періодом"));
        TransactionAnalyzer analyzer = new TransactionAnalyzer(transactions);

        LocalDate from = LocalDate.of(2023, 12, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);

        Assertions.assertEquals(-3200.0, analyzer.findLargestExpense(from, to).orElseThrow().getAmount());
        Assertions.assertEquals(-120.0, analyzer.findSmallestExpense(from, to).orElseThrow().getAmount());
    }

    @Test
    public void testExpenseInPeriodBoundariesAndEmpty() {
        List<Transaction> transactions = Arrays.asList(new Transaction("15-12-2023", -50.0, "А"));
        TransactionAnalyzer analyzer = new TransactionAnalyzer(transactions);

        // границі включно
        LocalDate day = LocalDate.of(2023, 12, 15);
        Assertions.assertTrue(analyzer.findLargestExpense(day, day).isPresent());

        // період без витрат
        Optional<Transaction> none = analyzer.findLargestExpense(
                LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 31));
        Assertions.assertTrue(none.isEmpty());
    }

    // ---------- Самостійна робота 2.2: суми по категоріях та місяцях ----------
    @Test
    public void testExpensesByCategoryAndMonth() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("05-12-2023", -450.0, "Сільпо"),
                new Transaction("20-12-2023", -550.0, "Сільпо"),
                new Transaction("07-01-2024", -120.0, "Аптека"),
                new Transaction("10-01-2024", 8000.0, "Зарплата"));
        TransactionAnalyzer analyzer = new TransactionAnalyzer(transactions);

        Map<String, Double> byCategory = analyzer.calculateExpensesByCategory();
        Assertions.assertEquals(1000.0, byCategory.get("Сільпо"));
        Assertions.assertEquals(120.0, byCategory.get("Аптека"));
        Assertions.assertFalse(byCategory.containsKey("Зарплата"), "Доходи не є витратами");
        Assertions.assertEquals("Сільпо", byCategory.keySet().iterator().next(), "Перша - найбільша категорія");

        Map<YearMonth, Double> byMonth = analyzer.calculateExpensesByMonth();
        Assertions.assertEquals(1000.0, byMonth.get(YearMonth.of(2023, 12)));
        Assertions.assertEquals(120.0, byMonth.get(YearMonth.of(2024, 1)));
    }
}
