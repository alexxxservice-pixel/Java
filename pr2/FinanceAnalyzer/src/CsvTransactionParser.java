/** Парсер рядка CSV формату: дата,сума,категорія. */
public class CsvTransactionParser implements TransactionParser {
    @Override
    public Transaction parse(String line) {
        // limit = 3, щоб кома в описі не ламала розбір
        String[] values = line.split(",", 3);
        if (values.length < 3) {
            throw new IllegalArgumentException("Очікується 3 поля, отримано: " + line);
        }
        try {
            return new Transaction(values[0].trim(),
                    Double.parseDouble(values[1].trim()),
                    values[2].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некоректна сума в рядку: " + line, e);
        }
    }
}
