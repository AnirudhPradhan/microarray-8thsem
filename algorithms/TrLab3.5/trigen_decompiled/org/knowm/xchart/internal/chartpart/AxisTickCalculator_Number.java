/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.Axis;
import org.knowm.xchart.internal.chartpart.AxisTickCalculator_;
import org.knowm.xchart.internal.chartpart.NumberFormatter;
import org.knowm.xchart.style.AxesChartStyler;

public class AxisTickCalculator_Number
extends AxisTickCalculator_ {
    NumberFormatter numberFormatter = null;

    public AxisTickCalculator_Number(Axis.Direction axisDirection, double workingSpace, double minValue, double maxValue, AxesChartStyler styler) {
        super(axisDirection, workingSpace, minValue, maxValue, styler);
        this.numberFormatter = new NumberFormatter(styler);
        this.calculate();
    }

    private void calculate() {
        if (this.minValue == this.maxValue) {
            this.tickLabels.add(this.numberFormatter.formatNumber(BigDecimal.valueOf(this.maxValue), this.minValue, this.maxValue, this.axisDirection));
            this.tickLocations.add(this.workingSpace / 2.0);
            return;
        }
        double tickSpace = this.styler.getPlotContentSize() * this.workingSpace;
        if (tickSpace < (double)this.styler.getXAxisTickMarkSpacingHint()) {
            return;
        }
        double margin = Utils.getTickStartOffset(this.workingSpace, tickSpace);
        double span = Math.abs(Math.min(this.maxValue - this.minValue, Double.MAX_VALUE));
        int tickSpacingHint = (this.axisDirection == Axis.Direction.X ? this.styler.getXAxisTickMarkSpacingHint() : this.styler.getYAxisTickMarkSpacingHint()) - 5;
        if (this.axisDirection == Axis.Direction.Y && tickSpace < 160.0) {
            tickSpacingHint = 20;
        }
        int gridStepInChartSpace = 0;
        do {
            BigDecimal cleanedFirstPosition;
            double gridStepHint;
            this.tickLabels.clear();
            this.tickLocations.clear();
            double significand = gridStepHint = span / tickSpace * (double)(tickSpacingHint += 5);
            int exponent = 0;
            if (significand == 0.0) {
                exponent = 1;
            } else if (significand < 1.0) {
                while (significand < 1.0) {
                    significand *= 10.0;
                    --exponent;
                }
            } else {
                while (significand >= 10.0 || significand == Double.NEGATIVE_INFINITY) {
                    significand /= 10.0;
                    ++exponent;
                }
            }
            double gridStep = significand > 7.5 ? 10.0 * Utils.pow(10.0, exponent) : (significand > 3.5 ? 5.0 * Utils.pow(10.0, exponent) : (significand > 1.5 ? 2.0 * Utils.pow(10.0, exponent) : Utils.pow(10.0, exponent)));
            gridStepInChartSpace = (int)(gridStep / span * tickSpace);
            BigDecimal gridStepBigDecimal = BigDecimal.valueOf(gridStep);
            int scale = Math.max(10, gridStepBigDecimal.scale());
            BigDecimal cleanedGridStep = gridStepBigDecimal.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();
            BigDecimal firstPosition = null;
            try {
                firstPosition = BigDecimal.valueOf(this.getFirstPosition(cleanedGridStep.doubleValue()));
            }
            catch (NumberFormatException e) {
                System.out.println("exponent: " + exponent);
                System.out.println("gridStep: " + gridStep);
                System.out.println("cleanedGridStep: " + cleanedGridStep);
                System.out.println("cleanedGridStep.doubleValue(): " + cleanedGridStep.doubleValue());
                System.out.println("NumberFormatException caused by this number: " + this.getFirstPosition(cleanedGridStep.doubleValue()));
            }
            BigDecimal value = cleanedFirstPosition = firstPosition.setScale(10, RoundingMode.HALF_UP).stripTrailingZeros();
            while (value.compareTo(BigDecimal.valueOf(this.maxValue + 2.0 * cleanedGridStep.doubleValue())) < 0) {
                String tickLabel = this.numberFormatter.formatNumber(value, this.minValue, this.maxValue, this.axisDirection);
                this.tickLabels.add(tickLabel);
                double tickLabelPosition = margin + (value.doubleValue() - this.minValue) / (this.maxValue - this.minValue) * tickSpace;
                this.tickLocations.add(tickLabelPosition);
                value = value.add(cleanedGridStep);
            }
        } while (!this.willLabelsFitInTickSpaceHint(this.tickLabels, gridStepInChartSpace));
    }
}

