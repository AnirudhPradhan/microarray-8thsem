/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import org.knowm.xchart.style.GGPlot2Theme;
import org.knowm.xchart.style.MatlabTheme;
import org.knowm.xchart.style.Theme;
import org.knowm.xchart.style.XChartTheme;
import org.knowm.xchart.style.markers.Marker;

public abstract class Styler {
    protected Theme theme = new XChartTheme();
    private Color chartBackgroundColor;
    private Color chartFontColor;
    private int chartPadding;
    private Color[] seriesColors;
    private BasicStroke[] seriesLines;
    private Marker[] seriesMarkers;
    private Font chartTitleFont;
    private boolean isChartTitleVisible;
    private boolean isChartTitleBoxVisible;
    private Color chartTitleBoxBackgroundColor;
    private Color chartTitleBoxBorderColor;
    private int chartTitlePadding;
    private boolean isLegendVisible;
    private Color legendBackgroundColor;
    private Color legendBorderColor;
    private Font legendFont;
    private int legendPadding;
    private int legendSeriesLineLength;
    private LegendPosition legendPosition;
    private Color plotBackgroundColor;
    private Color plotBorderColor;
    private boolean isPlotBorderVisible;
    private double plotContentSize = 0.92;
    private Font annotationsFont;
    public boolean hasAnnotations;

    protected void setAllStyles() {
        this.chartBackgroundColor = this.theme.getChartBackgroundColor();
        this.chartFontColor = this.theme.getChartFontColor();
        this.chartPadding = this.theme.getChartPadding();
        this.seriesColors = this.theme.getSeriesColors();
        this.seriesLines = this.theme.getSeriesLines();
        this.seriesMarkers = this.theme.getSeriesMarkers();
        this.chartTitleFont = this.theme.getChartTitleFont();
        this.isChartTitleVisible = this.theme.isChartTitleVisible();
        this.isChartTitleBoxVisible = this.theme.isChartTitleBoxVisible();
        this.chartTitleBoxBackgroundColor = this.theme.getChartTitleBoxBackgroundColor();
        this.chartTitleBoxBorderColor = this.theme.getChartTitleBoxBorderColor();
        this.chartTitlePadding = this.theme.getChartTitlePadding();
        this.isLegendVisible = this.theme.isLegendVisible();
        this.legendBackgroundColor = this.theme.getLegendBackgroundColor();
        this.legendBorderColor = this.theme.getLegendBorderColor();
        this.legendFont = this.theme.getLegendFont();
        this.legendPadding = this.theme.getLegendPadding();
        this.legendSeriesLineLength = this.theme.getLegendSeriesLineLength();
        this.legendPosition = this.theme.getLegendPosition();
        this.plotBackgroundColor = this.theme.getPlotBackgroundColor();
        this.plotBorderColor = this.theme.getPlotBorderColor();
        this.isPlotBorderVisible = this.theme.isPlotBorderVisible();
        this.plotContentSize = this.theme.getPlotContentSize();
        this.annotationsFont = this.theme.getAnnotationFont();
    }

    public Styler setChartBackgroundColor(Color color) {
        this.chartBackgroundColor = color;
        return this;
    }

    public Color getChartBackgroundColor() {
        return this.chartBackgroundColor;
    }

    public Styler setChartFontColor(Color color) {
        this.chartFontColor = color;
        return this;
    }

    public Color getChartFontColor() {
        return this.chartFontColor;
    }

    public Styler setChartPadding(int chartPadding) {
        this.chartPadding = chartPadding;
        return this;
    }

    public int getChartPadding() {
        return this.chartPadding;
    }

    public Color[] getSeriesColors() {
        return this.seriesColors;
    }

    public Styler setSeriesColors(Color[] seriesColors) {
        this.seriesColors = seriesColors;
        return this;
    }

    public BasicStroke[] getSeriesLines() {
        return this.seriesLines;
    }

    public Styler setSeriesLines(BasicStroke[] seriesLines) {
        this.seriesLines = seriesLines;
        return this;
    }

    public Marker[] getSeriesMarkers() {
        return this.seriesMarkers;
    }

    public Styler setSeriesMarkers(Marker[] seriesMarkers) {
        this.seriesMarkers = seriesMarkers;
        return this;
    }

    public Styler setChartTitleFont(Font chartTitleFont) {
        this.chartTitleFont = chartTitleFont;
        return this;
    }

    public Font getChartTitleFont() {
        return this.chartTitleFont;
    }

    public Styler setChartTitleVisible(boolean isChartTitleVisible) {
        this.isChartTitleVisible = isChartTitleVisible;
        return this;
    }

    public boolean isChartTitleVisible() {
        return this.isChartTitleVisible;
    }

