package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Student class:
 * - Inherits Person
 * - Holds either manual CGPA or list of Results for computed CGPA
 * - Demonstrates encapsulation, overriding, and overloading.
 */
public class Student extends Person implements Serializable, Comparable<Student> {
    private Double manualCgpa = null; // if set, use manual CGPA
    private final List<Result> results = new ArrayList<>();

    public Student() { super(); }
    public Student(String id, String firstName, String lastName, Double manualCgpa) {
        super(id, firstName, lastName);
        this.manualCgpa = manualCgpa;
    }

    // Results management for computed version
    public void addResult(Result r) { if (r != null) results.add(r); }
    public List<Result> getResults() { return new ArrayList<>(results); }

    public Double getManualCgpa() { return manualCgpa; }
    public void setManualCgpa(Double cgpa) { this.manualCgpa = cgpa; }

    /** Compute weighted CGPA from results (if any), else return manualCgpa or 0.0 */
    public double getComputedCgpa() {
        if (!results.isEmpty()) {
            double totalCredit = 0; double totalPoints = 0;
            for (Result r : results) {
                totalCredit += r.getCredit();
                totalPoints += r.gradePoint() * r.getCredit();
            }
            return totalCredit == 0 ? 0.0 : totalPoints / totalCredit;
        }
        return manualCgpa == null ? 0.0 : manualCgpa;
    }

    @Override
    public int compareTo(Student o) {
        int cmp = this.getLastName().compareToIgnoreCase(o.getLastName());
        if (cmp != 0) return cmp;
        cmp = this.getFirstName().compareToIgnoreCase(o.getFirstName());
        if (cmp != 0) return cmp;
        return this.getId().compareToIgnoreCase(o.getId());
    }
}