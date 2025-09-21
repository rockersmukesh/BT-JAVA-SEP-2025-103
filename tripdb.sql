-- Drop database if exists and create a new one
DROP DATABASE IF EXISTS trip_management;
CREATE DATABASE trip_management;
USE trip_management;

-- Create trip_status enum table
CREATE TABLE trip_status (
    id INT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(20) UNIQUE NOT NULL
);

-- Insert status values
INSERT INTO trip_status (status_name) VALUES
    ('PLANNED'),
    ('ONGOING'),
    ('COMPLETED'),
    ('CANCELLED');

-- Create trip table
CREATE TABLE trip (
    id INT PRIMARY KEY AUTO_INCREMENT,
    destination VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    price DOUBLE NOT NULL,
    status_id INT NOT NULL,
    FOREIGN KEY (status_id) REFERENCES trip_status(id),
    CHECK (end_date >= start_date),
    CHECK (price >= 0)
);

-- Insert sample trip data
INSERT INTO trip (destination, start_date, end_date, price, status_id) VALUES
    ('Paris', '2024-06-01', '2024-06-10', 1500.00, 1),
    ('London', '2024-07-15', '2024-07-25', 2000.00, 1),
    ('New York', '2024-05-01', '2024-05-10', 2500.00, 3),
    ('Tokyo', '2024-08-10', '2024-08-20', 3000.00, 1),
    ('Rome', '2024-04-01', '2024-04-10', 1800.00, 3),
    ('Barcelona', '2024-09-05', '2024-09-15', 1700.00, 1),
    ('Sydney', '2024-11-20', '2024-12-05', 4000.00, 1),
    ('Berlin', '2024-03-15', '2024-03-25', 1600.00, 3),
    ('Amsterdam', '2024-10-10', '2024-10-20', 1900.00, 1),
    ('Dubai', '2024-12-15', '2024-12-25', 2800.00, 1);

-- Create view for trip summary
CREATE VIEW trip_summary AS
SELECT
    COUNT(*) as total_trips,
    MIN(price) as min_price,
    MAX(price) as max_price,
    AVG(price) as avg_price
FROM trip;