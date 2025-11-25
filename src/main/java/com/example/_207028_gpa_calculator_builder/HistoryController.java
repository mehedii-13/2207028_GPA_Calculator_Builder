package com.example._207028_gpa_calculator_builder;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {
    
    @FXML
    private TableView<CalculationHistory> historyTable;
    
    @FXML
    private TableColumn<CalculationHistory, Integer> idColumn;
    
    @FXML
    private TableColumn<CalculationHistory, Double> cgpaColumn;
    
    @FXML
    private TableColumn<CalculationHistory, Double> creditsColumn;
    
    @FXML
    private TableColumn<CalculationHistory, Integer> coursesColumn;
    
    @FXML
    private TableColumn<CalculationHistory, String> dateColumn;
    
    @FXML
    private TableColumn<CalculationHistory, String> notesColumn;
    
    @FXML
    private Label totalCountLabel;
    
    @FXML
    private Label highestCgpaLabel;
    
    @FXML
    private Label latestCgpaLabel;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private Button editCalculationButton;
    
    @FXML
    private Button updateButton;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private Button clearAllButton;
    
    @FXML
    private Button backButton;
    
    private HistoryDatabaseController historyDB;
    private ObservableList<CalculationHistory> historyList;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initializing History Controller...");
        
        try {
            historyDB = new HistoryDatabaseController();
            System.out.println("History database initialized successfully");
        } catch (Exception e) {
            System.err.println("History database initialization failed");
            System.err.println("   Error: " + e.getMessage());
            e.printStackTrace();
            historyDB = null;
            showErrorMessage("Database initialization failed. History features will be unavailable.");
        }
        
        setupTableColumns();
        
        if (historyDB != null) {
            loadHistory();
        } else {
            clearAllButton.setDisable(true);
            statusLabel.setText("Database not available");
        }
        
        historyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            editCalculationButton.setDisable(newSelection == null || historyDB == null);
            updateButton.setDisable(newSelection == null || historyDB == null);
            deleteButton.setDisable(newSelection == null || historyDB == null);
        });
        
        editCalculationButton.setDisable(true);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }
    
    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        cgpaColumn.setCellValueFactory(new PropertyValueFactory<>("cgpa"));
        creditsColumn.setCellValueFactory(new PropertyValueFactory<>("totalCredits"));
        coursesColumn.setCellValueFactory(new PropertyValueFactory<>("courseCount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("calculationDate"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        
        cgpaColumn.setCellFactory(column -> new TableCell<CalculationHistory, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                    
                    if (item >= 3.5) {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    } else if (item >= 3.0) {
                        setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    }
                }
            }
        });
    }
    
    private void loadHistory() {
        statusLabel.setText("Loading history...");
        
        historyDB.fetchAllHistory().thenAccept(history -> {
            Platform.runLater(() -> {
                historyList = history;
                historyTable.setItems(historyList);
                updateStatistics();
                statusLabel.setText("Loaded " + historyList.size() + " records");
            });
        });
    }
    
    private void updateStatistics() {
        totalCountLabel.setText(String.valueOf(historyList.size()));
        
        if (!historyList.isEmpty()) {
            double highest = historyList.stream()
                .mapToDouble(CalculationHistory::getCgpa)
                .max()
                .orElse(0.0);
            highestCgpaLabel.setText(String.format("%.2f", highest));
            
            double latest = historyList.get(0).getCgpa();
            latestCgpaLabel.setText(String.format("%.2f", latest));
        } else {
            highestCgpaLabel.setText("0.00");
            latestCgpaLabel.setText("0.00");
        }
    }
    
    @FXML
    public void updateHistory(ActionEvent actionEvent) {
        if (historyDB == null) {
            showErrorMessage("Database not available. Cannot update history.");
            return;
        }
        
        CalculationHistory selected = historyTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a record to edit.", Alert.AlertType.WARNING);
            return;
        }
        
        Dialog<CalculationHistory> dialog = new Dialog<>();
        dialog.setTitle("Edit Calculation Record");
        dialog.setHeaderText("Edit notes for this calculation");
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        TextArea notesArea = new TextArea(selected.getNotes());
        notesArea.setPromptText("Enter notes...");
        notesArea.setPrefRowCount(4);
        notesArea.setWrapText(true);
        
        dialog.getDialogPane().setContent(notesArea);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                selected.setNotes(notesArea.getText());
                return selected;
            }
            return null;
        });
        
        Optional<CalculationHistory> result = dialog.showAndWait();
        
        result.ifPresent(history -> {
            historyDB.updateHistory(history).thenAccept(success -> {
                Platform.runLater(() -> {
                    if (success) {
                        historyTable.refresh();
                        showAlert("Success", "Record updated successfully!", Alert.AlertType.INFORMATION);
                        statusLabel.setText("Record updated");
                    } else {
                        showAlert("Error", "Failed to update record.", Alert.AlertType.ERROR);
                    }
                });
            });
        });
    }
    
    @FXML
    public void deleteHistory(ActionEvent actionEvent) {
        if (historyDB == null) {
            showErrorMessage("Database not available. Cannot delete history.");
            return;
        }
        
        CalculationHistory selected = historyTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a record to delete.", Alert.AlertType.WARNING);
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Calculation Record");
        confirmAlert.setContentText("Are you sure you want to delete this record?\n\n" +
                "CGPA: " + String.format("%.2f", selected.getCgpa()) + "\n" +
                "Date: " + selected.getCalculationDate());
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            historyDB.deleteHistory(selected.getId()).thenAccept(success -> {
                Platform.runLater(() -> {
                    if (success) {
                        historyList.remove(selected);
                        updateStatistics();
                        showAlert("Success", "Record deleted successfully!", Alert.AlertType.INFORMATION);
                        statusLabel.setText("Record deleted");
                    } else {
                        showAlert("Error", "Failed to delete record.", Alert.AlertType.ERROR);
                    }
                });
            });
        }
    }
    
    @FXML
    public void clearAllHistory(ActionEvent actionEvent) {
        if (historyDB == null) {
            showErrorMessage("Database not available. Cannot clear history.");
            return;
        }
        
        if (historyList.isEmpty()) {
            showAlert("No Records", "There are no records to delete.", Alert.AlertType.INFORMATION);
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Clear All");
        confirmAlert.setHeaderText("Clear All History");
        confirmAlert.setContentText("Are you sure you want to delete ALL " + historyList.size() + 
                " calculation records?\n\nThis action cannot be undone!");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            historyDB.deleteAllHistory().thenAccept(success -> {
                Platform.runLater(() -> {
                    if (success) {
                        historyList.clear();
                        updateStatistics();
                        showAlert("Success", "All history cleared successfully!", Alert.AlertType.INFORMATION);
                        statusLabel.setText("All history cleared");
                    } else {
                        showAlert("Error", "Failed to clear history.", Alert.AlertType.ERROR);
                    }
                });
            });
        }
    }
    
    @FXML
    public void editCalculation(ActionEvent actionEvent) {
        CalculationHistory selected = historyTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a calculation to edit.", Alert.AlertType.WARNING);
            return;
        }
        
        if (selected.getCoursesJson() == null || selected.getCoursesJson().isEmpty()) {
            showAlert("No Course Data", "This calculation doesn't have saved course data.", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("input.fxml"));
            Scene scene = new Scene(loader.load());
            
            InputController controller = loader.getController();
            controller.loadCoursesFromJson(selected.getCoursesJson());
            
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load input page: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    public void goBackToHome(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("welcome.fxml"));
            Scene scene = new Scene(loader.load());
            
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load home page: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void showErrorMessage(String message) {
        showAlert("Database Error", message, Alert.AlertType.ERROR);
    }
}
