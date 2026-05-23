/* ============================================
   Student Record Management System — app.js
   Core business logic, CRUD, GPA, Attendance
   ============================================ */

// ── Data Store ──
let students = [];       // ArrayList equivalent
let studentMap = {};     // HashMap<Integer, Student> equivalent
let deleteTargetId = null;
let currentSort = { field: null, asc: true };

// ── Student Model ──
function createStudent(id, name, age, department, admissionDate) {
  return {
    id: parseInt(id),
    name: name.trim(),
    age: parseInt(age),
    department: department,
    admissionDate: admissionDate,
    attendancePercentage: 0,
    grades: {},             // HashMap<String, Double>
    attendanceLog: []       // LinkedList<AttendanceRecord>
  };
}

// ── GradeCalculator (static utility) ──
const GradeCalculator = {
  getLetterGrade(marks) {
    if (marks >= 90) return 'O';
    if (marks >= 80) return 'A+';
    if (marks >= 70) return 'A';
    if (marks >= 60) return 'B+';
    if (marks >= 50) return 'B';
    if (marks >= 40) return 'C';
    return 'F';
  },

  getGradePoint(marks) {
    if (marks >= 90) return 10.0;
    if (marks >= 80) return 9.0;
    if (marks >= 70) return 8.0;
    if (marks >= 60) return 7.0;
    if (marks >= 50) return 6.0;
    if (marks >= 40) return 5.0;
    return 0.0;
  },

  calculateGPA(grades) {
    const subjects = Object.keys(grades);
    if (subjects.length === 0) return 0;
    let total = 0;
    subjects.forEach(s => total += this.getGradePoint(grades[s]));
    return Math.round((total / subjects.length) * 100) / 100;
  },

  calculateAverageMarks(grades) {
    const subjects = Object.keys(grades);
    if (subjects.length === 0) return 0;
    let total = 0;
    subjects.forEach(s => total += grades[s]);
    return Math.round((total / subjects.length) * 100) / 100;
  },

  getGPAClassification(gpa) {
    if (gpa >= 8.0) return 'Distinction';
    if (gpa >= 6.5) return 'First Class';
    if (gpa >= 5.5) return 'Second Class';
    if (gpa >= 4.0) return 'Pass';
    return 'Fail';
  }
};

// ── CRUD Operations ──
function addStudent(student) {
  if (studentMap[student.id]) {
    showToast('Student ID already exists!', 'error');
    return false;
  }
  students.push(student);
  studentMap[student.id] = student;
  refreshAll();
  return true;
}

function deleteStudent(id) {
  const index = students.findIndex(s => s.id === id);
  if (index === -1) return false;
  students.splice(index, 1);
  delete studentMap[id];
  refreshAll();
  return true;
}

function updateStudent(id, field, value) {
  const student = studentMap[id];
  if (!student) return false;
  student[field] = value;
  refreshAll();
  return true;
}

function addGradeToStudent(id, subject, marks) {
  const student = studentMap[id];
  if (!student) return false;
  student.grades[subject] = marks;
  refreshAll();
  return true;
}

function addAttendanceToStudent(id, date, present, remarks) {
  const student = studentMap[id];
  if (!student) return false;
  student.attendanceLog.push({ date, present, remarks });
  // Recalculate attendance percentage
  const total = student.attendanceLog.length;
  const presentCount = student.attendanceLog.filter(r => r.present).length;
  student.attendancePercentage = Math.round((presentCount / total) * 1000) / 10;
  refreshAll();
  return true;
}

// ── Search ──
function searchById(id) {
  return studentMap[parseInt(id)] || null;
}

function searchByName(name) {
  const lower = name.toLowerCase();
  return students.filter(s => s.name.toLowerCase().includes(lower));
}

function searchByDepartment(dept) {
  const lower = dept.toLowerCase();
  return students.filter(s => s.department.toLowerCase().includes(lower));
}

