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

    public GraphicsWindow(TellerManager tellerManager, CustomerGenerator generator) {
        this.tellerManager = tellerManager;
        this.generator = generator;
        tellerManager.suspendBlock();
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

    }

    public void dispose() {
        super.dispose();
        tellerManager.resumeBlock();
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
            } else {
                double time = generator.getTime();
                double[] time_arr = new double[6];
                double time_part = time / 5;
                time_arr[0] = 0;
                double sum = 0;
                for(int i = 1; i <= 5; i++) {
                    sum = sum + time_part;
                    time_arr[i] = sum;
                }
                /*
                Chart chart = new Chart(780, 620);
                Series series = chart.addSeries("Time", null, time_arr);
                series.setMarker(SeriesMarker.CIRCLE);
                Graphics2D lGraphics2D = (Graphics2D) g;
                chart.paint(lGraphics2D); */
            }
            //http://xeiam.com/xchart_examplecode.jsp
            /*
            Chart chart = new Chart(800, 640);
            Series series = chart.addSeries("y(x)", null, new double[] {0, 1});
            series.setMarker(SeriesMarker.CIRCLE);
            Graphics2D lGraphics2D = (Graphics2D) g;
            chart.paint(lGraphics2D);    */
        }

        public static void createChart() {

        }
    }
}


