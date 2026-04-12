/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Theme;
import org.knowm.xchart.style.colors.ChartColor;
import org.knowm.xchart.style.colors.GGPlot2SeriesColors;
import org.knowm.xchart.style.lines.GGPlot2SeriesLines;
import org.knowm.xchart.style.markers.GGPlot2SeriesMarkers;
import org.knowm.xchart.style.markers.Marker;

public class GGPlot2Theme
implements Theme {
    @Override
    public Color getChartBackgroundColor() {
        return ChartColor.getAWTColor(ChartColor.WHITE);
    }

    @Override
    public Color getChartFontColor() {
        return ChartColor.getAWTColor(ChartColor.BLACK);
    }

    @Override
    public int getChartPadding() {
        return 10;
    }

    @Override
    public Marker[] getSeriesMarkers() {
        return new GGPlot2SeriesMarkers().getSeriesMarkers();
    }

    @Override
    public BasicStroke[] getSeriesLines() {
        return new GGPlot2SeriesLines().getSeriesLines();
    }

    @Override
    public Color[] getSeriesColors() {
        return new GGPlot2SeriesColors().getSeriesColors();
    }

    @Override
    public Font getChartTitleFont() {
        return new Font("SansSerif", 0, 14);
    }

    @Override
    public boolean isChartTitleVisible() {
        return true;
    }

    @Override
    public boolean isChartTitleBoxVisible() {
        return true;
    }

    @Override
    public Color getChartTitleBoxBackgroundColor() {
        return ChartColor.getAWTColor(ChartColor.GREY);
    }

    @Override
    public Color getChartTitleBoxBorderColor() {
        return ChartColor.getAWTColor(ChartColor.GREY);
    }

    @Override
    public int getChartTitlePadding() {
        return 5;
    }

    @Override
    public Font getLegendFont() {
        return new Font("SansSerif", 0, 14);
    }

    @Override
    public boolean isLegendVisible() {
        return true;
    }

    @Override
    public Color getLegendBackgroundColor() {
        return ChartColor.getAWTColor(ChartColor.WHITE);
    }

    @Override
    public Color getLegendBorderColor() {
        return ChartColor.getAWTColor(ChartColor.WHITE);
    }

    @Override
    public int getLegendPadding() {
        return 10;
    }

    @Override
    public int getLegendSeriesLineLength() {
        return 24;
    }

    @Override
    public Styler.LegendPosition getLegendPosition() {
        return Styler.LegendPosition.OutsideE;
    }

    @Override
    public boolean isXAxisTitleVisible() {
        return true;
    }

    @Override
    public boolean isYAxisTitleVisible() {
        return true;
    }

    @Override
    public Font getAxisTitleFont() {
        return new Font("SansSerif", 0, 14);
    }

    @Override
    public boolean isXAxisTicksVisible() {
        return true;
    }

    @Override
    public boolean isYAxisTicksVisible() {
        return true;
    }

    @Override
    public Font getAxisTickLabelsFont() {
        return new Font("SansSerif", 1, 13);
    }

    @Override
    public int getAxisTickMarkLength() {
        return 8;
    }

    @Override
    public int getAxisTickPadding() {
        return 5;
    }

    @Override
    public int getPlotMargin() {
        return 0;
    }

    @Override
    public boolean isAxisTicksLineVisible() {
        return false;
    }

    @Override
    public boolean isAxisTicksMarksVisible() {
        return true;
    }

    @Override
    public Color getAxisTickMarksColor() {
        return ChartColor.getAWTColor(ChartColor.DARK_GREY);
    }

    @Override
    public Stroke getAxisTickMarksStroke() {
        return new BasicStroke(1.5f, 0, 2, 10.0f, new float[]{3.0f, 0.0f}, 0.0f);
    }

    @Override
    public Color getAxisTickLabelsColor() {
        return ChartColor.getAWTColor(ChartColor.DARK_GREY);
    }

    @Override
    public int getAxisTitlePadding() {
        return 10;
    }

    @Override
    public int getXAxisTickMarkSpacingHint() {
        return 74;
    }

    @Override
    public int getYAxisTickMarkSpacingHint() {
        return 44;
    }

    @Override
    public boolean isPlotGridLinesVisible() {
        return true;
    }

    @Override
    public boolean isPlotGridVerticalLinesVisible() {
        return true;
    }

    @Override
    public boolean isPlotGridHorizontalLinesVisible() {
        return true;
    }

    @Override
    public Color getPlotBackgroundColor() {
        return ChartColor.getAWTColor(ChartColor.LIGHT_GREY);
    }

    @Override
    public Color getPlotBorderColor() {
        return ChartColor.getAWTColor(ChartColor.WHITE);
    }

    @Override
    public boolean isPlotBorderVisible() {
        return false;
    }

    @Override
    public boolean isPlotTicksMarksVisible() {
        return false;
    }

    @Override
    public Color getPlotGridLinesColor() {
        return ChartColor.getAWTColor(ChartColor.WHITE);
    }

    @Override
    public Stroke getPlotGridLinesStroke() {
        return new BasicStroke(1.5f, 0, 2, 10.0f, new float[]{3.0f, 0.0f}, 0.0f);
    }

    @Override
    public double getPlotContentSize() {
        return 0.92;
    }

    @Override
    public double getAvailableSpaceFill() {
        return 0.9;
    }

    @Override
    public boolean isOverlapped() {
        return false;
    }

    @Override
    public boolean isCircular() {
        return true;
    }

    @Override
    public double getStartAngleInDegrees() {
        return 0.0;
    }

    @Override
    public Font getPieFont() {
        return new Font("SansSerif", 0, 15);
    }

    @Override
    public double getAnnotationDistance() {
        return 0.67;
    }

    @Override
    public PieStyler.AnnotationType getAnnotationType() {
        return PieStyler.AnnotationType.LabelAndPercentage;
    }

    @Override
    public boolean isDrawAllAnnotations() {
        return false;
    }

    @Override
    public double getDonutThickness() {
        return 0.25;
    }

    @Override
    public int getMarkerSize() {
        return 8;
    }

    @Override
    public boolean showMarkers() {
        return true;
    }

    @Override
    public Color getErrorBarsColor() {
        return ChartColor.getAWTColor(ChartColor.DARK_GREY);
    }

    @Override
    public boolean isErrorBarsColorSeriesColor() {
        return false;
    }

    @Override
    public Font getAnnotationFont() {
        return new Font("SansSerif", 0, 12);
    }
}

