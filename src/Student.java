import java.util.HashMap;
import java.util.LinkedList;

/**
 * ╔══════════════════════════════════════════════════════════════════════╗
 * ║                         Student.java                                ║
 * ║         Student Record Management System — Model Class              ║
 * ╚══════════════════════════════════════════════════════════════════════╝
 *
 * This class models a single student in the record management system.
 * It stores personal details, academic grades, and an attendance log.
 *
 * <h3>OOP Concepts demonstrated</h3>
 * <ul>
 *   <li><b>Encapsulation</b> — all fields are {@code private}; access is
 *       through public getters / setters that enforce validation rules.</li>
 *   <li><b>Constructor Overloading</b> — a default (no-arg) constructor and
 *       a parameterized constructor give callers flexibility when creating
 *       Student objects.</li>
 *   <li><b>Comparable Interface</b> — implementing {@code Comparable<Student>}
 *       lets us sort a list of students by their roll number using
 *       {@code Collections.sort()}.</li>
 *   <li><b>toString Override</b> — provides a human-readable summary of a
 *       Student object, useful for debugging and console display.</li>
 *   <li><b>Collections (HashMap &amp; LinkedList)</b> — grades are stored in
 *       a HashMap (fast lookup by subject name) and attendance history in a
 *       LinkedList (efficient chronological append).</li>
 * </ul>
 *
 * @author  Student Record Management System
 * @version 1.0
 */
public class Student implements Comparable<Student> {

    // ════════════════════════════════════════════════════════════════
    //                          FIELDS
    // ════════════════════════════════════════════════════════════════
    /*
     * OOP Concept — Encapsulation:
     * By making every field 'private', we hide the internal data from
     * other classes.  The only way to read or modify these values is
     * through the public getter/setter methods defined below.  This
     * lets us add validation (e.g. "age must be positive") without
     * changing how other classes interact with Student objects.
     */

    /** Unique student roll number. */
    private int id;

    /** Full name of the student. */
    private String name;

    /** Age of the student (must be greater than 0). */
    private int age;

    /** Department the student belongs to (e.g. "Computer Science"). */
    private String department;

    /**
     * Date of admission in DD-MM-YYYY format.
     * Example: "15-07-2025"
     */
    private String admissionDate;

    /**
     * Overall attendance percentage (0–100).
     * Recalculated whenever attendance records are updated.
     */
    private double attendancePercentage;

    /**
     * Maps a subject name (e.g. "Mathematics") to the student's marks
     * in that subject (0–100).
     *
     * <p>We use a {@link HashMap} because it provides <b>O(1)</b> average
     * time for look-ups and insertions — ideal when we need to quickly
     * check or update a student's grade for a specific subject.</p>
     */
    private HashMap<String, Double> grades;

    /**
     * Chronological log of attendance entries.
     *
     * <p>We use a {@link LinkedList} because:</p>
     * <ul>
     *   <li>New attendance entries are always <b>appended at the end</b>,
     *       and {@code addLast()} on a LinkedList is O(1).</li>
     *   <li>Reports typically iterate from the first record to the last,
     *       matching the natural traversal order of a linked list.</li>
     *   <li>If a record needs to be removed or corrected, LinkedList can
     *       do it in O(1) once the node is located.</li>
     * </ul>
     */
    private LinkedList<AttendanceRecord> attendanceLog;

    // ════════════════════════════════════════════════════════════════
    //                        CONSTRUCTORS
    // ════════════════════════════════════════════════════════════════
    /*
     * OOP Concept — Constructor Overloading:
     * Java allows multiple constructors in the same class as long as
     * they have different parameter lists.  This is called "overloading".
     *
     * 1. Default constructor — initialises every field to a safe default.
     * 2. Parameterized constructor — lets the caller supply the most
     *    important values right when the object is created.
     *
     * In both cases the collections (grades, attendanceLog) are created
     * as empty so they are ready to use immediately.
     */

    /**
     * Default constructor.
     * Creates a Student with placeholder values and empty collections.
     */
    public Student() {
        this.id                   = 0;
        this.name                 = "Unknown";
        this.age                  = 18;          // sensible default
        this.department           = "Unassigned";
        this.admissionDate        = "00-00-0000";
        this.attendancePercentage = 0.0;
        this.grades               = new HashMap<>();
        this.attendanceLog        = new LinkedList<>();
    }

