package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import gui.job.JobBrowseWindow;
import gui.job.ApplicationWindow;

public class MainFrame extends JFrame {
    
    private String currentUser;
    private String currentRole;
    private JPanel contentPanel;
    private JLabel userInfoLabel;
    
    public static final int WINDOW_WIDTH = 900;
    public static final int WINDOW_HEIGHT = 650;
    
    public MainFrame() {
        this.currentUser = null;
        this.currentRole = null;
        initialize();
    }
    
    private void initialize() {
        setTitle("EBU6304 Group63 - TA Recruitment System V2");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        
        showWelcomePanel();
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(51, 102, 153));
        panel.setPreferredSize(new Dimension(WINDOW_WIDTH, 60));
        
        JLabel titleLabel = new JLabel("TA Recruitment System V2");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panel.add(titleLabel, BorderLayout.WEST);
        
        userInfoLabel = new JLabel("Not Logged In");
        userInfoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userInfoLabel.setForeground(Color.WHITE);
        userInfoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        panel.add(userInfoLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(240, 240, 240));
        panel.setPreferredSize(new Dimension(WINDOW_WIDTH, 50));
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.setBackground(new Color(204, 51, 51));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setOpaque(true);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        logoutButton.setEnabled(false);
        
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 14));
        exitButton.setPreferredSize(new Dimension(100, 35));
        exitButton.setBackground(new Color(102, 102, 102));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false);
        exitButton.setOpaque(true);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        panel.add(logoutButton);
        panel.add(exitButton);
        
        return panel;
    }
    
    public void showWelcomePanel() {
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JLabel titleLabel = new JLabel("EBU6304 Group63", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(51, 102, 153));
        welcomePanel.add(titleLabel, BorderLayout.NORTH);
        
        JLabel subtitleLabel = new JLabel("TA Recruitment System V2", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        subtitleLabel.setForeground(new Color(102, 102, 102));
        welcomePanel.add(subtitleLabel, BorderLayout.CENTER);
        
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        
        JLabel noteLabel = new JLabel("<html><center>Waiting for Member 1 (ZhuangZuchen) to implement LoginWindow...</center></html>", SwingConstants.CENTER);
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        noteLabel.setForeground(new Color(153, 153, 153));
        infoPanel.add(noteLabel);
        
        welcomePanel.add(infoPanel, BorderLayout.SOUTH);
        
        setContentPanel(welcomePanel);
    }
    
    public void setCurrentUser(String userId, String role) {
        this.currentUser = userId;
        this.currentRole = role;
        updateUserInfo();

        // Show TA menu when role is TA
        if ("TA".equals(role)) {
           showTAMenu();
        }
    }

    public void showTAMenu() {
    JPanel taPanel = new JPanel(new GridLayout(2, 1, 10, 10));
    taPanel.setBackground(Color.WHITE);
    taPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    
    JButton browseJobsBtn = new JButton("Browse Jobs");
    JButton myApplicationsBtn = new JButton("My Applications");
    
    browseJobsBtn.setFont(new Font("Arial", Font.BOLD, 18));
    myApplicationsBtn.setFont(new Font("Arial", Font.BOLD, 18));
    
    browseJobsBtn.addActionListener(e -> {
        gui.job.JobBrowseWindow browseWindow = new gui.job.JobBrowseWindow(currentUser);
        browseWindow.setVisible(true);
    });
    
    myApplicationsBtn.addActionListener(e -> {
        gui.job.ApplicationWindow appWindow = new gui.job.ApplicationWindow(currentUser);
        appWindow.setVisible(true);
    });
    
    taPanel.add(browseJobsBtn);
    taPanel.add(myApplicationsBtn);
    
    setContentPanel(taPanel);
}
    
    public void updateUserInfo() {
        if (currentUser != null && currentRole != null) {
            userInfoLabel.setText("User: " + currentUser + " | Role: " + currentRole);
        } else {
            userInfoLabel.setText("Not Logged In");
        }
    }
    
    public void logout() {
        this.currentUser = null;
        this.currentRole = null;
        updateUserInfo();
        showWelcomePanel();
    }
    
    public String getCurrentUser() {
        return currentUser;
    }
    
    public String getCurrentRole() {
        return currentRole;
    }
    
    public void setContentPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}
