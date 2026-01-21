DROP DATABASE RapidRide;

CREATE DATABASE RapidRide;
USE RapidRide;

CREATE TABLE Employee (
    employee_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100)NOT NULL,
    role VARCHAR(20)NOT NULL,
    contact VARCHAR(20)NOT NULL,
    gmail VARCHAR(100)
);

CREATE TABLE User (
    user_id VARCHAR(20) PRIMARY KEY,
	employee_id VARCHAR(20) NOT NULL,
    name VARCHAR(50) NOT NULL,
    user_name VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE Customer (
    customer_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100)NOT NULL,
    contact VARCHAR(20)NOT NULL,
    gmail VARCHAR(100) UNIQUE,
    user_id VARCHAR(20)NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);


CREATE TABLE Booking (
    booking_id  VARCHAR(30)PRIMARY KEY,
    ride_to VARCHAR(100),
    distance DECIMAL(5,3),
    required_date DATE NOT NULL,
    total_amount DECIMAL(10, 2)NOT NULL,
    status VARCHAR(50)NOT NULL,
    vehicle_count INT NOT NULL,
    customer_id VARCHAR(20)NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE Payment (
    payment_id VARCHAR(20) PRIMARY KEY,
	booking_id VARCHAR(20)NOT NULL,
    amount DECIMAL(10, 2)NOT NULL,
    payment_method VARCHAR(50)NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    status VARCHAR(50)NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES Booking(booking_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE Vehicle (
    vehicle_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(50)NOT NULL,
    type VARCHAR(50)NOT NULL,
    fuel_to_km DECIMAL(5,2),
    status VARCHAR(50)NOT NULL
);


CREATE TABLE Booking_Details (
	booking_date DATE NOT NULL,
    booking_time TIME NOT NULL,
    booking_id VARCHAR(20)NOT NULL,
    vehicle_id VARCHAR(20)NOT NULL,
    PRIMARY KEY (booking_id, vehicle_id),
    FOREIGN KEY (booking_id) REFERENCES Booking(booking_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicle(vehicle_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE Driver (
    driver_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100)NOT NULL,
    contact VARCHAR(20)NOT NULL,
    gmail VARCHAR(100),
    vehicle_id VARCHAR(20)NOT NULL,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicle(vehicle_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

INSERT INTO Employee (employee_id, name, role, contact, gmail)
VALUES
('E001', 'Sachini', 'Admin', '0712353785', 'sachini785@gmail.com'),
('E002', 'Rethmi', 'Manager', '0704839927', 'rethmi927@gmail.com'),
('E003', 'Tharusha', 'Receptionist', '0781434888', 'tharusha888@gmail.com'),
('E004', 'Anjana', 'Receptionist', '0764810851', 'anjana851@gmail.com');

INSERT INTO User (user_id, employee_id, user_name,name, password)
VALUES
('U001', 'E001', 'sachini123','Sachini', 's123'),
('U002', 'E002', 'rethmi123','Rethmi', 'r123'),
('U003', 'E003', 'tharusha888','Tharusha', 't123'),
('U004', 'E004', 'anjana123','Anjana', 'a123');

INSERT INTO Customer (customer_id, name, contact, gmail, user_id)
VALUES
('C001', 'Kamal', '0711234567', 'kamal567@gmail.com', 'U003'),
('C002', 'Nimal', '0712345678', 'nimal678@gmail.com', 'U003'),
('C003', 'Malithi', '0723456789', 'malithi789@gmail.com', 'U004');

INSERT INTO Booking (booking_id, ride_to, distance, required_date, total_amount, status,vehicle_count, customer_id) 
VALUES 
('B001', 'Downtown', 12.5, '2024-10-05', 1250.00, 'completed', 1 , 'C001'),
('B002', 'Airport', 56, '2024-10-07', 5600.00, 'completed', 1 , 'C002'),
('B003', 'Shopping Mall', 10.2, '2024-10-06', 1020.00, 'pending', 1 , 'C003');


INSERT INTO Payment (payment_id, booking_id, amount, payment_method, date, time, status)
VALUES 
('P001', 'B001', 1250.00, 'bank transfer', '2024-10-05', '10:30', 'full-paid'),
('P002', 'B002', 2800.00, 'bank transfer', '2024-10-06', '12:00', 'half-paid'),
('P003', 'B003', 510.00, 'bank transfer', '2024-10-06', '09:45', 'half-paid'),
('P004', 'B002', 2800.00, 'cash', '2024-10-07', '08:15', 'finished');

INSERT INTO Vehicle (vehicle_id, name, type, fuel_to_km, status) 
VALUES 
('V001', 'Suzuki Wagon R', 'Hybrid', 24.3, 'available'),
('V002', 'Suzuki Wagon R', 'Hybrid', 24.3, 'available'),
('V003', 'Suzuki Wagon R', 'Hybrid', 24.3, 'in use'),
('V004', 'Suzuki Wagon R', 'Hybrid', 24.3, 'available'),
('V005', 'Suzuki Wagon R', 'Hybrid', 24.3, 'available');

INSERT INTO Booking_Details (booking_date,booking_time,booking_id, vehicle_id)
VALUES
('2024-10-05' , '10:30' , 'B001', 'V001'),
('2024-10-06' , '12:00' , 'B002', 'V002'),
('2024-10-06' , '09:45' , 'B003', 'V003');



INSERT INTO Driver (driver_id, name, contact, gmail, vehicle_id) 
VALUES
('D001', 'John Doe', '0711234567', 'john.doe@example.com', 'V001'),
('D002', 'Jane Smith', '0712345678', 'jane.smith@example.com', 'V002'),
('D003', 'Michael Johnson', '0723456789', 'michael.johnson@example.com', 'V003'),
('D004', 'Alice Williams', '0734567890', 'alice.williams@example.com', 'V004'),
('D005', 'Robert Brown', '0745678901', 'robert.brown@example.com', 'V005');


SELECT * FROM Employee;
SELECT * FROM User;
SELECT * FROM Customer;
SELECT * FROM Booking;
SELECT * FROM Booking_Details;
SELECT * FROM Payment;
SELECT * FROM Vehicle;
SELECT * FROM Driver;

SELECT * FROM Booking_Details WHERE booking_date = '2024/10/05';


