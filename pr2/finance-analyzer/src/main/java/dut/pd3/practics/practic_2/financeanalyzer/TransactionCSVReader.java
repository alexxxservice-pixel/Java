package dut.pd3.practics.practic_2.financeanalyzer;

import lombok.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class TransactionCSVReader {

    private static final String SEPARATOR = ",";
    private static final char BOM = '\uFEFF';

    public static List<Transaction> readTransactions(@NonNull String source) throws IOException {
        try (BufferedReader reader = openReader(source)) {
            return parseTransactions(reader);
        }
    }

    // джерело даних

    public static boolean isUrl(@NonNull String source) {
        String s = source.toLowerCase();
        return s.startsWith("http://") || s.startsWith("https://");
    }

    public static BufferedReader openReader(@NonNull String source) throws IOException {
        InputStream in = isUrl(source)
                ? URI.create(source).toURL().openStream()
                : Files.newInputStream(Path.of(source));
        return new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
    }

    // обробка даних

    public static List<Transaction> parseTransactions(@NonNull Reader source) throws IOException {
        BufferedReader reader = source instanceof BufferedReader br ? br : new BufferedReader(source);
        List<Transaction> transactions = new ArrayList<>();
        String line;
        int lineNumber = 0;
        while ((line = reader.readLine()) != null) {
            lineNumber++;
            if (lineNumber == 1 && !line.isEmpty() && line.charAt(0) == BOM) {
                line = line.substring(1);
            }
            if (line.isBlank()) {
                continue;
            }
            Optional<Transaction> parsed = parseLine(line);
            if (parsed.isPresent()) {
                transactions.add(parsed.get());
            } else if (lineNumber > 1) {
                System.err.println("Пропущено некоректний рядок " + lineNumber + ": " + line);
            }
        }
        return transactions;
    }

    public static Optional<Transaction> parseLine(@NonNull String line) {
        String[] values = line.split(SEPARATOR, 3);
        if (values.length < 3) {
            return Optional.empty();
        }
        try {
            double amount = Double.parseDouble(values[1].trim());
            return Optional.of(new Transaction(values[0].trim(), amount, values[2].trim()));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
