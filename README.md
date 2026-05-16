# TA-Recruitment-System-Group63

BUPT International School TA Recruitment System

This repository contains the group coursework project for `EBU6304 Software Processes`. The aim of the project is to digitise the Teaching Assistant (TA) recruitment workflow at BUPT International School, which was previously handled through forms and spreadsheets. The repository currently includes two runnable versions of the system:

- `V1`: a command-line console version
- `V2`: a Java Swing graphical user interface version

The system supports three user roles:

- `TA`: maintain personal information, upload a CV, browse vacancies, submit applications, and view application status
- `MO`: publish vacancies, review applicants, and approve or reject applications
- `ADMIN`: manage modules and review TA workload

## Project Overview

At BUPT International School, Teaching Assistants are recruited every semester to support academic modules and related teaching activities. This project implements a standalone Java-based recruitment system to support that process. In line with the coursework requirements, the project:

- is developed in `Java`
- stores persistent data in `CSV` files
- does not use a database
- is managed collaboratively through `Git` and `GitHub`

The project demonstrates the complete recruitment workflow from TA application to MO review and administrative module management.

## Implemented Versions

### V1 Console Version

`V1` is located in the root `src/` directory and provides a command-line implementation of the core business workflow:

- user registration and login
- TA profile updates
- CV upload
- vacancy browsing and application submission
- application tracking
- job publication by MO users
- applicant review and application approval by MO / Admin users
- module management and workload review by Admin users

Entry point:

- `src/Main.java`

### V2 GUI Version

`V2` is located in `V2/src/` and extends the existing system with a Java Swing graphical interface while preserving the main business logic:

- login and registration windows
- TA Dashboard
- MO Dashboard
- Admin Dashboard
- profile, CV upload, job browsing, and application management views
- module management view
- workload statistics view

Entry point:

- `V2/src/Main.java`

## Core Features

### TA

- register and log in to the system
- maintain personal details such as email and major
- upload or update CV information
- browse published vacancies
- apply for available jobs
- review personal application status

### MO

- publish new TA vacancies
- review applicants for posted jobs
- approve or reject applications

### ADMIN

- create, edit, delete, and query modules
- review TA workload statistics

## Tech Stack

- Language: `Java`
- UI: `Java Swing` (`V2`)
- Data Storage: `CSV`
- Build Tool: direct compilation and execution using `javac` and `java`
- Documentation: Markdown + Javadoc

## Repository Structure

```text
TA-Recruitment-System-Group63-main/
|-- src/                         # V1 console source code
|   |-- application/
|   |-- auth/
|   |-- common/
|   |-- job/
|   |-- module/
|   |-- position/
|   |-- test/
|   |-- user/
|   `-- Main.java
|-- V2/
|   |-- src/                     # V2 GUI source code
|   |   |-- application/
|   |   |-- auth/
|   |   |-- common/
|   |   |-- gui/
|   |   |-- job/
|   |   |-- module/
|   |   |-- position/
|   |   |-- user/
|   |   `-- Main.java
|   |-- docs/javadoc/            # Javadoc output
|   `-- USER_MANUAL.md           # V2 user manual
|-- README.md
|-- DEVELOPMENT_STANDARDS.md
|-- V2_DEVELOPMENT_GUIDE.md
|-- users.csv
|-- modules.csv
|-- jobs.csv
`-- applications.csv
```

## Environment Requirements

- JDK `8` or higher
- Windows / macOS / Linux with Java runtime available
- Terminal or IDE capable of running `javac` and `java`

## Quick Start

### Run V1

Run the following commands in the repository root:

```bash
javac -d out -sourcepath src src/Main.java
java -cp out Main
```

### Run V2

Run the following commands in the `V2/` directory:

```bash
javac -d bin -sourcepath src src/Main.java
java -cp bin Main
```

Notes:

- `V2` launches the Swing main frame together with the login window
- `V2` continues to use the CSV files stored in the repository root as its data source

## Test

The repository includes a simple `V1` business-flow test entry:

```bash
javac -d out -sourcepath src src/test/CompleteTest.java
java -cp out test.CompleteTest
```

Notes:

- this test reads and may modify `applications.csv`
- if repeated demonstrations are required, backing up the data files is recommended

## Data Files



| File | Purpose | Main Fields |
|---|---|---|
| `users.csv` | user account and profile data | `studentId,name,password,email,major,role,intention,experience,skills` |
| `modules.csv` | module records | `moduleId,moduleName,moduleLeader,creationDate` |
| `jobs.csv` | job vacancy records | `jobId,title,hours,moduleId,requirements,description` |
| `applications.csv` | job application records | `studentId,jobId,status,applyDate,decisionDate` |

The repository already includes sample data that can be used directly for demonstration and testing.

## Demo Accounts

Several demo accounts are already included in the project data files and can be used for local testing:

| Role | Student ID | Password |
|---|---|---|
| `TA` | `2023001` | `123456` |
| `MO` | `MO001` | `mo123456` |
| `ADMIN` | `ADMIN001` | `admin123456` |

For additional accounts, please refer to `users.csv`.

## Project Documents

- `README.md`: overall project overview
- `V2/USER_MANUAL.md`: user manual for the GUI version
- `V2_DEVELOPMENT_GUIDE.md`: development and task-allocation guide for `V2`
- `DEVELOPMENT_STANDARDS.md`: project development standards
- `V2/docs/javadoc/`: generated Javadoc documentation
- `PROJECT_STRUCTURE.txt`: early project structure notes

## Team Members

| GitHub Username | Name | QMUL ID |
|---|---|---|
| `ZZC10` | Zuchen Zhuang | 231226107 |
| `SixId666` | Xinpeng Zhang | 231226071 |
| `XY20210401` | Yue Yu | 231226152 |
| `showmaker423` | Bohan Wu | 231226200 |
| `aaoozzhhii` | Aozhi Guo | 231226082 |
| `Xwy2005` | Wanying Xing | 231226934 |

## Notes

- This project is coursework and is intended to demonstrate software process, teamwork, modular development, and end-to-end functionality.
- The system uses text-file persistence for simplicity and to satisfy the coursework constraints; it is suitable for academic demonstration rather than production deployment.
- For detailed GUI operation guidance, please refer to `V2/USER_MANUAL.md`.
