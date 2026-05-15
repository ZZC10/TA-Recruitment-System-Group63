package gui.job;

import job.JobService;
import application.ApplicationService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class JobBrowseWindow extends JFrame {
    private JobService jobService;
    private ApplicationService applicationService;
    private String currentStudentId;
    private JTable jobTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> categoryFilter;

    public JobBrowseWindow(String studentId) {
        this.currentStudentId = studentId;
        this.jobService = new JobService();
        this.applicationService = new ApplicationService();
        
        setTitle("Browse Jobs - TA Panel");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initUI();
        loadJobs();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        
        // Top panel with filter
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Filter by Category:"));
        categoryFilter = new JComboBox<>();
        categoryFilter.addItem("All");
        categoryFilter.addActionListener(e -> filterJobs());
        topPanel.add(categoryFilter);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Table to display jobs
        String[] columns = {"Job ID", "Job Title", "Category", "Deadline"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jobTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(jobTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel();
        JButton viewDetailsBtn = new JButton("View Details");
        JButton applyBtn = new JButton("Apply for this Job");
        JButton refreshBtn = new JButton("Refresh");
        
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
        
        for (String[] job : jobs) {
            String jobId = job.length > 0 ? job[0] : "";
            String title = job.length > 1 ? job[1] : "";
            String category = job.length > 2 ? job[2] : "Uncategorized";
            String deadline = job.length > 4 ? job[4] : "";
            tableModel.addRow(new Object[]{jobId, title, category, deadline});
            categories.add(category);
        }
        
        categoryFilter.removeAllItems();
        categoryFilter.addItem("All");
        for (String cat : categories) {
            categoryFilter.addItem(cat);
        }
    }

    private void filterJobs() {
        String selectedCategory = (String) categoryFilter.getSelectedItem();
        tableModel.setRowCount(0);
        List<String[]> jobs = jobService.getAllJobs();
        
        for (String[] job : jobs) {
            String category = job.length > 2 ? job[2] : "Uncategorized";
            if (selectedCategory.equals("All") || category.equals(selectedCategory)) {
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
            JOptionPane.showMessageDialog(this, "Please select a job first.");
            return;
        }
        
        String jobId = (String) tableModel.getValueAt(selectedRow, 0);
        String jobTitle = (String) tableModel.getValueAt(selectedRow, 1);
        
        JOptionPane.showMessageDialog(this, "Job ID: " + jobId + "\nTitle: " + jobTitle + "\n\nDescription: This is a TA position.");
    }

    private void applyForSelectedJob() {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job to apply.");
            return;
        }
        
        String jobId = (String) tableModel.getValueAt(selectedRow, 0);
        String jobTitle = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apply for job: " + jobTitle + "?", 
            "Confirm Application", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = jobService.applyForJob(currentStudentId, jobId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Application submitted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to apply. You may have already applied.");
            }
        }
    }
}
