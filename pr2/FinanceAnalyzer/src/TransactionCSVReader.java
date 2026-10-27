import java.util.List;

/**
 * Читає транзакції з CSV та перетворює їх у Transaction.
 * Працює і з URL, і з локальним файлом (Задача 1.2*).
 */
public class TransactionCSVReader {

    public List<Transaction> readTransactions(String filePath) {
        DataSource source = (filePath.startsWith("http://") || filePath.startsWith("https://"))
                ? new UrlDataSource(filePath)
                : new FileDataSource(filePath);
        return readTransactions(source);
    }

    public List<Transaction> readTransactions(DataSource source) {
        return new TransactionLoader(source, new CsvTransactionParser()).load();
    }
}
