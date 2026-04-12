/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal;

import java.awt.Color;
import org.knowm.xchart.internal.chartpart.RenderableSeries;

public abstract class Series {
    private final String name;
    private Color fillColor;
    private boolean showInLegend = true;

    public abstract RenderableSeries.LegendRenderType getLegendRenderType();

    public Series(String name) {
        if (name == null || name.length() < 1) {
            throw new IllegalArgumentException("Series name cannot be null or zero-length!!!");
        }
        this.name = name;
    }

    public Color getFillColor() {
        return this.fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public String getName() {
        return this.name;
    }

    public boolean isShowInLegend() {
        return this.showInLegend;
    }

    public void setShowInLegend(boolean showInLegend) {
        this.showInLegend = showInLegend;
    }
}