// ── Sort ──
function sortStudents(field) {
  if (currentSort.field === field) {
    currentSort.asc = !currentSort.asc;
  } else {
    currentSort.field = field;
    currentSort.asc = field === 'name';
  }

  students.sort((a, b) => {
    let va, vb;
    switch (field) {
      case 'name':
        va = a.name.toLowerCase();
        vb = b.name.toLowerCase();
        return currentSort.asc ? va.localeCompare(vb) : vb.localeCompare(va);
      case 'gpa':
        va = GradeCalculator.calculateGPA(a.grades);
        vb = GradeCalculator.calculateGPA(b.grades);
        return currentSort.asc ? va - vb : vb - va;
      case 'attendance':
        va = a.attendancePercentage;
        vb = b.attendancePercentage;
        return currentSort.asc ? va - vb : vb - va;
      default:
        return a.id - b.id;
    }
  });

  renderStudentTable(students);
  showToast(`Sorted by ${field} (${currentSort.asc ? 'ascending' : 'descending'})`, 'info');
}

// ── Sample Data ──
function loadSampleData() {
  if (students.length > 0) {
    if (!confirm('This will add sample data alongside existing data. Continue?')) return;
  }

  const sampleStudents = [
    { id: 1001, name: 'Rahul Sharma', age: 18, dept: 'Computer Science', date: '2025-08-01',
      grades: { Mathematics: 85, Physics: 78, Chemistry: 72, English: 88, Programming: 95 },
      attendance: [
        { date: '2025-08-04', present: true, remarks: '' },
        { date: '2025-08-05', present: true, remarks: '' },
        { date: '2025-08-06', present: true, remarks: '' },
        { date: '2025-08-07', present: false, remarks: 'Medical leave' },
        { date: '2025-08-08', present: true, remarks: '' }
      ]
    },
    { id: 1002, name: 'Priya Patel', age: 19, dept: 'Electronics', date: '2025-07-15',
      grades: { Mathematics: 92, Physics: 88, Chemistry: 79, English: 91, Programming: 85 },
      attendance: [
        { date: '2025-08-04', present: true, remarks: '' },
        { date: '2025-08-05', present: true, remarks: '' },
        { date: '2025-08-06', present: true, remarks: '' },
        { date: '2025-08-07', present: true, remarks: '' },
        { date: '2025-08-08', present: true, remarks: '' }
      ]
    },
    { id: 1003, name: 'Amit Kumar', age: 18, dept: 'Mechanical', date: '2025-07-20',
      grades: { Mathematics: 65, Physics: 70, Chemistry: 68, English: 72, Programming: 60 },
      attendance: [
        { date: '2025-08-04', present: true, remarks: '' },
        { date: '2025-08-05', present: false, remarks: 'Personal leave' },
        { date: '2025-08-06', present: true, remarks: '' },
        { date: '2025-08-07', present: false, remarks: 'Sick leave' },
        { date: '2025-08-08', present: true, remarks: '' }
      ]
    },
    { id: 1004, name: 'Sneha Reddy', age: 19, dept: 'Computer Science', date: '2025-08-01',
      grades: { Mathematics: 90, Physics: 85, Chemistry: 88, English: 94, Programming: 92 },
      attendance: [
        { date: '2025-08-04', present: true, remarks: '' },
        { date: '2025-08-05', present: true, remarks: '' },
        { date: '2025-08-06', present: true, remarks: '' },
        { date: '2025-08-07', present: true, remarks: '' },
        { date: '2025-08-08', present: false, remarks: 'Family event' }
      ]
    },
    { id: 1005, name: 'Vikram Singh', age: 20, dept: 'Civil', date: '2025-07-25',
      grades: { Mathematics: 75, Physics: 82, Chemistry: 78, English: 80, Programming: 70 },
      attendance: [
        { date: '2025-08-04', present: true, remarks: '' },
        { date: '2025-08-05', present: true, remarks: '' },
        { date: '2025-08-06', present: false, remarks: 'Transport issue' },
        { date: '2025-08-07', present: true, remarks: '' },
        { date: '2025-08-08', present: true, remarks: '' }
      ]
    }
  ];

  sampleStudents.forEach(s => {
    if (studentMap[s.id]) return; // skip if already exists
    const student = createStudent(s.id, s.name, s.age, s.dept, s.date);
    student.grades = { ...s.grades };
    s.attendance.forEach(a => {
      student.attendanceLog.push(a);
    });
    const total = student.attendanceLog.length;
    const presentCount = student.attendanceLog.filter(r => r.present).length;
    student.attendancePercentage = Math.round((presentCount / total) * 1000) / 10;
    students.push(student);
    studentMap[student.id] = student;
  });

  refreshAll();
  showToast('Sample data loaded — 5 students added!', 'success');
}

