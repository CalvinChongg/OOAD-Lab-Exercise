package dashboard;

import dao.SubmissionDAO;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class StudentPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField titleField, supervisorField;
    private JTextArea abstractArea;
    private JComboBox<String> typeCombo;
    private JTextField filePathField;
    private JButton browseBtn, submitBtn, logoutBtn, viewBtn, refreshBtn, downloadBtn;
    private JTable submissionsTable;
    private DefaultTableModel tableModel;
    
    public StudentPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 245, 250));
        
<<<<<<< HEAD
        // Header Panel 
        //test git
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel welcome = new JLabel("Student Dashboard - Presentation Submission");
        welcome.setFont(new Font("Arial", Font.BOLD, 20));
        welcome.setForeground(Color.WHITE);
        headerPanel.add(welcome, BorderLayout.WEST);
        
        logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> mainFrame.switchScreen("LOGIN"));
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        
=======
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
>>>>>>> register
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content Panel with Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Tab 1: Submission Form
        tabbedPane.addTab("Submit Presentation", createSubmissionPanel());
        
        // Tab 2: View Submissions
        tabbedPane.addTab("My Submissions", createViewSubmissionsPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Footer
        add(createFooterPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        
        // Left side: Title and welcome message
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setOpaque(false);
        
        JLabel title = new JLabel("Student Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        
        JLabel subtitle = new JLabel("Presentation Submission Portal");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(200, 200, 200));
        
        leftPanel.add(title);
        leftPanel.add(subtitle);
        
        // Right side: Logout button (MATCHING COORDINATOR STYLE)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        
        logoutBtn = new JButton("LOGOUT");
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
                StudentPanel.this,
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
    
    private JPanel createSubmissionPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Form Panel with Card Layout
        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        formCard.setBackground(new Color(250, 250, 252));
        
        // Form Title
        JLabel formTitle = new JLabel("Submit New Presentation");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(new Color(41, 128, 185));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(20));
        
        // Form Fields
        formCard.add(createFormField("Research Title", titleField = new JTextField(30), true));
        formCard.add(Box.createVerticalStrut(15));
        
        formCard.add(createLabel("Abstract *"));
        abstractArea = new JTextArea(6, 30);
        abstractArea.setLineWrap(true);
        abstractArea.setWrapStyleWord(true);
        JScrollPane abstractScroll = new JScrollPane(abstractArea);
        abstractScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(abstractScroll);
        formCard.add(Box.createVerticalStrut(15));
        
        formCard.add(createFormField("Supervisor", supervisorField = new JTextField(30), false));
        formCard.add(Box.createVerticalStrut(15));
        
        // Presentation Type
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        typePanel.setOpaque(false);
        typePanel.add(createLabel("Presentation Type *"));
        typePanel.add(Box.createHorizontalStrut(10));
        typeCombo = new JComboBox<>(new String[]{"ORAL", "POSTER"});
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typePanel.add(typeCombo);
        typePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(typePanel);
        formCard.add(Box.createVerticalStrut(15));
        
        // File Upload
        JPanel filePanel = new JPanel(new BorderLayout(5, 0));
        filePanel.setOpaque(false);
        filePanel.add(createLabel("Presentation File (Optional)"), BorderLayout.WEST);
        filePanel.add(Box.createHorizontalStrut(10), BorderLayout.CENTER);
        
        JPanel fileInputPanel = new JPanel(new BorderLayout(5, 0));
        filePathField = new JTextField();
        filePathField.setEditable(false);
        browseBtn = new JButton("Browse...");
        browseBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        browseBtn.addActionListener(e -> browseFile());
        fileInputPanel.add(filePathField, BorderLayout.CENTER);
        fileInputPanel.add(browseBtn, BorderLayout.EAST);
        filePanel.add(fileInputPanel, BorderLayout.EAST);
        filePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(filePanel);
        formCard.add(Box.createVerticalStrut(25));
        
        // Submit Button
        submitBtn = new JButton("Submit Presentation");
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitBtn.setBackground(new Color(46, 204, 113));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setBorder(new RoundedBorder(8));
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.setPreferredSize(new Dimension(200, 40));
        submitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitBtn.addActionListener(this::submitPresentation);
        
        formCard.add(submitBtn);
        
        panel.add(formCard, BorderLayout.CENTER);
        
        // Instructions Panel
        panel.add(createInstructionsPanel(), BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createViewSubmissionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Header for submissions panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("My Submission History");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(52, 73, 94));
        
        refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorder(new RoundedBorder(6));
        refreshBtn.addActionListener(this::loadSubmissions);
        
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(refreshBtn, BorderLayout.EAST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Table for submissions
        String[] columnNames = {"ID", "Title", "Type", "Supervisor", "Status", "Submission Date", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only Actions column is editable
            }
        };
        
        submissionsTable = new JTable(tableModel);
        submissionsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        submissionsTable.setRowHeight(30);
        submissionsTable.setSelectionBackground(new Color(220, 237, 200));
        submissionsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        submissionsTable.getTableHeader().setBackground(new Color(52, 73, 94));
        submissionsTable.getTableHeader().setForeground(Color.WHITE);
        
        // Center align text in columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columnNames.length; i++) {
            if (i != 1) { // Title column left aligned
                submissionsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        // Custom renderer for status column
        submissionsTable.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
        
        // Add action buttons in the Actions column
        submissionsTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        submissionsTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(submissionsTable);
        scrollPane.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Statistics Panel
        panel.add(createStatisticsPanel(), BorderLayout.SOUTH);
        
        // Load initial data
        loadSubmissions(null);
        
        return panel;
    }
    
    private void loadSubmissions(ActionEvent e) {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // In real implementation, get actual student ID from logged-in user
        int studentId = 1;
        
        SubmissionDAO dao = new SubmissionDAO();
        var submissions = dao.getSubmissionsByStudent(studentId);
        
        if (submissions.isEmpty()) {
            // Show empty state
            Object[] row = {"", "No submissions found", "", "", "", "", ""};
            tableModel.addRow(row);
            
            // Disable the status column for empty row
            submissionsTable.setEnabled(false);
        } else {
            // Enable table
            submissionsTable.setEnabled(true);
            
            // Add submissions to table
            for (Object[] rowData : submissions) {
                // rowData: [id, title, type, supervisor, status, submissionDate]
                Object[] row = new Object[7];
                row[0] = rowData[0]; // ID
                row[1] = rowData[1]; // Title
                row[2] = rowData[2]; // Type
                row[3] = rowData[3]; // Supervisor
                row[4] = rowData[4]; // Status
                row[5] = rowData[5]; // Submission Date
                row[6] = "View";
                
                tableModel.addRow(row);
            }
        }
    }
    
    private JPanel createFormField(String label, JTextField field, boolean required) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        
        JLabel jLabel = createLabel(label + (required ? " *" : ""));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 30));
        
        panel.add(jLabel);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(field);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        return panel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }
    
    private JPanel createInstructionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            "Submission Guidelines",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(52, 73, 94)
        ));
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
            panel.getBorder(),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JTextArea instructions = new JTextArea();
        instructions.setEditable(false);
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);
        instructions.setText(String.join("\n• ",
            "",
            "• Fill all required fields marked with *",
            "• Abstract should be 150-300 words",
            "• Supported file formats: PDF, PPT, PPTX",
            "• Oral presentations: 15 minutes + 5 minutes Q&A",
            "• Poster size: A1 (594mm x 841mm) portrait",
            "• Submission deadline: Check seminar schedule",
            "• You can edit your submission before deadline"
        ));
        instructions.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        instructions.setForeground(new Color(100, 100, 100));
        instructions.setBackground(new Color(248, 249, 250));
        
        panel.add(instructions, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Statistics cards
        panel.add(createStatCard("Total Submissions", "3", new Color(52, 152, 219)));
        panel.add(createStatCard("Approved", "2", new Color(46, 204, 113)));
        panel.add(createStatCard("Pending", "1", new Color(230, 126, 34)));
        panel.add(createStatCard("Upcoming", "1", new Color(155, 89, 182)));
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        card.setPreferredSize(new Dimension(150, 70));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(100, 100, 100));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(52, 73, 94));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel footerText = new JLabel(
            "© 2024 Seminar Management System - Student Module v1.0",
            SwingConstants.CENTER
        );
        footerText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerText.setForeground(new Color(200, 200, 200));
        
        footer.add(footerText, BorderLayout.CENTER);
        return footer;
    }
    
    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Presentation Files (*.pdf, *.ppt, *.pptx, *.doc, *.docx)", 
            "pdf", "ppt", "pptx", "doc", "docx"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            filePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }
    
    private void submitPresentation(ActionEvent e) {
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter research title", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (abstractArea.getText().trim().isEmpty() || abstractArea.getText().trim().length() < 50) {
            JOptionPane.showMessageDialog(this, "Abstract should be at least 50 characters", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // In real implementation, get actual student ID from logged-in user
        // For now, use a dummy ID (student1 has ID=5 in seeded data)
        int studentId = 1;
        
        SubmissionDAO dao = new SubmissionDAO();
        boolean success = dao.addSubmission(
            studentId,
            titleField.getText().trim(),
            abstractArea.getText().trim(),
            supervisorField.getText().trim(),
            (String) typeCombo.getSelectedItem(),
            filePathField.getText().trim()
        );
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Presentation submitted successfully!\n\n" +
                "Title: " + titleField.getText() + "\n" +
                "Type: " + typeCombo.getSelectedItem() + "\n" +
                "Status: Submitted for review",
                "Submission Successful",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear form
            titleField.setText("");
            abstractArea.setText("");
            supervisorField.setText("");
            filePathField.setText("");
            typeCombo.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to submit presentation. Please try again.",
                "Submission Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Custom Border for rounded buttons
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
    
    // Custom cell renderer for status
    class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            JLabel label = (JLabel) c;
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setOpaque(true);
            
            if (value == null || value.toString().isEmpty()) {
                return c;
            }
            
            String status = value.toString();
            switch (status.toUpperCase()) {
                case "APPROVED":
                    label.setBackground(new Color(220, 237, 200));
                    label.setForeground(new Color(60, 118, 61));
                    label.setText("Approved");
                    break;
                case "SUBMITTED":
                    label.setBackground(new Color(252, 248, 227));
                    label.setForeground(new Color(138, 109, 59));
                    label.setText("Pending");
                    break;
                case "PENDING":
                    label.setBackground(new Color(252, 248, 227));
                    label.setForeground(new Color(138, 109, 59));
                    label.setText("Pending");
                    break;
                case "REJECTED":
                    label.setBackground(new Color(242, 222, 222));
                    label.setForeground(new Color(169, 68, 66));
                    label.setText("Rejected");
                    break;
                case "REVIEWING":
                    label.setBackground(new Color(217, 237, 247));
                    label.setForeground(new Color(49, 112, 143));
                    label.setText("Reviewing");
                    break;
                default:
                    label.setBackground(Color.WHITE);
                    label.setForeground(Color.BLACK);
            }
            
            label.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(2, 10, 2, 10)
            ));
            return label;
        }
    }
    
    // Button renderer for table actions
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            setFont(new Font("Segoe UI", Font.PLAIN, 12));
            setBackground(new Color(52, 152, 219));
            setForeground(Color.WHITE);
            setBorder(new RoundedBorder(4));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            return this;
        }
    }
    
    // Button editor for table actions
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }
        
        public Object getCellEditorValue() {
            if (isPushed) {
                // Show detailed view of the submission
                int selectedRow = submissionsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String title = (String) tableModel.getValueAt(selectedRow, 1);
                    String type = (String) tableModel.getValueAt(selectedRow, 2);
                    String status = (String) tableModel.getValueAt(selectedRow, 4);
                    
                    showSubmissionDetails(title, type, status);
                }
            }
            isPushed = false;
            return label;
        }
        
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
        
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
    
    private void showSubmissionDetails(String title, String type, String status) {
        JDialog dialog = new JDialog((Frame) null, "Submission Details", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel header = new JLabel("Submission Details", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setForeground(new Color(52, 73, 94));
        
        // Details panel
        JPanel detailsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        addDetailRow(detailsPanel, "Title:", title);
        addDetailRow(detailsPanel, "Type:", type);
        addDetailRow(detailsPanel, "Status:", status);
        addDetailRow(detailsPanel, "Submitted:", "2024-03-15");
        
        // Close button
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        closeBtn.setBackground(new Color(52, 152, 219));
        closeBtn.setForeground(Color.WHITE);
        
        content.add(header, BorderLayout.NORTH);
        content.add(detailsPanel, BorderLayout.CENTER);
        content.add(closeBtn, BorderLayout.SOUTH);
        
        dialog.add(content);
        dialog.setVisible(true);
    }
    
    private void addDetailRow(JPanel panel, String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lbl);
        
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(val);
    }
}