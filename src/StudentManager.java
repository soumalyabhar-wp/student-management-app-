/*
 * ============================================================================
 *                    STUDENT RECORD MANAGEMENT SYSTEM
 *                        StudentManager.java
 * ============================================================================
 *
 * This class is the CORE ENGINE of our application. It handles all business
 * logic — adding, deleting, updating, searching, sorting, and reporting
 * on student records.
 *
 * ── WHY THESE DATA STRUCTURES? ──────────────────────────────────────────────
 *
 *  1. ArrayList<Student> studentList
 *     ➤ ArrayList is a resizable array (grows automatically when full).
 *     ➤ We use it as the PRIMARY storage because:
 *        - It maintains insertion order, so students appear in the sequence
 *          they were added.
 *        - It supports index-based access, which makes iterating through
 *          all students fast and simple.
 *        - Sorting (Collections.sort) works directly on ArrayLists.
 *        - It is the most commonly used collection in Java — perfect for
 *          a beginner-friendly project.
 *     ➤ Trade-off: Searching by ID in an ArrayList is O(n) — we solve
 *        this with the HashMap below.
 *
 *  2. HashMap<Integer, Student> studentMap
 *     ➤ HashMap stores key-value pairs and provides O(1) average-time
 *        lookups by key.
 *     ➤ We map Student ID → Student object so that any ID-based search,
 *        update, or delete is nearly instant regardless of how many
 *        students are in the system.
 *     ➤ This is a classic "dual data structure" pattern: ArrayList for
 *        ordered traversal, HashMap for fast keyed access.
 *
 * ── DESIGN NOTES ────────────────────────────────────────────────────────────
 *  - Both collections always stay in sync — every add/delete touches BOTH.
 *  - Validation is done at this layer (e.g., duplicate IDs, age range,
 *    attendance bounds) to keep the Student class clean.
 *  - Display methods use Unicode box-drawing characters for a polished,
 *    professional console UI.
 *
 * @author  Student Record Management System Team
 * @version 1.0
 * @since   2025-08-01
 * ============================================================================
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class StudentManager {

    // ════════════════════════════════════════════════════════════════════════
    //                              FIELDS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Primary storage for all students.
     * ArrayList preserves insertion order and supports indexed access,
     * making it ideal for iteration, display, and sorting operations.
     */
    private ArrayList<Student> studentList;

    /**
     * Secondary lookup structure for O(1) ID-based access.
     * Maps each student's unique ID (Integer) to the Student object,
     * so we never have to loop through the entire list just to find one student.
     */
    private HashMap<Integer, Student> studentMap;

    // ════════════════════════════════════════════════════════════════════════
    //                            CONSTRUCTOR
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Constructs a new StudentManager with empty collections.
     * Both the ArrayList and HashMap are initialized and ready to accept
     * student records.
     */
    public StudentManager() {
        // Initialize the ArrayList — our ordered, iterable storage
        this.studentList = new ArrayList<>();

        // Initialize the HashMap — our fast-lookup index
        this.studentMap = new HashMap<>();
    }

    // ════════════════════════════════════════════════════════════════════════
    //                        CRUD METHODS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Adds a new student to the system after validating that the ID
     * is not already taken. The student is added to BOTH the ArrayList
     * and the HashMap to keep them synchronized.
     *
     * @param student the Student object to add
     */
    public void addStudent(Student student) {
        // Guard clause: check for null input
        if (student == null) {
            System.out.println("  ⚠  Error: Cannot add a null student.");
            return;
        }

        // Check for duplicate ID using HashMap (O(1) lookup — very fast!)
        if (studentMap.containsKey(student.getId())) {
            System.out.println("  ⚠  Error: A student with ID " + student.getId()
                    + " already exists. Each student must have a unique ID.");
            return;
        }

        // Add to both data structures to keep them in sync
        studentList.add(student);           // Append to end of the list
        studentMap.put(student.getId(), student);  // Index by ID in the map

        System.out.println("  ✔  Student added successfully: "
                + student.getName() + " (ID: " + student.getId() + ")");
    }

    /**
     * Deletes a student from the system by their unique ID.
     * Removes from BOTH the ArrayList and the HashMap.
     *
     * @param id the unique ID of the student to delete
     */
    public void deleteStudent(int id) {
        // First, try to find the student in our fast-lookup map
        Student student = studentMap.get(id);

        if (student == null) {
            // Student not found — inform the user
            System.out.println("  ⚠  Student with ID " + id + " not found.");
            return;
        }

        // Remove from both collections
        studentList.remove(student);   // ArrayList.remove(Object) — removes first occurrence
        studentMap.remove(id);         // HashMap.remove(key) — removes the key-value pair

        System.out.println("  ✔  Student deleted successfully: "
                + student.getName() + " (ID: " + id + ")");
    }

    /**
     * Updates the name of an existing student.
     *
     * @param id      the unique ID of the student to update
     * @param newName the new name to assign
     */
    public void updateStudentName(int id, String newName) {
        // Look up the student via HashMap for O(1) access
        Student student = studentMap.get(id);

        if (student == null) {
            System.out.println("  ⚠  Student with ID " + id + " not found.");
            return;
        }

        // Validate the new name
        if (newName == null || newName.trim().isEmpty()) {
            System.out.println("  ⚠  Error: Name cannot be empty.");
            return;
        }

        String oldName = student.getName();
        student.setName(newName.trim());
        System.out.println("  ✔  Name updated: \"" + oldName + "\" → \"" + newName.trim() + "\"");
    }

    /**
     * Updates the age of an existing student with validation.
     * Age must be between 16 and 60 (typical college age range).
     *
     * @param id     the unique ID of the student to update
     * @param newAge the new age to assign (must be 16–60)
     */
    public void updateStudentAge(int id, int newAge) {
        Student student = studentMap.get(id);

        if (student == null) {
            System.out.println("  ⚠  Student with ID " + id + " not found.");
            return;
        }

        // Validate age is within a reasonable college range
        if (newAge < 16 || newAge > 60) {
            System.out.println("  ⚠  Error: Age must be between 16 and 60. You entered: " + newAge);
            return;
        }

        int oldAge = student.getAge();
        student.setAge(newAge);
        System.out.println("  ✔  Age updated for " + student.getName()
                + ": " + oldAge + " → " + newAge);
    }

    /**
     * Updates the department of an existing student.
     *
     * @param id      the unique ID of the student to update
     * @param newDept the new department name to assign
     */
    public void updateStudentDepartment(int id, String newDept) {
        Student student = studentMap.get(id);

        if (student == null) {
            System.out.println("  ⚠  Student with ID " + id + " not found.");
            return;
        }

        if (newDept == null || newDept.trim().isEmpty()) {
            System.out.println("  ⚠  Error: Department cannot be empty.");
            return;
        }

        String oldDept = student.getDepartment();
        student.setDepartment(newDept.trim());
        System.out.println("  ✔  Department updated for " + student.getName()
                + ": \"" + oldDept + "\" → \"" + newDept.trim() + "\"");
    }

    /**
     * Updates the attendance percentage of a student.
     * Attendance must be between 0.0 and 100.0.
     *
     * @param id            the unique ID of the student
     * @param newAttendance the new attendance percentage (0.0–100.0)
     */
    public void updateStudentAttendance(int id, double newAttendance) {
        Student student = studentMap.get(id);

        if (student == null) {
            System.out.println("  ⚠  Student with ID " + id + " not found.");
            return;
        }

        // Validate attendance is a valid percentage
        if (newAttendance < 0.0 || newAttendance > 100.0) {
            System.out.println("  ⚠  Error: Attendance must be between 0% and 100%. "
                    + "You entered: " + newAttendance);
            return;
        }

        student.setAttendancePercentage(newAttendance);
        System.out.println("  ✔  Attendance updated for " + student.getName()
                + ": " + String.format("%.2f%%", newAttendance));
    }

    /**
     * Adds a grade (subject + marks) to a student's record.
     * Marks must be between 0.0 and 100.0. After adding, the student's
     * GPA is automatically recalculated.
     *
     * @param id      the unique ID of the student
     * @param subject the subject name (e.g., "Mathematics")
     * @param marks   the marks scored (0.0–100.0)
     */
    public void addGrade(int id, String subject, double marks) {
        Student student = studentMap.get(id);

        if (student == null) {
            System.out.println("  ⚠  Student with ID " + id + " not found.");
            return;
        }

        // Validate subject name
        if (subject == null || subject.trim().isEmpty()) {
            System.out.println("  ⚠  Error: Subject name cannot be empty.");
            return;
        }

        // Validate marks range
        if (marks < 0.0 || marks > 100.0) {
            System.out.println("  ⚠  Error: Marks must be between 0 and 100. "
                    + "You entered: " + marks);
            return;
        }

        // Add the grade to the student's grades map
        student.addGrade(subject.trim(), marks);
        System.out.println("  ✔  Grade added for " + student.getName()
                + ": " + subject.trim() + " = " + marks + " marks");
    }

    // ════════════════════════════════════════════════════════════════════════
    //                         SEARCH METHODS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Searches for a student by their unique ID using the HashMap.
     * This is an O(1) operation — the fastest possible lookup!
     *
     * @param id the unique ID to search for
     * @return the Student object if found, or null if not found
     */
    public Student searchById(int id) {
        // HashMap.get() returns null if the key doesn't exist
        return studentMap.get(id);
    }

    /**
     * Searches for students whose names contain the given search term.
     * This is a PARTIAL, CASE-INSENSITIVE match — for example, searching
     * "rah" would match "Rahul Sharma".
     *
     * Since we can't use the HashMap for name-based searches (it's keyed
     * by ID), we loop through the entire ArrayList. This is O(n).
     *
     * @param name the name (or partial name) to search for
     * @return an ArrayList of all matching students (may be empty)
     */
    public ArrayList<Student> searchByName(String name) {
        ArrayList<Student> results = new ArrayList<>();

        if (name == null || name.trim().isEmpty()) {
            return results;  // Return empty list for invalid input
        }

        // Convert search term to lowercase for case-insensitive comparison
        String searchTerm = name.trim().toLowerCase();

        // Iterate through all students and check for partial match
        for (Student student : studentList) {
            if (student.getName().toLowerCase().contains(searchTerm)) {
                results.add(student);
            }
        }

        return results;
    }

    /**
     * Searches for all students belonging to a specific department.
     * The comparison is case-insensitive for user convenience.
     *
     * @param department the department name to search for
     * @return an ArrayList of all students in that department (may be empty)
     */
    public ArrayList<Student> searchByDepartment(String department) {
        ArrayList<Student> results = new ArrayList<>();

        if (department == null || department.trim().isEmpty()) {
            return results;
        }

        String searchDept = department.trim().toLowerCase();

        for (Student student : studentList) {
            if (student.getDepartment().toLowerCase().contains(searchDept)) {
                results.add(student);
            }
        }

        return results;
    }

    // ════════════════════════════════════════════════════════════════════════
    //                         DISPLAY METHODS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Displays ALL students in a professionally formatted table using
     * Unicode box-drawing characters. Each row shows: ID, Name, Age,
     * Department, Admission Date, Attendance %, and GPA.
     *
     * Uses String.format() for precise column alignment.
     */
    public void displayAllStudents() {
        if (studentList.isEmpty()) {
            System.out.println("\n  ℹ  No students in the system yet.\n");
            return;
        }

        // ── Define column widths ──
        // These widths are chosen to accommodate typical data lengths
        int idW    = 6;   // e.g., "1001"
        int nameW  = 18;  // e.g., "Rahul Sharma"
        int ageW   = 5;   // e.g., "18"
        int deptW  = 16;  // e.g., "Computer Science" (shortened to fit)
        int admW   = 14;  // e.g., "01-08-2025"
        int attW   = 12;  // e.g., "92.50%"
        int gpaW   = 7;   // e.g., "8.40"

        // ── Build the horizontal border lines ──
        String topBorder = "╔" + "═".repeat(idW) + "╦" + "═".repeat(nameW) + "╦"
                + "═".repeat(ageW) + "╦" + "═".repeat(deptW) + "╦"
                + "═".repeat(admW) + "╦" + "═".repeat(attW) + "╦"
                + "═".repeat(gpaW) + "╗";

        String headerSep = "╠" + "═".repeat(idW) + "╬" + "═".repeat(nameW) + "╬"
                + "═".repeat(ageW) + "╬" + "═".repeat(deptW) + "╬"
                + "═".repeat(admW) + "╬" + "═".repeat(attW) + "╬"
                + "═".repeat(gpaW) + "╣";

        String bottomBorder = "╚" + "═".repeat(idW) + "╩" + "═".repeat(nameW) + "╩"
                + "═".repeat(ageW) + "╩" + "═".repeat(deptW) + "╩"
                + "═".repeat(admW) + "╩" + "═".repeat(attW) + "╩"
                + "═".repeat(gpaW) + "╝";

        String rowSep = "╟" + "─".repeat(idW) + "╫" + "─".repeat(nameW) + "╫"
                + "─".repeat(ageW) + "╫" + "─".repeat(deptW) + "╫"
                + "─".repeat(admW) + "╫" + "─".repeat(attW) + "╫"
                + "─".repeat(gpaW) + "╢";

        // ── Print the table ──
        System.out.println("\n  ╔══════════════════════════════════════════════════════════════╗");
        System.out.println("  ║              📋  ALL STUDENT RECORDS                        ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════════╝\n");

        // Top border
        System.out.println(topBorder);

        // Header row — uses String.format for precise alignment
        System.out.printf("║%-" + idW + "s║%-" + nameW + "s║%-" + ageW + "s║%-"
                        + deptW + "s║%-" + admW + "s║%-" + attW + "s║%-" + gpaW + "s║%n",
                centerText("ID", idW),
                centerText("Name", nameW),
                centerText("Age", ageW),
                centerText("Department", deptW),
                centerText("Admission", admW),
                centerText("Attendance", attW),
                centerText("GPA", gpaW));

        // Separator between header and data
        System.out.println(headerSep);

        // Data rows — one row per student
        for (int i = 0; i < studentList.size(); i++) {
            Student s = studentList.get(i);

            // Truncate department name if it's too long for the column
            String dept = s.getDepartment();
            if (dept.length() > deptW - 2) {
                dept = dept.substring(0, deptW - 2);
            }

            // Format each field with proper alignment
            // %-Ns  = left-aligned string in N characters
            // %Nd   = right-aligned integer in N characters
            System.out.printf("║ %-" + (idW - 1) + "d"      // ID (left-aligned number)
                            + "║ %-" + (nameW - 1) + "s"     // Name (left-aligned)
                            + "║ %" + (ageW - 1) + "d"       // Age (right-aligned)
                            + "║ %-" + (deptW - 1) + "s"     // Department (left-aligned)
                            + "║ %-" + (admW - 1) + "s"      // Admission Date
                            + "║ %" + (attW - 1) + "s"       // Attendance (right-aligned)
                            + "║ %" + (gpaW - 1) + "s"       // GPA (right-aligned)
                            + "║%n",
                    s.getId(),
                    truncate(s.getName(), nameW - 2),
                    s.getAge(),
                    truncate(dept, deptW - 2),
                    s.getAdmissionDate(),
                    String.format("%.2f%%", s.getAttendancePercentage()),
                    String.format("%.2f", GradeCalculator.calculateGPA(s.getGrades())));

            // Print row separator between students (but not after the last one)
            if (i < studentList.size() - 1) {
                System.out.println(rowSep);
            }
        }

        // Bottom border
        System.out.println(bottomBorder);
        System.out.println("  Total Students: " + studentList.size() + "\n");
    }

    /**
     * Displays a detailed view of a single student, including personal
     * information, all grades with marks, and attendance history.
     *
     * @param s the Student object to display in detail
     */
    public void displayStudentDetails(Student s) {
        if (s == null) {
            System.out.println("  ⚠  No student to display.");
            return;
        }

        int boxWidth = 56;
        String hLine = "═".repeat(boxWidth);

        System.out.println();
        System.out.println("╔" + hLine + "╗");
        System.out.println("║" + centerText("📄  STUDENT DETAILS", boxWidth) + "║");
        System.out.println("╠" + hLine + "╣");

        // ── Personal Information ──
        System.out.printf("║  %-18s : %-" + (boxWidth - 23) + "s║%n", "Student ID", s.getId());
        System.out.printf("║  %-18s : %-" + (boxWidth - 23) + "s║%n", "Name", s.getName());
        System.out.printf("║  %-18s : %-" + (boxWidth - 23) + "d║%n", "Age", s.getAge());
        System.out.printf("║  %-18s : %-" + (boxWidth - 23) + "s║%n", "Department", s.getDepartment());
        System.out.printf("║  %-18s : %-" + (boxWidth - 23) + "s║%n", "Admission Date", s.getAdmissionDate());
        System.out.printf("║  %-18s : %-" + (boxWidth - 23) + "s║%n", "Attendance",
                String.format("%.2f%%", s.getAttendancePercentage()));
        System.out.printf("║  %-18s : %-" + (boxWidth - 23) + "s║%n", "GPA",
                String.format("%.2f", GradeCalculator.calculateGPA(s.getGrades())));

        // ── Grades Section ──
        System.out.println("╠" + hLine + "╣");
        System.out.println("║" + centerText("📝  GRADES", boxWidth) + "║");
        System.out.println("╠" + hLine + "╣");

        HashMap<String, Double> grades = s.getGrades();
        if (grades.isEmpty()) {
            System.out.printf("║  %-" + (boxWidth - 2) + "s║%n", "No grades recorded yet.");
        } else {
            // Print each subject and its marks
            System.out.printf("║  %-25s  %-10s  %-" + (boxWidth - 41) + "s║%n",
                    "Subject", "Marks", "Grade");
            System.out.println("║  " + "─".repeat(boxWidth - 4) + "  ║");

            for (Map.Entry<String, Double> entry : grades.entrySet()) {
                String letterGrade = getLetterGrade(entry.getValue());
                System.out.printf("║  %-25s  %-10.2f  %-" + (boxWidth - 41) + "s║%n",
                        entry.getKey(), entry.getValue(), letterGrade);
            }
        }

        // ── Attendance History Section ──
        System.out.println("╠" + hLine + "╣");
        System.out.println("║" + centerText("📅  ATTENDANCE HISTORY", boxWidth) + "║");
        System.out.println("╠" + hLine + "╣");

        LinkedList<AttendanceRecord> records = s.getAttendanceLog();
        if (records.isEmpty()) {
            System.out.printf("║  %-" + (boxWidth - 2) + "s║%n", "No attendance records yet.");
        } else {
            System.out.printf("║  %-14s  %-10s  %-" + (boxWidth - 30) + "s║%n",
                    "Date", "Status", "Remarks");
            System.out.println("║  " + "─".repeat(boxWidth - 4) + "  ║");

            for (AttendanceRecord record : records) {
                System.out.printf("║  %-14s  %-10s  %-" + (boxWidth - 30) + "s║%n",
                        record.getDate(),
                        record.isPresent() ? "Present" : "Absent",
                        truncate(record.getRemarks(), boxWidth - 30));
            }
        }

        // Bottom border
        System.out.println("╚" + hLine + "╝");
        System.out.println();
    }

    // ════════════════════════════════════════════════════════════════════════
    //                          SORT METHODS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Sorts the student list alphabetically by name (A → Z).
     * Uses Collections.sort() with a Comparator that compares names
     * in a case-insensitive manner.
     */
    public void sortByName() {
        if (studentList.isEmpty()) {
            System.out.println("  ℹ  No students to sort.");
            return;
        }

        // Collections.sort modifies the list in-place
        // The Comparator uses compareToIgnoreCase for case-insensitive alphabetical order
        Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return s1.getName().compareToIgnoreCase(s2.getName());
            }
        });

        System.out.println("  ✔  Students sorted by Name (A → Z).");
    }

    /**
     * Sorts the student list by GPA in descending order (highest first).
     * This makes it easy to see top-performing students at the top.
     *
     * Note: We reverse the comparison (s2 - s1) to get descending order.
     */
    public void sortByGPA() {
        if (studentList.isEmpty()) {
            System.out.println("  ℹ  No students to sort.");
            return;
        }

        Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                // Double.compare returns negative if s2's GPA > s1's GPA
                // So this gives descending order
                return Double.compare(GradeCalculator.calculateGPA(s2.getGrades()), GradeCalculator.calculateGPA(s1.getGrades()));
            }
        });

        System.out.println("  ✔  Students sorted by GPA (Highest → Lowest).");
    }

    /**
     * Sorts the student list by attendance percentage in descending order.
     * Students with the highest attendance appear first.
     */
    public void sortByAttendance() {
        if (studentList.isEmpty()) {
            System.out.println("  ℹ  No students to sort.");
            return;
        }

        Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Double.compare(s2.getAttendancePercentage(),
                        s1.getAttendancePercentage());
            }
        });

        System.out.println("  ✔  Students sorted by Attendance (Highest → Lowest).");
    }

    // ════════════════════════════════════════════════════════════════════════
    //                       ATTENDANCE METHODS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Adds an attendance record to a student's attendance history and
     * automatically recalculates their overall attendance percentage.
     *
     * The attendance percentage is calculated as:
     *   (number of "present" records / total records) × 100
     *
     * @param id     the unique ID of the student
     * @param record the AttendanceRecord to add
     */
    public void addAttendanceRecord(int id, AttendanceRecord record) {
        Student student = studentMap.get(id);

        if (student == null) {
            System.out.println("  ⚠  Student with ID " + id + " not found.");
            return;
        }

        if (record == null) {
            System.out.println("  ⚠  Error: Attendance record cannot be null.");
            return;
        }

        // Add the record to the student's LinkedList of attendance
        student.getAttendanceLog().add(record);

        // ── Recalculate attendance percentage ──
        // Count how many days the student was present
        LinkedList<AttendanceRecord> allRecords = student.getAttendanceLog();
        int totalDays = allRecords.size();
        int presentDays = 0;

        for (AttendanceRecord rec : allRecords) {
            if (rec.isPresent()) {
                presentDays++;
            }
        }

        // Calculate and update the percentage
        double percentage = (totalDays > 0) ? ((double) presentDays / totalDays) * 100.0 : 0.0;
        student.setAttendancePercentage(percentage);

        System.out.println("  ✔  Attendance recorded for " + student.getName()
                + " on " + record.getDate()
                + " — " + (record.isPresent() ? "Present" : "Absent")
                + " (Overall: " + String.format("%.2f%%", percentage) + ")");
    }

    /**
     * Displays the complete attendance history for a student in a
     * neatly formatted table, including the date, status, and remarks
     * for each record.
     *
     * @param id the unique ID of the student
     */
    public void viewAttendanceHistory(int id) {
        Student student = studentMap.get(id);

        if (student == null) {
            System.out.println("  ⚠  Student with ID " + id + " not found.");
            return;
        }

        LinkedList<AttendanceRecord> records = student.getAttendanceLog();

        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║           📅  ATTENDANCE HISTORY                      ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.printf("║  Student : %-43s║%n", student.getName() + " (ID: " + id + ")");
        System.out.printf("║  Overall : %-43s║%n",
                String.format("%.2f%%", student.getAttendancePercentage()));
        System.out.println("╠════════════════════════════════════════════════════════╣");

        if (records.isEmpty()) {
            System.out.println("║  No attendance records found.                        ║");
        } else {
            // Table header for records
            System.out.printf("║  %-4s  %-14s  %-10s  %-18s║%n",
                    "No.", "Date", "Status", "Remarks");
            System.out.println("║  " + "─".repeat(52) + "  ║");

            int count = 1;
            for (AttendanceRecord record : records) {
                System.out.printf("║  %-4d  %-14s  %-10s  %-18s║%n",
                        count++,
                        record.getDate(),
                        record.isPresent() ? "✔ Present" : "✘ Absent",
                        truncate(record.getRemarks(), 18));
            }
        }

        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    // ════════════════════════════════════════════════════════════════════════
    //                        REPORT GENERATION
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Generates and displays a comprehensive summary report of all
     * students in the system. The report includes:
     *   - Total number of students
     *   - Department-wise student count
     *   - Average GPA across all students
     *   - Highest and lowest GPA students
     *   - Average attendance
     *   - List of students with attendance below 75%
     */
    public void generateSummaryReport() {
        if (studentList.isEmpty()) {
            System.out.println("\n  ℹ  No students in the system. Cannot generate report.\n");
            return;
        }

        int boxWidth = 60;
        String hLine = "═".repeat(boxWidth);

        System.out.println();
        System.out.println("╔" + hLine + "╗");
        System.out.println("║" + centerText("📊  SUMMARY REPORT", boxWidth) + "║");
        System.out.println("╠" + hLine + "╣");

        // ── 1. Total Students ──
        System.out.printf("║  %-35s : %-" + (boxWidth - 40) + "d║%n",
                "Total Students", studentList.size());

        // ── 2. Department-wise Count ──
        // We use a HashMap to count students per department
        System.out.println("╠" + hLine + "╣");
        System.out.println("║" + centerText("Department-wise Count", boxWidth) + "║");
        System.out.println("║  " + "─".repeat(boxWidth - 4) + "  ║");

        HashMap<String, Integer> deptCount = new HashMap<>();
        for (Student student : studentList) {
            String dept = student.getDepartment();
            // getOrDefault: returns current count or 0 if dept not yet in the map
            deptCount.put(dept, deptCount.getOrDefault(dept, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : deptCount.entrySet()) {
            System.out.printf("║    %-33s : %-" + (boxWidth - 42) + "d║%n",
                    entry.getKey(), entry.getValue());
        }

        // ── 3. GPA Statistics ──
        System.out.println("╠" + hLine + "╣");
        System.out.println("║" + centerText("GPA Statistics", boxWidth) + "║");
        System.out.println("║  " + "─".repeat(boxWidth - 4) + "  ║");

        double totalGPA = 0;
        Student highestGPAStudent = studentList.get(0);
        Student lowestGPAStudent = studentList.get(0);

        for (Student student : studentList) {
            double gpa = GradeCalculator.calculateGPA(student.getGrades());
            totalGPA += gpa;

            if (gpa > GradeCalculator.calculateGPA(highestGPAStudent.getGrades())) {
                highestGPAStudent = student;
            }
            if (gpa < GradeCalculator.calculateGPA(lowestGPAStudent.getGrades())) {
                lowestGPAStudent = student;
            }
        }

        double averageGPA = totalGPA / studentList.size();

        System.out.printf("║  %-35s : %-" + (boxWidth - 40) + "s║%n",
                "Average GPA", String.format("%.2f", averageGPA));
        System.out.printf("║  %-35s : %-" + (boxWidth - 40) + "s║%n",
                "Highest GPA",
                highestGPAStudent.getName() + " (" + String.format("%.2f", GradeCalculator.calculateGPA(highestGPAStudent.getGrades())) + ")");
        System.out.printf("║  %-35s : %-" + (boxWidth - 40) + "s║%n",
                "Lowest GPA",
                lowestGPAStudent.getName() + " (" + String.format("%.2f", GradeCalculator.calculateGPA(lowestGPAStudent.getGrades())) + ")");

        // ── 4. Attendance Statistics ──
        System.out.println("╠" + hLine + "╣");
        System.out.println("║" + centerText("Attendance Statistics", boxWidth) + "║");
        System.out.println("║  " + "─".repeat(boxWidth - 4) + "  ║");

        double totalAttendance = 0;
        ArrayList<Student> lowAttendance = new ArrayList<>();

        for (Student student : studentList) {
            totalAttendance += student.getAttendancePercentage();

            // Flag students with attendance below 75%
            if (student.getAttendancePercentage() < 75.0) {
                lowAttendance.add(student);
            }
        }

        double averageAttendance = totalAttendance / studentList.size();

        System.out.printf("║  %-35s : %-" + (boxWidth - 40) + "s║%n",
                "Average Attendance", String.format("%.2f%%", averageAttendance));

        // ── 5. Low Attendance Warning ──
        System.out.println("╠" + hLine + "╣");
        System.out.println("║" + centerText("⚠  Students Below 75% Attendance", boxWidth) + "║");
        System.out.println("║  " + "─".repeat(boxWidth - 4) + "  ║");

        if (lowAttendance.isEmpty()) {
            System.out.printf("║  %-" + (boxWidth - 2) + "s║%n",
                    "All students have attendance above 75%. 🎉");
        } else {
            for (Student student : lowAttendance) {
                System.out.printf("║    ⚠ %-30s  Attendance: %-" + (boxWidth - 46) + "s║%n",
                        student.getName() + " (ID: " + student.getId() + ")",
                        String.format("%.2f%%", student.getAttendancePercentage()));
            }
        }

        System.out.println("╚" + hLine + "╝");
        System.out.println();
    }

    // ════════════════════════════════════════════════════════════════════════
    //                         SAMPLE DATA
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Loads 5 sample students with realistic Indian names, departments,
     * grades for 5 subjects, and attendance records. This is useful for
     * testing and demonstrating the system without manual data entry.
     *
     * Sample Students:
     *   1001 — Rahul Sharma,  18, Computer Science
     *   1002 — Priya Patel,   19, Electronics
     *   1003 — Amit Kumar,    18, Mechanical
     *   1004 — Sneha Reddy,   19, Computer Science
     *   1005 — Vikram Singh,  20, Civil
     */
    public void loadSampleData() {
        System.out.println("\n  ⏳  Loading sample data...\n");

        // ── Student 1: Rahul Sharma ──
        Student s1 = new Student(1001, "Rahul Sharma", 18, "Computer Science", "01-08-2025");
        s1.addGrade("Mathematics", 85.0);
        s1.addGrade("Physics", 78.0);
        s1.addGrade("Chemistry", 72.0);
        s1.addGrade("English", 88.0);
        s1.addGrade("Programming", 95.0);
        addStudent(s1);
        addAttendanceRecord(1001, new AttendanceRecord("01-08-2025", true, "First day"));
        addAttendanceRecord(1001, new AttendanceRecord("02-08-2025", true, "Regular class"));
        addAttendanceRecord(1001, new AttendanceRecord("03-08-2025", true, "Lab session"));
        addAttendanceRecord(1001, new AttendanceRecord("04-08-2025", false, "Medical leave"));
        addAttendanceRecord(1001, new AttendanceRecord("05-08-2025", true, "Regular class"));

        // ── Student 2: Priya Patel ──
        Student s2 = new Student(1002, "Priya Patel", 19, "Electronics", "15-07-2025");
        s2.addGrade("Mathematics", 92.0);
        s2.addGrade("Physics", 88.0);
        s2.addGrade("Chemistry", 79.0);
        s2.addGrade("English", 91.0);
        s2.addGrade("Programming", 85.0);
        addStudent(s2);
        addAttendanceRecord(1002, new AttendanceRecord("01-08-2025", true, "First day"));
        addAttendanceRecord(1002, new AttendanceRecord("02-08-2025", true, "Regular class"));
        addAttendanceRecord(1002, new AttendanceRecord("03-08-2025", true, "Lab session"));
        addAttendanceRecord(1002, new AttendanceRecord("04-08-2025", true, "Regular class"));
        addAttendanceRecord(1002, new AttendanceRecord("05-08-2025", true, "Regular class"));

        // ── Student 3: Amit Kumar ──
        Student s3 = new Student(1003, "Amit Kumar", 18, "Mechanical", "20-07-2025");
        s3.addGrade("Mathematics", 65.0);
        s3.addGrade("Physics", 70.0);
        s3.addGrade("Chemistry", 68.0);
        s3.addGrade("English", 72.0);
        s3.addGrade("Programming", 60.0);
        addStudent(s3);
        addAttendanceRecord(1003, new AttendanceRecord("01-08-2025", true, "First day"));
        addAttendanceRecord(1003, new AttendanceRecord("02-08-2025", false, "Personal leave"));
        addAttendanceRecord(1003, new AttendanceRecord("03-08-2025", true, "Lab session"));
        addAttendanceRecord(1003, new AttendanceRecord("04-08-2025", false, "Sick leave"));
        addAttendanceRecord(1003, new AttendanceRecord("05-08-2025", true, "Regular class"));

        // ── Student 4: Sneha Reddy ──
        Student s4 = new Student(1004, "Sneha Reddy", 19, "Computer Science", "01-08-2025");
        s4.addGrade("Mathematics", 90.0);
        s4.addGrade("Physics", 85.0);
        s4.addGrade("Chemistry", 88.0);
        s4.addGrade("English", 94.0);
        s4.addGrade("Programming", 92.0);
        addStudent(s4);
        addAttendanceRecord(1004, new AttendanceRecord("01-08-2025", true, "First day"));
        addAttendanceRecord(1004, new AttendanceRecord("02-08-2025", true, "Regular class"));
        addAttendanceRecord(1004, new AttendanceRecord("03-08-2025", true, "Lab session"));
        addAttendanceRecord(1004, new AttendanceRecord("04-08-2025", true, "Regular class"));
        addAttendanceRecord(1004, new AttendanceRecord("05-08-2025", false, "Family event"));

        // ── Student 5: Vikram Singh ──
        Student s5 = new Student(1005, "Vikram Singh", 20, "Civil", "25-07-2025");
        s5.addGrade("Mathematics", 75.0);
        s5.addGrade("Physics", 82.0);
        s5.addGrade("Chemistry", 78.0);
        s5.addGrade("English", 80.0);
        s5.addGrade("Programming", 70.0);
        addStudent(s5);
        addAttendanceRecord(1005, new AttendanceRecord("01-08-2025", true, "First day"));
        addAttendanceRecord(1005, new AttendanceRecord("02-08-2025", true, "Regular class"));
        addAttendanceRecord(1005, new AttendanceRecord("03-08-2025", false, "Transport issue"));
        addAttendanceRecord(1005, new AttendanceRecord("04-08-2025", true, "Regular class"));
        addAttendanceRecord(1005, new AttendanceRecord("05-08-2025", true, "Regular class"));

        System.out.println("\n  ✔  Sample data loaded successfully! (5 students added)\n");
    }

    // ════════════════════════════════════════════════════════════════════════
    //                        UTILITY METHODS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Returns the total number of students currently in the system.
     *
     * @return the number of students stored in the ArrayList
     */
    public int getStudentCount() {
        return studentList.size();
    }

    /**
     * Checks whether a student with the given ID already exists in
     * the system. Uses the HashMap for O(1) lookup speed.
     *
     * @param id the student ID to check
     * @return true if a student with this ID exists, false otherwise
     */
    public boolean isIdExists(int id) {
        return studentMap.containsKey(id);
    }

    // ════════════════════════════════════════════════════════════════════════
    //                    PRIVATE HELPER METHODS
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Centers a text string within a given width by adding spaces on
     * both sides. Used for table headers and section titles.
     *
     * Example: centerText("Hello", 11) returns "   Hello   "
     *
     * @param text  the text to center
     * @param width the total width to center within
     * @return the centered string
     */
    private String centerText(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }

        int totalPadding = width - text.length();
        int leftPadding  = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;

        return " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
    }

    /**
     * Truncates a string to the given maximum length, adding ".." at
     * the end if truncation occurred. This prevents table columns from
     * overflowing.
     *
     * @param text      the text to potentially truncate
     * @param maxLength the maximum allowed length
     * @return the (possibly truncated) string
     */
    private String truncate(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 2) + "..";
    }

    /**
     * Converts a numerical mark (0–100) to a letter grade.
     * Grading scale:
     *   90–100 = A+    80–89 = A    70–79 = B+
     *   60–69  = B     50–59 = C    40–49 = D
     *   Below 40 = F
     *
     * @param marks the numerical marks (0–100)
     * @return the corresponding letter grade as a String
     */
    private String getLetterGrade(double marks) {
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B+";
        if (marks >= 60) return "B";
        if (marks >= 50) return "C";
        if (marks >= 40) return "D";
        return "F";
    }
}
