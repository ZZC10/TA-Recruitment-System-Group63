package gui.user;

import gui.MainFrame;
import gui.common.ButtonPanel;
import gui.common.MessageDialog;
import auth.AuthService;
import user.UserService;
import common.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProfileWindow extends JPanel {

    private MainFrame mainFrame;
    private String currentUserId;
    private JTextField userIdField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField majorField;
    private JTextField roleField;

    public ProfileWindow(MainFrame mainFrame, String currentUserId) {
        this.mainFrame = mainFrame;
        this.currentUserId = currentUserId;
        initialize();
        loadUserInfo();
    }

    private void initialize() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("Personal Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(51, 102, 153));
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        centerPanel.add(createLabel("User ID:", labelFont));
        userIdField = createTextField(fieldFont, false);
        centerPanel.add(userIdField);

        centerPanel.add(createLabel("Name:", labelFont));
        nameField = createTextField(fieldFont, false);
        centerPanel.add(nameField);

        centerPanel.add(createLabel("Email:", labelFont));
        emailField = createTextField(fieldFont, true);
        centerPanel.add(emailField);

        centerPanel.add(createLabel("Major:", labelFont));
        majorField = createTextField(fieldFont, true);
        centerPanel.add(majorField);

        centerPanel.add(createLabel("Role:", labelFont));
        roleField = createTextField(fieldFont, false);
        centerPanel.add(roleField);

        add(centerPanel, BorderLayout.CENTER);

        ButtonPanel buttonPanel = new ButtonPanel();
        buttonPanel.addPrimaryButton("Save Changes", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProfile();
            }
        });
        buttonPanel.addButton("Back", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setPreferredSize(new Dimension(100, 30));
        return label;
    }

    private JTextField createTextField(Font font, boolean editable) {
        JTextField field = new JTextField(20);
        field.setFont(font);
        field.setEditable(editable);
        field.setPreferredSize(new Dimension(250, 30));
        if (!editable) {
            field.setBackground(new Color(240, 240, 240));
        }
        return field;
    }

    private void loadUserInfo() {
        AuthService authService = new AuthService();
        UserService userService = new UserService();

        String name = authService.getUserName(currentUserId);
        String role = authService.getUserRole(currentUserId);
        
        String[] user = userService.getUserByUserId(currentUserId);
        String email = "";
        String major = "";
        
        if (user != null && user.length > 3) {
            email = user[3];
        }
        if (user != null && user.length > 4) {
            major = user[4];
        }

        userIdField.setText(currentUserId != null ? currentUserId : "");
        nameField.setText(name != null ? name : "");
        emailField.setText(email != null ? email : "");
        majorField.setText(major != null ? major : "");
        roleField.setText(role != null ? role : "");
    }

    private void saveProfile() {
        String email = emailField.getText().trim();
        String major = majorField.getText().trim();

        if (email.isEmpty() || major.isEmpty()) {
            MessageDialog.showWarning(this, "Warning", "Email and Major cannot be empty!");
            return;
        }

        try {
            List<String[]> users = FileUtil.readCSV("../users.csv");
            boolean found = false;

            for (int i = 1; i < users.size(); i++) {
                String[] user = users.get(i);
                if (user.length > 0 && user[0].equals(currentUserId)) {
                    found = true;
                    String[] updatedUser = new String[9];
                    for (int j = 0; j < Math.min(user.length, 9); j++) {
                        updatedUser[j] = user[j];
                    }
                    for (int j = user.length; j < 9; j++) {
                        updatedUser[j] = "";
                    }
                    updatedUser[0] = user[0];
                    updatedUser[1] = user[1];
                    updatedUser[2] = user[2];
                    updatedUser[3] = email;
                    updatedUser[4] = major;
                    updatedUser[5] = user.length > 5 ? user[5] : "TA";
                    users.set(i, updatedUser);
                    break;
                }
            }

            if (found) {
                FileUtil.writeCSV("../users.csv", users);
                MessageDialog.showSuccess(this, "Profile updated successfully!");
            } else {
                MessageDialog.showError(this, "Error", "User not found!");
            }
        } catch (Exception e) {
            MessageDialog.showError(this, "Error", "Failed to update profile: " + e.getMessage());
        }
    }

    private void goBack() {
        mainFrame.showTAMenu();
    }
}