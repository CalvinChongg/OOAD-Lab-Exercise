package dashboard;

import dao.AssignmentDAO;
import dao.ReportDAO;
import dao.SessionDAO;
import dao.SubmissionDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class CoordinatorPanel extends JPanel {
    private JPanel awardsCardsPanel;
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

        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.getSelectedIndex();
            if (index == 0) {
                refreshDashboardStats(); // Refresh Dashboard
            } else if (index == 1) {
                loadSessionsFromDB();
            } else if (index == 2) {
                    loadSubmissionsFromDB();
            } else if (index == 4) {
                loadAwardsFromDB(); 
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
    
    private JLabel totalSubLabel, oralSubLabel, posterSubLabel, sessionsLabel;

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Header Overview Card
        JPanel overviewCard = new JPanel(new BorderLayout(10, 10));
        overviewCard.setBackground(new Color(41, 128, 185));
        overviewCard.setBorder(new CompoundBorder(new LineBorder(new Color(32, 102, 148), 2), BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        
        JLabel overviewTitle = new JLabel("Seminar Overview");
        overviewTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        overviewTitle.setForeground(Color.WHITE);
        
        JTextArea overviewText = new JTextArea("Welcome, Coordinator! This dashboard provides real-time tracking of seminar submissions and active sessions.");
        overviewText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        overviewText.setForeground(Color.WHITE);
        overviewText.setEditable(false);
        overviewText.setOpaque(false);
        
        overviewCard.add(overviewTitle, BorderLayout.NORTH);
        overviewCard.add(overviewText, BorderLayout.CENTER);
        panel.add(overviewCard, BorderLayout.NORTH);
        
        // Stats Grid (Total, Oral, Poster, Sessions)
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 15)); // Changed to 1 row
        statsPanel.setBackground(Color.WHITE);
        
        // Initialize labels as class variables
        totalSubLabel = new JLabel("0");
        oralSubLabel = new JLabel("0");
        posterSubLabel = new JLabel("0");
        sessionsLabel = new JLabel("0");

        statsPanel.add(createCoordStatCard("Total Submissions", totalSubLabel, new Color(41, 128, 185)));
        statsPanel.add(createCoordStatCard("Oral Presentations", oralSubLabel, new Color(52, 152, 219)));
        statsPanel.add(createCoordStatCard("Poster Presentations", posterSubLabel, new Color(46, 204, 113)));
        statsPanel.add(createCoordStatCard("Sessions Created", sessionsLabel, new Color(155, 89, 182)));
        
        panel.add(statsPanel, BorderLayout.CENTER);
        
        // Initial data load
        refreshDashboardStats();
        
        return panel;
    }

    // Updated Helper to accept JLabel instead of String for dynamic updates
    private JPanel createCoordStatCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(220, 220, 220), 1), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(color);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    public void refreshDashboardStats() {
        SubmissionDAO subDao = new SubmissionDAO();
        SessionDAO sessDao = new SessionDAO();
        
        totalSubLabel.setText(String.valueOf(subDao.getTotalSubmissionCount()));
        oralSubLabel.setText(String.valueOf(subDao.getSubmissionCountByType("ORAL")));
        posterSubLabel.setText(String.valueOf(subDao.getSubmissionCountByType("POSTER")));
        sessionsLabel.setText(String.valueOf(sessDao.getTotalSessionCount()));
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
        loadSessionsFromDB(); // Load data
        
        panel.add(new JScrollPane(sessionsTable), BorderLayout.CENTER);

        // CREATE SESSION ACTION
        createSessionBtn.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField dateField = new JTextField("2026-05-15");
            JTextField timeField = new JTextField("09:00-10:00");
            JComboBox<String> typeBox = new JComboBox<>(new String[]{"ORAL", "POSTER"});
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
                if (new SessionDAO().createSession(nameField.getText(), dateField.getText(), timeField.getText(), (String)typeBox.getSelectedItem(), venueField.getText())) {
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

            String evalIdStr = JOptionPane.showInputDialog(CoordinatorPanel.this, 
                "Enter Evaluator ID for Submission #" + submissionId + ":");
            
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
    
        String[] columnNames = {"ID", "Student", "Title", "Type", "Supervisor", "Status", "Action"};
        submissionsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int col) { return col == 6; }
        };
        
        loadSubmissionsFromDB(); //refresh data
        
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
                    loadSessionsFromDB();
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
                // Manually fetch the username based on the student_id (row[1])
                int studentId = (int) row[1];
                String actualUsername = getUsernameById(studentId); 

                Object[] tableRow = new Object[]{
                    row[0],             // Column 0: Submission ID
                    actualUsername,     // Column 1: Username
                    row[2],             // Column 2: Research Title
                    row[3],             // Column 3: Presentation Type
                    row[4],             // Column 4: Supervisor Name
                    row[5],             // Column 5: Status
                    "Review"            // Column 6: Action Button
                };
                submissionsTableModel.addRow(tableRow);
            }
        }
    }

    private String getUsernameById(int id) {
        String username = "Unknown";
        String sql = "SELECT username FROM users WHERE id = ?";
        try (java.sql.Connection conn = database.SQLiteConnection.connect();
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            java.sql.ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                username = rs.getString("username");
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return username;
    }

    private void generatePDFReport(JTable table, String reportTitle) {
        try {
            // Selecting "Microsoft Print to PDF" or "Save as PDF" generates the file
            boolean complete = table.print(JTable.PrintMode.FIT_WIDTH, 
                new java.text.MessageFormat(reportTitle), 
                new java.text.MessageFormat("Page {0}"));
            
            if (complete) {
                JOptionPane.showMessageDialog(this, reportTitle + " Generated Successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (java.awt.print.PrinterException e) {
            JOptionPane.showMessageDialog(this, "Error generating PDF: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
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
        
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonContainer.setOpaque(false);

        JButton generateBtn = createActionButton("Generate/Refresh Schedule", new Color(46, 204, 113));
        generateBtn.addActionListener(e -> loadScheduleFromDB());
        
        JButton printScheduleBtn = createActionButton("Generate Schedule PDF", new Color(41, 128, 185));
        printScheduleBtn.addActionListener(e -> generatePDFReport(scheduleTable, "Seminar Schedule 2026"));

        buttonContainer.add(generateBtn);
        buttonContainer.add(printScheduleBtn);
        
        panel.add(buttonContainer, BorderLayout.SOUTH);

        return panel;
    }

    public void loadScheduleFromDB() {
        scheduleTableModel.setRowCount(0);
        List<Object[]> data = new SessionDAO().getFullSchedule();
        for (Object[] row : data) {
            scheduleTableModel.addRow(row);
        }
    }
    
    public void loadAwardsFromDB() {
        ReportDAO dao = new ReportDAO();
        
        // 1. Update the Leaderboard Table
        awardsTableModel.setRowCount(0);
        List<Object[]> leaderboard = dao.getLeaderboard();
        for (Object[] row : leaderboard) {
            awardsTableModel.addRow(row);
        }
        
        String oralWinner = dao.getWinner("Oral");
        String posterWinner = dao.getWinner("Poster");
        
        revalidate();
        repaint();
    }

    private void updateAwardCards() {
        ReportDAO dao = new ReportDAO();
        
        String oralWinner = dao.getFinalizedWinner("BEST_ORAL");
        String posterWinner = dao.getFinalizedWinner("BEST_POSTER");
        
        if (awardsCardsPanel != null) {
            awardsCardsPanel.removeAll();
            
            awardsCardsPanel.add(createAwardCard("🏆 Best Oral", "Top Oral Presentation", new Color(255, 193, 7), oralWinner));
            awardsCardsPanel.add(createAwardCard("📊 Best Poster", "Top Poster Presentation", new Color(33, 150, 243), posterWinner));
            awardsCardsPanel.add(createAwardCard("👥 People's Choice", "Attendee Vote", new Color(156, 39, 176), "Open"));
            
            awardsCardsPanel.revalidate();
            awardsCardsPanel.repaint();
        }
    }

    private JPanel createAwardsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Award Nomination & Evaluation Results");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        // Top Section: Visual Cards
        awardsCardsPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        awardsCardsPanel.setBackground(Color.WHITE);
        awardsCardsPanel.add(createAwardCard("🏆 Best Oral", "Top Oral Presentation", new Color(255, 193, 7), "TBD"));
        awardsCardsPanel.add(createAwardCard("📊 Best Poster", "Top Poster Presentation", new Color(33, 150, 243), "TBD"));
        awardsCardsPanel.add(createAwardCard("👥 People's Choice", "Attendee Vote", new Color(156, 39, 176), "Open"));
        
        // Middle Section: Table of all results (Best for PDF Generation)
        String[] cols = {"Rank", "Submission Title", "Student", "Type", "Score / 10"};
        awardsTableModel = new DefaultTableModel(cols, 0);
        awardsTable = new JTable(awardsTableModel);
        JScrollPane tableScroll = new JScrollPane(awardsTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Full Evaluation Leaderboard"));

        // Layout Assembly
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(awardsCardsPanel, BorderLayout.NORTH);
        centerPanel.add(tableScroll, BorderLayout.CENTER);

        panel.add(title, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        // Bottom Section: Centered Buttons
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonContainer.setOpaque(false);

        JButton finalizeBtn = createActionButton("Finalize Winners", new Color(46, 204, 113));
        finalizeBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Do you want to permanently save these winners to the database?", 
                "Confirm Finalization", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                if (new ReportDAO().finalizeAwards()) {
                    JOptionPane.showMessageDialog(this, "Awards saved to database successfully!");
                    loadAwardsFromDB(); // Refresh UI to show the new winners in the cards
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Could not save awards. Check if winners exist.");
                }
            }

            if (new ReportDAO().finalizeAwards()) {
                JOptionPane.showMessageDialog(this, "Awards finalized!");
                updateAwardCards(); 
            }
        });

        JButton printAwardsBtn = createActionButton("Generate Awards PDF", new Color(41, 128, 185));
        printAwardsBtn.addActionListener(e -> generatePDFReport(awardsTable, "Seminar Evaluation Results 2026"));

        buttonContainer.add(finalizeBtn);
        buttonContainer.add(printAwardsBtn);
        panel.add(buttonContainer, BorderLayout.SOUTH);
        
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
                if (selectedRow == -1) return "Review";

                int subId = (int) submissionsTableModel.getValueAt(selectedRow, 0);
                
                // 1. Fetch data for dropdowns
                AssignmentDAO assignDao = new AssignmentDAO();
                java.util.List<String> evaluators = assignDao.getAllEvaluatorNames();
                List<String> sessions = new SessionDAO().getAllSessionTitles();

                // 2. Create the Dropdowns
                JComboBox<String> evaluatorBox = new JComboBox<>(evaluators.toArray(new String[0]));
                JComboBox<String> sessionBox = new JComboBox<>(sessions.toArray(new String[0]));

                // 3. Design the Dialog Layout
                JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
                panel.add(new JLabel("Assign Evaluator for Submission #" + subId));
                panel.add(evaluatorBox);
                panel.add(new JLabel("Assign to Session:"));
                panel.add(sessionBox);

                int result = JOptionPane.showConfirmDialog(CoordinatorPanel.this, panel, 
                    "Assign Presentation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String selectedSess = (String) sessionBox.getSelectedItem();
                    String selected = (String) evaluatorBox.getSelectedItem();
                    
                    int evalId = Integer.parseInt(selected.split(" - ")[0]);
                    int sessId = new SessionDAO().getSessionIdByName(selectedSess);

                    if (evalId != -1 && sessId != -1) {
                        // Save the link in the session_assignments table
                        if (assignDao.assignToEvaluator(sessId, subId, evalId)) {
                            // Update the UI status to 'ASSIGNED'
                            new SubmissionDAO().updateSubmissionStatus(subId, "ASSIGNED");
                            JOptionPane.showMessageDialog(CoordinatorPanel.this, "Assigned successfully!");
                            loadSubmissionsFromDB(); 
                        }
                    } else {
                        JOptionPane.showMessageDialog(CoordinatorPanel.this, "Error: Could not retrieve database IDs.");
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