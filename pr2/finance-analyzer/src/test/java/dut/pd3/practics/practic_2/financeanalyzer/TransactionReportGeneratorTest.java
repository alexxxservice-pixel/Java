package dut.pd3.practics.practic_2.financeanalyzer;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransactionReportGeneratorTest {

    @Test
    void buildBarScalesByThousand() {
        assertEquals("", TransactionReportGenerator.buildBar(0));
        assertEquals("*", TransactionReportGenerator.buildBar(120));    // ненульова сума — мінімум 1
        assertEquals("*", TransactionReportGenerator.buildBar(1000));
        assertEquals("***", TransactionReportGenerator.buildBar(3200));
        assertEquals("*****", TransactionReportGenerator.buildBar(-5000)); // знак ігнорується
    }

    @Test
    void categoryReportContainsCategoriesAndBars() {
        Map<String, Double> data = new LinkedHashMap<>();
        data.put("Оренда квартири", 3200.0);
        data.put("Аптека", 120.0);

        String out = capture(() -> TransactionReportGenerator.printExpensesByCategoryReport(data));

        assertTrue(out.contains("Оренда квартири"));
        assertTrue(out.contains("3200.00 ***"));
        assertTrue(out.contains("120.00 *"));
    }

    @Test
    void balanceReportPrintsTotal() {
        String out = capture(() -> TransactionReportGenerator.printBalanceReport(1500.0));
        assertTrue(out.contains("Загальний баланс: 1500.0"));
    }

    private static String capture(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}
