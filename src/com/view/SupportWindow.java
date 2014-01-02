package com.view;

import com.controller.TellerManager;
import com.interfaces.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SupportWindow extends JFrame{
    private TextField textCustomersMaxSize;
    private TextField textTellersMaxSize;
    private TextField textMaxGenericTime;
    private TextField textMaxServedTime;


    private TellerManager tellerManager;

    public SupportWindow(TellerManager tellerManager) {
        this.tellerManager = tellerManager;
        tellerManager.suspendBlock();
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
        setBottomContentPanel(panel);
    }

    private void setBottomContentPanel(JPanel panel) {
        JPanel bottomPanel = new JPanel();

        JButton saveButton = new JButton("Save configuration");
        saveButton.setBounds(10, 10, 80, 30);
        saveButton.addActionListener(new SaveAction());
        saveButton.setToolTipText("Save configuration");
        bottomPanel.add(saveButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setCenterContentPanel(JPanel panel) {
        JPanel centerPanel = new JPanel();
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.setLayout(new GridLayout(10, 1, 5, 5));

        textCustomersMaxSize = createTextField("100");
        textTellersMaxSize = createTextField("2");
        textMaxGenericTime = createTextField("3");
        textMaxServedTime = createTextField("10");
        centerPanel.add(textCustomersMaxSize);
        centerPanel.add(textTellersMaxSize);
        centerPanel.add(textMaxGenericTime);
        centerPanel.add(textMaxServedTime);

        panel.add(centerPanel, BorderLayout.CENTER);
    }

    private TextField createTextField(String data) {
        TextField textField = new TextField();
        textField.setSize(140, 16);
        Font font = new Font("Verdana", Font.BOLD, 12);
        textField.setFont(font);
        textField.setText(data);
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

    class SaveAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Constants.TELLERS_MAX_SIZE = Integer.valueOf(textTellersMaxSize.getText());
            Constants.CUSTOMERS_MAX_SIZE = Integer.valueOf(textCustomersMaxSize.getText());
            Constants.MAX_GENERIC_TIME = Integer.valueOf(textMaxGenericTime.getText());
            Constants.MAX_SERVED_TIME = Integer.valueOf(textMaxServedTime.getText());
            tellerManager.reloadBlock();
            dispose();
        }
    }
}
