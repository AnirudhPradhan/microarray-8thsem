/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style;

import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.util.Locale;
import java.util.TimeZone;
import org.knowm.xchart.style.Styler;

public abstract class AxesChartStyler
extends Styler {
    private boolean xAxisTitleVisible;
    private boolean yAxisTitleVisible;
    private Font axisTitleFont;
    private boolean xAxisTicksVisible;
    private boolean yAxisTicksVisible;
    private Font axisTickLabelsFont;
    private int axisTickMarkLength;
    private int axisTickPadding;
    private Color axisTickMarksColor;
    private Stroke axisTickMarksStroke;
    private Color axisTickLabelsColor;
    private boolean isAxisTicksLineVisible;
    private boolean isAxisTicksMarksVisible;
    private int plotMargin;
    private int axisTitlePadding;
    private int xAxisTickMarkSpacingHint;
    private int yAxisTickMarkSpacingHint;
    private boolean isXAxisLogarithmic;
    private boolean isYAxisLogarithmic;
    private Double xAxisMin;
    private Double xAxisMax;
    private Double yAxisMin;
    private Double yAxisMax;
    private Styler.TextAlignment xAxisLabelAlignment = Styler.TextAlignment.Centre;
    private Styler.TextAlignment yAxisLabelAlignment = Styler.TextAlignment.Left;
    private int xAxisLabelRotation = 0;
    private boolean isPlotGridHorizontalLinesVisible;
    private boolean isPlotGridVerticalLinesVisible;
    private boolean isPlotTicksMarksVisible;
    private Color plotGridLinesColor;
    private Stroke plotGridLinesStroke;
    private int markerSize;
    private Color errorBarsColor;
    private boolean isErrorBarsColorSeriesColor;
    private Locale locale;
    private TimeZone timezone;
    private String datePattern;
    private String decimalPattern;
    private String xAxisDecimalPattern;
    private String yAxisDecimalPattern;

    @Override
    protected void setAllStyles() {
        super.setAllStyles();
        this.xAxisTitleVisible = this.theme.isXAxisTitleVisible();
        this.yAxisTitleVisible = this.theme.isYAxisTitleVisible();
        this.axisTitleFont = this.theme.getAxisTitleFont();
        this.xAxisTicksVisible = this.theme.isXAxisTicksVisible();
        this.yAxisTicksVisible = this.theme.isYAxisTicksVisible();
        this.axisTickLabelsFont = this.theme.getAxisTickLabelsFont();
        this.axisTickMarkLength = this.theme.getAxisTickMarkLength();
        this.axisTickPadding = this.theme.getAxisTickPadding();
        this.axisTickMarksColor = this.theme.getAxisTickMarksColor();
        this.axisTickMarksStroke = this.theme.getAxisTickMarksStroke();
        this.axisTickLabelsColor = this.theme.getAxisTickLabelsColor();
        this.isAxisTicksLineVisible = this.theme.isAxisTicksLineVisible();
        this.isAxisTicksMarksVisible = this.theme.isAxisTicksMarksVisible();
        this.plotMargin = this.theme.getPlotMargin();
        this.axisTitlePadding = this.theme.getAxisTitlePadding();
        this.xAxisTickMarkSpacingHint = this.theme.getXAxisTickMarkSpacingHint();
        this.yAxisTickMarkSpacingHint = this.theme.getYAxisTickMarkSpacingHint();
        this.isXAxisLogarithmic = false;
        this.isYAxisLogarithmic = false;
        this.xAxisMin = null;
        this.xAxisMax = null;
        this.yAxisMin = null;
        this.yAxisMax = null;
        this.isPlotGridVerticalLinesVisible = this.theme.isPlotGridVerticalLinesVisible();
        this.isPlotGridHorizontalLinesVisible = this.theme.isPlotGridHorizontalLinesVisible();
        this.isPlotTicksMarksVisible = this.theme.isPlotTicksMarksVisible();
        this.plotGridLinesColor = this.theme.getPlotGridLinesColor();
        this.plotGridLinesStroke = this.theme.getPlotGridLinesStroke();
        this.markerSize = this.theme.getMarkerSize();
        this.errorBarsColor = this.theme.getErrorBarsColor();
        this.isErrorBarsColorSeriesColor = this.theme.isErrorBarsColorSeriesColor();
        this.locale = Locale.getDefault();
        this.timezone = TimeZone.getDefault();
        this.datePattern = null;
        this.decimalPattern = null;
        this.xAxisDecimalPattern = null;
        this.yAxisDecimalPattern = null;
        this.hasAnnotations = false;
    }

    public AxesChartStyler setXAxisTitleVisible(boolean xAxisTitleVisible) {
        this.xAxisTitleVisible = xAxisTitleVisible;
        return this;
    }

    public boolean isXAxisTitleVisible() {
        return this.xAxisTitleVisible;
    }

    public AxesChartStyler setYAxisTitleVisible(boolean yAxisTitleVisible) {
        this.yAxisTitleVisible = yAxisTitleVisible;
        return this;
    }

    public boolean isYAxisTitleVisible() {
        return this.yAxisTitleVisible;
    }

    public AxesChartStyler setAxisTitlesVisible(boolean isVisible) {
        this.xAxisTitleVisible = isVisible;
        this.yAxisTitleVisible = isVisible;
        return this;
    }

    public AxesChartStyler setAxisTitleFont(Font axisTitleFont) {
        this.axisTitleFont = axisTitleFont;
        return this;
    }

    public Font getAxisTitleFont() {
        return this.axisTitleFont;
    }

    public AxesChartStyler setXAxisTicksVisible(boolean xAxisTicksVisible) {
        this.xAxisTicksVisible = xAxisTicksVisible;
        return this;
    }

    public boolean isXAxisTicksVisible() {
        return this.xAxisTicksVisible;
    }

    public AxesChartStyler setYAxisTicksVisible(boolean yAxisTicksVisible) {
        this.yAxisTicksVisible = yAxisTicksVisible;
        return this;
    }

    public boolean isYAxisTicksVisible() {
        return this.yAxisTicksVisible;
    }

    public AxesChartStyler setAxisTicksVisible(boolean isVisible) {
        this.xAxisTicksVisible = isVisible;
        this.yAxisTicksVisible = isVisible;
        return this;
    }

    public AxesChartStyler setAxisTickLabelsFont(Font axisTicksFont) {
        this.axisTickLabelsFont = axisTicksFont;
        return this;
    }

    public Font getAxisTickLabelsFont() {
        return this.axisTickLabelsFont;
    }

    public AxesChartStyler setAxisTickMarkLength(int axisTickMarkLength) {
        this.axisTickMarkLength = axisTickMarkLength;
        return this;
    }

    public int getAxisTickMarkLength() {
        return this.axisTickMarkLength;
    }

    public AxesChartStyler setAxisTickPadding(int axisTickPadding) {
        this.axisTickPadding = axisTickPadding;
        return this;
    }

    public int getAxisTickPadding() {
        return this.axisTickPadding;
    }

    public AxesChartStyler setAxisTickMarksColor(Color axisTickColor) {
        this.axisTickMarksColor = axisTickColor;
        return this;
    }

    public Color getAxisTickMarksColor() {
        return this.axisTickMarksColor;
    }

    public AxesChartStyler setAxisTickMarksStroke(Stroke axisTickMarksStroke) {
        this.axisTickMarksStroke = axisTickMarksStroke;
        return this;
    }

    public Stroke getAxisTickMarksStroke() {
        return this.axisTickMarksStroke;
    }

    public AxesChartStyler setAxisTickLabelsColor(Color axisTickLabelsColor) {
        this.axisTickLabelsColor = axisTickLabelsColor;
        return this;
    }

    public Color getAxisTickLabelsColor() {
        return this.axisTickLabelsColor;
    }

    public AxesChartStyler setAxisTicksLineVisible(boolean isAxisTicksLineVisible) {
        this.isAxisTicksLineVisible = isAxisTicksLineVisible;
        return this;
    }

    public boolean isAxisTicksLineVisible() {
        return this.isAxisTicksLineVisible;
    }

    public AxesChartStyler setAxisTicksMarksVisible(boolean isAxisTicksMarksVisible) {
        this.isAxisTicksMarksVisible = isAxisTicksMarksVisible;
        return this;
    }

    public boolean isAxisTicksMarksVisible() {
        return this.isAxisTicksMarksVisible;
    }

    public AxesChartStyler setPlotMargin(int plotMargin) {
        this.plotMargin = plotMargin;
        return this;
    }

    public int getPlotMargin() {
        return this.plotMargin;
    }

    public AxesChartStyler setAxisTitlePadding(int axisTitlePadding) {
        this.axisTitlePadding = axisTitlePadding;
        return this;
    }

    public int getAxisTitlePadding() {
        return this.axisTitlePadding;
    }

    public AxesChartStyler setXAxisTickMarkSpacingHint(int xAxisTickMarkSpacingHint) {
        this.xAxisTickMarkSpacingHint = xAxisTickMarkSpacingHint;
        return this;
    }

    public int getXAxisTickMarkSpacingHint() {
        return this.xAxisTickMarkSpacingHint;
    }

    public AxesChartStyler setYAxisTickMarkSpacingHint(int yAxisTickMarkSpacingHint) {
        this.yAxisTickMarkSpacingHint = yAxisTickMarkSpacingHint;
        return this;
    }

    public int getYAxisTickMarkSpacingHint() {
        return this.yAxisTickMarkSpacingHint;
    }

    public AxesChartStyler setXAxisLogarithmic(boolean isXAxisLogarithmic) {
        this.isXAxisLogarithmic = isXAxisLogarithmic;
        return this;
    }

    public boolean isXAxisLogarithmic() {
        return this.isXAxisLogarithmic;
    }

    public AxesChartStyler setYAxisLogarithmic(boolean isYAxisLogarithmic) {
        this.isYAxisLogarithmic = isYAxisLogarithmic;
        return this;
    }

    public boolean isYAxisLogarithmic() {
        return this.isYAxisLogarithmic;
    }

    public AxesChartStyler setXAxisMin(double xAxisMin) {
        this.xAxisMin = xAxisMin;
        return this;
    }

    public Double getXAxisMin() {
        return this.xAxisMin;
    }

    public AxesChartStyler setXAxisMax(double xAxisMax) {
        this.xAxisMax = xAxisMax;
        return this;
    }

    public Double getXAxisMax() {
        return this.xAxisMax;
    }

    public AxesChartStyler setYAxisMin(double yAxisMin) {
        this.yAxisMin = yAxisMin;
        return this;
    }

    public Double getYAxisMin() {
        return this.yAxisMin;
    }

    public AxesChartStyler setYAxisMax(double yAxisMax) {
        this.yAxisMax = yAxisMax;
        return this;
    }

    public Double getYAxisMax() {
        return this.yAxisMax;
    }

    public Styler.TextAlignment getXAxisLabelAlignment() {
        return this.xAxisLabelAlignment;
    }

    public void setXAxisLabelAlignment(Styler.TextAlignment xAxisLabelAlignment) {
        this.xAxisLabelAlignment = xAxisLabelAlignment;
    }

    public Styler.TextAlignment getYAxisLabelAlignment() {
        return this.yAxisLabelAlignment;
    }

    public AxesChartStyler setYAxisLabelAlignment(Styler.TextAlignment yAxisLabelAlignment) {
        this.yAxisLabelAlignment = yAxisLabelAlignment;
        return this;
    }

    public int getXAxisLabelRotation() {
        return this.xAxisLabelRotation;
    }

    public AxesChartStyler setXAxisLabelRotation(int xAxisLabelRotation) {
        this.xAxisLabelRotation = xAxisLabelRotation;
        return this;
    }

    public AxesChartStyler setPlotGridLinesVisible(boolean isPlotGridLinesVisible) {
        this.isPlotGridHorizontalLinesVisible = isPlotGridLinesVisible;
        this.isPlotGridVerticalLinesVisible = isPlotGridLinesVisible;
        return this;
    }

    public boolean isPlotGridLinesVisible() {
        return this.isPlotGridHorizontalLinesVisible && this.isPlotGridVerticalLinesVisible;
    }

    public AxesChartStyler setPlotGridHorizontalLinesVisible(boolean isPlotGridHorizontalLinesVisible) {
        this.isPlotGridHorizontalLinesVisible = isPlotGridHorizontalLinesVisible;
        return this;
    }

    public boolean isPlotGridHorizontalLinesVisible() {
        return this.isPlotGridHorizontalLinesVisible;
    }

    public AxesChartStyler setPlotGridVerticalLinesVisible(boolean isPlotGridVerticalLinesVisible) {
        this.isPlotGridVerticalLinesVisible = isPlotGridVerticalLinesVisible;
        return this;
    }

    public boolean isPlotGridVerticalLinesVisible() {
        return this.isPlotGridVerticalLinesVisible;
    }

    public AxesChartStyler setPlotTicksMarksVisible(boolean isPlotTicksMarksVisible) {
        this.isPlotTicksMarksVisible = isPlotTicksMarksVisible;
        return this;
    }

    public boolean isPlotTicksMarksVisible() {
        return this.isPlotTicksMarksVisible;
    }

    public AxesChartStyler setPlotGridLinesColor(Color plotGridLinesColor) {
        this.plotGridLinesColor = plotGridLinesColor;
        return this;
    }

    public Color getPlotGridLinesColor() {
        return this.plotGridLinesColor;
    }

    public AxesChartStyler setPlotGridLinesStroke(Stroke plotGridLinesStroke) {
        this.plotGridLinesStroke = plotGridLinesStroke;
        return this;
    }

    public Stroke getPlotGridLinesStroke() {
        return this.plotGridLinesStroke;
    }

    public AxesChartStyler setMarkerSize(int markerSize) {
        this.markerSize = markerSize;
        return this;
    }

    public int getMarkerSize() {
        return this.markerSize;
    }

    public AxesChartStyler setErrorBarsColor(Color errorBarsColor) {
        this.errorBarsColor = errorBarsColor;
        return this;
    }

    public Color getErrorBarsColor() {
        return this.errorBarsColor;
    }

    public AxesChartStyler setErrorBarsColorSeriesColor(boolean isErrorBarsColorSeriesColor) {
        this.isErrorBarsColorSeriesColor = isErrorBarsColorSeriesColor;
        return this;
    }

    public boolean isErrorBarsColorSeriesColor() {
        return this.isErrorBarsColorSeriesColor;
    }

    public AxesChartStyler setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public AxesChartStyler setTimezone(TimeZone timezone) {
        this.timezone = timezone;
        return this;
    }

    public TimeZone getTimezone() {
        return this.timezone;
    }

    public AxesChartStyler setDatePattern(String datePattern) {
        this.datePattern = datePattern;
        return this;
    }

    public String getDatePattern() {
        return this.datePattern;
    }

    public AxesChartStyler setDecimalPattern(String decimalPattern) {
        this.decimalPattern = decimalPattern;
        return this;
    }

    public String getDecimalPattern() {
        return this.decimalPattern;
    }

    public String getXAxisDecimalPattern() {
        return this.xAxisDecimalPattern;
    }

    public AxesChartStyler setXAxisDecimalPattern(String xAxisDecimalPattern) {
        this.xAxisDecimalPattern = xAxisDecimalPattern;
        return this;
    }

    public String getYAxisDecimalPattern() {
        return this.yAxisDecimalPattern;
    }

    public AxesChartStyler setYAxisDecimalPattern(String yAxisDecimalPattern) {
        this.yAxisDecimalPattern = yAxisDecimalPattern;
        return this;
    }
}

