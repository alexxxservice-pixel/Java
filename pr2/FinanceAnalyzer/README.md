# Фінансовий аналізатор (Java)

Структура: `src/` – код програми, `test/` – JUnit 5 тести, `pr2_sample.csv` – локальний приклад даних.

Запуск (JDK 21):
    javac -encoding UTF-8 -d out src/*.java
    java -cp out Main                    # дані з https://informer.com.ua/dut/java/pr2.csv
    java -cp out Main pr2_sample.csv     # або локальний файл

Тести: додайте JUnit 5 (jupiter-api + engine) у classpath IDE (IntelliJ: Add library) і запустіть тести з папки test/.
