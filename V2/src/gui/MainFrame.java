package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import gui.auth.LoginWindow;
import gui.auth.RegisterWindow;
import gui.job.JobBrowseWindow;
import gui.job.ApplicationWindow;
import gui.position.PublishJobWindow;
import gui.position.MOWorkbenchWindow;
import gui.module.ModuleManageWindow;
import gui.module.WorkloadWindow;
import gui.user.ProfileWindow;
import gui.user.CVUploadWindow;

public class MainFrame extends JFrame {
    
    private String currentUser;
    private String currentRole;
    private JPanel contentPanel;
    private JLabel userInfoLabel;
    private JButton logoutButton;
    
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
        
        logoutButton = new JButton("Logout");
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
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        JLabel titleLabel = new JLabel("EBU6304 Group63", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(51, 102, 153));
        welcomePanel.add(titleLabel, BorderLayout.NORTH);
        
        JLabel subtitleLabel = new JLabel("TA Recruitment System V2", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        subtitleLabel.setForeground(new Color(102, 102, 102));
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(subtitleLabel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 40));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Arial", Font.BOLD, 16));
        loginBtn.setPreferredSize(new Dimension(150, 45));
        loginBtn.setBackground(new Color(51, 102, 153));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setOpaque(true);
        loginBtn.addActionListener(e -> {
            LoginWindow loginWindow = new LoginWindow(this);
            loginWindow.setVisible(true);
        });
        buttonPanel.add(loginBtn);
        
        JButton registerBtn = new JButton("Register");
        registerBtn.setFont(new Font("Arial", Font.BOLD, 16));
        registerBtn.setPreferredSize(new Dimension(150, 45));
        registerBtn.setBackground(new Color(102, 153, 51));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setBorderPainted(false);
        registerBtn.setOpaque(true);
        registerBtn.addActionListener(e -> {
            RegisterWindow registerWindow = new RegisterWindow(this);
            registerWindow.setVisible(true);
        });
        buttonPanel.add(registerBtn);
        
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        welcomePanel.add(centerPanel, BorderLayout.CENTER);
        
        JLabel noteLabel = new JLabel("Please login to access the system", SwingConstants.CENTER);
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        noteLabel.setForeground(new Color(153, 153, 153));
        noteLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        welcomePanel.add(noteLabel, BorderLayout.SOUTH);
        
        setContentPanel(welcomePanel);
    }
    
    public void setCurrentUser(String userId, String role) {
        this.currentUser = userId;
        this.currentRole = role;
        logoutButton.setEnabled(true);
        updateUserInfo();

        if ("TA".equals(role)) {
           showTAMenu();
        } else if ("MO".equals(role)) {
            showMOMenu();
        } else if ("ADMIN".equals(role)) {
            showAdminMenu();
        }
    }

    public void showTAMenu() {
        JPanel taPanel = new JPanel(new BorderLayout(10, 10));
        taPanel.setBackground(Color.WHITE);
        taPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        JLabel titleLabel = new JLabel("TA Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 102, 153));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton profileBtn = createMenuButton("My Profile");
        profileBtn.addActionListener(e -> {
            ProfileWindow profileWindow = new ProfileWindow(this, currentUser);
            setContentPanel(profileWindow);
        });
        
        JButton cvBtn = createMenuButton("Upload CV");
        cvBtn.addActionListener(e -> {
            CVUploadWindow cvWindow = new CVUploadWindow(this, currentUser);
            setContentPanel(cvWindow);
        });
        
        JButton browseBtn = createMenuButton("Browse Jobs");
        browseBtn.addActionListener(e -> {
            JobBrowseWindow browseWindow = new JobBrowseWindow(this, currentUser);
            setContentPanel(browseWindow);
        });
        
        JButton appBtn = createMenuButton("My Applications");
        appBtn.addActionListener(e -> {
            ApplicationWindow appWindow = new ApplicationWindow(this, currentUser);
            setContentPanel(appWindow);
        });
        
        buttonPanel.add(profileBtn);
        buttonPanel.add(cvBtn);
        buttonPanel.add(browseBtn);
        buttonPanel.add(appBtn);
        
        taPanel.add(titleLabel, BorderLayout.NORTH);
        taPanel.add(buttonPanel, BorderLayout.CENTER);
        
        setContentPanel(taPanel);
    }

    public void showMOMenu() {
        JPanel moPanel = new JPanel(new BorderLayout(10, 10));
        moPanel.setBackground(Color.WHITE);
        moPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        JLabel titleLabel = new JLabel("MO Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 102, 153));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton publishBtn = createMenuButton("Publish Job");
        publishBtn.addActionListener(e -> {
            PublishJobWindow publishWindow = new PublishJobWindow(this, currentUser);
            setContentPanel(publishWindow);
        });
        
        JButton manageBtn = createMenuButton("Manage Applications");
        manageBtn.addActionListener(e -> {
            MOWorkbenchWindow workbenchWindow = new MOWorkbenchWindow(this, currentUser);
            setContentPanel(workbenchWindow);
        });
        
        buttonPanel.add(publishBtn);
        buttonPanel.add(manageBtn);
        
        moPanel.add(titleLabel, BorderLayout.NORTH);
        moPanel.add(buttonPanel, BorderLayout.CENTER);
        
        setContentPanel(moPanel);
    }

    public void showAdminMenu() {
        JPanel adminPanel = new JPanel(new BorderLayout(10, 10));
        adminPanel.setBackground(Color.WHITE);
        adminPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 102, 153));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton manageBtn = createMenuButton("Module Management");
        manageBtn.addActionListener(e -> {
            ModuleManageWindow manageWindow = new ModuleManageWindow(this);
            setContentPanel(manageWindow);
        });
        
        JButton workloadBtn = createMenuButton("Workload Statistics");
        workloadBtn.addActionListener(e -> {
            WorkloadWindow workloadWindow = new WorkloadWindow(this);
            setContentPanel(workloadWindow);
        });
        
        buttonPanel.add(manageBtn);
        buttonPanel.add(workloadBtn);
        
        adminPanel.add(titleLabel, BorderLayout.NORTH);
        adminPanel.add(buttonPanel, BorderLayout.CENTER);
        
        setContentPanel(adminPanel);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 60));
        button.setBackground(new Color(51, 102, 153));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
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
        logoutButton.setEnabled(false);
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