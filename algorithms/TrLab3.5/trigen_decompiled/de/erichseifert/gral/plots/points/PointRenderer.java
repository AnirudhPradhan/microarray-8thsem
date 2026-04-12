/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.points;

import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.Location;
import de.erichseifert.gral.plots.colors.ColorMapper;
import de.erichseifert.gral.plots.points.PointData;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.text.Format;

public interface PointRenderer {
    public Shape getShape();

    public void setShape(Shape var1);

    public ColorMapper getColor();

    public void setColor(ColorMapper var1);

    public void setColor(Paint var1);

    public boolean isValueVisible();

    public void setValueVisible(boolean var1);

    public int getValueColumn();

    public void setValueColumn(int var1);

    public Format getValueFormat();

    public void setValueFormat(Format var1);

    public Location getValueLocation();

    public void setValueLocation(Location var1);

    public double getValueAlignmentX();

    public void setValueAlignmentX(double var1);

    public double getValueAlignmentY();

    public void setValueAlignmentY(double var1);

    public double getValueRotation();

    public void setValueRotation(double var1);

    public double getValueDistance();

    public void setValueDistance(double var1);

    public ColorMapper getValueColor();

    public void setValueColor(ColorMapper var1);

    public void setValueColor(Paint var1);

    public Font getValueFont();

    public void setValueFont(Font var1);

    public boolean isErrorVisible();

    public void setErrorVisible(boolean var1);

    public int getErrorColumnTop();

    public void setErrorColumnTop(int var1);

    public int getErrorColumnBottom();

    public void setErrorColumnBottom(int var1);

    public ColorMapper getErrorColor();

    public void setErrorColor(ColorMapper var1);

    public void setErrorColor(Paint var1);

    public Shape getErrorShape();

    public void setErrorShape(Shape var1);

    public Stroke getErrorStroke();

    public void setErrorStroke(Stroke var1);

    public Shape getPointShape(PointData var1);

    public Drawable getPoint(PointData var1, Shape var2);

    public Drawable getValue(PointData var1, Shape var2);
}

