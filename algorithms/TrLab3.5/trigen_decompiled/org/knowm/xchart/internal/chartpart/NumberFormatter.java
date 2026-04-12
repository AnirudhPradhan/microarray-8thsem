/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.knowm.xchart.internal.chartpart.Axis;
import org.knowm.xchart.style.AxesChartStyler;

public class NumberFormatter {
    private final AxesChartStyler styler;

    public NumberFormatter(AxesChartStyler styler) {
        this.styler = styler;
    }

    public String getFormatPattern(BigDecimal value, double min, double max) {
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }
        double difference = max - min;
        int placeOfDifference = difference == 0.0 ? 0 : (int)Math.floor(Math.log(difference) / Math.log(10.0));
        int placeOfValue = value.doubleValue() == 0.0 ? 0 : (int)Math.floor(Math.log(value.doubleValue()) / Math.log(10.0));
        if (placeOfDifference <= 4 && placeOfDifference >= -4) {
            return this.getNormalDecimalPatternPositive(placeOfValue, placeOfDifference);
        }
        return this.getScientificDecimalPattern();
    }

    private String getNormalDecimalPatternPositive(int placeOfValue, int placeOfDifference) {
        int maxNumPlaces = 15;
        StringBuilder sb = new StringBuilder();
        for (int i = maxNumPlaces - 1; i >= -1 * maxNumPlaces; --i) {
            if (i >= 0 && i < placeOfValue) {
                sb.append("0");
            } else if (i < 0 && i > placeOfValue) {
                sb.append("0");
            } else {
                sb.append("#");
            }
            if (i % 3 == 0 && i > 0) {
                sb.append(",");
            }
            if (i != 0) continue;
            sb.append(".");
        }
        return sb.toString();
    }

    private String getScientificDecimalPattern() {
        return "0.###############E0";
    }

    public String formatNumber(BigDecimal value, double min, double max, Axis.Direction axisDirection) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(this.styler.getLocale());
        String decimalPattern = axisDirection == Axis.Direction.X && this.styler.getXAxisDecimalPattern() != null ? this.styler.getXAxisDecimalPattern() : (axisDirection == Axis.Direction.Y && this.styler.getYAxisDecimalPattern() != null ? this.styler.getYAxisDecimalPattern() : (this.styler.getDecimalPattern() != null ? this.styler.getDecimalPattern() : this.getFormatPattern(value, min, max)));
        DecimalFormat normalFormat = (DecimalFormat)numberFormat;
        normalFormat.applyPattern(decimalPattern);
        return normalFormat.format(value);
    }

    public String formatLogNumber(double value, Axis.Direction axisDirection) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(this.styler.getLocale());
        String decimalPattern = axisDirection == Axis.Direction.X && this.styler.getXAxisDecimalPattern() != null ? this.styler.getXAxisDecimalPattern() : (axisDirection == Axis.Direction.Y && this.styler.getYAxisDecimalPattern() != null ? this.styler.getYAxisDecimalPattern() : (this.styler.getDecimalPattern() != null ? this.styler.getDecimalPattern() : (Math.abs(value) > 1000.0 || Math.abs(value) < 0.001 ? "0E0" : "0.###")));
        DecimalFormat normalFormat = (DecimalFormat)numberFormat;
        normalFormat.applyPattern(decimalPattern);
        return normalFormat.format(value);
    }
}

