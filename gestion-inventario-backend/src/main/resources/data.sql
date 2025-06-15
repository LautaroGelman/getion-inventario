-- ---------- Usuario Administrador Inicial ----------
-- Inserta el usuario 'admin' con la contraseña 'admin123' encriptada.
INSERT INTO admin_users (username, password)
VALUES (
           'admin',
           '$2a$10$eB6X7L9jfKx9/5nY9X0RLe1lYQgFbw/9bHZ1QXlQtZzPz8V6F6vG6'
       );