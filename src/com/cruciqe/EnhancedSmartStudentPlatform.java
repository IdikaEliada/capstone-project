package com.cruciqe;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

class ModernButton extends JButton {
    private Color defaultColor = new Color(52, 152, 219);
    private Color hoverColor = new Color(41, 128, 185);
    private boolean isHovered = false;
    
    public ModernButton(String text) {
        super(text);
        setUI(new ModernButtonUI());
        setupButton();
    }
    
    private void setupButton() {
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Color bgColor = isHovered ? hoverColor : defaultColor;
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        
        super.paintComponent(g);
        g2.dispose();
    }
    
    public void setButtonColor(Color defaultColor, Color hoverColor) {
        this.defaultColor = defaultColor;
        this.hoverColor = hoverColor;
        repaint();
    }
}

class ModernButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
    @Override
    public void paint(Graphics g, JComponent c) {
        // Custom painting is handled in ModernButton
        super.paint(g, c);
    }
}

// Custom Table Renderer
class ModernTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (isSelected) {
            c.setBackground(new Color(52, 152, 219, 100));
            c.setForeground(Color.BLACK);
        } else {
            if (row % 2 == 0) {
                c.setBackground(Color.WHITE);
            } else {
                c.setBackground(new Color(248, 249, 250));
            }
            c.setForeground(Color.BLACK);
        }
        
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));
        
        return c;
    }
}



public class EnhancedSmartStudentPlatform extends JFrame {
    private StudentDataManager dataManager;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> sortComboBox;
    private JPanel statsPanel;
    private JLabel totalStudentsLabel, avgCgpaLabel, topPerformerLabel;
    
    // Colors
    private final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private final Color SECONDARY_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private final Color CARD_COLOR = Color.WHITE;
    
    public EnhancedSmartStudentPlatform() {
        dataManager = new StudentDataManager();
        initializeModernGUI();
        loadStudentData();
        updateStatistics();
    }
    
