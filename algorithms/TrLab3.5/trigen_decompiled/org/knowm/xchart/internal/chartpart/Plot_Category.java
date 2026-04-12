/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.PlotContent_Category_Bar;
import org.knowm.xchart.internal.chartpart.PlotContent_Category_Line_Area_Scatter;
import org.knowm.xchart.internal.chartpart.Plot_AxesChart;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.CategoryStyler;

public class Plot_Category<ST extends AxesChartStyler, S extends Series>
extends Plot_AxesChart {
    CategoryStyler stylerCategory;

    public Plot_Category(Chart<CategoryStyler, CategorySeries> chart) {
        super((Chart<AxesChartStyler, XYSeries>)chart);
        this.stylerCategory = chart.getStyler();
    }

    @Override
    public void paint(Graphics2D g) {
        this.plotContent = CategorySeries.CategorySeriesRenderStyle.Bar.equals(this.stylerCategory.getDefaultSeriesRenderStyle()) || CategorySeries.CategorySeriesRenderStyle.Stick.equals(this.stylerCategory.getDefaultSeriesRenderStyle()) ? new PlotContent_Category_Bar(this.chart) : new PlotContent_Category_Line_Area_Scatter(this.chart);
        super.paint(g);
    }
}

