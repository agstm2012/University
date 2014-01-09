package com.view;

import com.controller.TellerManager;
import com.xeiam.xchart.*;

import javax.swing.*;
import java.awt.*;

public class GraphicsWindow extends JFrame{
    private TellerManager tellerManager;

    public GraphicsWindow(TellerManager tellerManager) {
        this.tellerManager = tellerManager;
        tellerManager.suspendBlock();
        initUI();
    }

    private void initUI() {
        GraphicsPanel panel = new GraphicsPanel();
        add(panel);

        setTitle("SMO generateGraphics");
        setSize(800, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void dispose() {
        super.dispose();
        tellerManager.resumeBlock();
    }

    class GraphicsPanel extends JPanel {
        public GraphicsPanel() {
            setBorder(BorderFactory.createLineBorder(Color.black));
        }
        public Dimension getPreferredSize() {
            return new Dimension(800, 640);
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

            System.out.println(MainWindow.model.getRowCount());
            //http://xeiam.com/xchart_examplecode.jsp
            /*
            Chart chart = new Chart(800, 640);
            Series series = chart.addSeries("y(x)", null, new double[] {0, 1});
            series.setMarker(SeriesMarker.CIRCLE);
            Graphics2D lGraphics2D = (Graphics2D) g;
            chart.paint(lGraphics2D);    */
        }
    }
}


