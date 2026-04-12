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

public class CategorySeries
extends Series_Markers {
    private CategorySeriesRenderStyle chartCategorySeriesRenderStyle = null;

    public CategorySeries(String name, List<?> xData, List<? extends Number> yData, List<? extends Number> errorBars) {
        super(name, xData, yData, errorBars);
    }

    public CategorySeriesRenderStyle getChartCategorySeriesRenderStyle() {
        return this.chartCategorySeriesRenderStyle;
    }

    public CategorySeries setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle chartXYSeriesRenderStyle) {
        this.chartCategorySeriesRenderStyle = chartXYSeriesRenderStyle;
        return this;
    }

    @Override
    public RenderableSeries.LegendRenderType getLegendRenderType() {
        return this.chartCategorySeriesRenderStyle.getLegendRenderType();
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
        } else if (dataPoint instanceof String) {
            axisType = Axis.AxisDataType.String;
        } else {
            throw new IllegalArgumentException("Series data must be either Number, Date or String type!!!");
        }
        return axisType;
    }

    public static enum CategorySeriesRenderStyle implements RenderableSeries
    {
        Line(RenderableSeries.LegendRenderType.Line),
        Area(RenderableSeries.LegendRenderType.Line),
        Scatter(RenderableSeries.LegendRenderType.Scatter),
        Bar(RenderableSeries.LegendRenderType.Box),
        Stick(RenderableSeries.LegendRenderType.Line);

        private final RenderableSeries.LegendRenderType legendRenderType;

        private CategorySeriesRenderStyle(RenderableSeries.LegendRenderType legendRenderType) {
            this.legendRenderType = legendRenderType;
        }

        @Override
        public RenderableSeries.LegendRenderType getLegendRenderType() {
            return this.legendRenderType;
        }
    }
}

