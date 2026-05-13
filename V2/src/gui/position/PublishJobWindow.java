package gui.position;

import common.FileUtil;
import gui.common.ButtonPanel;
import gui.common.InputPanel;
import gui.common.MessageDialog;
import module.ModuleService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * PublishJobWindow - MO权限职位发布窗口
 * 成员5：Yu Yue
 */
public class PublishJobWindow extends JPanel {
    private String currentMoId;
    private ModuleService moduleService;
    
    private InputPanel jobIdPanel;
    private InputPanel titlePanel;
    private InputPanel categoryPanel;
    private JComboBox<String> moduleCombo;
    private InputPanel deadlinePanel;
    private JTextArea descriptionArea;

    public PublishJobWindow(String moId) {
        this.currentMoId = moId;
        this.moduleService = new ModuleService();
        
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
    }

    private void initComponents() {
        // Title
        JLabel titleLabel = new JLabel("Publish New Job Position");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);

        jobIdPanel = new InputPanel("Job ID (e.g. J001):", 20);
        titlePanel = new InputPanel("Job Title:", 20);
        categoryPanel = new InputPanel("Job Category:", 20);
        
        // Module Selection
        JPanel modulePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        modulePanel.setBackground(Color.WHITE);
        JLabel moduleLabel = new JLabel("Associated Module:");
        moduleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        moduleLabel.setPreferredSize(new Dimension(150, 25));
        moduleCombo = new JComboBox<>();
        moduleCombo.setPreferredSize(new Dimension(300, 30));
        loadModules();
        modulePanel.add(moduleLabel);
        modulePanel.add(moduleCombo);

        deadlinePanel = new InputPanel("Deadline (YYYY-MM-DD):", 20);

        // Description
        JPanel descPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        descPanel.setBackground(Color.WHITE);
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setPreferredSize(new Dimension(150, 25));
        descriptionArea = new JTextArea(5, 27);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descPanel.add(descLabel);
        descPanel.add(descScroll);

        formPanel.add(jobIdPanel);
        formPanel.add(titlePanel);
        formPanel.add(categoryPanel);
        formPanel.add(modulePanel);
        formPanel.add(deadlinePanel);
        formPanel.add(descPanel);

        add(formPanel, BorderLayout.CENTER);

        // Buttons
        ButtonPanel buttonPanel = new ButtonPanel();
        buttonPanel.addPrimaryButton("Publish", e -> performPublish());
        buttonPanel.addButton("Clear", e -> clearFields());
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadModules() {
        moduleCombo.removeAllItems();
        List<String[]> modules = moduleService.getModulesByLeader(currentMoId);
        if (modules.isEmpty()) {
            // If no modules found for this MO, check if current user is ADMIN
            // This is for testing purposes
            if ("ADMIN".equalsIgnoreCase(currentMoId)) {
                List<String[]> allModules = moduleService.getAllModules();
                for (String[] m : allModules) {
                    moduleCombo.addItem(m[0]);
                }
            }
        } else {
            for (String[] m : modules) {
                moduleCombo.addItem(m[0]);
            }
        }
    }

    private void performPublish() {
        String jobId = jobIdPanel.getText();
        String title = titlePanel.getText();
        String category = categoryPanel.getText();
        String moduleId = (String) moduleCombo.getSelectedItem();
        String deadline = deadlinePanel.getText();
        String description = descriptionArea.getText().trim();

        if (jobId.isEmpty() || title.isEmpty() || category.isEmpty() || moduleId == null || deadline.isEmpty()) {
            MessageDialog.showError(this, "Error", "Please fill in all required fields.");
            return;
        }

        // Validate Job ID format (optional, but good practice)
        if (!jobId.matches("J\\d{3}")) {
            MessageDialog.showWarning(this, "Invalid ID", "Job ID should follow format Jxxx (e.g., J004).");
        }

        // Check if Job ID exists
        List<String[]> existingJobs = FileUtil.readCSV("jobs.csv");
        for (String[] job : existingJobs) {
            if (job.length > 0 && job[0].equalsIgnoreCase(jobId)) {
                MessageDialog.showError(this, "Error", "Job ID " + jobId + " already exists.");
                return;
            }
        }

        // Save to CSV
        String[] newJob = {jobId, title, category, moduleId, deadline, description};
        existingJobs.add(newJob);
        FileUtil.writeCSV("jobs.csv", existingJobs);

        MessageDialog.showSuccess(this, "Job Published successfully!");
        clearFields();
    }

    private void clearFields() {
        jobIdPanel.setText("");
        titlePanel.setText("");
        categoryPanel.setText("");
        if (moduleCombo.getItemCount() > 0) moduleCombo.setSelectedIndex(0);
        deadlinePanel.setText("");
        descriptionArea.setText("");
    }
}
