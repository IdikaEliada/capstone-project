// Smart Student Platform - Complete Implementation
// File: SmartStudentPlatform.java

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

//import java.util.List;

// Abstract base class for Person
abstract class Person {
    protected String name;
    protected String id;
    
    public Person(String name, String id) {
        this.name = name;
        this.id = id;
    }
    
    // Abstract method to be implemented by subclasses
    public abstract String getDisplayInfo();
    
    // Getters and setters
    public String getName() { return name; }
    public String getId() { return id; }
    public void setName(String name) { this.name = name; }
    public void setId(String id) { this.id = id; }
}

// Interface for sortable objects
interface Sortable {
    int compareByName(Sortable other);
    int compareByCGPA(Sortable other);
    int compareById(Sortable other);
}

// Course class
class Course {
    private String courseCode;
    private String courseName;
    private int creditHours;
    
    public Course(String courseCode, String courseName, int creditHours) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.creditHours = creditHours;
    }
    
    // Getters and setters
    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public int getCreditHours() { return creditHours; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public void setCreditHours(int creditHours) { this.creditHours = creditHours; }
    
    @Override
    public String toString() {
        return courseCode + " - " + courseName + " (" + creditHours + " credits)";
    }
}

// Result class
class Result {
    private Course course;
    private double grade;
    private String letterGrade;
    
    public Result(Course course, double grade) {
        this.course = course;
        this.grade = grade;
        this.letterGrade = calculateLetterGrade(grade);
    }
    
    private String calculateLetterGrade(double grade) {
        if (grade >= 90) return "A";
        else if (grade >= 80) return "B";
        else if (grade >= 70) return "C";
        else if (grade >= 60) return "D";
        else return "F";
    }
    
    // Getters and setters
    public Course getCourse() { return course; }
    public double getGrade() { return grade; }
    public String getLetterGrade() { return letterGrade; }
    public void setGrade(double grade) { 
        this.grade = grade; 
        this.letterGrade = calculateLetterGrade(grade);
    }
}

// Student class extending Person and implementing Sortable
class Student extends Person implements Sortable, Serializable {
    private String level;
    private String department;
    private ArrayList<Result> results;
    private double cgpa;
    
    public Student(String name, String id, String level, String department) {
        super(name, id);
        this.level = level;
        this.department = department;
        this.results = new ArrayList<>();
        this.cgpa = 0.0;
    }
    
    // Method overloading - different ways to add results
    public void addResult(Course course, double grade) {
        results.add(new Result(course, grade));
        calculateCGPA();
    }
    
    public void addResult(Result result) {
        results.add(result);
        calculateCGPA();
    }
    
    // Calculate CGPA based on all results
    private void calculateCGPA() {
        if (results.isEmpty()) {
            cgpa = 0.0;
            return;
        }
        
        double totalPoints = 0;
        int totalCreditHours = 0;
        
        for (Result result : results) {
            double gradePoints = convertToGradePoints(result.getGrade());
            int credits = result.getCourse().getCreditHours();
            totalPoints += gradePoints * credits;
            totalCreditHours += credits;
        }
        
        cgpa = totalCreditHours > 0 ? totalPoints / totalCreditHours : 0.0;
    }
    
    private double convertToGradePoints(double grade) {
        if (grade >= 70) return 5.0;
        else if (grade >= 55) return 4.0;
        else if (grade >= 40) return 3.0;
        else if (grade >= 30) return 2.0;
        else if (grade >= 20) return 1.0;
        else return 0.0;
    }
    
    // Override abstract method from Person
    @Override
    public String getDisplayInfo() {
        return String.format("ID: %s, Name: %s, CGPA: %.2f, Department: %s", 
                           id, name, cgpa, department);
    }
    
    // Implement Sortable interface methods
    @Override
    public int compareByName(Sortable other) {
        if (other instanceof Student) {
            return this.name.compareToIgnoreCase(((Student) other).name);
        }
        return 0;
    }
    
    @Override
    public int compareByCGPA(Sortable other) {
        if (other instanceof Student) {
            return Double.compare(((Student) other).cgpa, this.cgpa); // Descending order
        }
        return 0;
    }
    
    @Override
    public int compareById(Sortable other) {
        if (other instanceof Student) {
            return this.id.compareToIgnoreCase(((Student) other).id);
        }
        return 0;
    }
    
    // Getters and setters
    public String getlevel() { return level; }
    public String getDepartment() { return department; }
    public ArrayList<Result> getResults() { return results; }
    public double getCgpa() { return cgpa; }
    public void setlevel(String level) { this.level = level; }
    public void setDepartment(String department) { this.department = department; }
}

// Custom exception for student management
class StudentManagementException extends Exception {
    public StudentManagementException(String message) {
        super(message);
    }
}

// Data manager class for handling file operations and data management
class StudentDataManager {
    private ArrayList<Student> students;
    private ArrayList<Course> courses;
    private final String DATA_FILE = "students.dat";
    //private final String COURSES_FILE = "courses.dat";
    
    public StudentDataManager() {
        students = new ArrayList<>();
        courses = new ArrayList<>();
        initializeSampleCourses();
        loadData();
    }
    
