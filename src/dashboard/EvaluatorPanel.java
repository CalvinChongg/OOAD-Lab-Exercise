package dashboard;

import dao.EvaluationDAO;
import dao.SubmissionDAO;
import database.SQLiteConnection;
import java.awt.*;
import java.sql.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class EvaluatorPanel extends JPanel {
    private MainFrame mainFrame;
    private JTable assignmentsTable, completedTable;
    private DefaultTableModel assignmentsTableModel, completedTableModel;
    private int currentSubmissionId = -1; 
    private int currentEvaluatorId;
    
    // Form components
    private JLabel presenterLabel, titleLabel, typeLabel, sessionLabel;
    private JSpinner claritySpinner, methodSpinner, resultsSpinner, presSpinner;
    private JTextField clarityComment, methodComment, resultsComment, presComment;
    private JTextArea overallComment;

    public void setEvaluatorId(int id) {
        this.currentEvaluatorId = id;
    }
    
    public EvaluatorPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 245, 250));
        
        // Header Panel
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        tabbedPane.addTab("Assigned Presentations", createAssignmentsPanel());
        tabbedPane.addTab("Evaluate Presentation", createEvaluationFormPanel());
        tabbedPane.addTab("Evaluation Rubric", createRubricPanel());
        tabbedPane.addTab("Completed", createCompletedPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
        
        // Add tab change listener
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 3) {
                loadCompletedEvaluations();
            }
        });
    }

    public void loadAssignments() {
        if (assignmentsTableModel != null) {
            assignmentsTableModel.setRowCount(0); 

            SubmissionDAO dao = new SubmissionDAO();
            List<Object[]> assignments = dao.getAssignmentsForEvaluator(currentEvaluatorId);

            if (assignments.isEmpty()) {
                assignmentsTableModel.addRow(new Object[]{"", "No assignments found", "", "", "No Action"});
            } else {
                for (Object[] row : assignments) {
                    assignmentsTableModel.addRow(row);
                }
            }
        }
    }
    
    private void loadCompletedEvaluations() {
        if (completedTableModel != null) {
            completedTableModel.setRowCount(0);
            
            String sql = """
                SELECT e.submission_id, s.research_title, s.presentation_type, 
                       e.overall_score, e.evaluation_date
                FROM evaluations e
                JOIN submissions s ON e.submission_id = s.id
                WHERE e.evaluator_id = ?
                ORDER BY e.evaluation_date DESC
                """;
                
            try (Connection conn = SQLiteConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, currentEvaluatorId);
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    completedTableModel.addRow(new Object[]{
                        rs.getInt("submission_id"),
                        rs.getString("research_title"),
                        rs.getString("presentation_type"),
                        String.format("%.2f", rs.getDouble("overall_score")),
                        rs.getString("evaluation_date"),
                        "View Details"
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void updateEvaluationForm(int submissionId) {
        if (submissionId == -1) {
            presenterLabel.setText("Select a submission first");
            titleLabel.setText("No submission selected");
            typeLabel.setText("N/A");
            sessionLabel.setText("N/A");
            return;
        }
        
        String sql = """
            SELECT u.username, s.research_title, s.presentation_type, 
                   ses.name as session_name
            FROM submissions s
            JOIN users u ON s.student_id = u.id
            LEFT JOIN session_assignments sa ON s.id = sa.submission_id
            LEFT JOIN sessions ses ON sa.session_id = ses.id
            WHERE s.id = ?
            """;
            
        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, submissionId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                presenterLabel.setText(rs.getString("username"));
                titleLabel.setText(rs.getString("research_title"));
                typeLabel.setText(rs.getString("presentation_type"));
                String sessionName = rs.getString("session_name");
                sessionLabel.setText(sessionName != null ? sessionName : "Not assigned to session");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private JPanel createAssignmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Assigned Presentations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        String[] columnNames = {"ID", "Title", "Type", "Status", "Action"};
        assignmentsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; 
            }
        };
        
        assignmentsTable = new JTable(assignmentsTableModel);
        assignmentsTable.setRowHeight(35);
        
        // Custom renderer and editor for Action column
        assignmentsTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer("Evaluate"));
        assignmentsTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), "Evaluate"));
        
        assignmentsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = assignmentsTable.getSelectedRow();
                if (row != -1) {
                    Object idValue = assignmentsTableModel.getValueAt(row, 0);
                    if (idValue instanceof Integer) {
                        currentSubmissionId = (int) idValue;
                    } else if (idValue instanceof String && !((String) idValue).isEmpty()) {
                        try {
                            currentSubmissionId = Integer.parseInt((String) idValue);
                        } catch (NumberFormatException ex) {
                            currentSubmissionId = -1;
                        }
                    }
                }
            }
        });
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(assignmentsTable), BorderLayout.CENTER);
        
        JButton refreshBtn = new JButton("Refresh Assignments");
        refreshBtn.addActionListener(e -> loadAssignments());
        panel.add(refreshBtn, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createCompletedPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Completed Evaluations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        
        String[] columnNames = {"ID", "Title", "Type", "Score", "Date", "Action"};
        completedTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        
        completedTable = new JTable(completedTableModel);
        completedTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        completedTable.setRowHeight(35);
        completedTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        completedTable.getTableHeader().setBackground(new Color(22, 160, 133));
        completedTable.getTableHeader().setForeground(Color.WHITE);
        
        // Custom renderer and editor for Action column
        completedTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer("View"));
        completedTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), "View"));
        
        JScrollPane scrollPane = new JScrollPane(completedTable);
        scrollPane.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton refreshBtn = new JButton("Refresh Completed");
        refreshBtn.addActionListener(e -> loadCompletedEvaluations());
        panel.add(refreshBtn, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createEvaluationFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Evaluate Presentation");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        
        // Main form panel
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        formPanel.setBackground(new Color(248, 249, 250));
        
        // Presentation info
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        infoPanel.setBackground(new Color(248, 249, 250));
        
        infoPanel.add(new JLabel("Presenter:"));
        presenterLabel = new JLabel("Select a submission from Assigned tab");
        infoPanel.add(presenterLabel);
        
        infoPanel.add(new JLabel("Title:"));
        titleLabel = new JLabel("No submission selected");
        infoPanel.add(titleLabel);
        
        infoPanel.add(new JLabel("Type:"));
        typeLabel = new JLabel("N/A");
        infoPanel.add(typeLabel);
        
        infoPanel.add(new JLabel("Session:"));
        sessionLabel = new JLabel("N/A");
        infoPanel.add(sessionLabel);
        
        // Rubric scoring panel
        JPanel rubricPanel = new JPanel(new GridLayout(5, 3, 10, 10));
        rubricPanel.setBorder(new TitledBorder("Evaluation Rubric (Score 1-10)"));
        rubricPanel.setBackground(new Color(248, 249, 250));
        
        rubricPanel.add(new JLabel("Criterion"));
        rubricPanel.add(new JLabel("Score (1-10)"));
        rubricPanel.add(new JLabel("Comments"));
        
        // Problem Clarity
        rubricPanel.add(new JLabel("Problem Clarity"));
        claritySpinner = new JSpinner(new SpinnerNumberModel(7, 1, 10, 1));
        rubricPanel.add(claritySpinner);
        clarityComment = new JTextField();
        rubricPanel.add(clarityComment);
        
        // Methodology
        rubricPanel.add(new JLabel("Methodology"));
        methodSpinner = new JSpinner(new SpinnerNumberModel(8, 1, 10, 1));
        rubricPanel.add(methodSpinner);
        methodComment = new JTextField();
        rubricPanel.add(methodComment);
        
        // Results
        rubricPanel.add(new JLabel("Results"));
        resultsSpinner = new JSpinner(new SpinnerNumberModel(7, 1, 10, 1));
        rubricPanel.add(resultsSpinner);
        resultsComment = new JTextField();
        rubricPanel.add(resultsComment);
        
        // Presentation
        rubricPanel.add(new JLabel("Presentation Quality"));
        presSpinner = new JSpinner(new SpinnerNumberModel(9, 1, 10, 1));
        rubricPanel.add(presSpinner);
        presComment = new JTextField();
        rubricPanel.add(presComment);
        
        // Overall comments
        JPanel overallPanel = new JPanel(new BorderLayout(10, 10));
        overallPanel.setBorder(new TitledBorder("Overall Comments"));
        overallPanel.setBackground(new Color(248, 249, 250));
        
        overallComment = new JTextArea(4, 50);
        overallComment.setLineWrap(true);
        overallComment.setWrapStyleWord(true);
        JScrollPane commentScroll = new JScrollPane(overallComment);
        overallPanel.add(commentScroll, BorderLayout.CENTER);
        
        // Submit button
        JButton submitBtn = createActionButton("Submit Evaluation", new Color(46, 204, 113));
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        submitBtn.addActionListener(e -> {
            if (currentSubmissionId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a submission first from the 'Assigned Presentations' tab!");
                return;
            }

            int clarity = (int) claritySpinner.getValue();
            int method = (int) methodSpinner.getValue();
            int res = (int) resultsSpinner.getValue();
            int quality = (int) presSpinner.getValue();
            String comment = overallComment.getText();

            EvaluationDAO evalDao = new EvaluationDAO();
            if (evalDao.submitEvaluation(currentSubmissionId, currentEvaluatorId, 
                                         clarity, method, res, quality, comment)) {
                JOptionPane.showMessageDialog(this, "Evaluation Saved Successfully!");
                
                // Update submission status
                new SubmissionDAO().updateSubmissionStatus(currentSubmissionId, "COMPLETED");
                
                // Refresh tables
                loadAssignments();
                loadCompletedEvaluations();
                
                // Reset form
                claritySpinner.setValue(7);
                methodSpinner.setValue(8);
                resultsSpinner.setValue(7);
                presSpinner.setValue(9);
                clarityComment.setText("");
                methodComment.setText("");
                resultsComment.setText("");
                presComment.setText("");
                overallComment.setText("");
                currentSubmissionId = -1;
                updateEvaluationForm(-1);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save evaluation!");
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(submitBtn);
        
        // Assemble form
        formPanel.add(infoPanel, BorderLayout.NORTH);
        formPanel.add(rubricPanel, BorderLayout.CENTER);
        formPanel.add(overallPanel, BorderLayout.SOUTH);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(22, 160, 133));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setOpaque(false);
        
        JLabel title = new JLabel("Evaluator Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        
        JLabel subtitle = new JLabel("Presentation Evaluation Portal");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(200, 200, 200));
        
        leftPanel.add(title);
        leftPanel.add(subtitle);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        
        JButton logoutBtn = new JButton("LOGOUT");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(192, 57, 43), 2),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(120, 45));
        
        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(231, 76, 60));
            }
        });
        
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                EvaluatorPanel.this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                mainFrame.switchScreen("LOGIN");
            }
        });
        
        rightPanel.add(logoutBtn);
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createRubricPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Evaluation Rubric");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        
        String[] columnNames = {"Criterion", "Excellent (9-10)", "Good (7-8)", "Average (5-6)", "Poor (1-4)"};
        Object[][] rubricData = {
            {"Problem Clarity", "Clear research question, well-defined problem", "Problem defined but could be clearer", "Problem statement vague", "No clear problem statement"},
            {"Methodology", "Appropriate methods, well-executed", "Methods appropriate but minor issues", "Methods questionable or poorly executed", "Inappropriate methods or missing details"},
            {"Results & Analysis", "Clear results, thorough analysis", "Results presented, adequate analysis", "Results incomplete or analysis superficial", "Results missing or incorrect analysis"},
            {"Presentation Quality", "Professional, engaging, well-paced", "Good presentation, minor issues", "Adequate but unengaging", "Poor delivery or preparation"},
            {"Q&A Handling", "Answers all questions confidently", "Answers most questions adequately", "Struggles with some questions", "Unable to answer questions"}
        };
        
        JTable rubricTable = new JTable(rubricData, columnNames);
        rubricTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rubricTable.setRowHeight(60);
        rubricTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        rubricTable.getTableHeader().setBackground(new Color(22, 160, 133));
        rubricTable.getTableHeader().setForeground(Color.WHITE);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < rubricTable.getColumnCount(); i++) {
            rubricTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(rubricTable);
        scrollPane.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        
        JPanel guidelinesPanel = new JPanel(new BorderLayout(10, 10));
        guidelinesPanel.setBorder(new TitledBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            "Scoring Guidelines",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(22, 160, 133)
        ));
        guidelinesPanel.setBackground(new Color(248, 249, 250));
        
        JTextArea guidelines = new JTextArea();
        guidelines.setText("""
            • Overall Score Calculation:
              - Sum of all criteria scores (max 50)
              - Divided by 5 for final score (max 10)
            
            • Scoring Scale:
              9-10: Excellent (Outstanding work)
              7-8: Good (Above average)
              5-6: Average (Meets minimum requirements)
              1-4: Poor (Needs significant improvement)
            
            • Comments:
              - Provide specific, constructive feedback
              - Highlight strengths and areas for improvement
              - Be professional and respectful
            """);
        guidelines.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        guidelines.setForeground(new Color(100, 100, 100));
        guidelines.setEditable(false);
        guidelines.setOpaque(false);
        
        guidelinesPanel.add(guidelines, BorderLayout.CENTER);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(guidelinesPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(22, 160, 133));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel footerText = new JLabel(
            "© 2024 Seminar Management System - Evaluator Module v1.0",
            SwingConstants.CENTER
        );
        footerText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerText.setForeground(new Color(200, 200, 200));
        
        footer.add(footerText, BorderLayout.CENTER);
        return footer;
    }
    
    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(5));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    // Inner classes for table buttons
    class ButtonRenderer extends JButton implements TableCellRenderer {
        private String buttonText;
        
        public ButtonRenderer(String text) {
            buttonText = text;
            setOpaque(true);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(buttonText);
            setBackground(new Color(52, 152, 219));
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setBorder(new RoundedBorder(4));
            return this;
        }
    }
    
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String buttonText;
        private boolean isPushed;
        
        public ButtonEditor(JCheckBox checkBox, String text) {
            super(checkBox);
            buttonText = text;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            button.setText(buttonText);
            isPushed = true;
            return button;
        }
        
        public Object getCellEditorValue() {
            if (isPushed) {
                JTable sourceTable = (button.getParent() instanceof JTable) ? 
                    (JTable) button.getParent() : null;
                    
                if (sourceTable != null) {
                    int row = sourceTable.getSelectedRow();
                    if (row != -1) {
                        if (buttonText.equals("Evaluate")) {
                            Object idValue = sourceTable.getValueAt(row, 0);
                            if (idValue instanceof Integer) {
                                currentSubmissionId = (int) idValue;
                            } else if (idValue instanceof String && !((String) idValue).isEmpty()) {
                                try {
                                    currentSubmissionId = Integer.parseInt((String) idValue);
                                } catch (NumberFormatException ex) {
                                    currentSubmissionId = -1;
                                }
                            }
                            // Update evaluation form with submission details
                            updateEvaluationForm(currentSubmissionId);
                            // Switch to evaluation tab
                            ((JTabbedPane)getParent().getParent().getParent()).setSelectedIndex(1);
                        } else if (buttonText.equals("View")) {
                            // Show evaluation details
                            int submissionId = (int) sourceTable.getValueAt(row, 0);
                            showEvaluationDetails(submissionId);
                        }
                    }
                }
            }
            isPushed = false;
            return buttonText;
        }
        
        private void showEvaluationDetails(int submissionId) {
            String sql = """
                SELECT e.problem_clarity, e.methodology, e.results, 
                       e.presentation_quality, e.overall_score, e.comments,
                       s.research_title, u.username, e.evaluation_date
                FROM evaluations e
                JOIN submissions s ON e.submission_id = s.id
                JOIN users u ON s.student_id = u.id
                WHERE e.submission_id = ? AND e.evaluator_id = ?
                """;
                
            try (Connection conn = SQLiteConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, submissionId);
                pstmt.setInt(2, currentEvaluatorId);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    StringBuilder details = new StringBuilder();
                    details.append("Evaluation Details\n");
                    details.append("==================\n\n");
                    details.append("Title: ").append(rs.getString("research_title")).append("\n");
                    details.append("Student: ").append(rs.getString("username")).append("\n");
                    details.append("Date: ").append(rs.getString("evaluation_date")).append("\n\n");
                    details.append("Scores:\n");
                    details.append("• Problem Clarity: ").append(rs.getInt("problem_clarity")).append("/10\n");
                    details.append("• Methodology: ").append(rs.getInt("methodology")).append("/10\n");
                    details.append("• Results: ").append(rs.getInt("results")).append("/10\n");
                    details.append("• Presentation Quality: ").append(rs.getInt("presentation_quality")).append("/10\n");
                    details.append("• Overall Score: ").append(String.format("%.2f", rs.getDouble("overall_score"))).append("/10\n\n");
                    details.append("Comments:\n").append(rs.getString("comments"));
                    
                    JTextArea textArea = new JTextArea(details.toString());
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(500, 400));
                    
                    JOptionPane.showMessageDialog(EvaluatorPanel.this, scrollPane, 
                        "Evaluation Details", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    class RoundedBorder implements Border {
        private int radius;
        
        RoundedBorder(int radius) {
            this.radius = radius;
        }
        
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+1, this.radius+1);
        }
        
        public boolean isBorderOpaque() {
            return true;
        }
        
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }
}