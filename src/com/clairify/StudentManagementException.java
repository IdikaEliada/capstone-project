package com.clairify;

import java.util.ArrayList;

public class StudentManagementException extends Exception {
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
        Student s1 = new Student("Hezekiah-Okezu Chukwuoma Emmanuel", "20231398022", "200 Level", "Cyber Security");
        s1.addResult(courses.get(0), 85.0);
        s1.addResult(courses.get(1), 92.0);
        students.add(s1);

        Student s2 = new Student("Ibe Bruno Okechukwu", "20231391192", "200 Level", "Cyber Security");
        s2.addResult(courses.get(0), 78.0);
        s2.addResult(courses.get(1), 88.0);
        students.add(s2);

        Student s3 = new Student("Ibe Obioma Lucky", "20231388112", "200 Level", "Cyber Security");
        s3.addResult(courses.get(0), 95.0);
        students.add(s3);

        Student s4 = new Student("Ibra Handsome Prosper Emelike", "20231367242", "200 Level", "Cyber Security");
        s4.addResult(courses.get(0), 85.0);
        students.add(s4);

        Student s5 = new Student("IfenyiChukwu Precious Adaeze", "20231364342", "200 Level", "Cyber Security");
        s5.addResult(courses.get(0), 78.0);
        students.add(s5);

        Student s6 = new Student("Igwe Chisom Precious", "20231380582", "200 Level", "Cyber Security");
        s6.addResult(courses.get(0), 92.0);
        students.add(s6);

        Student s7 = new Student("Ihe Chibuike Victor", "20231393542", "200 Level", "Cyber Security");
        s7.addResult(courses.get(0), 88.0);
        students.add(s7);

        Student s8 = new Student("Ihenagaram Praise Jachima", "20231409542", "200 Level", "Cyber Security");
        s8.addResult(courses.get(0), 85.0);
        students.add(s8);

        Student s9 = new Student("Ikechukwu Marvellous Chibuzo", "20242492522", "200 Level", "Cyber Security");
        s9.addResult(courses.get(0), 78.0);
        students.add(s9);

        Student s10 = new Student("Ikeama Tobenna Princelynn", "20231385502", "200 Level", "Cyber Security");
        s10.addResult(courses.get(0), 92.0);
        students.add(s10);

        Student s11 = new Student("Ilorah Sharon Eberechukwu", "20231399332", "200 Level", "Cyber Security");
        s11.addResult(courses.get(0), 88.0);
        students.add(s11);
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

    public ArrayList<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public ArrayList<Course> getCourses() {
        return new ArrayList<>(courses);
    }
}
