package dao;

import database.SQLiteConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}