package gui.position;

import gui.MainFrame;
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
    private MainFrame mainFrame;
    private String currentUserId;

    private JComboBox<String> moduleComboBox;
    private InputPanel titleInput;
    private InputPanel hoursInput;
    private InputPanel requirementsInput;
    private JTextArea descriptionArea;

    public PublishJobWindow(MainFrame mainFrame, String currentUserId) {
        this.mainFrame = mainFrame;
        this.currentUserId = currentUserId;
        this.positionService = new PositionService();
        this.positionService.setCurrentUserId(currentUserId);
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.WHITE);
        
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        backBtn.setPreferredSize(new Dimension(80, 35));
        backBtn.setBackground(new Color(102, 102, 102));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setOpaque(true);
        backBtn.addActionListener(e -> mainFrame.showMOMenu());
        leftPanel.add(backBtn);
        
        JLabel titleLabel = new JLabel("Publish New Job Position");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 0));
        leftPanel.add(titleLabel);
        
        topPanel.add(leftPanel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

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
        List<String[]> modules = FileUtil.readCSV("../modules.csv");
        for (String[] module : modules) {
            if (module.length > 2 && module[2].equals(currentUserId)) {
                moduleComboBox.addItem(module[0] + " - " + module[1]);
            }
        }
    }

    private void publishJob() {
        String selectedModule = (String) moduleComboBox.getSelectedItem();
        if (selectedModule == null) {
            MessageDialog.showWarning(mainFrame, "No Selection", "Please select a module first!");
            return;
        }

        String moduleId = selectedModule.split(" - ")[0];
        String title = titleInput.getText();
        String hours = hoursInput.getText();
        String requirements = requirementsInput.getText();
        String description = descriptionArea.getText();

        if (title.isEmpty() || hours.isEmpty() || requirements.isEmpty()) {
            MessageDialog.showWarning(mainFrame, "Missing Information", "Please fill in all required fields!");
            return;
        }

        try {
            int hoursInt = Integer.parseInt(hours);
            boolean success = positionService.publishJob(moduleId, title, hoursInt, requirements, description);

            if (success) {
                MessageDialog.showSuccess(mainFrame, "Success", "Job published successfully!");
                clearForm();
            } else {
                MessageDialog.showError(mainFrame, "Error", "Failed to publish job.");
            }
        } catch (NumberFormatException ex) {
            MessageDialog.showError(mainFrame, "Invalid Input", "Working hours must be a number!");
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