import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** Джерело даних: файл за URL */
public class UrlDataSource implements DataSource {
    private final String url;

    public UrlDataSource(String url) {
        this.url = url;
    }

    @Override
    public List<String> readLines() throws IOException {
        List<String> lines = new ArrayList<>();
        URL u = URI.create(url).toURL();
        // Відкриття потоку для читання з URL
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(u.openStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
