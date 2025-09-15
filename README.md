#  Expense Tracker (Java Project)

A simple and visually appealing **Expense Tracker application** built in **Java (Swing)**.  
This project helps users efficiently manage their expenses, categorize spending, and view totals in a user-friendly GUI.

---

##  Features
-  Add Expense with amount, category, and description  
-  Categorize Expenses (e.g., Food, Travel, Bills, etc.)  
-  View Total & Category-wise Expenses  
-  Delete Expenses  
-  Data Persistence (expenses saved to file, reloads when app restarts)  
-  Color-coded & Interactive GUI for a better user experience  

---

##  Tech Stack
- **Language:** Java  
- **GUI Framework:** Swing  
- **IDE:** VS Code  

---

##  Getting Started  

###  Clone the Repository
```bash
git clone https://github.com/Moksh1704/Expense-Tracker-Java.git
cd Expense-Tracker-Java
 Compile the Code

javac *.java
 Run the Application

java Main

```
-----

### Project Structure

Expense-Tracker-Java/
│── Expense.java          # Expense model class
│── ExpenseManager.java   # Handles logic (add, delete, calculate totals)
│── ExpenseTrackerGUI.java # Swing-based user interface
│── Main.java             # Entry point
│── expenses.txt          # Saved data file

------

### Future Enhancements
 Add charts for visual spending analysis

 Multi-user support with login

 Save data in a database (e.g., MySQL)
