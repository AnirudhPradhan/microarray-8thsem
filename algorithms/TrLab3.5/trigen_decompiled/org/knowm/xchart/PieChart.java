/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_Pie;
import org.knowm.xchart.internal.chartpart.Plot_Pie;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Theme;

public class PieChart
extends Chart<PieStyler, PieSeries> {
    public PieChart(int width, int height) {
        super(width, height, new PieStyler());
        this.plot = new Plot_Pie(this);
        this.legend = new Legend_Pie(this);
    }

    public PieChart(int width, int height, Theme theme) {
        this(width, height);
        ((PieStyler)this.styler).setTheme(theme);
    }

    public PieChart(int width, int height, Styler.ChartTheme chartTheme) {
        this(width, height, chartTheme.newInstance(chartTheme));
    }

    public PieChart(PieChartBuilder chartBuilder) {
        this(chartBuilder.width, chartBuilder.height, chartBuilder.chartTheme);
        this.setTitle(chartBuilder.title);
    }

    public PieSeries addSeries(String seriesName, Number value) {
        PieSeries series = new PieSeries(seriesName, value);
        if (this.seriesMap.keySet().contains(seriesName)) {
            throw new IllegalArgumentException("Series name >" + seriesName + "< has already been used. Use unique names for each series!!!");
        }
        this.seriesMap.put(seriesName, series);
        return series;
    }

    @Override
    public void paint(Graphics2D g, int width, int height) {
        this.setWidth(width);
        this.setHeight(height);
        this.paint(g);
    }

    @Override
    public void paint(Graphics2D g) {
        for (PieSeries seriesPie : this.getSeriesMap().values()) {
            PieSeries.PieSeriesRenderStyle seriesType = seriesPie.getChartPieSeriesRenderStyle();
            if (seriesType != null) continue;
            seriesPie.setChartPieSeriesRenderStyle(((PieStyler)this.getStyler()).getDefaultSeriesRenderStyle());
        }
        this.setSeriesStyles();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(((PieStyler)this.styler).getChartBackgroundColor());
        Rectangle2D.Double rect = new Rectangle2D.Double(0.0, 0.0, this.getWidth(), this.getHeight());
        g.fill(rect);
        this.plot.paint(g);
        this.chartTitle.paint(g);
        this.legend.paint(g);
        g.dispose();
    }

    public void setSeriesStyles() {
        SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler = new SeriesColorMarkerLineStyleCycler(((PieStyler)this.getStyler()).getSeriesColors(), ((PieStyler)this.getStyler()).getSeriesMarkers(), ((PieStyler)this.getStyler()).getSeriesLines());
        for (Series series : this.getSeriesMap().values()) {
            SeriesColorMarkerLineStyle seriesColorMarkerLineStyle = seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle();
            if (series.getFillColor() != null) continue;
            series.setFillColor(seriesColorMarkerLineStyle.getColor());
        }
    }
}

