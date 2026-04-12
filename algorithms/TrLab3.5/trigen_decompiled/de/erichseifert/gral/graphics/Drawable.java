/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.graphics;

import de.erichseifert.gral.graphics.DrawingContext;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

public interface Drawable {
    public Rectangle2D getBounds();

    public void setBounds(Rectangle2D var1);

    public void setBounds(double var1, double var3, double var5, double var7);

    public double getX();

    public double getY();

    public void setPosition(double var1, double var3);

    public double getWidth();

    public double getHeight();

    public Dimension2D getPreferredSize();

    public void draw(DrawingContext var1);
}

