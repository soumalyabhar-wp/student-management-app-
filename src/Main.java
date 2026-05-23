/*
 * ============================================================
 * STUDENT RECORD MANAGEMENT SYSTEM
 * ============================================================
 * 
 * Project: Student Record Management System
 * Language: Java (Core Java)
 * Type: Console-Based Application
 * Suitable for: 1st Year Engineering Mini Project
 * 
 * ============================================================
 * ANALYSIS: Why ArrayList was chosen over Arrays
 * ============================================================
 * 
 * In this project, we chose ArrayList over traditional arrays
 * for the following reasons:
 * 
 * 1. DYNAMIC SIZING: Arrays have a fixed size defined at creation.
 *    If we use an array of size 100 and get 101 students, the 
 *    program crashes. ArrayList grows automatically as needed.
 * 
 * 2. BUILT-IN METHODS: ArrayList provides add(), remove(), get(),
 *    contains(), size(), indexOf() etc. With arrays, we would need
 *    to write all these operations manually.
 * 
 * 3. EASY DELETION: Removing an element from an array requires
 *    shifting all subsequent elements. ArrayList handles this
 *    internally with its remove() method.
 * 
 * 4. COLLECTIONS FRAMEWORK: ArrayList works with Collections.sort(),
 *    Comparator, and other Java Collections utilities. Arrays 
 *    require Arrays.sort() which is less flexible.
 * 
 * 5. TYPE SAFETY: ArrayList<Student> ensures only Student objects
 *    can be stored (generics). Raw arrays don't provide this 
 *    compile-time safety.
 * 
 * 6. SCALABILITY: For a student management system where the number
 *    of students is unknown and changes frequently, ArrayList
 *    is the natural choice.
 * 
 * Trade-off: Arrays are slightly faster for direct index access
 * (O(1) vs O(1) — same actually) and use less memory overhead.
 * But for this project, the flexibility of ArrayList far outweighs
 * the minimal performance difference.
 * 
 * ============================================================
 * DATA STRUCTURES USED IN THIS PROJECT
 * ============================================================
 * 
 * 1. ArrayList<Student> — Primary storage for all students
 *    → Dynamic size, easy CRUD operations
 * 
 * 2. HashMap<Integer, Student> — Fast ID-based lookup
 *    → O(1) search time instead of O(n) linear search
 * 
 * 3. LinkedList<AttendanceRecord> — Per-student attendance log
 *    → Efficient append for chronological records
 * 
 * 4. HashMap<String, Double> — Subject-wise grades per student
 *    → Key-value mapping for subject → marks
 * 
 * ============================================================
 */

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main class — Entry point of the Student Record Management System.
 * <p>
 * This class displays a menu-driven console interface that allows users
 * to add, delete, update, search, sort, and generate reports on student
 * records. All user input is wrapped in try-catch blocks for safety.
 * </p>
 *
 * @author Student Project
 * @version 1.0
 */
public class Main {

    // ────────────────────────────────────────────────────────
    //  MAIN METHOD
    // ────────────────────────────────────────────────────────

