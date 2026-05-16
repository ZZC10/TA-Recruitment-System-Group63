package gui.job;

import gui.MainFrame;
import gui.common.MessageDialog;
import job.JobService;
import application.ApplicationService;
import common.FileUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ApplicationWindow extends JPanel {
    private JobService jobService;
    private ApplicationService applicationService;
    private String currentUserId;
    private JTable applicationTable;
    private DefaultTableModel tableModel;
    private MainFrame mainFrame;

    public ApplicationWindow(MainFrame mainFrame, String userId) {
        this.mainFrame = mainFrame;
        this.currentUserId = userId;
        this.jobService = new JobService();
        this.applicationService = new ApplicationService();
        
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initUI();
        loadApplications();
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
        
        JLabel titleLabel = new JLabel("My Applications");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        leftPanel.add(titleLabel);
        
        topPanel.add(leftPanel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);
        
        String[] columns = {"Job ID", "Job Title", "Status", "Apply Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        applicationTable = new JTable(tableModel);
        applicationTable.setRowHeight(25);
        applicationTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(applicationTable);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(Color.WHITE);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        refreshBtn.setPreferredSize(new Dimension(100, 35));
        refreshBtn.setBackground(new Color(102, 102, 102));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setOpaque(true);
        refreshBtn.addActionListener(e -> loadApplications());
        bottomPanel.add(refreshBtn);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadApplications() {
        tableModel.setRowCount(0);
        
        List<String[]> allApplications = FileUtil.readCSV("../applications.csv");
        List<String[]> jobs = FileUtil.readCSV("../jobs.csv");
        
        for (int i = 1; i < allApplications.size(); i++) {
            String[] app = allApplications.get(i);
            if (app.length > 0 && app[0].equals(currentUserId)) {
                String jobId = app.length > 1 ? app[1] : "";
                String status = app.length > 2 ? app[2] : "PENDING";
                String applyDate = app.length > 3 ? app[3] : "N/A";
                
                String jobTitle = "Unknown";
                for (int j = 1; j < jobs.size(); j++) {
                    String[] job = jobs.get(j);
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