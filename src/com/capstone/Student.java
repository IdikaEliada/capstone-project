package com.capstone;

import java.io.Serializable;
import java.util.ArrayList;

abstract class Person {
    protected String name;
    protected String reg;
    
    public Person(String name, String reg) {
        this.name = name;
        this.reg = reg;
    }
    
    // Abstract method to be implemented by subclasses
    public abstract String getDisplayInfo();
    
    // Getters and setters
    public String getName() { return name; }
    public String getreg() { return reg; }
    public void setName(String name) { this.name = name; }
    public void setreg(String reg) { this.reg = reg; }
}

interface Sortable {
    int compareByName(Sortable other);
    int compareByCGPA(Sortable other);
    int compareById(Sortable other);
}

public class Student extends Person implements Sortable, Serializable {

    private String level;
    private String department;
    private ArrayList<Result> results;
    private double cgpa;
    
    public Student(String name, String reg, String level, String department) {
        super(name, reg);
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
                           reg, name, cgpa, department);
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
            return this.reg.compareToIgnoreCase(((Student) other).reg);
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
