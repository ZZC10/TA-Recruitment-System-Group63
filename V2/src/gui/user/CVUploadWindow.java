package gui.user;

import gui.MainFrame;
import gui.common.InputPanel;
import gui.common.ButtonPanel;
import gui.common.MessageDialog;
import common.FileUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CVUploadWindow extends JPanel {

    private MainFrame mainFrame;
    private String currentUserId;
    private InputPanel intentionPanel;
    private InputPanel experiencePanel;
    private InputPanel skillsPanel;

    public CVUploadWindow(MainFrame mainFrame, String currentUserId) {
        this.mainFrame = mainFrame;
        this.currentUserId = currentUserId;
        initialize();
        loadCVInfo();
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

        JLabel titleLabel = new JLabel("Edit CV");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(51, 102, 153));
        panel.add(titleLabel);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "CV Information",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(51, 102, 153)
        ));

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        intentionPanel = new InputPanel("Intention (e.g., Software Development TA):", 30);
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(intentionPanel, gbc);

        experiencePanel = new InputPanel("Experience (e.g., Internship, Projects):", 30);
        gbc.gridx = 0;
        gbc.gridy = 1;
        infoPanel.add(experiencePanel, gbc);

        skillsPanel = new InputPanel("Skills (e.g., Java, Python, Communication):", 30);
        gbc.gridx = 0;
        gbc.gridy = 2;
        infoPanel.add(skillsPanel, gbc);

        panel.add(infoPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomPanel() {
        ButtonPanel buttonPanel = new ButtonPanel();

        buttonPanel.addPrimaryButton("Save CV", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCV();
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

    private void loadCVInfo() {
        try {
            List<String[]> users = FileUtil.readCSV("../users.csv");

            for (int i = 1; i < users.size(); i++) {
                String[] user = users.get(i);
                if (user.length > 0 && user[0].equals(currentUserId)) {
                    if (user.length > 6) {
                        intentionPanel.setText(user[6]);
                    }
                    if (user.length > 7) {
                        experiencePanel.setText(user[7]);
                    }
                    if (user.length > 8) {
                        skillsPanel.setText(user[8]);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveCV() {
        String intention = intentionPanel.getText().trim().replace(",", ";");
        String experience = experiencePanel.getText().trim().replace(",", ";");
        String skills = skillsPanel.getText().trim().replace(",", ";");

        if (intention.isEmpty() || experience.isEmpty() || skills.isEmpty()) {
            MessageDialog.showWarning(this, "Warning", "Please fill in all CV information fields!");
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
                    updatedUser[6] = intention;
                    updatedUser[7] = experience;
                    updatedUser[8] = skills;
                    users.set(i, updatedUser);
                    break;
                }
            }

            if (found) {
                FileUtil.writeCSV("../users.csv", users);
                MessageDialog.showSuccess(this, "CV saved successfully!");
            } else {
                MessageDialog.showError(this, "Error", "User not found!");
            }
        } catch (Exception e) {
            MessageDialog.showError(this, "Error", "Failed to save CV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void goBack() {
        mainFrame.showTAMenu();
    }
}
