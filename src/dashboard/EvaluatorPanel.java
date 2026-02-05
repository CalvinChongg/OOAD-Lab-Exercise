package dashboard;

import dao.EvaluationDAO;
import dao.SubmissionDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class EvaluatorPanel extends JPanel {
    private MainFrame mainFrame;
    private JTable assignmentsTable, completedTable;
    private DefaultTableModel assignmentsTableModel, completedTableModel;
    // We will use assignmentsTableModel instead of the generic tableModel variable
    private int currentSubmissionId = -1; 
    private int currentEvaluatorId; 

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

        // Fetch data from database immediately
        loadAssignments();
    }

    public void loadAssignments() {
        if (assignmentsTableModel != null) {
            assignmentsTableModel.setRowCount(0); 

            SubmissionDAO dao = new SubmissionDAO();
            // Now using the dynamic ID passed from the login process
            List<Object[]> assignments = dao.getAssignmentsForEvaluator(currentEvaluatorId);

            if (assignments.isEmpty()) {
                // Optional: Add a placeholder if nothing is assigned to THIS evaluator
                assignmentsTableModel.addRow(new Object[]{"", "No assignments found", "", "", ""});
            } else {
                for (Object[] row : assignments) {
                    assignmentsTableModel.addRow(row);
                }
            }
        }
    }
    
    private JPanel createAssignmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Assigned Evaluations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        // Initialize the model here
        String[] columnNames = {"ID", "Title", "Type", "Status", "Action"};
        assignmentsTableModel = new DefaultTableModel(columnNames, 0);
        
        assignmentsTable = new JTable(assignmentsTableModel);
        assignmentsTable.setRowHeight(35);
        
        // Selection Listener to update currentSubmissionId
        assignmentsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = assignmentsTable.getSelectedRow();
                if (row != -1) {
                    currentSubmissionId = (int) assignmentsTable.getValueAt(row, 0);
                }
            }
        });

        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(assignmentsTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCompletedPanel() {
        return createCompletedEvaluationsPanel();
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
        infoPanel.add(new JLabel("John Doe"));
        infoPanel.add(new JLabel("Title:"));
        infoPanel.add(new JLabel("AI in Healthcare: A Comprehensive Review"));
        infoPanel.add(new JLabel("Type:"));
        infoPanel.add(new JLabel("Oral Presentation"));
        infoPanel.add(new JLabel("Session:"));
        infoPanel.add(new JLabel("SES-002: Oral Session A (10:30-10:45)"));
        
        // Rubric scoring panel
        JPanel rubricPanel = new JPanel(new GridLayout(5, 3, 10, 10));
        rubricPanel.setBorder(new TitledBorder("Evaluation Rubric (Score 1-10)"));
        rubricPanel.setBackground(new Color(248, 249, 250));
        
        // Headers
        rubricPanel.add(new JLabel("Criterion"));
        rubricPanel.add(new JLabel("Score (1-10)"));
        rubricPanel.add(new JLabel("Comments"));
        
        // Problem Clarity
        rubricPanel.add(new JLabel("Problem Clarity"));
        JSpinner claritySpinner = new JSpinner(new SpinnerNumberModel(7, 1, 10, 1));
        rubricPanel.add(claritySpinner);
        JTextField clarityComment = new JTextField();
        rubricPanel.add(clarityComment);
        
        // Methodology
        rubricPanel.add(new JLabel("Methodology"));
        JSpinner methodSpinner = new JSpinner(new SpinnerNumberModel(8, 1, 10, 1));
        rubricPanel.add(methodSpinner);
        JTextField methodComment = new JTextField();
        rubricPanel.add(methodComment);
        
        // Results
        rubricPanel.add(new JLabel("Results"));
        JSpinner resultsSpinner = new JSpinner(new SpinnerNumberModel(7, 1, 10, 1));
        rubricPanel.add(resultsSpinner);
        JTextField resultsComment = new JTextField();
        rubricPanel.add(resultsComment);
        
        // Presentation
        rubricPanel.add(new JLabel("Presentation Quality"));
        JSpinner presSpinner = new JSpinner(new SpinnerNumberModel(9, 1, 10, 1));
        rubricPanel.add(presSpinner);
        JTextField presComment = new JTextField();
        rubricPanel.add(presComment);
        
        // Overall comments
        JPanel overallPanel = new JPanel(new BorderLayout(10, 10));
        overallPanel.setBorder(new TitledBorder("Overall Comments"));
        overallPanel.setBackground(new Color(248, 249, 250));
        
        JTextArea overallComment = new JTextArea(4, 50);
        overallComment.setLineWrap(true);
        overallComment.setWrapStyleWord(true);
        JScrollPane commentScroll = new JScrollPane(overallComment);
        overallPanel.add(commentScroll, BorderLayout.CENTER);
        
        // Calculate total
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(new Color(248, 249, 250));
        totalPanel.add(new JLabel("Total Score: "));
        JLabel totalLabel = new JLabel("31/40 (7.75/10)");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalLabel.setForeground(new Color(46, 204, 113));
        totalPanel.add(totalLabel);
        
        // Submit button
        JButton submitBtn = createActionButton("Submit Evaluation", new Color(46, 204, 113));
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        submitBtn.addActionListener(e -> {
            if (currentSubmissionId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a submission first!");
                return;
            }

            int clarity = (int) claritySpinner.getValue();
            int method = (int) methodSpinner.getValue();
            int res = (int) resultsSpinner.getValue();
            int quality = (int) presSpinner.getValue();
            String comment = overallComment.getText();

            EvaluationDAO evalDao = new EvaluationDAO();
            if (evalDao.submitEvaluation(currentSubmissionId, currentEvaluatorId, clarity, method, res, quality, comment)) {
                JOptionPane.showMessageDialog(this, "Evaluation Saved!");
                new SubmissionDAO().updateSubmissionStatus(currentSubmissionId, "COMPLETED");
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
        panel.add(totalPanel, BorderLayout.SOUTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(22, 160, 133));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        
        // Left side: Title and welcome message
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
        
        // Right side: Logout button (MATCHING COORDINATOR STYLE)
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
        
        // Add hover effects
        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(192, 57, 43));
                logoutBtn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(169, 50, 38), 2),
                    BorderFactory.createEmptyBorder(10, 25, 10, 25)
                ));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(231, 76, 60));
                logoutBtn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(192, 57, 43), 2),
                    BorderFactory.createEmptyBorder(10, 25, 10, 25)
                ));
            }
        });
        
        // Logout action with confirmation
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
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Welcome Card
        JPanel welcomeCard = new JPanel(new BorderLayout(10, 10));
        welcomeCard.setBackground(new Color(22, 160, 133));
        welcomeCard.setBorder(new CompoundBorder(
            new LineBorder(new Color(20, 143, 119), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel welcomeTitle = new JLabel("Welcome, Evaluator");
        welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeTitle.setForeground(Color.WHITE);
        
        JTextArea welcomeMessage = new JTextArea("As an evaluator, your role is crucial in maintaining the quality of presentations. Please review assigned submissions, provide fair evaluations, and submit scores before deadlines.");
        welcomeMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeMessage.setForeground(Color.WHITE);
        welcomeMessage.setEditable(false);
        welcomeMessage.setLineWrap(true);
        welcomeMessage.setWrapStyleWord(true);
        welcomeMessage.setOpaque(false);
        
        welcomeCard.add(welcomeTitle, BorderLayout.NORTH);
        welcomeCard.add(welcomeMessage, BorderLayout.CENTER);
        
        panel.add(welcomeCard, BorderLayout.NORTH);
        
        // Evaluation Stats
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        statsPanel.setBackground(Color.WHITE);
        
        statsPanel.add(createEvalStatCard("Pending Evaluations", "8", new Color(230, 126, 34)));
        statsPanel.add(createEvalStatCard("Completed", "15", new Color(46, 204, 113)));
        statsPanel.add(createEvalStatCard("Average Score", "7.8/10", new Color(52, 152, 219)));
        statsPanel.add(createEvalStatCard("Oral Presentations", "10", new Color(155, 89, 182)));
        statsPanel.add(createEvalStatCard("Poster Presentations", "13", new Color(22, 160, 133)));
        statsPanel.add(createEvalStatCard("Next Deadline", "3 days", new Color(231, 76, 60)));
        
        panel.add(statsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createEvaluationTasksPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Assigned Evaluations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        
        JLabel subtitle = new JLabel("Please complete evaluations before the deadline");
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        subtitle.setForeground(new Color(100, 100, 100));
        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        northPanel.add(title, BorderLayout.WEST);
        northPanel.add(subtitle, BorderLayout.EAST);
        
        panel.add(northPanel, BorderLayout.NORTH);
        
        // Assignments Table
        String[] columnNames = {"ID", "Student", "Title", "Type", "Session", "Deadline", "Status", "Action"};
        assignmentsTableModel = new DefaultTableModel(columnNames, 0);
        
        // Add sample data
        Object[][] sampleData = {
            {101, "John Doe", "AI in Healthcare", "Oral", "SES-002", "2024-05-10", "Pending", "Evaluate"},
            {102, "Jane Smith", "Quantum Computing", "Poster", "SES-003", "2024-05-10", "Pending", "Evaluate"},
            {103, "Bob Wilson", "Renewable Energy", "Oral", "SES-002", "2024-05-11", "In Progress", "Continue"},
            {104, "Alice Brown", "Climate Change", "Poster", "SES-003", "2024-05-12", "Pending", "Evaluate"},
            {105, "Charlie Lee", "Space Exploration", "Oral", "SES-005", "2024-05-13", "Pending", "Evaluate"}
        };
        
        for (Object[] row : sampleData) {
            assignmentsTableModel.addRow(row);
        }
        
        assignmentsTable = new JTable(assignmentsTableModel);
        assignmentsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        assignmentsTable.setRowHeight(35);
        assignmentsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        assignmentsTable.getTableHeader().setBackground(new Color(22, 160, 133));
        assignmentsTable.getTableHeader().setForeground(Color.WHITE);
        
        // Custom renderer for status column
        assignmentsTable.getColumnModel().getColumn(6).setCellRenderer(new StatusCellRenderer());
        
        JScrollPane scrollPane = new JScrollPane(assignmentsTable);
        scrollPane.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Instructions
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        instructionsPanel.setBorder(new TitledBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            "Evaluation Guidelines",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(22, 160, 133)
        ));
        instructionsPanel.setBackground(new Color(248, 249, 250));
        
        JTextArea guidelines = new JTextArea("• Score each criterion from 1-10\n• Provide constructive comments\n• Submit evaluation within 48 hours of assignment\n• Contact coordinator for any questions\n• Use the rubric as reference for scoring");
        guidelines.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        guidelines.setForeground(new Color(100, 100, 100));
        guidelines.setEditable(false);
        guidelines.setOpaque(false);
        
        instructionsPanel.add(guidelines, BorderLayout.CENTER);
        
        panel.add(instructionsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createCompletedEvaluationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Completed Evaluations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        
        // Completed Evaluations Table
        String[] columnNames = {"ID", "Student", "Title", "Type", "Score", "Date Completed", "View"};
        completedTableModel = new DefaultTableModel(columnNames, 0);
        
        // Add sample data
        Object[][] sampleData = {
            {201, "David Chen", "Machine Learning", "Oral", "9.2/10", "2024-05-05", "View"},
            {202, "Emma Wilson", "Biotechnology", "Poster", "8.5/10", "2024-05-06", "View"},
            {203, "Frank Miller", "Data Privacy", "Oral", "7.8/10", "2024-05-07", "View"},
            {204, "Grace Lee", "Robotics", "Poster", "8.9/10", "2024-05-08", "View"},
            {205, "Henry Brown", "Cybersecurity", "Oral", "8.1/10", "2024-05-09", "View"}
        };
        
        for (Object[] row : sampleData) {
            completedTableModel.addRow(row);
        }
        
        completedTable = new JTable(completedTableModel);
        completedTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        completedTable.setRowHeight(35);
        completedTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        completedTable.getTableHeader().setBackground(new Color(22, 160, 133));
        completedTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(completedTable);
        scrollPane.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRubricPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Evaluation Rubric");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        
        // Rubric Table
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
        
        // Center align all cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < rubricTable.getColumnCount(); i++) {
            rubricTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(rubricTable);
        scrollPane.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        
        // Scoring Guidelines
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
    
    private JPanel createEvalStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(100, 100, 100));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(22, 160, 133));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel footerText = new JLabel(
            "© 2024 Seminar Management System - Evaluator Module v1.0 | Evaluator: Dr. Smith | Department: Computer Science",
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
    
    // Rounded Border class
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
    
    // Custom cell renderer for status column
    class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            JLabel label = (JLabel) c;
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setOpaque(true);
            
            if (value == null) return c;
            
            String status = value.toString();
            switch (status.toLowerCase()) {
                case "pending":
                    label.setBackground(new Color(252, 248, 227));
                    label.setForeground(new Color(138, 109, 59));
                    break;
                case "in progress":
                    label.setBackground(new Color(217, 237, 247));
                    label.setForeground(new Color(49, 112, 143));
                    break;
                case "completed":
                    label.setBackground(new Color(220, 237, 200));
                    label.setForeground(new Color(60, 118, 61));
                    break;
                default:
                    label.setBackground(Color.WHITE);
                    label.setForeground(Color.BLACK);
            }
            
            label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            return label;
        }
    }

    
}
