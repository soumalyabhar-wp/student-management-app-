import java.util.HashMap;
import java.util.ArrayList;

/**
 * GradeCalculator - Utility class for calculating GPA, CGPA, and letter grades.
 * Uses the standard 10-point grading scale commonly used in Indian universities.
 *
 * Grade Scale:
 * 90-100 → O  (Outstanding)  → 10.0
 * 80-89  → A+ (Excellent)     → 9.0
 * 70-79  → A  (Very Good)     → 8.0
 * 60-69  → B+ (Good)          → 7.0
 * 50-59  → B  (Above Average) → 6.0
 * 40-49  → C  (Average)       → 5.0
 * Below 40 → F (Fail)         → 0.0
 *
 * ─────────────────────────────────────────────────────────────────────
 * WHAT IS A UTILITY CLASS?
 * ─────────────────────────────────────────────────────────────────────
 * A utility class is a class that only contains static methods.
 * You don't need to create an object (instance) to use it.
 *
 * Example usage:
 *     double gpa = GradeCalculator.calculateGPA(myGrades);
 *     String grade = GradeCalculator.getLetterGrade(85.0);
 *
 * Notice we call methods directly on the CLASS NAME, not on an object.
 * The constructor is made private so nobody can accidentally write:
 *     GradeCalculator calc = new GradeCalculator();  // ← This will NOT compile
 *
 * Think of it like a toolbox — you use the tools inside it,
 * but you never need to "create" the toolbox itself.
 * ─────────────────────────────────────────────────────────────────────
 */
public class GradeCalculator {

    // ══════════════════════════════════════════════════════════════════
    //  PRIVATE CONSTRUCTOR — prevents anyone from creating an object
    //  of this class. All methods are static, so no object is needed.
    // ══════════════════════════════════════════════════════════════════

    /**
     * Private constructor to prevent instantiation of this utility class.
     * Since all methods are static, creating an object is unnecessary.
     */
    private GradeCalculator() {
        // This constructor is intentionally empty.
        // Making it private ensures no one can write:
        //     GradeCalculator gc = new GradeCalculator();
    }

    // ══════════════════════════════════════════════════════════════════
    //  METHOD 1: calculateGPA
    //  Takes a map of subject names → marks and returns the GPA
    // ══════════════════════════════════════════════════════════════════

    /**
     * Calculates the Grade Point Average (GPA) from a map of subject marks.
     *
     * How it works:
     * 1. Each subject's marks are converted to a grade point (0.0 to 10.0)
     * 2. All grade points are added together
     * 3. The sum is divided by the number of subjects
     *
     * Example:
     *   Math → 92 (grade point 10.0), Science → 75 (grade point 8.0)
     *   GPA = (10.0 + 8.0) / 2 = 9.0
     *
     * @param grades A HashMap where keys are subject names (String)
     *               and values are marks obtained (Double, 0-100)
     * @return The calculated GPA on a 10-point scale, or 0.0 if no grades provided
     */
    public static double calculateGPA(HashMap<String, Double> grades) {
        // If the map is empty or null, there's nothing to calculate
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }

        double totalGradePoints = 0.0;  // Running total of all grade points
        int subjectCount = 0;            // How many subjects we've processed

        // Loop through each subject-marks pair in the HashMap
        // "entry" gives us both the subject name (key) and marks (value)
        for (HashMap.Entry<String, Double> entry : grades.entrySet()) {
            double marks = entry.getValue();           // Get the marks for this subject
            double gradePoint = getGradePoint(marks);  // Convert marks → grade point
            totalGradePoints += gradePoint;             // Add to running total
            subjectCount++;                             // Count this subject
        }

