package gui.position;

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
    private JFrame parentFrame;
    private String currentUserId;

    public MOWorkbenchWindow(JFrame parentFrame, String currentUserId) {
        this.parentFrame = parentFrame;
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

        JLabel titleLabel = new JLabel("MO Workbench - View and Manage Applications");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 0));
        mainPanel.setBackground(Color.WHITE);

        JPanel leftPanel = createJobListPanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = createApplicantListPanel();
        mainPanel.add(rightPanel, BorderLayout.CENTER);

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

        String[] columnNames = {"Student ID", "Status", "Apply Date"};
        applicantTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        applicantTable = new JTable(applicantTableModel);
        applicantTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        applicantTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        applicantTable.getColumnModel().getColumn(2).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(applicantTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton approveButton = new JButton("Approve");
        approveButton.setPreferredSize(new Dimension(120, 35));
        approveButton.setBackground(new Color(0, 153, 51));
        approveButton.setForeground(Color.WHITE);
        approveButton.setFocusPainted(false);
        approveButton.addActionListener(e -> approveApplication());
        buttonPanel.add(approveButton);

        JButton rejectButton = new JButton("Reject");
        rejectButton.setPreferredSize(new Dimension(120, 35));
        rejectButton.setBackground(new Color(204, 0, 0));
        rejectButton.setForeground(Color.WHITE);
        rejectButton.setFocusPainted(false);
        rejectButton.addActionListener(e -> rejectApplication());
        buttonPanel.add(rejectButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadData() {
        jobTableModel.setRowCount(0);
        moModuleIds.clear();

        List<String[]> modules = FileUtil.readCSV("modules.csv");
        for (String[] module : modules) {
            if (module.length > 2 && module[2].equals(currentUserId)) {
                moModuleIds.add(module[0]);
            }
        }

        List<String[]> jobs = FileUtil.readCSV("jobs.csv");
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

        List<String[]> applications = FileUtil.readCSV("applications.csv");
        for (String[] app : applications) {
            if (app.length > 1 && app[1].equals(jobId)) {
                String status = app.length > 2 ? app[2] : "PENDING";
                String applyDate = app.length > 3 ? app[3] : "";
                applicantTableModel.addRow(new Object[]{app[0], status, applyDate});
            }
        }
    }

    private void approveApplication() {
        int selectedRow = applicantTable.getSelectedRow();
        if (selectedRow < 0) {
            MessageDialog.showWarning(parentFrame, "No Selection", "Please select an applicant first!");
            return;
        }

        String studentId = (String) applicantTableModel.getValueAt(selectedRow, 0);
        int selectedJobRow = jobTable.getSelectedRow();
        if (selectedJobRow < 0) return;
        String jobId = (String) jobTableModel.getValueAt(selectedJobRow, 0);

        boolean success = applicationService.approveApplication(studentId, jobId, true);
        if (success) {
            MessageDialog.showSuccess(parentFrame, "Success", "Application approved successfully!");
            loadApplicants();
        } else {
            MessageDialog.showError(parentFrame, "Error", "Failed to approve application.");
        }
    }

    private void rejectApplication() {
        int selectedRow = applicantTable.getSelectedRow();
        if (selectedRow < 0) {
            MessageDialog.showWarning(parentFrame, "No Selection", "Please select an applicant first!");
            return;
        }

        String studentId = (String) applicantTableModel.getValueAt(selectedRow, 0);
        int selectedJobRow = jobTable.getSelectedRow();
        if (selectedJobRow < 0) return;
        String jobId = (String) jobTableModel.getValueAt(selectedJobRow, 0);

        boolean success = applicationService.approveApplication(studentId, jobId, false);
        if (success) {
            MessageDialog.showSuccess(parentFrame, "Success", "Application rejected successfully!");
            loadApplicants();
        } else {
            MessageDialog.showError(parentFrame, "Error", "Failed to reject application.");
        }
    }
}