    private void initializeModernGUI() {
        setTitle("Smart Student Platform - Modern Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        createModernHeader();
        createModernMainPanel();
        createModernControlPanel();
        createModernStatsPanel();
        
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));
        setVisible(true);
    }
    
    private void createModernHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        // Title
        JLabel titleLabel = new JLabel("Smart Student Platform", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Comprehensive Student Management System", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(PRIMARY_COLOR);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private void createModernMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create table with modern styling
        String[] columnNames = {"Student ID", "Name", "Level", "Department", "CGPA", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        studentTable = new JTable(tableModel);
        setupModernTable();
        
        // Table container with shadow effect
        JPanel tableContainer = createCardPanel();
        tableContainer.setLayout(new BorderLayout());
        
        JLabel tableTitle = new JLabel("Student Records");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tableContainer.add(tableTitle, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(tableContainer, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupModernTable() {
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        studentTable.setRowHeight(40);
        studentTable.setGridColor(new Color(220, 220, 220));
        studentTable.setSelectionBackground(new Color(52, 152, 219, 100));
        studentTable.setSelectionForeground(Color.BLACK);
        studentTable.setShowGrid(true);
        studentTable.setIntercellSpacing(new Dimension(0, 1));
        
        // Custom header
        JTableHeader header = studentTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 45));
        header.setBorder(BorderFactory.createEmptyBorder());
        
        // Set custom renderer
        for (int i = 0; i < studentTable.getColumnCount(); i++) {
            studentTable.getColumnModel().getColumn(i).setCellRenderer(new ModernTableCellRenderer());
        }
        
        // Column widths
        int[] columnWidths = {100, 200, 100, 200, 80, 100};
        for (int i = 0; i < columnWidths.length; i++) {
            studentTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
        
        // Double-click listener
        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewStudentDetails();
                }
            }
        });
    }
    
    private void createModernControlPanel() {
        JPanel controlPanel = createCardPanel();
        controlPanel.setLayout(new GridBagLayout());
        controlPanel.setPreferredSize(new Dimension(300, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Control panel title
        JLabel controlTitle = new JLabel("Actions & Controls");
        controlTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.insets = new Insets(15, 20, 20, 20);
        gbc.anchor = GridBagConstraints.WEST;
        controlPanel.add(controlTitle, gbc);
        
        // Student Management Section
        gbc.gridwidth = 2; gbc.gridy++;
        gbc.insets = new Insets(0, 20, 10, 20);
        JLabel studentMgmtLabel = new JLabel("Student Management");
        studentMgmtLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        studentMgmtLabel.setForeground(PRIMARY_COLOR);
        controlPanel.add(studentMgmtLabel, gbc);
        
        // Add Student Button
        ModernButton addStudentBtn = new ModernButton("Add Student");
        addStudentBtn.setButtonColor(SECONDARY_COLOR, new Color(39, 174, 96));
        addStudentBtn.addActionListener(e -> showAddStudentDialog());
        gbc.gridy++; gbc.insets = new Insets(5, 20, 5, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        controlPanel.add(addStudentBtn, gbc);
        
        // Update Student Button
        ModernButton updateStudentBtn = new ModernButton("Update Student");
        updateStudentBtn.setButtonColor(WARNING_COLOR, new Color(212, 172, 13));
        updateStudentBtn.addActionListener(e -> showUpdateStudentDialog());
        gbc.gridy++;
        controlPanel.add(updateStudentBtn, gbc);
        
        // Remove Student Button
        ModernButton removeStudentBtn = new ModernButton("Remove Student");
        removeStudentBtn.setButtonColor(DANGER_COLOR, new Color(192, 57, 43));
        removeStudentBtn.addActionListener(e -> removeSelectedStudent());
        gbc.gridy++;
        controlPanel.add(removeStudentBtn, gbc);
        
        // Add Result Button
        ModernButton addResultBtn = new ModernButton("Add Result");
        addResultBtn.addActionListener(e -> showAddResultDialog());
        gbc.gridy++;
        controlPanel.add(addResultBtn, gbc);
        
        // Search Section
        gbc.gridy++; gbc.insets = new Insets(20, 20, 10, 20);
        JLabel searchLabel = new JLabel("Search & Sort");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setForeground(PRIMARY_COLOR);
        controlPanel.add(searchLabel, gbc);
        
        // Search Field
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridy++; gbc.insets = new Insets(5, 20, 5, 20);
        controlPanel.add(searchField, gbc);
        
        // Search Button
        ModernButton searchBtn = new ModernButton("Search by ID");
        searchBtn.addActionListener(e -> performSearch());
        gbc.gridy++;
        controlPanel.add(searchBtn, gbc);
        
        // Sort Combo
        String[] sortOptions = {"Name", "CGPA", "ID"};
        sortComboBox = new JComboBox<>(sortOptions);
        sortComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy++; gbc.insets = new Insets(10, 20, 5, 20);
        controlPanel.add(sortComboBox, gbc);
        
        // Sort Button
        ModernButton sortBtn = new ModernButton("Sort Students");
        sortBtn.addActionListener(e -> performSort());
        gbc.gridy++; gbc.insets = new Insets(5, 20, 5, 20);
        controlPanel.add(sortBtn, gbc);
        
        // Refresh Button
        ModernButton refreshBtn = new ModernButton("Refresh Data");
        refreshBtn.setButtonColor(new Color(155, 89, 182), new Color(142, 68, 173));
        refreshBtn.addActionListener(e -> {
            loadStudentData();
            updateStatistics();
            JOptionPane.showMessageDialog(this, "Data refreshed successfully!", "Refresh", JOptionPane.INFORMATION_MESSAGE);
        });
        gbc.gridy++; gbc.insets = new Insets(20, 20, 20, 20);
        controlPanel.add(refreshBtn, gbc);
        
        add(controlPanel, BorderLayout.WEST);
    }
    
    private void createModernStatsPanel() {
        statsPanel = createCardPanel();
        statsPanel.setLayout(new GridLayout(1, 3, 20, 0));
        statsPanel.setPreferredSize(new Dimension(0, 120));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Total Students Card
        totalStudentsLabel = createStatCard("Total Students", "0", SECONDARY_COLOR);
        statsPanel.add(totalStudentsLabel);
        
        // Average CGPA Card
        avgCgpaLabel = createStatCard("Average CGPA", "0.00", PRIMARY_COLOR);
        statsPanel.add(avgCgpaLabel);
        
        // Top Performer Card
        topPerformerLabel = createStatCard("Top Performer", "None", WARNING_COLOR);
        statsPanel.add(topPerformerLabel);
        
        add(statsPanel, BorderLayout.SOUTH);
    }
    
    private JLabel createStatCard(String title, String value, Color color) {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(color);
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 15, 10));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        JLabel containerLabel = new JLabel();
        containerLabel.setLayout(new BorderLayout());
        containerLabel.add(card);
        
        return containerLabel;
    }
    
    private JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return panel;
    }
    
    private void loadStudentData() {
        tableModel.setRowCount(0);
        
        for (Student student : dataManager.getStudents()) {
            String status = student.getCgpa() >= 3.5 ? "Excellent" : 
                           student.getCgpa() >= 2.5 ? "Good" : "Needs Improvement";
            
            Object[] rowData = {
                student.getId(),
                student.getName(),
                student.getLevel(),
                student.getDepartment(),
                String.format("%.2f", student.getCgpa()),
                status
            };
            tableModel.addRow(rowData);
        }
    }
    
    private void updateStatistics() {
        List<Student> students = dataManager.getStudents();
        int totalStudents = students.size();
        
        // Update total students
        Component totalCard = ((JLabel) totalStudentsLabel).getComponent(0);
        JLabel totalValue = (JLabel) ((JPanel) totalCard).getComponent(1);
        totalValue.setText(String.valueOf(totalStudents));
        
        if (totalStudents > 0) {
            // Calculate average CGPA
            double avgCgpa = students.stream()
                    .mapToDouble(Student::getCgpa)
                    .average()
                    .orElse(0.0);
            
            Component avgCard = ((JLabel) avgCgpaLabel).getComponent(0);
            JLabel avgValue = (JLabel) ((JPanel) avgCard).getComponent(1);
            avgValue.setText(String.format("%.2f", avgCgpa));
            
            // Find top performer
            Student topPerformer = students.stream()
                    .max((s1, s2) -> Double.compare(s1.getCgpa(), s2.getCgpa()))
                    .orElse(null);
            
            Component topCard = ((JLabel) topPerformerLabel).getComponent(0);
            JLabel topValue = (JLabel) ((JPanel) topCard).getComponent(1);
            if (topPerformer != null) {
                topValue.setText(topPerformer.getName());
            }
        }
    }
    
    // Dialog and action methods (simplified for brevity)
    private void showAddStudentDialog() {
        JDialog dialog = createModernDialog("Add New Student");
        
        JTextField idField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField levelField = new JTextField(15);
        JTextField departmentField = new JTextField(15);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        addFormField(formPanel, gbc, "Student ID:", idField, 0);
        addFormField(formPanel, gbc, "Name:", nameField, 1);
        addFormField(formPanel, gbc, "Level:", levelField, 2);
        addFormField(formPanel, gbc, "Department:", departmentField, 3);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        ModernButton saveBtn = new ModernButton("Save Student");
        saveBtn.setButtonColor(SECONDARY_COLOR, new Color(39, 174, 96));
        saveBtn.addActionListener(e -> {
            try {
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                String level = levelField.getText().trim();
                String department = departmentField.getText().trim();
                
                if (id.isEmpty() || name.isEmpty() || level.isEmpty() || department.isEmpty()) {
                    throw new StudentManagementException("All fields are required!");
                }
                
                Student student = new Student(name, id, level, department);
                dataManager.addStudent(student);
                loadStudentData();
                updateStatistics();
                dialog.dispose();
                
                showSuccessMessage("Student added successfully!");
                
            } catch (StudentManagementException ex) {
                showErrorMessage(ex.getMessage());
            }
        });
        
        ModernButton cancelBtn = new ModernButton("Cancel");
        cancelBtn.setButtonColor(new Color(149, 165, 166), new Color(127, 140, 141));
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.WEST;
        JLabel lblField = new JLabel(label);
        lblField.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lblField, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        if (field instanceof JTextField) {
            ((JTextField) field).setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
        }
        panel.add(field, gbc);
    }
    
    private JDialog createModernDialog(String title) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);
        
        // Add title bar
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(PRIMARY_COLOR);
        titlePanel.setPreferredSize(new Dimension(0, 50));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        dialog.add(titlePanel, BorderLayout.NORTH);
        
        return dialog;
    }
    
    private void showUpdateStudentDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarningMessage("Please select a student to update.");
            return;
        }
        
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        Student student = dataManager.findStudentById(studentId);
        
        if (student == null) {
            showErrorMessage("Student not found!");
            return;
        }
        
        JDialog dialog = createModernDialog("Update Student Information");
        
        JTextField idField = new JTextField(student.getId(), 15);
        idField.setEditable(false);
        idField.setBackground(new Color(240, 240, 240));
        JTextField nameField = new JTextField(student.getName(), 15);
        JTextField levelField = new JTextField(student.getLevel(), 15);
        JTextField departmentField = new JTextField(student.getDepartment(), 15);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        addFormField(formPanel, gbc, "Student ID:", idField, 0);
        addFormField(formPanel, gbc, "Name:", nameField, 1);
        addFormField(formPanel, gbc, "Level:", levelField, 2);
        addFormField(formPanel, gbc, "Department:", departmentField, 3);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        
        ModernButton updateBtn = new ModernButton("Update Student");
        updateBtn.setButtonColor(WARNING_COLOR, new Color(212, 172, 13));
        updateBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String level = levelField.getText().trim();
                String department = departmentField.getText().trim();
                
                if (name.isEmpty() || level.isEmpty() || department.isEmpty()) {
                    throw new StudentManagementException("All fields are required!");
                }
                
                // Create updated student with same results
                Student updatedStudent = new Student(name, student.getId(), level, department);
                for (Result result : student.getResults()) {
                    updatedStudent.addResult(result);
                }
                
                // Update in data manager
                dataManager.removeStudent(student.getId());
                dataManager.addStudent(updatedStudent);
                
                loadStudentData();
                updateStatistics();
                dialog.dispose();
                
                showSuccessMessage("Student updated successfully!");
                
            } catch (StudentManagementException ex) {
                showErrorMessage(ex.getMessage());
            }
        });
        
        ModernButton cancelBtn = new ModernButton("Cancel");
        cancelBtn.setButtonColor(new Color(149, 165, 166), new Color(127, 140, 141));
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(updateBtn);
        buttonPanel.add(cancelBtn);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void removeSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarningMessage("Please select a student to remove.");
            return;
        }
        
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        String studentName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to remove student:\n" + studentName + " (ID: " + studentId + ")?",
            "Confirm Removal",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            if (dataManager.removeStudent(studentId)) {
                loadStudentData();
                updateStatistics();
                showSuccessMessage("Student removed successfully!");
            } else {
                showErrorMessage("Failed to remove student!");
            }
        }
    }
    
    private void showAddResultDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarningMessage("Please select a student to add results for.");
            return;
        }
        
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        Student student = dataManager.findStudentById(studentId);
        
        if (student == null) {
            showErrorMessage("Student not found!");
            return;
        }
        
        JDialog dialog = createModernDialog("Add Course Result");
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Student info display
        JLabel studentInfo = new JLabel("Student: " + student.getName() + " (" + student.getId() + ")");
        studentInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        studentInfo.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(studentInfo, gbc);
        
        // Course selection
        JComboBox<Course> courseComboBox = new JComboBox<>();
        for (Course course : dataManager.getCourses()) {
            courseComboBox.addItem(course);
        }
        courseComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Grade input
        JTextField gradeField = new JTextField(15);
        
        gbc.gridwidth = 1;
        addFormField(formPanel, gbc, "Course:", courseComboBox, 1);
        addFormField(formPanel, gbc, "Grade (0-100):", gradeField, 2);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        
        ModernButton addBtn = new ModernButton("Add Result");
        addBtn.setButtonColor(SECONDARY_COLOR, new Color(39, 174, 96));
        addBtn.addActionListener(e -> {
            try {
                String gradeText = gradeField.getText().trim();
                if (gradeText.isEmpty()) {
                    throw new StudentManagementException("Grade is required!");
                }
                
                double grade = Double.parseDouble(gradeText);
                if (grade < 0 || grade > 100) {
                    throw new StudentManagementException("Grade must be between 0 and 100!");
                }
                
                Course selectedCourse = (Course) courseComboBox.getSelectedItem();
                student.addResult(selectedCourse, grade);
                
                loadStudentData();
                updateStatistics();
                dialog.dispose();
                
                showSuccessMessage("Result added successfully!\nNew CGPA: " + 
                    String.format("%.2f", student.getCgpa()));
                
            } catch (NumberFormatException ex) {
                showErrorMessage("Please enter a valid numeric grade!");
            } catch (StudentManagementException ex) {
                showErrorMessage(ex.getMessage());
            }
        });
        
        ModernButton cancelBtn = new ModernButton("Cancel");
        cancelBtn.setButtonColor(new Color(149, 165, 166), new Color(127, 140, 141));
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void performSearch() {
        String searchId = searchField.getText().trim();
        if (searchId.isEmpty()) {
            showWarningMessage("Please enter a student ID to search for.");
            return;
        }
        
        Student student = dataManager.findStudentById(searchId);
        if (student != null) {
            // Highlight the found student in the table
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(searchId)) {
                    studentTable.setRowSelectionInterval(i, i);
                    studentTable.scrollRectToVisible(studentTable.getCellRect(i, 0, true));
                    break;
                }
            }
            
            showSuccessMessage("Student found: " + student.getDisplayInfo());
        } else {
            showErrorMessage("Student with ID '" + searchId + "' not found.");
        }
    }
    
    private void performSort() {
        String selectedSort = (String) sortComboBox.getSelectedItem();
        List<Student> students = dataManager.getStudents();
        
        if (students.isEmpty()) {
            showWarningMessage("No students to sort!");
            return;
        }
        
        switch (selectedSort) {
            case "Name":
                students.sort((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
                break;
            case "CGPA":
                students.sort((s1, s2) -> Double.compare(s2.getCgpa(), s1.getCgpa()));
                break;
            case "ID":
                students.sort((s1, s2) -> s1.getId().compareToIgnoreCase(s2.getId()));
                break;
        }
        
        loadStudentData();
        showSuccessMessage("Students sorted by " + selectedSort);
    }
    
    private void viewStudentDetails() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        Student student = dataManager.findStudentById(studentId);
        if (student == null) return;
        
        JDialog dialog = createModernDialog("Student Details");
        dialog.setSize(600, 500);
        
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBackground(Color.BLACK);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Student info panel
        JPanel infoPanel = createCardPanel();
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        addInfoRow(infoPanel, gbc, "Student Reg:", student.getId(), 0);
        addInfoRow(infoPanel, gbc, "Name:", student.getName(), 1);
        addInfoRow(infoPanel, gbc, "Level:", student.getLevel(), 2);
        addInfoRow(infoPanel, gbc, "Department:", student.getDepartment(), 3);
        addInfoRow(infoPanel, gbc, "CGPA:", String.format("%.2f", student.getCgpa()), 4);
        
        contentPanel.add(infoPanel, BorderLayout.NORTH);
        
        // Results table
        if (!student.getResults().isEmpty()) {
            String[] resultColumns = {"Course Code", "Course Name", "Credits", "Grade", "Letter"};
            DefaultTableModel resultModel = new DefaultTableModel(resultColumns, 0);
            
            for (Result result : student.getResults()) {
                Object[] rowData = {
                    result.getCourse().getCourseCode(),
                    result.getCourse().getCourseName(),
                    result.getCourse().getCreditHours(),
                    String.format("%.1f", result.getGrade()),
                    result.getLetterGrade()
                };
                resultModel.addRow(rowData);
            }
            
            JTable resultTable = new JTable(resultModel);
            setupResultTable(resultTable);
            
            JScrollPane scrollPane = new JScrollPane(resultTable);
            scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                "Course Results",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY_COLOR
            ));
            
            contentPanel.add(scrollPane, BorderLayout.CENTER);
        } else {
            JLabel noResultsLabel = new JLabel("No results available for this student.", SwingConstants.CENTER);
            noResultsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            noResultsLabel.setForeground(new Color(127, 140, 141));
            contentPanel.add(noResultsLabel, BorderLayout.CENTER);
        }
        
        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        ModernButton closeBtn = new ModernButton("Close");
        closeBtn.setButtonColor(new Color(149, 165, 166), new Color(127, 140, 141));
        closeBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeBtn);
        
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(contentPanel, BorderLayout.CENTER);
        
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void addInfoRow(JPanel panel, GridBagConstraints gbc, String label, String value, int row) {
    gbc.gridx = 0; gbc.gridy = row;
    JLabel lblField = new JLabel(label);
    lblField.setFont(new Font("Segoe UI", Font.BOLD, 12));
    lblField.setForeground(Color.BLACK);
    panel.add(lblField, gbc);
        
    gbc.gridx = 1;
    JLabel valueField = new JLabel(value);
    valueField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    valueField.setForeground(Color.BLACK);
    panel.add(valueField, gbc);
    }
    
    private void setupResultTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setRowHeight(30);
        table.setGridColor(new Color(220, 220, 220));
        table.setEnabled(false);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 35));
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new ModernTableCellRenderer());
        }
    }
    
    // Message display methods
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    // Main method
    
}

