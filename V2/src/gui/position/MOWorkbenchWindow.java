package gui.position;

import gui.MainFrame;
import application.ApplicationService;
import common.FileUtil;
import gui.common.ButtonPanel;
import gui.common.MessageDialog;
import module.ModuleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MOWorkbenchWindow extends JPanel {
    private String currentMoId;
    private ModuleService moduleService;
    private ApplicationService applicationService;

    private JTable jobTable;
    private DefaultTableModel jobTableModel;
    private JTable applicantTable;
    private DefaultTableModel applicantTableModel;

    private List<String> moModuleIds;
    private MainFrame mainFrame;
    private String currentUserId;

    public MOWorkbenchWindow(MainFrame mainFrame, String currentUserId) {
        this.mainFrame = mainFrame;
        this.currentUserId = currentUserId;
        this.moduleService = new ModuleService();
        this.applicationService = new ApplicationService();
        this.moModuleIds = new ArrayList<>();
        initialize();
        loadData();
    }

    private void initialize() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
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
        
        JLabel titleLabel = new JLabel("MO Workbench - Manage Applications");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 0));
        leftPanel.add(titleLabel);
        
        topPanel.add(leftPanel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 0));
        mainPanel.setBackground(Color.WHITE);

        JPanel leftJobPanel = createJobListPanel();
        mainPanel.add(leftJobPanel, BorderLayout.WEST);

        JPanel rightApplicantPanel = createApplicantListPanel();
        mainPanel.add(rightApplicantPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createJobListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(400, 500));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("My Published Jobs"));

        String[] columnNames = {"Job ID", "Title", "Module"};
        jobTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jobTable = new JTable(jobTableModel);
        jobTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jobTable.setRowHeight(25);
        jobTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        jobTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        jobTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        jobTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        jobTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadApplicants();
            }
        });

        JScrollPane scrollPane = new JScrollPane(jobTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createApplicantListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(500, 500));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Applicants"));

        String[] columnNames = {"User ID", "Status", "Apply Date"};
        applicantTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        applicantTable = new JTable(applicantTableModel);
        applicantTable.setRowHeight(25);
        applicantTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        applicantTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        applicantTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        applicantTable.getColumnModel().getColumn(2).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(applicantTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setPreferredSize(new Dimension(120, 35));
        viewDetailsButton.setBackground(new Color(51, 102, 153));
        viewDetailsButton.setForeground(Color.WHITE);
        viewDetailsButton.setFocusPainted(false);
        viewDetailsButton.setBorderPainted(false);
        viewDetailsButton.setOpaque(true);
        viewDetailsButton.addActionListener(e -> viewApplicantDetails());
        buttonPanel.add(viewDetailsButton);

        JButton approveButton = new JButton("Approve");
        approveButton.setPreferredSize(new Dimension(120, 35));
        approveButton.setBackground(new Color(0, 153, 51));
        approveButton.setForeground(Color.WHITE);
        approveButton.setFocusPainted(false);
        approveButton.setBorderPainted(false);
        approveButton.setOpaque(true);
        approveButton.addActionListener(e -> approveApplication());
        buttonPanel.add(approveButton);

        JButton rejectButton = new JButton("Reject");
        rejectButton.setPreferredSize(new Dimension(120, 35));
        rejectButton.setBackground(new Color(204, 0, 0));
        rejectButton.setForeground(Color.WHITE);
        rejectButton.setFocusPainted(false);
        rejectButton.setBorderPainted(false);
        rejectButton.setOpaque(true);
        rejectButton.addActionListener(e -> rejectApplication());
        buttonPanel.add(rejectButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadData() {
        jobTableModel.setRowCount(0);
        moModuleIds.clear();

        List<String[]> modules = FileUtil.readCSV("../modules.csv");
        for (String[] module : modules) {
            if (module.length > 2 && module[2].equals(currentUserId)) {
                moModuleIds.add(module[0]);
            }
        }

        List<String[]> jobs = FileUtil.readCSV("../jobs.csv");
        for (String[] job : jobs) {
            if (job.length > 3 && moModuleIds.contains(job[3])) {
                jobTableModel.addRow(new Object[]{job[0], job[1], job[3]});
            }
        }
    }

    private void loadApplicants() {
        applicantTableModel.setRowCount(0);

        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow < 0) return;

        String jobId = (String) jobTableModel.getValueAt(selectedRow, 0);

        List<String[]> applications = FileUtil.readCSV("../applications.csv");
        for (String[] app : applications) {
            if (app.length > 1 && app[1].equals(jobId)) {
                String status = app.length > 2 ? app[2] : "PENDING";
                String applyDate = app.length > 3 ? app[3] : "";
                applicantTableModel.addRow(new Object[]{app[0], status, applyDate});
            }
        }
    }

    private void viewApplicantDetails() {
        int selectedRow = applicantTable.getSelectedRow();
        if (selectedRow < 0) {
            MessageDialog.showWarning(mainFrame, "No Selection", "Please select an applicant first!");
            return;
        }

        String userId = (String) applicantTableModel.getValueAt(selectedRow, 0);
        
        List<String[]> users = FileUtil.readCSV("../users.csv");
        String name = "";
        String email = "";
        String major = "";
        String intention = "";
        String experience = "";
        String skills = "";
        String role = "";

        for (String[] user : users) {
            if (user.length > 0 && user[0].equals(userId)) {
                name = user.length > 1 ? user[1] : "";
                email = user.length > 3 ? user[3] : "";
                major = user.length > 4 ? user[4] : "";
                intention = user.length > 5 ? user[5] : "";
                experience = user.length > 6 ? user[6] : "";
                skills = user.length > 7 ? user[7] : "";
                role = user.length > 8 ? user[8] : "";
                break;
            }
        }

        StringBuilder details = new StringBuilder();
        details.append("User ID: ").append(userId).append("\n");
        details.append("Name: ").append(name).append("\n");
        details.append("Email: ").append(email).append("\n");
        details.append("Major: ").append(major).append("\n");
        details.append("Role: ").append(role).append("\n\n");
        details.append("===== CV Information =====\n");
        details.append("Intention: ").append(intention.isEmpty() ? "Not provided" : intention).append("\n");
        details.append("Experience: ").append(experience.isEmpty() ? "Not provided" : experience).append("\n");
        details.append("Skills: ").append(skills.isEmpty() ? "Not provided" : skills);

        MessageDialog.showInfo(mainFrame, "Applicant Details", details.toString());
    }

    private void approveApplication() {
        int selectedRow = applicantTable.getSelectedRow();
        if (selectedRow < 0) {
            MessageDialog.showWarning(mainFrame, "No Selection", "Please select an applicant first!");
            return;
        }

        String userId = (String) applicantTableModel.getValueAt(selectedRow, 0);
        int selectedJobRow = jobTable.getSelectedRow();
        if (selectedJobRow < 0) return;
        String jobId = (String) jobTableModel.getValueAt(selectedJobRow, 0);

        boolean success = applicationService.approveApplication(userId, jobId, true);
        if (success) {
            MessageDialog.showSuccess(mainFrame, "Success", "Application approved successfully!");
            loadApplicants();
        } else {
            MessageDialog.showError(mainFrame, "Error", "Failed to approve application.");
        }
    }

    private void rejectApplication() {
        int selectedRow = applicantTable.getSelectedRow();
        if (selectedRow < 0) {
            MessageDialog.showWarning(mainFrame, "No Selection", "Please select an applicant first!");
            return;
        }

        String userId = (String) applicantTableModel.getValueAt(selectedRow, 0);
        int selectedJobRow = jobTable.getSelectedRow();
        if (selectedJobRow < 0) return;
        String jobId = (String) jobTableModel.getValueAt(selectedJobRow, 0);

        boolean success = applicationService.approveApplication(userId, jobId, false);
        if (success) {
            MessageDialog.showSuccess(mainFrame, "Success", "Application rejected successfully!");
            loadApplicants();
        } else {
            MessageDialog.showError(mainFrame, "Error", "Failed to reject application.");
        }
    }
}