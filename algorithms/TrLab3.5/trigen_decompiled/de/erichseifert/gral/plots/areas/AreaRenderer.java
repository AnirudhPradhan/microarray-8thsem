/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.areas;

import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.plots.DataPoint;
import java.awt.Paint;
import java.awt.Shape;
import java.util.List;

public interface AreaRenderer {
    public Shape getAreaShape(List<DataPoint> var1);

    public Drawable getArea(List<DataPoint> var1, Shape var2);

    public double getGap();

    public void setGap(double var1);

    public boolean isGapRounded();

    public void setGapRounded(boolean var1);

    public Paint getColor();

    public void setColor(Paint var1);
}

