/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.math.BigDecimal;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.Axis;
import org.knowm.xchart.internal.chartpart.AxisTickCalculator_;
import org.knowm.xchart.internal.chartpart.NumberFormatter;
import org.knowm.xchart.style.AxesChartStyler;

public class AxisTickCalculator_Logarithmic
extends AxisTickCalculator_ {
    NumberFormatter numberFormatter = null;

    public AxisTickCalculator_Logarithmic(Axis.Direction axisDirection, double workingSpace, double minValue, double maxValue, AxesChartStyler styler) {
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
        int logMin = (int)Math.floor(Math.log10(this.minValue));
        int logMax = (int)Math.ceil(Math.log10(this.maxValue));
        double firstPosition = Utils.pow(10.0, logMin);
        double tickStep = Utils.pow(10.0, logMin - 1);
        for (int i = logMin; i <= logMax; ++i) {
            for (double j = firstPosition; j <= Utils.pow(10.0, i) + 1.0E-8; j += tickStep) {
                if (j < this.minValue - tickStep) continue;
                if (j > this.maxValue + tickStep) break;
                if (Math.abs(Math.log10(j) % 1.0) < 1.0E-8) {
                    this.tickLabels.add(this.numberFormatter.formatLogNumber(j, this.axisDirection));
                } else {
                    this.tickLabels.add(null);
                }
                double tickLabelPosition = (int)(margin + (Math.log10(j) - Math.log10(this.minValue)) / (Math.log10(this.maxValue) - Math.log10(this.minValue)) * tickSpace);
                this.tickLocations.add(tickLabelPosition);
            }
            firstPosition = (tickStep *= Utils.pow(10.0, 1)) + Utils.pow(10.0, i);
        }
    }
}

