package dao;

import database.SQLiteConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionDAO {
    // For the "Create Session" button
    public boolean createSession(String name, String date, String time, String type, String venue) {
        String sql = "INSERT INTO sessions (name, date, time, type, venue, status) VALUES (?, ?, ?, ?, ?, 'Scheduled')";
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, date);
            pstmt.setString(3, time);
            pstmt.setString(4, type);
            pstmt.setString(5, venue);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // For refreshing the Session Management Table
    public List<Object[]> getAllSessions() {
        List<Object[]> sessions = new ArrayList<>();
        // Select the exact columns defined in the table above
        String sql = "SELECT id, name, date, time, type, venue, status FROM sessions";
        try (Connection conn = SQLiteConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sessions.add(new Object[]{
                    "SES-" + String.format("%03d", rs.getInt("id")),
                    rs.getString("name"),
                    rs.getString("date"),
                    rs.getString("time"),
                    rs.getString("type"),
                    rs.getString("venue"),
                    rs.getString("status"),
                    "Edit"
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }

    public boolean updateSession(int id, String name, String date, String time, String venue) {
        String sql = "UPDATE sessions SET name = ?, date = ?, time = ?, venue = ? WHERE id = ?";
        try (Connection conn = SQLiteConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, date);
            pstmt.setString(3, time);
            pstmt.setString(4, venue);
            pstmt.setInt(5, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSession(int sessionId) {
        // 1. First, remove any assignments linked to this session to prevent foreign key errors
        String deleteAssignmentsSql = "DELETE FROM session_assignments WHERE session_id = ?";
        String deleteSessionSql = "DELETE FROM sessions WHERE id = ?";
        
        try (Connection conn = SQLiteConnection.connect()) {
            conn.setAutoCommit(false); // Use a transaction
            
            try (PreparedStatement pstmt1 = conn.prepareStatement(deleteAssignmentsSql);
                PreparedStatement pstmt2 = conn.prepareStatement(deleteSessionSql)) {
                
                pstmt1.setInt(1, sessionId);
                pstmt1.executeUpdate();
                
                pstmt2.setInt(1, sessionId);
                int affectedRows = pstmt2.executeUpdate();
                
                conn.commit();
                return affectedRows > 0;
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
}