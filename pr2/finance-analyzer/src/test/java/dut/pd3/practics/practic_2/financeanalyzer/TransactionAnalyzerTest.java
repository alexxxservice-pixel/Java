package dut.pd3.practics.practic_2.financeanalyzer;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TransactionAnalyzerTest {

    private static Transaction tx(String date, double amount, String description) {
        return new Transaction(date, amount, description);
    }

    @Test
    void testCalculateTotalBalance() {
        List<Transaction> transactions = Arrays.asList(
                tx("2023-01-01", 100.0, "Дохід"),
                tx("2023-01-02", -50.0, "Витрата"),
                tx("2023-01-03", 150.0, "Дохід"));

        assertEquals(200.0, TransactionAnalyzer.calculateTotalBalance(transactions), 0.001,
                "Розрахунок загального балансу неправильний");
    }

    @Test
    void testCalculateTotalBalanceEmptyList() {
        assertEquals(0.0, TransactionAnalyzer.calculateTotalBalance(List.of()), 0.001);
    }

    @Test
    void testCountTransactionsByMonth() {
        List<Transaction> transactions = Arrays.asList(
                tx("01-02-2023", 50.0, "Дохід"),
                tx("15-02-2023", -20.0, "Витрата"),
                tx("05-03-2023", 100.0, "Дохід"));

        assertEquals(2, TransactionAnalyzer.countTransactionsByMonth(transactions, "02-2023"),
                "Кількість транзакцій за лютий неправильна");
        assertEquals(1, TransactionAnalyzer.countTransactionsByMonth(transactions, "03-2023"),
                "Кількість транзакцій за березень неправильна");
        assertEquals(0, TransactionAnalyzer.countTransactionsByMonth(transactions, "02-2024"));
    }

    @Test
    void testFindTopExpensesReturnsTenLargestSortedAndIgnoresIncome() {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            transactions.add(tx("05-12-2023", -100.0 * i, "Витрата" + i));
        }
        transactions.add(tx("06-12-2023", 99999.0, "Зарплата"));

        List<Transaction> top = TransactionAnalyzer.findTopExpenses(transactions);

        assertEquals(10, top.size());
        assertEquals(-1200.0, top.get(0).getAmount(), 0.001);
        assertEquals(-300.0, top.get(9).getAmount(), 0.001);
        assertTrue(top.stream().allMatch(t -> t.getAmount() < 0));
    }

    @Test
    void testFindTopExpensesFewerThanLimit() {
        List<Transaction> transactions = List.of(tx("05-12-2023", -10.0, "A"), tx("06-12-2023", 5.0, "B"));
        assertEquals(1, TransactionAnalyzer.findTopExpenses(transactions).size());
    }

    @Test
    void testLargestAndSmallestExpenseInPeriod() {
        List<Transaction> transactions = List.of(
                tx("05-12-2023", -450, "Сільпо"),
                tx("10-12-2023", 8000, "Зарплата"),
                tx("14-12-2023", -3200, "Оренда квартири"),
                tx("18-12-2023", -200, "Кав'ярня"),
                tx("05-01-2024", -500, "Сільпо"));
        LocalDate from = LocalDate.of(2023, 12, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);

        assertEquals(-3200, TransactionAnalyzer.findLargestExpense(transactions, from, to).orElseThrow().getAmount(), 0.001);
        assertEquals(-200, TransactionAnalyzer.findSmallestExpense(transactions, from, to).orElseThrow().getAmount(), 0.001);
    }

    @Test
    void testExtremesPeriodBoundsInclusiveAndEmptyPeriod() {
        List<Transaction> transactions = List.of(tx("05-01-2024", -500, "Сільпо"));

        LocalDate day = LocalDate.of(2024, 1, 5);
        assertTrue(TransactionAnalyzer.findLargestExpense(transactions, day, day).isPresent());
        assertEquals(Optional.empty(),
                TransactionAnalyzer.findLargestExpense(transactions, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 31)));
    }

    @Test
    void testSumExpensesByCategorySortedDescending() {
        List<Transaction> transactions = List.of(
                tx("05-12-2023", -450, "Сільпо"),
                tx("07-12-2023", -120, "Аптека"),
                tx("10-12-2023", 8000, "Зарплата"),
                tx("05-01-2024", -500, "Сільпо"));

        var result = TransactionAnalyzer.sumExpensesByCategory(transactions);

        assertEquals(List.of("Сільпо", "Аптека"), new ArrayList<>(result.keySet()));
        assertEquals(950.0, result.get("Сільпо"), 0.001);
        assertEquals(120.0, result.get("Аптека"), 0.001);
        assertFalse(result.containsKey("Зарплата"));
    }

    @Test
    void testSumExpensesByMonthChronological() {
        List<Transaction> transactions = List.of(
                tx("05-01-2024", -500, "Сільпо"),
                tx("05-12-2023", -450, "Сільпо"),
                tx("14-12-2023", -3200, "Оренда"));

        var result = TransactionAnalyzer.sumExpensesByMonth(transactions);

        assertEquals(List.of(YearMonth.of(2023, 12), YearMonth.of(2024, 1)), new ArrayList<>(result.keySet()));
        assertEquals(3650.0, result.get(YearMonth.of(2023, 12)), 0.001);
        assertEquals(500.0, result.get(YearMonth.of(2024, 1)), 0.001);
    }
}
