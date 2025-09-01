package com.capstone;

// import com.capstone.Student;
// import com.capstone.StudentManagementException;
import java.io.*;
import java.util.*;

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
        if (findStudentById(student.getreg()) != null) {
            throw new StudentManagementException("Student with ID " + student.getreg() + " already exists!");
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
            if (student.getreg().equalsIgnoreCase(id)) {
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
            int comparison = students.get(mid).getreg().compareToIgnoreCase(id);
            
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
        String pivot = students.get(high).getreg();
        int i = (low - 1);
        
        for (int j = low; j < high; j++) {
            if (students.get(j).getreg().compareToIgnoreCase(pivot) <= 0) {
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

