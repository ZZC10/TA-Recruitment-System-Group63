package gui.auth;

import gui.MainFrame;
import auth.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JDialog {

    private MainFrame mainFrame;
    private AuthService authService;

    private JTextField userIdField;
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

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("TA Recruitment System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(51, 102, 153));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel idLabel = new JLabel("User ID:");
        idLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        userIdField = new JTextField(15);
        userIdField.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(userIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(roleLabel, gbc);

        gbc.gridx = 1;
        String[] roles = {"TA", "MO", "ADMIN"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messageLabel.setForeground(Color.RED);
        mainPanel.add(messageLabel, gbc);

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

        getRootPane().setDefaultButton(loginButton);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userId = userIdField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String selectedRole = (String) roleComboBox.getSelectedItem();

            if (userId.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter both User ID and Password");
                return;
            }

            try {
                boolean loginSuccess = authService.login(userId, password);
                if (loginSuccess) {
                    String actualRole = authService.getUserRole(userId);

                    if (!actualRole.equals(selectedRole)) {
                        messageLabel.setText("Role mismatch! You are a " + actualRole);
                        return;
                    }

                    messageLabel.setText("");
                    mainFrame.setCurrentUser(userId, actualRole);
                    dispose();

                    JOptionPane.showMessageDialog(mainFrame,
                        "Welcome " + authService.getUserName(userId) + "!\nRole: " + actualRole,
                        "Login Success",
                        JOptionPane.INFORMATION_MESSAGE);

                } else {
                    messageLabel.setText("Invalid User ID or Password");
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