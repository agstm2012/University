package com.view;

import com.controller.CustomerGenerator;
import com.controller.TellerManager;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesMarker;

import javax.swing.*;
import java.awt.*;

public class GraphicsWindow extends JFrame{
    private TellerManager tellerManager;
    private static CustomerGenerator generator;
    private GraphicsPanel graphicsPanel;
    private JPanel topPanel;
    private static boolean onResume;
    private JButton resumeButton;
    private JButton suspendButton;

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
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        topPanel.setLayout(new GridLayout(5, 4, 5, 5));
    }

    public void dispose() {
        super.dispose();
        //if(onResume)
        tellerManager.resumeBlock();
        resumeButton.setEnabled(false);
        suspendButton.setEnabled(true);
    }

    static class GraphicsPanel extends JPanel {
        private int x;
        private int y;

        public GraphicsPanel() {
            setBorder(BorderFactory.createLineBorder(Color.black));
            this.x = 800;
            this.y = 640;
        }
        public Dimension getPreferredSize() {
            return new Dimension(x, y);
        }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            /*double[] xData = new double[] { 1.0 };
            double[] yData = new double[] { 2.0 };
            Chart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);*/

            /*
            C точками
            double[] yData = new double[] { 2.0 };
            Chart chart = new Chart(500, 400);
            chart.setChartTitle("Sample Chart");
            chart.setXAxisTitle("X");
            chart.setYAxisTitle("Y");
            Series series = chart.addSeries("y(x)", null, yData);
            series.setMarker(SeriesMarker.CIRCLE);
            */
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
                createChart(800, 640, data_arr, time_arr, g);
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
}


