# EBU6304 Group63 V1 Development Standards Manual

# Development Standards Document

# EBU6304 Group63 V1 Development Standards Manual

**Applicable Scenario**: Project V1 Parallel Development | **Effective Date**: 2026.03.29

**Document Purpose**: Unify code standards, ensure 6-member code has no conflicts, and can be integrated into a runnable version with one click

---

## I. Unified Core Standards (All Members Must Follow)

### 1. Fixed Package Structure (Cannot Modify/Add)

```Plain Text

src/
├── auth/        # Member 1: Login/Registration Module
├── user/        # Member 2: Personal Information Module
├── job/         # Member 3: Job Browsing Module
├── module/      # Member 4: Module Management Module
├── position/    # Member 5: Job Posting Module
├── common/      # Member 6: Common Utility Classes
└── Main.java    # System Single Entry Point (Cannot Modify Logic)
```

### 2. Fixed Data Files (Cannot Modify Filename/Format)

- `users.csv`: User Data

- `modules.csv`: Module Data

- `jobs.csv`: Job Data

- Storage Path: Project Root Directory, uniformly use `common.FileUtil` for read/write

### 3. Unified Code Standards

1. Class/Method names use **CamelCase**, clear comments

2. Prohibit modifying others' packages, classes, methods

3. Prohibit custom data files, utility classes

4. All functions provided through **fixed service methods**

### 4. Git Standards

1. Personal Branch: `feature-name`

2. Commit Format: `feat/module_name: complete xx function`

3. Only merge to `dev` branch, do not directly modify `master`

---

## II. Development Standards & Notes for Each Member

### Zhuang Zuchen (auth/ Login Registration Module)

1. Responsible: `AuthService` class, implement `register()` / `login()` methods

2. Only operate: `users.csv`

3. Note: Student ID uniqueness validation, password format validation

4. Prohibit: Modify other packages, modify utility classes, modify Main

### Wu Bohan (user/ Personal Information Module)

1. Responsible: `UserService` class, implement `updateInfo()` method

2. Only operate: `users.csv` (only modify non-core fields)

3. Note: Student ID/Name cannot be modified

4. Prohibit: Modify data file format, call other module business code

### Xing Wanying (job/ Job Browsing Module)

1. Responsible: `JobService` class, implement `showCategories()` method

2. Only operate: `jobs.csv`

3. Note: Job category display, filtering function

4. Prohibit: Modify module/job data structure

### Guo Aozhi (module/ Module Management Module)

1. Responsible: `ModuleService` class, implement `createModule()` method

2. Only operate: `modules.csv`

3. Note: Module information add/query/modify, bind MO account

4. Prohibit: Write job posting related logic in advance

### Yu Yue (position/ Job Posting Module)

1. Responsible: `PositionService` class, implement `publishJob()` method

2. Only operate: `jobs.csv`, associate with `modules.csv` fixed format

3. Note: Deadline validation, job storage

4. Prohibit: Modify module data, depend on member 4's runtime code

### Zhang Xinpeng (common/ Framework + Utility Classes)

1. **Pre-task**: Build complete project framework in 10 minutes (package structure + empty service classes + Main)

2. Responsible: `FileUtil` utility class, implement CSV unified read/write

3. Note: Encapsulate common methods, handle file exceptions

4. Permission: Only member who can modify framework and merge branches

---

## III. Code Integration Rules

1. After framework setup, **all members develop in parallel**, no waiting dependencies

2. After development completion, submit to personal branch, apply to merge to `dev`

3. Member 6 uniformly merges and tests, no other operations needed

4. Finally run `Main.java` to start the complete V1 system

---

## IV. Prohibited Behaviors (Violations Will Cause Integration Failure)

1. Prohibit modifying unified package structure, data file names

2. Prohibit modifying others' code, methods, classes

3. Prohibit custom utility classes, data storage methods

4. Prohibit directly committing code to `master` branch

---