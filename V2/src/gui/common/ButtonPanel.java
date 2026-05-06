package gui.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ButtonPanel extends JPanel {
    
    private List<JButton> buttons;
    
    public ButtonPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        setBackground(Color.WHITE);
        buttons = new ArrayList<>();
    }
    
    public void addButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(new Color(51, 102, 153));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.addActionListener(listener);
        buttons.add(button);
        add(button);
    }
    
    public void addPrimaryButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 45));
        button.setBackground(new Color(0, 102, 51));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.addActionListener(listener);
        buttons.add(button);
        add(button);
    }
    
    public void addDangerButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(new Color(204, 51, 51));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.addActionListener(listener);
        buttons.add(button);
        add(button);
    }
    
    public void clearButtons() {
        for (JButton button : buttons) {
            remove(button);
        }
        buttons.clear();
    }
}