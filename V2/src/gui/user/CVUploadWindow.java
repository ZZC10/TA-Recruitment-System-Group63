package gui.user;

import gui.MainFrame;
import gui.common.InputPanel;
import gui.common.ButtonPanel;
import gui.common.MessageDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class CVUploadWindow extends JPanel {

    private MainFrame mainFrame;
    private InputPanel intentionPanel;
    private InputPanel experiencePanel;
    private InputPanel skillsPanel;
    private JLabel fileLabel;
    private JProgressBar progressBar;
    private JLabel previewLabel;
    private File selectedFile;

    public CVUploadWindow(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.6);
        splitPane.setBorder(null);

        JPanel upperPanel = createUpperPanel();
        splitPane.setTopComponent(upperPanel);

        JPanel lowerPanel = createLowerPanel();
        splitPane.setBottomComponent(lowerPanel);

        add(splitPane, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Upload / Update CV");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(51, 102, 153));
        panel.add(titleLabel);

        return panel;
    }

    private JPanel createUpperPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "CV Information",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(51, 102, 153)
        ));

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        intentionPanel = new InputPanel("Intention (e.g., Software Development TA):", 30);
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(intentionPanel, gbc);

        experiencePanel = new InputPanel("Experience (e.g., Internship, Projects):", 30);
        gbc.gridx = 0;
        gbc.gridy = 1;
        infoPanel.add(experiencePanel, gbc);

        skillsPanel = new InputPanel("Skills (e.g., Java, Python, Communication):", 30);
        gbc.gridx = 0;
        gbc.gridy = 2;
        infoPanel.add(skillsPanel, gbc);

        panel.add(infoPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLowerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "File Upload",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(51, 102, 153)
        ));

        JPanel filePanel = new JPanel(new BorderLayout(10, 10));
        filePanel.setBackground(Color.WHITE);
        filePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel fileSelectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        fileSelectPanel.setBackground(Color.WHITE);

        JButton selectFileButton = new JButton("Select File");
        selectFileButton.setFont(new Font("Arial", Font.PLAIN, 14));
        selectFileButton.setPreferredSize(new Dimension(120, 35));
        selectFileButton.setBackground(new Color(51, 102, 153));
        selectFileButton.setForeground(Color.WHITE);
        selectFileButton.setFocusPainted(false);
        selectFileButton.setBorderPainted(false);
        selectFileButton.setOpaque(true);
        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFile();
            }
        });

        fileLabel = new JLabel("No file selected");
        fileLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        fileLabel.setForeground(new Color(102, 102, 102));

        fileSelectPanel.add(selectFileButton);
        fileSelectPanel.add(fileLabel);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        previewLabel = new JLabel("<html><center><i>CV Preview Area</i></center></html>", SwingConstants.CENTER);
        previewLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        previewLabel.setForeground(new Color(153, 153, 153));
        previewLabel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        previewLabel.setPreferredSize(new Dimension(0, 100));

        filePanel.add(fileSelectPanel, BorderLayout.NORTH);
        filePanel.add(progressBar, BorderLayout.CENTER);
        filePanel.add(previewLabel, BorderLayout.SOUTH);

        panel.add(filePanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomPanel() {
        ButtonPanel buttonPanel = new ButtonPanel();

        buttonPanel.addPrimaryButton("Upload CV", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadCV();
            }
        });

        buttonPanel.addButton("Back", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });

        return buttonPanel;
    }

    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select CV File");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Document Files (*.pdf, *.doc, *.docx)", "pdf", "doc", "docx"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            fileLabel.setText(selectedFile.getName());
            previewLabel.setText("<html><center><b>File selected:</b><br>" + selectedFile.getName() + "</center></html>");
            previewLabel.setForeground(new Color(51, 102, 153));
        }
    }

    private void uploadCV() {
        String intention = intentionPanel.getText();
        String experience = experiencePanel.getText();
        String skills = skillsPanel.getText();

        if (intention.isEmpty() || experience.isEmpty() || skills.isEmpty()) {
            MessageDialog.showWarning(this, "Warning", "Please fill in all CV information fields!");
            return;
        }

        progressBar.setVisible(true);
        progressBar.setValue(0);

        Timer timer = new Timer(50, new ActionListener() {
            int progress = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 5;
                progressBar.setValue(progress);
                if (progress >= 100) {
                    ((Timer) e.getSource()).stop();
                    progressBar.setVisible(false);
                    MessageDialog.showSuccess(CVUploadWindow.this, "CV uploaded successfully!");
                    clearFields();
                }
            }
        });
        timer.start();
    }

    private void clearFields() {
        intentionPanel.setText("");
        experiencePanel.setText("");
        skillsPanel.setText("");
        fileLabel.setText("No file selected");
        previewLabel.setText("<html><center><i>CV Preview Area</i></center></html>");
        previewLabel.setForeground(new Color(153, 153, 153));
        selectedFile = null;
    }

    private void goBack() {
        mainFrame.showWelcomePanel();
    }
}
