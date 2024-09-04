-- To complete the first task I created these SQL instructions in 
-- VS Code and then used the console to run and add them

-- Step 1: Create the Database
CREATE DATABASE PoisePMS;

-- Step 2: Use the Database
USE PoisePMS;

-- Step 3: Create the Customer Table
CREATE TABLE Customer (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(30),
    last_name VARCHAR(40) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(20),
    address VARCHAR(20) NOT NULL,
    post_code VARCHAR(8) NOT NULL
);

-- Step 4: Create the Contractor Table
CREATE TABLE Contractor (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(30),
    last_name VARCHAR(40) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(20),
    address VARCHAR(20) NOT NULL,
    post_code VARCHAR(8) NOT NULL
);

-- Step 5: Create the Architect Table
CREATE TABLE Architect (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(30),
    last_name VARCHAR(40) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(20),
    address VARCHAR(20) NOT NULL,
    post_code VARCHAR(8) NOT NULL
);


-- Step 6: Create the Projects Table
CREATE TABLE Projects (
    project_number INT PRIMARY KEY AUTO_INCREMENT,
    project_name VARCHAR(50),
    building_type VARCHAR(50) NOT NULL,
    project_address VARCHAR(50) NOT NULL,
    erf_number VARCHAR(10) NOT NULL,
    total_fee_gbp DECIMAL(10, 2) DEFAULT 0,
    paid_to_date_gbp DECIMAL(10, 2) DEFAULT 0,
    deadline_date DATE NOT NULL,
    completion_date DATE,
    finalised BOOLEAN NOT NULL, 
    architect_id INT NOT NULL,
    contractor_id INT NOT NULL,
    customer_id INT NOT NULL,
    FOREIGN KEY (architect_id) REFERENCES Architect(id),
    FOREIGN KEY (contractor_id) REFERENCES Contractor(id),
    FOREIGN KEY (customer_id) REFERENCES Customer(id)
) AUTO_INCREMENT=1001;

-- Step 9: Create a Trigger to Set project_name
DELIMITER //

CREATE TRIGGER set_project_name
BEFORE INSERT ON Projects
FOR EACH ROW
BEGIN
    IF NEW.project_name IS NULL OR NEW.project_name = '' THEN
        SET NEW.project_name = CONCAT(
            NEW.building_type, ' ', 
            (SELECT last_name FROM Customer WHERE id = NEW.customer_id)
        );
    END IF;
END//

DELIMITER ;