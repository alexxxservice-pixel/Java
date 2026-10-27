import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Задача 1.2*: поєднує будь-яке джерело даних (DataSource)
 * з будь-яким парсером (TransactionParser).
 */
public class TransactionLoader {
    private final DataSource source;
    private final TransactionParser parser;

    public TransactionLoader(DataSource source, TransactionParser parser) {
        this.source = source;
        this.parser = parser;
    }

    public List<Transaction> load() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            boolean firstLine = true;
            for (String raw : source.readLines()) {
                String line = raw.replace("\uFEFF", "").trim(); // прибираємо BOM і пробіли
                if (line.isEmpty()) {
                    continue;
                }
                try {
                    transactions.add(parser.parse(line));
                } catch (IllegalArgumentException e) {
                    // Перший рядок, що не розібрався, вважаємо заголовком ("Дата,Сума,Категорія")
                    if (!firstLine) {
                        System.err.println("Пропущено рядок: " + e.getMessage());
                    }
                }
                firstLine = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
