/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.util;

import java.awt.geom.Rectangle2D;

public class PageSize {
    public static final PageSize A3 = new PageSize(297.0, 420.0);
    public static final PageSize A4 = new PageSize(210.0, 297.0);
    public static final PageSize A5 = new PageSize(148.0, 210.0);
    public static final PageSize LETTER = new PageSize(21.59, 27.94);
    public static final PageSize LEGAL = new PageSize(21.59, 35.56);
    public static final PageSize TABLOID = new PageSize(27.94, 43.18);
    public static final PageSize LEDGER = TABLOID.getLandscape();
    public final double x;
    public final double y;
    public final double width;
    public final double height;

    public PageSize(double d, double d2, double d3, double d4) {
        this.x = d;
        this.y = d2;
        this.width = d3;
        this.height = d4;
    }

    public PageSize(double d, double d2) {
        this(0.0, 0.0, d, d2);
    }

    public PageSize(Rectangle2D rectangle2D) {
        this(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    public PageSize getPortrait() {
        if (this.width <= this.height) {
            return this;
        }
        return new PageSize(this.x, this.y, this.height, this.width);
    }

    public PageSize getLandscape() {
        if (this.width >= this.height) {
            return this;
        }
        return new PageSize(this.x, this.y, this.height, this.width);
    }
}