// ══════════════════════════════════
//  UI RENDERING
// ══════════════════════════════════

function refreshAll() {
  renderStudentTable(students);
  updateDashboard();
  updateStudentSelects();
  updateReport();
}

// ── Student Table ──
function renderStudentTable(list) {
  const tbody = document.getElementById('studentsTableBody');
  const empty = document.getElementById('studentsEmpty');

  if (list.length === 0) {
    tbody.innerHTML = '';
    empty.style.display = 'block';
    return;
  }

  empty.style.display = 'none';
  tbody.innerHTML = list.map(s => {
    const gpa = GradeCalculator.calculateGPA(s.grades);
    const gpaClass = gpa >= 8 ? 'gpa-high' : gpa >= 5 ? 'gpa-mid' : 'gpa-low';
    const attClass = s.attendancePercentage >= 75 ? 'high' : s.attendancePercentage >= 50 ? 'mid' : 'low';
    const attColor = s.attendancePercentage >= 75 ? 'var(--accent-emerald)' : s.attendancePercentage >= 50 ? 'var(--accent-amber)' : 'var(--accent-rose)';
    const deptClass = getDeptClass(s.department);
    const dateFormatted = formatDate(s.admissionDate);

    return `<tr>
      <td style="font-weight:600;color:var(--text-primary)">${s.id}</td>
      <td>
        <div style="display:flex;align-items:center;gap:10px">
          <div style="width:32px;height:32px;border-radius:50%;background:var(--gradient-primary);display:flex;align-items:center;justify-content:center;font-size:13px;font-weight:700;color:white;flex-shrink:0">${s.name.charAt(0)}</div>
          <span style="color:var(--text-primary);font-weight:500">${s.name}</span>
        </div>
      </td>
      <td>${s.age}</td>
      <td><span class="dept-badge ${deptClass}">${s.department}</span></td>
      <td>${dateFormatted}</td>
      <td>
        <div class="attendance-bar">
          <div class="bar-track"><div class="bar-fill ${attClass}" style="width:${s.attendancePercentage}%"></div></div>
          <span class="bar-value" style="color:${attColor}">${s.attendancePercentage.toFixed(1)}%</span>
        </div>
      </td>
      <td><span class="gpa-badge ${gpaClass}">${gpa > 0 ? gpa.toFixed(2) : '—'}</span></td>
      <td>
        <div class="actions">
          <button class="btn-icon view" title="View" onclick="viewStudent(${s.id})">👁</button>
          <button class="btn-icon edit" title="Edit" onclick="openEditStudentModal(${s.id})">✏️</button>
          <button class="btn-icon delete" title="Delete" onclick="openDeleteModal(${s.id})">🗑️</button>
        </div>
      </td>
    </tr>`;
  }).join('');
}

