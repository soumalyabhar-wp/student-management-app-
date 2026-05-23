# Program Flowcharts

> Visual representation of the Student Record Management System program flow using Mermaid diagrams.

---

## 1. Main Program Flowchart

This flowchart shows the complete program flow from startup through the main menu to each operation and back.

```mermaid
graph TD
    START(["🟢 START"]) --> WELCOME["Display Welcome Screen\n(ASCII Art Banner)"]
    WELCOME --> INIT["Initialize StudentManager\nCreate Scanner for input"]
    INIT --> MENU["📋 Display Main Menu\n(15 Options + Exit)"]
    
    MENU --> INPUT["Read User Choice\n(Integer Input)"]
    INPUT --> VALIDATE{"Is input\na valid integer?"}
    
    VALIDATE -- "No (InputMismatchException)" --> ERROR1["❌ Display Error:\n'Invalid input.\nPlease enter a number.'"]
    ERROR1 --> CLEAR["Clear Scanner buffer\n(scanner.nextLine())"]
    CLEAR --> MENU
    
    VALIDATE -- Yes --> SWITCH{"Switch on\nuser choice"}
    
    SWITCH -- "1" --> ADD["➕ Add Student\nCollect: ID, Name, Dept, Age"]
    SWITCH -- "2" --> DELETE["🗑️ Delete Student\nInput: Student ID"]
    SWITCH -- "3" --> UPDATE["✏️ Update Student\nInput: ID + new details"]
    SWITCH -- "4" --> SEARCH["🔍 Search Student\nSub-menu: ID / Name / Dept"]
    SWITCH -- "5" --> DISPLAY["📋 Display All Students\nFormatted table output"]
    SWITCH -- "6" --> GRADES["📝 Add Grades\nInput: ID, Subject, Marks"]
    SWITCH -- "7" --> GPA["📊 Calculate GPA\nInput: Student ID"]
    SWITCH -- "8" --> MARK_ATT["✅ Mark Attendance\nInput: ID, Date, Status"]
    SWITCH -- "9" --> VIEW_ATT["📅 View Attendance\nInput: Student ID"]
    SWITCH -- "10" --> SORT["🔀 Sort Students\nSub-menu: Name / GPA / Att"]
    SWITCH -- "11" --> SUMMARY["📈 Class Summary\nDisplay statistics"]
    SWITCH -- "12" --> TOP["🏆 Top Performers\nInput: Number N"]
    SWITCH -- "13" --> COMPARE["📚 Array vs ArrayList\nEducational demo"]
    SWITCH -- "14" --> SAMPLE["📦 Load Sample Data\n5 pre-defined students"]
    SWITCH -- "0" --> EXIT_CONFIRM{"Confirm Exit?"}
    SWITCH -- "Other" --> ERROR2["❌ Display Error:\n'Invalid option.\nPlease choose 0-14.'"]
    
    ADD --> RESULT1["Display result message"]
    DELETE --> RESULT2["Display result message"]
    UPDATE --> RESULT3["Display result message"]
    SEARCH --> RESULT4["Display search results"]
    DISPLAY --> RESULT5["Display formatted table"]
    GRADES --> RESULT6["Display result message"]
    GPA --> RESULT7["Display GPA value"]
    MARK_ATT --> RESULT8["Display result message"]
    VIEW_ATT --> RESULT9["Display attendance log"]
    SORT --> RESULT10["Display sorted list"]
    SUMMARY --> RESULT11["Display summary report"]
    TOP --> RESULT12["Display top N students"]
    COMPARE --> RESULT13["Display comparison"]
    SAMPLE --> RESULT14["Display load status"]
    ERROR2 --> MENU
    
    RESULT1 --> MENU
    RESULT2 --> MENU
    RESULT3 --> MENU
    RESULT4 --> MENU
    RESULT5 --> MENU
    RESULT6 --> MENU
    RESULT7 --> MENU
    RESULT8 --> MENU
    RESULT9 --> MENU
    RESULT10 --> MENU
    RESULT11 --> MENU
    RESULT12 --> MENU
    RESULT13 --> MENU
    RESULT14 --> MENU
    
    EXIT_CONFIRM -- "No" --> MENU
    EXIT_CONFIRM -- "Yes" --> CLEANUP["Close Scanner\nClean up resources"]
    CLEANUP --> GOODBYE["Display Goodbye Message\n'Thank you for using SRMS!'"]
    GOODBYE --> END(["🔴 END"])
```

---

## 2. Add Student — Detailed Flowchart

This flowchart shows the detailed process of adding a new student record, including all input validation steps.

