package dut.pd3.practics.practic_2.financeanalyzer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCSVReaderTest {

    @Test
    void shouldParseOneCorrectLine() {
        Transaction t = TransactionCSVReader.parseLine("05-12-2023,-450,Сільпо").orElseThrow();

        assertEquals("05-12-2023", t.getDate());
        assertEquals(-450.0, t.getAmount(), 0.001);
        assertEquals("Сільпо", t.getDescription());
    }

    @Test
    void shouldReturnEmptyForHeaderAndBrokenLines() {
        assertTrue(TransactionCSVReader.parseLine("Дата,Сума,Категорія").isEmpty());
        assertTrue(TransactionCSVReader.parseLine("лише,два").isEmpty());
        assertTrue(TransactionCSVReader.parseLine("").isEmpty());
    }

    @Test
    void shouldParseSeveralLinesSkippingHeaderBomBlankAndInvalidLines() throws IOException {
        String csv = "\uFEFFДата,Сума,Категорія\n"
                + "05-12-2023,-450,Сільпо\n"
                + "\n"
                + "це не транзакція\n"
                + "10-12-2023,8000,Зарплата\n"
                + "01-01-2024,-10,Кава, цукор\n";

        List<Transaction> result = TransactionCSVReader.parseTransactions(new StringReader(csv));

        assertEquals(3, result.size());
        assertEquals(new Transaction("05-12-2023", -450.0, "Сільпо"), result.get(0));
        assertEquals(8000.0, result.get(1).getAmount(), 0.001);
        assertEquals("Кава, цукор", result.get(2).getDescription());
    }

    @Test
    void shouldReadTransactionsFromLocalFile(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("test.csv");
        Files.writeString(file, "Дата,Сума,Категорія\n05-12-2023,-450,Сільпо\n07-12-2023,-120,Аптека\n",
                StandardCharsets.UTF_8);

        List<Transaction> result = TransactionCSVReader.readTransactions(file.toString());

        assertEquals(2, result.size());
        assertEquals("Аптека", result.get(1).getDescription());
    }

    @Test
    void shouldThrowForMissingFile() {
        assertThrows(IOException.class, () -> TransactionCSVReader.readTransactions("no/such/file.csv"));
    }

    @Test
    void shouldDetectUrlSource() {
        assertTrue(TransactionCSVReader.isUrl("https://informer.com.ua/dut/java/pr2.csv"));
        assertTrue(TransactionCSVReader.isUrl("HTTP://example.com/a.csv"));
        assertFalse(TransactionCSVReader.isUrl("C:\\data\\pr2.csv"));
    }
}
