package dut.pd3.practics.practic_2.financeanalyzer;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Модель банківської транзакції.
 * Lombok генерує getters, setters, equals, hashCode, toString та конструктор з усіма полями.
 */
@Data
@AllArgsConstructor
public class Transaction {
    /** Дата у форматі dd-MM-yyyy. */
    private String date;
    /** Сума: додатна — дохід, від'ємна — витрата. */
    private double amount;
    /** Опис / категорія (колонка «Категорія» у CSV). */
    private String description;
}
