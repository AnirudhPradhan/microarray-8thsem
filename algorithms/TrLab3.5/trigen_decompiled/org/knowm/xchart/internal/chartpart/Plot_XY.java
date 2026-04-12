/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.PlotContent_XY;
import org.knowm.xchart.internal.chartpart.Plot_AxesChart;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.XYStyler;

public class Plot_XY<ST extends AxesChartStyler, S extends Series>
extends Plot_AxesChart {
    public Plot_XY(Chart<XYStyler, XYSeries> chart) {
        super(chart);
        this.plotContent = new PlotContent_XY((Chart<XYStyler, XYSeries>)chart);
    }
}

