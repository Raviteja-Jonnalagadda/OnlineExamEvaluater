# 🧠 Online Exam Evaluator

A full-stack web application that allows administrators to create and manage online exams, and students to attempt them with real-time evaluation — built using **Spring Boot**, **Core Java**, **JSP**, **jQuery**, and **Oracle SQL**.

---

## 🔍 Features

### 👨‍🏫 Admin Panel
- Create unique exams with dynamic IDs
- Add multiple-choice questions with options
- Update or delete existing questions
- View student results and exam attempts

### 👨‍🎓 Student Portal
- Student login with validation
- Attempt live exams with timer-based UI
- See results instantly upon submission
- Review attempted questions with correct answers

---

## ⚙️ Tech Stack

| Layer        | Technology Used                         |
|--------------|-----------------------------------------|
| Frontend     | HTML, CSS, JSP, jQuery                  |
| Backend      | Core Java, Spring Boot, Servlets , AWT  |
| Database     | Oracle SQL 11g                          |
| Persistence  | Classic JDBC , Spring JDBC Templets     |
| Tools        | Eclipse IDE, Maven, Git                 |

---

## 🗂️ Project Structure

OnlineExamEvaluator/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com.onlineexam/ ← Core logic & controllers
│ │ └── webapp/
│ │ ├── WEB-INF/
│ │ │ └── jsp/ ← JSP views
│ │ └── static/ ← CSS, JS
├── resources/
│ └── application.properties
├── pom.xml
└── README.md
==================================================================================================================================================================================================
##  How to Run

1. Clone this repository:
   ```bash
   git clone https://github.com/Raviteja-Jonnalagadda/OnlineExamEvaluater.git
2.) Import into Eclipse as a Maven project

4.) Configure your database:
Oracle DB

Update application.properties or DB config section with your credentials

5.) Run the project:

Launch via SpringBootApplication class or deploy on a servlet container (Tomcat)

Access the app:

http://localhost:8080/
