-- ---------- usuario administrador inicial ----------
INSERT INTO admin_user (username, password_hash)
VALUES (
           'admin',
           '$2a$10$eB6X7L9jfKx9/5nY9X0RLe1lYQgFbw/9bHZ1QXlQtZzPz8V6F6vG6' -- admin123
       );

-- Rol (si tu tabla se llama admin_user_role)
INSERT INTO admin_user_role (admin_user_id, role)
VALUES (
           (SELECT id FROM admin_user WHERE username='admin'),
           'ROLE_ADMIN'
       );
