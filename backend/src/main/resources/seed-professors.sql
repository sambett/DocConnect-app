-- Clear existing data first (if needed)
DELETE FROM status_history;
DELETE FROM notification;
DELETE FROM favorite;
DELETE FROM announcement;
DELETE FROM professor;
DELETE FROM user WHERE role = 'PROFESSOR';

-- Insert Professor Users
-- Note: For production, use proper password hashing. Using simple hashes here for demonstration
-- Password: docconnect2025

INSERT INTO user (email, full_name, password_hash, role, created_at, updated_at)
VALUES 
('amina.benali@university.edu', 'Amina Ben Ali', '$2a$10$fJ2K6B5LB4Z3DX.1EYJ1QOm4Ck.Sq4rZ6yrz7HFZ2L5NUSI0LVfJu', 'PROFESSOR', NOW(), NOW()),
('karim.hassan@university.edu', 'Karim Hassan', '$2a$10$fJ2K6B5LB4Z3DX.1EYJ1QOm4Ck.Sq4rZ6yrz7HFZ2L5NUSI0LVfJu', 'PROFESSOR', NOW(), NOW()),
('fatima.zahra@university.edu', 'Fatima Zahra', '$2a$10$fJ2K6B5LB4Z3DX.1EYJ1QOm4Ck.Sq4rZ6yrz7HFZ2L5NUSI0LVfJu', 'PROFESSOR', NOW(), NOW());

-- Insert Professor Details
INSERT INTO professor (user_id, department, office_location, working_hours, email_verified, created_at, updated_at)
SELECT id, 'Computer Science', 'Building A, Room 304', 'Mon, Wed, Fri: 10:00-12:00', TRUE, NOW(), NOW()
FROM user WHERE email = 'amina.benali@university.edu';

INSERT INTO professor (user_id, department, office_location, working_hours, email_verified, created_at, updated_at)
SELECT id, 'Mathematics', 'Building B, Room 201', 'Tue, Thu: 14:00-16:00', TRUE, NOW(), NOW()
FROM user WHERE email = 'karim.hassan@university.edu';

INSERT INTO professor (user_id, department, office_location, working_hours, email_verified, created_at, updated_at)
SELECT id, 'Physics', 'Building C, Room 105', 'Mon, Wed: 13:00-15:00, Fri: 09:00-11:00', TRUE, NOW(), NOW()
FROM user WHERE email = 'fatima.zahra@university.edu';

-- Set initial status for each professor
INSERT INTO status_history (professor_id, status, timestamp)
SELECT p.id, 'AVAILABLE', NOW()
FROM professor p
JOIN user u ON p.user_id = u.id
WHERE u.email = 'amina.benali@university.edu';

INSERT INTO status_history (professor_id, status, timestamp)
SELECT p.id, 'BUSY', NOW()
FROM professor p
JOIN user u ON p.user_id = u.id
WHERE u.email = 'karim.hassan@university.edu';

INSERT INTO status_history (professor_id, status, timestamp)
SELECT p.id, 'AWAY', NOW() 
FROM professor p
JOIN user u ON p.user_id = u.id
WHERE u.email = 'fatima.zahra@university.edu';

-- Create sample announcements
INSERT INTO announcement (professor_id, content, posted_at)
SELECT p.id, 'Office hours changed to 11:00-13:00 for next week due to faculty meeting.', NOW()
FROM professor p
JOIN user u ON p.user_id = u.id
WHERE u.email = 'amina.benali@university.edu';

INSERT INTO announcement (professor_id, content, posted_at)
SELECT p.id, 'Final exam review session scheduled for Friday at 15:00 in Lecture Hall B.', NOW()
FROM professor p
JOIN user u ON p.user_id = u.id
WHERE u.email = 'karim.hassan@university.edu';

INSERT INTO announcement (professor_id, content, posted_at)
SELECT p.id, 'Research seminar postponed until next week. See department website for details.', NOW()
FROM professor p
JOIN user u ON p.user_id = u.id
WHERE u.email = 'fatima.zahra@university.edu';