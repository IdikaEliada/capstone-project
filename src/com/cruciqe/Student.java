package com.cruciqe;

import java.io.Serializable;
import java.util.ArrayList;

abstract class Person {
    protected String name;
    protected String id;
    
    public Person(String name, String id) {
        this.name = name;
        this.id = id;
    }
    
    public abstract String getDisplayInfo();
    
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

public class Student extends Person implements Sortable, Serializable {
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
  
  public void addResult(Course course, double grade) {
      results.add(new Result(course, grade));
      calculateCGPA();
  }
  
  public void addResult(Result result) {
      results.add(result);
      calculateCGPA();
  }
  
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
  
  @Override
  public String getDisplayInfo() {
      return String.format("REG: %s, Name: %s, CGPA: %.2f, Department: %s",   id, name, cgpa, department);
  }
  
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
          return Double.compare(((Student) other).cgpa, this.cgpa);
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
  
  // Getters
  public String getLevel() { return level; }
  public String getDepartment() { return department; }
  public ArrayList<Result> getResults() { return results; }
  public double getCgpa() { return cgpa; }


}