    /**
     * Parameterized constructor.
     * Creates a Student with the supplied core details.  The grades map
     * and attendance log are initialised as empty collections.
     *
     * @param id            unique roll number for the student
     * @param name          full name of the student
     * @param age           age of the student (must be &gt; 0)
     * @param department    department name (e.g. "Computer Science")
     * @param admissionDate date of admission in DD-MM-YYYY format
     * @throws IllegalArgumentException if age is not positive
     */
    public Student(int id, String name, int age, String department,
                   String admissionDate) {
        // Validate age before assigning
        if (age <= 0) {
            throw new IllegalArgumentException("Age must be greater than 0. Received: " + age);
        }

        this.id                   = id;
        this.name                 = name;
        this.age                  = age;
        this.department           = department;
        this.admissionDate        = admissionDate;
        this.attendancePercentage = 0.0;
        this.grades               = new HashMap<>();
        this.attendanceLog        = new LinkedList<>();
    }

    // ════════════════════════════════════════════════════════════════
    //                     GETTERS  AND  SETTERS
    // ════════════════════════════════════════════════════════════════

    // ───────────────── id ─────────────────

    /**
     * Returns the unique roll number of this student.
     *
     * @return the student's roll number
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique roll number for this student.
     *
     * @param id the new roll number
     */
    public void setId(int id) {
        this.id = id;
    }

    // ───────────────── name ─────────────────

    /**
     * Returns the full name of this student.
     *
     * @return the student's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the full name of this student.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    // ───────────────── age ─────────────────

    /**
     * Returns the age of this student.
     *
     * @return the student's age
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of this student.
     * <p><b>Validation:</b> age must be greater than 0.</p>
     *
     * @param age the new age (must be &gt; 0)
     * @throws IllegalArgumentException if age is not positive
     */
    public void setAge(int age) {
        if (age <= 0) {
            throw new IllegalArgumentException("Age must be greater than 0. Received: " + age);
        }
        this.age = age;
    }

    // ───────────────── department ─────────────────

    /**
     * Returns the department this student belongs to.
     *
     * @return the department name
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department for this student.
     *
     * @param department the new department name
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    // ───────────────── admissionDate ─────────────────

    /**
     * Returns the admission date in DD-MM-YYYY format.
     *
     * @return the admission date string
     */
    public String getAdmissionDate() {
        return admissionDate;
    }

    /**
     * Sets the admission date for this student.
     *
     * @param admissionDate the date in DD-MM-YYYY format
     */
    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    // ───────────────── attendancePercentage ─────────────────

    /**
     * Returns the overall attendance percentage (0–100).
     *
     * @return the attendance percentage
     */
    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    /**
     * Sets the overall attendance percentage.
     * <p><b>Validation:</b> value must be between 0 and 100 (inclusive).</p>
     *
     * @param attendancePercentage the new percentage (0–100)
     * @throws IllegalArgumentException if the value is outside the 0–100 range
     */
    public void setAttendancePercentage(double attendancePercentage) {
        if (attendancePercentage < 0 || attendancePercentage > 100) {
            throw new IllegalArgumentException(
                "Attendance percentage must be between 0 and 100. Received: "
                + attendancePercentage);
        }
        this.attendancePercentage = attendancePercentage;
    }

    // ───────────────── grades ─────────────────

    /**
     * Returns the grades map (subject name → marks).
     *
     * @return a {@link HashMap} mapping subject names to marks (0–100)
     */
    public HashMap<String, Double> getGrades() {
        return grades;
    }

    /**
     * Replaces the entire grades map with the supplied one.
     * <p><b>Validation:</b> every mark in the map must be between 0 and
     * 100 (inclusive).</p>
     *
     * @param grades a {@link HashMap} mapping subject names to marks
     * @throws IllegalArgumentException if any mark is outside the 0–100 range
     */
    public void setGrades(HashMap<String, Double> grades) {
        // Validate every grade value before accepting the map
        for (HashMap.Entry<String, Double> entry : grades.entrySet()) {
            double mark = entry.getValue();
            if (mark < 0 || mark > 100) {
                throw new IllegalArgumentException(
                    "Grade for \"" + entry.getKey()
                    + "\" must be between 0 and 100. Received: " + mark);
            }
        }
        this.grades = grades;
    }

