package dashboard;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class EvaluatorPanel extends JPanel {
    private MainFrame mainFrame;
    private JTable assignmentsTable, evaluationsTable;
    private DefaultTableModel assignmentsTableModel, evaluationsTableModel;
    
    public EvaluatorPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 245, 250));
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content Panel with Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Tab 1: Dashboard
        tabbedPane.addTab("Dashboard", createDashboardPanel());
        
        // Tab 2: Evaluation Tasks
        tabbedPane.addTab("Evaluation Tasks", createEvaluationTasksPanel());
        
        // Tab 3: Completed Evaluations
        tabbedPane.addTab("Completed", createCompletedEvaluationsPanel());
        
        // Tab 4: Rubric
        tabbedPane.addTab("Evaluation Rubric", createRubricPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Footer
        add(createFooterPanel(), BorderLayout.SOUTH);
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
        
        // Right side: Logout button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(new RoundedBorder(10));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> mainFrame.switchScreen("LOGIN"));
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        
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
        evaluationsTableModel = new DefaultTableModel(columnNames, 0);
        
        // Add sample data
        Object[][] sampleData = {
            {201, "David Chen", "Machine Learning", "Oral", "9.2/10", "2024-05-05", "View"},
            {202, "Emma Wilson", "Biotechnology", "Poster", "8.5/10", "2024-05-06", "View"},
            {203, "Frank Miller", "Data Privacy", "Oral", "7.8/10", "2024-05-07", "View"},
            {204, "Grace Lee", "Robotics", "Poster", "8.9/10", "2024-05-08", "View"},
            {205, "Henry Brown", "Cybersecurity", "Oral", "8.1/10", "2024-05-09", "View"}
        };
        
        for (Object[] row : sampleData) {
            evaluationsTableModel.addRow(row);
        }
        
        evaluationsTable = new JTable(evaluationsTableModel);
        evaluationsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        evaluationsTable.setRowHeight(35);
        evaluationsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        evaluationsTable.getTableHeader().setBackground(new Color(22, 160, 133));
        evaluationsTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(evaluationsTable);
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