// ── Dashboard ──
function updateDashboard() {
  const total = students.length;
  document.getElementById('statTotalStudents').textContent = total;

  if (total === 0) {
    document.getElementById('statAvgGPA').textContent = '0.00';
    document.getElementById('statAvgAttendance').textContent = '0.0%';
    document.getElementById('statDepartments').textContent = '0';
    document.getElementById('topPerformers').innerHTML = '<div class="empty-state"><p>Add students to see top performers</p></div>';
    document.getElementById('lowAttendance').innerHTML = '<div class="empty-state"><p>No attendance warnings</p></div>';
    document.getElementById('deptDistribution').innerHTML = '<div class="empty-state"><p>Add students to see department distribution</p></div>';
    return;
  }

  // Avg GPA
  const gpas = students.map(s => GradeCalculator.calculateGPA(s.grades));
  const avgGPA = gpas.reduce((a, b) => a + b, 0) / total;
  document.getElementById('statAvgGPA').textContent = avgGPA.toFixed(2);

  // Avg Attendance
  const avgAtt = students.reduce((a, s) => a + s.attendancePercentage, 0) / total;
  document.getElementById('statAvgAttendance').textContent = avgAtt.toFixed(1) + '%';

  // Departments
  const depts = {};
  students.forEach(s => depts[s.department] = (depts[s.department] || 0) + 1);
  document.getElementById('statDepartments').textContent = Object.keys(depts).length;

  // Top Performers (top 3 by GPA)
  const ranked = students
    .map(s => ({ ...s, gpa: GradeCalculator.calculateGPA(s.grades) }))
    .filter(s => s.gpa > 0)
    .sort((a, b) => b.gpa - a.gpa)
    .slice(0, 5);

  if (ranked.length > 0) {
    document.getElementById('topPerformers').innerHTML = `<ul class="report-list">${ranked.map((s, i) => {
      const medal = i === 0 ? '🥇' : i === 1 ? '🥈' : i === 2 ? '🥉' : '🏅';
      const gpaClass = s.gpa >= 8 ? 'gpa-high' : s.gpa >= 5 ? 'gpa-mid' : 'gpa-low';
      return `<li><span>${medal} ${s.name}</span><span class="gpa-badge ${gpaClass}">${s.gpa.toFixed(2)}</span></li>`;
    }).join('')}</ul>`;
  }

  // Low Attendance
  const lowAtt = students.filter(s => s.attendancePercentage < 75 && s.attendanceLog.length > 0);
  if (lowAtt.length > 0) {
    document.getElementById('lowAttendance').innerHTML = `<ul class="report-list">${lowAtt.map(s =>
      `<li><span>⚠️ ${s.name}</span><span style="color:var(--accent-rose);font-weight:600">${s.attendancePercentage.toFixed(1)}%</span></li>`
    ).join('')}</ul>`;
  } else {
    document.getElementById('lowAttendance').innerHTML = '<div class="empty-state" style="padding:20px"><p>✅ All students above 75% attendance</p></div>';
  }

  // Department Distribution
  const deptHTML = Object.entries(depts).map(([dept, count]) => {
    const pct = ((count / total) * 100).toFixed(0);
    const cls = getDeptClass(dept);
    return `<div style="display:flex;align-items:center;gap:12px;margin-bottom:10px">
      <span class="dept-badge ${cls}" style="min-width:140px">${dept}</span>
      <div style="flex:1;height:8px;background:rgba(255,255,255,0.06);border-radius:4px;overflow:hidden">
        <div style="height:100%;width:${pct}%;background:var(--gradient-primary);border-radius:4px;transition:width 0.6s ease"></div>
      </div>
      <span style="font-size:13px;font-weight:600;min-width:60px;text-align:right">${count} (${pct}%)</span>
    </div>`;
  }).join('');
  document.getElementById('deptDistribution').innerHTML = deptHTML;
}

// ── Report ──
function updateReport() {
  const container = document.getElementById('reportContent');
  if (students.length === 0) {
    container.innerHTML = '<div class="empty-state" style="grid-column:1/-1"><div class="empty-icon">📝</div><h3>No Data for Reports</h3><p>Add students to generate summary reports.</p></div>';
    return;
  }

  const total = students.length;
  const gpas = students.map(s => GradeCalculator.calculateGPA(s.grades));
  const avgGPA = gpas.reduce((a, b) => a + b, 0) / total;
  const maxGPA = Math.max(...gpas);
  const minGPA = Math.min(...gpas.filter(g => g > 0));
  const topStudent = students.find(s => GradeCalculator.calculateGPA(s.grades) === maxGPA);
  const lowStudent = students.find(s => GradeCalculator.calculateGPA(s.grades) === minGPA);
  const avgAtt = students.reduce((a, s) => a + s.attendancePercentage, 0) / total;
  const lowAtt = students.filter(s => s.attendancePercentage < 75 && s.attendanceLog.length > 0);

  const depts = {};
  students.forEach(s => depts[s.department] = (depts[s.department] || 0) + 1);

  container.innerHTML = `
    <div class="report-item">
      <h4>👥 Total Students</h4>
      <div class="report-value" style="color:var(--accent-indigo)">${total}</div>
    </div>
    <div class="report-item">
      <h4>📊 Average GPA</h4>
      <div class="report-value" style="color:var(--accent-emerald)">${avgGPA.toFixed(2)}</div>
    </div>
    <div class="report-item">
      <h4>📋 Average Attendance</h4>
      <div class="report-value" style="color:var(--accent-amber)">${avgAtt.toFixed(1)}%</div>
    </div>
    <div class="report-item">
      <h4>⚠️ Low Attendance Count</h4>
      <div class="report-value" style="color:var(--accent-rose)">${lowAtt.length}</div>
    </div>
    <div class="report-item">
      <h4>🏆 Highest GPA</h4>
      <div style="font-size:16px;font-weight:600;margin-bottom:4px">${topStudent ? topStudent.name : '—'}</div>
      <div style="font-size:24px;font-weight:800;color:var(--accent-emerald)">${maxGPA > 0 ? maxGPA.toFixed(2) : '—'}</div>
    </div>
    <div class="report-item">
      <h4>📉 Lowest GPA</h4>
      <div style="font-size:16px;font-weight:600;margin-bottom:4px">${lowStudent ? lowStudent.name : '—'}</div>
      <div style="font-size:24px;font-weight:800;color:var(--accent-rose)">${minGPA > 0 ? minGPA.toFixed(2) : '—'}</div>
    </div>
    <div class="report-item" style="grid-column:1/-1">
      <h4>🏛️ Department-wise Distribution</h4>
      <ul class="report-list">
        ${Object.entries(depts).map(([d, c]) => `<li><span>${d}</span><span>${c} student${c > 1 ? 's' : ''}</span></li>`).join('')}
      </ul>
    </div>
    ${lowAtt.length > 0 ? `<div class="report-item" style="grid-column:1/-1;border-color:rgba(244,63,94,0.2)">
      <h4>⚠️ Low Attendance Warning (&lt;75%)</h4>
      <ul class="report-list">
        ${lowAtt.map(s => `<li><span>${s.name} (ID: ${s.id})</span><span style="color:var(--accent-rose)">${s.attendancePercentage.toFixed(1)}%</span></li>`).join('')}
      </ul>
    </div>` : ''}
  `;
}

