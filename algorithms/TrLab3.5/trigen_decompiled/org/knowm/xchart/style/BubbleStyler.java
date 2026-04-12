/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style;

import org.knowm.xchart.BubbleSeries;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Theme;

public class BubbleStyler
extends AxesChartStyler {
    private BubbleSeries.BubbleSeriesRenderStyle bubbleChartSeriesRenderStyle;

    public BubbleStyler() {
        this.setAllStyles();
    }

    @Override
    protected void setAllStyles() {
        this.bubbleChartSeriesRenderStyle = BubbleSeries.BubbleSeriesRenderStyle.Round;
    }

    public BubbleSeries.BubbleSeriesRenderStyle getDefaultSeriesRenderStyle() {
        return this.bubbleChartSeriesRenderStyle;
    }

    public BubbleStyler setDefaultSeriesRenderStyle(BubbleSeries.BubbleSeriesRenderStyle bubbleChartSeriesRenderStyle) {
        this.bubbleChartSeriesRenderStyle = bubbleChartSeriesRenderStyle;
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

