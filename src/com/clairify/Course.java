package com.clairify;

public class Course {
  private String courseCode;
  private String courseName;
  private int creditHours;
  
  public Course(String courseCode, String courseName, int creditHours) {
    this.courseCode = courseCode;
    this.courseName = courseName;
    this.creditHours = creditHours;
  }
  
  public String getCourseCode() { return courseCode; }
  public String getCourseName() { return courseName; }
  public int getCreditHours() { return creditHours; }
  
  @Override
  public String toString() {
      return courseCode + " - " + courseName + " (" + creditHours + " credits)";
  }
}
