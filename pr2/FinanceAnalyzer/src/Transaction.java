/**
 * Модель транзакції (Задача 1).
 */
public class Transaction {
    private String date;          // формат dd-MM-yyyy
    private double amount;        // > 0 дохід, < 0 витрата
    private String description;   // категорія / опис

    public Transaction(String date, double amount, String description) {
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    // Getters
    public String getDate() { return date; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }

    // Setters
    public void setDate(String date) { this.date = date; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }

    // toString метод для зручності виводу
    @Override
    public String toString() {
        return "Transaction{" +
                "date='" + date + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }
}
