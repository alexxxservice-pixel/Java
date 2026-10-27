import java.io.IOException;
import java.util.List;

/**
 * Задача 1.2*: абстракція джерела даних.
 * Повертає "сирі" рядки, нічого не знаючи про формат (CSV, JSON тощо).
 */
public interface DataSource {
    List<String> readLines() throws IOException;
}
