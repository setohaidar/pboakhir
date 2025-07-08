INSERT INTO Users (name, email, password, role_id)
VALUES ('John Doe', 'john.doe@example.com', 'password123', 0),
       ('Jane Smith', 'jane.smith@example.com', 'password456', 1),
       ('Alice Johnson', 'alice.johnson@example.com', 'password789', 2);

INSERT INTO Rooms (id, name)
VALUES (251, 'Room 251'),
       (252, 'Room 252'),
       (253, 'Room 253'),
       (254, 'Room 254'),
       (255, 'Room 255'),
       (256, 'Room 256'),
       (257, 'Room 257');

INSERT INTO Reservations (room_id, purpose, loaner_id, use_start, use_end, confirmation)
VALUES (251, 'Meeting', 1, '2025-08-25 09:00:00', '2025-08-25 11:00:00', NULL),
       (252, 'Training', 2, '2025-08-26 14:00:00', '2025-08-26 17:00:00', NULL),
       (253, 'Workshop', 3, '2025-08-27 10:00:00', '2025-08-27 12:00:00', 1),
       (254, 'Meeting', 2, '2025-08-28 08:00:00', '2025-08-28 16:00:00', 0),
       (255, 'Meeting', 1, '2025-08-29 09:00:00', '2025-08-29 12:00:00', 2);