package dashboard;

import java.awt.*;
import javax.swing.*;
import model.User;

public class MainFrame extends JFrame {
    
    private User loggedInUser;
    
    private CardLayout cardLayout;
    private JPanel mainContainer;

    // No arguments in constructor
    public MainFrame() {
<<<<<<< HEAD
        setTitle("Seminar System (Prototype)");
        setSize(800, 600);
=======
        setTitle("Seminar Management System");
        setSize(1000, 800);
>>>>>>> ff01260c6e3ff9aa335d433518d5d24f13441535
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // Add the panels
        mainContainer.add(new LoginPanel(this), "LOGIN");
        mainContainer.add(new StudentPanel(this), "STUDENT");
        mainContainer.add(new CoordinatorPanel(this), "COORDINATOR");
        mainContainer.add(new EvaluatorPanel(this), "EVALUATOR");



        add(mainContainer);
    }

    public void switchScreen(String screenName) {
        cardLayout.show(mainContainer, screenName);
    }

    public void setLoggedInUser(User user) { 
        this.loggedInUser = user; 
    }
}
