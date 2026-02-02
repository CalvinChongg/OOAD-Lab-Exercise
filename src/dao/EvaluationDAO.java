package dao;

import database.SQLiteConnection;
import java.sql.*;

public class EvaluationDAO {
    public boolean submitEvaluation(int subId, int evalId, int c, int m, int r, int q, String com) {
        double score = (c + m + r + q) / 4.0;
        String sql = "INSERT INTO evaluations (submission_id, evaluator_id, problem_clarity, methodology, results, presentation_quality, overall_score, comments) VALUES (?,?,?,?,?,?,?,?)";
        
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, subId);
            pstmt.setInt(2, evalId);
            pstmt.setInt(3, c);
            pstmt.setInt(4, m);
            pstmt.setInt(5, r);
            pstmt.setInt(6, q);
            pstmt.setDouble(7, score);
            pstmt.setString(8, com);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}