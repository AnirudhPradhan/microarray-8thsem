/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style;

import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Theme;

public class XYStyler
extends AxesChartStyler {
    private XYSeries.XYSeriesRenderStyle xySeriesRenderStyle;

    public XYStyler() {
        this.setAllStyles();
    }

    @Override
    protected void setAllStyles() {
        this.xySeriesRenderStyle = XYSeries.XYSeriesRenderStyle.Line;
    }

    public XYSeries.XYSeriesRenderStyle getDefaultSeriesRenderStyle() {
        return this.xySeriesRenderStyle;
    }

    public XYStyler setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle xySeriesRenderStyle) {
        this.xySeriesRenderStyle = xySeriesRenderStyle;
        return this;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
        super.setAllStyles();
    }

    public Theme getTheme() {
        return this.theme;
    }
}

