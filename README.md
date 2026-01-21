# 🚖 RapidRide - Cab Service Management System

**RapidRide** is a robust desktop application designed for efficient taxi fleet management and booking operations. Built using **Java 21** and **JavaFX**, it leverages the **MVC (Model-View-Controller)** pattern to provide a scalable and maintainable solution for transportation businesses.

---

## ✨ Key Features

* **Authentication System:** Secure Sign-In and Sign-Up interfaces to control access for staff members.
* **Centralized Dashboard:** A sidebar-driven navigation hub for managing all business entities.
* **Personnel Management:** Full CRUD operations for managing Employees, Drivers, and Customers.
* **Fleet Tracking:** Monitor vehicle availability, fuel efficiency (Fuel per KM), and hybrid status.
* **Booking Engine:** Streamlined ride scheduling with automated distance and cost calculations.
* **Payment Ledger:** Comprehensive tracking of transactions with support for Cash, Card, and Bank Transfers.
* **Reporting:** Integrated **JasperReports** functionality for generating professional PDF documents.

---

## 🛠️ Technical Stack

* **Language:** Java 21
* **UI Framework:** JavaFX 21 (OpenJFX)
* **Build Tool:** Maven
* **Database:** MySQL (via JDBC Connector 9.0.0)
* **Utilities:** Lombok (Boilerplate reduction), Jackson (JSON/XML data processing), and SLF4J (Logging)
* **Testing:** JUnit 5

---

## 🏗️ Architecture

The project follows a standard MVC architecture to separate business logic from the user interface:



* **Model:** Handles data entities and database persistence using MySQL.
* **View:** FXML-based layouts styled for a modern user experience.
* **Controller:** Manages user interactions and connects the view to the underlying data.

---

## ⚙️ Setup and Installation

1.  **Clone the Repository:**
    `git clone https://github.com/tharu-2003/RapidRide.git`
2.  **Database Configuration:**
    Set up a MySQL server and import the required database schema.
3.  **Build the Project:**
    `mvn clean install`
4.  **Run the Application:**
    `mvn javafx:run`

---

## 📧 Contact
**Project Developer:** [tharu-2003](https://github.com/tharu-2003)  
**Project Link:** [https://github.com/tharu-2003/RapidRide](https://github.com/tharu-2003/RapidRide)