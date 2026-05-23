# 📚 Student Record Management System

A complete console-based Student Record Management System built in Core Java. Designed as a 1st year engineering college mini-project.

---

## ✨ Features

- **Add, Delete, Update, and Search** student records
- **Automatic GPA/CGPA calculation** (10-point scale)
- **Attendance tracking** with history log
- **Sort students** by Name, GPA, or Attendance
- **Search** by ID, Name (partial match), or Department
- **Formatted table display** with box-drawing characters
- **Summary report** generation
- **Array vs ArrayList comparison** (educational)
- **Sample data loading** for demo

---

## 🛠️ Technologies Used

| Technology       | Details                                      |
|------------------|----------------------------------------------|
| **Language**      | Java (Core Java)                            |
| **Data Structures** | ArrayList, HashMap, LinkedList            |
| **Concepts**      | OOP, Collections Framework, Exception Handling |
| **JDK Version**   | Java 8 or higher                            |

---

## 📁 Project Structure

```
student management app/
├── src/
│   ├── Main.java                  # Entry point, menu interface
│   ├── Student.java               # Student model class
│   ├── AttendanceRecord.java      # Attendance log entry
│   ├── StudentManager.java        # Business logic & CRUD
│   └── GradeCalculator.java       # GPA calculation utility
├── docs/
│   ├── ProjectReport.md           # Full project documentation
│   ├── ArrayVsArrayList.md        # Data structure comparison
│   ├── Flowchart.md               # Program flowchart
│   └── VivaQuestions.md           # Viva prep Q&A
└── README.md
```

---

## 🚀 How to Run

### Using Command Line

```bash
cd "student management app/src"
javac *.java
java Main
```

### Using VS Code

1. Install **Extension Pack for Java** from VS Code marketplace
2. Open the `student management app` folder in VS Code
3. Open `src/Main.java`
4. Click the **Run** button (▶️) at the top right, or press `Ctrl+F5`
5. The program will run in the integrated terminal

### Using IntelliJ IDEA

1. Open IntelliJ IDEA → **File → Open** → Select `student management app` folder
2. Mark `src` as **Sources Root** (right-click → Mark Directory as → Sources Root)
3. Open `Main.java` → Click the green **Run** button (▶️)

### Requirements

- Java JDK 8 or higher
- Any text editor or IDE (VS Code recommended)
- No external libraries or dependencies required

---

## 📸 Sample Output

When running with sample data loaded via **menu option 14**:

```
╔════════╤══════════════════════╤════════════╤═══════╤════════════╗
║   ID   │        Name          │ Department │  GPA  │ Attendance ║
╠════════╪══════════════════════╪════════════╪═══════╪════════════╣
║ 1001   │ Rahul Sharma         │ CSE        │ 8.50  │ 85.0%      ║
║ 1002   │ Priya Patel          │ ECE        │ 9.20  │ 92.0%      ║
║ 1003   │ Amit Kumar           │ ME         │ 7.80  │ 78.0%      ║
║ 1004   │ Sneha Reddy          │ CSE        │ 8.90  │ 88.0%      ║
║ 1005   │ Karan Singh          │ CIVIL      │ 7.50  │ 75.0%      ║
╚════════╧══════════════════════╧════════════╧═══════╧════════════╝
Total Students: 5 | Average GPA: 8.38 | Average Attendance: 83.6%
```

---

## 🏗️ OOP Concepts Used

| Concept | Where Used |
|---------|------------|
| **Classes & Objects** | `Student`, `AttendanceRecord`, `StudentManager`, `GradeCalculator` |
| **Encapsulation** | Private fields + public getters/setters in all model classes |
| **Constructors** | Default and parameterized constructors in `Student` and `AttendanceRecord` |
| **Composition** | `Student` has-a `HashMap` of grades, `LinkedList` of attendance records |
| **Comparable** | `Student` implements `Comparable` for natural ordering by student ID |
| **Static Methods** | `GradeCalculator` utility methods for GPA/CGPA computation |
| **Exception Handling** | Try-catch blocks for input validation and error handling |
| **Collections Framework** | `ArrayList`, `HashMap`, `LinkedList` used throughout the project |

---

## 📊 Data Structures Summary

| Data Structure | Purpose in Project |
|----------------|-------------------|
| `ArrayList<Student>` | Stores the main list of all student records |
| `HashMap<String, Double>` | Stores subject → grade mappings per student |
| `LinkedList<AttendanceRecord>` | Stores chronological attendance history per student |

---

## 🔑 Key Menu Options

| # | Option | Description |
|---|--------|-------------|
| 1 | Add Student | Register a new student with all details |
| 2 | Delete Student | Remove a student record by ID |
| 3 | Update Student | Modify existing student information |
| 4 | Search Student | Find students by ID, name, or department |
| 5 | Display All | Show all students in formatted table |
| 6 | Add Grades | Add subject grades for a student |
| 7 | Calculate GPA | Compute GPA for a specific student |
| 8 | Mark Attendance | Record attendance for a student |
| 9 | View Attendance | Display attendance history |
| 10 | Sort Students | Sort by name, GPA, or attendance |
| 11 | Class Summary | Generate overall class statistics |
| 12 | Top Performers | Display top N students by GPA |
| 13 | Array vs ArrayList | Educational comparison demo |
| 14 | Load Sample Data | Pre-load demo student records |
| 0 | Exit | Save and exit the program |

---

## 👤 Author

| Field | Details |
|-------|---------|
| **Student Name** | ________________ |
| **Roll Number** | ________________ |
| **Department** | ________________ |
| **College** | ________________ |
| **Academic Year** | 2025–2026 |

---

## 📄 License

This project is developed as an academic mini-project for educational purposes only.
