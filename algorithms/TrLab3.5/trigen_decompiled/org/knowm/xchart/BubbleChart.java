/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.List;
import org.knowm.xchart.BubbleChartBuilder;
import org.knowm.xchart.BubbleSeries;
import org.knowm.xchart.internal.chartpart.AxisPair;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_Bubble;
import org.knowm.xchart.internal.chartpart.Plot_Bubble;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.BubbleStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Theme;

public class BubbleChart
extends Chart<BubbleStyler, BubbleSeries> {
    public BubbleChart(int width, int height) {
        super(width, height, new BubbleStyler());
        this.axisPair = new AxisPair(this);
        this.plot = new Plot_Bubble(this);
        this.legend = new Legend_Bubble(this);
    }

    public BubbleChart(int width, int height, Theme theme) {
        this(width, height);
        ((BubbleStyler)this.styler).setTheme(theme);
    }

    public BubbleChart(int width, int height, Styler.ChartTheme chartTheme) {
        this(width, height, chartTheme.newInstance(chartTheme));
    }

    public BubbleChart(BubbleChartBuilder chartBuilder) {
        this(chartBuilder.width, chartBuilder.height, chartBuilder.chartTheme);
        this.setTitle(chartBuilder.title);
    }

    public BubbleSeries addSeries(String seriesName, double[] xData, double[] yData, double[] bubbleData) {
        return this.addSeries(seriesName, this.getNumberListFromDoubleArray(xData), this.getNumberListFromDoubleArray(yData), this.getNumberListFromDoubleArray(bubbleData));
    }

    public BubbleSeries addSeries(String seriesName, List<?> xData, List<? extends Number> yData, List<? extends Number> bubbleData) {
        this.sanityCheck(seriesName, xData, yData, bubbleData);
        BubbleSeries series = null;
        if (xData != null) {
            if (xData.size() != yData.size()) {
                throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
            }
            series = new BubbleSeries(seriesName, xData, yData, bubbleData);
        } else {
            series = new BubbleSeries(seriesName, this.getGeneratedData(yData.size()), yData, bubbleData);
        }
        this.seriesMap.put(seriesName, series);
        return series;
    }

    private void sanityCheck(String seriesName, List<?> xData, List<? extends Number> yData, List<? extends Number> bubbleData) {
        if (this.seriesMap.keySet().contains(seriesName)) {
            throw new IllegalArgumentException("Series name >" + seriesName + "< has already been used. Use unique names for each series!!!");
        }
        if (yData == null) {
            throw new IllegalArgumentException("Y-Axis data cannot be null!!! >" + seriesName);
        }
        if (yData.size() == 0) {
            throw new IllegalArgumentException("Y-Axis data cannot be empty!!! >" + seriesName);
        }
        if (xData != null && xData.size() == 0) {
            throw new IllegalArgumentException("X-Axis data cannot be empty!!! >" + seriesName);
        }
        if (bubbleData.size() != yData.size()) {
            throw new IllegalArgumentException("Bubble Data and Y-Axis sizes are not the same!!! >" + seriesName);
        }
    }

    @Override
    public void paint(Graphics2D g, int width, int height) {
        this.setWidth(width);
        this.setHeight(height);
        this.paint(g);
    }

    @Override
    public void paint(Graphics2D g) {
        for (BubbleSeries bubbleSeries : this.getSeriesMap().values()) {
            BubbleSeries.BubbleSeriesRenderStyle seriesType = bubbleSeries.getBubbleSeriesRenderStyle();
            if (seriesType != null) continue;
            bubbleSeries.setBubbleSeriesRenderStyle(((BubbleStyler)this.getStyler()).getDefaultSeriesRenderStyle());
        }
        this.setSeriesStyles();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(((BubbleStyler)this.styler).getChartBackgroundColor());
        Rectangle2D.Double rect = new Rectangle2D.Double(0.0, 0.0, this.getWidth(), this.getHeight());
        g.fill(rect);
        this.axisPair.paint(g);
        this.plot.paint(g);
        this.chartTitle.paint(g);
        this.legend.paint(g);
        g.dispose();
    }

    public void setSeriesStyles() {
        SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler = new SeriesColorMarkerLineStyleCycler(((BubbleStyler)this.getStyler()).getSeriesColors(), ((BubbleStyler)this.getStyler()).getSeriesMarkers(), ((BubbleStyler)this.getStyler()).getSeriesLines());
        for (BubbleSeries series : this.getSeriesMap().values()) {
            SeriesColorMarkerLineStyle seriesColorMarkerLineStyle = seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle();
            float lineWidth = -1.0f;
            if (series.getLineStyle() == null) {
                series.setLineStyle(seriesColorMarkerLineStyle.getStroke());
            }
            if (series.getLineColor() == null) {
                series.setLineColor(seriesColorMarkerLineStyle.getColor());
            }
            if (series.getFillColor() != null) continue;
            series.setFillColor(seriesColorMarkerLineStyle.getColor());
        }
    }
}

