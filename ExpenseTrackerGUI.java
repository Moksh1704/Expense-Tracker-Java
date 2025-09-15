import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

public class ExpenseTrackerGUI extends JFrame {
    private ExpenseManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> monthBox;
    private JComboBox<String> yearBox;
    private JLabel totalLabel;
    private PieChartPanel chartPanel;

    private static final String[] DEFAULT_CATEGORIES = new String[] {
        "Food", "Travel", "Bills", "Shopping", "Entertainment", "Health", "Other"
    };

    public ExpenseTrackerGUI() {
        manager = ExpenseManager.load();

        setTitle("Expense Tracker");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
        refreshTableAndSummary();
    }

    private void initUI() {
        // Top toolbar
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton addBtn = new JButton("Add Expense");
        JButton delBtn = new JButton("Delete Expense");
        JButton saveBtn = new JButton("Save");
        top.add(addBtn);
        top.add(delBtn);
        top.add(saveBtn);

        top.add(new JLabel(" Month: "));
        monthBox = new JComboBox<String>();
        for (int m = 1; m <= 12; m++) monthBox.addItem(String.format("%02d", m));
        monthBox.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        top.add(monthBox);

        top.add(new JLabel(" Year: "));
        yearBox = new JComboBox<String>();
        int currentYear = LocalDate.now().getYear();
        for (int y = currentYear - 5; y <= currentYear + 1; y++) yearBox.addItem(String.valueOf(y));
        yearBox.setSelectedItem(String.valueOf(currentYear));
        top.add(yearBox);

        JButton refreshBtn = new JButton("Refresh");
        top.add(refreshBtn);

        // Table
        String[] cols = new String[] {"ID", "Date", "Category", "Notes", "Amount"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(360);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.setDefaultRenderer(Object.class, new CategoryColorRenderer());

        JScrollPane tableScroll = new JScrollPane(table);

        // Right panel: summary + chart
        JPanel right = new JPanel();
        right.setPreferredSize(new Dimension(320, 0));
        right.setLayout(new BorderLayout(8, 8));
        JPanel summary = new JPanel();
        summary.setLayout(new BoxLayout(summary, BoxLayout.Y_AXIS));
        summary.setBorder(BorderFactory.createTitledBorder("Monthly Summary"));

        totalLabel = new JLabel("Total: ₹0.00");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        summary.add(Box.createVerticalStrut(8));
        summary.add(totalLabel);
        summary.add(Box.createVerticalStrut(8));

        chartPanel = new PieChartPanel();
        chartPanel.setPreferredSize(new Dimension(300, 300));
        summary.add(chartPanel);

        right.add(summary, BorderLayout.NORTH);

        // Layout
        add(top, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(right, BorderLayout.EAST);

        // Actions
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { openAddDialog(); }
        });
        delBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) { JOptionPane.showMessageDialog(ExpenseTrackerGUI.this, "Select a row to delete."); return; }
                int id = (Integer) tableModel.getValueAt(row, 0);
                int opt = JOptionPane.showConfirmDialog(ExpenseTrackerGUI.this, "Delete expense ID " + id + " ?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION) {
                    manager.removeExpenseById(id);
                    refreshTableAndSummary();
                }
            }
        });
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { manager.save(); JOptionPane.showMessageDialog(ExpenseTrackerGUI.this, "Saved."); }
        });
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { refreshTableAndSummary(); }
        });

        monthBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { refreshTableAndSummary(); }
        });
        yearBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { refreshTableAndSummary(); }
        });
    }

    private void openAddDialog() {
        JPanel p = new JPanel(new GridLayout(5, 2, 6, 6));
        JTextField amountField = new JTextField();
        JComboBox<String> catBox = new JComboBox<String>(DEFAULT_CATEGORIES);
        JTextField dateField = new JTextField(LocalDate.now().toString());
        JTextField notesField = new JTextField();

        p.add(new JLabel("Amount (₹):"));
        p.add(amountField);
        p.add(new JLabel("Category:"));
        p.add(catBox);
        p.add(new JLabel("Date (YYYY-MM-DD):"));
        p.add(dateField);
        p.add(new JLabel("Notes:"));
        p.add(notesField);

        int opt = JOptionPane.showConfirmDialog(this, p, "Add Expense", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            String amtStr = amountField.getText().trim();
            String cat = (String) catBox.getSelectedItem();
            String dateStr = dateField.getText().trim();
            String notes = notesField.getText().trim();

            double amt;
            try {
                amt = Double.parseDouble(amtStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount.");
                return;
            }

            LocalDate date;
            try {
                date = LocalDate.parse(dateStr);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date. Use YYYY-MM-DD.");
                return;
            }

            manager.addExpense(amt, cat, date, notes);
            refreshTableAndSummary();
        }
    }

    private void refreshTableAndSummary() {
        tableModel.setRowCount(0);
        int year = Integer.parseInt((String) yearBox.getSelectedItem());
        int month = Integer.parseInt((String) monthBox.getSelectedItem());

        List<Expense> list = manager.getExpensesForMonth(year, month);
        for (Expense e : list) {
            tableModel.addRow(new Object[] { e.getId(), e.getDate().toString(), e.getCategory(), e.getNotes(), e.getAmount() });
        }

        double total = manager.getTotalForMonth(year, month);
        totalLabel.setText(String.format("Total: \u20B9%.2f", total));

        Map<String, Double> catTotals = manager.getCategoryTotalsForMonth(year, month);
        chartPanel.updateData(catTotals);
    }

    // Renderer to color categories
    private class CategoryColorRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String category = (String) table.getValueAt(row, 2);
            if (!isSelected) {
                if ("Food".equalsIgnoreCase(category)) c.setBackground(new Color(255, 245, 238));
                else if ("Travel".equalsIgnoreCase(category)) c.setBackground(new Color(235, 245, 255));
                else if ("Bills".equalsIgnoreCase(category)) c.setBackground(new Color(245, 255, 235));
                else if ("Shopping".equalsIgnoreCase(category)) c.setBackground(new Color(255, 240, 255));
                else if ("Entertainment".equalsIgnoreCase(category)) c.setBackground(new Color(255, 250, 230));
                else if ("Health".equalsIgnoreCase(category)) c.setBackground(new Color(235, 255, 250));
                else c.setBackground(Color.WHITE);
            } else {
                c.setBackground(new Color(173, 216, 230));
            }
            return c;
        }
    }

    // Simple pie chart panel
    private static class PieChartPanel extends JPanel {
        private Map<String, Double> data;

        public PieChartPanel() {
            this.data = new java.util.LinkedHashMap<String, Double>();
        }

        public void updateData(Map<String, Double> map) {
            this.data = map;
            repaint();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (data == null || data.isEmpty()) {
                g2.setColor(Color.DARK_GRAY);
                g2.drawString("No data for this month", 20, 20);
                return;
            }

            int width = Math.min(getWidth(), getHeight()) - 20;
            int x = (getWidth() - width) / 2;
            int y = 10;
            double total = 0;
            for (double v : data.values()) total += v;

            // colors
            Color[] palette = new Color[] {
                new Color(102,165,199),
                new Color(244,180,109),
                new Color(196,120,171),
                new Color(120,198,134),
                new Color(239,128,128),
                new Color(170,170,170),
                new Color(169,204,227)
            };

            double start = 0;
            int i = 0;
            for (Map.Entry<String, Double> e : data.entrySet()) {
                double value = e.getValue();
                double angle = value / total * 360.0;
                g2.setColor(palette[i % palette.length]);
                g2.fillArc(x, y, width, width, (int) Math.round(start), (int) Math.round(angle));
                start += angle;
                i++;
            }

            // Legend
            int lx = 10;
            int ly = y + width + 10;
            i = 0;
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            for (Map.Entry<String, Double> e : data.entrySet()) {
                g2.setColor(palette[i % palette.length]);
                g2.fillRect(lx, ly, 12, 12);
                g2.setColor(Color.BLACK);
                String label = String.format("%s: \u20B9%.2f", e.getKey(), e.getValue());
                g2.drawString(label, lx + 18, ly + 11);
                ly += 18;
                i++;
            }
        }
    }
}
