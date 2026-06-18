# 🧠 Smart Course Registration & Student Management System (SCRNS)

A Java Swing-based desktop application using **JDBC (SQLite), DAO Pattern, and MVC-inspired architecture**.  
This project demonstrates full CRUD operations with a modern UI using FlatLaf.

---

## 🚀 Features

- ➕ Add Student (Register)
- ✏ Update Student
- ❌ Delete Student
- 🔍 Search Student
- 📋 View All Students
- 🖱 Click Row → Auto Fill Form
- 💾 SQLite Database Integration
- 🎨 Modern Dark UI (FlatLaf)
- 🏗 Layered Architecture (Model + DAO + Service + GUI)

---

## 🏗 Project Structure

```

com.scrns
│
├── model
│   ├── Student
│   ├── User
│   └── Course
│
├── database
│   ├── DatabaseManager
│   └── StudentDAO
│
├── service
│   └── StudentService
│
├── gui
│   └── MainFrame
│
└── Main

````

---

## 🛠 Tech Stack

- Java (Swing)
- JDBC
- SQLite
- Maven
- FlatLaf (UI Theme)

---

## ⚙ How to Run Project

### 1. Clone Repository
```bash
git clone <your-repo-link>
````

### 2. Open in IntelliJ IDEA

* Open IntelliJ IDEA
* Click **Open Project**
* Select project folder

---

### 3. Build Maven Dependencies

```bash
mvn clean install
```

OR:

```
Maven → Reload Project
```

---

### 4. Run Application

Run:

```
Main.java
```

---

## 📦 Database Info

SQLite database auto-created:

```
scrns.db
```

### Table:

```sql
CREATE TABLE students (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL
);
```

---

## 🎯 Learning Outcomes

This project demonstrates:

* DAO Design Pattern
* MVC Architecture Concept
* JDBC CRUD Operations
* Swing GUI Development
* Layered Software Architecture

---

## 👨‍💻 Developer

* Name: Your Name Here
* Role: Java Developer / AI Engineer (Student)

---

## ⭐ Future Improvements

* Login System (Admin / Student)
* Dashboard Analytics
* Export to PDF/Excel
* Spring Boot Backend Version

```
