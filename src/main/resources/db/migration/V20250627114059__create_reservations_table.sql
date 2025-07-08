CREATE TABLE Reservations
(
    id              INT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    room_id         INT       NOT NULL,
    purpose         TEXT      NOT NULL,
    loaner_id       INT       NOT NULL,
    use_start       TIMESTAMP NULL,
    use_end         TIMESTAMP NULL,
    submission_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    confirmation    INT                DEFAULT NULL,
    FOREIGN KEY (room_id) REFERENCES Rooms (id),
    FOREIGN KEY (loaner_id) REFERENCES Users (id)
);

