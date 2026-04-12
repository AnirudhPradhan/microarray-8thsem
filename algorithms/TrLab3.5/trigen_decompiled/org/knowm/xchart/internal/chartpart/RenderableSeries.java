/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

public interface RenderableSeries {
    public LegendRenderType getLegendRenderType();

    public static enum LegendRenderType {
        Line,
        Scatter,
        Box;

    }
}

