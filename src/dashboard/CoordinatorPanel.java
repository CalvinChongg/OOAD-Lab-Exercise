package dashboard;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class CoordinatorPanel extends JPanel {
    private MainFrame mainFrame;
    private JTable sessionsTable, submissionsTable, awardsTable;
    private DefaultTableModel sessionsTableModel, submissionsTableModel, awardsTableModel;
    
    public CoordinatorPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 245, 250));
        
        // Header Panel with LOGOUT BUTTON
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content Panel with Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Tab 1: Dashboard
        tabbedPane.addTab("Dashboard", createDashboardPanel());
        
        // Tab 2: Session Management
        tabbedPane.addTab("Session Management", createSessionManagementPanel());
        
        // Tab 3: Submission Review
        tabbedPane.addTab("Submission Review", createSubmissionReviewPanel());
        
        // Tab 4: Schedule
        tabbedPane.addTab("Schedule", createSchedulePanel());
        
        // Tab 5: Awards & Evaluations
        tabbedPane.addTab("Awards", createAwardsPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Footer
        add(createFooterPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        
        // Left side: Title and welcome message
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setOpaque(false);
        
        JLabel title = new JLabel("Coordinator Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        
        JLabel subtitle = new JLabel("Seminar Coordination Portal");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(200, 200, 200));
        
        leftPanel.add(title);
        leftPanel.add(subtitle);
        
        // RIGHT SIDE: LOGOUT BUTTON (FIXED AND PROMINENT)
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
                CoordinatorPanel.this,
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
        
        // Overview Card
        JPanel overviewCard = new JPanel(new BorderLayout(10, 10));
        overviewCard.setBackground(new Color(41, 128, 185));
        overviewCard.setBorder(new CompoundBorder(
            new LineBorder(new Color(32, 102, 148), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel overviewTitle = new JLabel("Seminar Overview");
        overviewTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        overviewTitle.setForeground(Color.WHITE);
        
        JTextArea overviewText = new JTextArea("Welcome, Coordinator! You can manage seminar sessions, review submissions, assign evaluators, and create the event schedule from this dashboard.");
        overviewText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        overviewText.setForeground(Color.WHITE);
        overviewText.setEditable(false);
        overviewText.setLineWrap(true);
        overviewText.setWrapStyleWord(true);
        overviewText.setOpaque(false);
        
        overviewCard.add(overviewTitle, BorderLayout.NORTH);
        overviewCard.add(overviewText, BorderLayout.CENTER);
        
        panel.add(overviewCard, BorderLayout.NORTH);
        
        // Quick Stats
        JPanel statsPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        statsPanel.setBackground(Color.WHITE);
        
        statsPanel.add(createCoordStatCard("Total Submissions", "89", new Color(41, 128, 185)));
        statsPanel.add(createCoordStatCard("Pending Review", "23", new Color(230, 126, 34)));
        statsPanel.add(createCoordStatCard("Sessions Created", "15", new Color(46, 204, 113)));
        statsPanel.add(createCoordStatCard("Evaluators", "12", new Color(155, 89, 182)));
        statsPanel.add(createCoordStatCard("Oral Presentations", "45", new Color(52, 73, 94)));
        statsPanel.add(createCoordStatCard("Poster Presentations", "44", new Color(22, 160, 133)));
        statsPanel.add(createCoordStatCard("Venues Booked", "3", new Color(231, 76, 60)));
        statsPanel.add(createCoordStatCard("Days to Event", "45", new Color(142, 68, 173)));
        
        panel.add(statsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSessionManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Session Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton createSessionBtn = createActionButton("Create Session", new Color(46, 204, 113));
        JButton editSessionBtn = createActionButton("Edit Session", new Color(52, 152, 219));
        JButton assignBtn = createActionButton("Assign Presentations", new Color(155, 89, 182));
        
        buttonPanel.add(createSessionBtn);
        buttonPanel.add(editSessionBtn);
        buttonPanel.add(assignBtn);
        
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Sessions Table
        String[] columnNames = {"Session ID", "Name", "Date", "Time", "Type", "Venue", "Status", "Actions"};
        sessionsTableModel = new DefaultTableModel(columnNames, 0);
        
        // Add sample data
        Object[][] sampleData = {
            {"SES-001", "Opening Ceremony", "2024-05-15", "09:00-10:00", "General", "Main Hall", "Scheduled", "Edit"},
            {"SES-002", "Oral Session A", "2024-05-15", "10:30-12:30", "Oral", "Room 101", "Scheduled", "Edit"},
            {"SES-003", "Poster Session I", "2024-05-15", "14:00-16:00", "Poster", "Exhibition Hall", "Scheduled", "Edit"},
            {"SES-004", "Keynote Speech", "2024-05-16", "09:00-10:30", "General", "Main Hall", "Scheduled", "Edit"},
            {"SES-005", "Oral Session B", "2024-05-16", "11:00-13:00", "Oral", "Room 102", "Planning", "Edit"}
        };
        
        for (Object[] row : sampleData) {
            sessionsTableModel.addRow(row);
        }
        
        sessionsTable = new JTable(sessionsTableModel);
        sessionsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sessionsTable.setRowHeight(35);
        sessionsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        sessionsTable.getTableHeader().setBackground(new Color(41, 128, 185));
        sessionsTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(sessionsTable);
        scrollPane.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSubmissionReviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Submission Review");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        
        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setOpaque(false);
        
        filterPanel.add(new JLabel("Filter by:"));
        JComboBox<String> filterCombo = new JComboBox<>(new String[]{"All", "Pending", "Approved", "Rejected", "Oral", "Poster"});
        filterPanel.add(filterCombo);
        
        JButton filterBtn = new JButton("Apply Filter");
        filterBtn.setBackground(new Color(52, 152, 219));
        filterBtn.setForeground(Color.WHITE);
        filterPanel.add(filterBtn);
        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        northPanel.add(title, BorderLayout.WEST);
        northPanel.add(filterPanel, BorderLayout.EAST);
        
        panel.add(northPanel, BorderLayout.NORTH);
        
        // Submissions Table
        String[] columnNames = {"ID", "Student", "Title", "Type", "Supervisor", "Status", "Review", "Action"};
        submissionsTableModel = new DefaultTableModel(columnNames, 0);
        
        // Add sample data
        Object[][] sampleSubmissions = {
            {1, "John Doe", "AI in Healthcare", "Oral", "Dr. Smith", "Pending", "", "Review"},
            {2, "Jane Smith", "Quantum Computing", "Poster", "Dr. Johnson", "Pending", "", "Review"},
            {3, "Bob Wilson", "Renewable Energy", "Oral", "Dr. Brown", "Approved", "8.5/10", "View"},
            {4, "Alice Brown", "Climate Change", "Poster", "Dr. Davis", "Rejected", "4/10", "View"},
            {5, "Charlie Lee", "Space Exploration", "Oral", "Dr. Wilson", "Pending", "", "Review"}
        };
        
        for (Object[] row : sampleSubmissions) {
            submissionsTableModel.addRow(row);
        }
        
        submissionsTable = new JTable(submissionsTableModel);
        submissionsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        submissionsTable.setRowHeight(35);
        submissionsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        submissionsTable.getTableHeader().setBackground(new Color(41, 128, 185));
        submissionsTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(submissionsTable);
        scrollPane.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Seminar Schedule");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        
        // Schedule Cards
        JPanel scheduleGrid = new JPanel(new GridLayout(3, 1, 15, 15));
        scheduleGrid.setBackground(Color.WHITE);
        
        scheduleGrid.add(createScheduleCard("Day 1 - May 15, 2024", "Main Hall & Exhibition Area", 
            "• 09:00-10:00: Opening Ceremony\n• 10:30-12:30: Oral Session A\n• 14:00-16:00: Poster Session I"));
        
        scheduleGrid.add(createScheduleCard("Day 2 - May 16, 2024", "Main Hall & Seminar Rooms", 
            "• 09:00-10:30: Keynote Speech\n• 11:00-13:00: Oral Session B\n• 14:30-16:30: Workshop"));
        
        scheduleGrid.add(createScheduleCard("Day 3 - May 17, 2024", "Main Hall", 
            "• 10:00-12:00: Awards Ceremony\n• 12:00-13:00: Closing Remarks\n• 13:00-14:00: Networking Lunch"));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scheduleGrid, BorderLayout.CENTER);
        
        // Print Button
        JButton printBtn = new JButton("Print Schedule");
        printBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        printBtn.setBackground(new Color(46, 204, 113));
        printBtn.setForeground(Color.WHITE);
        printBtn.setBorder(new RoundedBorder(8));
        
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setOpaque(false);
        southPanel.add(printBtn);
        
        panel.add(southPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createAwardsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Award Nomination & Evaluation Results");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        
        // Awards Cards
        JPanel awardsPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        awardsPanel.setBackground(Color.WHITE);
        
        awardsPanel.add(createAwardCard("🏆 Best Oral", "Highest scoring oral presentation", 
            new Color(255, 193, 7), "John Doe - AI in Healthcare (9.2/10)"));
        awardsPanel.add(createAwardCard("📊 Best Poster", "Highest scoring poster presentation", 
            new Color(33, 150, 243), "Jane Smith - Quantum Computing (9.5/10)"));
        awardsPanel.add(createAwardCard("👥 People's Choice", "Voted by attendees", 
            new Color(156, 39, 176), "Open for Voting"));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(awardsPanel, BorderLayout.CENTER);
        
        // Finalize Button
        JButton finalizeBtn = new JButton("Finalize Award Winners");
        finalizeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        finalizeBtn.setBackground(new Color(46, 204, 113));
        finalizeBtn.setForeground(Color.WHITE);
        finalizeBtn.setBorder(new RoundedBorder(8));
        
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setOpaque(false);
        southPanel.add(finalizeBtn);
        
        panel.add(southPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createAwardCard(String awardName, String description, Color color, String winner) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel nameLabel = new JLabel(awardName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(color);
        
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descArea.setForeground(new Color(100, 100, 100));
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setOpaque(false);
        
        JLabel winnerLabel = new JLabel("Leading: " + winner);
        winnerLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        winnerLabel.setForeground(new Color(60, 60, 60));
        
        JButton nominateBtn = new JButton("Nominate");
        nominateBtn.setBackground(color);
        nominateBtn.setForeground(Color.WHITE);
        nominateBtn.setBorder(new RoundedBorder(5));
        
        card.add(nameLabel, BorderLayout.NORTH);
        card.add(descArea, BorderLayout.CENTER);
        card.add(winnerLabel, BorderLayout.SOUTH);
        card.add(nominateBtn, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createCoordStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
    
    private JPanel createScheduleCard(String day, String venue, String schedule) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(41, 128, 185), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel dayLabel = new JLabel(day);
        dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dayLabel.setForeground(new Color(41, 128, 185));
        
        JLabel venueLabel = new JLabel(venue);
        venueLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        venueLabel.setForeground(new Color(100, 100, 100));
        
        headerPanel.add(dayLabel, BorderLayout.WEST);
        headerPanel.add(venueLabel, BorderLayout.EAST);
        
        JTextArea scheduleArea = new JTextArea(schedule);
        scheduleArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        scheduleArea.setForeground(new Color(50, 50, 50));
        scheduleArea.setEditable(false);
        scheduleArea.setOpaque(false);
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(scheduleArea, BorderLayout.CENTER);
        
        return card;
    }
    
    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(6));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(41, 128, 185));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel footerText = new JLabel(
            "© 2024 Seminar Management System - Coordinator Module v1.0 | Event: Annual Research Symposium 2024",
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
}