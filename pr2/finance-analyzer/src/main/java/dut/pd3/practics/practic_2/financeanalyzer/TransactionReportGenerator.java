package dut.pd3.practics.practic_2.financeanalyzer;

import lombok.NonNull;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Виведення звітів. Абстрактний клас зі статичними методами.
 */
public abstract class TransactionReportGenerator {

    public static final double MONEY_PER_SYMBOL = 1000.0;
    public static final char BAR_SYMBOL = '*';

    public static void printBalanceReport(double totalBalance) {
        System.out.println("Загальний баланс: " + totalBalance);
    }

    public static void printTransactionsCountByMonth(@NonNull String monthYear, int count) {
        System.out.println("Кількість транзакцій за " + monthYear + ": " + count);
    }

    public static void printTopExpensesReport(@NonNull List<Transaction> topExpenses) {
        System.out.println("10 найбільших витрат:");
        for (Transaction expense : topExpenses) {
            System.out.println(expense.getDescription() + ": " + expense.getAmount());
        }
    }

    public static void printExtremesReport(@NonNull LocalDate from, @NonNull LocalDate to,
                                           @NonNull Optional<Transaction> largest,
                                           @NonNull Optional<Transaction> smallest) {
        System.out.println("Витрати за період " + from.format(TransactionAnalyzer.DATE_FORMATTER)
                + " — " + to.format(TransactionAnalyzer.DATE_FORMATTER) + ":");
        System.out.println("  Найбільша: " + describe(largest));
        System.out.println("  Найменша:  " + describe(smallest));
    }

    public static void printExpensesByCategoryReport(@NonNull Map<String, Double> byCategory) {
        System.out.println("Витрати по категоріях (1 '" + BAR_SYMBOL + "' = " + (int) MONEY_PER_SYMBOL + " грн):");
        byCategory.forEach((category, sum) ->
                System.out.println(String.format("%-22s %10.2f %s", category, sum, buildBar(sum))));
    }

    public static void printExpensesByMonthReport(@NonNull Map<YearMonth, Double> byMonth) {
        System.out.println("Витрати по місяцях (1 '" + BAR_SYMBOL + "' = " + (int) MONEY_PER_SYMBOL + " грн):");
        byMonth.forEach((month, sum) ->
                System.out.println(String.format("%-22s %10.2f %s",
                        month.format(TransactionAnalyzer.MONTH_YEAR_FORMATTER), sum, buildBar(sum))));
    }

    public static String buildBar(double amount) {
        double abs = Math.abs(amount);
        if (abs == 0) {
            return "";
        }
        int symbols = (int) Math.max(1, Math.round(abs / MONEY_PER_SYMBOL));
        return String.valueOf(BAR_SYMBOL).repeat(symbols);
    }

    private static String describe(Optional<Transaction> transaction) {
        return transaction
                .map(t -> t.getDate() + " " + t.getDescription() + ": " + t.getAmount())
                .orElse("немає витрат за цей період");
    }
}
