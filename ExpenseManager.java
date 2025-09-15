import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class ExpenseManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Expense> expenses;
    private int nextId;
    private static final String FILE_NAME = "expenses.dat";

    public ExpenseManager() {
        expenses = new ArrayList<Expense>();
        nextId = 1;
    }

    public static ExpenseManager load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (ExpenseManager) ois.readObject();
        } catch (Exception e) {
            return new ExpenseManager();
        }
    }

    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Expense addExpense(double amount, String category, LocalDate date, String notes) {
        Expense e = new Expense(nextId++, amount, category, date, notes);
        expenses.add(e);
        return e;
    }

    public boolean removeExpenseById(int id) {
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getId() == id) {
                expenses.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<Expense> getAllExpenses() {
        return new ArrayList<Expense>(expenses);
    }

    // returns expenses for a given month/year
    public List<Expense> getExpensesForMonth(int year, int month) {
        ArrayList<Expense> out = new ArrayList<Expense>();
        for (Expense e : expenses) {
            if (e.getDate().getYear() == year && e.getDate().getMonthValue() == month) out.add(e);
        }
        return out;
    }

    // total for a given month
    public double getTotalForMonth(int year, int month) {
        double s = 0.0;
        for (Expense e : getExpensesForMonth(year, month)) s += e.getAmount();
        return s;
    }

    // category totals for a month (keeps insertion order)
    public Map<String, Double> getCategoryTotalsForMonth(int year, int month) {
        Map<String, Double> map = new LinkedHashMap<String, Double>();
        for (Expense e : getExpensesForMonth(year, month)) {
            String c = e.getCategory();
            map.put(c, map.getOrDefault(c, 0.0) + e.getAmount());
        }
        return map;
    }
}
