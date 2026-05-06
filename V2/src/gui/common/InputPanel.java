package gui.common;

import javax.swing.*;
import java.awt.*;

public class InputPanel extends JPanel {
    
    private JLabel label;
    private JTextField textField;
    
    public InputPanel(String labelText, int columns) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        setBackground(Color.WHITE);
        
        label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setPreferredSize(new Dimension(150, 25));
        
        textField = new JTextField(columns);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(300, 30));
        
        add(label);
        add(textField);
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
}