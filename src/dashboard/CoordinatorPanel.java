package dashboard;

import dao.AssignmentDAO;
import dao.SessionDAO;
import dao.SubmissionDAO; // Ensure this exists
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class CoordinatorPanel extends JPanel {
    private MainFrame mainFrame;
    private JTable sessionsTable, submissionsTable, awardsTable;
    private DefaultTableModel sessionsTableModel, submissionsTableModel, awardsTableModel;

    private JButton createSessionBtn, editSessionBtn, deleteSessionBtn;

    public CoordinatorPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 245, 250));
        
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Session Management", createSessionManagementPanel());
        tabbedPane.addTab("Submission Review", createSubmissionReviewPanel());
        tabbedPane.addTab("Schedule", createSchedulePanel());
        tabbedPane.addTab("Awards", createAwardsPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // AUTO-REFRESH logic when switching tabs
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) { // Session Management Tab
                loadSessionsFromDB();
            } else if (tabbedPane.getSelectedIndex() == 2) { // Submission Review Tab
                loadSubmissionsFromDB();
            }
        });
        
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
        
        // RIGHT SIDE: LOGOUT BUTTON
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
            int confirm = JOptionPane.showConfirmDialog(CoordinatorPanel.this,
                "Are you sure you want to logout?", "Confirm Logout",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Session Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);

        // INITIALIZE THE BUTTONS
        createSessionBtn = createActionButton("Create Session", new Color(46, 204, 113));
        editSessionBtn = createActionButton("Edit Session", new Color(52, 152, 219));
        deleteSessionBtn = createActionButton("Delete Session", new Color(231, 76, 60)); 
        buttonPanel.add(createSessionBtn);
        buttonPanel.add(editSessionBtn);
        buttonPanel.add(deleteSessionBtn);
        
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);
        
        sessionsTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Date", "Time", "Type", "Venue", "Status"}, 0);
        sessionsTable = new JTable(sessionsTableModel);
        loadSessionsFromDB(); // Load real data
        
        panel.add(new JScrollPane(sessionsTable), BorderLayout.CENTER);

        // CREATE SESSION ACTION
        createSessionBtn.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField dateField = new JTextField("2026-05-15");
            JTextField timeField = new JTextField("09:00-10:00");
            JComboBox<String> typeBox = new JComboBox<>(new String[]{"General", "Oral", "Poster"});
            JTextField venueField = new JTextField();

            Object[] message = {
                "Session Name:", nameField,
                "Date (YYYY-MM-DD):", dateField,
                "Time (HH:mm-HH:mm):", timeField,
                "Type:", typeBox,
                "Venue:", venueField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Create New Session", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                if (new SessionDAO().createSession(nameField.getText(), dateField.getText(), timeField.getText(), 
                                                (String)typeBox.getSelectedItem(), venueField.getText())) {
                    JOptionPane.showMessageDialog(this, "Session Created Successfully!");
                    loadSessionsFromDB();
                }
            }
        });

        editSessionBtn.addActionListener(e -> {
            int selectedRow = sessionsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a session from the table first!");
                return;
            }

            // Extract ID from the "SES-001" format
            String idStr = (String) sessionsTableModel.getValueAt(selectedRow, 0);
            int sessionId = Integer.parseInt(idStr.replace("SES-", ""));

            JTextField nameField = new JTextField((String) sessionsTableModel.getValueAt(selectedRow, 1));
            JTextField dateField = new JTextField((String) sessionsTableModel.getValueAt(selectedRow, 2));
            JTextField timeField = new JTextField((String) sessionsTableModel.getValueAt(selectedRow, 3));
            JTextField venueField = new JTextField((String) sessionsTableModel.getValueAt(selectedRow, 5));

            Object[] message = {
                "Session Name:", nameField,
                "Date:", dateField,
                "Time:", timeField,
                "Venue:", venueField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Edit Session Details", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                if (new SessionDAO().updateSession(sessionId, nameField.getText(), dateField.getText(), 
                                                timeField.getText(), venueField.getText())) {
                    JOptionPane.showMessageDialog(this, "Session updated successfully!");
                    loadSessionsFromDB();
                }
            }
        });

        return panel;
    }
    
    public Object getCellEditorValue() {
        if (isPushed) {
            int selectedRow = submissionsTable.getSelectedRow();
            int submissionId = (int) submissionsTableModel.getValueAt(selectedRow, 0);

            // 1. Get Evaluator ID from User
            String evalIdStr = JOptionPane.showInputDialog(CoordinatorPanel.this, 
                "Enter Evaluator ID for Submission #" + submissionId + ":");
            
            // 2. Get Session ID from User (Since you just made sessions like 'SESA')
            String sessIdStr = JOptionPane.showInputDialog(CoordinatorPanel.this, 
                "Enter Session ID (e.g., 1 for SESA):");

            if (evalIdStr != null && sessIdStr != null) {
                try {
                    int evalId = Integer.parseInt(evalIdStr);
                    int sessId = Integer.parseInt(sessIdStr);

                    AssignmentDAO assignDao = new AssignmentDAO();
                    // 3. Save to session_assignments table
                    if (assignDao.assignToEvaluator(sessId, submissionId, evalId)) {
                        // 4. Update status in submissions table to 'ASSIGNED'
                        new SubmissionDAO().updateSubmissionStatus(submissionId, "ASSIGNED");
                        JOptionPane.showMessageDialog(CoordinatorPanel.this, "Successfully Assigned!");
                        loadSubmissionsFromDB(); // Refresh UI
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(CoordinatorPanel.this, "Error: Use numbers for IDs.");
                }
            }
        }
        isPushed = false;
        return "Review";
    }

    private JPanel createSubmissionReviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Submission Review");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Filter by:"));
        filterPanel.add(new JComboBox<>(new String[]{"All", "Pending", "Approved", "Rejected"}));
        filterPanel.add(createActionButton("Apply Filter", new Color(52, 152, 219)));
        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        northPanel.add(title, BorderLayout.WEST);
        northPanel.add(filterPanel, BorderLayout.EAST);
        panel.add(northPanel, BorderLayout.NORTH);
        
        // INTEGRATED DATABASE DATA
        String[] columnNames = {"ID", "Student", "Title", "Type", "Supervisor", "Status", "Action"};
        submissionsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int col) { return col == 6; }
        };
        
        loadSubmissionsFromDB(); // Helper to refresh data
        
        submissionsTable = new JTable(submissionsTableModel);
        submissionsTable.setRowHeight(35);
        submissionsTable.getTableHeader().setBackground(new Color(41, 128, 185));
        submissionsTable.getTableHeader().setForeground(Color.WHITE);
        
        // Custom Renderer and Editor for the "Action" button
        submissionsTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        submissionsTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        panel.add(new JScrollPane(submissionsTable), BorderLayout.CENTER);

        deleteSessionBtn.addActionListener(e -> {
            int selectedRow = sessionsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a session to delete.");
                return;
            }

            String sesIdStr = (String) sessionsTableModel.getValueAt(selectedRow, 0);
            int sessionId = Integer.parseInt(sesIdStr.replace("SES-", ""));

            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete " + sesIdStr + "?\nAll assignments to this session will also be removed.", 
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (new SessionDAO().deleteSession(sessionId)) {
                    JOptionPane.showMessageDialog(this, "Session deleted successfully.");
                    loadSessionsFromDB(); // Refresh the table
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting session.");
                }
            }
        });
        return panel;
    }

    public void loadSessionsFromDB() {
        if (sessionsTableModel != null) {
            sessionsTableModel.setRowCount(0);
            List<Object[]> sessions = new SessionDAO().getAllSessions();
            for (Object[] row : sessions) sessionsTableModel.addRow(row);
        }
    } 

    // public as needed for refreshing in mainframe
    public void loadSubmissionsFromDB() {
        if (submissionsTableModel != null) {
            submissionsTableModel.setRowCount(0);
            List<Object[]> submissions = new SubmissionDAO().getAllSubmissions();
            for (Object[] row : submissions) {
                Object[] tableRow = new Object[]{
                    row[0],               
                    "Student " + row[0],  
                    row[1],               
                    row[4],               
                    row[3],               
                    row[6],               
                    "Review"             
                };
                submissionsTableModel.addRow(tableRow);
            }
        }
    }
    
    private DefaultTableModel scheduleTableModel;

    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Generated Seminar Schedule");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        String[] cols = {"Session", "Date/Time", "Venue", "Presentation Title"};
        scheduleTableModel = new DefaultTableModel(cols, 0);
        JTable scheduleTable = new JTable(scheduleTableModel);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(scheduleTable), BorderLayout.CENTER);
        
        // Add a refresh button to "Generate" the schedule
        JButton generateBtn = createActionButton("Generate/Refresh Schedule", new Color(46, 204, 113));
        generateBtn.addActionListener(e -> loadScheduleFromDB());
        panel.add(generateBtn, BorderLayout.SOUTH);

        return panel;
    }

    public void loadScheduleFromDB() {
        scheduleTableModel.setRowCount(0);
        List<Object[]> data = new SessionDAO().getFullSchedule();
        for (Object[] row : data) {
            scheduleTableModel.addRow(row);
        }
    }
    
    private JPanel createAwardsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Award Nomination & Evaluation Results");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        JPanel awardsPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        awardsPanel.setBackground(Color.WHITE);
        awardsPanel.add(createAwardCard("🏆 Best Oral", "Top Oral Presentation", new Color(255, 193, 7), "John Doe (9.2/10)"));
        awardsPanel.add(createAwardCard("📊 Best Poster", "Top Poster Presentation", new Color(33, 150, 243), "Jane Smith (9.5/10)"));
        awardsPanel.add(createAwardCard("👥 People's Choice", "Attendee Vote", new Color(156, 39, 176), "Open"));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(awardsPanel, BorderLayout.CENTER);
        panel.add(createActionButton("Finalize Award Winners", new Color(46, 204, 113)), BorderLayout.SOUTH);
        
        return panel;
    }

    // --- INNER CLASSES AND UI HELPERS ---

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() { setOpaque(true); }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Review");
            setBackground(new Color(52, 152, 219));
            setForeground(Color.WHITE);
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                int selectedRow = submissionsTable.getSelectedRow();
                int subId = (int) submissionsTableModel.getValueAt(selectedRow, 0);

                // 1. Get Evaluator ID from user
                String evalIdStr = JOptionPane.showInputDialog(CoordinatorPanel.this, 
                    "Enter Evaluator ID for Submission #" + subId + ":");
                
                // 2. Get Session ID from user (e.g., 1 for 'SESA')
                String sessIdStr = JOptionPane.showInputDialog(CoordinatorPanel.this, 
                    "Enter Session ID for this presentation:");

                if (evalIdStr != null && sessIdStr != null) {
                    try {
                        int evalId = Integer.parseInt(evalIdStr);
                        int sessId = Integer.parseInt(sessIdStr);

                        AssignmentDAO assignDao = new AssignmentDAO();
                        // 3. Insert into session_assignments table
                        if (assignDao.assignToEvaluator(sessId, subId, evalId)) {
                            // 4. Update submission status to 'ASSIGNED'
                            new SubmissionDAO().updateSubmissionStatus(subId, "ASSIGNED");
                            JOptionPane.showMessageDialog(CoordinatorPanel.this, "Successfully Assigned!");
                            loadSubmissionsFromDB(); // Refresh table view
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(CoordinatorPanel.this, "Error: Use numeric IDs.");
                    }
                }
            }
            isPushed = false;
            return "Review";
        }
    }
    
    private JPanel createAwardCard(String name, String desc, Color color, String winner) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(color, 2), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        card.add(new JLabel(name), BorderLayout.NORTH);
        card.add(new JLabel(winner), BorderLayout.SOUTH);
        return card;
    }
    
    private JPanel createCoordStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(220, 220, 220), 1), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        card.add(new JLabel(title), BorderLayout.NORTH);
        JLabel val = new JLabel(value); val.setFont(new Font("Segoe UI", Font.BOLD, 24)); val.setForeground(color);
        card.add(val, BorderLayout.CENTER);
        return card;
    }
    
    private JPanel createScheduleCard(String day, String venue, String schedule) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(41, 128, 185), 1), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        card.add(new JLabel(day), BorderLayout.NORTH);
        card.add(new JTextArea(schedule), BorderLayout.CENTER);
        return card;
    }
    
    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(new RoundedBorder(6));
        return button;
    }
    
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel();
        footer.setBackground(new Color(41, 128, 185));
        footer.add(new JLabel("© 2024 Seminar Management System"));
        return footer;
    }
    
    class RoundedBorder implements Border {
        private int radius;
        RoundedBorder(int radius) { this.radius = radius; }
        public Insets getBorderInsets(Component c) { return new Insets(this.radius+1, this.radius+1, this.radius+1, this.radius+1); }
        public boolean isBorderOpaque() { return true; }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }
}