package gui.module;

import gui.common.ButtonPanel;
import gui.common.InputPanel;
import gui.common.MessageDialog;
import module.ModuleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class ModuleManageWindow extends JPanel {
    private ModuleService moduleService;
    private JTable moduleTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;

    public ModuleManageWindow() {
        this.moduleService = new ModuleService();
        // Set current role to ADMIN for permission check in ModuleService
        this.moduleService.setCurrentRole("ADMIN");
        
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
        refreshTable();
    }

    private void initComponents() {
        // Top Panel: Title and Search
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Module Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);

        searchTypeCombo = new JComboBox<>(new String[]{"Search by Name", "Search by Leader ID"});
        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> performSearch());

        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        topPanel.add(searchPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Table
        String[] columnNames = {"Module ID", "Module Name", "Leader ID", "Creation Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        moduleTable = new JTable(tableModel);
        moduleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moduleTable.setRowHeight(25);
        moduleTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(moduleTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel: Actions
        ButtonPanel actionPanel = new ButtonPanel();
        actionPanel.addButton("Refresh", e -> refreshTable());
        actionPanel.addButton("View Details", e -> showDetails());
        actionPanel.addPrimaryButton("Add Module", e -> showAddDialog());
        actionPanel.addButton("Edit Module", e -> showEditDialog());
        actionPanel.addDangerButton("Delete Module", e -> performDelete());

        add(actionPanel, BorderLayout.SOUTH);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<String[]> modules = moduleService.getAllModules();
        for (String[] module : modules) {
            tableModel.addRow(module);
        }
    }

    private void performSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            refreshTable();
            return;
        }

        int searchType = searchTypeCombo.getSelectedIndex(); // 0: Name, 1: Leader
        tableModel.setRowCount(0);
        List<String[]> modules = moduleService.getAllModules();
        for (String[] module : modules) {
            boolean match = false;
            if (searchType == 0 && module[1].toLowerCase().contains(keyword)) {
                match = true;
            } else if (searchType == 1 && module[2].equalsIgnoreCase(keyword)) {
                match = true;
            }

            if (match) {
                tableModel.addRow(module);
            }
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Module", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);

        JPanel content = new JPanel(new GridLayout(0, 1, 10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setBackground(Color.WHITE);

        InputPanel idPanel = new InputPanel("Module ID (Mxxx):", 20);
        InputPanel namePanel = new InputPanel("Module Name:", 20);
        InputPanel leaderPanel = new InputPanel("Leader ID (Student ID):", 20);

        content.add(idPanel);
        content.add(namePanel);
        content.add(leaderPanel);

        ButtonPanel buttons = new ButtonPanel();
        buttons.addPrimaryButton("Save", e -> {
            String id = idPanel.getText();
            String name = namePanel.getText();
            String leader = leaderPanel.getText();

            if (moduleService.addModule(id, name, leader)) {
                MessageDialog.showSuccess(dialog, "Module added successfully!");
                dialog.dispose();
                refreshTable();
            } else {
                MessageDialog.showError(dialog, "Error", "Failed to add module. Check:\n1. ID format (e.g. M001)\n2. ID uniqueness\n3. Leader ID validity (MO role required)");
            }
        });
        buttons.addButton("Cancel", e -> dialog.dispose());

        dialog.add(content, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int selectedRow = moduleTable.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialog.showWarning(this, "Warning", "Please select a module to edit.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        String currentLeader = (String) tableModel.getValueAt(selectedRow, 2);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Module", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);

        JPanel content = new JPanel(new GridLayout(0, 1, 10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setBackground(Color.WHITE);

        InputPanel idPanel = new InputPanel("Module ID:", 20);
        idPanel.setText(id);
        idPanel.getTextField().setEditable(false);
        
        InputPanel namePanel = new InputPanel("Module Name:", 20);
        namePanel.setText(currentName);
        
        InputPanel leaderPanel = new InputPanel("Leader ID:", 20);
        leaderPanel.setText(currentLeader);

        content.add(idPanel);
        content.add(namePanel);
        content.add(leaderPanel);

        ButtonPanel buttons = new ButtonPanel();
        buttons.addPrimaryButton("Update", e -> {
            String name = namePanel.getText();
            String leader = leaderPanel.getText();

            if (moduleService.updateModule(id, name, leader)) {
                MessageDialog.showSuccess(dialog, "Module updated successfully!");
                dialog.dispose();
                refreshTable();
            } else {
                MessageDialog.showError(dialog, "Error", "Failed to update module. Ensure Leader ID is valid and has MO role.");
            }
        });
        buttons.addButton("Cancel", e -> dialog.dispose());

        dialog.add(content, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void performDelete() {
        int selectedRow = moduleTable.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialog.showWarning(this, "Warning", "Please select a module to delete.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        if (MessageDialog.confirm(this, "Confirm Delete", "Are you sure you want to delete module " + id + "?\nThis action cannot be undone.")) {
            if (moduleService.deleteModule(id)) {
                MessageDialog.showSuccess(this, "Module deleted successfully!");
                refreshTable();
            } else {
                MessageDialog.showError(this, "Error", "Failed to delete module.");
            }
        }
    }

    private void showDetails() {
        int selectedRow = moduleTable.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialog.showWarning(this, "Warning", "Please select a module to view details.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        String leader = (String) tableModel.getValueAt(selectedRow, 2);
        String date = (String) tableModel.getValueAt(selectedRow, 3);

        ModuleDetailWindow detailWindow = new ModuleDetailWindow(id, name, leader, date);
        detailWindow.setVisible(true);
    }
}
