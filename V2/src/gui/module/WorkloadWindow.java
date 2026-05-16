package gui.module;

import gui.MainFrame;
import gui.common.ButtonPanel;
import common.FileUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

public class WorkloadWindow extends JPanel {

    private MainFrame mainFrame;
    private JTable applicationTable;
    private JTable taTable;
    private DefaultTableModel applicationTableModel;
    private DefaultTableModel taTableModel;

    public WorkloadWindow(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
        loadStatistics();
    }

    private void initialize() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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
        backBtn.addActionListener(e -> mainFrame.showAdminMenu());
        leftPanel.add(backBtn);

        JLabel titleLabel = new JLabel("Workload Statistics");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        leftPanel.add(titleLabel);

        topPanel.add(leftPanel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBackground(Color.WHITE);

        JPanel upperPanel = createApplicationPanel();
        centerPanel.add(upperPanel);

        JPanel lowerPanel = createTAPanel();
        centerPanel.add(lowerPanel);

        add(centerPanel, BorderLayout.CENTER);

        ButtonPanel bottomPanel = new ButtonPanel();
        bottomPanel.addButton("Refresh", e -> loadStatistics());
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createApplicationPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Job Application Statistics",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(51, 102, 153)
        ));

        String[] columnNames = {"Job ID", "Job Title", "Total Applications", "Accepted", "Rejected", "Pending"};
        applicationTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        applicationTable = new JTable(applicationTableModel);
        applicationTable.setRowHeight(25);
        applicationTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(applicationTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTAPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "TA Application Statistics",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(51, 102, 153)
        ));

        String[] columnNames = {"User ID", "Name", "Total Applications", "Accepted", "Rejected", "Pending"};
        taTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        taTable = new JTable(taTableModel);
        taTable.setRowHeight(25);
        taTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(taTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadStatistics() {
        applicationTableModel.setRowCount(0);
        taTableModel.setRowCount(0);

        Map<String, JobStats> jobStatsMap = new HashMap<>();
        Map<String, TAStats> taStatsMap = new HashMap<>();
        Map<String, String> jobTitleMap = new HashMap<>();

        java.util.List<String[]> jobs = FileUtil.readCSV("../jobs.csv");
        for (int i = 1; i < jobs.size(); i++) {
            String[] job = jobs.get(i);
            if (job.length > 0) {
                jobTitleMap.put(job[0], job.length > 1 ? job[1] : "Unknown");
            }
        }

        java.util.List<String[]> applications = FileUtil.readCSV("../applications.csv");
        for (int i = 1; i < applications.size(); i++) {
            String[] app = applications.get(i);
            if (app.length < 2) continue;

            String userId = app[0];
            String jobId = app[1];
            String status = app.length > 2 ? app[2] : "PENDING";

            JobStats jobStats = jobStatsMap.getOrDefault(jobId, new JobStats(jobId, jobTitleMap.getOrDefault(jobId, "Unknown")));
            jobStats.total++;
            if ("ACCEPTED".equals(status)) jobStats.accepted++;
            else if ("REJECTED".equals(status)) jobStats.rejected++;
            else jobStats.pending++;
            jobStatsMap.put(jobId, jobStats);

            TAStats taStats = taStatsMap.getOrDefault(userId, new TAStats(userId));
            taStats.total++;
            if ("ACCEPTED".equals(status)) taStats.accepted++;
            else if ("REJECTED".equals(status)) taStats.rejected++;
            else taStats.pending++;
            taStatsMap.put(userId, taStats);
        }

        java.util.List<String[]> users = FileUtil.readCSV("../users.csv");
        for (TAStats taStats : taStatsMap.values()) {
            String name = taStats.userId;
            for (int i = 1; i < users.size(); i++) {
                String[] user = users.get(i);
                if (user.length > 0 && user[0].equals(taStats.userId)) {
                    name = user.length > 1 ? user[1] : taStats.userId;
                    break;
                }
            }
            taStats.name = name;
        }

        for (JobStats stats : jobStatsMap.values()) {
            applicationTableModel.addRow(new Object[]{
                stats.jobId,
                stats.jobTitle,
                stats.total,
                stats.accepted,
                stats.rejected,
                stats.pending
            });
        }

        for (TAStats stats : taStatsMap.values()) {
            taTableModel.addRow(new Object[]{
                stats.userId,
                stats.name,
                stats.total,
                stats.accepted,
                stats.rejected,
                stats.pending
            });
        }
    }

    private static class JobStats {
        String jobId;
        String jobTitle;
        int total;
        int accepted;
        int rejected;
        int pending;

        JobStats(String jobId, String jobTitle) {
            this.jobId = jobId;
            this.jobTitle = jobTitle;
            this.total = 0;
            this.accepted = 0;
            this.rejected = 0;
            this.pending = 0;
        }
    }

    private static class TAStats {
        String userId;
        String name;
        int total;
        int accepted;
        int rejected;
        int pending;

        TAStats(String userId) {
            this.userId = userId;
            this.name = userId;
            this.total = 0;
            this.accepted = 0;
            this.rejected = 0;
            this.pending = 0;
        }
    }
}
