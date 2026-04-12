/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style;

import org.knowm.xchart.PieSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Theme;

public class PieStyler
extends Styler {
    private PieSeries.PieSeriesRenderStyle chartPieSeriesRenderStyle;
    private boolean isCircular;
    private double startAngleInDegrees;
    private double annotationDistance;
    private AnnotationType annotationType;
    private boolean drawAllAnnotations;
    private double donutThickness;

    public PieStyler() {
        this.setAllStyles();
    }

    @Override
    protected void setAllStyles() {
        this.chartPieSeriesRenderStyle = PieSeries.PieSeriesRenderStyle.Pie;
        this.isCircular = this.theme.isCircular();
        this.annotationDistance = this.theme.getAnnotationDistance();
        this.annotationType = this.theme.getAnnotationType();
        this.drawAllAnnotations = this.theme.isDrawAllAnnotations();
        this.donutThickness = this.theme.getDonutThickness();
        this.hasAnnotations = true;
    }

    public PieSeries.PieSeriesRenderStyle getDefaultSeriesRenderStyle() {
        return this.chartPieSeriesRenderStyle;
    }

    public PieStyler setDefaultSeriesRenderStyle(PieSeries.PieSeriesRenderStyle chartPieSeriesRenderStyle) {
        this.chartPieSeriesRenderStyle = chartPieSeriesRenderStyle;
        return this;
    }

    public boolean isCircular() {
        return this.isCircular;
    }

    public PieStyler setCircular(boolean isCircular) {
        this.isCircular = isCircular;
        return this;
    }

    public double getStartAngleInDegrees() {
        return this.startAngleInDegrees;
    }

    public PieStyler setStartAngleInDegrees(double startAngleInDegrees) {
        this.startAngleInDegrees = startAngleInDegrees;
        return this;
    }

    public double getAnnotationDistance() {
        return this.annotationDistance;
    }

    public void setAnnotationDistance(double annotationDistance) {
        this.annotationDistance = annotationDistance;
    }

    public AnnotationType getAnnotationType() {
        return this.annotationType;
    }

    public PieStyler setAnnotationType(AnnotationType annotationType) {
        this.annotationType = annotationType;
        return this;
    }

    public boolean isDrawAllAnnotations() {
        return this.drawAllAnnotations;
    }

    public void setDrawAllAnnotations(boolean drawAllAnnotations) {
        this.drawAllAnnotations = drawAllAnnotations;
    }

    public double getDonutThickness() {
        return this.donutThickness;
    }

    public void setDonutThickness(double donutThickness) {
        this.donutThickness = donutThickness;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
        super.setAllStyles();
    }

    public Theme getTheme() {
        return this.theme;
    }

    public static enum AnnotationType {
        Percentage,
        Label,
        LabelAndPercentage;

    }
}

