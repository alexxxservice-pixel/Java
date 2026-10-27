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

public class TransactionAnalyzer {
    private final List<Transaction> transactions;
    private final DateTimeFormatter dateFormatter;

    public TransactionAnalyzer(List<Transaction> transactions) {
        this.transactions = transactions;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    // ---------- Задача 2: Метод для розрахунку загального балансу ----------
    public double calculateTotalBalance() {
        double balance = 0;
        for (Transaction transaction : transactions) {
            balance += transaction.getAmount();
        }
        return balance;
    }

    // ---------- Задача 3: Метод для підрахунку транзакцій за місяць і рік ("MM-yyyy") ----------
    public int countTransactionsByMonth(String monthYear) {
        int count = 0;
        for (Transaction transaction : transactions) {
            LocalDate date = LocalDate.parse(transaction.getDate(), dateFormatter);
            String transactionMonthYear = date.format(DateTimeFormatter.ofPattern("MM-yyyy"));
            if (transactionMonthYear.equals(monthYear)) {
                count++;
            }
        }
        return count;
    }

    // ---------- Задача 5: 10 найбільших витрат (Java Streams) ----------
    public List<Transaction> findTopExpenses() {
        return findTopExpenses(10);
    }

    public List<Transaction> findTopExpenses(int limit) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)                          // лише витрати (від'ємні значення)
                .sorted(Comparator.comparing(Transaction::getAmount))    // сортування за сумою (найбільша витрата першою)
                .limit(limit)                                            // обмеження результату
                .collect(Collectors.toList());                           // збір результату в список
    }

    // ---------- Самостійна робота 2.1: найбільша / найменша витрата за період ----------
    /** Найбільша витрата (за модулем) у періоді [from; to] включно. */
    public Optional<Transaction> findLargestExpense(LocalDate from, LocalDate to) {
        return expensesInPeriod(from, to)
                .min(Comparator.comparing(Transaction::getAmount));
    }

    /** Найменша витрата (за модулем) у періоді [from; to] включно. */
    public Optional<Transaction> findSmallestExpense(LocalDate from, LocalDate to) {
        return expensesInPeriod(from, to)
                .max(Comparator.comparing(Transaction::getAmount));
    }

    private java.util.stream.Stream<Transaction> expensesInPeriod(LocalDate from, LocalDate to) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .filter(t -> {
                    LocalDate d = parseDate(t);
                    return !d.isBefore(from) && !d.isAfter(to);
                });
    }

    // ---------- Задача 5 (п.5 етапів): на що витрачено найбільше грошей ----------
    /** Сума витрат (додатні числа) по категоріях, від найбільшої до найменшої. */
    public Map<String, Double> calculateExpensesByCategory() {
        Map<String, Double> sums = transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(Transaction::getDescription,
                        Collectors.summingDouble(t -> -t.getAmount())));

        Map<String, Double> sorted = new LinkedHashMap<>();
        sums.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(e -> sorted.put(e.getKey(), e.getValue()));
        return sorted;
    }

    // ---------- Самостійна робота 2.2: сумарні витрати по місяцях ----------
    /** Сума витрат по місяцях у хронологічному порядку. */
    public Map<YearMonth, Double> calculateExpensesByMonth() {
        Map<YearMonth, Double> sums = new TreeMap<>();
        for (Transaction t : transactions) {
            if (t.getAmount() < 0) {
                sums.merge(YearMonth.from(parseDate(t)), -t.getAmount(), Double::sum);
            }
        }
        return sums;
    }

    private LocalDate parseDate(Transaction t) {
        return LocalDate.parse(t.getDate(), dateFormatter);
    }
}
