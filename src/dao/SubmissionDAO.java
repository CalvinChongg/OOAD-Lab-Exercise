package dao;

import database.SQLiteConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubmissionDAO {
    
    public boolean addSubmission(int studentId, String title, String abstractText, 
                               String supervisor, String type, String filePath) {
        String sql = """
            INSERT INTO submissions 
            (student_id, research_title, abstract, supervisor_name, presentation_type, file_path, status) 
            VALUES (?, ?, ?, ?, ?, ?, 'PENDING')
            """;
        
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setString(2, title);
            pstmt.setString(3, abstractText);
            pstmt.setString(4, supervisor);
            pstmt.setString(5, type);
            pstmt.setString(6, filePath);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Submission error: " + e.getMessage());
            return false;
        }
    }
    
    public List<Object[]> getSubmissionsByStudent(int studentId) {
        List<Object[]> submissions = new ArrayList<>();
        String sql = "SELECT id, research_title, supervisor_name, presentation_type, status, submission_date FROM submissions WHERE student_id = ? ORDER BY submission_date DESC";
        
        try (Connection conn = SQLiteConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Object[] row = new Object[6];
                row[0] = rs.getInt("id");
                row[1] = rs.getString("research_title");
                row[2] = rs.getString("presentation_type");
                row[3] = rs.getString("supervisor_name");
                row[4] = rs.getString("status");
                row[5] = rs.getString("submission_date");
                
                submissions.add(row);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching submissions: " + e.getMessage());
        }
        
        return submissions;
    }

    public List<Object[]> getAllSubmissions() {
        List<Object[]> submissions = new ArrayList<>();
        // Order must match: 0:id, 1:student_id, 2:research_title, 3:presentation_type, 4:supervisor_name, 5:status
        String sql = "SELECT id, student_id, research_title, presentation_type, supervisor_name, status FROM submissions";
        
        try (java.sql.Connection conn = database.SQLiteConnection.connect();
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                submissions.add(new Object[]{
                    rs.getInt("id"),
                    rs.getInt("student_id"), // This is what getUsernameById uses
                    rs.getString("research_title"),
                    rs.getString("presentation_type"),
                    rs.getString("supervisor_name"),
                    rs.getString("status")
                });
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return submissions;
    }

    public boolean updateSubmissionStatus(int id, String newStatus) {
        String sql = "UPDATE submissions SET status = ? WHERE id = ?";
        try (Connection conn = SQLiteConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Object[]> getAssignmentsForEvaluator(int evaluatorId) {
        List<Object[]> assignments = new ArrayList<>();
        // Join logic to find submissions linked to this specific evaluator
        String sql = """
            SELECT s.id, s.research_title, s.presentation_type, s.status 
            FROM submissions s
            JOIN session_assignments sa ON s.id = sa.submission_id
            WHERE sa.evaluator_id = ?
            """;
        
        try (Connection conn = SQLiteConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, evaluatorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                assignments.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("research_title"),
                    rs.getString("presentation_type"),
                    rs.getString("status"),
                    "Grade" // Label for the action button
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignments;
    }

    public int getTotalSubmissionCount() {
        String sql = "SELECT COUNT(*) FROM submissions";
        try (Connection conn = SQLiteConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public int getSubmissionCountByType(String type) {
        String sql = "SELECT COUNT(*) FROM submissions WHERE presentation_type = ?";
        try (Connection conn = SQLiteConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public int getTotalStudentSubmissions(int studentId) {
        String sql = "SELECT COUNT(*) FROM submissions WHERE student_id = ?";
        try (java.sql.Connection conn = database.SQLiteConnection.connect();
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            java.sql.ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getSubmissionCountByStatus(int studentId, String status) {
        String sql;
        // If the UI asks for APPROVED, we also count ASSIGNED status
        if (status.equalsIgnoreCase("APPROVED")) {
            sql = "SELECT COUNT(*) FROM submissions WHERE student_id = ? AND status IN ('APPROVED', 'ASSIGNED')";
        } else {
            sql = "SELECT COUNT(*) FROM submissions WHERE student_id = ? AND status = ?";
        }

        try (java.sql.Connection conn = database.SQLiteConnection.connect();
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            // Only set the second parameter if we are using the single-status query
            if (!status.equalsIgnoreCase("APPROVED")) {
                pstmt.setString(2, status);
            }
            
            java.sql.ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
