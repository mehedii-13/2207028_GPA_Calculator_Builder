package com.example._207028_gpa_calculator_builder;

import javafx.beans.property.*;

public class AddCourse {
    private final StringProperty courseName;
    private final StringProperty courseCode;
    private final DoubleProperty courseCredit;
    private final StringProperty teacher1Name;
    private final StringProperty teacher2Name;
    private final StringProperty grade;
    private final DoubleProperty gradePoint;

    public AddCourse() {
        this.courseName = new SimpleStringProperty();
        this.courseCode = new SimpleStringProperty();
        this.courseCredit = new SimpleDoubleProperty();
        this.teacher1Name = new SimpleStringProperty();
        this.teacher2Name = new SimpleStringProperty();
        this.grade = new SimpleStringProperty();
        this.gradePoint = new SimpleDoubleProperty();
    }

    public AddCourse(String courseName, String courseCode, double courseCredit,
                     String teacher1Name, String teacher2Name, String grade) {
        this();
        setCourseName(courseName);
        setCourseCode(courseCode);
        setCourseCredit(courseCredit);
        setTeacher1Name(teacher1Name);
        setTeacher2Name(teacher2Name);
        setGrade(grade);
        setGradePoint(getGradePointValue(grade));
    }


    public String getCourseName() {
        return courseName.get();
    }

    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode.get();
    }

    public void setCourseCode(String courseCode) {
        this.courseCode.set(courseCode);
    }

    public StringProperty courseCodeProperty() {
        return courseCode;
    }

    public double getCourseCredit() {
        return courseCredit.get();
    }

    public void setCourseCredit(double courseCredit) {
        this.courseCredit.set(courseCredit);
    }

    public DoubleProperty courseCreditProperty() {
        return courseCredit;
    }

    public String getTeacher1Name() {
        return teacher1Name.get();
    }

    public void setTeacher1Name(String teacher1Name) {
        this.teacher1Name.set(teacher1Name);
    }

    public StringProperty teacher1NameProperty() {
        return teacher1Name;
    }

    public String getTeacher2Name() {
        return teacher2Name.get();
    }

    public void setTeacher2Name(String teacher2Name) {
        this.teacher2Name.set(teacher2Name);
    }

    public StringProperty teacher2NameProperty() {
        return teacher2Name;
    }


    public String getGrade() {
        return grade.get();
    }

    public void setGrade(String grade) {
        this.grade.set(grade);
        setGradePoint(getGradePointValue(grade));
    }

    public StringProperty gradeProperty() {
        return grade;
    }

    public double getGradePoint() {
        return gradePoint.get();
    }

    public void setGradePoint(double gradePoint) {
        this.gradePoint.set(gradePoint);
    }

    public DoubleProperty gradePointProperty() {
        return gradePoint;
    }

    private double getGradePointValue(String grade) {
        if (grade == null) return 0.0;
        
        switch (grade.toUpperCase()) {
            case "A+": return 4.0;
            case "A": return 3.75;
            case "A-": return 3.5;
            case "B+": return 3.25;
            case "B": return 3.0;
            case "B-": return 2.75;
            case "C+": return 2.5;
            case "C": return 2.25;
            case "C-": return 2.0;
            case "D+": return 1.75;
            case "D": return 1.5;
            case "F": return 0.0;
            default: return 0.0;
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s - %.2f credits", 
                           getCourseName(), getCourseCode(), getGrade(), getCourseCredit());
    }
}
