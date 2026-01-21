package dashboard;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class AdminPanel extends JPanel {
    private MainFrame mainFrame;
    private JTable userTable;
    private DefaultTableModel userTableModel;
    
    public AdminPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 245, 250));
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content Panel with Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Tab 1: User Management
        tabbedPane.addTab("User Management", createUserManagementPanel());
        
        // Tab 2: System Configuration
        tabbedPane.addTab("System Configuration", createSystemConfigPanel());
        
        // Tab 3: Role Management
        tabbedPane.addTab("Role Management", createRoleManagementPanel());
        
        // Tab 4: Reports & Analytics
        tabbedPane.addTab("Reports & Analytics", createReportsPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Footer
        add(createFooterPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("User Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton addUserBtn = createActionButton("Add New User", new Color(46, 204, 113));
        JButton editUserBtn = createActionButton("Edit User", new Color(52, 152, 219));
        JButton resetPassBtn = createActionButton("Reset Password", new Color(155, 89, 182));
        JButton deleteUserBtn = createActionButton("Delete User", new Color(231, 76, 60));
        
        buttonPanel.add(addUserBtn);
        buttonPanel.add(editUserBtn);
        buttonPanel.add(resetPassBtn);
        buttonPanel.add(deleteUserBtn);
        
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // User Table with Role Selector
        String[] columnNames = {"ID", "Username", "Role", "Email", "Status", "Created", "Actions"};
        userTableModel = new DefaultTableModel(columnNames, 0);
        
        // Add sample data
        Object[][] sampleData = {
            {1, "student1", "Student", "student1@uni.edu", "Active", "2024-01-15", "Edit"},
            {2, "coordinator1", "Coordinator", "coord@uni.edu", "Active", "2024-01-20", "Edit"},
            {3, "evaluator1", "Evaluator", "eval@uni.edu", "Active", "2024-02-10", "Edit"},
            {4, "evaluator2", "Evaluator", "eval2@uni.edu", "Active", "2024-02-15", "Edit"},
            {5, "admin", "Administrator", "admin@uni.edu", "Active", "2024-01-01", "Edit"}
        };
        
        for (Object[] row : sampleData) {
            userTableModel.addRow(row);
        }
        
        userTable = new JTable(userTableModel);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userTable.setRowHeight(35);
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        userTable.getTableHeader().setBackground(new Color(52, 73, 94));
        userTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSystemConfigPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("System Configuration");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        
        JPanel settingsCard = new JPanel(new GridLayout(6, 2, 15, 15));
        settingsCard.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        settingsCard.setBackground(new Color(250, 250, 252));
        
        // System Settings
        settingsCard.add(createSettingLabel("Seminar Title:"));
        settingsCard.add(createSettingField("Annual Research Symposium 2024"));
        
        settingsCard.add(createSettingLabel("Submission Deadline:"));
        settingsCard.add(createSettingField("2024-04-30"));
        
        settingsCard.add(createSettingLabel("Max File Size (MB):"));
        settingsCard.add(createSettingField("50"));
        
        settingsCard.add(createSettingLabel("Allowed File Types:"));
        settingsCard.add(createSettingField("PDF, PPT, PPTX, DOC, DOCX"));
        
        settingsCard.add(createSettingLabel("Evaluation Deadline:"));
        settingsCard.add(createSettingField("2024-05-10"));
        
        settingsCard.add(createSettingLabel("System Mode:"));
        JComboBox<String> modeCombo = new JComboBox<>(new String[]{"Active", "Maintenance", "Closed"});
        modeCombo.setSelectedItem("Active");
        settingsCard.add(modeCombo);
        
        JButton saveBtn = createActionButton("Save Configuration", new Color(46, 204, 113));
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setOpaque(false);
        southPanel.add(saveBtn);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(settingsCard, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createRoleManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Role Permissions Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        
        JPanel rolesPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        rolesPanel.setBackground(Color.WHITE);
        
        rolesPanel.add(createRoleCard("Student", "Submit presentations\nView own submissions\nUpload files", 
            new Color(52, 152, 219), "👨‍🎓"));
        rolesPanel.add(createRoleCard("Evaluator", "Review assigned presentations\nScore using rubrics\nProvide feedback", 
            new Color(22, 160, 133), "👨‍🏫"));
        rolesPanel.add(createRoleCard("Coordinator", "Manage sessions\nAssign evaluators\nGenerate reports\nOversee awards", 
            new Color(41, 128, 185), "👨‍💼"));
        rolesPanel.add(createRoleCard("Admin", "User management\nSystem configuration\nRole assignment\nAll permissions", 
            new Color(52, 73, 94), "👨‍💻"));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(rolesPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRoleCard(String role, String permissions, Color color, String icon) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        
        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        roleLabel.setForeground(color);
        
        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(roleLabel, BorderLayout.CENTER);
        
        JTextArea permArea = new JTextArea(permissions);
        permArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        permArea.setForeground(new Color(100, 100, 100));
        permArea.setEditable(false);
        permArea.setLineWrap(true);
        permArea.setWrapStyleWord(true);
        permArea.setOpaque(false);
        
        JButton editBtn = new JButton("Edit Permissions");
        editBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        editBtn.setBackground(color);
        editBtn.setForeground(Color.WHITE);
        editBtn.setBorder(new RoundedBorder(4));
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(permArea, BorderLayout.CENTER);
        card.add(editBtn, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        
        // Left side: Title and welcome message
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setOpaque(false);
        
        JLabel title = new JLabel("Administrator Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        
        JLabel subtitle = new JLabel("System Administration Portal");
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
                AdminPanel.this,
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
        welcomeCard.setBackground(new Color(52, 152, 219));
        welcomeCard.setBorder(new CompoundBorder(
            new LineBorder(new Color(41, 128, 185), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel welcomeTitle = new JLabel("Welcome, Administrator");
        welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeTitle.setForeground(Color.WHITE);
        
        JTextArea welcomeMessage = new JTextArea("Manage all aspects of the Seminar Management System from this dashboard. You can oversee users, configure system settings, generate reports, and monitor system performance.");
        welcomeMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeMessage.setForeground(Color.WHITE);
        welcomeMessage.setEditable(false);
        welcomeMessage.setLineWrap(true);
        welcomeMessage.setWrapStyleWord(true);
        welcomeMessage.setOpaque(false);
        
        welcomeCard.add(welcomeTitle, BorderLayout.NORTH);
        welcomeCard.add(welcomeMessage, BorderLayout.CENTER);
        
        panel.add(welcomeCard, BorderLayout.NORTH);
        
        // Statistics Panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        statsPanel.setBackground(Color.WHITE);
        
        statsPanel.add(createStatCard("Total Users", "124", new Color(52, 152, 219), "👥"));
        statsPanel.add(createStatCard("Active Today", "47", new Color(46, 204, 113), "📊"));
        statsPanel.add(createStatCard("Submissions", "89", new Color(155, 89, 182), "📝"));
        statsPanel.add(createStatCard("Pending Reviews", "23", new Color(230, 126, 34), "⏳"));
        statsPanel.add(createStatCard("Sessions", "15", new Color(231, 76, 60), "📅"));
        statsPanel.add(createStatCard("System Health", "100%", new Color(22, 160, 133), "💻"));
        
        panel.add(statsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("System Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        
        JPanel settingsCard = new JPanel(new GridLayout(5, 2, 15, 15));
        settingsCard.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        settingsCard.setBackground(new Color(250, 250, 252));
        
        // System Settings Options
        settingsCard.add(createSettingLabel("Seminar Title:"));
        settingsCard.add(createSettingField("Annual Research Symposium 2024"));
        
        settingsCard.add(createSettingLabel("Submission Deadline:"));
        settingsCard.add(createSettingField("2024-04-30"));
        
        settingsCard.add(createSettingLabel("Max File Size (MB):"));
        settingsCard.add(createSettingField("50"));
        
        settingsCard.add(createSettingLabel("Notification Emails:"));
        JCheckBox emailCheckbox = new JCheckBox("Send email notifications", true);
        emailCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        settingsCard.add(emailCheckbox);
        
        settingsCard.add(createSettingLabel("System Maintenance:"));
        JButton maintenanceBtn = new JButton("Enable Maintenance Mode");
        maintenanceBtn.setBackground(new Color(230, 126, 34));
        maintenanceBtn.setForeground(Color.WHITE);
        settingsCard.add(maintenanceBtn);
        
        // Save Button
        JButton saveBtn = new JButton("Save Settings");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBorder(new RoundedBorder(8));
        
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setOpaque(false);
        southPanel.add(saveBtn);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(settingsCard, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Reports & Analytics");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        
        JPanel reportsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        reportsPanel.setBackground(Color.WHITE);
        
        reportsPanel.add(createReportCard("📈 Submission Report", "View all submissions statistics", new Color(52, 152, 219)));
        reportsPanel.add(createReportCard("👥 User Activity", "Track user login and activity", new Color(46, 204, 113)));
        reportsPanel.add(createReportCard("📊 Evaluation Results", "Review evaluation scores", new Color(155, 89, 182)));
        reportsPanel.add(createReportCard("📅 Session Schedule", "Print session schedules", new Color(230, 126, 34)));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(reportsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color, String icon) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setPreferredSize(new Dimension(200, 100));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(100, 100, 100));
        
        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createReportCard(String title, String description, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(color);
        
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descArea.setForeground(new Color(100, 100, 100));
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setOpaque(false);
        
        JButton generateBtn = new JButton("Generate");
        generateBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        generateBtn.setBackground(color);
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setBorder(new RoundedBorder(4));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(descArea, BorderLayout.CENTER);
        card.add(generateBtn, BorderLayout.SOUTH);
        
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
    
    private JLabel createSettingLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }
    
    private JTextField createSettingField(String value) {
        JTextField field = new JTextField(value);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
    
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(52, 73, 94));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel footerText = new JLabel(
            "© 2024 Seminar Management System - Administrator Module v1.0 | System Version: 2.4.1",
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