// ── Student Selects ──
function updateStudentSelects() {
  const options = students.map(s => `<option value="${s.id}">${s.id} — ${s.name}</option>`).join('');
  const base = '<option value="">-- Select Student --</option>' + options;

  const attSelect = document.getElementById('attendanceStudentSelect');
  const gradeSelect = document.getElementById('gradeStudentSelect');
  const prevAtt = attSelect.value;
  const prevGrade = gradeSelect.value;
  attSelect.innerHTML = base;
  gradeSelect.innerHTML = base;
  if (prevAtt && studentMap[prevAtt]) attSelect.value = prevAtt;
  if (prevGrade && studentMap[prevGrade]) gradeSelect.value = prevGrade;
}

// ══════════════════════════════════
//  MODAL HANDLERS
// ══════════════════════════════════

function openModal(id) {
  document.getElementById(id).classList.add('open');
}

function closeModal(id) {
  document.getElementById(id).classList.remove('open');
}

// ── Add Student ──
function openAddStudentModal() {
  document.getElementById('studentModalTitle').textContent = 'Add New Student';
  document.getElementById('saveStudentBtn').textContent = 'Add Student';
  document.getElementById('editStudentId').value = '';
  document.getElementById('studentIdInput').value = '';
  document.getElementById('studentIdInput').disabled = false;
  document.getElementById('studentName').value = '';
  document.getElementById('studentAge').value = '';
  document.getElementById('studentDept').value = '';
  document.getElementById('studentAdmission').value = '';
  openModal('studentModal');
}

// ── Edit Student ──
function openEditStudentModal(id) {
  const s = studentMap[id];
  if (!s) return;
  document.getElementById('studentModalTitle').textContent = 'Edit Student';
  document.getElementById('saveStudentBtn').textContent = 'Save Changes';
  document.getElementById('editStudentId').value = id;
  document.getElementById('studentIdInput').value = s.id;
  document.getElementById('studentIdInput').disabled = true;
  document.getElementById('studentName').value = s.name;
  document.getElementById('studentAge').value = s.age;
  document.getElementById('studentDept').value = s.department;
  document.getElementById('studentAdmission').value = s.admissionDate;
  openModal('studentModal');
}

function saveStudent() {
  const editId = document.getElementById('editStudentId').value;
  const id = document.getElementById('studentIdInput').value;
  const name = document.getElementById('studentName').value.trim();
  const age = document.getElementById('studentAge').value;
  const dept = document.getElementById('studentDept').value;
  const admDate = document.getElementById('studentAdmission').value;

  // Validation
  if (!id || !name || !age || !dept || !admDate) {
    showToast('Please fill in all fields', 'error');
    return;
  }

  if (parseInt(age) < 1 || parseInt(age) > 100) {
    showToast('Age must be between 1 and 100', 'error');
    return;
  }

  if (editId) {
    // Update
    const student = studentMap[parseInt(editId)];
    student.name = name;
    student.age = parseInt(age);
    student.department = dept;
    student.admissionDate = admDate;
    refreshAll();
    showToast(`Student ${name} updated successfully!`, 'success');
  } else {
    // Add
    const student = createStudent(id, name, age, dept, admDate);
    if (addStudent(student)) {
      showToast(`Student ${name} added successfully!`, 'success');
    } else {
      return;
    }
  }

  closeModal('studentModal');
}