    public Styler setChartTitleBoxVisible(boolean isChartTitleBoxVisible) {
        this.isChartTitleBoxVisible = isChartTitleBoxVisible;
        return this;
    }

    public boolean isChartTitleBoxVisible() {
        return this.isChartTitleBoxVisible;
    }

    public Styler setChartTitleBoxBackgroundColor(Color chartTitleBoxBackgroundColor) {
        this.chartTitleBoxBackgroundColor = chartTitleBoxBackgroundColor;
        return this;
    }

    public Color getChartTitleBoxBackgroundColor() {
        return this.chartTitleBoxBackgroundColor;
    }

    public Styler setChartTitleBoxBorderColor(Color chartTitleBoxBorderColor) {
        this.chartTitleBoxBorderColor = chartTitleBoxBorderColor;
        return this;
    }

    public Color getChartTitleBoxBorderColor() {
        return this.chartTitleBoxBorderColor;
    }

    public Styler setChartTitlePadding(int chartTitlePadding) {
        this.chartTitlePadding = chartTitlePadding;
        return this;
    }

    public int getChartTitlePadding() {
        return this.chartTitlePadding;
    }

    public Styler setLegendBackgroundColor(Color color) {
        this.legendBackgroundColor = color;
        return this;
    }

    public Color getLegendBackgroundColor() {
        return this.legendBackgroundColor;
    }

    public Color getLegendBorderColor() {
        return this.legendBorderColor;
    }

    public Styler setLegendBorderColor(Color legendBorderColor) {
        this.legendBorderColor = legendBorderColor;
        return this;
    }

    public Styler setLegendFont(Font font) {
        this.legendFont = font;
        return this;
    }

    public Font getLegendFont() {
        return this.legendFont;
    }

    public Styler setLegendVisible(boolean isLegendVisible) {
        this.isLegendVisible = isLegendVisible;
        return this;
    }

    public boolean isLegendVisible() {
        return this.isLegendVisible;
    }

    public Styler setLegendPadding(int legendPadding) {
        this.legendPadding = legendPadding;
        return this;
    }

    public int getLegendPadding() {
        return this.legendPadding;
    }

    public Styler setLegendSeriesLineLength(int legendSeriesLineLength) {
        this.legendSeriesLineLength = legendSeriesLineLength < 0 ? 0 : legendSeriesLineLength;
        return this;
    }

    public int getLegendSeriesLineLength() {
        return this.legendSeriesLineLength;
    }

    public Styler setLegendPosition(LegendPosition legendPosition) {
        this.legendPosition = legendPosition;
        return this;
    }

    public LegendPosition getLegendPosition() {
        return this.legendPosition;
    }

    public Styler setPlotBackgroundColor(Color plotBackgroundColor) {
        this.plotBackgroundColor = plotBackgroundColor;
        return this;
    }

    public Color getPlotBackgroundColor() {
        return this.plotBackgroundColor;
    }

    public Styler setPlotBorderColor(Color plotBorderColor) {
        this.plotBorderColor = plotBorderColor;
        return this;
    }

    public Color getPlotBorderColor() {
        return this.plotBorderColor;
    }

    public Styler setPlotBorderVisible(boolean isPlotBorderVisible) {
        this.isPlotBorderVisible = isPlotBorderVisible;
        return this;
    }

    public boolean isPlotBorderVisible() {
        return this.isPlotBorderVisible;
    }

    public double getPlotContentSize() {
        return this.plotContentSize;
    }

    public Styler setPlotContentSize(double plotContentSize) {
        if (plotContentSize < 0.0 || plotContentSize > 1.0) {
            throw new IllegalArgumentException("Plot content size must be tween 0 and 1!!!");
        }
        this.plotContentSize = plotContentSize;
        return this;
    }

    public Boolean hasAnnotations() {
        return this.hasAnnotations;
    }

    public void setHasAnnotations(boolean hasAnnotations) {
        this.hasAnnotations = hasAnnotations;
    }

    public Font getAnnotationsFont() {
        return this.annotationsFont;
    }

    public void setAnnotationsFont(Font annotationsFont) {
        this.annotationsFont = annotationsFont;
    }

    public static enum TextAlignment {
        Left,
        Centre,
        Right;

    }

    public static enum ChartTheme {
        XChart,
        GGPlot2,
        Matlab;


        public Theme newInstance(ChartTheme chartTheme) {
            switch (chartTheme) {
                case GGPlot2: {
                    return new GGPlot2Theme();
                }
                case Matlab: {
                    return new MatlabTheme();
                }
            }
            return new XChartTheme();
        }
    }

    public static enum LegendPosition {
        OutsideE,
        InsideNW,
        InsideNE,
        InsideSE,
        InsideSW,
        InsideN;

    }
}

