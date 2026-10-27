import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Задача 4: клас для виведення звітів.
 */
public class TransactionReportGenerator {

    public static final double AMOUNT_PER_STAR = 1000.0;

    // ---------- Задача 4 ----------
    public void printBalanceReport(double totalBalance) {
        System.out.println("Загальний баланс: " + totalBalance);
    }

    public void printTransactionsCountByMonth(String monthYear, int count) {
        System.out.println("Кількість транзакцій за " + monthYear + ": " + count);
    }

    // ---------- Задача 5 ----------
    public void printTopExpensesReport(List<Transaction> topExpenses) {
        System.out.println("10 найбільших витрат:");
        for (Transaction expense : topExpenses) {
            System.out.println(expense.getDescription() + ": " + expense.getAmount());
        }
    }

    // ---------- Самостійна робота ----------
    public void printExpenseRangeReport(LocalDate from, LocalDate to,
                                        Optional<Transaction> largest, Optional<Transaction> smallest) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println("Витрати за період " + from.format(f) + " – " + to.format(f) + ":");
        System.out.println("  Найбільша: " + describe(largest));
        System.out.println("  Найменша:  " + describe(smallest));
    }

    private String describe(Optional<Transaction> t) {
        return t.map(x -> x.getDescription() + " (" + x.getDate() + "): " + x.getAmount())
                .orElse("немає витрат за цей період");
    }

    public void printCategoryChart(Map<String, Double> expensesByCategory) {
        System.out.println("Витрати по категоріях (1 * = " + (int) AMOUNT_PER_STAR + " грн):");
        for (Map.Entry<String, Double> e : expensesByCategory.entrySet()) {
            printChartLine(e.getKey(), e.getValue());
        }
    }

    public void printMonthChart(Map<YearMonth, Double> expensesByMonth) {
        System.out.println("Витрати по місяцях (1 * = " + (int) AMOUNT_PER_STAR + " грн):");
        DateTimeFormatter f = DateTimeFormatter.ofPattern("MM-yyyy");
        for (Map.Entry<YearMonth, Double> e : expensesByMonth.entrySet()) {
            printChartLine(e.getKey().format(f), e.getValue());
        }
    }

    private void printChartLine(String label, double amount) {
        System.out.println(String.format(Locale.US, "  %-22s %10.2f грн %s",
                label, amount, stars(amount)));
    }

    String stars(double amount) {
        int count = (int) Math.round(amount / AMOUNT_PER_STAR);
        if (count == 0 && amount > 0) {
            count = 1;
        }
        return "*".repeat(count);
    }
}
