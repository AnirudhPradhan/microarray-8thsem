/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.knowm.xchart.internal.Series_Markers;
import org.knowm.xchart.internal.chartpart.Axis;
import org.knowm.xchart.internal.chartpart.RenderableSeries;

public class XYSeries
extends Series_Markers {
    private XYSeriesRenderStyle xyChartSeriesRenderStyle = null;

    public XYSeries(String name, List<?> xData, List<? extends Number> yData, List<? extends Number> errorBars) {
        super(name, xData, yData, errorBars);
    }

    public XYSeriesRenderStyle getXYSeriesRenderStyle() {
        return this.xyChartSeriesRenderStyle;
    }

    public XYSeries setXYSeriesRenderStyle(XYSeriesRenderStyle chartXYSeriesRenderStyle) {
        this.xyChartSeriesRenderStyle = chartXYSeriesRenderStyle;
        return this;
    }

    @Override
    public RenderableSeries.LegendRenderType getLegendRenderType() {
        return this.xyChartSeriesRenderStyle.getLegendRenderType();
    }

    @Override
    public Axis.AxisDataType getAxesType(List<?> data) {
        Axis.AxisDataType axisType;
        Iterator<?> itr = data.iterator();
        Object dataPoint = itr.next();
        if (dataPoint instanceof Number) {
            axisType = Axis.AxisDataType.Number;
        } else if (dataPoint instanceof Date) {
            axisType = Axis.AxisDataType.Date;
        } else {
            throw new IllegalArgumentException("Series data must be either Number or Date type!!!");
        }
        return axisType;
    }

    public static enum XYSeriesRenderStyle implements RenderableSeries
    {
        Line(RenderableSeries.LegendRenderType.Line),
        Area(RenderableSeries.LegendRenderType.Line),
        Scatter(RenderableSeries.LegendRenderType.Scatter);

        private final RenderableSeries.LegendRenderType legendRenderType;

        private XYSeriesRenderStyle(RenderableSeries.LegendRenderType legendRenderType) {
            this.legendRenderType = legendRenderType;
        }

        @Override
        public RenderableSeries.LegendRenderType getLegendRenderType() {
            return this.legendRenderType;
        }
    }
}

