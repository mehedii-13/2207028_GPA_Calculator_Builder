package com.example._207028_gpa_calculator_builder;

import javafx.beans.property.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CalculationHistory {
    private final IntegerProperty id;
    private final DoubleProperty cgpa;
    private final DoubleProperty totalCredits;
    private final IntegerProperty courseCount;
    private final StringProperty calculationDate;
    private final StringProperty notes;
    private final StringProperty coursesJson;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public CalculationHistory() {
        this.id = new SimpleIntegerProperty();
        this.cgpa = new SimpleDoubleProperty();
        this.totalCredits = new SimpleDoubleProperty();
        this.courseCount = new SimpleIntegerProperty();
        this.calculationDate = new SimpleStringProperty();
        this.notes = new SimpleStringProperty();
        this.coursesJson = new SimpleStringProperty("");
    }
    
    public CalculationHistory(double cgpa, double totalCredits, int courseCount, String notes) {
        this();
        this.cgpa.set(cgpa);
        this.totalCredits.set(totalCredits);
        this.courseCount.set(courseCount);
        this.calculationDate.set(LocalDateTime.now().format(DATE_FORMATTER));
        this.notes.set(notes);
    }
    
    public CalculationHistory(double cgpa, double totalCredits, int courseCount, String notes, String coursesJson) {
        this();
        this.cgpa.set(cgpa);
        this.totalCredits.set(totalCredits);
        this.courseCount.set(courseCount);
        this.calculationDate.set(LocalDateTime.now().format(DATE_FORMATTER));
        this.notes.set(notes);
        this.coursesJson.set(coursesJson);
    }
    
    public CalculationHistory(int id, double cgpa, double totalCredits, int courseCount, String calculationDate, String notes) {
        this();
        this.id.set(id);
        this.cgpa.set(cgpa);
        this.totalCredits.set(totalCredits);
        this.courseCount.set(courseCount);
        this.calculationDate.set(calculationDate);
        this.notes.set(notes);
    }
    
    public CalculationHistory(int id, double cgpa, double totalCredits, int courseCount, String calculationDate, String notes, String coursesJson) {
        this();
        this.id.set(id);
        this.cgpa.set(cgpa);
        this.totalCredits.set(totalCredits);
        this.courseCount.set(courseCount);
        this.calculationDate.set(calculationDate);
        this.notes.set(notes);
        this.coursesJson.set(coursesJson != null ? coursesJson : "");
    }
    
    public int getId() {
        return id.get();
    }
    
    public void setId(int id) {
        this.id.set(id);
    }
    
    public IntegerProperty idProperty() {
        return id;
    }
    
    public double getCgpa() {
        return cgpa.get();
    }
    
    public void setCgpa(double cgpa) {
        this.cgpa.set(cgpa);
    }
    
    public DoubleProperty cgpaProperty() {
        return cgpa;
    }
    
    public double getTotalCredits() {
        return totalCredits.get();
    }
    
    public void setTotalCredits(double totalCredits) {
        this.totalCredits.set(totalCredits);
    }
    
    public DoubleProperty totalCreditsProperty() {
        return totalCredits;
    }
    
    public int getCourseCount() {
        return courseCount.get();
    }
    
    public void setCourseCount(int courseCount) {
        this.courseCount.set(courseCount);
    }
    
    public IntegerProperty courseCountProperty() {
        return courseCount;
    }
    
    public String getCalculationDate() {
        return calculationDate.get();
    }
    
    public void setCalculationDate(String calculationDate) {
        this.calculationDate.set(calculationDate);
    }
    
    public StringProperty calculationDateProperty() {
        return calculationDate;
    }
    
    public String getNotes() {
        return notes.get();
    }
    
    public void setNotes(String notes) {
        this.notes.set(notes);
    }
    
    public StringProperty notesProperty() {
        return notes;
    }
    
    public String getCoursesJson() {
        return coursesJson.get();
    }
    
    public void setCoursesJson(String coursesJson) {
        this.coursesJson.set(coursesJson != null ? coursesJson : "");
    }
    
    public StringProperty coursesJsonProperty() {
        return coursesJson;
    }
    
    @Override
    public String toString() {
        return String.format("CGPA: %.2f | Credits: %.1f | Courses: %d | Date: %s", 
                           cgpa.get(), totalCredits.get(), courseCount.get(), calculationDate.get());
    }
}
