package dashboard;

import java.awt.*;
import javax.swing.*;
import model.User;

public class MainFrame extends JFrame {
    
    private User loggedInUser;
    private CardLayout cardLayout;
    private JPanel mainContainer;

    // References to your panels so we can trigger refreshes
    private EvaluatorPanel evaluatorPanel;
    private CoordinatorPanel coordinatorPanel;
    private StudentPanel studentPanel;

    public MainFrame() {
        setTitle("Seminar Management System");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // Initialize your specific panels
        evaluatorPanel = new EvaluatorPanel(this);
        coordinatorPanel = new CoordinatorPanel(this);
        studentPanel = new StudentPanel(this);

        // Add them to the container with their string keys
        mainContainer.add(new LoginPanel(this), "LOGIN");
        mainContainer.add(studentPanel, "STUDENT");
        mainContainer.add(coordinatorPanel, "COORDINATOR");
        mainContainer.add(evaluatorPanel, "EVALUATOR");

        add(mainContainer);
    }

    public void switchScreen(String screenName) {
        // refresh data
        switch (screenName) {
            case "EVALUATOR":
                if (loggedInUser != null) {
                    evaluatorPanel.setEvaluatorId(loggedInUser.getId()); // Pass the real ID
                    evaluatorPanel.loadAssignments(); // Refresh the table
                }
                break;
            case "COORDINATOR":
                coordinatorPanel.loadSubmissionsFromDB(); 
                break;
            case "STUDENT":
                // This is the bridge that prevents the ID 0 error
                if (loggedInUser != null) {
                    studentPanel.setStudentId(loggedInUser.getId()); 
                    studentPanel.loadMySubmissions(); // This ensures the student sees their own list
                }
                break;
        }
        
        // Show the requested screen
        cardLayout.show(mainContainer, screenName);
    }

    public void setLoggedInUser(User user) { 
        this.loggedInUser = user; 
    }
    
    public User getLoggedInUser() {
        return loggedInUser;
    }
}