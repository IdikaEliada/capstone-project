package com.clairify;
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
        if (grade >= 70) return "A";
        else if (grade >= 60) return "B";
        else if (grade >= 50) return "C";
        else if (grade >= 40) return "D";
        else return "F";
    }
    
    public Course getCourse() { return course; }
    public double getGrade() { return grade; }
    public String getLetterGrade() { return letterGrade; }
}

