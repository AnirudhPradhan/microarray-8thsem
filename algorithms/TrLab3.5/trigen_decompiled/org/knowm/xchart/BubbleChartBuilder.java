/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import org.knowm.xchart.BubbleChart;
import org.knowm.xchart.internal.ChartBuilder;

public class BubbleChartBuilder
extends ChartBuilder<BubbleChartBuilder, BubbleChart> {
    String xAxisTitle = "";
    String yAxisTitle = "";

    public BubbleChartBuilder xAxisTitle(String xAxisTitle) {
        this.xAxisTitle = xAxisTitle;
        return this;
    }

    public BubbleChartBuilder yAxisTitle(String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
        return this;
    }

    @Override
    public BubbleChart build() {
        return new BubbleChart(this);
    }
}

