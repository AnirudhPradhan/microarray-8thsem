/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.RenderableSeries;

public class PieSeries
extends Series {
    private PieSeriesRenderStyle chartPieSeriesRenderStyle = null;
    private Number value;

    public PieSeries(String name, Number value) {
        super(name);
        this.value = value;
    }

    public PieSeriesRenderStyle getChartPieSeriesRenderStyle() {
        return this.chartPieSeriesRenderStyle;
    }

    public PieSeries setChartPieSeriesRenderStyle(PieSeriesRenderStyle chartPieSeriesRenderStyle) {
        this.chartPieSeriesRenderStyle = chartPieSeriesRenderStyle;
        return this;
    }

    @Override
    public RenderableSeries.LegendRenderType getLegendRenderType() {
        return this.chartPieSeriesRenderStyle.getLegendRenderType();
    }

    public Number getValue() {
        return this.value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public static enum PieSeriesRenderStyle implements RenderableSeries
    {
        Pie(RenderableSeries.LegendRenderType.Box),
        Donut(RenderableSeries.LegendRenderType.Box);

        private final RenderableSeries.LegendRenderType legendRenderType;

        private PieSeriesRenderStyle(RenderableSeries.LegendRenderType legendRenderType) {
            this.legendRenderType = legendRenderType;
        }

        @Override
        public RenderableSeries.LegendRenderType getLegendRenderType() {
            return this.legendRenderType;
        }
    }
}

