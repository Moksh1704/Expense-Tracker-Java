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
```
 Compile the Code
```bash
javac *.java
```
 Run the Application
```bash
java Main

```
-----

### Project Structure

Expense-Tracker-Java/
│── Main.java # Entry point of the application
│── Expense.java # Model class for expense data
│── ExpenseManager.java # Handles expense logic (CRUD, sorting, filtering)
│── ExpenseTrackerGUI.java # Graphical User Interface (Swing)
│── expenses.txt # Local file to store expenses (auto-generated)
│── README.md # Project documentation


------

### Future Enhancements
- Add charts for visual spending analysis
- Multi-user support with login
- Save data in a database (e.g., MySQL)
