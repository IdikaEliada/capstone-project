package com.capstone;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class SmartStudentPlatform extends JFrame implements ActionListener {
    private StudentDataManager dataManager;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> sortComboBox;
    private JTextArea statsArea;
    
    // Menu items
    private JMenuItem addStudentItem, updateStudentItem, removeStudentItem, addResultItem;
    private JMenuItem exitItem, aboutItem;
    
    public SmartStudentPlatform() {
        dataManager = new StudentDataManager();
        initializeGUI();
        loadStudentData();
    }
    
    private void initializeGUI() {
        setTitle("Smart Student Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create menu bar
        createMenuBar();
        
        // Create main panels
        createMainPanel();
        createToolPanel();
        createStatsPanel();
        
        // Set window properties
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        addStudentItem = new JMenuItem("Add Student");
        updateStudentItem = new JMenuItem("Update Student");
        removeStudentItem = new JMenuItem("Remove Student");
        addResultItem = new JMenuItem("Add Result");
        exitItem = new JMenuItem("Exit");
        
        addStudentItem.addActionListener(this);
        updateStudentItem.addActionListener(this);
        removeStudentItem.addActionListener(this);
        addResultItem.addActionListener(this);
        exitItem.addActionListener(this);
        
        fileMenu.add(addStudentItem);
        fileMenu.add(updateStudentItem);
        fileMenu.add(removeStudentItem);
        fileMenu.addSeparator();
        fileMenu.add(addResultItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(this);
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }
    
    private void createMainPanel() {
        // Create table model
        String[] columnNames = {"Student Reg  Number", "Name", "level", "Department", "CGPA"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.getTableHeader().setReorderingAllowed(false);
        
        // Add mouse listener for double-click to view details
        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewStudentDetails();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void createToolPanel() {
        JPanel toolPanel = new JPanel(new FlowLayout());
        
        // Search components
        toolPanel.add(new JLabel("Search by ID:"));
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        JButton binarySearchButton = new JButton("Binary Search");
        searchButton.addActionListener(this);
        binarySearchButton.addActionListener(this);
        
        toolPanel.add(searchField);
        toolPanel.add(searchButton);
        toolPanel.add(binarySearchButton);
        
        // Sort components
        toolPanel.add(new JLabel("Sort by:"));
        String[] sortOptions = {"Name (Quick Sort)", "CGPA (Bubble Sort)", "ID"};
        sortComboBox = new JComboBox<>(sortOptions);
        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(this);
        
        toolPanel.add(sortComboBox);
        toolPanel.add(sortButton);
        
        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(this);
        toolPanel.add(refreshButton);
        
        add(toolPanel, BorderLayout.NORTH);
    }
    
    private void createStatsPanel() {
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBorder(BorderFactory.createTitledBorder("Class Statistics"));
        
        statsArea = new JTextArea(5, 30);
        statsArea.setEditable(false);
        statsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane statsScrollPane = new JScrollPane(statsArea);
        statsPanel.add(statsScrollPane, BorderLayout.CENTER);
        
        JButton updateStatsButton = new JButton("Update Statistics");
        updateStatsButton.addActionListener(this);
        statsPanel.add(updateStatsButton, BorderLayout.SOUTH);
        
        add(statsPanel, BorderLayout.SOUTH);
        updateStatistics();
    }
    
    private void loadStudentData() {
        tableModel.setRowCount(0); // Clear existing data
        
        for (Student student : dataManager.getStudents()) {
            Object[] rowData = {
                student.getreg(),
                student.getName(),
                student.getlevel(),
                student.getDepartment(),
                String.format("%.2f", student.getCgpa())
            };
            tableModel.addRow(rowData);
        }
    }
    
    private void updateStatistics() {
        Map<String, Double> stats = dataManager.calculateClassStatistics();
        Student topPerformer = dataManager.getTopPerformer();
        
        StringBuilder sb = new StringBuilder();
        sb.append("Class Statistics:\n");
        sb.append("================\n");
        sb.append(String.format("Total Students: %d\n", dataManager.getStudents().size()));
        sb.append(String.format("Average CGPA: %.2f\n", stats.get("average")));
        sb.append(String.format("Highest CGPA: %.2f\n", stats.get("highest")));
        sb.append(String.format("Lowest CGPA: %.2f\n", stats.get("lowest")));
        
        if (topPerformer != null) {
            sb.append(String.format("Top Performer: %s (ID: %s, CGPA: %.2f)\n", 
                     topPerformer.getName(), topPerformer.getreg(), topPerformer.getCgpa()));
        }
        
        statsArea.setText(sb.toString());
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String command = e.getActionCommand();
            
            switch (command) {
                case "Add Student":
                    showAddStudentDialog();
                    break;
                case "Update Student":
                    showUpdateStudentDialog();
                    break;
                case "Remove Student":
                    removeSelectedStudent();
                    break;
                case "Add Result":
                    showAddResultDialog();
                    break;
                case "Search":
                    performLinearSearch();
                    break;
                case "Binary Search":
                    performBinarySearch();
                    break;
                case "Sort":
                    performSort();
                    break;
                case "Refresh":
                    loadStudentData();
                    break;
                case "Update Statistics":
                    updateStatistics();
                    break;
                case "Exit":
                    System.exit(0);
                    break;
                case "About":
                    showAboutDialog();
                    break;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "An error occurred: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showAddStudentDialog() {
        JDialog dialog = new JDialog(this, "Add New Student", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Input fields
        JTextField idField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField levelField = new JTextField(15);
        JTextField departmentField = new JTextField(15);
        
        // Layout components
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        dialog.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("level:"), gbc);
        gbc.gridx = 1;
        dialog.add(levelField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        dialog.add(departmentField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(ae -> {
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
                
                JOptionPane.showMessageDialog(this, 
                    "Student added successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (StudentManagementException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(ae -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dialog.add(buttonPanel, gbc);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void showUpdateStudentDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a student to update.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        Student student = dataManager.findStudentById(studentId);
        
        if (student == null) {
            JOptionPane.showMessageDialog(this, 
                "Student not found!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog(this, "Update Student", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Pre-populate fields with existing data
        JTextField idField = new JTextField(student.getreg(), 15);
        idField.setEditable(false); // Don't allow ID changes
        JTextField nameField = new JTextField(student.getName(), 15);
        JTextField levelField = new JTextField(student.getlevel(), 15);
        JTextField departmentField = new JTextField(student.getDepartment(), 15);
        
        // Layout similar to add dialog
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        dialog.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("level:"), gbc);
        gbc.gridx = 1;
        dialog.add(levelField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        dialog.add(departmentField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton updateButton = new JButton("Update");
        JButton cancelButton = new JButton("Cancel");
        
        updateButton.addActionListener(ae -> {
            try {
                String name = nameField.getText().trim();
                String level = levelField.getText().trim();
                String department = departmentField.getText().trim();
                
                if (name.isEmpty() || level.isEmpty() || department.isEmpty()) {
                    throw new StudentManagementException("All fields are required!");
                }
                
                // Create updated student with same ID and results
                Student updatedStudent = new Student(name, student.getreg(), level, department);
                // Copy existing results
                for (Result result : student.getResults()) {
                    updatedStudent.addResult(result);
                }
                
                dataManager.updateStudent(student.getreg(), updatedStudent);
                loadStudentData();
                updateStatistics();
                dialog.dispose();
                
                JOptionPane.showMessageDialog(this, 
                    "Student updated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (StudentManagementException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(ae -> dialog.dispose());
        
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dialog.add(buttonPanel, gbc);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void removeSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a student to remove.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        String studentName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to remove student: " + studentName + " (ID: " + studentId + ")?", 
            "Confirm Removal", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            if (dataManager.removeStudent(studentId)) {
                loadStudentData();
                updateStatistics();
                JOptionPane.showMessageDialog(this, 
                    "Student removed successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to remove student!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showAddResultDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a student to add results for.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        Student student = dataManager.findStudentById(studentId);
        
        if (student == null) {
            JOptionPane.showMessageDialog(this, 
                "Student not found!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog(this, "Add Result for " + student.getName(), true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Course selection
        JComboBox<Course> courseComboBox = new JComboBox<>();
        for (Course course : dataManager.getCourses()) {
            courseComboBox.addItem(course);
        }
        
        // Grade input
        JTextField gradeField = new JTextField(10);
        
        // Layout components
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1;
        dialog.add(new JLabel(student.getName() + " (" + student.getreg() + ")"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        dialog.add(courseComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Grade (0-100):"), gbc);
        gbc.gridx = 1;
        dialog.add(gradeField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Result");
        JButton cancelButton = new JButton("Cancel");
        
        addButton.addActionListener(ae -> {
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
                
                // Update the student in the data manager
                dataManager.updateStudent(student.getreg(), student);
                loadStudentData();
                updateStatistics();
                dialog.dispose();
                
                JOptionPane.showMessageDialog(this, 
                    "Result added successfully!\nNew CGPA: " + String.format("%.2f", student.getCgpa()), 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Please enter a valid numeric grade!", 
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (StudentManagementException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(ae -> dialog.dispose());
        
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dialog.add(buttonPanel, gbc);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void performLinearSearch() {
        String searchId = searchField.getText().trim();
        if (searchId.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a student ID to search for.", 
                "Empty Search", JOptionPane.WARNING_MESSAGE);
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
            
            JOptionPane.showMessageDialog(this, 
                "Student found using Linear Search:\n" + student.getDisplayInfo(), 
                "Search Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Student with ID '" + searchId + "' not found.", 
                "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void performBinarySearch() {
        String searchId = searchField.getText().trim();
        if (searchId.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a student ID to search for.", 
                "Empty Search", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Student student = dataManager.binarySearchById(searchId);
        if (student != null) {
            // Reload data since binary search sorts the list
            loadStudentData();
            
            // Highlight the found student
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(searchId)) {
                    studentTable.setRowSelectionInterval(i, i);
                    studentTable.scrollRectToVisible(studentTable.getCellRect(i, 0, true));
                    break;
                }
            }
            
            JOptionPane.showMessageDialog(this, 
                "Student found using Binary Search:\n" + student.getDisplayInfo() + 
                "\n\nNote: List has been sorted by ID for binary search.", 
                "Search Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            loadStudentData(); // Reload since list was sorted
            JOptionPane.showMessageDialog(this, 
                "Student with ID '" + searchId + "' not found.", 
                "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void performSort() {
        String selectedSort = (String) sortComboBox.getSelectedItem();
        ArrayList<Student> students = dataManager.getStudents();
        
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No students to sort!", 
                "Empty List", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        long startTime = System.currentTimeMillis();
        
        switch (selectedSort) {
            case "Name (Quick Sort)":
                dataManager.quickSortByName(0, students.size() - 1);
                break;
            case "CGPA (Bubble Sort)":
                dataManager.bubbleSortByCGPA();
                break;
            case "ID":
                // Simple sort by ID using Collections.sort
                students.sort((s1, s2) -> s1.getreg().compareToIgnoreCase(s2.getreg()));
                break;
        }
        
        long endTime = System.currentTimeMillis();
        
        loadStudentData();
        
        JOptionPane.showMessageDialog(this, 
            "Students sorted by " + selectedSort + "\nTime taken: " + (endTime - startTime) + " ms", 
            "Sort Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void viewStudentDetails() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        Student student = dataManager.findStudentById(studentId);
        
        if (student == null) return;
        
        JDialog dialog = new JDialog(this, "Student Details", true);
        dialog.setLayout(new BorderLayout());
        
        // Student info panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(student.getreg()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(student.getName()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("level:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(student.getlevel()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(student.getDepartment()), gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        infoPanel.add(new JLabel("CGPA:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(String.format("%.2f", student.getCgpa())), gbc);
        
        dialog.add(infoPanel, BorderLayout.NORTH);
        
        // Results table
        if (!student.getResults().isEmpty()) {
            String[] resultColumns = {"Course Code", "Course Name", "Credits", "Grade", "Letter Grade"};
            DefaultTableModel resultTableModel = new DefaultTableModel(resultColumns, 0);
            
            for (Result result : student.getResults()) {
                Object[] rowData = {
                    result.getCourse().getCourseCode(),
                    result.getCourse().getCourseName(),
                    result.getCourse().getCreditHours(),
                    String.format("%.1f", result.getGrade()),
                    result.getLetterGrade()
                };
                resultTableModel.addRow(rowData);
            }
            
            JTable resultTable = new JTable(resultTableModel);
            resultTable.setEnabled(false);
            JScrollPane resultScrollPane = new JScrollPane(resultTable);
            resultScrollPane.setPreferredSize(new Dimension(500, 200));
            resultScrollPane.setBorder(BorderFactory.createTitledBorder("Course Results"));
            
            dialog.add(resultScrollPane, BorderLayout.CENTER);
        } else {
            JLabel noResultsLabel = new JLabel("No results available for this student.", JLabel.CENTER);
            dialog.add(noResultsLabel, BorderLayout.CENTER);
        }
        
        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(ae -> dialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void showAboutDialog() {
        String aboutText = "Smart Student Platform v1.0\n\n" +
                          "A comprehensive student management system built with Java Swing.\n\n" +
                          "Features:\n" +
                          "• Student record management (Add, Update, Remove)\n" +
                          "• Course result tracking with CGPA calculation\n" +
                          "• Multiple sorting algorithms (QuickSort, BubbleSort)\n" +
                          "• Linear and Binary search functionality\n" +
                          "• Class statistics and performance analytics\n" +
                          "• Data persistence with file I/O\n" +
                          "• Complete exception handling\n\n" +
                          "Built using Object-Oriented Programming principles\n" +
                          "with proper inheritance, interfaces, and polymorphism.\n\n" +
                          "© 2024 Smart Student Platform";
        
        JOptionPane.showMessageDialog(this, aboutText, "About", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Main method to run the application
    public static void main(String[] args) {
        // Set Look and Feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set look and feel: " + e.getMessage());
        }
        
        // Create and run the application on the EDT
        SwingUtilities.invokeLater(() -> {
            new SmartStudentPlatform();
        });
    }
}

