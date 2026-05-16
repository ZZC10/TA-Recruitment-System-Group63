package gui.job;

import job.JobService;
import application.ApplicationService;
import common.FileUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ApplicationWindow extends JFrame {
    private JobService jobService;
    private ApplicationService applicationService;
    private String currentStudentId;
    private JTable applicationTable;
    private DefaultTableModel tableModel;

    public ApplicationWindow(String studentId) {
        this.currentStudentId = studentId;
        this.jobService = new JobService();
        this.applicationService = new ApplicationService();
        
        setTitle("My Applications - TA Panel");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initUI();
        loadApplications();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        
        String[] columns = {"Job ID", "Job Title", "Status", "Apply Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        applicationTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(applicationTable);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadApplications());
        bottomPanel.add(refreshBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadApplications() {
        tableModel.setRowCount(0);
        
        List<String[]> allApplications = FileUtil.readCSV("applications.csv");
        List<String[]> jobs = FileUtil.readCSV("jobs.csv");
        
        for (String[] app : allApplications) {
            if (app.length > 0 && app[0].equals(currentStudentId)) {
                String jobId = app[1];
                String status = app.length > 2 ? app[2] : "PENDING";
                String applyDate = app.length > 3 ? app[3] : "N/A";
                
                String jobTitle = "Unknown";
                for (String[] job : jobs) {
                    if (job.length > 0 && job[0].equals(jobId)) {
                        jobTitle = job.length > 1 ? job[1] : "Unknown";
                        break;
                    }
                }
                
                tableModel.addRow(new Object[]{jobId, jobTitle, status, applyDate});
            }
        }
        
        if (tableModel.getRowCount() == 0) {
            tableModel.addRow(new Object[]{"", "No applications found", "", ""});
        }
    }
}