// ── Delete ──
function openDeleteModal(id) {
  deleteTargetId = id;
  const s = studentMap[id];
  document.getElementById('deleteConfirmText').textContent =
    `Are you sure you want to delete "${s.name}" (ID: ${s.id})? This action cannot be undone.`;
  openModal('deleteModal');
}

function confirmDelete() {
  if (deleteTargetId !== null) {
    const name = studentMap[deleteTargetId]?.name;
    deleteStudent(deleteTargetId);
    showToast(`Student "${name}" deleted successfully`, 'success');
    deleteTargetId = null;
    closeModal('deleteModal');
  }
}

// ── View Student ──
function viewStudent(id) {
  const s = studentMap[id];
  if (!s) return;

  const gpa = GradeCalculator.calculateGPA(s.grades);
  const avgMarks = GradeCalculator.calculateAverageMarks(s.grades);
  const classification = GradeCalculator.getGPAClassification(gpa);
  const gpaClass = gpa >= 8 ? 'gpa-high' : gpa >= 5 ? 'gpa-mid' : 'gpa-low';
  const subjects = Object.keys(s.grades);

  let gradesHTML = '';
  if (subjects.length > 0) {
    gradesHTML = `<div style="margin-top:20px"><h4 style="font-size:14px;margin-bottom:10px;display:flex;align-items:center;gap:8px">📈 Grades</h4>
      <table class="data-table"><thead><tr><th>Subject</th><th>Marks</th><th>Grade</th></tr></thead><tbody>
      ${subjects.map(sub => {
        const m = s.grades[sub];
        return `<tr><td>${sub}</td><td>${m}</td><td><span class="badge badge-info">${GradeCalculator.getLetterGrade(m)}</span></td></tr>`;
      }).join('')}
      </tbody></table>
      <div style="margin-top:12px;display:flex;gap:16px;flex-wrap:wrap">
        <span style="font-size:13px;color:var(--text-muted)">Avg Marks: <strong style="color:var(--text-primary)">${avgMarks}</strong></span>
        <span style="font-size:13px;color:var(--text-muted)">GPA: <strong class="${gpaClass}">${gpa.toFixed(2)}</strong></span>
        <span style="font-size:13px;color:var(--text-muted)">Classification: <strong style="color:var(--text-primary)">${classification}</strong></span>
      </div>
    </div>`;
  }

  let attendanceHTML = '';
  if (s.attendanceLog.length > 0) {
    attendanceHTML = `<div style="margin-top:20px"><h4 style="font-size:14px;margin-bottom:10px;display:flex;align-items:center;gap:8px">📋 Attendance History</h4>
      <table class="data-table"><thead><tr><th>Date</th><th>Status</th><th>Remarks</th></tr></thead><tbody>
      ${s.attendanceLog.map(a => `<tr><td>${formatDate(a.date)}</td><td><span class="badge ${a.present ? 'badge-success' : 'badge-danger'}">${a.present ? '✓ Present' : '✗ Absent'}</span></td><td style="color:var(--text-muted)">${a.remarks || '—'}</td></tr>`).join('')}
      </tbody></table>
    </div>`;
  }

  document.getElementById('viewStudentContent').innerHTML = `
    <div class="detail-header" style="border:none;padding:0;background:transparent;margin-bottom:16px">
      <div class="detail-avatar">${s.name.charAt(0)}</div>
      <div class="detail-info">
        <h2>${s.name}</h2>
        <p>ID: ${s.id} · Age: ${s.age} · ${s.department}</p>
        <div class="detail-stats">
          <div class="detail-stat"><span class="label">Admission:</span><span class="value">${formatDate(s.admissionDate)}</span></div>
          <div class="detail-stat"><span class="label">Attendance:</span><span class="value" style="color:${s.attendancePercentage >= 75 ? 'var(--accent-emerald)' : 'var(--accent-rose)'}">${s.attendancePercentage.toFixed(1)}%</span></div>
        </div>
      </div>
    </div>
    ${gradesHTML}
    ${attendanceHTML}
  `;
  openModal('viewStudentModal');
}

