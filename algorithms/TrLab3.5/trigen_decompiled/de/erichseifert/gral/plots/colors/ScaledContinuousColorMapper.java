/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.colors;

import de.erichseifert.gral.plots.colors.AbstractColorMapper;
import de.erichseifert.gral.plots.colors.ContinuousColorMapper;

public abstract class ScaledContinuousColorMapper
extends ContinuousColorMapper {
    private static final long serialVersionUID = 323911118647457851L;
    private double a;
    private double b;

    public ScaledContinuousColorMapper() {
        this(0.0, 1.0);
    }

    public ScaledContinuousColorMapper(double d, double d2) {
        this.a = d;
        this.b = d2;
    }

    public double getOffset() {
        return this.a;
    }

    public void setOffset(double d) {
        this.a = d;
    }

    public double getScale() {
        return this.b;
    }

    public void setScale(double d) {
        this.b = d;
    }

    public void setRange(double d, double d2) {
        this.setOffset(d);
        this.setScale(d2 - d);
    }

    protected Double scale(double d) {
        return (d - this.getOffset()) / this.getScale();
    }

    public boolean equals(Object object) {
        if (!(object instanceof ScaledContinuousColorMapper)) {
            return false;
        }
        object = (ScaledContinuousColorMapper)object;
        return this.getOffset() == ((ScaledContinuousColorMapper)object).getOffset() && this.getScale() == ((ScaledContinuousColorMapper)object).getScale() && this.getMode() == ((AbstractColorMapper)object).getMode();
    }

    public int hashCode() {
        long l = Double.doubleToLongBits(this.getOffset());
        return (int)(l ^= Double.doubleToLongBits(this.getScale()) * 31L) ^ (int)(l >> 32);
    }
}

