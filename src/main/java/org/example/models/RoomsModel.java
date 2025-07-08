package org.example.models;

import org.example.data.Room;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomsModel extends Model {
    public RoomsModel(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * Get all rooms.
     *
     * @return list of all rooms or null if not found
     */
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT id, name FROM Rooms";

        // Menggunakan try-with-resources untuk memastikan koneksi dan statement ditutup otomatis
        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()
        ) {

            // Looping untuk setiap baris data yang ditemukan
            while (rs.next()) {
                // Membuat objek Room dari data di database
                Room room = new Room(
                    rs.getInt("id"),
                    rs.getString("name")
                );

                // Menambahkan objek ke dalam list
                rooms.add(room);
            }
        } catch (SQLException e) {
            // Jika terjadi error koneksi atau query, cetak error dan kembalikan null
            System.err.println("SQL Error in getAllRooms: " + e.getMessage());
            return null;
        }

        // Jika tidak ada data, akan mengembalikan list kosong, yang juga valid.
        return rooms;
    }
}