// ── Attendance ──
function openAddAttendanceModal() {
  const studentId = document.getElementById('attendanceStudentSelect').value;
  if (!studentId) {
    showToast('Please select a student first', 'warning');
    return;
  }
  document.getElementById('attendanceDate').value = new Date().toISOString().split('T')[0];
  document.getElementById('attendanceStatus').value = 'true';
  document.getElementById('attendanceRemarks').value = '';
  openModal('attendanceModal');
}

function saveAttendance() {
  const studentId = document.getElementById('attendanceStudentSelect').value;
  const date = document.getElementById('attendanceDate').value;
  const present = document.getElementById('attendanceStatus').value === 'true';
  const remarks = document.getElementById('attendanceRemarks').value.trim();

  if (!studentId) { showToast('No student selected', 'error'); return; }
  if (!date) { showToast('Please enter a date', 'error'); return; }

  addAttendanceToStudent(parseInt(studentId), date, present, remarks);
  showToast('Attendance record added!', 'success');
  closeModal('attendanceModal');
  showAttendanceHistory();
}

function showAttendanceHistory() {
  const studentId = document.getElementById('attendanceStudentSelect').value;
  const tbody = document.getElementById('attendanceTableBody');
  const empty = document.getElementById('attendanceEmpty');
  const summary = document.getElementById('attendanceSummary');

  if (!studentId) {
    tbody.innerHTML = '';
    empty.style.display = 'block';
    summary.textContent = '';
    return;
  }

  const student = studentMap[parseInt(studentId)];
  if (!student || student.attendanceLog.length === 0) {
    tbody.innerHTML = '';
    empty.style.display = 'block';
    summary.textContent = student ? `${student.name} — No records yet` : '';
    return;
  }

  empty.style.display = 'none';
  const total = student.attendanceLog.length;
  const presentCount = student.attendanceLog.filter(r => r.present).length;
  summary.textContent = `${student.name} — ${presentCount}/${total} present (${student.attendancePercentage.toFixed(1)}%)`;

  tbody.innerHTML = student.attendanceLog.map((a, i) => `<tr>
    <td>${i + 1}</td>
    <td>${formatDate(a.date)}</td>
    <td><span class="badge ${a.present ? 'badge-success' : 'badge-danger'}">${a.present ? '✓ Present' : '✗ Absent'}</span></td>
    <td style="color:var(--text-muted)">${a.remarks || '—'}</td>
  </tr>`).join('');
}

// ── Grades ──
function openAddGradeModal() {
  const studentId = document.getElementById('gradeStudentSelect').value;
  if (!studentId) {
    showToast('Please select a student first', 'warning');
    return;
  }
  document.getElementById('gradeSubject').value = '';
  document.getElementById('gradeMarks').value = '';
  openModal('gradeModal');
}

function saveGrade() {
  const studentId = document.getElementById('gradeStudentSelect').value;
  const subject = document.getElementById('gradeSubject').value.trim();
  const marks = parseFloat(document.getElementById('gradeMarks').value);

  if (!studentId) { showToast('No student selected', 'error'); return; }
  if (!subject) { showToast('Please enter a subject name', 'error'); return; }
  if (isNaN(marks) || marks < 0 || marks > 100) { showToast('Marks must be between 0 and 100', 'error'); return; }

  addGradeToStudent(parseInt(studentId), subject, marks);
  showToast(`Grade added for ${subject}!`, 'success');
  closeModal('gradeModal');
  showGradeDetails();
}

