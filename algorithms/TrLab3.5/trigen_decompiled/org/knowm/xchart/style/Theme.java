/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style;

import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.colors.SeriesColors;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

public interface Theme
extends SeriesMarkers,
SeriesLines,
SeriesColors {
    public Color getChartBackgroundColor();

    public Color getChartFontColor();

    public int getChartPadding();

    public Font getChartTitleFont();

    public boolean isChartTitleVisible();

    public boolean isChartTitleBoxVisible();

    public Color getChartTitleBoxBackgroundColor();

    public Color getChartTitleBoxBorderColor();

    public int getChartTitlePadding();

    public Font getLegendFont();

    public boolean isLegendVisible();

    public Color getLegendBackgroundColor();

    public Color getLegendBorderColor();

    public int getLegendPadding();

    public int getLegendSeriesLineLength();

    public Styler.LegendPosition getLegendPosition();

    public boolean isXAxisTitleVisible();

    public boolean isYAxisTitleVisible();

    public Font getAxisTitleFont();

    public boolean isXAxisTicksVisible();

    public boolean isYAxisTicksVisible();

    public Font getAxisTickLabelsFont();

    public int getAxisTickMarkLength();

    public int getAxisTickPadding();

    public Color getAxisTickMarksColor();

    public Stroke getAxisTickMarksStroke();

    public Color getAxisTickLabelsColor();

    public boolean isAxisTicksLineVisible();

    public boolean isAxisTicksMarksVisible();

    public int getAxisTitlePadding();

    public int getXAxisTickMarkSpacingHint();

    public int getYAxisTickMarkSpacingHint();

    public boolean isPlotGridLinesVisible();

    public boolean isPlotGridVerticalLinesVisible();

    public boolean isPlotGridHorizontalLinesVisible();

    public Color getPlotBackgroundColor();

    public Color getPlotBorderColor();

    public boolean isPlotBorderVisible();

    public Color getPlotGridLinesColor();

    public Stroke getPlotGridLinesStroke();

    public boolean isPlotTicksMarksVisible();

    public double getPlotContentSize();

    public int getPlotMargin();

    public double getAvailableSpaceFill();

    public boolean isOverlapped();

    public boolean isCircular();

    public double getStartAngleInDegrees();

    public Font getPieFont();

    public double getAnnotationDistance();

    public PieStyler.AnnotationType getAnnotationType();

    public boolean isDrawAllAnnotations();

    public double getDonutThickness();

    public int getMarkerSize();

    public boolean showMarkers();

    public Color getErrorBarsColor();

    public boolean isErrorBarsColorSeriesColor();

    public Font getAnnotationFont();
}

