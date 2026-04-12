/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.internal.ChartBuilder;

public class CategoryChartBuilder
extends ChartBuilder<CategoryChartBuilder, CategoryChart> {
    String xAxisTitle = "";
    String yAxisTitle = "";

    public CategoryChartBuilder xAxisTitle(String xAxisTitle) {
        this.xAxisTitle = xAxisTitle;
        return this;
    }

    public CategoryChartBuilder yAxisTitle(String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
        return this;
    }

    @Override
    public CategoryChart build() {
        return new CategoryChart(this);
    }
}