    /**
     * Adds or updates a single subject's grade.
     * <p><b>Validation:</b> marks must be between 0 and 100 (inclusive).</p>
     *
     * @param subject the subject name (e.g. "Mathematics")
     * @param marks   the marks obtained (0–100)
     * @throws IllegalArgumentException if marks are outside the 0–100 range
     */
    public void addGrade(String subject, double marks) {
        if (marks < 0 || marks > 100) {
            throw new IllegalArgumentException(
                "Marks for \"" + subject
                + "\" must be between 0 and 100. Received: " + marks);
        }
        this.grades.put(subject, marks);
    }

    // ───────────────── attendanceLog ─────────────────

    /**
     * Returns the full attendance log as a {@link LinkedList}.
     *
     * @return the chronological list of {@link AttendanceRecord} entries
     */
    public LinkedList<AttendanceRecord> getAttendanceLog() {
        return attendanceLog;
    }

    /**
     * Replaces the entire attendance log with the supplied list.
     *
     * @param attendanceLog a {@link LinkedList} of {@link AttendanceRecord}
     */
    public void setAttendanceLog(LinkedList<AttendanceRecord> attendanceLog) {
        this.attendanceLog = attendanceLog;
    }

    /**
     * Appends a single attendance record to the end of the log.
     * Because the log is a {@link LinkedList}, this operation is O(1).
     *
     * @param record the {@link AttendanceRecord} to add
     */
    public void addAttendanceRecord(AttendanceRecord record) {
        this.attendanceLog.addLast(record);
    }

    // ════════════════════════════════════════════════════════════════
    //                  COMPARABLE  IMPLEMENTATION
    // ════════════════════════════════════════════════════════════════
    /*
     * OOP Concept — Interfaces (Comparable):
     * By implementing the Comparable<Student> interface, we promise that
     * our Student class provides a 'compareTo' method.  This allows
     * utility methods like Collections.sort() to sort a list of Students
     * automatically.  We compare students by their roll number (id).
     *
     * Return value rules:
     *   negative  → this student comes BEFORE the other
     *   zero      → they are equal (same id)
     *   positive  → this student comes AFTER the other
     */

    /**
     * Compares this student to another student by roll number (id).
     * This ordering is used by {@code Collections.sort()} and similar
     * utilities to arrange students in ascending order of their IDs.
     *
     * @param other the other {@link Student} to compare against
     * @return a negative value if this ID is smaller, zero if equal,
     *         or a positive value if this ID is larger
     */
    @Override
    public int compareTo(Student other) {
        return Integer.compare(this.id, other.id);
    }

    // ════════════════════════════════════════════════════════════════
    //                       toString OVERRIDE
    // ════════════════════════════════════════════════════════════════

    /**
     * Returns a formatted single-line summary of this student, enclosed
     * in a box-drawing border for professional console output.
     *
     * <p>Example:</p>
     * <pre>
     * ╔══════════════════════════════════════════════════════════════╗
     * ║ ID: 101 │ Name: Alice Johnson │ Age: 20 │ Dept: CS         ║
     * ║ Admitted: 15-07-2025 │ Attendance: 92.50% │ Subjects: 5    ║
     * ╚══════════════════════════════════════════════════════════════╝
     * </pre>
     *
     * @return a multi-line, box-drawn string describing this student
     */
    @Override
    public String toString() {
        // Calculate the number of subjects the student is enrolled in
        int subjectCount = (grades != null) ? grades.size() : 0;

        // Build the formatted output using box-drawing characters
        String topBorder    = "╔══════════════════════════════════════════════════════════════╗";
        String bottomBorder = "╚══════════════════════════════════════════════════════════════╝";

        // Line 1: ID, Name, Age, Department
        String line1 = String.format(
            "║ ID: %-5d │ Name: %-15s │ Age: %-3d │ Dept: %-8s ║",
            id,
            (name != null ? name : "N/A"),
            age,
            (department != null ? department : "N/A")
        );

        // Line 2: Admission date, Attendance %, Subject count
        String line2 = String.format(
            "║ Admitted: %-10s │ Attendance: %6.2f%% │ Subjects: %-3d  ║",
            (admissionDate != null ? admissionDate : "N/A"),
            attendancePercentage,
            subjectCount
        );

        return topBorder + "\n" + line1 + "\n" + line2 + "\n" + bottomBorder;
    }
}
