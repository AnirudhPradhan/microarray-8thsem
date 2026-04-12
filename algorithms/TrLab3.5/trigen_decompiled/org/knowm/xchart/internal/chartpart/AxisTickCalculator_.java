/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.RectangularShape;
import java.util.LinkedList;
import java.util.List;
import org.knowm.xchart.internal.chartpart.Axis;
import org.knowm.xchart.style.AxesChartStyler;

public abstract class AxisTickCalculator_ {
    protected List<Double> tickLocations = new LinkedList<Double>();
    protected List<String> tickLabels = new LinkedList<String>();
    protected final Axis.Direction axisDirection;
    protected final double workingSpace;
    protected final double minValue;
    protected final double maxValue;
    protected final AxesChartStyler styler;

    public AxisTickCalculator_(Axis.Direction axisDirection, double workingSpace, double minValue, double maxValue, AxesChartStyler styler) {
        this.axisDirection = axisDirection;
        this.workingSpace = workingSpace;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.styler = styler;
    }

    double getFirstPosition(double gridStep) {
        double firstPosition = this.minValue - this.minValue % gridStep - gridStep;
        return firstPosition;
    }

    public List<Double> getTickLocations() {
        return this.tickLocations;
    }

    public List<String> getTickLabels() {
        return this.tickLabels;
    }

    boolean willLabelsFitInTickSpaceHint(List<String> tickLabels, int tickSpacingHint) {
        if (this.axisDirection == Axis.Direction.Y) {
            return true;
        }
        String sampleLabel = " ";
        for (int i = 0; i < tickLabels.size(); ++i) {
            if (tickLabels.get(i) == null || tickLabels.get(i).length() <= sampleLabel.length()) continue;
            sampleLabel = tickLabels.get(i);
        }
        TextLayout textLayout = new TextLayout(sampleLabel, this.styler.getAxisTickLabelsFont(), new FontRenderContext(null, true, false));
        AffineTransform rot = this.styler.getXAxisLabelRotation() == 0 ? null : AffineTransform.getRotateInstance(-1.0 * Math.toRadians(this.styler.getXAxisLabelRotation()));
        Shape shape = textLayout.getOutline(rot);
        Rectangle rectangle = shape.getBounds();
        double largestLabelWidth = ((RectangularShape)rectangle).getWidth();
        return largestLabelWidth * 1.1 < (double)tickSpacingHint;
    }
}

