/**
 * ╔══════════════════════════════════════════════════════════════════════╗
 * ║                    AttendanceRecord.java                            ║
 * ║         Student Record Management System — Model Class              ║
 * ╚══════════════════════════════════════════════════════════════════════╝
 *
 * This class represents a single attendance entry for a student on a
 * particular date.  Instances of this class are stored inside a
 * {@link java.util.LinkedList} in the {@link Student} class so that
 * the attendance history is maintained in chronological order.
 *
 * <h3>Why a LinkedList?</h3>
 * <ul>
 *   <li><b>Chronological log</b> — new records are always appended at
 *       the end, and LinkedList's {@code addLast()} runs in O(1) time.</li>
 *   <li><b>Efficient insertion / deletion</b> — if a record needs to be
 *       corrected or removed, LinkedList can do it in O(1) once the
 *       node is located, unlike an ArrayList which must shift elements.</li>
 *   <li><b>Sequential traversal</b> — attendance reports are usually
 *       read from first record to last, which is the natural traversal
 *       order of a linked list.</li>
 * </ul>
 *
 * <h3>OOP Concept — Encapsulation</h3>
 * All fields are declared {@code private} so that outside code cannot
 * modify them directly.  Access is provided through public getter and
 * setter methods.  This protects the internal state and lets us add
 * validation logic later without breaking other classes.
 *
 * @author  Student Record Management System
 * @version 1.0
 */
public class AttendanceRecord {

    // ──────────────────────────── Fields ────────────────────────────

    /**
     * The date of this attendance entry in DD-MM-YYYY format.
     * Example: "23-05-2026"
     */
    private String date;

    /**
     * {@code true} if the student was present on this date,
     * {@code false} if absent.
     */
    private boolean present;

    /**
     * Optional remarks for this attendance entry.
     * Examples: "Sick Leave", "Holiday", "Late arrival".
     * Should be an empty string ({@code ""}) when the student is
     * simply present with no special notes.
     */
    private String remarks;

    // ────────────────────────── Constructors ────────────────────────
    /*
     * OOP Concept — Constructors:
     * A constructor is a special method that is called automatically
     * when you create a new object with the 'new' keyword.
     * We provide TWO constructors here:
     *   1. Default (no-arg)  — sets safe default values.
     *   2. Parameterized     — lets the caller supply all values at
     *                          creation time.
     * This technique is called "constructor overloading".
     */

    /**
     * Default constructor.
     * Initialises the record with today-like placeholder values:
     * date = "00-00-0000", present = false, remarks = "".
     */
    public AttendanceRecord() {
        this.date    = "00-00-0000";
        this.present = false;
        this.remarks = "";
    }

    /**
     * Parameterized constructor — creates an attendance record with
     * the supplied values.
     *
     * @param date    the date in DD-MM-YYYY format (e.g. "23-05-2026")
     * @param present {@code true} if the student was present
     * @param remarks additional notes (use {@code ""} if none)
     */
    public AttendanceRecord(String date, boolean present, String remarks) {
        this.date    = date;
        this.present = present;
        this.remarks = remarks;
    }

    // ──────────────────── Getters and Setters ──────────────────────

    /**
     * Returns the date of this attendance record.
     *
     * @return the date string in DD-MM-YYYY format
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of this attendance record.
     *
     * @param date the date string in DD-MM-YYYY format
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Checks whether the student was present on this date.
     *
     * @return {@code true} if the student was present, {@code false} otherwise
     */
    public boolean isPresent() {
        return present;
    }

    /**
     * Sets the presence status for this record.
     *
     * @param present {@code true} if the student was present
     */
    public void setPresent(boolean present) {
        this.present = present;
    }

    /**
     * Returns the remarks associated with this attendance entry.
     *
     * @return the remarks string (may be empty)
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the remarks for this attendance entry.
     * Pass an empty string ({@code ""}) when no special note is needed.
     *
     * @param remarks the remark text (e.g. "Sick Leave", "Holiday")
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // ──────────────────────── toString ──────────────────────────────

    /**
     * Returns a neatly formatted string representation of this record,
     * suitable for console display.
     * <p>Example output:</p>
     * <pre>
     * ┌─────────────────────────────────────────┐
     * │ Date: 23-05-2026 │ Status: Present │
     * │ Remarks: —                              │
     * └─────────────────────────────────────────┘
     * </pre>
     *
     * @return a formatted multi-line string describing this record
     */
    @Override
    public String toString() {
        // Determine a human-readable status label
        String status = present ? "Present" : "Absent";

        // If remarks are null or empty, show a dash for clarity
        String displayRemarks = (remarks == null || remarks.isEmpty()) ? "—" : remarks;

        // Build a compact, professional-looking box
        return "┌─────────────────────────────────────────┐\n"
             + String.format("│ Date: %-12s │ Status: %-8s │%n", date, status)
             + String.format("│ Remarks: %-30s│%n", displayRemarks)
             + "└─────────────────────────────────────────┘";
    }
}
