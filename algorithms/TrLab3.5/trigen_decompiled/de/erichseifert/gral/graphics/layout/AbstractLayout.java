/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.graphics.layout;

import de.erichseifert.gral.graphics.layout.Layout;

public abstract class AbstractLayout
implements Layout {
    private static final long serialVersionUID = 5961215915010787754L;
    private double a;
    private double b;

    public AbstractLayout(double d, double d2) {
        this.a = d;
        this.b = d2;
    }

    @Override
    public double getGapX() {
        return this.a;
    }

    @Override
    public void setGapX(double d) {
        this.a = d;
    }

    @Override
    public double getGapY() {
        return this.b;
    }

    @Override
    public void setGapY(double d) {
        this.b = d;
    }
}

