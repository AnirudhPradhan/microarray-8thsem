/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.internal.ChartBuilder;

public class XYChartBuilder
extends ChartBuilder<XYChartBuilder, XYChart> {
    String xAxisTitle = "";
    String yAxisTitle = "";

    public XYChartBuilder xAxisTitle(String xAxisTitle) {
        this.xAxisTitle = xAxisTitle;
        return this;
    }

    public XYChartBuilder yAxisTitle(String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
        return this;
    }

    @Override
    public XYChart build() {
        return new XYChart(this);
    }
}

