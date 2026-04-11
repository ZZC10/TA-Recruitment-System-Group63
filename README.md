# TA-Recruitment-System-Group63

BUPT International School TA Recruitment System

This repository hosts the group project for EBU6304 Software Processes (Spring 2026). We are developing a Teaching Assistant (TA) recruitment system for BUPT International School to digitise and streamline the current form-based application process.

# Project Overview

BUPT International School recruits Teaching Assistants (TA) each semester to support academic modules and various school activities. Currently, the application process relies on forms and Excel files. This system aims to streamline the workflow by providing a simple application for:

* **TAs**: Create profiles, upload CVs, find and apply for jobs, check application status
* **Module Organisers (MOs)**: Post jobs, select applicants
* **Admins**: Check TA workload

# Technical Constraints

As required by the coursework specification:

* **Development Options** (choose one):

  * Stand-alone Java application
  * Java Servlet/JSP web-based application
* **Data Storage**: Simple text files (TXT, CSV, JSON, or XML) - **NO databases**
* **Version Control**: Git + GitHub with individual branches for each member

# Team Members

|GitHub Username|Name|QMUL ID|
|-|-|-|
|ZZC10|Zuchen Zhuang|231226107|
|SixId666|Xinpeng Zhang|231226071|
|XY20210401|Yue Yu|231226152|
|showmaker423|Bohan Wu|231226200|
|aaoozzhhii|Aozhi Guo|231226082|
|Xwy2005|Wanying Xing|231226934|

# Module Structure

## 1. Auth Module (`src/auth/`)

### AuthService.java
- **Purpose**: Handles user authentication (registration and login)
- **Key Functions**:
  - `register(String studentId, String password, String name)`: Registers a new user
  - `login(String studentId, String password)`: Authenticates an existing user
  - `register()`: No-argument method for Main.java call
  - `login()`: No-argument method for Main.java call

## 2. User Module (`src/user/`)

### UserService.java
- **Purpose**: Manages user personal information
- **Key Functions**:
  - `updateInfo()`: Updates user personal information (email, major)

### UploadCV.java
- **Purpose**: Handles CV upload and update functionality
- **Key Functions**:
  - `upload()`: Uploads or updates user CV information (intention, experience, skills)

## 3. Job Module (`src/job/`)

### JobService.java
- **Purpose**: Manages job categories and job listings
- **Key Functions**:
  - `showCategories()`: Displays available job categories
  - `displayJobsByCategory()`: Displays jobs in a selected category

## 4. Module Module (`src/module/`)

### ModuleService.java
- **Purpose**: Manages academic modules
- **Key Functions**:
  - `createModule()`: Main entry for module management
  - `performCreate()`: Creates a new module
  - `displayModules()`: Displays all existing modules
  - `performModify()`: Modifies module information
  - `performDelete()`: Deletes a module
  - `performSearch()`: Searches for modules

## 5. Position Module (`src/position/`)

### PositionService.java
- **Purpose**: Handles job position publishing
- **Key Functions**:
  - `publishJob()`: Publishes a new job position

## 6. Common Module (`src/common/`)

### FileUtil.java
- **Purpose**: Provides common file operations for CSV files
- **Key Functions**:
  - `readCSV(String filePath)`: Reads data from a CSV file
  - `writeCSV(String filePath, List<String[]> data)`: Writes data to a CSV file
  - `appendToCSV(String filePath, String[] data)`: Appends data to a CSV file
  - `ensureHeader(String filePath, String[] header)`: Ensures the CSV file has the correct header

### TestFramework.java
- **Purpose**: Provides testing functionality for the system

## 7. Main Module (`src/`)

### Main.java
- **Purpose**: Main entry point for the application
- **Key Functions**:
  - `main(String[] args)`: Main method that starts the application
  - Displays the main menu and handles user input

# System Operation Instructions

## How to Run the System

1. **Compile the code**:
   ```bash
   javac -cp src src/Main.java
   ```

2. **Run the application**:
   ```bash
   java -cp src Main
   ```

## Main Menu Options

1. **User Registration** - Register a new user account
2. **User Login** - Login to an existing account
3. **Update Personal Info** - Update user personal information
4. **Browse Job Categories** - Browse available job categories and jobs
5. **Create Module** - Manage academic modules (create, query, modify, delete, search)
6. **Publish Job** - Publish a new job position
7. **Upload/Update CV** - Upload or update user CV information
0. **Exit System** - Exit the application

## Detailed Operation Steps

### 1. User Registration
1. Enter option `1` from the main menu
2. Enter your Student ID (e.g., 2023001)
3. Enter a password (at least 6 characters, including letters and numbers)
4. Enter your full name
5. System will confirm registration success

### 2. User Login
1. Enter option `2` from the main menu
2. Enter your Student ID
3. Enter your password
4. System will confirm login success

### 3. Update Personal Info
1. Enter option `3` from the main menu
2. Enter your Student ID
3. Enter new email (press enter to keep current)
4. Enter new major (press enter to keep current)
5. System will confirm information update success

### 4. Browse Job Categories
1. Enter option `4` from the main menu
2. Select a job category from the list
3. View jobs in the selected category
4. Press Enter to return to main menu

### 5. Create Module
1. Enter option `5` from the main menu
2. Select an option:
   - `1`: Create New Module
   - `2`: Query All Modules
   - `3`: Modify Module Info
   - `4`: Delete Module
   - `5`: Search Module
   - `0`: Back to main menu

### 6. Publish Job
1. Enter option `6` from the main menu
2. Enter Job ID (e.g., J001)
3. Enter Job Title (e.g., Teaching Assistant)
4. Enter Job Category (e.g., Teaching, Research)
5. Enter Module ID (e.g., M001)
6. Enter Deadline (YYYY-MM-DD, e.g., 2026-12-31)
7. Enter Job Description
8. System will confirm job publication success

### 7. Upload/Update CV
1. Enter option `7` from the main menu
2. Enter your Student ID
3. Enter Intention (e.g., Software Development TA)
4. Enter Personal Experience (e.g., Internship, Projects)
5. Enter Personal Skills (e.g., Java, Python, Communication)
6. System will confirm CV upload/update success

# Development Notes

## Data Files

The system uses CSV files for data storage:

- `users.csv` - Stores user information (studentId, name, password, email, major, intention, experience, skills)
- `jobs.csv` - Stores job information (jobId, jobTitle, jobCategory, moduleId, deadline, description)
- `modules.csv` - Stores module information (moduleId, moduleName, moduleLeader, creationDate)

## Scanner Instance

The system uses a single Scanner instance shared across all service classes to avoid input stream conflicts. The Scanner instance is created in Main.java and passed to all service classes via the `setScanner()` method.

## Error Handling

The system includes error handling for common issues:
- Invalid input format
- Duplicate IDs
- Non-existent records
- File operations

## Coding Standards

- Follow Java naming conventions
- Use meaningful variable and method names
- Include comments for complex functionality
- Handle exceptions appropriately
- Maintain consistent code style

## Testing

Use the TestFramework.java class to test individual components of the system. Run the main application to test end-to-end functionality.


