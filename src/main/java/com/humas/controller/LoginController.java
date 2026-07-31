package com.humas.controller;

import com.humas.config.Database;
import com.humas.model.User;
import com.humas.util.UserSession;
import com.humas.view.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    public String handleLogin(String username, String password, Stage stage) {
        if (username.isEmpty() || password.isEmpty()) {
            return "Username dan Password wajib diisi!";
        }

        User user = authenticate(username, password);

        if (user != null) {
            // 1. Simpan session lokal
            UserSession.setSession(user);

            // 2. Navigasi berdasarkan Role (sesuai ENUM database)
            switch (user.getRole().toLowerCase()) {
                case "admin":
                    new AdminDashboardView().start(stage);
                    break;
                case "kabag":
                    new KabagDashboardView().start(stage);
                    break;
                case "staff":
                    new StaffDashboardView().start(stage);
                    break;
                case "intern":
                    new InternDashboardView().start(stage);
                    break;
                default:
                    return "Role user tidak valid!";
            }
            return null; // Login berhasil
        } else {
            return "Username/password salah atau akun nonaktif!";
        }
    }

    private User authenticate(String username, String plainPassword) {
    String sql = "SELECT id_user, nama, username, password, role FROM users WHERE username = ? AND status_aktif = 1";

    try (Connection conn = Database.getConnection()) {
        if (conn == null) {
            System.err.println("[ERROR] Koneksi database NULL!");
            return null;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPasswordFromDB = rs.getString("password");
                String roleFromDB = rs.getString("role");

                System.out.println("[DEBUG] User ditemukan di DB: " + rs.getString("username"));
                System.out.println("[DEBUG] Role di DB: " + roleFromDB);

                // Verifikasi BCrypt
                if (BCrypt.checkpw(plainPassword, hashedPasswordFromDB)) {
                    System.out.println("[DEBUG] Password cocok!");
                    return new User(
                        rs.getInt("id_user"),
                        rs.getString("nama"),
                        rs.getString("username"),
                        roleFromDB
                    );
                } else {
                    System.err.println("[ERROR] Password TIDAK cocok dengan Hash BCrypt di DB!");
                }
            } else {
                System.err.println("[ERROR] Username '" + username + "' tidak ditemukan atau status_aktif != 1");
            }
        }
    } catch (Exception e) {
        System.err.println("[ERROR] Terjadi exception SQL / Database:");
        e.printStackTrace();
    }
    return null;
}
}