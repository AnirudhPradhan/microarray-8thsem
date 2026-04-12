/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import java.util.List;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.XYStyler;
import org.knowm.xchart.style.markers.SeriesMarkers;

public final class QuickChart {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private QuickChart() {
    }

    public static XYChart getChart(String chartTitle, String xTitle, String yTitle, String seriesName, double[] xData, double[] yData) {
        double[][] yData2d = new double[][]{yData};
        if (seriesName == null) {
            return QuickChart.getChart(chartTitle, xTitle, yTitle, null, xData, yData2d);
        }
        return QuickChart.getChart(chartTitle, xTitle, yTitle, new String[]{seriesName}, xData, yData2d);
    }

    public static XYChart getChart(String chartTitle, String xTitle, String yTitle, String[] seriesNames, double[] xData, double[][] yData) {
        XYChart chart = new XYChart(600, 400);
        chart.setTitle(chartTitle);
        chart.setXAxisTitle(xTitle);
        chart.setYAxisTitle(yTitle);
        for (int i = 0; i < yData.length; ++i) {
            XYSeries series;
            if (seriesNames != null) {
                series = chart.addSeries(seriesNames[i], xData, yData[i]);
            } else {
                ((XYStyler)chart.getStyler()).setLegendVisible(false);
                series = chart.addSeries(" " + i, xData, yData[i]);
            }
            series.setMarker(SeriesMarkers.NONE);
        }
        return chart;
    }

    public static XYChart getChart(String chartTitle, String xTitle, String yTitle, String seriesName, List<? extends Number> xData, List<? extends Number> yData) {
        XYChart chart = new XYChart(600, 400);
        chart.setTitle(chartTitle);
        chart.setXAxisTitle(xTitle);
        chart.setYAxisTitle(yTitle);
        XYSeries series = chart.addSeries(seriesName, xData, yData);
        series.setMarker(SeriesMarkers.NONE);
        return chart;
    }
}

