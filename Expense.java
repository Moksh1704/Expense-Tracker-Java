import java.io.Serializable;
import java.time.LocalDate;

public class Expense implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private double amount;
    private String category;
    private LocalDate date;
    private String notes;

    public Expense(int id, double amount, String category, LocalDate date, String notes) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.notes = notes;
    }

    public int getId() { return id; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }
    public String getNotes() { return notes; }

    public void setAmount(double amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setNotes(String notes) { this.notes = notes; }
}
