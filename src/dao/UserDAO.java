package dao;

import database.SQLiteConnection;
import model.Coordinator;
import model.Evaluator;
import model.Student;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public boolean checkLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            System.out.println("Checking database for user: " + username);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // User found, now check password
                String dbPass = rs.getString("password");
                System.out.println("User found! DB Password: " + dbPass + " | Entered: " + password);
                
                if (dbPass.equals(password)) {
                    return true;
                } else {
                    System.out.println("Password Mismatch!");
                    return false;
                }
            } else {
                System.out.println("User NOT found in database.");
                return false;
            }
            
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public User getUserData(String username) {
        String sql = "SELECT id, username, password, roles FROM users WHERE username = ?";
        try (Connection conn = SQLiteConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("id"); // Get the actual ID (e.g., 2 for abu)
                String user = rs.getString("username");
                String pass = rs.getString("password");
                String role = rs.getString("roles");

                // Create the correct subclass based on the role
                if (role.equalsIgnoreCase("student")) {
                    return new Student(id, user, pass, role);
                } else if (role.equalsIgnoreCase("coordinator")) {
                    return new Coordinator(id, user, pass, role);
                } else if (role.equalsIgnoreCase("evaluator")) {
                    return new Evaluator(id, user, pass, role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUserRole(String username) {
        String role = "";

        String sql = "SELECT roles FROM users WHERE username = ?";
        
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                role = rs.getString("roles");
            }
        } catch (SQLException e) {
            System.out.println("ROLE ERROR: " + e.getMessage());
        }
        return role;
    }
}