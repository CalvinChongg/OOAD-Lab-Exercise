package dao;

import database.SQLiteConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    
    // Calculates average scores for the leaderboard table
    public List<Object[]> getLeaderboard() {
        List<Object[]> results = new ArrayList<>();
        String sql = """
            SELECT s.id, s.research_title, u.username, s.presentation_type, 
                AVG(e.overall_score) as avg_score
            FROM submissions s
            JOIN evaluations e ON s.id = e.submission_id
            JOIN users u ON s.student_id = u.id
            GROUP BY s.id
            ORDER BY avg_score DESC
        """;
        
        try (Connection conn = SQLiteConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int rank = 1;
            while (rs.next()) {
                results.add(new Object[]{
                    rank++,
                    rs.getString("research_title"),
                    rs.getString("username"),
                    rs.getString("presentation_type"),
                    String.format("%.2f", rs.getDouble("avg_score"))
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // Finds the top winner for a specific category (Oral/Poster)
    public String getWinner(String type) {
        String sql = """
            SELECT u.username, AVG(e.overall_score) as avg_score
            FROM submissions s
            JOIN evaluations e ON s.id = e.submission_id
            JOIN users u ON s.student_id = u.id
            WHERE s.presentation_type = ?
            GROUP BY s.id
            ORDER BY avg_score DESC LIMIT 1
        """;
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username") + " (" + String.format("%.1f", rs.getDouble("avg_score")) + ")";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "TBD";
    }

    public boolean finalizeAwards() {
        String insertSql = "INSERT INTO awards (award_type, submission_id, winner_id, ceremony_date) VALUES (?, ?, ?, CURRENT_DATE)";
        
        try (Connection conn = SQLiteConnection.connect()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                // 1. Finalize Best Oral
                Object[] oral = getTopWinnerByCategory("ORAL");
                if (oral != null) {
                    pstmt.setString(1, "BEST_ORAL"); // Changed from "Best Oral"
                    pstmt.setInt(2, (int) oral[0]);
                    pstmt.setInt(3, (int) oral[1]);
                    pstmt.addBatch();
                }

                // 2. Finalize Best Poster
                Object[] poster = getTopWinnerByCategory("POSTER");
                if (poster != null) {
                    pstmt.setString(1, "BEST_POSTER"); // Changed from "Best Poster"
                    pstmt.setInt(2, (int) poster[0]);
                    pstmt.setInt(3, (int) poster[1]);
                    pstmt.addBatch();
                }

                pstmt.executeBatch();
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper to get raw IDs for the winners
    private Object[] getTopWinnerByCategory(String type) {
        String sql = "SELECT s.id, s.student_id FROM submissions s " +
                    "JOIN evaluations e ON s.id = e.submission_id " +
                    "WHERE s.presentation_type = ? " +
                    "ORDER BY e.overall_score DESC LIMIT 1";
        try (Connection conn = SQLiteConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Object[]{rs.getInt("id"), rs.getInt("student_id")};
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}