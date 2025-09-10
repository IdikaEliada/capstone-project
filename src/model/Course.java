package model;

import java.io.Serializable;

/** Course entity used by computed version. */
public class Course implements Serializable {
    private String code;
    private String title;
    private double credit;

    public Course() {}

    public Course(String code, String title, double credit) {
        setCode(code);
        setTitle(title);
        setCredit(credit);
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code == null ? null : code.trim().toUpperCase(); }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title == null ? "" : title.trim(); }

    public double getCredit() { return credit; }
    public void setCredit(double credit) { this.credit = credit; }

    @Override public String toString() { return code + " - " + title + " (" + credit + "cr)"; }
}