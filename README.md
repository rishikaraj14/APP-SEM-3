Java Swing Projects – Calculator & Student Registration Form

This repository contains two Java-based GUI projects developed using Swing and JDBC as part of the Advanced Programming Practices course.

Both projects are simple desktop applications designed to demonstrate practical use of Java Swing, event handling, and database connectivity.

🧮 Project 1: Calculator
Description

A simple calculator application built using Java Swing that performs basic arithmetic operations such as addition, subtraction, multiplication, and division.

Features

Clean and responsive GUI

Supports +, -, *, / operations

Handles decimal values

Displays calculation errors (like divide-by-zero) gracefully

“C” button to clear the display

How to Run

Open the project in your IDE (e.g., IntelliJ, Eclipse, or NetBeans).

Compile and run the Calculator.java file.

Use the on-screen buttons to perform calculations.

🎓 Project 2: Student Registration Form
Description

A desktop application that allows users to manage student records using Java Swing and MySQL (JDBC).
You can add, update, delete, and view student information.

Features

Add new student records

Update or delete existing records

View all students in a table

Input validation for name, age, and email

MySQL database integration via JDBC

Database Setup

Create a MySQL database named studentdb.

Create a table named students using the following SQL:

CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    age INT,
    email VARCHAR(100) UNIQUE,
    course VARCHAR(100)
);


Update your database username and password in the code:

static final String USER = "root";
static final String PASS = "your_password";

How to Run

Make sure MySQL server is running.

Open the project in your IDE.

Compile and run StudentRegistrationForm.java.

Use the form to manage student records.

🛠 Technologies Used

Java Swing – GUI design

JDBC – Database connectivity

MySQL – Backend database

Java AWT – Event handling and layout management

👩‍💻 Author

Rishika Raj
Advanced Programming Practices – Java Swing Projects
