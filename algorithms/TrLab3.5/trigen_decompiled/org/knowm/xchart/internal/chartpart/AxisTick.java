/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.internal.chartpart.Axis;
import org.knowm.xchart.internal.chartpart.AxisTickLabels;
import org.knowm.xchart.internal.chartpart.AxisTickMarks;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.ChartPart;
import org.knowm.xchart.style.AxesChartStyler;

public class AxisTick<ST extends AxesChartStyler, S extends Series>
implements ChartPart {
    private final Chart<AxesChartStyler, Series_AxesChart> chart;
    private Rectangle2D bounds;
    private final Axis.Direction direction;
    private AxisTickLabels<AxesChartStyler, Series_AxesChart> axisTickLabels;
    private AxisTickMarks<AxesChartStyler, Series_AxesChart> axisTickMarks;

    protected AxisTick(Chart<AxesChartStyler, Series_AxesChart> chart, Axis.Direction direction) {
        this.chart = chart;
        this.direction = direction;
        this.axisTickLabels = new AxisTickLabels(chart, direction);
        this.axisTickMarks = new AxisTickMarks(chart, direction);
    }

    @Override
    public Rectangle2D getBounds() {
        return this.bounds;
    }

    @Override
    public void paint(Graphics2D g) {
        if (this.direction == Axis.Direction.Y && this.chart.getStyler().isYAxisTicksVisible()) {
            this.axisTickLabels.paint(g);
            this.axisTickMarks.paint(g);
            this.bounds = new Rectangle2D.Double(this.axisTickLabels.getBounds().getX(), this.axisTickLabels.getBounds().getY(), this.axisTickLabels.getBounds().getWidth() + (double)this.chart.getStyler().getAxisTickPadding() + this.axisTickMarks.getBounds().getWidth(), this.axisTickMarks.getBounds().getHeight());
        } else if (this.direction == Axis.Direction.X && this.chart.getStyler().isXAxisTicksVisible()) {
            this.axisTickLabels.paint(g);
            this.axisTickMarks.paint(g);
            this.bounds = new Rectangle2D.Double(this.axisTickMarks.getBounds().getX(), this.axisTickMarks.getBounds().getY(), this.axisTickLabels.getBounds().getWidth(), this.axisTickMarks.getBounds().getHeight() + (double)this.chart.getStyler().getAxisTickPadding() + this.axisTickLabels.getBounds().getHeight());
        } else {
            this.bounds = new Rectangle2D.Double();
        }
    }

    protected AxisTickLabels<AxesChartStyler, Series_AxesChart> getAxisTickLabels() {
        return this.axisTickLabels;
    }
}

