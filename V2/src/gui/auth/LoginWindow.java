package gui.auth;

import gui.MainFrame;
import auth.AuthService;
import auth.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JDialog {
    
    private MainFrame mainFrame;
    private AuthService authService;
    
    // UI Components
    private JTextField studentIdField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel messageLabel;
    
    public LoginWindow(MainFrame parent) {
        super(parent, "Login - TA Recruitment System", true);
        this.mainFrame = parent;
        this.authService = new AuthService();
        initialize();
    }
    
    private void initialize() {
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("TA Recruitment System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(51, 102, 153));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        // Student ID Label
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(idLabel, gbc);
        
        // Student ID Field
        gbc.gridx = 1;
        studentIdField = new JTextField(15);
        studentIdField.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(studentIdField, gbc);
        
        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(passwordLabel, gbc);
        
        // Password Field
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(passwordField, gbc);
        
        // Role Label
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(roleLabel, gbc);
        
        // Role ComboBox
        gbc.gridx = 1;
        String[] roles = {"TA", "MO", "ADMIN"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(roleComboBox, gbc);
        
        // Message Label
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messageLabel.setForeground(Color.RED);
        mainPanel.add(messageLabel, gbc);
        
        // Button Panel
        gbc.gridy = 5;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(51, 102, 153));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(100, 40));
        loginButton.addActionListener(new LoginAction());
        
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 14));
        registerButton.setBackground(new Color(102, 102, 102));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(100, 40));
        registerButton.addActionListener(e -> openRegisterWindow());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Enter key to login
        getRootPane().setDefaultButton(loginButton);
    }
    
    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String studentId = studentIdField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String selectedRole = (String) roleComboBox.getSelectedItem();
            
            if (studentId.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter both Student ID and Password");
                return;
            }
            
            // Call V1 AuthService
            try {
                User user = authService.login(studentId, password);
                if (user != null) {
                    String actualRole = authService.getUserRole(user);
                    
                    // Check if role matches selected role
                    if (!actualRole.equals(selectedRole)) {
                        messageLabel.setText("Role mismatch! You are a " + actualRole);
                        return;
                    }
                    
                    // Login success
                    messageLabel.setText("");
                    mainFrame.setCurrentUser(studentId, actualRole);
                    
                    // Close login window
                    dispose();
                    
                    // Show welcome message
                    JOptionPane.showMessageDialog(mainFrame, 
                        "Welcome " + studentId + "!\nRole: " + actualRole,
                        "Login Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                        
                } else {
                    messageLabel.setText("Invalid Student ID or Password");
                }
            } catch (Exception ex) {
                messageLabel.setText("Login error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    private void openRegisterWindow() {
        RegisterWindow registerWindow = new RegisterWindow(this);
        registerWindow.setVisible(true);
    }
}