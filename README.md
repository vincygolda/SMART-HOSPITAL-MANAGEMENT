# Smart Hospital Emergency & Intelligent Queue Management System

## Project Overview
The Smart Hospital Emergency & Intelligent Queue Management System is a Java-based project designed to organize patient registration and manage the hospital waiting queue based on emergency priority.

It helps prioritize patients according to their assigned emergency level and maintains patient records and treatment history using file handling.

## Objectives
- Register patients with unique patient IDs.
- Prioritize patients based on emergency levels.
- Display the waiting queue and estimated waiting time.
- Maintain patient records using file handling.
- Store and view treatment history.

## Technologies Used
- Java
- Visual Studio Code
- Java Collections Framework
- File Handling

## Main Features
1. **Patient Registration:** Registers patients with an automatically generated ID.
2. **Emergency Priority Queue:** Prioritizes Critical, Urgent, and Normal categories.
3. **Input Validation:** Validates patient details and emergency levels.
4. **Waiting Time Estimation:** Displays an estimated waiting time based on queue priority.
5. **Patient Record Storage:** Saves waiting patient records in a file.
6. **Treatment History:** Stores and displays records of treated patients.

## Emergency Levels
- Level 1 – Critical
- Level 2 – Urgent
- Level 3 – Normal

## Files Used
- `main.java` – Main program and menu operations.
- `patient.java` – Patient details and related methods.
- `patients.txt` – Stores waiting patient records when the program runs.
- `treatment_history.txt` – Stores treatment history when the program runs.

## How to Run
1. Install the Java Development Kit (JDK).
2. Open the project folder in Visual Studio Code.
3. Open the terminal in the project folder.
4. Compile the Java files:

   `javac main.java patient.java`

5. Run the program:

   `java Main`

## Future Enhancements
- Add a graphical user interface.
- Connect the application to a database.
- Add separate login access for hospital staff.
- Improve waiting-time estimates using configurable treatment durations.

## Disclaimer
This is an educational prototype. Emergency priority levels and waiting-time estimates are illustrative and should not be used for actual medical triage or treatment decisions.