/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import org.knowm.xchart.BubbleSeries;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.PlotContent_Bubble;
import org.knowm.xchart.internal.chartpart.Plot_AxesChart;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.BubbleStyler;

public class Plot_Bubble<ST extends AxesChartStyler, S extends Series>
extends Plot_AxesChart {
    public Plot_Bubble(Chart<BubbleStyler, BubbleSeries> chart) {
        super((Chart<AxesChartStyler, XYSeries>)chart);
        this.plotContent = new PlotContent_Bubble(chart);
    }
}

