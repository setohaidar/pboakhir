package org.example.models;

import org.example.data.Roles;
import org.example.data.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersModel extends Model {
    public UsersModel(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * Get user data by credentials.
     *
     * @param email    the user's email
     * @param password the user's password
     * @return the user's data or null if not found or credentials are incorrect
     */
    public User getUserByCredentials(String email, String password) {
        // Query untuk mencari user berdasarkan email dan password.
        // Penting: Password di sini dibandingkan secara langsung (plain text).
        String sql = "SELECT id, name, email, role_id FROM Users WHERE email = ? AND password = ?";

        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {

            // Mengatur parameter query untuk mencegah SQL Injection
            pstmt.setString(
                1,
                email
            );
            pstmt.setString(
                2,
                password
            );

            try (ResultSet rs = pstmt.executeQuery()) {
                // Periksa apakah ada baris data yang ditemukan
                if (rs.next()) {
                    // Jika ditemukan, buat objek User
                    int roleId = rs.getInt("role_id");

                    // Konversi role_id (integer) dari database ke enum Roles
                    Roles userRole = switch (roleId) {
                        case 0 -> Roles.LOANER;
                        case 1 -> Roles.ADMIN_STAFF;
                        case 2 -> Roles.CLEANING_STAFF;
                        default -> throw new IllegalStateException("Invalid role_id: " + roleId);
                    };

                    return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        userRole
                    );
                } else {
                    // Jika tidak ada data yang cocok, kembalikan null sesuai Javadoc
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getUserByCredentials: " + e.getMessage());
            // Jika terjadi error, kembalikan null
            return null;
        }
    }
}