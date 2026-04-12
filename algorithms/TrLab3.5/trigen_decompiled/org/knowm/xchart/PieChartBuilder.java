/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.internal.ChartBuilder;

public class PieChartBuilder
extends ChartBuilder<PieChartBuilder, PieChart> {
    @Override
    public PieChart build() {
        return new PieChart(this);
    }
}

