package gui.common;

import javax.swing.*;
import java.awt.*;

public class InputPanel extends JPanel {
    
    private JLabel label;
    private JTextField textField;
    
    public InputPanel(String labelText, int columns) {
        setLayout(new BorderLayout(10, 5));
        setBackground(Color.WHITE);
        
        label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setPreferredSize(new Dimension(350, 30));
        label.setVerticalAlignment(SwingConstants.CENTER);
        
        textField = new JTextField(columns);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(250, 30));
        
        add(label, BorderLayout.WEST);
        add(textField, BorderLayout.CENTER);
    }
    
    public String getText() {
        return textField.getText().trim();
    }
    
    public void setText(String text) {
        textField.setText(text);
    }
    
    public JTextField getTextField() {
        return textField;
    }

    public JLabel getLabel() {
        return label;
    }
}