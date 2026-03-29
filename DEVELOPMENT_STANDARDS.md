# EBU6304 Group63 V1 Development Standards Manual

# Development Standards Document

# EBU6304 Group63 V1 Development Standards Manual

**Applicable Scenario**: Parallel development for project V1 | **Effective Date**: 2026.03.29

**Purpose**: To unify code standards, ensuring the work of six members does not conflict and can be integrated into a runnable V1 version with a single merge.

---

## 1. Unified Core Standards (Mandatory for all members)

### 1. Fixed package structure (do not modify or add)

```Plain Text

src/
├── auth/        # Member 1: Login/Register module
├── user/        # Member 2: Personal information module
├── job/         # Member 3: Job browsing module
├── module/      # Member 4: Module management module
├── position/    # Member 5: Job posting module
├── common/      # Member 6: Common utilities
└── Main.java    # The single system entry point (do not change logic)
```

### 2. Fixed data files (do not change file names or formats)

- `users.csv`: user data

- `modules.csv`: module data

- `jobs.csv`: job data

- Storage location: project root; always read/write via `common.FileUtil`

### 3. Unified coding conventions

1. Class and method names use camelCase; comments must be clear.

2. Do not modify other members' packages, classes, or methods.

3. Do not create custom data files or utility classes.

4. All functionality must be exposed through the fixed service methods.

### 4. Git conventions

1. Personal branch name: `feature-Name`

2. Commit message format: `feat/<module>: completed <feature>`

3. Merge only into the `dev` branch; do not commit directly to `master`.

---

## 2. Individual Member Development Rules & Notes

### Zhuang Zuchen (auth/ Login & Registration module)

1. Responsible: `AuthService` class — implement `register()` and `login()` methods.

2. Only operate on: `users.csv`.

3. Notes: Ensure unique student ID validation and password format validation.

4. Prohibited: Modifying other packages, utility classes, or `Main`.

### Wu Bohan (user/ Personal Information module)

1. Responsible: `UserService` class — implement `updateInfo()` method.

2. Only operate on: `users.csv` (only modify non-core fields).

3. Notes: Student ID and name must not be changed.

4. Prohibited: Modifying data file formats or calling other modules' business logic.

### Xing Wanying (job/ Job Browsing module)

1. Responsible: `JobService` class — implement `showCategories()` method.

2. Only operate on: `jobs.csv`.

3. Notes: Implement job category display and filtering features.

4. Prohibited: Modifying module or job data structures.

### Guo Aozhi (module/ Module Management module)

1. Responsible: `ModuleService` class — implement `createModule()` method.

2. Only operate on: `modules.csv`.

3. Notes: Handle module addition, retrieval, and updates; bind MO accounts.

4. Prohibited: Preemptively writing job posting related logic.

### Yu Yue (position/ Job Posting module)

1. Responsible: `PositionService` class — implement `publishJob()` method.

2. Only operate on: `jobs.csv`; must relate to `modules.csv` in the fixed format.

3. Notes: Validate deadlines and persist job postings.

4. Prohibited: Modifying module data or depending on Member 4's runtime code.

### Zhang Xinpeng (common/ Framework & Utility classes)

1. **Pre-task**: Spend 10 minutes building the complete project skeleton (package structure + empty service classes + Main).

2. Responsible: `FileUtil` utility class to implement unified CSV read/write.

3. Notes: Encapsulate common methods and handle file exceptions.

4. Privileges: Sole authority to modify the framework and perform merges.

---

## 3. Code Integration Rules

1. After the framework is set up, all members develop in parallel without waiting for dependencies.

2. After development, push to your personal branch and request a merge into `dev`.

3. Member 6 will handle merging and testing centrally; others need not take action.

4. Running `Main.java` should start the full V1 system.

---

## 4. Prohibited Actions (Violations may cause integration failure)

1. Do not modify the unified package structure or data file names.

2. Do not modify other people's code, methods, or classes.

3. Do not create custom utility classes or change the data storage approach.

4. Do not commit directly to the `master` branch.

---


