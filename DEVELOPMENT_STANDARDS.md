# EBU6304 Group63 V1 Development Standards Manual

**Applicable Scenario**: Parallel development for project V1 | **Effective Date**: 2026.03.29 | **Last Updated**: 2026.04.19

**Purpose**: To unify code standards, ensuring the work of six members does not conflict and can be integrated into a runnable V1 version with a single merge.

---

## 1. Unified Core Standards

### 1.1 Fixed Package Structure

```
src/
├── auth/        # Member 1: Login/Register + Role management
├── user/        # Member 2: Personal information + CV
├── job/         # Member 3: Job browsing + Application
├── module/      # Member 4: Module management
├── position/    # Member 5: Job publishing
├── common/      # Member 6: FileUtil + Framework
└── Main.java    # Single entry point (role-based menu)
```

### 1.2 Fixed Data Files

| File | Fields | Storage |
|------|--------|---------|
| `users.csv` | studentId,name,password,email,major,intention,experience,skills,role | Project root |
| `jobs.csv` | jobId,jobTitle,jobCategory,moduleId,deadline,description | Project root |
| `modules.csv` | moduleId,moduleName,moduleLeader,creationDate | Project root |
| `applications.csv` | studentId,jobId,status,applyDate,decisionDate | Project root |

**Note**: All file operations must use `common.FileUtil`

### 1.3 Role Definition

| Role | Description | Permissions |
|------|-------------|-------------|
| TA | Teaching Assistant applicant | Browse jobs, apply, check status, update CV |
| MO | Module Organiser | Publish jobs, view applicants, approve/reject |
| ADMIN | System administrator | Manage modules, view TA workload, manage users |

---

## 2. Team Member Assignments

### Member 1: Zhuang Zuchen (auth/)
**Files**: `src/auth/AuthService.java`, `users.csv`
**Tasks**:
- Add `role` field to `users.csv` (default: TA)
- Implement role management methods: `getUserRole()`, `hasRole()`
- Modify `register()` to accept role parameter (default: TA)
- Modify `login()` to return user role

### Member 2: Wu Bohan (user/)
**Files**: `src/user/UserService.java`, `src/user/UploadCV.java`
**Tasks**:
- Add `getUserByStudentId()`, `getAllUsers()`, `updateUserRole()`
- Ensure CV upload only for TA role
- Add permission checks to existing methods

### Member 3: Xing Wanying (job/)
**Files**: `src/job/JobService.java`
**Tasks**:
- Add `applyForJob()` - TA applies for a position
- Add `viewApplicants()` - MO views applicants for a job
- Add `showJobsByModule()` - MO views jobs by module
- Add permission checks (TA: apply; MO: view applicants)

### Member 4: Guo Aozhi (module/)
**Files**: `src/module/ModuleService.java`
**Tasks**:
- Add `getModulesByLeader()`, `getModuleById()`
- Add permission checks (ADMIN only for create/modify/delete)
- Bind MO accounts to modules

### Member 5: Yu Yue (position/)
**Files**: `src/position/PositionService.java`
**Tasks**:
- Add permission checks to `publishJob()` (MO/ADMIN only)
- Validate module existence before publishing

### Member 6: Zhang Xinpeng (common/)
**Files**: `src/common/FileUtil.java`, `src/Main.java`, `applications.csv`
**Tasks**:
- Create `applications.csv` file
- Create `ApplicationService.java` (or assign to another member)
- Modify `Main.java` for role-based menus (showTAMenu/showMOMenu/showAdminMenu)
- Coordinate module integration
- Optimize FileUtil if needed

### Application Module (NEW - Assign to one member)
**Files**: `applications.csv`, `src/application/ApplicationService.java`
**Tasks**:
- Create `applications.csv` with fields: studentId,jobId,status,applyDate,decisionDate
- Implement methods:
  - `applyForJob(studentId, jobId)` - TA applies
  - `getApplicationStatus(studentId, jobId)` - TA checks status
  - `getApplicantsByJob(jobId)` - MO views applicants
  - `approveApplication(studentId, jobId, approved)` - MO approves/rejects
  - `getTAWorkload(studentId)` - ADMIN views statistics
- Implement constraints:
  - Prevent duplicate applications
  - Validate job existence
  - Validate user existence
  - Status flow: PENDING → ACCEPTED/REJECTED

---

## 3. Permission Matrix

| Function | TA | MO | ADMIN |
|----------|----|----|-------|
| Register | ✅ | ✅ | ✅ |
| Login | ✅ | ✅ | ✅ |
| Update Personal Info | ✅ | ✅ | ✅ |
| Upload/Update CV | ✅ | ❌ | ❌ |
| Browse Jobs | ✅ | ✅ | ✅ |
| Apply for Job | ✅ | ❌ | ❌ |
| Check Application Status | ✅ | ❌ | ❌ |
| Publish Job | ❌ | ✅ | ✅ |
| View Applicants | ❌ | ✅ | ✅ |
| Approve/Reject | ❌ | ✅ | ✅ |
| Manage Modules | ❌ | ❌ | ✅ |
| View TA Workload | ❌ | ❌ | ✅ |
| Manage Users | ❌ | ❌ | ✅ |

---

## 4. Business Process

### TA Flow
```
Browse Jobs → Apply for Job → Check Application Status → (Result: ACCEPTED/REJECTED)
```

### MO Flow
```
View Job Applicants → Review Applicant Info → Approve/Reject Application
```

### ADMIN Flow
```
View TA Workload Statistics → (Optional) Manage Users/Modules
```

---

## 5. Module Integration

| From | To | Call |
|------|----|------|
| Main | Auth | `getUserRole()`, `login()` |
| Job | Application | `applyForJob()`, `getApplicantsByJob()` |
| Application | User | `getUserByStudentId()` |
| Position | Module | `getModuleById()` (validate) |
| Module | Job | `getJobsByModule()` |

---

## 6. Git Conventions

- **Branch name**: `feature-Name`
- **Commit format**: `feat/<module>: completed <feature>`
- **Merge target**: `dev` branch only
- **Prohibited**: Direct commit to `master`

---

## 7. Prohibited Actions

1. Do not modify package structure or data file names
2. Do not modify other members' code without permission
3. Do not create custom utility classes (use FileUtil)
4. Do not change CSV storage approach
5. Do not delete existing functionality (extend only)
6. Do not commit directly to `master`

---

## 8. File Modification Checklist

| File | Owner | Status | Description |
|------|-------|--------|-------------|
| users.csv | Zhuang Zuchen | ⬜ | Add role field |
| AuthService.java | Zhuang Zuchen | ⬜ | Add role methods |
| Main.java | Zhang Xinpeng | ⬜ | Role-based menus |
| applications.csv | TBD | ⬜ | Create new file |
| ApplicationService.java | TBD | ⬜ | Create new class |
| UserService.java | Wu Bohan | ⬜ | Add query methods |
| JobService.java | Xing Wanying | ⬜ | Add apply functions |
| PositionService.java | Yu Yue | ⬜ | Add permission checks |
| ModuleService.java | Guo Aozhi | ⬜ | Add permission checks |

---

## 9. Notes

1. **Default role for new users**: TA
2. **Application status values**: PENDING, ACCEPTED, REJECTED
3. **Scanner management**: Single instance in Main, passed via `setScanner()`
4. **All members must read this document before starting development**
5. **Questions? Contact Member 6 for framework-related issues**