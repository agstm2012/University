package com.view;

import com.controller.TellerManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

public class MainWindow extends JFrame {
    public JTextArea outputText;
    public TellerManager tellerManager;

    public MainWindow(TellerManager tellerManager) {
        this.tellerManager = tellerManager;
        initUI();

        setTitle("SMO Project");
        setSize(640, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private void initUI() {
        JPanel panel = new JPanel();
        getContentPane().add(panel);

        JMenuBar menu = new JMenuBar();
        setMenuContent(menu);
        setJMenuBar(menu);

        setPanelContent(panel);
    }

    private void setMenuContent(JMenuBar menu) {
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener(new ExitAction());

        file.add(eMenuItem);
        menu.add(file);
    }


    private void setPanelContent(JPanel panel) {
        panel.setLayout(new BorderLayout());
        setLeftPartPanel(panel);
        setBottomPartPanel(panel);
    }

    private void setBottomPartPanel(JPanel panel) {
        /* result text area */
        outputText = new JTextArea(6, 20);
        JScrollPane scrollingArea = new JScrollPane(outputText);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(scrollingArea, BorderLayout.CENTER);

        panel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setLeftPartPanel(JPanel panel) {
        /* exit button */
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(10, 10, 80, 30);
        exitButton.addActionListener(new ExitAction());
        exitButton.setToolTipText("Exit application");

        JButton suspendButton = new JButton("Pause");
        suspendButton.setBounds(10, 10, 80, 30);
        suspendButton.addActionListener(new SuspendAction());
        suspendButton.setToolTipText("Pause thread");

        JButton resumeButton = new JButton("Resume");
        resumeButton.setBounds(10, 10, 80, 30);
        resumeButton.addActionListener(new ResumeAction());
        resumeButton.setToolTipText("Resume thread");

        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        leftPanel.setLayout(new GridLayout(7, 1, 5, 5));

        leftPanel.add(resumeButton);
        leftPanel.add(suspendButton);
        leftPanel.add(exitButton);

        panel.add(leftPanel, BorderLayout.WEST);
    }


    class ExitAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    class SuspendAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            tellerManager.suspendBlock();
        }
    }

    class ResumeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            tellerManager.resumeBlock();
        }
    }
}
