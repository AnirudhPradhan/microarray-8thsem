/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.Axis;
import org.knowm.xchart.internal.chartpart.AxisTickCalculator_;
import org.knowm.xchart.internal.chartpart.NumberFormatter;
import org.knowm.xchart.style.AxesChartStyler;

public class AxisTickCalculator_Category
extends AxisTickCalculator_ {
    public AxisTickCalculator_Category(Axis.Direction axisDirection, double workingSpace, List<?> categories, Axis.AxisDataType axisType, AxesChartStyler styler) {
        super(axisDirection, workingSpace, Double.NaN, Double.NaN, styler);
        this.calculate(categories, axisType);
    }

    private void calculate(List<?> categories, Axis.AxisDataType axisType) {
        double tickSpace = this.styler.getPlotContentSize() * this.workingSpace;
        double margin = Utils.getTickStartOffset(this.workingSpace, tickSpace);
        double gridStep = tickSpace / (double)categories.size();
        double firstPosition = gridStep / 2.0;
        NumberFormatter numberFormatter = null;
        SimpleDateFormat simpleDateformat = null;
        if (axisType == Axis.AxisDataType.Number) {
            numberFormatter = new NumberFormatter(this.styler);
        } else if (axisType == Axis.AxisDataType.Date) {
            if (this.styler.getDatePattern() == null) {
                throw new RuntimeException("You need to set the Date Formatting Pattern!!!");
            }
            simpleDateformat = new SimpleDateFormat(this.styler.getDatePattern(), this.styler.getLocale());
            simpleDateformat.setTimeZone(this.styler.getTimezone());
        }
        int counter = 0;
        for (Object category : categories) {
            if (axisType == Axis.AxisDataType.String) {
                this.tickLabels.add(category.toString());
            } else if (axisType == Axis.AxisDataType.Number) {
                this.tickLabels.add(numberFormatter.formatNumber(new BigDecimal(category.toString()), this.minValue, this.maxValue, this.axisDirection));
            } else if (axisType == Axis.AxisDataType.Date) {
                this.tickLabels.add(simpleDateformat.format(((Date)category).getTime()));
            }
            double tickLabelPosition = margin + firstPosition + gridStep * (double)counter++;
            this.tickLocations.add(tickLabelPosition);
        }
    }
}

