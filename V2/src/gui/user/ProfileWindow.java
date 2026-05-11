package gui.user;

import gui.MainFrame;
import gui.common.InputPanel;
import gui.common.ButtonPanel;
import gui.common.MessageDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfileWindow extends JPanel {

    private MainFrame mainFrame;
    private InputPanel studentIdPanel;
    private InputPanel namePanel;
    private InputPanel emailPanel;
    private InputPanel majorPanel;
    private InputPanel rolePanel;
    private JLabel titleLabel;

    public ProfileWindow(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);

        titleLabel = new JLabel("Personal Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(51, 102, 153));
        panel.add(titleLabel);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        studentIdPanel = new InputPanel("Student ID:", 20);
        studentIdPanel.getTextField().setEditable(false);
        studentIdPanel.getTextField().setBackground(new Color(240, 240, 240));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(studentIdPanel, gbc);

        namePanel = new InputPanel("Name:", 20);
        namePanel.getTextField().setEditable(false);
        namePanel.getTextField().setBackground(new Color(240, 240, 240));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(namePanel, gbc);

        emailPanel = new InputPanel("Email:", 20);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(emailPanel, gbc);

        majorPanel = new InputPanel("Major:", 20);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(majorPanel, gbc);

        rolePanel = new InputPanel("Role:", 20);
        rolePanel.getTextField().setEditable(false);
        rolePanel.getTextField().setBackground(new Color(240, 240, 240));
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(rolePanel, gbc);

        return panel;
    }

    private JPanel createBottomPanel() {
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

        return buttonPanel;
    }

    public void loadUserInfo(String studentId, String name, String email, String major, String role) {
        studentIdPanel.setText(studentId != null ? studentId : "");
        namePanel.setText(name != null ? name : "");
        emailPanel.setText(email != null ? email : "");
        majorPanel.setText(major != null ? major : "");
        rolePanel.setText(role != null ? role : "");
    }

    private void saveProfile() {
        String email = emailPanel.getText();
        String major = majorPanel.getText();

        if (email.isEmpty() || major.isEmpty()) {
            MessageDialog.showWarning(this, "Warning", "Email and Major cannot be empty!");
            return;
        }

        MessageDialog.showSuccess(this, "Profile updated successfully!");
    }

    private void goBack() {
        mainFrame.showWelcomePanel();
    }
}
