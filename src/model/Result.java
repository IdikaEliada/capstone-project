package model;

import java.io.Serializable;

/** Result for a student in a course. */
public class Result implements Serializable {
    private String courseCode;
    private double score;
    private double credit;

    public Result() {}
    public Result(String courseCode, double score, double credit) {
        this.courseCode = courseCode; this.score = score; this.credit = credit;
    }

    public String getCourseCode() { return courseCode; }
    public double getScore() { return score; }
    public double getCredit() { return credit; }

    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setScore(double score) { this.score = score; }
    public void setCredit(double credit) { this.credit = credit; }

    public double gradePoint() {
        if (score >= 70) return 5.0;
        if (score >= 60) return 4.0;
        if (score >= 50) return 3.0;
        if (score >= 45) return 2.0;
        if (score >= 40) return 1.0;
        return 0.0;
    }
}