import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Шлях можна передати аргументом (URL або локальний файл), за замовчуванням - URL з презентації
        String filePath = args.length > 0 ? args[0] : "https://informer.com.ua/dut/java/pr2.csv";

        // Задача 1: читання даних
        TransactionCSVReader csvReader = new TransactionCSVReader();
        List<Transaction> transactions = csvReader.readTransactions(filePath);

        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
        System.out.println();

        TransactionAnalyzer analyzer = new TransactionAnalyzer(transactions);
        TransactionReportGenerator reportGenerator = new TransactionReportGenerator();

        // Задача 2: загальний баланс
        double totalBalance = analyzer.calculateTotalBalance();
        reportGenerator.printBalanceReport(totalBalance);

        // Задача 3: кількість транзакцій за місяць
        String monthYear = "01-2024";
        int transactionsCount = analyzer.countTransactionsByMonth(monthYear);
        reportGenerator.printTransactionsCountByMonth(monthYear, transactionsCount);

        // Задача 5: 10 найбільших витрат
        List<Transaction> topExpenses = analyzer.findTopExpenses();
        reportGenerator.printTopExpensesReport(topExpenses);
        System.out.println();

        // Самостійна робота: найбільша / найменша витрата за період
        LocalDate from = LocalDate.of(2023, 12, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);
        reportGenerator.printExpenseRangeReport(from, to,
                analyzer.findLargestExpense(from, to),
                analyzer.findSmallestExpense(from, to));
        System.out.println();

        // Самостійна робота: текстові звіти з візуалізацією
        reportGenerator.printCategoryChart(analyzer.calculateExpensesByCategory());
        System.out.println();
        reportGenerator.printMonthChart(analyzer.calculateExpensesByMonth());
    }
}
