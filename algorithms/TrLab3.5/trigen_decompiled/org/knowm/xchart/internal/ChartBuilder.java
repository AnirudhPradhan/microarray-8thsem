/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal;

import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler;

public abstract class ChartBuilder<T extends ChartBuilder<?, ?>, C extends Chart> {
    public int width = 800;
    public int height = 600;
    public String title = "";
    public Styler.ChartTheme chartTheme = Styler.ChartTheme.XChart;

    public T width(int width) {
        this.width = width;
        return (T)this;
    }

    public T height(int height) {
        this.height = height;
        return (T)this;
    }

    public T title(String title) {
        this.title = title;
        return (T)this;
    }

    public T theme(Styler.ChartTheme chartTheme) {
        this.chartTheme = chartTheme;
        return (T)this;
    }

    public abstract C build();
}

