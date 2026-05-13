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

/**
 * MOWorkbenchWindow - MO工作台窗口
 * 成员5：Yu Yue
 */
public class MOWorkbenchWindow extends JPanel {
    private String currentMoId;
    private ModuleService moduleService;
    private ApplicationService applicationService;

    private JTable jobTable;
    private DefaultTableModel jobTableModel;
    private JTable applicantTable;
    private DefaultTableModel applicantTableModel;
    
    private List<String> moModuleIds;

    public MOWorkbenchWindow(String moId) {
        this.currentMoId = moId;
        this.moduleService = new ModuleService();
        this.applicationService = new ApplicationService();
        this.moModuleIds = new ArrayList<>();
        
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        loadMoModules();
        initComponents();
        refreshJobTable();
    }

    private void loadMoModules() {
        List<String[]> modules = moduleService.getModulesByLeader(currentMoId);
        for (String[] m : modules) {
            moModuleIds.add(m[0]);
        }
        // For testing/admin
        if (moModuleIds.isEmpty() && "ADMIN".equalsIgnoreCase(currentMoId)) {
            List<String[]> allModules = moduleService.getAllModules();
            for (String[] m : allModules) {
                moModuleIds.add(m[0]);
            }
        }
    }

    private void initComponents() {
        // Title
        JLabel titleLabel = new JLabel("MO Workbench");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Main Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(250);
        splitPane.setBackground(Color.WHITE);

        // Top: Job List
        JPanel jobPanel = new JPanel(new BorderLayout());
        jobPanel.setBackground(Color.WHITE);
        jobPanel.setBorder(BorderFactory.createTitledBorder("Your Published Jobs"));

        String[] jobColumns = {"Job ID", "Title", "Category", "Module", "Deadline"};
        jobTableModel = new DefaultTableModel(jobColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        jobTable = new JTable(jobTableModel);
        jobTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                refreshApplicantTable();
            }
        });
        jobPanel.add(new JScrollPane(jobTable), BorderLayout.CENTER);

        // Bottom: Applicant List
        JPanel applicantPanel = new JPanel(new BorderLayout());
        applicantPanel.setBackground(Color.WHITE);
        applicantPanel.setBorder(BorderFactory.createTitledBorder("Applicants for Selected Job"));

        String[] appColumns = {"Student ID", "Status", "Applied Date", "Decision Date"};
        applicantTableModel = new DefaultTableModel(appColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        applicantTable = new JTable(applicantTableModel);
        applicantPanel.add(new JScrollPane(applicantTable), BorderLayout.CENTER);

        // Action Buttons for Applicants
        ButtonPanel actionPanel = new ButtonPanel();
        actionPanel.addPrimaryButton("Approve", e -> handleApproval(true));
        actionPanel.addDangerButton("Reject", e -> handleApproval(false));
        actionPanel.addButton("Refresh", e -> {
            refreshJobTable();
            refreshApplicantTable();
        });
        applicantPanel.add(actionPanel, BorderLayout.SOUTH);

        splitPane.setTopComponent(jobPanel);
        splitPane.setBottomComponent(applicantPanel);

        add(splitPane, BorderLayout.CENTER);
    }

    private void refreshJobTable() {
        jobTableModel.setRowCount(0);
        List<String[]> allJobs = FileUtil.readCSV("jobs.csv");
        if (allJobs.size() <= 1) return;

        for (int i = 1; i < allJobs.size(); i++) {
            String[] job = allJobs.get(i);
            String moduleId = job[3];
            if (moModuleIds.contains(moduleId)) {
                // job: jobId,jobTitle,jobCategory,moduleId,deadline,description
                jobTableModel.addRow(new String[]{job[0], job[1], job[2], job[3], job[4]});
            }
        }
    }

    private void refreshApplicantTable() {
        applicantTableModel.setRowCount(0);
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow == -1) return;

        String jobId = (String) jobTableModel.getValueAt(selectedRow, 0);
        List<String[]> applicants = applicationService.getApplicantsByJob(jobId);
        
        for (String[] app : applicants) {
            // app: studentId, jobId, status, applyDate, decisionDate (if exists)
            String studentId = app[0];
            String status = app[2];
            String applyDate = app[3];
            String decisionDate = app.length > 4 ? app[4] : "-";
            applicantTableModel.addRow(new String[]{studentId, status, applyDate, decisionDate});
        }
    }

    private void handleApproval(boolean approved) {
        int jobRow = jobTable.getSelectedRow();
        int appRow = applicantTable.getSelectedRow();

        if (jobRow == -1 || appRow == -1) {
            MessageDialog.showWarning(this, "Selection Required", "Please select both a job and an applicant.");
            return;
        }

        String jobId = (String) jobTableModel.getValueAt(jobRow, 0);
        String studentId = (String) applicantTableModel.getValueAt(appRow, 0);
        String currentStatus = (String) applicantTableModel.getValueAt(appRow, 1);

        if (!"PENDING".equalsIgnoreCase(currentStatus)) {
            MessageDialog.showWarning(this, "Invalid Action", "This application has already been processed.");
            return;
        }

        String action = approved ? "approve" : "reject";
        if (MessageDialog.confirm(this, "Confirm Action", "Are you sure you want to " + action + " this application?")) {
            if (applicationService.approveApplication(studentId, jobId, approved)) {
                MessageDialog.showSuccess(this, "Application " + (approved ? "approved" : "rejected") + " successfully!");
                refreshApplicantTable();
            } else {
                MessageDialog.showError(this, "Error", "Failed to process application.");
            }
        }
    }
}
