DELIMITER $$

CREATE EVENT update_confirmation
    ON SCHEDULE EVERY 1 DAY
        STARTS '2025-06-25 00:00:00'
    DO
    BEGIN
        UPDATE Reservations
        SET confirmation = 2
        WHERE confirmation IS NULL
          AND DATEDIFF(use_start, CURDATE()) < 3;
    END$$

DELIMITER ;