function showGradeDetails() {
  const studentId = document.getElementById('gradeStudentSelect').value;
  const tbody = document.getElementById('gradesTableBody');
  const empty = document.getElementById('gradesEmpty');
  const summaryCard = document.getElementById('gpaSummaryCard');

  if (!studentId) {
    tbody.innerHTML = '';
    empty.style.display = 'block';
    summaryCard.style.display = 'none';
    return;
  }

  const student = studentMap[parseInt(studentId)];
  const subjects = Object.keys(student.grades);

  if (subjects.length === 0) {
    tbody.innerHTML = '';
    empty.style.display = 'block';
    summaryCard.style.display = 'none';
    return;
  }

  empty.style.display = 'none';
  summaryCard.style.display = 'block';

  const gpa = GradeCalculator.calculateGPA(student.grades);
  const avgMarks = GradeCalculator.calculateAverageMarks(student.grades);
  const classification = GradeCalculator.getGPAClassification(gpa);

  document.getElementById('studentGPA').textContent = gpa.toFixed(2);
  document.getElementById('studentAvgMarks').textContent = avgMarks.toFixed(1);
  document.getElementById('studentClassification').textContent = classification;

  tbody.innerHTML = subjects.map(sub => {
    const m = student.grades[sub];
    const letter = GradeCalculator.getLetterGrade(m);
    const gp = GradeCalculator.getGradePoint(m);
    const badgeClass = gp >= 8 ? 'badge-success' : gp >= 5 ? 'badge-warning' : 'badge-danger';
    return `<tr>
      <td style="font-weight:500;color:var(--text-primary)">${sub}</td>
      <td>${m}</td>
      <td><span class="badge ${badgeClass}">${letter}</span></td>
      <td>${gp.toFixed(1)}</td>
    </tr>`;
  }).join('');
}

// ══════════════════════════════════
//  NAVIGATION
// ══════════════════════════════════

const pageTitles = {
  dashboard: { title: 'Dashboard', subtitle: 'Overview of student records' },
  students: { title: 'Students', subtitle: 'Manage student records' },
  attendance: { title: 'Attendance', subtitle: 'Track student attendance' },
  grades: { title: 'Grades & GPA', subtitle: 'Manage grades and calculate GPA' },
  reports: { title: 'Summary Report', subtitle: 'Comprehensive analytics' },
  comparison: { title: 'Array vs ArrayList', subtitle: 'Data structure comparison' }
};

document.querySelectorAll('.nav-item').forEach(item => {
  item.addEventListener('click', () => {
    const page = item.dataset.page;
    navigateTo(page);
  });
});

function navigateTo(page) {
  // Update nav
  document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));
  document.querySelector(`[data-page="${page}"]`)?.classList.add('active');

  // Update pages
  document.querySelectorAll('.page-section').forEach(p => p.classList.remove('active'));
  document.getElementById(`page-${page}`)?.classList.add('active');

  // Update header
  const info = pageTitles[page] || {};
  document.getElementById('pageTitle').textContent = info.title || page;
  document.getElementById('pageSubtitle').textContent = info.subtitle || '';

  // Close sidebar on mobile
  document.getElementById('sidebar').classList.remove('open');
}

function toggleSidebar() {
  document.getElementById('sidebar').classList.toggle('open');
}

// ── Global Search ──
function handleGlobalSearch(query) {
  if (!query.trim()) {
    renderStudentTable(students);
    return;
  }

  // Switch to students page
  navigateTo('students');

  const q = query.toLowerCase();
  const results = students.filter(s =>
    s.id.toString().includes(q) ||
    s.name.toLowerCase().includes(q) ||
    s.department.toLowerCase().includes(q)
  );

  renderStudentTable(results);
}

// ══════════════════════════════════
//  UTILITIES
// ══════════════════════════════════

function getDeptClass(dept) {
  const d = dept.toLowerCase();
  if (d.includes('computer') || d.includes('cse') || d.includes('it')) return 'dept-cse';
  if (d.includes('electron') || d.includes('ece')) return 'dept-ece';
  if (d.includes('mechani') || d.includes('me')) return 'dept-me';
  if (d.includes('civil')) return 'dept-civil';
  return 'dept-other';
}

function formatDate(dateStr) {
  if (!dateStr) return '—';
  try {
    const d = new Date(dateStr);
    if (isNaN(d.getTime())) return dateStr;
    return d.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
  } catch {
    return dateStr;
  }
}

// ── Toast Notifications ──
function showToast(message, type = 'info') {
  const container = document.getElementById('toastContainer');
  const icons = { success: '✅', error: '❌', warning: '⚠️', info: 'ℹ️' };

  const toast = document.createElement('div');
  toast.className = `toast ${type}`;
  toast.innerHTML = `<span>${icons[type] || 'ℹ️'}</span><span>${message}</span>`;
  container.appendChild(toast);

  setTimeout(() => {
    toast.classList.add('toast-exit');
    setTimeout(() => toast.remove(), 300);
  }, 3000);
}

// ── Initialize ──
document.addEventListener('DOMContentLoaded', () => {
  refreshAll();
});
