package ui;

import model.Student;
import service.StudentService;

import javax.swing.*;
import ui.validation.FieldValidators;
import ui.theme.Styles;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;

/** Smart Student Platform – Manual CGPA UI (with validation, styles, print). */
public class StudentAppManual extends JFrame {
    private final StudentService service;
    private final StudentTableModel tableModel;
    private final JTable table;

    // form fields
    private final JTextField idField = new JTextField(12);
    private final JTextField fnField = new JTextField(14);
    private final JTextField lnField = new JTextField(14);
    private final JTextField cgpaField = new JTextField(8);

    private final JComboBox<String> sortCombo =
            new JComboBox<>(new String[]{"Name (QuickSort)", "CGPA (Bubble)", "ID"});

    public StudentAppManual(StudentService service) {
        super("Smart Student Platform — Manual CGPA");
        Styles.applyGlobalNimbus();                 // start in Light mode
        getContentPane().setBackground(Styles.BG);

        this.service = service;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 640);
        setLocationRelativeTo(null);

        tableModel = new StudentTableModel(service.getStudents());
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                if (r >= 0) {
                    Student s = service.getStudents().get(r);
                    idField.setText(s.getId());
                    fnField.setText(s.getFirstName());
                    lnField.setText(s.getLastName());
                    cgpaField.setText(String.format("%.2f", s.getComputedCgpa()));
                }
            }
        });
        Styles.styleTable(table);

        // ---------- Left: FORM ----------
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.anchor = GridBagConstraints.WEST;
        g.gridx = 0; g.gridy = 0; formGrid.add(label("ID"), g);
        g.gridx = 1; formGrid.add(styleField(idField), g);
        g.gridx = 0; g.gridy = 1; formGrid.add(label("First Name"), g);
        g.gridx = 1; formGrid.add(styleField(fnField), g);
        g.gridx = 0; g.gridy = 2; formGrid.add(label("Last Name"), g);
        g.gridx = 1; formGrid.add(styleField(lnField), g);
        g.gridx = 0; g.gridy = 3; formGrid.add(label("CGPA"), g);
        g.gridx = 1; formGrid.add(styleField(cgpaField), g);

        JPanel formCard = Styles.cardPanel("Student Details", formGrid);

        // ---------- Right: TABLE ----------
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.getViewport().setBackground(Styles.CARD);
        JPanel tableCard = Styles.cardPanel("Students", tableScroll);

        // ---------- Actions row ----------
        JButton addBtn = new JButton("Add Student");
        JButton updateBtn = new JButton("Update");
        JButton summaryBtn = new JButton("Summaries");
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");
        JButton searchBtn = new JButton("Search ID");
        JButton printBtn = new JButton("Print Table");    // ✅ Print only

        Styles.stylePrimary(addBtn);
        Styles.stylePrimary(updateBtn);
        Styles.styleSubtle(summaryBtn);
        Styles.styleSubtle(saveBtn);
        Styles.styleSubtle(loadBtn);
        Styles.styleSubtle(searchBtn);
        Styles.styleSubtle(printBtn);
        sortCombo.setFont(Styles.BASE_FONT);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actions.setOpaque(false);
        actions.add(new JLabel("Sort:"));
        actions.add(sortCombo);
        actions.add(summaryBtn);
        actions.add(saveBtn);
        actions.add(loadBtn);
        actions.add(searchBtn);
        actions.add(printBtn);
        actions.add(addBtn);
        actions.add(updateBtn);

        JPanel actionsCard = Styles.cardPanel(null, actions);

        // ---------- Layout ----------
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints r = new GridBagConstraints();
        r.insets = new Insets(12, 12, 12, 12);
        r.fill = GridBagConstraints.BOTH;
        r.weighty = 1;

        r.gridx = 0; r.gridy = 0; r.weightx = 0.38; content.add(formCard, r);
        r.gridx = 1; r.gridy = 0; r.weightx = 0.62; content.add(tableCard, r);

        add(content, BorderLayout.CENTER);
        add(actionsCard, BorderLayout.SOUTH);

        // ---------- Listeners ----------
        addBtn.addActionListener(e -> onAdd());
        updateBtn.addActionListener(e -> onUpdate());
        summaryBtn.addActionListener(e -> onSummary());
        saveBtn.addActionListener(e -> onSave());
        loadBtn.addActionListener(e -> onLoad());
        searchBtn.addActionListener(e -> onSearch());
        printBtn.addActionListener(e -> onPrint());

        sortCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String sel = (String) e.getItem();
                if (sel.contains("Name")) service.quickSortByName();
                else if (sel.contains("CGPA")) service.bubbleSortByCgpaDesc();
                else service.sortById();
                refresh();
            }
        });

        // validators
        attachValidation();
    }

    // ---------- UI helpers ----------
    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Styles.BASE_FONT);
        l.setForeground(Styles.MUTED);
        return l;
    }
    private JTextField styleField(JTextField f) {
        Styles.styleField(f); return f;
    }

    // ---------- Actions ----------
    private void onAdd() {
        if (!validateAllInputs()) return;
        try {
            String id = idField.getText().trim();
            String fn = fnField.getText().trim();
            String ln = lnField.getText().trim();
            double cgpa = Double.parseDouble(cgpaField.getText().trim());
            Student s = new Student(id, fn, ln, cgpa);
            if (service.addStudent(s)) {
                refresh();
                JOptionPane.showMessageDialog(this, "Student added successfully.");
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void onUpdate() {
        if (!validateAllInputs()) return;
        String id = idField.getText().trim();
        Student s = service.findById(id);
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Student not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            s.setFirstName(fnField.getText().trim());
            s.setLastName(lnField.getText().trim());
            s.setManualCgpa(Double.parseDouble(cgpaField.getText().trim()));
            refresh();
            JOptionPane.showMessageDialog(this, "Updated");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please input numbers for CGPA (0.00–5.00).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void onSummary() {
        double avg = service.classAverageScore();
        Student top = service.topPerformer();
        String msg = "Class average CGPA: " + String.format("%.2f", avg);
        if (top != null) msg += "\nTop: " + top.getId() + " - " + top.getFullName()
                + " (" + String.format("%.2f", top.getComputedCgpa()) + ")";
        JOptionPane.showMessageDialog(this, msg, "Summaries", JOptionPane.INFORMATION_MESSAGE);
    }
    private void onSave() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try { service.saveToFile(fc.getSelectedFile().getAbsolutePath());
                  JOptionPane.showMessageDialog(this, "Saved"); }
            catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        }
    }
    private void onLoad() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try { service.loadFromFile(fc.getSelectedFile().getAbsolutePath());
                  refresh(); JOptionPane.showMessageDialog(this, "Loaded"); }
            catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        }
    }
    private void onSearch() {
        String id = JOptionPane.showInputDialog(this, "Enter ID to search:");
        if (id != null) {
            Student s1 = service.findById(id);
            Student s2 = service.binarySearchById(id);
            String msg = "Linear => " + (s1 == null ? "Not found" : s1.getFullName())
                    + "\nBinary => " + (s2 == null ? "Not found" : s2.getFullName());
            JOptionPane.showMessageDialog(this, msg, "Search", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void onPrint() {
        try {
            boolean done = table.print(JTable.PrintMode.FIT_WIDTH,
                    new MessageFormat("Student Records"),
                    new MessageFormat("Page {0}"));
            if (done) JOptionPane.showMessageDialog(this, "Printed/Saved successfully.");
            else JOptionPane.showMessageDialog(this, "Printing canceled.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refresh() {
        tableModel.setData(service.getStudents());
    }

    // ---------- Validation ----------
    private void attachValidation() {
        FieldValidators.applyAlpha(fnField);
        FieldValidators.applyAlpha(lnField);
        FieldValidators.applyNumeric(idField, false);
        FieldValidators.applyNumeric(cgpaField, true);

        fnField.setInputVerifier(new InputVerifier() {
            @Override public boolean verify(JComponent c) {
                return FieldValidators.requireAlpha(c, (JTextField)c, "First Name");
            }
        });
        lnField.setInputVerifier(new InputVerifier() {
            @Override public boolean verify(JComponent c) {
                return FieldValidators.requireAlpha(c, (JTextField)c, "Last Name");
            }
        });
        idField.setInputVerifier(new InputVerifier() {
            @Override public boolean verify(JComponent c) {
                JTextField f = (JTextField)c;
                String t = f.getText().trim();
                if (t.isEmpty()) return FieldValidators.requireNotEmpty(f, f, "ID");
                if (!t.matches("\\d+")) { FieldValidators.showError(f, "Please input numbers for ID."); return false; }
                return true;
            }
        });
        cgpaField.setInputVerifier(new InputVerifier() {
            @Override public boolean verify(JComponent c) {
                return FieldValidators.requireNumericRange(c, (JTextField)c, "CGPA", 0.0, 5.0);
            }
        });
    }

    private boolean validateAllInputs() {
        if (!FieldValidators.requireNotEmpty(idField, idField, "ID")) return false;
        if (!idField.getText().trim().matches("\\d+")) {
            FieldValidators.showError(idField, "Please input numbers for ID.");
            idField.requestFocus();
            return false;
        }
        if (!FieldValidators.requireNotEmpty(fnField, fnField, "First Name")) return false;
        if (!FieldValidators.requireAlpha(fnField, fnField, "First Name")) return false;
        if (!FieldValidators.requireNotEmpty(lnField, lnField, "Last Name")) return false;
        if (!FieldValidators.requireAlpha(lnField, lnField, "Last Name")) return false;
        if (!FieldValidators.requireNotEmpty(cgpaField, cgpaField, "CGPA")) return false;
        if (!FieldValidators.requireNumericRange(cgpaField, cgpaField, "CGPA", 0.0, 5.0)) return false;
        return true;
    }
}