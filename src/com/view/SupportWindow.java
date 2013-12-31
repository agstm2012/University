package com.view;

import javax.swing.*;
import java.awt.*;

//Todo окно настроек
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
        centerPanel.setLayout(new GridLayout(7, 1, 5, 5));
        Label labelCustomerMaxSize = new Label("Max size customers");

        //centerPanel.addElements...
        panel.add(centerPanel, BorderLayout.CENTER);
    }

    private void setLeftContentPanel(JPanel panel) {
        JPanel leftPanel = new JPanel();
        //leftPanel.setSize...
        leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        leftPanel.setLayout(new GridLayout(7, 1, 5, 5));
        //leftPanel.addElements...
        panel.add(leftPanel, BorderLayout.WEST);
    }

    /**
     frame1.setVisible(false); //hides it temporarily
     frame2.setVisible(true); //shows it

     frame1.dispose(); //closes the window--cannot be recovered
     frame2.setVisible(true); //shows it
     */
}
