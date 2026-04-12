/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.Axis;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.PlotContent_;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.XYStyler;
import org.knowm.xchart.style.lines.SeriesLines;

public class PlotContent_XY<ST extends AxesChartStyler, S extends Series>
extends PlotContent_ {
    XYStyler stylerXY;

    protected PlotContent_XY(Chart<XYStyler, XYSeries> chart) {
        super(chart);
        this.stylerXY = chart.getStyler();
    }

    @Override
    public void doPaint(Graphics2D g) {
        double xTickSpace = this.stylerXY.getPlotContentSize() * this.getBounds().getWidth();
        double xLeftMargin = Utils.getTickStartOffset((int)this.getBounds().getWidth(), xTickSpace);
        double yTickSpace = this.stylerXY.getPlotContentSize() * this.getBounds().getHeight();
        double yTopMargin = Utils.getTickStartOffset((int)this.getBounds().getHeight(), yTickSpace);
        double xMin = this.chart.getXAxis().getMin();
        double xMax = this.chart.getXAxis().getMax();
        double yMin = this.chart.getYAxis().getMin();
        double yMax = this.chart.getYAxis().getMax();
        if (this.stylerXY.isXAxisLogarithmic()) {
            xMin = Math.log10(xMin);
            xMax = Math.log10(xMax);
        }
        if (this.stylerXY.isYAxisLogarithmic()) {
            yMin = Math.log10(yMin);
            yMax = Math.log10(yMax);
        }
        Map map = this.chart.getSeriesMap();
        for (XYSeries series : map.values()) {
            Collection<?> xData = series.getXData();
            Collection<? extends Number> yData = series.getYData();
            double previousX = -1.7976931348623157E308;
            double previousY = -1.7976931348623157E308;
            Iterator<?> xItr = xData.iterator();
            Iterator<? extends Number> yItr = yData.iterator();
            Iterator<? extends Number> ebItr = null;
            Collection<? extends Number> errorBars = series.getExtraValues();
            if (errorBars != null) {
                ebItr = errorBars.iterator();
            }
            Path2D.Double path = null;
            while (xItr.hasNext()) {
                boolean isSeriesLineOrArea;
                Number next;
                double x = 0.0;
                if (this.chart.getXAxis().getAxisDataType() == Axis.AxisDataType.Number) {
                    x = ((Number)xItr.next()).doubleValue();
                } else if (this.chart.getXAxis().getAxisDataType() == Axis.AxisDataType.Date) {
                    x = ((Date)xItr.next()).getTime();
                }
                if (this.stylerXY.isXAxisLogarithmic()) {
                    x = Math.log10(x);
                }
                if ((next = yItr.next()) == null) {
                    this.closePath(g, path, previousX, this.getBounds(), yTopMargin);
                    path = null;
                    previousX = -1.7976931348623157E308;
                    previousY = -1.7976931348623157E308;
                    continue;
                }
                double yOrig = next.doubleValue();
                double y = 0.0;
                y = this.stylerXY.isYAxisLogarithmic() ? Math.log10(yOrig) : yOrig;
                double xTransform = xLeftMargin + (x - xMin) / (xMax - xMin) * xTickSpace;
                double yTransform = this.getBounds().getHeight() - (yTopMargin + (y - yMin) / (yMax - yMin) * yTickSpace);
                if (Math.abs(xMax - xMin) / 5.0 == 0.0) {
                    xTransform = this.getBounds().getWidth() / 2.0;
                }
                if (Math.abs(yMax - yMin) / 5.0 == 0.0) {
                    yTransform = this.getBounds().getHeight() / 2.0;
                }
                double xOffset = this.getBounds().getX() + xTransform;
                double yOffset = this.getBounds().getY() + yTransform;
                boolean bl = isSeriesLineOrArea = XYSeries.XYSeriesRenderStyle.Line == series.getXYSeriesRenderStyle() || XYSeries.XYSeriesRenderStyle.Area == series.getXYSeriesRenderStyle();
                if (isSeriesLineOrArea && series.getLineStyle() != SeriesLines.NONE && previousX != -1.7976931348623157E308 && previousY != -1.7976931348623157E308) {
                    g.setColor(series.getLineColor());
                    g.setStroke(series.getLineStyle());
                    Line2D.Double line = new Line2D.Double(previousX, previousY, xOffset, yOffset);
                    g.draw(line);
                }
                if (XYSeries.XYSeriesRenderStyle.Area == series.getXYSeriesRenderStyle()) {
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
                previousX = xOffset;
                previousY = yOffset;
                if (series.getMarker() != null) {
                    g.setColor(series.getMarkerColor());
                    series.getMarker().paint(g, xOffset, yOffset, this.stylerXY.getMarkerSize());
                }
                if (errorBars == null) continue;
                double eb = ebItr.next().doubleValue();
                if (this.stylerXY.isErrorBarsColorSeriesColor()) {
                    g.setColor(series.getLineColor());
                } else {
                    g.setColor(this.stylerXY.getErrorBarsColor());
                }
                g.setStroke(this.errorBarStroke);
                double topValue = 0.0;
                if (this.stylerXY.isYAxisLogarithmic()) {
                    topValue = yOrig + eb;
                    topValue = Math.log10(topValue);
                } else {
                    topValue = y + eb;
                }
                double topEBTransform = this.getBounds().getHeight() - (yTopMargin + (topValue - yMin) / (yMax - yMin) * yTickSpace);
                double topEBOffset = this.getBounds().getY() + topEBTransform;
                double bottomValue = 0.0;
                if (this.stylerXY.isYAxisLogarithmic()) {
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

