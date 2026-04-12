/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.List;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.internal.chartpart.AxisTick;
import org.knowm.xchart.internal.chartpart.AxisTickCalculator_;
import org.knowm.xchart.internal.chartpart.AxisTickCalculator_Category;
import org.knowm.xchart.internal.chartpart.AxisTickCalculator_Date;
import org.knowm.xchart.internal.chartpart.AxisTickCalculator_Logarithmic;
import org.knowm.xchart.internal.chartpart.AxisTickCalculator_Number;
import org.knowm.xchart.internal.chartpart.AxisTitle;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.ChartPart;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.Styler;

public class Axis<ST extends AxesChartStyler, S extends Series>
implements ChartPart {
    private final Chart<AxesChartStyler, Series_AxesChart> chart;
    private Rectangle2D bounds;
    private final AxesChartStyler stylerAxesChart;
    private AxisDataType axisDataType;
    private AxisTitle<AxesChartStyler, Series_AxesChart> axisTitle;
    private AxisTick<AxesChartStyler, Series_AxesChart> axisTick;
    private AxisTickCalculator_ axisTickCalculator;
    private Direction direction;
    private double min;
    private double max;

    public Axis(Chart<AxesChartStyler, Series_AxesChart> chart, Direction direction) {
        this.chart = chart;
        this.stylerAxesChart = chart.getStyler();
        this.direction = direction;
        this.axisTitle = new AxisTitle(chart, direction);
        this.axisTick = new AxisTick(chart, direction);
    }

    protected void resetMinMax() {
        this.min = Double.MAX_VALUE;
        this.max = -1.7976931348623157E308;
    }

    protected void addMinMax(double min, double max) {
        if (this.min == Double.NaN || min < this.min) {
            this.min = min;
        }
        if (this.max == Double.NaN || max > this.max) {
            this.max = max;
        }
    }

    @Override
    public void paint(Graphics2D g) {
        this.bounds = new Rectangle2D.Double();
        if (this.direction == Direction.Y) {
            double xOffset = this.stylerAxesChart.getChartPadding();
            double yOffset = this.chart.getChartTitle().getBounds().getHeight() + (double)this.stylerAxesChart.getChartPadding();
            int i = 1;
            double width = 60.0;
            double height = 0.0;
            do {
                double approximateXAxisWidth = (double)this.chart.getWidth() - width - (this.stylerAxesChart.getLegendPosition() == Styler.LegendPosition.OutsideE ? this.chart.getLegend().getBounds().getWidth() : 0.0) - (double)(2 * this.stylerAxesChart.getChartPadding()) - (double)(this.stylerAxesChart.isYAxisTicksVisible() ? this.stylerAxesChart.getPlotMargin() : 0) - (double)(this.stylerAxesChart.getLegendPosition() == Styler.LegendPosition.OutsideE && this.stylerAxesChart.isLegendVisible() ? this.stylerAxesChart.getChartPadding() : 0);
                height = (double)this.chart.getHeight() - yOffset - this.chart.getXAxis().getXAxisHeightHint(approximateXAxisWidth) - (double)this.stylerAxesChart.getPlotMargin() - (double)this.stylerAxesChart.getChartPadding();
                width = this.getYAxisWidthHint(height);
            } while (i-- > 0);
            this.bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);
            this.axisTitle.paint(g);
            this.axisTick.paint(g);
            width = (this.stylerAxesChart.isYAxisTitleVisible() ? this.axisTitle.getBounds().getWidth() : 0.0) + this.axisTick.getBounds().getWidth();
            this.bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);
        } else {
            double xOffset = this.chart.getYAxis().getBounds().getWidth() + (double)(this.stylerAxesChart.isYAxisTicksVisible() ? this.stylerAxesChart.getPlotMargin() : 0) + (double)this.stylerAxesChart.getChartPadding();
            double yOffset = this.chart.getYAxis().getBounds().getY() + this.chart.getYAxis().getBounds().getHeight() + (double)this.stylerAxesChart.getPlotMargin();
            double width = (double)this.chart.getWidth() - this.chart.getYAxis().getBounds().getWidth() - (this.stylerAxesChart.getLegendPosition() == Styler.LegendPosition.OutsideE ? this.chart.getLegend().getBounds().getWidth() : 0.0) - (double)(2 * this.stylerAxesChart.getChartPadding()) - (double)(this.stylerAxesChart.isYAxisTicksVisible() ? this.stylerAxesChart.getPlotMargin() : 0) - (double)(this.stylerAxesChart.getLegendPosition() == Styler.LegendPosition.OutsideE && this.stylerAxesChart.isLegendVisible() ? this.stylerAxesChart.getChartPadding() : 0);
            double height = (double)this.chart.getHeight() - this.chart.getYAxis().getBounds().getY() - this.chart.getYAxis().getBounds().getHeight() - (double)this.stylerAxesChart.getChartPadding() - (double)this.stylerAxesChart.getPlotMargin();
            this.bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);
            this.axisTickCalculator = this.getAxisTickCalculator(this.bounds.getWidth());
            this.axisTitle.paint(g);
            this.axisTick.paint(g);
        }
    }

    private double getXAxisHeightHint(double workingSpace) {
        double titleHeight = 0.0;
        if (this.chart.getXAxisTitle() != null && !this.chart.getXAxisTitle().trim().equalsIgnoreCase("") && this.stylerAxesChart.isXAxisTitleVisible()) {
            TextLayout textLayout = new TextLayout(this.chart.getXAxisTitle(), this.stylerAxesChart.getAxisTitleFont(), new FontRenderContext(null, true, false));
            Rectangle2D rectangle = textLayout.getBounds();
            titleHeight = rectangle.getHeight() + (double)this.stylerAxesChart.getAxisTitlePadding();
        }
        this.axisTickCalculator = this.getAxisTickCalculator(workingSpace);
        double axisTickLabelsHeight = 0.0;
        if (this.stylerAxesChart.isXAxisTicksVisible()) {
            String sampleLabel = "";
            for (int i = 0; i < this.axisTickCalculator.getTickLabels().size(); ++i) {
                if (this.axisTickCalculator.getTickLabels().get(i) == null || this.axisTickCalculator.getTickLabels().get(i).length() <= sampleLabel.length()) continue;
                sampleLabel = this.axisTickCalculator.getTickLabels().get(i);
            }
            TextLayout textLayout = new TextLayout(sampleLabel.length() == 0 ? " " : sampleLabel, this.stylerAxesChart.getAxisTickLabelsFont(), new FontRenderContext(null, true, false));
            AffineTransform rot = this.stylerAxesChart.getXAxisLabelRotation() == 0 ? null : AffineTransform.getRotateInstance(-1.0 * Math.toRadians(this.stylerAxesChart.getXAxisLabelRotation()));
            Shape shape = textLayout.getOutline(rot);
            Rectangle rectangle = shape.getBounds();
            axisTickLabelsHeight = ((RectangularShape)rectangle).getHeight() + (double)this.stylerAxesChart.getAxisTickPadding() + (double)this.stylerAxesChart.getAxisTickMarkLength();
        }
        return titleHeight + axisTickLabelsHeight;
    }

    private double getYAxisWidthHint(double workingSpace) {
        double titleHeight = 0.0;
        if (this.chart.getyYAxisTitle() != null && !this.chart.getyYAxisTitle().trim().equalsIgnoreCase("") && this.stylerAxesChart.isYAxisTitleVisible()) {
            TextLayout textLayout = new TextLayout(this.chart.getyYAxisTitle(), this.stylerAxesChart.getAxisTitleFont(), new FontRenderContext(null, true, false));
            Rectangle2D rectangle = textLayout.getBounds();
            titleHeight = rectangle.getHeight() + (double)this.stylerAxesChart.getAxisTitlePadding();
        }
        this.axisTickCalculator = this.getAxisTickCalculator(workingSpace);
        double axisTickLabelsHeight = 0.0;
        if (this.stylerAxesChart.isYAxisTicksVisible()) {
            String sampleLabel = "";
            for (int i = 0; i < this.axisTickCalculator.getTickLabels().size(); ++i) {
                if (this.axisTickCalculator.getTickLabels().get(i) == null || this.axisTickCalculator.getTickLabels().get(i).length() <= sampleLabel.length()) continue;
                sampleLabel = this.axisTickCalculator.getTickLabels().get(i);
            }
            TextLayout textLayout = new TextLayout(sampleLabel.length() == 0 ? " " : sampleLabel, this.stylerAxesChart.getAxisTickLabelsFont(), new FontRenderContext(null, true, false));
            Rectangle2D rectangle = textLayout.getBounds();
            axisTickLabelsHeight = rectangle.getWidth() + (double)this.stylerAxesChart.getAxisTickPadding() + (double)this.stylerAxesChart.getAxisTickMarkLength();
        }
        return titleHeight + axisTickLabelsHeight;
    }

    private AxisTickCalculator_ getAxisTickCalculator(double workingSpace) {
        if (this.getDirection() == Direction.X) {
            if (this.stylerAxesChart instanceof CategoryStyler) {
                List categories = (List)this.chart.getSeriesMap().values().iterator().next().getXData();
                AxisDataType axisType = this.chart.getAxisPair().getXAxis().getAxisDataType();
                return new AxisTickCalculator_Category(this.getDirection(), workingSpace, categories, axisType, this.stylerAxesChart);
            }
            if (this.getAxisDataType() == AxisDataType.Date) {
                return new AxisTickCalculator_Date(this.getDirection(), workingSpace, this.min, this.max, this.stylerAxesChart);
            }
            if (this.stylerAxesChart.isXAxisLogarithmic()) {
                return new AxisTickCalculator_Logarithmic(this.getDirection(), workingSpace, this.min, this.max, this.stylerAxesChart);
            }
            return new AxisTickCalculator_Number(this.getDirection(), workingSpace, this.min, this.max, this.stylerAxesChart);
        }
        if (this.stylerAxesChart.isYAxisLogarithmic() && this.getAxisDataType() != AxisDataType.Date) {
            return new AxisTickCalculator_Logarithmic(this.getDirection(), workingSpace, this.min, this.max, this.stylerAxesChart);
        }
        return new AxisTickCalculator_Number(this.getDirection(), workingSpace, this.min, this.max, this.stylerAxesChart);
    }

    protected AxisDataType getAxisDataType() {
        return this.axisDataType;
    }

    public void setAxisDataType(AxisDataType axisDataType) {
        if (axisDataType != null && this.axisDataType != null && this.axisDataType != axisDataType) {
            throw new IllegalArgumentException("Different Axes (e.g. Date, Number, String) cannot be mixed on the same chart!!");
        }
        this.axisDataType = axisDataType;
    }

    protected double getMin() {
        return this.min;
    }

    protected void setMin(double min) {
        this.min = min;
    }

    protected double getMax() {
        return this.max;
    }

    protected void setMax(double max) {
        this.max = max;
    }

    protected AxisTick<AxesChartStyler, Series_AxesChart> getAxisTick() {
        return this.axisTick;
    }

    protected Direction getDirection() {
        return this.direction;
    }

    protected AxisTitle<AxesChartStyler, Series_AxesChart> getAxisTitle() {
        return this.axisTitle;
    }

    public AxisTickCalculator_ getAxisTickCalculator() {
        return this.axisTickCalculator;
    }

    @Override
    public Rectangle2D getBounds() {
        return this.bounds;
    }

    public static enum Direction {
        X,
        Y;

    }

    public static enum AxisDataType {
        Number,
        Date,
        String;

    }
}

