package dut.pd3.practics.practic_2.financeanalyzer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Main {

    private static final String DEFAULT_SOURCE = "https://informer.com.ua/dut/java/pr2.csv";

    public static void main(String[] args) {
        String source = args.length > 0 ? args[0] : DEFAULT_SOURCE;

        List<Transaction> transactions;
        try {
            transactions = TransactionCSVReader.readTransactions(source);
        } catch (IOException e) {
            System.err.println("Не вдалося прочитати дані з " + source + ": " + e);
            return;
        }

        // Задача 1: вивід вмісту файлу
        transactions.forEach(System.out::println);
        System.out.println();

        // Задача 2: загальний баланс
        TransactionReportGenerator.printBalanceReport(
                TransactionAnalyzer.calculateTotalBalance(transactions));

        // Задача 3: кількість транзакцій за місяць
        String monthYear = "01-2024";
        TransactionReportGenerator.printTransactionsCountByMonth(
                monthYear, TransactionAnalyzer.countTransactionsByMonth(transactions, monthYear));

        // Задача 5: 10 найбільших витрат
        TransactionReportGenerator.printTopExpensesReport(TransactionAnalyzer.findTopExpenses(transactions));
        System.out.println();

        // Самостійна: найбільша/найменша витрата за період
        LocalDate from = LocalDate.of(2023, 12, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);
        TransactionReportGenerator.printExtremesReport(from, to,
                TransactionAnalyzer.findLargestExpense(transactions, from, to),
                TransactionAnalyzer.findSmallestExpense(transactions, from, to));
        System.out.println();

        // Самостійна: звіти з візуалізацією '*'
        TransactionReportGenerator.printExpensesByCategoryReport(
                TransactionAnalyzer.sumExpensesByCategory(transactions));
        System.out.println();
        TransactionReportGenerator.printExpensesByMonthReport(
                TransactionAnalyzer.sumExpensesByMonth(transactions));
    }
}
