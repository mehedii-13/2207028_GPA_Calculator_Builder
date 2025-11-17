package com.example._207028_gpa_calculator_builder;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ResultController implements Initializable {
    
    @FXML
    private Label gpaLabel;
    @FXML
    private Label totalCreditsLabel;
    @FXML
    private Label targetCreditsLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private TableView<AddCourse> resultsTable;
    @FXML
    private TableColumn<AddCourse, String> courseNameCol;
    @FXML
    private TableColumn<AddCourse, String> courseCodeCol;
    @FXML
    private TableColumn<AddCourse, Double> creditCol;
    @FXML
    private TableColumn<AddCourse, String> gradeCol;
    @FXML
    private TableColumn<AddCourse, Double> gradePointCol;
    
    private ObservableList<AddCourse> courses;
    private double targetCredit;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        courseNameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseCodeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        creditCol.setCellValueFactory(new PropertyValueFactory<>("courseCredit"));
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradePointCol.setCellValueFactory(new PropertyValueFactory<>("gradePoint"));
    }
    
    public void initData(ObservableList<AddCourse> courses, double targetCredit) {
        this.courses = courses;
        this.targetCredit = targetCredit;

        resultsTable.setItems(courses);

        calculateAndDisplayResults();
    }
    
    private void calculateAndDisplayResults() {
        double totalGradePoints = 0.0;
        double totalCredits = 0.0;

        for (AddCourse course : courses) {
            double credits = course.getCourseCredit();
            double gradePoint = course.getGradePoint();
            totalGradePoints += (credits * gradePoint);
            totalCredits += credits;
        }

        double gpa = totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;

        gpaLabel.setText(String.format("%.2f", gpa));
        totalCreditsLabel.setText(String.format("%.1f", totalCredits));
        
        if (targetCredit > 0) {
            targetCreditsLabel.setText(String.format("%.1f", targetCredit));
            double remainingCredits = targetCredit - totalCredits;
            
            if (remainingCredits > 0) {
                statusLabel.setText(String.format("You need %.1f more credits to reach your target", remainingCredits));
                statusLabel.setStyle("-fx-text-fill: #ff9800; -fx-font-weight: bold;");
            } else {
                statusLabel.setText("🎉 Congratulations! Target credits completed!");
                statusLabel.setStyle("-fx-text-fill: #4caf50; -fx-font-weight: bold;");
            }
        } else {
            targetCreditsLabel.setText("Not Set");
            statusLabel.setText("Set a target credit to track your progress");
            statusLabel.setStyle("-fx-text-fill: #757575;");
        }

        if (gpa >= 3.5) {
            gpaLabel.setStyle("-fx-text-fill: #4caf50; -fx-font-weight: bold; -fx-font-size: 24px;");
        } else if (gpa >= 3.0) {
            gpaLabel.setStyle("-fx-text-fill: #ff9800; -fx-font-weight: bold; -fx-font-size: 24px;");
        } else {
            gpaLabel.setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold; -fx-font-size: 24px;");
        }
    }
    
    @FXML
    public void goBackToInput(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("input.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }
    
    @FXML
    public void goToWelcome(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("welcome.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }
}
