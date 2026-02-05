package dao;

import database.SQLiteConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDAO {
    
    // In AssignmentDAO.java
    public boolean assignToEvaluator(int sessionId, int submissionId, int evaluatorId) {
        String sql = "INSERT INTO session_assignments (session_id, submission_id, evaluator_id) VALUES (?, ?, ?)";
        try (Connection conn = SQLiteConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sessionId);
            pstmt.setInt(2, submissionId);
            pstmt.setInt(3, evaluatorId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getAllEvaluatorNames() {
        List<String> evaluators = new ArrayList<>();
        // Select both ID and Username
        String sql = "SELECT id, username FROM users WHERE roles = 'evaluator'";
        try (Connection conn = SQLiteConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // Combine them into one string for the dropdown
                evaluators.add(rs.getInt("id") + " - " + rs.getString("username"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return evaluators;
    }

    // Converts the dropdown string back to a numeric ID
    public int getEvaluatorIdByName(String username) {
        // We check username and ensure the role is 'evaluator' to prevent logic errors
        String sql = "SELECT id FROM users WHERE username = ? AND roles = 'evaluator'";
        try (Connection conn = SQLiteConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id"); // Returns the ID (e.g., 3 for 'evaluator')
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if not found
    }
}