package com.example._207028_gpa_calculator_builder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.sql.*;
import java.util.concurrent.CompletableFuture;

public class HistoryDatabaseController {
    
    private static final String DB_FILE_PATH = System.getProperty("user.dir") + File.separator + "gpa_calculator.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE_PATH;
    private static final String HISTORY_TABLE = "calculation_history";
    
    public HistoryDatabaseController() {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found!");
            e.printStackTrace();
            throw new RuntimeException("SQLite driver not found", e);
        }
        initializeHistoryTable();
    }
    
    private void initializeHistoryTable() {
        System.out.println("🗄️ Initializing calculation history table...");
        System.out.println("📁 Database location: " + DB_FILE_PATH);
        
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + HISTORY_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "cgpa REAL NOT NULL, " +
                "total_credits REAL NOT NULL, " +
                "course_count INTEGER NOT NULL, " +
                "calculation_date TEXT NOT NULL, " +
                "notes TEXT, " +
                "courses_json TEXT" +
                ")";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createTableSQL);
            
            try {
                ResultSet rs = conn.getMetaData().getColumns(null, null, HISTORY_TABLE, "courses_json");
                if (!rs.next()) {
                    String alterTableSQL = "ALTER TABLE " + HISTORY_TABLE + " ADD COLUMN courses_json TEXT";
                    stmt.execute(alterTableSQL);
                    System.out.println("Added courses_json column to existing table");
                }
            } catch (SQLException e) {
                System.out.println("courses_json column check: " + e.getMessage());
            }
            
            System.out.println("Calculation history table initialized successfully");
            
        } catch (SQLException e) {
            if (e.getMessage().contains("malformed") || e.getMessage().contains("CORRUPT")) {
                System.err.println("Database is corrupted! Attempting to recreate...");
                
                File dbFile = new File(DB_FILE_PATH);
                if (dbFile.exists()) {
                    boolean deleted = dbFile.delete();
                    if (deleted) {
                        System.out.println("Corrupted database file deleted");
                        
                        try (Connection conn = DriverManager.getConnection(DB_URL);
                             Statement stmt = conn.createStatement()) {
                            
                            stmt.execute(createTableSQL);
                            System.out.println("Fresh database created successfully");
                            return;
                            
                        } catch (SQLException e2) {
                            System.err.println("Failed to create fresh database: " + e2.getMessage());
                            e2.printStackTrace();
                        }
                    } else {
                        System.err.println("Failed to delete corrupted database file");
                    }
                }
            }
            
            System.err.println("Failed to initialize history table: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize history table", e);
        }
    }
    
    public CompletableFuture<Boolean> insertHistory(CalculationHistory history) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "INSERT INTO " + HISTORY_TABLE + " " +
                "(cgpa, total_credits, course_count, calculation_date, notes, courses_json) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
            
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                
                pstmt.setDouble(1, history.getCgpa());
                pstmt.setDouble(2, history.getTotalCredits());
                pstmt.setInt(3, history.getCourseCount());
                pstmt.setString(4, history.getCalculationDate());
                pstmt.setString(5, history.getNotes());
                pstmt.setString(6, history.getCoursesJson());
                
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = pstmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        history.setId(generatedKeys.getInt(1));
                    }
                    System.out.println("History record inserted: CGPA=" + history.getCgpa());
                    return true;
                }
                
                return false;
                
            } catch (SQLException e) {
                System.err.println("Failed to insert history: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        });
    }
    
    public CompletableFuture<ObservableList<CalculationHistory>> fetchAllHistory() {
        return CompletableFuture.supplyAsync(() -> {
            ObservableList<CalculationHistory> historyList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM " + HISTORY_TABLE + " ORDER BY calculation_date DESC";
            
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    CalculationHistory history = new CalculationHistory(
                        rs.getInt("id"),
                        rs.getDouble("cgpa"),
                        rs.getDouble("total_credits"),
                        rs.getInt("course_count"),
                        rs.getString("calculation_date"),
                        rs.getString("notes"),
                        rs.getString("courses_json")
                    );
                    historyList.add(history);
                }
                
                System.out.println("Fetched " + historyList.size() + " history records");
                
            } catch (SQLException e) {
                System.err.println("Failed to fetch history: " + e.getMessage());
                e.printStackTrace();
            }
            
            return historyList;
        });
    }
    
    public CompletableFuture<Boolean> updateHistory(CalculationHistory history) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "UPDATE " + HISTORY_TABLE + " SET notes = ? WHERE id = ?";
            
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, history.getNotes());
                pstmt.setInt(2, history.getId());
                
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Updated " + rowsAffected + " history record(s)");
                return rowsAffected > 0;
                
            } catch (SQLException e) {
                System.err.println("Failed to update history: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        });
    }
    
    public CompletableFuture<Boolean> deleteHistory(int id) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "DELETE FROM " + HISTORY_TABLE + " WHERE id = ?";
            
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, id);
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Deleted " + rowsAffected + " history record(s)");
                return rowsAffected > 0;
                
            } catch (SQLException e) {
                System.err.println("Failed to delete history: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        });
    }
    
    public CompletableFuture<Boolean> deleteAllHistory() {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "DELETE FROM " + HISTORY_TABLE;
            
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement()) {
                
                int rowsAffected = stmt.executeUpdate(sql);
                System.out.println("Deleted " + rowsAffected + " history records");
                return true;
                
            } catch (SQLException e) {
                System.err.println("Failed to delete all history: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        });
    }
    
    public boolean testConnection() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + HISTORY_TABLE);
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Database connection test successful. Records: " + count);
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
