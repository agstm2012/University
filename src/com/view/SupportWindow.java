package com.view;

import javax.swing.*;
import java.awt.*;

//Todo окно настроек добавить переключение и все константы.
public class SupportWindow extends JFrame{
    public SupportWindow() {
        initUI();

        setTitle("SMO support");
        setSize(320, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void initUI() {
        JPanel panel = new JPanel();
        getContentPane().add(panel);

        setPanelContent(panel);
    }

    private void setPanelContent(JPanel panel) {
        panel.setLayout(new BorderLayout());

        setLeftContentPanel(panel);
        setCenterContentPanel(panel);
    }

    private void setCenterContentPanel(JPanel panel) {
        JPanel centerPanel = new JPanel();
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.setLayout(new GridLayout(10, 1, 5, 5));

        TextField textCustomersMaxSize = createTextField();
        TextField textTellersMaxSize = createTextField();
        TextField textMaxGenericTime = createTextField();
        TextField textMaxServedTime = createTextField();
        centerPanel.add(textCustomersMaxSize);
        centerPanel.add(textTellersMaxSize);
        centerPanel.add(textMaxGenericTime);
        centerPanel.add(textMaxServedTime);

        panel.add(centerPanel, BorderLayout.CENTER);
    }

    private TextField createTextField() {
        TextField textField = new TextField();
        textField.setSize(140, 16);
        Font font = new Font("Verdana", Font.BOLD, 12);
        textField.setFont(font);
        return textField;
    }

    private void setLeftContentPanel(JPanel panel) {
        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        leftPanel.setLayout(new GridLayout(10, 1, 5, 5));

        Label labelCustomersMaxSize = createLabel("Max size customers");
        Label labelTellersMaxSize = createLabel("Max size tellers");
        Label labelMaxGenericTime = createLabel("Max generic time");
        Label labelMaxServedTime = createLabel("Max served time");
        leftPanel.add(labelCustomersMaxSize);
        leftPanel.add(labelTellersMaxSize);
        leftPanel.add(labelMaxGenericTime);
        leftPanel.add(labelMaxServedTime);

        panel.add(leftPanel, BorderLayout.WEST);
    }
    
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setSize(140, 16);
        Font font = new Font("Verdana", Font.BOLD, 12);
        label.setFont(font);
        return label;
    }
}
