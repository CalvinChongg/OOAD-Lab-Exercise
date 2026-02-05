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
        // REMOVE any WHERE clause to see everything
        String sql = "SELECT id, research_title, presentation_type, supervisor_name, status FROM submissions";
        
        try (Connection conn = SQLiteConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                submissions.add(new Object[]{
                    rs.getInt("id"),
                    "Student " + rs.getInt("id"), // Placeholder for student name
                    rs.getString("research_title"),
                    rs.getString("presentation_type"),
                    rs.getString("supervisor_name"),
                    rs.getString("status"),
                    "Review" // Button label
                });
            }
        } catch (SQLException e) {
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
}