    /**
     * Application entry point. Creates a Scanner and StudentManager,
     * displays the welcome banner, and enters the main menu loop.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        // Create a Scanner for reading user input from the console
        Scanner scanner = new Scanner(System.in);

        // Create a StudentManager instance to handle all student operations
        StudentManager manager = new StudentManager();

        // Display the welcome banner when the program starts
        displayWelcomeBanner();

        // Main program loop — runs until the user chooses to exit (option 0)
        while (true) {

            // Show the menu options
            displayMenu();

            // Variable to store the user's menu choice
            int choice = -1;

            try {
                System.out.print("  ➤ Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // consume the leftover newline character
            } catch (InputMismatchException e) {
                // If the user types a non-integer value, handle it gracefully
                System.out.println("\n  ⚠  Invalid input! Please enter a number between 0 and 14.");
                scanner.nextLine(); // clear the invalid input from the scanner
                pressEnterToContinue(scanner);
                continue; // go back to the top of the loop
            }

            // Print a separator for clean output
            System.out.println("\n  ════════════════════════════════════════════════════");

            // Process the user's choice using a switch-case
            switch (choice) {

                // ─────────────── OPTION 1: ADD NEW STUDENT ───────────────
                case 1:
                    try {
                        System.out.println("  📝  ADD NEW STUDENT");
                        System.out.println("  ────────────────────────────────────────");

                        // Prompt for Student ID
                        System.out.print("  Enter Student ID      : ");
                        int id = scanner.nextInt();
                        scanner.nextLine(); // consume newline

                        // Prompt for Student Name
                        System.out.print("  Enter Student Name    : ");
                        String name = scanner.nextLine().trim();

                        // Prompt for Student Age
                        System.out.print("  Enter Student Age     : ");
                        int age = scanner.nextInt();
                        scanner.nextLine(); // consume newline

                        // Prompt for Department
                        System.out.print("  Enter Department      : ");
                        String department = scanner.nextLine().trim();

                        // Prompt for Admission Date
                        System.out.print("  Enter Admission Date  : ");
                        String admissionDate = scanner.nextLine().trim();

                        // Create a new Student object with the provided details
                        Student student = new Student(id, name, age, department, admissionDate);

                        // Add the student using the manager
                        manager.addStudent(student);

                        // Ask if the user wants to add grades right now
                        System.out.print("\n  Do you want to add grades now? (Y/N): ");
                        String addGrades = scanner.nextLine().trim();

                        if (addGrades.equalsIgnoreCase("Y")) {
                            System.out.print("  How many subjects? : ");
                            int subjectCount = scanner.nextInt();
                            scanner.nextLine(); // consume newline

                            // Loop to collect each subject name and marks
                            for (int i = 1; i <= subjectCount; i++) {
                                System.out.print("  Enter Subject " + i + " name  : ");
                                String subject = scanner.nextLine().trim();

                                System.out.print("  Enter marks for " + subject + " : ");
                                double marks = scanner.nextDouble();
                                scanner.nextLine(); // consume newline

                                // Add the grade to the student's record
                                student.addGrade(subject, marks);
                            }
                            System.out.println("\n  ✅  Grades added successfully!");
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("\n  ⚠  Invalid input! Please enter correct data types.");
                        scanner.nextLine(); // clear invalid input
                    }
                    break;

                // ─────────────── OPTION 2: DELETE STUDENT ───────────────
                case 2:
                    try {
                        System.out.println("  🗑️  DELETE STUDENT");
                        System.out.println("  ────────────────────────────────────────");

                        // Ask for the ID of the student to delete
                        System.out.print("  Enter Student ID to delete: ");
                        int deleteId = scanner.nextInt();
                        scanner.nextLine(); // consume newline

                        // Confirm the deletion with the user
                        System.out.print("  Are you sure you want to delete student " + deleteId + "? (Y/N): ");
                        String confirm = scanner.nextLine().trim();

                        if (confirm.equalsIgnoreCase("Y")) {
                            manager.deleteStudent(deleteId);
                        } else {
                            System.out.println("  ❌  Deletion cancelled.");
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("\n  ⚠  Invalid input! Please enter a valid Student ID.");
                        scanner.nextLine(); // clear invalid input
                    }
                    break;

                // ─────────────── OPTION 3: UPDATE STUDENT ───────────────
                case 3:
                    try {
                        System.out.println("  ✏️  UPDATE STUDENT INFORMATION");
                        System.out.println("  ────────────────────────────────────────");

                        // Ask for the ID of the student to update
                        System.out.print("  Enter Student ID to update: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine(); // consume newline

                        // Show the update sub-menu
                        System.out.println("\n  What would you like to update?");
                        System.out.println("  ┌──────────────────────────────┐");
                        System.out.println("  │  1. Name                     │");
                        System.out.println("  │  2. Age                      │");
                        System.out.println("  │  3. Department               │");
                        System.out.println("  │  4. Add Attendance Record    │");
                        System.out.println("  │  5. Add Grade                │");
                        System.out.println("  └──────────────────────────────┘");
                        System.out.print("  ➤ Enter your choice: ");
                        int updateChoice = scanner.nextInt();
                        scanner.nextLine(); // consume newline

                        // Process the update sub-menu choice
                        switch (updateChoice) {
                            case 1:
                                System.out.print("  Enter new Name: ");
                                String newName = scanner.nextLine().trim();
                                manager.updateStudentName(updateId, newName);
                                break;
                            case 2:
                                System.out.print("  Enter new Age: ");
                                int newAge = scanner.nextInt();
                                scanner.nextLine(); // consume newline
                                manager.updateStudentAge(updateId, newAge);
                                break;
                            case 3:
                                System.out.print("  Enter new Department: ");
                                String newDept = scanner.nextLine().trim();
                                manager.updateStudentDepartment(updateId, newDept);
                                break;
                            case 4:
                                System.out.print("  Enter Date (DD/MM/YYYY)   : ");
                                String attDate = scanner.nextLine().trim();
                                System.out.print("  Present or Absent? (P/A)  : ");
                                String status = scanner.nextLine().trim();
                                System.out.print("  Remarks (or press Enter)  : ");
                                String remarks = scanner.nextLine().trim();
                                // Convert P/A to boolean
                                boolean isPresent = status.equalsIgnoreCase("P");
                                manager.addAttendanceRecord(updateId, new AttendanceRecord(attDate, isPresent, remarks));
                                break;
                            case 5:
                                System.out.print("  Enter Subject Name : ");
                                String subName = scanner.nextLine().trim();
                                System.out.print("  Enter Marks        : ");
                                double subMarks = scanner.nextDouble();
                                scanner.nextLine(); // consume newline
                                manager.addGrade(updateId, subName, subMarks);
                                break;
                            default:
                                System.out.println("  ⚠  Invalid update option.");
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("\n  ⚠  Invalid input! Please enter correct data types.");
                        scanner.nextLine(); // clear invalid input
                    }
                    break;

                // ─────────────── OPTION 4: SEARCH BY ID ───────────────
                case 4:
                    try {
                        System.out.println("  🔍  SEARCH STUDENT BY ID");
                        System.out.println("  ────────────────────────────────────────");

                        System.out.print("  Enter Student ID: ");
                        int searchId = scanner.nextInt();
                        scanner.nextLine(); // consume newline

                        // Search for the student and display the result
                        Student foundById = manager.searchById(searchId);
                        if (foundById != null) {
                            System.out.println("\n  ✅  Student Found!");
                            manager.displayStudentDetails(foundById);
                        } else {
                            System.out.println("\n  ❌  No student found with ID: " + searchId);
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("\n  ⚠  Invalid input! Please enter a valid Student ID.");
                        scanner.nextLine(); // clear invalid input
                    }
                    break;

                // ─────────────── OPTION 5: SEARCH BY NAME ───────────────
                case 5:
                    System.out.println("  🔍  SEARCH STUDENT BY NAME");
                    System.out.println("  ────────────────────────────────────────");

                    System.out.print("  Enter Student Name (or partial name): ");
                    String searchName = scanner.nextLine().trim();

                    // Search by name — supports partial matching
                    ArrayList<Student> nameResults = manager.searchByName(searchName);
                    if (nameResults.isEmpty()) {
                        System.out.println("\n  ❌  No students found matching: " + searchName);
                    } else {
                        System.out.println("\n  ✅  Found " + nameResults.size() + " student(s):");
                        for (Student s : nameResults) {
                            manager.displayStudentDetails(s);
                        }
                    }
                    break;

                // ─────────────── OPTION 6: SEARCH BY DEPARTMENT ───────────────
                case 6:
                    System.out.println("  🔍  SEARCH BY DEPARTMENT");
                    System.out.println("  ────────────────────────────────────────");

                    System.out.print("  Enter Department name: ");
                    String searchDept = scanner.nextLine().trim();

                    // Search and display all students in the given department
                    ArrayList<Student> deptResults = manager.searchByDepartment(searchDept);
                    if (deptResults.isEmpty()) {
                        System.out.println("\n  ❌  No students found in department: " + searchDept);
                    } else {
                        System.out.println("\n  ✅  Found " + deptResults.size() + " student(s):");
                        for (Student s : deptResults) {
                            manager.displayStudentDetails(s);
                        }
                    }
                    break;

                // ─────────────── OPTION 7: DISPLAY ALL STUDENTS ───────────────
                case 7:
                    System.out.println("  📋  ALL STUDENT RECORDS");
                    System.out.println("  ────────────────────────────────────────");

                    // Display all students currently in the system
                    manager.displayAllStudents();
                    break;

                // ─────────────── OPTION 8: ADD ATTENDANCE RECORD ───────────────
                case 8:
                    try {
                        System.out.println("  📅  ADD ATTENDANCE RECORD");
                        System.out.println("  ────────────────────────────────────────");

                        System.out.print("  Enter Student ID          : ");
                        int attId = scanner.nextInt();
                        scanner.nextLine(); // consume newline

                        System.out.print("  Enter Date (DD/MM/YYYY)   : ");
                        String date = scanner.nextLine().trim();

                        System.out.print("  Present or Absent? (P/A)  : ");
                        String attStatus = scanner.nextLine().trim();

                        System.out.print("  Remarks (or press Enter)  : ");
                        String attRemarks = scanner.nextLine().trim();

                        // Convert the P/A input to a boolean value
                        boolean present = attStatus.equalsIgnoreCase("P");

                        // Add the attendance record through the manager
                        manager.addAttendanceRecord(attId, new AttendanceRecord(date, present, attRemarks));

                    } catch (InputMismatchException e) {
                        System.out.println("\n  ⚠  Invalid input! Please enter a valid Student ID.");
                        scanner.nextLine(); // clear invalid input
                    }
                    break;

                // ─────────────── OPTION 9: VIEW ATTENDANCE HISTORY ───────────────
                case 9:
                    try {
                        System.out.println("  📊  VIEW ATTENDANCE HISTORY");
                        System.out.println("  ────────────────────────────────────────");

                        System.out.print("  Enter Student ID: ");
                        int viewAttId = scanner.nextInt();
                        scanner.nextLine(); // consume newline

                        // Display the full attendance history for this student
                        manager.viewAttendanceHistory(viewAttId);

                    } catch (InputMismatchException e) {
                        System.out.println("\n  ⚠  Invalid input! Please enter a valid Student ID.");
                        scanner.nextLine(); // clear invalid input
                    }
                    break;

                // ─────────────── OPTION 10: CALCULATE & DISPLAY GPA ───────────────
                case 10:
                    try {
                        System.out.println("  🎓  CALCULATE & DISPLAY GPA");
                        System.out.println("  ────────────────────────────────────────");

                        System.out.print("  Enter Student ID: ");
                        int gpaId = scanner.nextInt();
                        scanner.nextLine(); // consume newline

                        // Search for the student first
                        Student gpaStudent = manager.searchById(gpaId);

                        if (gpaStudent != null) {
                            // Use GradeCalculator to compute and display GPA
                            HashMap<String, Double> grades = gpaStudent.getGrades();
                            if (grades.isEmpty()) {
                                System.out.println("\n  ❌  No grades recorded for this student.");
                            } else {
                                double gpa = GradeCalculator.calculateGPA(grades);
                                double avgMarks = GradeCalculator.calculateAverageMarks(grades);
                                String classification = GradeCalculator.getGPAClassification(gpa);

                                System.out.println();
                                System.out.println("  ╔════════════════════════════════════════════════╗");
                                System.out.println("  ║           🎓  GPA REPORT                       ║");
                                System.out.println("  ╠════════════════════════════════════════════════╣");
                                System.out.printf("  ║  Student : %-37s║%n", gpaStudent.getName());
                                System.out.printf("  ║  ID      : %-37d║%n", gpaStudent.getId());
                                System.out.println("  ╠════════════════════════════════════════════════╣");
                                System.out.printf("  ║  %-20s  %-8s  %-12s  ║%n", "Subject", "Marks", "Grade");
                                System.out.println("  ║  ──────────────────────────────────────────    ║");
                                for (HashMap.Entry<String, Double> entry : grades.entrySet()) {
                                    System.out.printf("  ║  %-20s  %-8.2f  %-12s  ║%n",
                                            entry.getKey(), entry.getValue(),
                                            GradeCalculator.getLetterGrade(entry.getValue()));
                                }
                                System.out.println("  ╠════════════════════════════════════════════════╣");
                                System.out.printf("  ║  Average Marks    : %-26.2f║%n", avgMarks);
                                System.out.printf("  ║  GPA (10-point)   : %-26.2f║%n", gpa);
                                System.out.printf("  ║  Classification   : %-26s║%n", classification);
                                System.out.println("  ╚════════════════════════════════════════════════╝");
                            }
                        } else {
                            System.out.println("\n  ❌  No student found with ID: " + gpaId);
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("\n  ⚠  Invalid input! Please enter a valid Student ID.");
                        scanner.nextLine(); // clear invalid input
                    }
                    break;

                // ─────────────── OPTION 11: SORT STUDENTS ───────────────
                case 11:
                    try {
                        System.out.println("  🔀  SORT STUDENTS");
                        System.out.println("  ────────────────────────────────────────");

                        // Show sort sub-menu
                        System.out.println("\n  Sort by:");
                        System.out.println("  ┌──────────────────────────────┐");
                        System.out.println("  │  1. Name (A → Z)            │");
                        System.out.println("  │  2. GPA  (High → Low)       │");
                        System.out.println("  │  3. Attendance (High → Low)  │");
                        System.out.println("  └──────────────────────────────┘");
                        System.out.print("  ➤ Enter your choice: ");
                        int sortChoice = scanner.nextInt();
                        scanner.nextLine(); // consume newline

                        // Perform the sort based on the user's choice
                        switch (sortChoice) {
                            case 1:
                                manager.sortByName();
                                break;
                            case 2:
                                manager.sortByGPA();
                                break;
                            case 3:
                                manager.sortByAttendance();
                                break;
                            default:
                                System.out.println("  ⚠  Invalid sort option.");
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("\n  ⚠  Invalid input! Please enter a number.");
                        scanner.nextLine(); // clear invalid input
                    }
                    break;

                // ─────────────── OPTION 12: GENERATE SUMMARY REPORT ───────────────
                case 12:
                    System.out.println("  📊  SUMMARY REPORT");
                    System.out.println("  ────────────────────────────────────────");

                    // Generate and display a comprehensive summary report
                    manager.generateSummaryReport();
                    break;

                // ─────────────── OPTION 13: ARRAY VS ARRAYLIST ───────────────
                case 13:
                    System.out.println("  📚  ARRAY vs ARRAYLIST COMPARISON");
                    System.out.println("  ────────────────────────────────────────");

                    // Display the formatted comparison table
                    displayArrayVsArrayList();
                    break;

                // ─────────────── OPTION 14: LOAD SAMPLE DATA ───────────────
                case 14:
                    System.out.println("  📦  LOAD SAMPLE DATA");
                    System.out.println("  ────────────────────────────────────────");

                    // Load pre-defined sample students for testing
                    manager.loadSampleData();
                    System.out.println("\n  ✅  Sample data loaded successfully!");
                    System.out.println("  Use option 7 to view all loaded students.");
                    break;

                // ─────────────── OPTION 0: EXIT ───────────────
                case 0:
                    // Display a goodbye message with box-drawing art
                    System.out.println();
                    System.out.println("  ╔════════════════════════════════════════════════════╗");
                    System.out.println("  ║                                                    ║");
                    System.out.println("  ║        Thank you for using the                     ║");
                    System.out.println("  ║     STUDENT RECORD MANAGEMENT SYSTEM               ║");
                    System.out.println("  ║                                                    ║");
                    System.out.println("  ║           Goodbye! Have a great day! 👋            ║");
                    System.out.println("  ║                                                    ║");
                    System.out.println("  ╚════════════════════════════════════════════════════╝");
                    System.out.println();

                    // Close the scanner to release system resources
                    scanner.close();

                    // Exit the program
                    return;

                // ─────────────── DEFAULT: INVALID CHOICE ───────────────
                default:
                    System.out.println("  ⚠  Invalid choice! Please select a number between 0 and 14.");
            }

            // After each operation, pause and wait for the user to press Enter
            System.out.println();
            pressEnterToContinue(scanner);
        }
    }

    // ════════════════════════════════════════════════════════
    //  HELPER METHODS
    // ════════════════════════════════════════════════════════

    /**
     * Displays the main menu of the Student Record Management System
     * using box-drawing characters for a professional console appearance.
     */
    public static void displayMenu() {
        System.out.println();
        System.out.println("  ╔════════════════════════════════════════════════════╗");
        System.out.println("  ║     STUDENT RECORD MANAGEMENT SYSTEM               ║");
        System.out.println("  ╠════════════════════════════════════════════════════╣");
        System.out.println("  ║                                                    ║");
        System.out.println("  ║   1.  Add New Student                              ║");
        System.out.println("  ║   2.  Delete Student                               ║");
        System.out.println("  ║   3.  Update Student Information                   ║");
        System.out.println("  ║   4.  Search Student by ID                         ║");
        System.out.println("  ║   5.  Search Student by Name                       ║");
        System.out.println("  ║   6.  Search by Department                         ║");
        System.out.println("  ║   7.  Display All Students                         ║");
        System.out.println("  ║   8.  Add Attendance Record                        ║");
        System.out.println("  ║   9.  View Attendance History                      ║");
        System.out.println("  ║  10.  Calculate & Display GPA                      ║");
        System.out.println("  ║  11.  Sort Students                                ║");
        System.out.println("  ║  12.  Generate Summary Report                      ║");
        System.out.println("  ║  13.  Show Array vs ArrayList Comparison           ║");
        System.out.println("  ║  14.  Load Sample Data                             ║");
        System.out.println("  ║   0.  Exit                                         ║");
        System.out.println("  ║                                                    ║");
        System.out.println("  ╚════════════════════════════════════════════════════╝");
        System.out.println();
    }

