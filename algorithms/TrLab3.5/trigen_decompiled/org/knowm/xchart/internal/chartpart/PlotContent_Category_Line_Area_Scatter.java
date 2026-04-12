/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.PlotContent_;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.lines.SeriesLines;

public class PlotContent_Category_Line_Area_Scatter<ST extends Styler, S extends Series>
extends PlotContent_ {
    CategoryStyler stylerCategory;

    protected PlotContent_Category_Line_Area_Scatter(Chart<CategoryStyler, CategorySeries> chart) {
        super(chart);
        this.stylerCategory = chart.getStyler();
    }

    @Override
    public void doPaint(Graphics2D g) {
        double xTickSpace = this.stylerCategory.getPlotContentSize() * this.getBounds().getWidth();
        double xLeftMargin = Utils.getTickStartOffset((int)this.getBounds().getWidth(), xTickSpace);
        double yTickSpace = this.stylerCategory.getPlotContentSize() * this.getBounds().getHeight();
        double yTopMargin = Utils.getTickStartOffset((int)this.getBounds().getHeight(), yTickSpace);
        double xMin = this.chart.getAxisPair().getXAxis().getMin();
        double xMax = this.chart.getAxisPair().getXAxis().getMax();
        double yMin = this.chart.getAxisPair().getYAxis().getMin();
        double yMax = this.chart.getAxisPair().getYAxis().getMax();
        if (this.stylerCategory.isXAxisLogarithmic()) {
            xMin = Math.log10(xMin);
            xMax = Math.log10(xMax);
        }
        if (this.stylerCategory.isYAxisLogarithmic()) {
            yMin = Math.log10(yMin);
            yMax = Math.log10(yMax);
        }
        Map seriesMap = this.chart.getSeriesMap();
        int numCategories = ((CategorySeries)seriesMap.values().iterator().next()).getXData().size();
        double gridStep = xTickSpace / (double)numCategories;
        for (CategorySeries series : seriesMap.values()) {
            Collection<? extends Number> yData = series.getYData();
            double previousX = -1.7976931348623157E308;
            double previousY = -1.7976931348623157E308;
            Iterator<? extends Number> yItr = yData.iterator();
            Iterator<? extends Number> ebItr = null;
            Collection<? extends Number> errorBars = series.getExtraValues();
            if (errorBars != null) {
                ebItr = errorBars.iterator();
            }
            Path2D.Double path = null;
            int categoryCounter = 0;
            while (yItr.hasNext()) {
                Number next = yItr.next();
                if (next == null) {
                    this.closePath(g, path, previousX, this.getBounds(), yTopMargin);
                    path = null;
                    previousX = -1.7976931348623157E308;
                    previousY = -1.7976931348623157E308;
                    continue;
                }
                double yOrig = next.doubleValue();
                double y = 0.0;
                y = this.stylerCategory.isYAxisLogarithmic() ? Math.log10(yOrig) : yOrig;
                double yTransform = this.getBounds().getHeight() - (yTopMargin + (y - yMin) / (yMax - yMin) * yTickSpace);
                if (Math.abs(yMax - yMin) / 5.0 == 0.0) {
                    yTransform = this.getBounds().getHeight() / 2.0;
                }
                double xOffset = this.getBounds().getX() + xLeftMargin + (double)categoryCounter++ * gridStep + gridStep / 2.0;
                double yOffset = this.getBounds().getY() + yTransform;
                if ((CategorySeries.CategorySeriesRenderStyle.Line.equals(series.getChartCategorySeriesRenderStyle()) || CategorySeries.CategorySeriesRenderStyle.Area.equals(series.getChartCategorySeriesRenderStyle())) && series.getLineStyle() != SeriesLines.NONE && previousX != -1.7976931348623157E308 && previousY != -1.7976931348623157E308) {
                    g.setColor(series.getLineColor());
                    g.setStroke(series.getLineStyle());
                    Line2D.Double line = new Line2D.Double(previousX, previousY, xOffset, yOffset);
                    g.draw(line);
                }
                if (CategorySeries.CategorySeriesRenderStyle.Area.equals(series.getChartCategorySeriesRenderStyle())) {
                    if (previousX != -1.7976931348623157E308 && previousY != -1.7976931348623157E308) {
                        g.setColor(series.getFillColor());
                        double yBottomOfArea = this.getBounds().getY() + this.getBounds().getHeight() - yTopMargin;
                        if (path == null) {
                            path = new Path2D.Double();
                            path.moveTo(previousX, yBottomOfArea);
                            path.lineTo(previousX, previousY);
                        }
                        path.lineTo(xOffset, yOffset);
                    }
                    if (xOffset < previousX) {
                        throw new RuntimeException("X-Data must be in ascending order for Area Charts!!!");
                    }
                }
                if (CategorySeries.CategorySeriesRenderStyle.Stick.equals(series.getChartCategorySeriesRenderStyle()) && series.getLineStyle() != SeriesLines.NONE) {
                    double yBottomOfArea = this.getBounds().getY() + this.getBounds().getHeight() - yTopMargin;
                    g.setColor(series.getLineColor());
                    g.setStroke(series.getLineStyle());
                    Line2D.Double line = new Line2D.Double(xOffset, yBottomOfArea, xOffset, yOffset);
                    g.draw(line);
                }
                previousX = xOffset;
                previousY = yOffset;
                if (series.getMarker() != null) {
                    g.setColor(series.getMarkerColor());
                    series.getMarker().paint(g, xOffset, yOffset, this.stylerCategory.getMarkerSize());
                }
                if (errorBars == null) continue;
                double eb = ebItr.next().doubleValue();
                if (this.stylerCategory.isErrorBarsColorSeriesColor()) {
                    g.setColor(series.getLineColor());
                } else {
                    g.setColor(this.stylerCategory.getErrorBarsColor());
                }
                g.setStroke(this.errorBarStroke);
                double topValue = 0.0;
                if (this.stylerCategory.isYAxisLogarithmic()) {
                    topValue = yOrig + eb;
                    topValue = Math.log10(topValue);
                } else {
                    topValue = y + eb;
                }
                double topEBTransform = this.getBounds().getHeight() - (yTopMargin + (topValue - yMin) / (yMax - yMin) * yTickSpace);
                double topEBOffset = this.getBounds().getY() + topEBTransform;
                double bottomValue = 0.0;
                if (this.stylerCategory.isYAxisLogarithmic()) {
                    bottomValue = yOrig - eb;
                    bottomValue = Math.log10(bottomValue);
                } else {
                    bottomValue = y - eb;
                }
                double bottomEBTransform = this.getBounds().getHeight() - (yTopMargin + (bottomValue - yMin) / (yMax - yMin) * yTickSpace);
                double bottomEBOffset = this.getBounds().getY() + bottomEBTransform;
                Line2D.Double line = new Line2D.Double(xOffset, topEBOffset, xOffset, bottomEBOffset);
                g.draw(line);
                line = new Line2D.Double(xOffset - 3.0, bottomEBOffset, xOffset + 3.0, bottomEBOffset);
                g.draw(line);
                line = new Line2D.Double(xOffset - 3.0, topEBOffset, xOffset + 3.0, topEBOffset);
                g.draw(line);
            }
            this.closePath(g, path, previousX, this.getBounds(), yTopMargin);
        }
    }
}

