package dut.pd3.practics.practic_2.financeanalyzer;

import lombok.NonNull;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Аналіз списку транзакцій. Абстрактний клас зі статичними методами:
 * стану немає, список транзакцій передається параметром.
 */
public abstract class TransactionAnalyzer {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter MONTH_YEAR_FORMATTER = DateTimeFormatter.ofPattern("MM-yyyy");
    public static final int DEFAULT_TOP_LIMIT = 10;

    public static double calculateTotalBalance(@NonNull List<Transaction> transactions) {
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    public static int countTransactionsByMonth(@NonNull List<Transaction> transactions, @NonNull String monthYear) {
        YearMonth target = YearMonth.parse(monthYear, MONTH_YEAR_FORMATTER);
        return (int) transactions.stream()
                .filter(t -> YearMonth.from(parseDate(t)).equals(target))
                .count();
    }

    public static List<Transaction> findTopExpenses(@NonNull List<Transaction> transactions) {
        return findTopExpenses(transactions, DEFAULT_TOP_LIMIT);
    }

    public static List<Transaction> findTopExpenses(@NonNull List<Transaction> transactions, int limit) {
        return transactions.stream()
                .filter(TransactionAnalyzer::isExpense)
                .sorted(Comparator.comparingDouble(Transaction::getAmount)) // найменше (найбільш від'ємне) — першим
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static Optional<Transaction> findLargestExpense(@NonNull List<Transaction> transactions,
                                                           @NonNull LocalDate from, @NonNull LocalDate to) {
        return expensesInPeriod(transactions, from, to)
                .min(Comparator.comparingDouble(Transaction::getAmount));
    }

    public static Optional<Transaction> findSmallestExpense(@NonNull List<Transaction> transactions,
                                                            @NonNull LocalDate from, @NonNull LocalDate to) {
        return expensesInPeriod(transactions, from, to)
                .max(Comparator.comparingDouble(Transaction::getAmount));
    }

    public static Map<String, Double> sumExpensesByCategory(@NonNull List<Transaction> transactions) {
        return transactions.stream()
                .filter(TransactionAnalyzer::isExpense)
                .collect(Collectors.groupingBy(Transaction::getDescription,
                        Collectors.summingDouble(t -> -t.getAmount())))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a, LinkedHashMap::new));
    }

    public static Map<YearMonth, Double> sumExpensesByMonth(@NonNull List<Transaction> transactions) {
        return transactions.stream()
                .filter(TransactionAnalyzer::isExpense)
                .collect(Collectors.groupingBy(t -> YearMonth.from(parseDate(t)), TreeMap::new,
                        Collectors.summingDouble(t -> -t.getAmount())));
    }


    private static boolean isExpense(Transaction t) {
        return t.getAmount() < 0;
    }

    private static LocalDate parseDate(Transaction t) {
        return LocalDate.parse(t.getDate(), DATE_FORMATTER);
    }

    private static java.util.stream.Stream<Transaction> expensesInPeriod(List<Transaction> transactions,
                                                                         LocalDate from, LocalDate to) {
        return transactions.stream()
                .filter(TransactionAnalyzer::isExpense)
                .filter(t -> {
                    LocalDate d = parseDate(t);
                    return !d.isBefore(from) && !d.isAfter(to);
                });
    }
}