    /**
     * Displays an ASCII-art welcome banner when the application starts.
     * Uses box-drawing characters for a clean, professional look.
     */
    public static void displayWelcomeBanner() {
        System.out.println();
        System.out.println("  ╔════════════════════════════════════════════════════╗");
        System.out.println("  ║                                                    ║");
        System.out.println("  ║   ███████╗████████╗██╗   ██╗██████╗ ███████╗       ║");
        System.out.println("  ║   ██╔════╝╚══██╔══╝██║   ██║██╔══██╗██╔════╝       ║");
        System.out.println("  ║   ███████╗   ██║   ██║   ██║██║  ██║█████╗         ║");
        System.out.println("  ║   ╚════██║   ██║   ██║   ██║██║  ██║██╔══╝         ║");
        System.out.println("  ║   ███████║   ██║   ╚██████╔╝██████╔╝███████╗       ║");
        System.out.println("  ║   ╚══════╝   ╚═╝    ╚═════╝ ╚═════╝ ╚══════╝       ║");
        System.out.println("  ║                                                    ║");
        System.out.println("  ║        RECORD  MANAGEMENT  SYSTEM                  ║");
        System.out.println("  ║                                                    ║");
        System.out.println("  ╠════════════════════════════════════════════════════╣");
        System.out.println("  ║                                                    ║");
        System.out.println("  ║   Version : 1.0                                    ║");
        System.out.println("  ║   Type    : Console-Based Application              ║");
        System.out.println("  ║   Project : 1st Year Engineering Mini Project      ║");
        System.out.println("  ║                                                    ║");
        System.out.println("  ║   Data Structures Used:                            ║");
        System.out.println("  ║     • ArrayList  — Student storage                 ║");
        System.out.println("  ║     • HashMap    — Fast ID lookup & grades         ║");
        System.out.println("  ║     • LinkedList — Attendance records              ║");
        System.out.println("  ║                                                    ║");
        System.out.println("  ╚════════════════════════════════════════════════════╝");
        System.out.println();
    }

