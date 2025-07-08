CREATE TABLE Roles
(
    id   INT          NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

INSERT INTO Roles (id, name)
VALUES (0, 'Peminjam'),
       (1, 'Staf Administrasi'),
       (2, 'Staf Kebersihan');