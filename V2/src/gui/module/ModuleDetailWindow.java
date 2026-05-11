package gui.module;

import common.FileUtil;
import gui.common.ButtonPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class ModuleDetailWindow extends JDialog {
    private String moduleId;
    private String moduleName;
    private String leaderId;
    private String creationDate;

    public ModuleDetailWindow(String id, String name, String leader, String date) {
        this.moduleId = id;
        this.moduleName = name;
        this.leaderId = leader;
        this.creationDate = date;

        setTitle("Module Details - " + moduleId);
        setSize(650, 500);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {
        // Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Module Information"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        infoPanel.add(createBoldLabel("Module ID:"));
        infoPanel.add(new JLabel(moduleId));
        infoPanel.add(createBoldLabel("Module Name:"));
        infoPanel.add(new JLabel(moduleName));
        infoPanel.add(createBoldLabel("Leader ID:"));
        infoPanel.add(new JLabel(leaderId));
        infoPanel.add(createBoldLabel("Creation Date:"));
        infoPanel.add(new JLabel(creationDate));

        add(infoPanel, BorderLayout.NORTH);

        // Associated Jobs Panel
        JPanel jobsPanel = new JPanel(new BorderLayout(5, 5));
        jobsPanel.setBackground(Color.WHITE);
        jobsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Associated Jobs"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        String[] columnNames = {"Job ID", "Job Title", "Category", "Deadline"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable jobsTable = new JTable(tableModel);
        jobsTable.setRowHeight(22);
        
        // Load associated jobs from jobs.csv
        List<String[]> allJobs = FileUtil.readCSV("jobs.csv");
        int jobCount = 0;
        for (String[] job : allJobs) {
            // Index 3 is moduleId in jobs.csv based on V1 standards
            if (job.length > 3 && job[3].equalsIgnoreCase(moduleId)) {
                tableModel.addRow(new String[]{job[0], job[1], job[2], job.length > 4 ? job[4] : "N/A"});
                jobCount++;
            }
        }

        JLabel countLabel = new JLabel("Total Associated Jobs: " + jobCount);
        countLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        
        jobsPanel.add(new JScrollPane(jobsTable), BorderLayout.CENTER);
        jobsPanel.add(countLabel, BorderLayout.SOUTH);
        
        add(jobsPanel, BorderLayout.CENTER);

        // Close button
        ButtonPanel bottomPanel = new ButtonPanel();
        bottomPanel.addButton("Close", e -> dispose());
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }
}