    /**
     * Displays a formatted comparison table of Array vs ArrayList
     * to help students understand the design choice made in this project.
     * The table uses box-drawing characters for professional output.
     */
    public static void displayArrayVsArrayList() {
        System.out.println();
        System.out.println("  ╔══════════════════╦═══════════════════════╦═══════════════════════════╗");
        System.out.println("  ║ Feature          ║ Array                 ║ ArrayList                 ║");
        System.out.println("  ╠══════════════════╬═══════════════════════╬═══════════════════════════╣");
        System.out.println("  ║ Size             ║ Fixed at creation     ║ Dynamic, grows as needed  ║");
        System.out.println("  ║ Type Safety      ║ No generics           ║ Supports generics         ║");
        System.out.println("  ║ Performance      ║ Slightly faster       ║ Negligible overhead       ║");
        System.out.println("  ║ Memory           ║ Less overhead         ║ Slightly more overhead    ║");
        System.out.println("  ║ Built-in Methods ║ None (manual code)    ║ add, remove, get, etc.    ║");
        System.out.println("  ║ Flexibility      ║ Low                   ║ High                      ║");
        System.out.println("  ║ Use Case         ║ Fixed-size data       ║ Dynamic collections       ║");
        System.out.println("  ╚══════════════════╩═══════════════════════╩═══════════════════════════╝");
        System.out.println();
        System.out.println("  ┌─────────────────────────────────────────────────────────────────────┐");
        System.out.println("  │  CONCLUSION: ArrayList is the better choice for this project        │");
        System.out.println("  │  because we need dynamic sizing, easy CRUD operations, and          │");
        System.out.println("  │  seamless integration with the Java Collections Framework.          │");
        System.out.println("  └─────────────────────────────────────────────────────────────────────┘");
    }

    /**
     * Pauses the program and waits for the user to press the Enter key.
     * This gives the user time to read the output before the menu is
     * displayed again.
     *
     * @param scanner the Scanner instance used to read user input
     */
    public static void pressEnterToContinue(Scanner scanner) {
        System.out.println("  ────────────────────────────────────────────────────");
        System.out.print("  Press Enter to continue...");
        scanner.nextLine();
    }
}
