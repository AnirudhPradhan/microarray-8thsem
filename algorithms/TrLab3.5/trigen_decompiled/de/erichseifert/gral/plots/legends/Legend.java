/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.legends;

import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.graphics.Container;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.Orientation;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Dimension2D;

public interface Legend
extends Container,
Drawable {
    public void add(DataSource var1);

    public boolean contains(DataSource var1);

    public void remove(DataSource var1);

    public void clear();

    public void refresh();

    public Font getBaseFont();

    public void setBaseFont(Font var1);

    public Paint getBackground();

    public void setBackground(Paint var1);

    public Stroke getBorderStroke();

    public void setBorderStroke(Stroke var1);

    public Font getFont();

    public void setFont(Font var1);

    public Paint getBorderColor();

    public void setBorderColor(Paint var1);

    public Orientation getOrientation();

    public void setOrientation(Orientation var1);

    public Dimension2D getSymbolSize();

    public void setSymbolSize(Dimension2D var1);

    public double getAlignmentX();

    public void setAlignmentX(double var1);

    public double getAlignmentY();

    public void setAlignmentY(double var1);

    public Dimension2D getGap();

    public void setGap(Dimension2D var1);
}

