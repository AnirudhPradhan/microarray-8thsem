/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.areas;

import de.erichseifert.gral.plots.areas.AreaRenderer;
import java.awt.Color;
import java.awt.Paint;
import java.io.Serializable;

public abstract class AbstractAreaRenderer
implements AreaRenderer,
Serializable {
    private static final long serialVersionUID = -9064749128190128428L;
    private double a = 0.0;
    private boolean b = false;
    private Paint c = Color.GRAY;

    @Override
    public double getGap() {
        return this.a;
    }

    @Override
    public void setGap(double d) {
        this.a = d;
    }

    @Override
    public boolean isGapRounded() {
        return this.b;
    }

    @Override
    public void setGapRounded(boolean bl) {
        this.b = bl;
    }

    @Override
    public Paint getColor() {
        return this.c;
    }

    @Override
    public void setColor(Paint paint) {
        this.c = paint;
    }
}

