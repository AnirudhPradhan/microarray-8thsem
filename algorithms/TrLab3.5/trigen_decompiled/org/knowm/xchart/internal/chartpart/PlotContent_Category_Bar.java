/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
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

public class PlotContent_Category_Bar<ST extends Styler, S extends Series>
extends PlotContent_ {
    CategoryStyler stylerCategory;

    protected PlotContent_Category_Bar(Chart<CategoryStyler, CategorySeries> chart) {
        super(chart);
        this.stylerCategory = chart.getStyler();
    }

    @Override
    public void doPaint(Graphics2D g) {
        double xTickSpace = this.stylerCategory.getPlotContentSize() * this.getBounds().getWidth();
        double xLeftMargin = Utils.getTickStartOffset(this.getBounds().getWidth(), xTickSpace);
        Map seriesMap = this.chart.getSeriesMap();
        int numCategories = ((CategorySeries)seriesMap.values().iterator().next()).getXData().size();
        double gridStep = xTickSpace / (double)numCategories;
        double yMin = this.chart.getAxisPair().getYAxis().getMin();
        double yMax = this.chart.getAxisPair().getYAxis().getMax();
        int chartForm = 1;
        chartForm = yMin > 0.0 && yMax > 0.0 ? 1 : (yMin < 0.0 && yMax < 0.0 ? -1 : 0);
        double yTickSpace = this.stylerCategory.getPlotContentSize() * this.getBounds().getHeight();
        double yTopMargin = Utils.getTickStartOffset(this.getBounds().getHeight(), yTickSpace);
        int seriesCounter = 0;
        for (CategorySeries series : seriesMap.values()) {
            double previousX = -1.7976931348623157E308;
            double previousY = -1.7976931348623157E308;
            Iterator<? extends Number> yItr = series.getYData().iterator();
            Iterator<? extends Number> ebItr = null;
            Collection<? extends Number> errorBars = series.getExtraValues();
            if (errorBars != null) {
                ebItr = errorBars.iterator();
            }
            int categoryCounter = 0;
            block6: while (yItr.hasNext()) {
                double xOffset;
                double barMargin;
                double barWidth;
                double barWidthPercentage;
                Number next = yItr.next();
                if (next == null) {
                    previousX = -1.7976931348623157E308;
                    previousY = -1.7976931348623157E308;
                    ++categoryCounter;
                    continue;
                }
                double y = next.doubleValue();
                double yTop = 0.0;
                double yBottom = 0.0;
                switch (chartForm) {
                    case 1: {
                        if (y < yMin) {
                            ++categoryCounter;
                            continue block6;
                        }
                        yTop = y;
                        yBottom = yMin;
                        break;
                    }
                    case -1: {
                        if (y > yMax) {
                            ++categoryCounter;
                            continue block6;
                        }
                        yTop = yMax;
                        yBottom = y;
                        break;
                    }
                    case 0: {
                        if (y >= 0.0) {
                            yTop = y;
                            yBottom = 0.0;
                            break;
                        }
                        yTop = 0.0;
                        yBottom = y;
                        break;
                    }
                }
                double yTransform = this.getBounds().getHeight() - (yTopMargin + (yTop - yMin) / (yMax - yMin) * yTickSpace);
                double yOffset = this.getBounds().getY() + yTransform;
                double zeroTransform = this.getBounds().getHeight() - (yTopMargin + (yBottom - yMin) / (yMax - yMin) * yTickSpace);
                double zeroOffset = this.getBounds().getY() + zeroTransform;
                if (this.stylerCategory.isOverlapped()) {
                    barWidthPercentage = this.stylerCategory.getAvailableSpaceFill();
                    barWidth = gridStep * barWidthPercentage;
                    barMargin = gridStep * (1.0 - barWidthPercentage) / 2.0;
                    xOffset = this.getBounds().getX() + xLeftMargin + gridStep * (double)categoryCounter++ + barMargin;
                } else {
                    barWidthPercentage = this.stylerCategory.getAvailableSpaceFill();
                    barWidth = gridStep / (double)this.chart.getSeriesMap().size() * barWidthPercentage;
                    barMargin = gridStep * (1.0 - barWidthPercentage) / 2.0;
                    xOffset = this.getBounds().getX() + xLeftMargin + gridStep * (double)categoryCounter++ + (double)seriesCounter * barWidth + barMargin;
                }
                if (series.getChartCategorySeriesRenderStyle() == CategorySeries.CategorySeriesRenderStyle.Bar) {
                    Path2D.Double path = new Path2D.Double();
                    path.moveTo(xOffset, yOffset);
                    path.lineTo(xOffset + barWidth, yOffset);
                    path.lineTo(xOffset + barWidth, zeroOffset);
                    path.lineTo(xOffset, zeroOffset);
                    path.closePath();
                    g.setColor(series.getFillColor());
                    g.fill(path);
                    if (this.stylerCategory.hasAnnotations().booleanValue() && next != null) {
                        DecimalFormat twoPlaces = new DecimalFormat("#.#");
                        if (this.stylerCategory.getYAxisDecimalPattern() != null) {
                            twoPlaces = new DecimalFormat(this.stylerCategory.getYAxisDecimalPattern());
                        }
                        String numberAsString = twoPlaces.format(next);
                        TextLayout textLayout = new TextLayout(numberAsString, this.stylerCategory.getAnnotationsFont(), new FontRenderContext(null, true, false));
                        Rectangle2D annotationRectangle = textLayout.getBounds();
                        double annotationX = xOffset + barWidth / 2.0 - annotationRectangle.getWidth() / 2.0;
                        double annotationY = next.doubleValue() >= 0.0 ? yOffset - 4.0 : zeroOffset + 4.0 + annotationRectangle.getHeight();
                        Shape shape = textLayout.getOutline(null);
                        g.setColor(this.stylerCategory.getChartFontColor());
                        g.setFont(this.stylerCategory.getAnnotationsFont());
                        AffineTransform orig = g.getTransform();
                        AffineTransform at = new AffineTransform();
                        at.translate(annotationX, annotationY);
                        g.transform(at);
                        g.fill(shape);
                        g.setTransform(orig);
                    }
                } else if (CategorySeries.CategorySeriesRenderStyle.Stick.equals(series.getChartCategorySeriesRenderStyle())) {
                    if (series.getLineStyle() != SeriesLines.NONE) {
                        g.setColor(series.getLineColor());
                        g.setStroke(series.getLineStyle());
                        Line2D.Double line = new Line2D.Double(xOffset + barWidth / 2.0, zeroOffset, xOffset + barWidth / 2.0, yOffset);
                        g.draw(line);
                    }
                    if (series.getMarker() != null) {
                        g.setColor(series.getMarkerColor());
                        if (y <= 0.0) {
                            series.getMarker().paint(g, xOffset + barWidth / 2.0, zeroOffset, this.stylerCategory.getMarkerSize());
                        } else {
                            series.getMarker().paint(g, xOffset + barWidth / 2.0, yOffset, this.stylerCategory.getMarkerSize());
                        }
                    }
                } else {
                    if (series.getChartCategorySeriesRenderStyle() == CategorySeries.CategorySeriesRenderStyle.Line && series.getLineStyle() != SeriesLines.NONE && previousX != -1.7976931348623157E308 && previousY != -1.7976931348623157E308) {
                        g.setColor(series.getLineColor());
                        g.setStroke(series.getLineStyle());
                        Line2D.Double line = new Line2D.Double(previousX, previousY, xOffset + barWidth / 2.0, yOffset);
                        g.draw(line);
                    }
                    previousX = xOffset + barWidth / 2.0;
                    previousY = yOffset;
                    if (series.getMarker() != null) {
                        g.setColor(series.getMarkerColor());
                        series.getMarker().paint(g, previousX, previousY, this.stylerCategory.getMarkerSize());
                    }
                }
                if (errorBars == null) continue;
                double eb = ebItr.next().doubleValue();
                if (this.stylerCategory.isErrorBarsColorSeriesColor()) {
                    g.setColor(series.getLineColor());
                } else {
                    g.setColor(this.stylerCategory.getErrorBarsColor());
                }
                g.setStroke(this.errorBarStroke);
                double topValue = y + eb;
                double topEBTransform = this.getBounds().getHeight() - (yTopMargin + (topValue - yMin) / (yMax - yMin) * yTickSpace);
                double topEBOffset = this.getBounds().getY() + topEBTransform;
                double bottomValue = y - eb;
                double bottomEBTransform = this.getBounds().getHeight() - (yTopMargin + (bottomValue - yMin) / (yMax - yMin) * yTickSpace);
                double bottomEBOffset = this.getBounds().getY() + bottomEBTransform;
                double errorBarOffset = xOffset + barWidth / 2.0;
                Line2D.Double line = new Line2D.Double(errorBarOffset, topEBOffset, errorBarOffset, bottomEBOffset);
                g.draw(line);
                line = new Line2D.Double(errorBarOffset - 3.0, bottomEBOffset, errorBarOffset + 3.0, bottomEBOffset);
                g.draw(line);
                line = new Line2D.Double(errorBarOffset - 3.0, topEBOffset, errorBarOffset + 3.0, topEBOffset);
                g.draw(line);
            }
            ++seriesCounter;
        }
    }
}

