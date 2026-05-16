package gui.position;

import common.FileUtil;
import gui.common.ButtonPanel;
import gui.common.InputPanel;
import gui.common.MessageDialog;
import position.PositionService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PublishJobWindow extends JPanel {
    private PositionService positionService;
    private JFrame parentFrame;
    private String currentUserId;

    private JComboBox<String> moduleComboBox;
    private InputPanel titleInput;
    private InputPanel hoursInput;
    private InputPanel requirementsInput;
    private JTextArea descriptionArea;

    public PublishJobWindow(JFrame parentFrame, String currentUserId) {
        this.parentFrame = parentFrame;
        this.currentUserId = currentUserId;
        this.positionService = new PositionService();
        this.positionService.setCurrentUserId(currentUserId);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Publish New Job Position");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Select Module:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        moduleComboBox = new JComboBox<>();
        moduleComboBox.setPreferredSize(new Dimension(300, 30));
        loadModules();
        formPanel.add(moduleComboBox, gbc);

        titleInput = new InputPanel("Job Title:", 30);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(titleInput.getLabel(), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(titleInput.getTextField(), gbc);

        hoursInput = new InputPanel("Working Hours:", 10);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(hoursInput.getLabel(), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(hoursInput.getTextField(), gbc);

        requirementsInput = new InputPanel("Requirements:", 30);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(requirementsInput.getLabel(), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(requirementsInput.getTextField(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Description:"), gbc);

        descriptionArea = new JTextArea(5, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(descScrollPane, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton publishButton = new JButton("Publish");
        publishButton.setPreferredSize(new Dimension(120, 40));
        publishButton.setBackground(new Color(51, 102, 153));
        publishButton.setForeground(Color.WHITE);
        publishButton.setFocusPainted(false);
        publishButton.addActionListener(e -> publishJob());
        buttonPanel.add(publishButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> clearForm());
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadModules() {
        moduleComboBox.removeAllItems();
        List<String[]> modules = FileUtil.readCSV("modules.csv");
        for (String[] module : modules) {
            if (module.length > 2 && module[2].equals(currentUserId)) {
                moduleComboBox.addItem(module[0] + " - " + module[1]);
            }
        }
    }

    private void publishJob() {
        String selectedModule = (String) moduleComboBox.getSelectedItem();
        if (selectedModule == null) {
            MessageDialog.showWarning(parentFrame, "No Selection", "Please select a module first!");
            return;
        }

        String moduleId = selectedModule.split(" - ")[0];
        String title = titleInput.getText();
        String hours = hoursInput.getText();
        String requirements = requirementsInput.getText();
        String description = descriptionArea.getText();

        if (title.isEmpty() || hours.isEmpty() || requirements.isEmpty()) {
            MessageDialog.showWarning(parentFrame, "Missing Information", "Please fill in all required fields!");
            return;
        }

        try {
            int hoursInt = Integer.parseInt(hours);
            boolean success = positionService.publishJob(moduleId, title, hoursInt, requirements, description);

            if (success) {
                MessageDialog.showSuccess(parentFrame, "Success", "Job published successfully!");
                clearForm();
            } else {
                MessageDialog.showError(parentFrame, "Error", "Failed to publish job.");
            }
        } catch (NumberFormatException ex) {
            MessageDialog.showError(parentFrame, "Invalid Input", "Working hours must be a number!");
        }
    }

    private void clearForm() {
        titleInput.setText("");
        hoursInput.setText("");
        requirementsInput.setText("");
        descriptionArea.setText("");
        moduleComboBox.setSelectedIndex(0);
    }
}