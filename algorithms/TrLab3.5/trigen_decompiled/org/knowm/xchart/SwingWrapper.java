/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.internal.chartpart.Chart;

public class SwingWrapper<T extends Chart> {
    private String windowTitle = "XChart";
    private List<T> charts = new ArrayList<T>();
    private int numRows;
    private int numColumns;

    public SwingWrapper(T chart) {
        this.charts.add(chart);
    }

    public SwingWrapper(List<T> charts) {
        this.charts = charts;
        this.numRows = (int)(Math.sqrt(charts.size()) + 0.5);
        this.numColumns = (int)((double)charts.size() / (double)this.numRows + 1.0);
    }

    public SwingWrapper(List<T> charts, int numRows, int numColumns) {
        this.charts = charts;
        this.numRows = numRows;
        this.numColumns = numColumns;
    }

    public JFrame displayChart(String windowTitle) {
        this.windowTitle = windowTitle;
        return this.displayChart();
    }

    public JFrame displayChart() {
        final JFrame frame = new JFrame(this.windowTitle);
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                frame.setDefaultCloseOperation(3);
                XChartPanel<Chart> chartPanel = new XChartPanel<Chart>((Chart)SwingWrapper.this.charts.get(0));
                frame.add(chartPanel);
                frame.pack();
                frame.setVisible(true);
            }
        });
        return frame;
    }

    public JFrame displayChartMatrix(String windowTitle) {
        this.windowTitle = windowTitle;
        return this.displayChartMatrix();
    }

    public JFrame displayChartMatrix() {
        final JFrame frame = new JFrame(this.windowTitle);
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                frame.setDefaultCloseOperation(3);
                frame.getContentPane().setLayout(new GridLayout(SwingWrapper.this.numRows, SwingWrapper.this.numColumns));
                for (Chart chart : SwingWrapper.this.charts) {
                    XChartPanel<Chart> chartPanel;
                    if (chart != null) {
                        chartPanel = new XChartPanel<Chart>(chart);
                        frame.add(chartPanel);
                        continue;
                    }
                    chartPanel = new XChartPanel<Chart>();
                    frame.getContentPane().add(chartPanel);
                }
                frame.pack();
                frame.setVisible(true);
            }
        });
        return frame;
    }
}