```mermaid
graph TD
    START(["Start: Add Student"]) --> PROMPT_ID["Prompt:\n'Enter Student ID'"]
    
    PROMPT_ID --> READ_ID["Read Student ID\n(integer input)"]
    READ_ID --> VALID_ID{"Is input\na valid integer?"}
    
    VALID_ID -- No --> ERR_ID["❌ Error:\n'Please enter a valid\nnumeric ID'"]
    ERR_ID --> PROMPT_ID
    
    VALID_ID -- Yes --> CHECK_POSITIVE{"Is ID > 0?"}
    CHECK_POSITIVE -- No --> ERR_POS["❌ Error:\n'ID must be a\npositive number'"]
    ERR_POS --> PROMPT_ID
    
    CHECK_POSITIVE -- Yes --> CHECK_DUP{"Does student\nwith this ID\nalready exist?"}
    
    CHECK_DUP -- Yes --> ERR_DUP["❌ Error:\n'Student ID already exists!\nPlease use a different ID.'"]
    ERR_DUP --> PROMPT_ID
    
    CHECK_DUP -- No --> PROMPT_NAME["Prompt:\n'Enter Student Name'"]
    PROMPT_NAME --> READ_NAME["Read Name (String)"]
    READ_NAME --> VALID_NAME{"Is name\nnon-empty?"}
    VALID_NAME -- No --> ERR_NAME["❌ Error:\n'Name cannot be empty'"]
    ERR_NAME --> PROMPT_NAME
    
    VALID_NAME -- Yes --> PROMPT_DEPT["Prompt:\n'Enter Department'"]
    PROMPT_DEPT --> READ_DEPT["Read Department (String)"]
    READ_DEPT --> VALID_DEPT{"Is department\nnon-empty?"}
    VALID_DEPT -- No --> ERR_DEPT["❌ Error:\n'Department cannot be empty'"]
    ERR_DEPT --> PROMPT_DEPT
    
    VALID_DEPT -- Yes --> PROMPT_AGE["Prompt:\n'Enter Age'"]
    PROMPT_AGE --> READ_AGE["Read Age (integer)"]
    READ_AGE --> VALID_AGE{"Is age between\n15 and 100?"}
    VALID_AGE -- No --> ERR_AGE["❌ Error:\n'Please enter a valid age\n(15-100)'"]
    ERR_AGE --> PROMPT_AGE
    
    VALID_AGE -- Yes --> CREATE["Create new Student object\nStudent(id, name, dept, age)"]
    CREATE --> ADD_LIST["Add Student to\nArrayList via\nStudentManager.addStudent()"]
    ADD_LIST --> SUCCESS["✅ Display:\n'Student added successfully!\nID: [id] Name: [name]'"]
    SUCCESS --> END(["Return to Main Menu"])
```

---

## 3. Search Student — Sub-Menu Flowchart

```mermaid
graph TD
    START(["Start: Search Student"]) --> SUBMENU["Display Search Sub-Menu:\n1. Search by ID\n2. Search by Name\n3. Search by Department\n4. Back to Main Menu"]
    
    SUBMENU --> CHOICE["Read Search Choice"]
    CHOICE --> SWITCH{"Search\nOption?"}
    
    SWITCH -- "1: By ID" --> ID_INPUT["Enter Student ID"]
    ID_INPUT --> ID_SEARCH["Call searchById(id)\nLinear search through ArrayList"]
    ID_SEARCH --> ID_FOUND{"Student\nfound?"}
    ID_FOUND -- Yes --> ID_DISPLAY["Display student details\nin formatted box"]
    ID_FOUND -- No --> ID_NOT["❌ 'No student found\nwith ID: [id]'"]
    
    SWITCH -- "2: By Name" --> NAME_INPUT["Enter Name\n(partial match supported)"]
    NAME_INPUT --> NAME_SEARCH["Call searchByName(name)\nCheck contains() for each"]
    NAME_SEARCH --> NAME_FOUND{"Any matches\nfound?"}
    NAME_FOUND -- Yes --> NAME_DISPLAY["Display all matching\nstudents in table"]
    NAME_FOUND -- No --> NAME_NOT["❌ 'No students found\nmatching: [name]'"]
    
    SWITCH -- "3: By Department" --> DEPT_INPUT["Enter Department Code"]
    DEPT_INPUT --> DEPT_SEARCH["Call searchByDepartment(dept)\nFilter by department field"]
    DEPT_SEARCH --> DEPT_FOUND{"Any matches\nfound?"}
    DEPT_FOUND -- Yes --> DEPT_DISPLAY["Display all matching\nstudents in table"]
    DEPT_FOUND -- No --> DEPT_NOT["❌ 'No students found\nin department: [dept]'"]
    
    SWITCH -- "4: Back" --> END
    
    ID_DISPLAY --> END(["Return to Main Menu"])
    ID_NOT --> END
    NAME_DISPLAY --> END
    NAME_NOT --> END
    DEPT_DISPLAY --> END
    DEPT_NOT --> END
```

---

## 4. GPA Calculation — Process Flowchart

```mermaid
graph TD
    START(["Start: Calculate GPA"]) --> INPUT["Enter Student ID"]
    INPUT --> FIND{"Student\nfound?"}
    
    FIND -- No --> ERR["❌ Student not found"]
    ERR --> END
    
    FIND -- Yes --> CHECK{"Does student\nhave grades?"}
    CHECK -- No --> ZERO["GPA = 0.0\nDisplay: 'No grades found.\nPlease add grades first.'"]
    ZERO --> END
    
    CHECK -- Yes --> INIT["totalPoints = 0\nsubjectCount = 0"]
    INIT --> LOOP["For each entry in\ngrades HashMap"]
    
    LOOP --> GET_MARKS["Get marks for\ncurrent subject"]
    GET_MARKS --> CONVERT["Convert marks to\ngrade point:\n≥90→10, ≥80→9, ≥70→8\n≥60→7, ≥50→6, ≥40→5, else→0"]
    CONVERT --> ACCUMULATE["totalPoints += gradePoint\nsubjectCount++"]
    ACCUMULATE --> MORE{"More\nsubjects?"}
    
    MORE -- Yes --> LOOP
    MORE -- No --> CALC["GPA = totalPoints / subjectCount\nRound to 2 decimal places"]
    CALC --> DISPLAY["✅ Display:\n'GPA for [name]: [gpa]'\nWith subject-wise breakdown"]
    DISPLAY --> END(["Return to Main Menu"])
```

---

## 5. Legend

| Symbol | Meaning |
|--------|---------|
| 🟢 Rounded rectangle | Start / End terminal |
| 📋 Rectangle | Process / Action step |
| ◇ Diamond | Decision / Condition |
| ➡️ Arrow | Flow direction |
| ❌ Red text | Error handling |
| ✅ Green text | Success message |

---

*These flowcharts were created using Mermaid.js syntax and can be rendered in any Mermaid-compatible viewer (VS Code, GitHub, etc.).*
