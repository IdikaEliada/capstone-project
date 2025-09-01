package com.capstone;

public class Course {
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
