/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.graphics;

import de.erichseifert.gral.graphics.Dimension2D;
import de.erichseifert.gral.graphics.Drawable;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public abstract class AbstractDrawable
implements Drawable,
Serializable {
    private static final long serialVersionUID = -684598008467326484L;
    private final Rectangle2D a = new Rectangle2D.Double();

    @Override
    public Rectangle2D getBounds() {
        Rectangle2D.Double double_ = new Rectangle2D.Double();
        double_.setFrame(this.a);
        return double_;
    }

    @Override
    public double getX() {
        return this.a.getX();
    }

    @Override
    public double getY() {
        return this.a.getY();
    }

    @Override
    public double getWidth() {
        return this.a.getWidth();
    }

    @Override
    public double getHeight() {
        return this.a.getHeight();
    }

    @Override
    public void setBounds(Rectangle2D rectangle2D) {
        this.setBounds(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override
    public void setBounds(double d, double d2, double d3, double d4) {
        this.a.setFrame(d, d2, d3, d4);
    }

    @Override
    public Dimension2D getPreferredSize() {
        return new Dimension2D.Double();
    }

    @Override
    public void setPosition(double d, double d2) {
        this.a.setFrame(d, d2, this.a.getWidth(), this.a.getHeight());
    }
}

