import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Джерело даних: локальний файл. */
public class FileDataSource implements DataSource {
    private final Path path;

    public FileDataSource(Path path) {
        this.path = path;
    }

    public FileDataSource(String path) {
        this(Path.of(path));
    }

    @Override
    public List<String> readLines() throws IOException {
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }
}
