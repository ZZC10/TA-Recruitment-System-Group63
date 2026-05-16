package gui.job;

import gui.MainFrame;
import gui.common.MessageDialog;
import job.JobService;
import application.ApplicationService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class JobBrowseWindow extends JPanel {
    private JobService jobService;
    private ApplicationService applicationService;
    private String currentUserId;
    private JTable jobTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> categoryFilter;
    private MainFrame mainFrame;

    public JobBrowseWindow(MainFrame mainFrame, String userId) {
        this.mainFrame = mainFrame;
        this.currentUserId = userId;
        this.jobService = new JobService();
        this.applicationService = new ApplicationService();
        
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initUI();
        loadJobs();
    }

    private void initUI() {
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
        backBtn.addActionListener(e -> mainFrame.showTAMenu());
        leftPanel.add(backBtn);
        
        JLabel titleLabel = new JLabel("Browse Jobs");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        leftPanel.add(titleLabel);
        
        topPanel.add(leftPanel, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.add(new JLabel("Filter by Category:"));
        categoryFilter = new JComboBox<>();
        categoryFilter.addItem("All");
        categoryFilter.addActionListener(e -> filterJobs());
        filterPanel.add(categoryFilter);
        topPanel.add(filterPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        String[] columns = {"Job ID", "Job Title", "Category", "Deadline"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jobTable = new JTable(tableModel);
        jobTable.setRowHeight(25);
        jobTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(jobTable);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(Color.WHITE);
        
        JButton viewDetailsBtn = new JButton("View Details");
        viewDetailsBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        viewDetailsBtn.setPreferredSize(new Dimension(120, 35));
        viewDetailsBtn.setBackground(new Color(102, 102, 102));
        viewDetailsBtn.setForeground(Color.WHITE);
        viewDetailsBtn.setFocusPainted(false);
        viewDetailsBtn.setBorderPainted(false);
        viewDetailsBtn.setOpaque(true);
        
        JButton applyBtn = new JButton("Apply for this Job");
        applyBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        applyBtn.setPreferredSize(new Dimension(150, 35));
        applyBtn.setBackground(new Color(51, 102, 153));
        applyBtn.setForeground(Color.WHITE);
        applyBtn.setFocusPainted(false);
        applyBtn.setBorderPainted(false);
        applyBtn.setOpaque(true);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        refreshBtn.setPreferredSize(new Dimension(100, 35));
        refreshBtn.setBackground(new Color(102, 102, 102));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setOpaque(true);
        
        viewDetailsBtn.addActionListener(e -> viewJobDetails());
        applyBtn.addActionListener(e -> applyForSelectedJob());
        refreshBtn.addActionListener(e -> loadJobs());
        
        bottomPanel.add(viewDetailsBtn);
        bottomPanel.add(applyBtn);
        bottomPanel.add(refreshBtn);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadJobs() {
        tableModel.setRowCount(0);
        List<String[]> jobs = jobService.getAllJobs();
        
        java.util.Set<String> categories = new java.util.HashSet<>();
        
        for (int i = 1; i < jobs.size(); i++) {
            String[] job = jobs.get(i);
            String jobId = job.length > 0 ? job[0] : "";
            String title = job.length > 1 ? job[1] : "";
            String category = job.length > 2 ? job[2] : "Uncategorized";
            String deadline = job.length > 4 ? job[4] : "";
            tableModel.addRow(new Object[]{jobId, title, category, deadline});
            categories.add(category);
        }
        
        categoryFilter.removeActionListener(categoryFilter.getActionListeners()[0]);
        categoryFilter.removeAllItems();
        categoryFilter.addItem("All");
        for (String cat : categories) {
            categoryFilter.addItem(cat);
        }
        categoryFilter.addActionListener(e -> filterJobs());
    }

    private void filterJobs() {
        String selectedCategory = (String) categoryFilter.getSelectedItem();
        if (selectedCategory == null) return;
        
        tableModel.setRowCount(0);
        List<String[]> jobs = jobService.getAllJobs();
        
        for (int i = 1; i < jobs.size(); i++) {
            String[] job = jobs.get(i);
            String category = job.length > 2 ? job[2] : "Uncategorized";
            if ("All".equals(selectedCategory) || selectedCategory.equals(category)) {
                String jobId = job.length > 0 ? job[0] : "";
                String title = job.length > 1 ? job[1] : "";
                String deadline = job.length > 4 ? job[4] : "";
                tableModel.addRow(new Object[]{jobId, title, category, deadline});
            }
        }
    }

    private void viewJobDetails() {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialog.showWarning(this, "Warning", "Please select a job first.");
            return;
        }
        
        String jobId = (String) tableModel.getValueAt(selectedRow, 0);
        String jobTitle = (String) tableModel.getValueAt(selectedRow, 1);
        String category = (String) tableModel.getValueAt(selectedRow, 2);
        String deadline = (String) tableModel.getValueAt(selectedRow, 3);
        
        MessageDialog.showInfo(this, "Job Details", 
            "Job ID: " + jobId + "\nTitle: " + jobTitle + "\nCategory: " + category + "\nDeadline: " + deadline);
    }

    private void applyForSelectedJob() {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialog.showWarning(this, "Warning", "Please select a job to apply.");
            return;
        }
        
        String jobId = (String) tableModel.getValueAt(selectedRow, 0);
        String jobTitle = (String) tableModel.getValueAt(selectedRow, 1);
        
        if (MessageDialog.confirm(this, "Confirm Application", "Apply for job: " + jobTitle + "?")) {
            boolean success = jobService.applyForJob(currentUserId, jobId);
            if (success) {
                MessageDialog.showSuccess(this, "Success", "Application submitted successfully!");
            } else {
                MessageDialog.showError(this, "Error", "Failed to apply. You may have already applied or permission denied.");
            }
        }
    }
}