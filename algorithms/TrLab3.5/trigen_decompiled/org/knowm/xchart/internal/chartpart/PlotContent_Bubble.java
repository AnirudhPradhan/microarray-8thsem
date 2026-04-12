/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.knowm.xchart.BubbleSeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.Axis;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.PlotContent_;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.BubbleStyler;

public class PlotContent_Bubble<ST extends AxesChartStyler, S extends Series>
extends PlotContent_ {
    BubbleStyler stylerBubble;

    protected PlotContent_Bubble(Chart<BubbleStyler, BubbleSeries> chart) {
        super(chart);
        this.stylerBubble = chart.getStyler();
    }

    @Override
    public void doPaint(Graphics2D g) {
        double xTickSpace = this.stylerBubble.getPlotContentSize() * this.getBounds().getWidth();
        double xLeftMargin = Utils.getTickStartOffset((int)this.getBounds().getWidth(), xTickSpace);
        double yTickSpace = this.stylerBubble.getPlotContentSize() * this.getBounds().getHeight();
        double yTopMargin = Utils.getTickStartOffset((int)this.getBounds().getHeight(), yTickSpace);
        double xMin = this.chart.getXAxis().getMin();
        double xMax = this.chart.getXAxis().getMax();
        double yMin = this.chart.getYAxis().getMin();
        double yMax = this.chart.getYAxis().getMax();
        if (this.stylerBubble.isXAxisLogarithmic()) {
            xMin = Math.log10(xMin);
            xMax = Math.log10(xMax);
        }
        if (this.stylerBubble.isYAxisLogarithmic()) {
            yMin = Math.log10(yMin);
            yMax = Math.log10(yMax);
        }
        Map map = this.chart.getSeriesMap();
        for (BubbleSeries series : map.values()) {
            Collection<?> xData = series.getXData();
            Collection<? extends Number> yData = series.getYData();
            double previousX = -1.7976931348623157E308;
            double previousY = -1.7976931348623157E308;
            Iterator<?> xItr = xData.iterator();
            Iterator<? extends Number> yItr = yData.iterator();
            Iterator<? extends Number> bubbleItr = null;
            Collection<? extends Number> bubbles = series.getExtraValues();
            if (bubbles != null) {
                bubbleItr = bubbles.iterator();
            }
            while (xItr.hasNext()) {
                Number next;
                double x = 0.0;
                if (this.chart.getXAxis().getAxisDataType() == Axis.AxisDataType.Number) {
                    x = ((Number)xItr.next()).doubleValue();
                } else if (this.chart.getXAxis().getAxisDataType() == Axis.AxisDataType.Date) {
                    x = ((Date)xItr.next()).getTime();
                }
                if (this.stylerBubble.isXAxisLogarithmic()) {
                    x = Math.log10(x);
                }
                if ((next = yItr.next()) == null) {
                    previousX = -1.7976931348623157E308;
                    previousY = -1.7976931348623157E308;
                    continue;
                }
                double yOrig = next.doubleValue();
                double y = 0.0;
                y = this.stylerBubble.isYAxisLogarithmic() ? Math.log10(yOrig) : yOrig;
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
                previousX = xOffset;
                previousY = yOffset;
                if (bubbles == null) continue;
                double bubbleSize = bubbleItr.next().doubleValue();
                Ellipse2D.Double bubble = null;
                bubble = new Ellipse2D.Double(xOffset - bubbleSize / 2.0, yOffset - bubbleSize / 2.0, bubbleSize, bubbleSize);
                g.setColor(series.getFillColor());
                g.fill(bubble);
                g.setColor(series.getLineColor());
                g.setStroke(series.getLineStyle());
                g.draw(bubble);
            }
        }
    }
}

