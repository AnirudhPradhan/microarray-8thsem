/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.knowm.xchart.internal.Series_Bubble;
import org.knowm.xchart.internal.chartpart.Axis;
import org.knowm.xchart.internal.chartpart.RenderableSeries;

public class BubbleSeries
extends Series_Bubble {
    private BubbleSeriesRenderStyle bubbleSeriesRenderStyle = null;
    private Number value;

    public BubbleSeries(String name, List<?> xData, List<? extends Number> yData, List<? extends Number> bubbleSizes) {
        super(name, xData, yData, bubbleSizes);
    }

    public BubbleSeriesRenderStyle getBubbleSeriesRenderStyle() {
        return this.bubbleSeriesRenderStyle;
    }

    public void setBubbleSeriesRenderStyle(BubbleSeriesRenderStyle bubbleSeriesRenderStyle) {
        this.bubbleSeriesRenderStyle = bubbleSeriesRenderStyle;
    }

    @Override
    public RenderableSeries.LegendRenderType getLegendRenderType() {
        return this.bubbleSeriesRenderStyle.getLegendRenderType();
    }

    public Number getValue() {
        return this.value;
    }

    public void setValue(Number value) {
        this.value = value;
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

    public static enum BubbleSeriesRenderStyle implements RenderableSeries
    {
        Round(RenderableSeries.LegendRenderType.Box);

        private final RenderableSeries.LegendRenderType legendRenderType;

        private BubbleSeriesRenderStyle(RenderableSeries.LegendRenderType legendRenderType) {
            this.legendRenderType = legendRenderType;
        }

        @Override
        public RenderableSeries.LegendRenderType getLegendRenderType() {
            return this.legendRenderType;
        }
    }
}

