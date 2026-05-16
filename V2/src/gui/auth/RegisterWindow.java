package gui.auth;

import auth.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterWindow extends JDialog {

    private AuthService authService;

    private JTextField userIdField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JComboBox<String> roleComboBox;
    private JButton registerButton;
    private JButton cancelButton;
    private JLabel messageLabel;

    public RegisterWindow(JDialog parent) {
        super(parent, "Register - TA Recruitment System", true);
        this.authService = new AuthService();
        initialize();
    }

    public RegisterWindow(JFrame parent) {
        super(parent, "Register - TA Recruitment System", true);
        this.authService = new AuthService();
        initialize();
    }

    private void initialize() {
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Register New Account", SwingConstants.CENTER);
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
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(roleLabel, gbc);

        gbc.gridx = 1;
        String[] roles = {"TA", "MO"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messageLabel.setForeground(Color.RED);
        mainPanel.add(messageLabel, gbc);

        gbc.gridy = 6;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(51, 102, 153));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(100, 40));
        registerButton.addActionListener(new RegisterAction());

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setBackground(new Color(102, 102, 102));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        getRootPane().setDefaultButton(registerButton);
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userId = userIdField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String name = nameField.getText().trim();
            String role = (String) roleComboBox.getSelectedItem();

            if (userId.isEmpty()) {
                messageLabel.setText("Please enter User ID");
                return;
            }

            if (password.isEmpty()) {
                messageLabel.setText("Please enter Password");
                return;
            }

            if (password.length() < 6) {
                messageLabel.setText("Password must be at least 6 characters");
                return;
            }

            if (name.isEmpty()) {
                messageLabel.setText("Please enter Full Name");
                return;
            }

            try {
                boolean loginSuccess = authService.login(userId, password);
                if (loginSuccess) {
                    messageLabel.setText("User ID already exists!");
                    return;
                }

                boolean success = authService.register(userId, password, name, role);

                if (success) {
                    messageLabel.setText("");
                    JOptionPane.showMessageDialog(RegisterWindow.this,
                        "Registration Successful!\nUser ID: " + userId + "\nRole: " + role,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    messageLabel.setText("Registration failed. Please try again.");
                }
            } catch (Exception ex) {
                messageLabel.setText("Registration error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}