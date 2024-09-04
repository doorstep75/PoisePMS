-- NEXT I CREATED THIS FILE (also in VS Code) to add data to each of the tables
-- I then used the console to add this data

-- use the poise database
USE PoisePMS;


-- Insert data into Architect table
INSERT INTO Architect (
    id,
    first_name,
    last_name,
    phone_number,
    email,
    address,
    post_code
) VALUES
(1, 'Brenda', 'Bosh', '0191 546 3164', 'bb@bb.com', '65 Bish Avenue', 'M5 6AH'),
(2, 'Harry', 'Mole', '07766 959626', 'harry@hm.co.uk', '22 Honest House', 'B7 8HH');

-- Insert data into Contractor table
INSERT INTO Contractor (
    id,
    first_name,
    last_name,
    phone_number,
    email,
    address,
    post_code
) VALUES
(1, 'Henrietta', 'Whacked', '0888 956323', 'henw@whacked.com', '15 The Plaza', 'E6 3AA'),
(2, 'Terry', 'Tibbs', '01546 941697', 'ttibbs@tibbs.com', '4 Sweet Factory', 'G3 9FA');

-- Insert data into Customer table
INSERT INTO Customer (
    id,
    first_name,
    last_name,
    phone_number,
    email,
    address,
    post_code
) VALUES
(1, 'Constantine', 'Hassler', '0161 896 5213', 'const@hassle.com', '1 Whinge Clock', 'M44 4MW'),
(2, 'Theresa', 'Hay', '0171 563 7894', 'thay@thay.co.uk', '12 Downing Street', 'W1 1BB');

-- Insert data into Projects table
INSERT INTO Projects (
    project_number,
    project_name,
    building_type,
    project_address,
    erf_number,
    total_fee_gbp,
    paid_to_date_gbp,
    deadline_date,
    completion_date,
    finalised,
    architect_id,
    contractor_id,
    customer_id
) VALUES (
    1001,
    'Bob Towers',
    'Flat',
    'Bob Street',
    'E001',
    500000,
    0,
    '2024-07-17',
    '2024-07-17',
    TRUE,
    1,
    2,
    1
);

INSERT INTO Projects (
    project_number,
    project_name,
    building_type,
    project_address,
    erf_number,
    total_fee_gbp,
    paid_to_date_gbp,
    deadline_date,
    completion_date,
    finalised,
    architect_id,
    contractor_id,
    customer_id
) VALUES (
    1002,
    'Stretford Road',
    'House',
    'Terry Lane',
    'E202',
    1000000,
    10000,
    '2024-12-30',
    NULL,
    FALSE,
    2,
    1,
    1
);