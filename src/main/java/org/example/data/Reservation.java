// File: PBO_FIX/siperu/src/main/java/org/example/data/Reservation.java

package org.example.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Arrays; // <-- Tambahkan import ini

public record Reservation(
        Integer id,
        Room room,
        User user,
        String purpose,
        Date startTime,
        Date endTime,
        Date submissionTime,
        ConfirmationStatus confirmationStatus
) {
    /**
     * Map a ResultSet to a Reservation object.
     *
     * @return a Reservation object
     */
    public static Reservation generateFromResultSet(ResultSet rs) throws SQLException {
        Room room = new Room(
                rs.getInt("room_id"),
                rs.getString("room_name")
        );

        User user = new User(
                rs.getInt("loaner_id"),
                rs.getString("user_name"),
                rs.getString("email"),
                Roles.values()[rs.getInt("role_id")]
        );


        ConfirmationStatus confirmationStatus;
        int confirmationStatusCode = rs.getInt("confirmation");

        if (rs.wasNull()) {
            confirmationStatus = ConfirmationStatus.PENDING;
        } else {
            // --- BAGIAN YANG DIPERBAIKI ---
            // Mencari status berdasarkan getStatusCode(), bukan berdasarkan indeks array.
            confirmationStatus = Arrays.stream(ConfirmationStatus.values())
                    .filter(status -> status.getStatusCode() != null && status.getStatusCode() == confirmationStatusCode)
                    .findFirst()
                    .orElse(ConfirmationStatus.PENDING); // Default jika tidak ditemukan
        }

        return new Reservation(
                rs.getInt("id"),
                room,
                user,
                rs.getString("purpose"),
                rs.getTimestamp("use_start"),
                rs.getTimestamp("use_end"),
                rs.getTimestamp("submission_time"),
                confirmationStatus
        );
    }
}