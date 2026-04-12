/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style;

import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Theme;

public class CategoryStyler
extends AxesChartStyler {
    private CategorySeries.CategorySeriesRenderStyle chartCategorySeriesRenderStyle;
    private double availableSpaceFill;
    private boolean isOverlapped;

    public CategoryStyler() {
        this.setAllStyles();
    }

    @Override
    protected void setAllStyles() {
        this.chartCategorySeriesRenderStyle = CategorySeries.CategorySeriesRenderStyle.Bar;
        this.availableSpaceFill = this.theme.getAvailableSpaceFill();
        this.isOverlapped = this.theme.isOverlapped();
    }

    public CategorySeries.CategorySeriesRenderStyle getDefaultSeriesRenderStyle() {
        return this.chartCategorySeriesRenderStyle;
    }

    public CategoryStyler setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle chartCategorySeriesRenderStyle) {
        this.chartCategorySeriesRenderStyle = chartCategorySeriesRenderStyle;
        return this;
    }

    public double getAvailableSpaceFill() {
        return this.availableSpaceFill;
    }

    public CategoryStyler setAvailableSpaceFill(double availableSpaceFill) {
        this.availableSpaceFill = availableSpaceFill;
        return this;
    }

    public boolean isOverlapped() {
        return this.isOverlapped;
    }

    public CategoryStyler setOverlapped(boolean isOverlapped) {
        this.isOverlapped = isOverlapped;
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

