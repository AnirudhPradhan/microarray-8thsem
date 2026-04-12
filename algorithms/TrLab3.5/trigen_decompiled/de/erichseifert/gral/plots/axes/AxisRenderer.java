/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.axes;

import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.axes.Tick;
import de.erichseifert.gral.util.PointND;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.text.Format;
import java.util.List;
import java.util.Map;

public interface AxisRenderer {
    public Drawable getRendererComponent(Axis var1);

    public double worldToView(Axis var1, Number var2, boolean var3);

    public Number viewToWorld(Axis var1, double var2, boolean var4);

    public List<Tick> getTicks(Axis var1);

    public PointND<Double> getPosition(Axis var1, Number var2, boolean var3, boolean var4);

    public PointND<Double> getNormal(Axis var1, Number var2, boolean var3, boolean var4);

    public Number getIntersection();

    public void setIntersection(Number var1);

    public Shape getShape();

    public void setShape(Shape var1);

    public boolean isShapeVisible();

    public void setShapeVisible(boolean var1);

    public boolean isShapeNormalOrientationClockwise();

    public void setShapeNormalOrientationClockwise(boolean var1);

    public Paint getShapeColor();

    public void setShapeColor(Paint var1);

    public Stroke getShapeStroke();

    public void setShapeStroke(Stroke var1);

    public boolean isShapeDirectionSwapped();

    public void setShapeDirectionSwapped(boolean var1);

    public boolean isTicksVisible();

    public void setTicksVisible(boolean var1);

    public Number getTickSpacing();

    public void setTickSpacing(Number var1);

    public boolean isTicksAutoSpaced();

    public void setTicksAutoSpaced(boolean var1);

    public double getTickLength();

    public void setTickLength(double var1);

    public Stroke getTickStroke();

    public void setTickStroke(Stroke var1);

    public double getTickAlignment();

    public void setTickAlignment(double var1);

    public Font getTickFont();

    public void setTickFont(Font var1);

    public Paint getTickColor();

    public void setTickColor(Paint var1);

    public boolean isTickLabelsVisible();

    public void setTickLabelsVisible(boolean var1);

    public Format getTickLabelFormat();

    public void setTickLabelFormat(Format var1);

    public double getTickLabelDistance();

    public void setTickLabelDistance(double var1);

    public boolean isTickLabelsOutside();

    public void setTickLabelsOutside(boolean var1);

    public double getTickLabelRotation();

    public void setTickLabelRotation(double var1);

    public boolean isMinorTicksVisible();

    public void setMinorTicksVisible(boolean var1);

    public int getMinorTicksCount();

    public void setMinorTicksCount(int var1);

    public double getMinorTickLength();

    public void setMinorTickLength(double var1);

    public Stroke getMinorTickStroke();

    public void setMinorTickStroke(Stroke var1);

    public double getMinorTickAlignment();

    public void setMinorTickAlignment(double var1);

    public Paint getMinorTickColor();

    public void setMinorTickColor(Paint var1);

    public Map<Double, String> getCustomTicks();

    public void setCustomTicks(Map<Double, String> var1);

    public Label getLabel();

    public void setLabel(Label var1);

    public double getLabelDistance();

    public void setLabelDistance(double var1);
}

