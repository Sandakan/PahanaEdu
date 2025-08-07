-- Pahana Edu Billing System - Seed Data
-- Initial data insertions for the system

USE pahana_edu;

-- Insert default categories
INSERT INTO categories (name, description)
VALUES ('Books', 'Educational and general books'),
       ('Stationery', 'Office and school stationery items'),
       ('Electronics', 'Educational electronic items'),
       ('Software', 'Educational software and licenses'),
       ('Accessories', 'Book accessories and supplies');

-- Insert default admin user
-- Password: admin123 (plain text for debugging)
INSERT INTO users (email, password, first_name, last_name, role)
VALUES ('admin@pahanaedu.com', 'admin123', 'System',
        'Administrator', 'ADMIN');

-- Insert sample cashier user
-- Password: cashier123 (plain text for debugging)
INSERT INTO users (email, password, first_name, last_name, role)
VALUES ('cashier@pahanaedu.com', 'cashier123', 'John', 'Doe',
        'CASHIER');

-- Insert sample customers
INSERT INTO customers (name, address, telephone, email)
VALUES ('Colombo International School', '123 Galle Road, Colombo 03', '+94-11-2345678', 'admin@cis.lk'),
       ('Royal College Colombo', '456 Reid Avenue, Colombo 07', '+94-11-3456789', 'office@royal.edu.lk'),
       ('St. Thomas College', '789 Mount Lavinia, Colombo 06', '+94-11-4567890', 'admin@stc.edu.lk'),
       ('Vishaka Vidyalaya', '321 Bambalapitiya, Colombo 04', '+94-11-5678901', 'office@vishaka.edu.lk');

-- Insert sample items
INSERT INTO items (name, description, category_id, unit_price)
VALUES
-- Books (category_id = 1)
('Mathematics Grade 10', 'Advanced Level Mathematics textbook', 1, 850.00),
('English Literature Grade 11', 'O/L English Literature guide', 1, 750.00),
('Science Practical Manual', 'Laboratory practical guide', 1, 650.00),
('History of Sri Lanka', 'Comprehensive history textbook', 1, 900.00),

-- Stationery (category_id = 2)
('A4 Exercise Books', 'Ruled exercise books pack of 10', 2, 250.00),
('Ball Point Pens', 'Blue ink pens pack of 12', 2, 150.00),
('Pencil Set', 'HB pencils pack of 10', 2, 120.00),
('Geometry Set', 'Complete geometry instruments', 2, 450.00),

-- Electronics (category_id = 3)
('Scientific Calculator', 'Casio FX-82MS scientific calculator', 3, 2500.00),
('USB Flash Drive 16GB', 'Data storage device', 3, 1200.00),

-- Software (category_id = 4)
('Educational Software License', 'Annual software license', 4, 5000.00),

-- Accessories (category_id = 5)
('Book Covers', 'Plastic book covers pack of 20', 5, 200.00),
('Book Labels', 'Self-adhesive labels pack of 50', 5, 180.00);


-- Insert sample bills
INSERT INTO bills (customer_id, user_id, total_amount, payment_status, payment_method, notes)
VALUES (1, 2, 1750.00, 'PAID', 'CASH', 'School supplies purchase'),
       (2, 2, 2950.00, 'PENDING', 'CREDIT_CARD', 'Textbook order for new semester'),
       (3, 2, 1200.00, 'PAID', 'DEBIT_CARD', 'Stationery supplies'),
       (1, 2, 5000.00, 'PENDING', 'CASH', 'Software license renewal');

-- Insert sample bill items
INSERT INTO bill_items (bill_id, item_id, quantity, unit_price, line_total)
VALUES
-- Bill 1: School supplies (Rs. 1750.00)
(1, 1, 2, 850.00, 1700.00), -- Mathematics Grade 10 x2
(1, 6, 1, 150.00, 150.00),  -- Ball Point Pens x1

-- Bill 2: Textbook order (Rs. 2950.00)
(2, 2, 2, 750.00, 1500.00), -- English Literature Grade 11 x2
(2, 3, 1, 650.00, 650.00),  -- Science Practical Manual x1
(3, 4, 1, 900.00, 900.00),  -- History of Sri Lanka x1

-- Bill 3: Stationery supplies (Rs. 1200.00)
(3, 5, 2, 250.00, 500.00),  -- A4 Exercise Books x2
(3, 7, 2, 120.00, 240.00),  -- Pencil Set x2
(3, 8, 1, 450.00, 450.00),  -- Geometry Set x1

-- Bill 4: Software license (Rs. 5000.00)
(4, 11, 1, 5000.00, 5000.00); -- Educational Software License x1
