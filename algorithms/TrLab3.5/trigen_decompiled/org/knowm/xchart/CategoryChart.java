/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.List;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.internal.chartpart.AxisPair;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_Marker;
import org.knowm.xchart.internal.chartpart.Plot_Category;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Theme;

public class CategoryChart
extends Chart<CategoryStyler, CategorySeries> {
    public CategoryChart(int width, int height) {
        super(width, height, new CategoryStyler());
        this.axisPair = new AxisPair(this);
        this.plot = new Plot_Category(this);
        this.legend = new Legend_Marker(this);
    }

    public CategoryChart(int width, int height, Theme theme) {
        this(width, height);
        ((CategoryStyler)this.styler).setTheme(theme);
    }

    public CategoryChart(int width, int height, Styler.ChartTheme chartTheme) {
        this(width, height, chartTheme.newInstance(chartTheme));
    }

    public CategoryChart(CategoryChartBuilder chartBuilder) {
        this(chartBuilder.width, chartBuilder.height, chartBuilder.chartTheme);
        this.setTitle(chartBuilder.title);
        this.setXAxisTitle(chartBuilder.xAxisTitle);
        this.setYAxisTitle(chartBuilder.yAxisTitle);
    }

    public CategorySeries addSeries(String seriesName, double[] xData, double[] yData) {
        return this.addSeries(seriesName, xData, yData, null);
    }

    public CategorySeries addSeries(String seriesName, double[] xData, double[] yData, double[] errorBars) {
        return this.addSeries(seriesName, this.getNumberListFromDoubleArray(xData), this.getNumberListFromDoubleArray(yData), this.getNumberListFromDoubleArray(errorBars));
    }

    public CategorySeries addSeries(String seriesName, int[] xData, int[] yData) {
        return this.addSeries(seriesName, xData, yData, (int[])null);
    }

    public CategorySeries addSeries(String seriesName, int[] xData, int[] yData, int[] errorBars) {
        return this.addSeries(seriesName, this.getNumberListFromIntArray(xData), this.getNumberListFromIntArray(yData), this.getNumberListFromIntArray(errorBars));
    }

    public CategorySeries addSeries(String seriesName, List<?> xData, List<? extends Number> yData) {
        return this.addSeries(seriesName, xData, yData, null);
    }

    public CategorySeries addSeries(String seriesName, List<?> xData, List<? extends Number> yData, List<? extends Number> errorBars) {
        this.sanityCheck(seriesName, xData, yData, errorBars);
        CategorySeries series = null;
        if (xData != null) {
            if (xData.size() != yData.size()) {
                throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
            }
            series = new CategorySeries(seriesName, xData, yData, errorBars);
        } else {
            series = new CategorySeries(seriesName, this.getGeneratedData(yData.size()), yData, errorBars);
        }
        this.seriesMap.put(seriesName, series);
        return series;
    }

    private void sanityCheck(String seriesName, List<?> xData, List<? extends Number> yData, List<? extends Number> errorBars) {
        if (this.seriesMap.keySet().contains(seriesName)) {
            throw new IllegalArgumentException("Series name >" + seriesName + "< has already been used. Use unique names for each series!!!");
        }
        if (yData == null) {
            throw new IllegalArgumentException("Y-Axis data cannot be null!!!");
        }
        if (yData.size() == 0) {
            throw new IllegalArgumentException("Y-Axis data cannot be empty!!!");
        }
        if (xData != null && xData.size() == 0) {
            throw new IllegalArgumentException("X-Axis data cannot be empty!!!");
        }
        if (errorBars != null && errorBars.size() != yData.size()) {
            throw new IllegalArgumentException("Error bars and Y-Axis sizes are not the same!!!");
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
        for (CategorySeries seriesCategory : this.getSeriesMap().values()) {
            CategorySeries.CategorySeriesRenderStyle seriesType = seriesCategory.getChartCategorySeriesRenderStyle();
            if (seriesType != null) continue;
            seriesCategory.setChartCategorySeriesRenderStyle(((CategoryStyler)this.getStyler()).getDefaultSeriesRenderStyle());
        }
        this.setSeriesStyles();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(((CategoryStyler)this.styler).getChartBackgroundColor());
        Rectangle2D.Double rect = new Rectangle2D.Double(0.0, 0.0, this.getWidth(), this.getHeight());
        g.fill(rect);
        this.axisPair.paint(g);
        this.plot.paint(g);
        this.chartTitle.paint(g);
        this.legend.paint(g);
        g.dispose();
    }

    public void setSeriesStyles() {
        SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler = new SeriesColorMarkerLineStyleCycler(((CategoryStyler)this.getStyler()).getSeriesColors(), ((CategoryStyler)this.getStyler()).getSeriesMarkers(), ((CategoryStyler)this.getStyler()).getSeriesLines());
        for (CategorySeries series : this.getSeriesMap().values()) {
            SeriesColorMarkerLineStyle seriesColorMarkerLineStyle = seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle();
            if (series.getLineStyle() == null) {
                series.setLineStyle(seriesColorMarkerLineStyle.getStroke());
            }
            if (series.getLineColor() == null) {
                series.setLineColor(seriesColorMarkerLineStyle.getColor());
            }
            if (series.getFillColor() == null) {
                series.setFillColor(seriesColorMarkerLineStyle.getColor());
            }
            if (series.getMarker() == null) {
                series.setMarker(seriesColorMarkerLineStyle.getMarker());
            }
            if (series.getMarkerColor() != null) continue;
            series.setMarkerColor(seriesColorMarkerLineStyle.getColor());
        }
    }
}

