# 📊 GPA Calculator - JavaFX Application

**Student ID:** 2207028  
**Version:** 1.0.0  
**Framework:** JavaFX 21.0.6

A modern, user-friendly JavaFX application for calculating and tracking academic GPA (Grade Point Average) with comprehensive course management features.

## ✨ Features

- **Course Management**: Add courses with detailed information including:
  - Course name and code
  - Credit hours
  - Teacher names (up to 2 teachers per course)
  - Letter grades (A+ to F)

- **GPA Calculation**: 
  - Automatic calculation based on standard 4.0 scale
  - Real-time updates as courses are added
  - Cumulative GPA tracking

- **Progress Tracking**:
  - Set target credit hours
  - Monitor remaining credits needed
  - Visual progress indicators

- **Professional UI**:
  - Modern Material Design-inspired interface
  - Responsive layout with clean typography
  - Color-coded GPA indicators
  - Detailed results dashboard

- **Data Management**:
  - Course validation and duplicate prevention
  - Reset functionality
  - Comprehensive error handling

## 🏗️ Architecture

### Project Structure
```
src/
├── main/
│   ├── java/
│   │   ├── module-info.java
│   │   └── com/example/_207028_gpa_calculator_builder/
│   │       ├── Course.java              # Data model for courses
│   │       ├── HelloApplication.java    # Main application class
│   │       ├── HelloController.java     # Basic controller (unused)
│   │       ├── InputController.java     # Course input management
│   │       ├── Launcher.java           # Application launcher
│   │       ├── ResultController.java   # Results display management
│   │       └── WelcomeController.java  # Welcome screen controller
│   └── resources/
│       └── com/example/_207028_gpa_calculator_builder/
│           ├── hello-view.fxml         # Basic view (unused)
│           ├── input.fxml              # Course input interface
│           ├── results.fxml            # Results dashboard
│           └── welcome.fxml            # Welcome screen
```

### Key Components

#### 1. Course Model (`Course.java`)
- JavaFX Properties-based data model
- Automatic grade point conversion
- Supports standard letter grades (A+ = 4.0, A = 3.75, etc.)
- Observable properties for real-time UI updates

#### 2. Controllers
- **WelcomeController**: Landing page navigation
- **InputController**: Course entry, validation, and management
- **ResultController**: GPA calculation and results display

#### 3. User Interface
- **Welcome Screen**: Professional landing page with feature overview
- **Input Form**: Comprehensive course entry with validation
- **Results Dashboard**: Detailed GPA analysis with visual indicators

## 📋 Requirements

### System Requirements
- **Java**: JDK 21 or higher
- **Maven**: 3.6.0 or higher
- **JavaFX**: 21.0.6 (included as dependency)

### Dependencies
- JavaFX Controls
- JavaFX FXML
- JavaFX Base
- Maven Surefire Plugin
- JavaFX Maven Plugin

## 🚀 Getting Started

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/mehedii-13/2207028_GPA_Calculator_Builder.git
   cd 2207028_GPA_Calculator_Builder
   ```

2. **Ensure Java 21+ is installed**:
   ```bash
   java -version
   ```

3. **Build the project**:
   ```bash
   mvn clean compile
   ```

### Running the Application

#### Option 1: Using Maven (Recommended)
```bash
mvn clean javafx:run
```

#### Option 2: Using IDE
1. Import the project into your IDE (IntelliJ IDEA, Eclipse, etc.)
2. Set up JavaFX as a dependency
3. Run the `Launcher.java` file

#### Option 3: Command Line
```bash
mvn clean package
java --module-path path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -cp target/classes com.example._207028_gpa_calculator_builder.Launcher
```

## 📖 Usage Guide

### 1. Welcome Screen
- Start the application to see the welcome screen
- Click "Start Calculating" to begin

### 2. Setting Target Credits (Optional)
- Enter your target credit hours in the "Target Credits" field
- Click "Set" to confirm

### 3. Adding Courses
- Fill in all required course information:
  - Course name (e.g., "Advanced Programming Lab")
  - Course code (e.g., "CSE 2200")
  - Credit hours (e.g., "3.0")
  - Teacher name(s)
  - Grade from dropdown menu
- Click "Add" to add the course to your list

### 4. Managing Courses
- View all added courses in the table
- Use "Reset" to clear all data
- Courses are validated for duplicates and required fields

### 5. Calculating GPA
- Click "Calculate GPA" when you have added all courses
- View detailed results on the results dashboard

### 6. Understanding Results
- **Current GPA**: Your cumulative GPA on a 4.0 scale
- **Completed Credits**: Total credit hours completed
- **Target Credits**: Your set target (if any)
- **Remaining Credits**: Credits needed to reach target
- **Course Details**: Complete breakdown of all courses

## 🎯 Grade Scale

| Letter Grade | Grade Point |
|-------------|-------------|
| A+          | 4.00        |
| A           | 3.75        |
| A-          | 3.50        |
| B+          | 3.25        |
| B           | 3.00        |
| B-          | 2.75        |
| C+          | 2.50        |
| C           | 2.25        |
| C-          | 2.00        |
| D+          | 1.75        |
| D           | 1.50        |
| F           | 0.00        |

## 🛠️ Development

### Building for Development
```bash
# Compile and run tests
mvn clean test

# Compile without running
mvn compile

# Clean build artifacts
mvn clean
```

### Code Structure
- **MVC Architecture**: Clear separation of Model, View, and Controller
- **JavaFX Properties**: Reactive data binding
- **FXML**: Declarative UI design
- **Modular Design**: Separate concerns and reusable components

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is created for educational purposes as part of coursework.

## 👤 Author

**Student ID**: 2207028  
**GitHub**: [@mehedii-13](https://github.com/mehedii-13)

## 🙏 Acknowledgments

- JavaFX team for the excellent framework
- Course instructors for guidance
- Material Design for UI inspiration

---

**Note**: This application is designed for academic use and follows standard GPA calculation methods. Always verify calculations with your institution's official policies.