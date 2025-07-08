// File: PBO_FIX/siperu/src/main/java/org/example/models/ReservationsModel.java

package org.example.models;

import org.example.data.ConfirmationStatus;
import org.example.data.Reservation;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationsModel extends Model {
    public ReservationsModel(DataSource dataSource) {
        super(dataSource);
    }

    // ... (metode-metode yang sudah ada seperti getAllReservationsByStartAndEndTime, dll.)

    // --- TAMBAHKAN METODE BARU DI SINI ---
    /**
     * Mengambil semua reservasi yang telah disetujui dan waktu penggunaannya sudah selesai.
     * @return Daftar reservasi yang perlu dibersihkan.
     */
    public List<Reservation> getAllCompletedReservationsForCleaning() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.id, r.purpose, r.use_start, r.use_end, r.submission_time, r.confirmation, " +
                "ro.id AS room_id, ro.name AS room_name, " +
                "u.id AS loaner_id, u.name AS user_name, u.email, u.role_id " +
                "FROM Reservations r " +
                "JOIN Rooms ro ON r.room_id = ro.id " +
                "JOIN Users u ON r.loaner_id = u.id " +
                "WHERE r.confirmation = 1 AND r.use_end < NOW()"; // confirmation = 1 (disetujui) dan use_end sudah lewat

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                reservations.add(Reservation.generateFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting completed reservations for cleaning: " + e.getMessage());
            e.printStackTrace();
        }
        return reservations;
    }

    /**
     * Mengambil semua reservasi dalam sistem (untuk admin).
     * @return Daftar semua reservasi.
     */
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.id, r.purpose, r.use_start, r.use_end, r.submission_time, r.confirmation, " +
                "ro.id AS room_id, ro.name AS room_name, " +
                "u.id AS loaner_id, u.name AS user_name, u.email, u.role_id " +
                "FROM Reservations r " +
                "JOIN Rooms ro ON r.room_id = ro.id " +
                "JOIN Users u ON r.loaner_id = u.id " +
                "ORDER BY r.submission_time DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                reservations.add(Reservation.generateFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all reservations: " + e.getMessage());
            e.printStackTrace();
        }
        return reservations;
    }

    /**
     * Mengambil reservasi yang sudah selesai dibersihkan (status = 2).
     * @return Daftar reservasi yang sudah dibersihkan.
     */
    public List<Reservation> getCleanedReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.id, r.purpose, r.use_start, r.use_end, r.submission_time, r.confirmation, " +
                "ro.id AS room_id, ro.name AS room_name, " +
                "u.id AS loaner_id, u.name AS user_name, u.email, u.role_id " +
                "FROM Reservations r " +
                "JOIN Rooms ro ON r.room_id = ro.id " +
                "JOIN Users u ON r.loaner_id = u.id " +
                "WHERE r.confirmation = 2 " + // confirmation = 2 (sudah dibersihkan)
                "ORDER BY r.use_end DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                reservations.add(Reservation.generateFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting cleaned reservations: " + e.getMessage());
            e.printStackTrace();
        }
        return reservations;
    }

    /**
     * Mengambil statistik reservasi untuk admin.
     * @return Array dengan [total, pending, approved, rejected, cleaned]
     */
    public int[] getReservationStatistics() {
        int[] stats = new int[5]; // [total, pending, approved, rejected, cleaned]

        String query = "SELECT " +
                "COUNT(*) as total, " +
                "SUM(CASE WHEN confirmation IS NULL THEN 1 ELSE 0 END) as pending, " +
                "SUM(CASE WHEN confirmation = 1 THEN 1 ELSE 0 END) as approved, " +
                "SUM(CASE WHEN confirmation = 0 THEN 1 ELSE 0 END) as rejected, " +
                "SUM(CASE WHEN confirmation = 2 THEN 1 ELSE 0 END) as cleaned " +
                "FROM Reservations";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                stats[0] = rs.getInt("total");
                stats[1] = rs.getInt("pending");
                stats[2] = rs.getInt("approved");
                stats[3] = rs.getInt("rejected");
                stats[4] = rs.getInt("cleaned");
            }
        } catch (SQLException e) {
            System.err.println("Error getting reservation statistics: " + e.getMessage());
            e.printStackTrace();
        }
        return stats;
    }

    /**
     * Mengambil statistik penggunaan ruangan.
     * @return List dengan data [room_name, usage_count]
     */
    public List<String[]> getRoomUsageStatistics() {
        List<String[]> stats = new ArrayList<>();
        String query = "SELECT ro.name, COUNT(r.id) as usage_count " +
                "FROM Rooms ro " +
                "LEFT JOIN Reservations r ON ro.id = r.room_id AND r.confirmation = 1 " +
                "GROUP BY ro.id, ro.name " +
                "ORDER BY usage_count DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                stats.add(new String[]{
                        rs.getString("name"),
                        String.valueOf(rs.getInt("usage_count"))
                });
            }
        } catch (SQLException e) {
            System.err.println("Error getting room usage statistics: " + e.getMessage());
            e.printStackTrace();
        }
        return stats;
    }

    // ... (metode-metode yang sudah ada sebelumnya)

    public List<Reservation> getAllReservationsByStartAndEndTime(@NotNull Date startTime, @NotNull Date endTime) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = """
                SELECT r.id, r.room_id, r.loaner_id, r.purpose, r.use_start, r.use_end,
                       r.submission_time, r.confirmation,
                       rm.name as room_name,
                       u.name as user_name, u.email, u.role_id
                FROM Reservations r
                JOIN Rooms rm ON r.room_id = rm.id
                JOIN Users u ON r.loaner_id = u.id
                WHERE (r.confirmation IS NULL OR r.confirmation = 1)
                AND (
                    (r.use_start < ? AND r.use_end > ?) OR
                    (r.use_start >= ? AND r.use_start < ?)
                )
            """;

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            Timestamp startTimestamp = new Timestamp(startTime.getTime());
            Timestamp endTimestamp = new Timestamp(endTime.getTime());

            stmt.setTimestamp(1, endTimestamp);
            stmt.setTimestamp(2, startTimestamp);
            stmt.setTimestamp(3, startTimestamp);
            stmt.setTimestamp(4, endTimestamp);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            System.out.println("Executing query: getAllReservationsByStartAndEndTime");
            System.out.println("Time range: " + sdf.format(startTime) + " to " + sdf.format(endTime));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(Reservation.generateFromResultSet(rs));
                }
            }
            System.out.println("Found " + reservations.size() + " overlapping reservations");
        } catch (SQLException e) {
            System.err.println("Error getting pending and accepted reservations: " + e.getMessage());
            e.printStackTrace();
        }

        return reservations;
    }

    public boolean addReservation(@NotNull Reservation reservation) {
        String query = "INSERT INTO Reservations (room_id, purpose, loaner_id, use_start, use_end) " + "VALUES (?, ?, ?, ?, ?)";

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setInt(1, reservation.room().id());
            stmt.setString(2, reservation.purpose());
            stmt.setInt(3, reservation.user().id());
            stmt.setTimestamp(4, new Timestamp(reservation.startTime().getTime()));
            stmt.setTimestamp(5, new Timestamp(reservation.endTime().getTime()));

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Reservation> getReservationsByUserId(int userId) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.id, r.purpose, r.use_start, r.use_end, r.submission_time, r.confirmation, " + "ro.id AS room_id, ro.name AS room_name, " + "u.id AS loaner_id, u.name AS user_name, u.email, u.role_id " + "FROM Reservations r " + "JOIN Rooms ro ON r.room_id = ro.id " + "JOIN Users u ON r.loaner_id = u.id " + "WHERE r.loaner_id = ? " + "ORDER BY r.submission_time DESC";

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)
        ) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reservations.add(Reservation.generateFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public List<Reservation> getAllPendingReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = """
            SELECT r.id, r.room_id, r.loaner_id, r.purpose, r.use_start, r.use_end,
                   r.submission_time, r.confirmation,
                   rm.name as room_name,
                   u.name as user_name, u.email, u.role_id
            FROM Reservations r
            JOIN Rooms rm ON r.room_id = rm.id
            JOIN Users u ON r.loaner_id = u.id
            WHERE r.confirmation IS NULL
            ORDER BY r.submission_time ASC
            """;

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            System.out.println("Executing query: getAllPendingReservations");
            while (rs.next()) {
                reservations.add(Reservation.generateFromResultSet(rs));
            }
            System.out.println("Found " + reservations.size() + " pending reservations");
        } catch (SQLException e) {
            System.err.println("Error getting all pending reservations: " + e.getMessage());
            e.printStackTrace();
        }

        return reservations;
    }

    public boolean confirmReservation(int reservationId, @NotNull ConfirmationStatus confirmationStatus) {
        String sql = "UPDATE Reservations SET confirmation = ? WHERE id = ?";

        Integer confirmationValue = confirmationStatus.getStatusCode();

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            if (confirmationValue == null) {
                stmt.setNull(1, Types.INTEGER);
            } else {
                stmt.setInt(1, confirmationValue);
            }
            stmt.setInt(2, reservationId);

            System.out.println("Executing update: confirmReservation for ID " + reservationId + " to status " + confirmationStatus.getStatusDescription());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✓ Successfully updated reservation ID " + reservationId);
                return true;
            } else {
                System.out.println("✗ No reservation found with ID: " + reservationId);
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error confirming reservation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Reservation> getAllAcceptedAndNotStartedReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.id, r.purpose, r.use_start, r.use_end, r.submission_time, r.confirmation, " + "ro.id AS room_id, ro.name AS room_name, " + "u.id AS loaner_id, u.name AS user_name, u.email, u.role_id " + "FROM Reservations r " + "JOIN Rooms ro ON r.room_id = ro.id " + "JOIN Users u ON r.loaner_id = u.id " + "WHERE r.confirmation = 1 AND r.use_start >= NOW()";

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                reservations.add(Reservation.generateFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
}
