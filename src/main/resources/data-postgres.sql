-- ==============================
-- LIMPIAR DATOS EXISTENTES
-- ==============================
TRUNCATE TABLE user_statistics CASCADE;
TRUNCATE TABLE trainer_statistics CASCADE;
TRUNCATE TABLE users CASCADE;
TRUNCATE TABLE students CASCADE;
TRUNCATE TABLE employees RESTART IDENTITY CASCADE;
TRUNCATE TABLE campuses CASCADE;

-- ==============================
-- CAMPUS
-- ==============================
INSERT INTO campuses (code, name, city_code)
VALUES
    (2, 'Icesi Principal', 76001),
    (3, 'Icesi Norte', 76002);

-- ==============================
-- EMPLOYEES (Entrenadores / Admins)
-- ==============================
INSERT INTO employees (first_name, last_name, email, contract_type, employee_type, faculty_code, campus_code, birth_place_code)
VALUES
    ('Juan', 'Pérez', 'juan.perez@icesi.edu.co', 'Full-Time', 'trainer', 101, 2, 76001),
    ('María', 'Rodríguez', 'maria.rod@icesi.edu.co', 'Part-Time', 'admin', 102, 3, 76002);

-- ==============================
-- STUDENTS (Usuarios normales)
-- ==============================
INSERT INTO students (id, first_name, last_name, email, birth_date, birth_place_code, campus_code)
VALUES
    ('A001', 'Carlos', 'Mendoza', 'carlos.men@icesi.edu.co', '2002-04-14', 76001, 2),
    ('A002', 'Laura', 'García', 'laura.gar@icesi.edu.co', '2001-11-02', 76002, 3);

-- ==============================
-- USERS (Login)
-- Contraseña para TODOS: password123
-- ==============================
INSERT INTO users (username, password_hash, role, student_id, employee_id, is_active, created_at)
VALUES
    ('trainer_juan', '$2a$10$N9qo8uLOickgx2ZMRZoMye4IjHnLJ7JZP/k6gZ2sMQnFy3hY2V7R6', 'trainer', NULL, 1, TRUE, NOW()),
    ('admin_maria', '$2a$10$N9qo8uLOickgx2ZMRZoMye4IjHnLJ7JZP/k6gZ2sMQnFy3hY2V7R6', 'admin', NULL, 2, TRUE, NOW()),
    ('user_carlos', '$2a$10$N9qo8uLOickgx2ZMRZoMye4IjHnLJ7JZP/k6gZ2sMQnFy3hY2V7R6', 'user', 'A001', NULL, TRUE, NOW()),
    ('user_laura', '$2a$10$N9qo8uLOickgx2ZMRZoMye4IjHnLJ7JZP/k6gZ2sMQnFy3hY2V7R6', 'user', 'A002', NULL, TRUE, NOW());

-- ==============================
-- TRAINER STATISTICS
-- ==============================
INSERT INTO trainer_statistics (trainer_id, year, month, new_assignments_count, recommendations_count, created_at, updated_at)
VALUES
    ('trainer_juan', 2025, 11, 5, 3, NOW(), NOW());

-- ==============================
-- USER STATISTICS
-- ==============================
INSERT INTO user_statistics (user_id, year, month, routines_started, progress_logs_count, created_at, updated_at)
VALUES
    ('user_carlos', 2025, 11, 4, 12, NOW(), NOW()),
    ('user_laura', 2025, 11, 3, 7, NOW(), NOW());