        // Calculate average: total grade points ÷ number of subjects
        // Math.round trick: rounds to 2 decimal places
        //   Multiply by 100, round, then divide by 100
        return Math.round((totalGradePoints / subjectCount) * 100.0) / 100.0;
    }

    // ══════════════════════════════════════════════════════════════════
    //  METHOD 2: getLetterGrade
    //  Converts a numeric mark (0-100) into a letter grade (O, A+, etc.)
    // ══════════════════════════════════════════════════════════════════

    /**
     * Converts numeric marks into a letter grade based on the 10-point scale.
     *
     * The grading thresholds are:
     * ┌────────────┬───────────┬─────────────────┐
     * │ Marks      │ Grade     │ Description     │
     * ├────────────┼───────────┼─────────────────┤
     * │ 90 - 100   │ O         │ Outstanding     │
     * │ 80 - 89    │ A+        │ Excellent       │
     * │ 70 - 79    │ A         │ Very Good       │
     * │ 60 - 69    │ B+        │ Good            │
     * │ 50 - 59    │ B         │ Above Average   │
     * │ 40 - 49    │ C         │ Average         │
     * │ Below 40   │ F         │ Fail            │
     * └────────────┴───────────┴─────────────────┘
     *
     * @param marks The marks obtained by the student (0.0 to 100.0)
     * @return The letter grade as a String (e.g., "O", "A+", "A", "B+", "B", "C", "F")
     */
    public static String getLetterGrade(double marks) {
        // Check from highest grade to lowest using if-else chain
        // The order matters! We check >= 90 first, then >= 80, etc.
        if (marks >= 90) {
            return "O";    // Outstanding
        } else if (marks >= 80) {
            return "A+";   // Excellent
        } else if (marks >= 70) {
            return "A";    // Very Good
        } else if (marks >= 60) {
            return "B+";   // Good
        } else if (marks >= 50) {
            return "B";    // Above Average
        } else if (marks >= 40) {
            return "C";    // Average
        } else {
            return "F";    // Fail
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  METHOD 3: getGradePoint
    //  Converts a numeric mark (0-100) into a grade point (0.0-10.0)
    // ══════════════════════════════════════════════════════════════════

    /**
     * Converts numeric marks into a grade point on the 10-point scale.
     *
     * The mapping is:
     *   90-100 → 10.0 | 80-89 → 9.0 | 70-79 → 8.0
     *   60-69  → 7.0  | 50-59 → 6.0 | 40-49 → 5.0
     *   Below 40 → 0.0
     *
     * @param marks The marks obtained by the student (0.0 to 100.0)
     * @return The grade point as a double (0.0 to 10.0)
     */
    public static double getGradePoint(double marks) {
        // Same logic as getLetterGrade, but returns a number instead
        if (marks >= 90) {
            return 10.0;   // Outstanding
        } else if (marks >= 80) {
            return 9.0;    // Excellent
        } else if (marks >= 70) {
            return 8.0;    // Very Good
        } else if (marks >= 60) {
            return 7.0;    // Good
        } else if (marks >= 50) {
            return 6.0;    // Above Average
        } else if (marks >= 40) {
            return 5.0;    // Average
        } else {
            return 0.0;    // Fail — no grade points awarded
        }
    }

    // ══════════════════════════════════════════════════════════════════
    //  METHOD 4: calculateCGPA
    //  Calculates Cumulative GPA from a list of semester GPAs
    // ══════════════════════════════════════════════════════════════════

    /**
     * Calculates the Cumulative Grade Point Average (CGPA) from multiple semesters.
     *
     * CGPA is simply the average of all semester GPAs.
     * For example, if a student has GPAs of 8.5, 9.0, and 7.5 across 3 semesters:
     *   CGPA = (8.5 + 9.0 + 7.5) / 3 = 8.33
     *
     * @param semesterGPAs An ArrayList of Double values, each representing
     *                     the GPA of one semester (on a 10-point scale)
     * @return The calculated CGPA rounded to 2 decimal places,
     *         or 0.0 if the list is empty or null
     */
    public static double calculateCGPA(ArrayList<Double> semesterGPAs) {
        // Safety check: if no semester data is available, return 0.0
        if (semesterGPAs == null || semesterGPAs.isEmpty()) {
            return 0.0;
        }

        double totalGPA = 0.0;  // Running total of all semester GPAs

        // Add up every semester's GPA
        for (double gpa : semesterGPAs) {
            totalGPA += gpa;
        }

        // Calculate the average and round to 2 decimal places
        return Math.round((totalGPA / semesterGPAs.size()) * 100.0) / 100.0;
    }

    // ══════════════════════════════════════════════════════════════════
    //  METHOD 5: calculateAverageMarks
    //  Calculates simple average of all marks (not grade points)
    // ══════════════════════════════════════════════════════════════════

    /**
     * Calculates the simple arithmetic average of all subject marks.
     *
     * Unlike GPA (which converts marks to grade points first),
     * this method directly averages the raw marks.
     *
     * Example:
     *   Math → 85, Science → 92, English → 78
     *   Average = (85 + 92 + 78) / 3 = 85.0
     *
     * @param grades A HashMap where keys are subject names (String)
     *               and values are marks obtained (Double, 0-100)
     * @return The average marks rounded to 2 decimal places,
     *         or 0.0 if no grades provided
     */
    public static double calculateAverageMarks(HashMap<String, Double> grades) {
        // Safety check: nothing to average if map is empty or null
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }

        double totalMarks = 0.0;  // Running total of all marks
        int subjectCount = 0;      // Number of subjects

        // Loop through each entry and sum up the marks
        for (HashMap.Entry<String, Double> entry : grades.entrySet()) {
            totalMarks += entry.getValue();  // Add this subject's marks
            subjectCount++;                   // Count this subject
        }

        // Calculate average and round to 2 decimal places
        return Math.round((totalMarks / subjectCount) * 100.0) / 100.0;
    }

    // ══════════════════════════════════════════════════════════════════
    //  METHOD 6: getGPAClassification
    //  Converts a GPA into a descriptive classification string
    // ══════════════════════════════════════════════════════════════════

    /**
     * Returns a descriptive classification based on the GPA value.
     *
     * Classification thresholds:
     * ┌────────────────┬──────────────────┐
     * │ GPA Range      │ Classification   │
     * ├────────────────┼──────────────────┤
     * │ 8.0 and above  │ Distinction      │
     * │ 6.5 - 7.99     │ First Class      │
     * │ 5.5 - 6.49     │ Second Class     │
     * │ 4.0 - 5.49     │ Pass             │
     * │ Below 4.0      │ Fail             │
     * └────────────────┴──────────────────┘
     *
     * @param gpa The Grade Point Average on a 10-point scale (0.0 to 10.0)
     * @return A String classification: "Distinction", "First Class",
     *         "Second Class", "Pass", or "Fail"
     */
    public static String getGPAClassification(double gpa) {
        // Check from highest classification to lowest
        if (gpa >= 8.0) {
            return "Distinction";    // Exceptional performance
        } else if (gpa >= 6.5) {
            return "First Class";    // Very good performance
        } else if (gpa >= 5.5) {
            return "Second Class";   // Good performance
        } else if (gpa >= 4.0) {
            return "Pass";           // Minimum acceptable performance
        } else {
            return "Fail";           // Below passing threshold
        }
    }
}
