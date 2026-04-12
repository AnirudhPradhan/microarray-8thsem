/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.PlotContent_Pie;
import org.knowm.xchart.internal.chartpart.PlotSurface_Pie;
import org.knowm.xchart.internal.chartpart.Plot_;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.Styler;

public class Plot_Pie<ST extends Styler, S extends Series>
extends Plot_ {
    public Plot_Pie(Chart<PieStyler, PieSeries> chart) {
        super(chart);
        this.plotContent = new PlotContent_Pie(chart);
        this.plotSurface = new PlotSurface_Pie(chart);
    }

    @Override
    public void paint(Graphics2D g) {
        double xOffset = ((Styler)this.chart.getStyler()).getChartPadding();
        double yOffset = this.chart.getChartTitle().getBounds().getHeight() + (double)((Styler)this.chart.getStyler()).getChartPadding();
        double width = (double)this.chart.getWidth() - (((Styler)this.chart.getStyler()).getLegendPosition() == Styler.LegendPosition.OutsideE ? this.chart.getLegend().getBounds().getWidth() : 0.0) - (double)(2 * ((Styler)this.chart.getStyler()).getChartPadding()) - (double)(((Styler)this.chart.getStyler()).getLegendPosition() == Styler.LegendPosition.OutsideE && ((Styler)this.chart.getStyler()).isLegendVisible() ? ((Styler)this.chart.getStyler()).getChartPadding() : 0);
        double height = (double)this.chart.getHeight() - this.chart.getChartTitle().getBounds().getHeight() - (double)(2 * ((Styler)this.chart.getStyler()).getChartPadding());
        this.bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);
        super.paint(g);
    }
}

