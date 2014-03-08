package com.view;

import com.controller.CustomerGenerator;
import com.controller.TellerManager;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesMarker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class GraphicsWindow extends JFrame implements ActionListener{
    private TellerManager tellerManager;
    private static CustomerGenerator generator;
    private GraphicsPanel graphicsPanel;
    private JPanel topPanel;
    private static boolean onResume;
    private JButton resumeButton;
    private JButton suspendButton;
    private String[] сheckboxes;
    private List<JCheckBox> boxList;

    public GraphicsWindow(TellerManager tellerManager, CustomerGenerator generator,JButton resumeButton, JButton suspendButton) {
        this.resumeButton = resumeButton;
        this.suspendButton = suspendButton;
        this.generator = generator;
        this.tellerManager = tellerManager;
        initUI();

        setTitle("SMO generateGraphics");
        setSize(800, 640);
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

        setTopContentPanel(panel);
        setCenterContentPanel(panel);
    }

    private void setCenterContentPanel(JPanel panel) {
        graphicsPanel = new GraphicsPanel();
        panel.add(graphicsPanel, BorderLayout.SOUTH);
    }

    private void setTopContentPanel(JPanel panel) {
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        JPanel checksPanel = new JPanel();
        checksPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        checksPanel.setLayout(new GridLayout(4, 4, 4, 4));
        сheckboxes = new String[]{
            "λ", "t", "μ", "ρ", "ρ0", "tпр", "ρотк", "Q", "nз", "nпр", "K3", "A", "tпр", "Lобс"
        };
        boxList = new ArrayList<JCheckBox>();
        for(int i = 0; i < сheckboxes.length; i++) {
            JCheckBox checkbox = new JCheckBox(сheckboxes[i], true);
            if(i == 0)
                checkbox.setSelected(true);
            else
                checkbox.setSelected(false);
            checkbox.setFocusable(false);
            checkbox.addActionListener(this);
            checksPanel.add(checkbox);
            boxList.add(checkbox);
        }
        checksPanel.setSize(800, 87);


        JPanel buttonPanel = new JPanel();
        JButton button = new JButton("Create chart");
        button.addActionListener(null);
        buttonPanel.add(button);
        buttonPanel.setSize(800, 13);

        topPanel.setSize(800, 100);
        topPanel.add(checksPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.SOUTH);
    }

    public void dispose() {
        super.dispose();
        //if(onResume)
        tellerManager.resumeBlock();
        resumeButton.setEnabled(false);
        suspendButton.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JCheckBox source = (JCheckBox) e.getSource();
        boolean state = source.isSelected();
        String name = source.getText();

        if (state) {
            for(int i = 0; i < boxList.size(); i++) {
                if(!boxList.get(i).getText().equals(name))
                    boxList.get(i).setSelected(false);
            }
        } else {
            //this.setTitle("");
        }
    }

    static class GraphicsPanel extends JPanel {
        private int x;
        private int y;

        public GraphicsPanel() {
            setBorder(BorderFactory.createLineBorder(Color.black));
            this.x = 800;
            this.y = 515;
        }
        public Dimension getPreferredSize() {
            return new Dimension(x, y);
        }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int count = MainWindow.model.getRowCount();
            if(count == 0) {
                g.setFont(new Font("Candara", Font.BOLD, 15));
                g.setColor(Color.BLACK);
                g.setPaintMode();
                String text = "Таблица результатов пуста. Необходимо заполнить таблицу результатов чтобы построить график.";
                g.drawString(text, x / 2 - ((text.length() / 4) * 15), y / 2);
                onResume = true;
            } else {
                onResume = false;
                System.out.println("\n\n\n\n\n\n\n\n\n");
                int size = MainWindow.model.getRowCount();
                double time_arr[] = new double[size];
                double data_arr[] = new double[size];
                for(int i = 0; i < size; i++) {
                    time_arr[i] = Double.valueOf(MainWindow.model.getValueAt(i, 7).toString());
                    data_arr[i] = Double.valueOf(MainWindow.model.getValueAt(i, 15).toString());
                }
                createChart(800, 515, data_arr, time_arr, g);
            }
            //http://xeiam.com/xchart_examplecode.jsp
        }

        public static void createChart(int width, int height, double[] data_arr, double[] time_arr,Graphics g) {
            Chart chart = new Chart(width, height);
            Series series = chart.addSeries("Time", data_arr, time_arr);
            series.setMarker(SeriesMarker.CIRCLE);
            Graphics2D lGraphics2D = (Graphics2D) g;
            chart.paint(lGraphics2D);
        }
    }

    class CreateChartListener implements ActionListener {

        public CreateChartListener() {

        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}


