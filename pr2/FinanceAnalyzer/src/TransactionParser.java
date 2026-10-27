/**
 * Задача 1.2*: перетворення одного рядка у Transaction.
 * Нова реалізація (JSON, TSV...) не вимагає змін у логіці аналізу.
 */
public interface TransactionParser {
    /** @throws IllegalArgumentException якщо рядок має неправильний формат */
    Transaction parse(String line);
}
