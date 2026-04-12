/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.lines;

import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.plots.DataPoint;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.List;

public interface LineRenderer {
    public Shape getLineShape(List<DataPoint> var1);

    public Drawable getLine(List<DataPoint> var1, Shape var2);

    public Stroke getStroke();

    public void setStroke(Stroke var1);

    public double getGap();

    public void setGap(double var1);

    public boolean isGapRounded();

    public void setGapRounded(boolean var1);

    public Paint getColor();

    public void setColor(Paint var1);
}

