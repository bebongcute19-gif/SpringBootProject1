-- Insert sample ADMIN user
INSERT INTO "Users" (username, password_hash, full_name, email, phone_number, role, is_active, created_at, updated_at)
VALUES ('admin', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy990qm', 'Admin User', 'admin@example.com', '0123456789', 'ADMIN', true, NOW(), NOW());

-- Insert sample STUDENT user
INSERT INTO "Users" (username, password_hash, full_name, email, phone_number, role, is_active, created_at, updated_at)
VALUES ('student01', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy990qm', 'Nguyễn Văn A', 'student01@example.com', '0987654321', 'STUDENT', true, NOW(), NOW());

-- Insert student record linked to the STUDENT user
INSERT INTO students (student_id, student_code, major, class_name, date_of_birth, address, created_at, updated_at)
VALUES ((SELECT id FROM "Users" WHERE username = 'student01'), 'SV001', 'Công Nghệ Thông Tin', 'CNTT - K20', '2003-05-15', '123 Nguyễn Huệ, Hà Nội', NOW(), NOW());

