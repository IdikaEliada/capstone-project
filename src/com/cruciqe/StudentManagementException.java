package com.cruciqe;

import java.util.ArrayList;

public // Custom exception
class StudentManagementException extends Exception {
    public StudentManagementException(String message) {
        super(message);
    }
}

// Data manager class (simplified for UI focus)
class StudentDataManager {
    private ArrayList<Student> students;
    private ArrayList<Course> courses;
    
    public StudentDataManager() {
        students = new ArrayList<>();
        courses = new ArrayList<>();
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        // Sample courses
        courses.add(new Course("MATH202", "Differential Equation", 2));
        courses.add(new Course("INS204", "System Design and Analysis", 3));
        courses.add(new Course("COS202", "Computer Programming 2", 3));
        courses.add(new Course("ILS204", "Information and Literacy Skills", 1));
        courses.add(new Course("GST212", "Logic and Philosophy", 2));
        
        // Sample students
        Student s1 = new Student("Chinazum", "20231392382", "200 Level", "Cyber security");
        s1.addResult(courses.get(0), 85.0);
        s1.addResult(courses.get(1), 92.0);
        students.add(s1);
        
        Student s2 = new Student("Nwafor Victor Chinedu", "20231389027", "200 Level", "Cyber Security");
        s2.addResult(courses.get(0), 78.0);
        s2.addResult(courses.get(1), 88.0);
        students.add(s2);
        
        Student s3 = new Student("Mike Johnson", "STU003", "400 Level", "Information Technology");
        s3.addResult(courses.get(0), 95.0);
        students.add(s3);
    }
    
    public void addStudent(Student student) throws StudentManagementException {
        if (findStudentById(student.getId()) != null) {
            throw new StudentManagementException("Student with ID " + student.getId() + " already exists!");
        }
        students.add(student);
    }
    
    public Student findStudentById(String id) {
        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(id)) {
                return student;
            }
        }
        return null;
    }
    
    public boolean removeStudent(String id) {
        Student student = findStudentById(id);
        if (student != null) {
            students.remove(student);
            return true;
        }
        return false;
    }
    
    public ArrayList<Student> getStudents() { return new ArrayList<>(students); }
    public ArrayList<Course> getCourses() { return new ArrayList<>(courses); }
}

