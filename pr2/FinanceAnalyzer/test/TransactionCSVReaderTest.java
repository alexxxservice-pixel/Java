import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class TransactionCSVReaderTest {

    // ---------- Самостійна робота 1.1: тест читання даних із CSV ----------
    @Test
    public void testReadTransactionsFromCsvFile(@TempDir Path tempDir) throws IOException {
        Path csv = tempDir.resolve("test.csv");
        Files.writeString(csv,
                "Дата,Сума,Категорія\n" +
                "05-12-2023,-450,Сільпо\n" +
                "10-12-2023,8000,Зарплата\n" +
                "\n" +
                "24-12-2023,-950.5,Кав'ярня, центр\n",
                StandardCharsets.UTF_8);

        List<Transaction> result = new TransactionCSVReader().readTransactions(csv.toString());

        Assertions.assertEquals(3, result.size(), "Заголовок і порожні рядки пропускаються");

        Assertions.assertEquals("05-12-2023", result.get(0).getDate());
        Assertions.assertEquals(-450.0, result.get(0).getAmount());
        Assertions.assertEquals("Сільпо", result.get(0).getDescription());

        Assertions.assertEquals(8000.0, result.get(1).getAmount());
        Assertions.assertEquals("Кав'ярня, центр", result.get(2).getDescription(),
                "Кома в описі не повинна ламати розбір");
    }

    @Test
    public void testReadFileWithoutHeaderAndWithBadLine(@TempDir Path tempDir) throws IOException {
        Path csv = tempDir.resolve("noheader.csv");
        Files.writeString(csv, "05-12-2023,-450,Сільпо\nсміттєвий рядок\n", StandardCharsets.UTF_8);

        List<Transaction> result = new TransactionCSVReader().readTransactions(csv.toString());
        Assertions.assertEquals(1, result.size());
    }

    @Test
    public void testMissingFileReturnsEmptyList() {
        List<Transaction> result = new TransactionCSVReader().readTransactions("/no/such/file.csv");
        Assertions.assertTrue(result.isEmpty());
    }

    // ---------- Задача 1.2*: підміна джерела без зміни логіки ----------
    @Test
    public void testCustomDataSource() {
        DataSource inMemory = () -> List.of("01-01-2024,-10,Тест", "02-01-2024,20,Дохід");
        List<Transaction> result = new TransactionCSVReader().readTransactions(inMemory);
        Assertions.assertEquals(2, result.size());
    }
}
