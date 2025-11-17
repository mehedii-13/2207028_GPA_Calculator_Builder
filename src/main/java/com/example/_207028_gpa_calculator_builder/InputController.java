package com.example._207028_gpa_calculator_builder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InputController implements Initializable {
    

    private double targetCredit = 0.0;
    

    @FXML
    private TextField targetCreditsField;
    @FXML
    private Label targetCreditLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField codeField;
    @FXML
    private TextField creditField;
    @FXML
    private TextField teacher1Field;
    @FXML
    private TextField teacher2Field;
    @FXML
    private ComboBox<String> gradeBox;
    @FXML
    private Button addButton;
    

    @FXML
    private TableView<AddCourse> courseTable;
    @FXML
    private TableColumn<AddCourse, String> nameCol;
    @FXML
    private TableColumn<AddCourse, String> codeCol;
    @FXML
    private TableColumn<AddCourse, Double> creditCol;
    @FXML
    private TableColumn<AddCourse, String> teacher1Col;
    @FXML
    private TableColumn<AddCourse, String> teacher2Col;
    @FXML
    private TableColumn<AddCourse, String> gradeCol;

    @FXML
    private Button resetButton;
    @FXML
    private Button calculateButton;
    

    private ObservableList<AddCourse> courseList = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        gradeBox.setItems(FXCollections.observableArrayList(
            "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "F"
        ));
        

        nameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        creditCol.setCellValueFactory(new PropertyValueFactory<>("courseCredit"));
        teacher1Col.setCellValueFactory(new PropertyValueFactory<>("teacher1Name"));
        teacher2Col.setCellValueFactory(new PropertyValueFactory<>("teacher2Name"));
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));

        courseTable.setItems(courseList);

        addButton.setOnAction(this::addCourse);
        resetButton.setOnAction(this::resetForm);
        calculateButton.setOnAction(this::calculateGPA);

        calculateButton.setDisable(true);

        courseList.addListener((javafx.collections.ListChangeListener<AddCourse>) change -> {
            calculateButton.setDisable(courseList.isEmpty());
        });
    }
    
    @FXML
    public void setCredit(ActionEvent actionEvent) {
        try {
            String creditText = targetCreditsField.getText().trim();
            if (!creditText.isEmpty()) {
                targetCredit = Double.parseDouble(creditText);
                targetCreditLabel.setText("Target Credit: " + creditText);
                targetCreditLabel.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for target credits.", Alert.AlertType.ERROR);
            targetCreditsField.clear();
        }
    }
    
    @FXML
    public void addCourse(ActionEvent actionEvent) {
        try {
            if (isValidInput()) {
                String courseName = nameField.getText().trim();
                String courseCode = codeField.getText().trim();
                double courseCredit = Double.parseDouble(creditField.getText().trim());
                String teacher1 = teacher1Field.getText().trim();
                String teacher2 = teacher2Field.getText().trim();
                String grade = gradeBox.getValue();

                boolean exists = courseList.stream()
                    .anyMatch(course -> course.getCourseCode().equalsIgnoreCase(courseCode));
                
                if (exists) {
                    showAlert("Duplicate Course", "A course with this code already exists!", Alert.AlertType.WARNING);
                    return;
                }

                AddCourse course = new AddCourse(courseName, courseCode, courseCredit, teacher1, teacher2, grade);
                courseList.add(course);

                clearInputFields();
                
                showAlert("Success", "Course added successfully!", Alert.AlertType.INFORMATION);
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for course credits.", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    public void resetForm(ActionEvent actionEvent) {
        clearInputFields();
        targetCreditsField.clear();

        courseList.clear();

        targetCredit = 0.0;
        targetCreditLabel.setText("Target Credits");
        targetCreditLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;");
        
        showAlert("Reset Complete", "All data has been cleared.", Alert.AlertType.INFORMATION);
    }
    
    @FXML
    public void calculateGPA(ActionEvent actionEvent) {
        if (courseList.isEmpty()) {
            showAlert("No Courses", "Please add at least one course before calculating GPA.", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("results.fxml"));
            Scene scene = new Scene(loader.load());

            ResultController resultController = loader.getController();
            resultController.initData(courseList, targetCredit);

            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            showAlert("Error", "Unable to load results page: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private boolean isValidInput() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert("Missing Information", "Please enter the course name.", Alert.AlertType.WARNING);
            nameField.requestFocus();
            return false;
        }
        
        if (codeField.getText().trim().isEmpty()) {
            showAlert("Missing Information", "Please enter the course code.", Alert.AlertType.WARNING);
            codeField.requestFocus();
            return false;
        }
        
        if (creditField.getText().trim().isEmpty()) {
            showAlert("Missing Information", "Please enter the course credits.", Alert.AlertType.WARNING);
            creditField.requestFocus();
            return false;
        }
        
        try {
            double credit = Double.parseDouble(creditField.getText().trim());
            if (credit <= 0) {
                showAlert("Invalid Credit", "Course credits must be greater than 0.", Alert.AlertType.WARNING);
                creditField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Credit", "Please enter a valid number for course credits.", Alert.AlertType.WARNING);
            creditField.requestFocus();
            return false;
        }
        
        if (teacher1Field.getText().trim().isEmpty()) {
            showAlert("Missing Information", "Please enter at least one teacher name.", Alert.AlertType.WARNING);
            teacher1Field.requestFocus();
            return false;
        }
        
        if (gradeBox.getValue() == null) {
            showAlert("Missing Grade", "Please select a grade for the course.", Alert.AlertType.WARNING);
            gradeBox.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void clearInputFields() {
        nameField.clear();
        codeField.clear();
        creditField.clear();
        teacher1Field.clear();
        teacher2Field.clear();
        gradeBox.setValue(null);
    }
    
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goToWelcome(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("welcome.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }
}