    private void initializeSampleCourses() {
        courses.add(new Course("MATH202", "Differential Equation", 2));
        courses.add(new Course("INS204", "System Deesign and Analysis", 3));
        courses.add(new Course("COS202", "Computer Programming 2", 3));
        courses.add(new Course("ILS204", "Information and Literacy Skills", 1));
        courses.add(new Course("GST212", "Logic and Philosophy", 2));
        courses.add(new Course("GET204", "Student Workshop Practice", 2));
        courses.add(new Course("IFT212", "Computer Organization and Architecture", 2));
        courses.add(new Course("SOE202", "Software Requirement Modeling", 2));
        courses.add(new Course("SOE206", "Introduction to Embedded Systems", 2));
        courses.add(new Course("ENT212", "Entreprenuership Studies", 1));
    }
    
    // Add student with validation
    public void addStudent(Student student) throws StudentManagementException {
        // Check for duplicate ID
        if (findStudentById(student.getId()) != null) {
            throw new StudentManagementException("Student with ID " + student.getId() + " already exists!");
        }
        students.add(student);
        saveData();
    }
    
    // Update student information
    public void updateStudent(String id, Student updatedStudent) throws StudentManagementException {
        Student existingStudent = findStudentById(id);
        if (existingStudent == null) {
            throw new StudentManagementException("Student with ID " + id + " not found!");
        }
        
        int index = students.indexOf(existingStudent);
        students.set(index, updatedStudent);
        saveData();
    }
    
    // Remove student
    public boolean removeStudent(String id) {
        Student student = findStudentById(id);
        if (student != null) {
            students.remove(student);
            saveData();
            return true;
        }
        return false;
    }
    
    // Linear search by ID
    public Student findStudentById(String id) {
        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(id)) {
                return student;
            }
        }
        return null;
    }
    
    // Binary search by ID (requires sorted list)
    public Student binarySearchById(String id) {
        // First sort by ID
        quickSortById(0, students.size() - 1);
        
        int left = 0, right = students.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = students.get(mid).getId().compareToIgnoreCase(id);
            
            if (comparison == 0) {
                return students.get(mid);
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }
    
    // QuickSort implementation for sorting by name
    public void quickSortByName(int low, int high) {
        if (low < high) {
            int pi = partitionByName(low, high);
            quickSortByName(low, pi - 1);
            quickSortByName(pi + 1, high);
        }
    }
    
    private int partitionByName(int low, int high) {
        String pivot = students.get(high).getName();
        int i = (low - 1);
        
        for (int j = low; j < high; j++) {
            if (students.get(j).getName().compareToIgnoreCase(pivot) <= 0) {
                i++;
                Collections.swap(students, i, j);
            }
        }
        Collections.swap(students, i + 1, high);
        return i + 1;
    }
    
    // QuickSort by ID
    private void quickSortById(int low, int high) {
        if (low < high) {
            int pi = partitionById(low, high);
            quickSortById(low, pi - 1);
            quickSortById(pi + 1, high);
        }
    }
    
    private int partitionById(int low, int high) {
        String pivot = students.get(high).getId();
        int i = (low - 1);
        
        for (int j = low; j < high; j++) {
            if (students.get(j).getId().compareToIgnoreCase(pivot) <= 0) {
                i++;
                Collections.swap(students, i, j);
            }
        }
        Collections.swap(students, i + 1, high);
        return i + 1;
    }
    
    // Bubble Sort implementation for sorting by CGPA
    public void bubbleSortByCGPA() {
        int n = students.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (students.get(j).getCgpa() < students.get(j + 1).getCgpa()) {
                    Collections.swap(students, j, j + 1);
                }
            }
        }
    }
    
    // Calculate class statistics
    public Map<String, Double> calculateClassStatistics() {
        Map<String, Double> stats = new HashMap<>();
        
        if (students.isEmpty()) {
            stats.put("average", 0.0);
            stats.put("highest", 0.0);
            stats.put("lowest", 0.0);
            return stats;
        }
        
        double sum = 0, highest = students.get(0).getCgpa(), lowest = students.get(0).getCgpa();
        
        for (Student student : students) {
            double cgpa = student.getCgpa();
            sum += cgpa;
            if (cgpa > highest) highest = cgpa;
            if (cgpa < lowest) lowest = cgpa;
        }
        
        stats.put("average", sum / students.size());
        stats.put("highest", highest);
        stats.put("lowest", lowest);
        
        return stats;
    }
    
    // Get top performer
    public Student getTopPerformer() {
        if (students.isEmpty()) return null;
        
        Student topStudent = students.get(0);
        for (Student student : students) {
            if (student.getCgpa() > topStudent.getCgpa()) {
                topStudent = student;
            }
        }
        return topStudent;
    }
    
    // File operations
    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(students);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            students = (ArrayList<Student>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Data file not found. Starting with empty list.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
    
    // Getters
    public ArrayList<Student> getStudents() { return new ArrayList<>(students); }
    public ArrayList<Course> getCourses() { return new ArrayList<>(courses); }
}

// Main GUI class
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
        String[] columnNames = {"Student ID", "Name", "level", "Department", "CGPA"};
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
                student.getId(),
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
                     topPerformer.getName(), topPerformer.getId(), topPerformer.getCgpa()));
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
        JTextField idField = new JTextField(student.getId(), 15);
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
                Student updatedStudent = new Student(name, student.getId(), level, department);
                // Copy existing results
                for (Result result : student.getResults()) {
                    updatedStudent.addResult(result);
                }
                
                dataManager.updateStudent(student.getId(), updatedStudent);
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
        dialog.add(new JLabel(student.getName() + " (" + student.getId() + ")"), gbc);
        
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
                dataManager.updateStudent(student.getId(), student);
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
                students.sort((s1, s2) -> s1.getId().compareToIgnoreCase(s2.getId()));
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
        infoPanel.add(new JLabel(student.getId()), gbc);
        
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
//