/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.graphics;

import java.io.Serializable;
import java.util.Locale;

public abstract class Dimension2D
extends java.awt.geom.Dimension2D
implements Serializable {
    private static final long serialVersionUID = 6961198271520384282L;

    public static class Double
    extends Dimension2D {
        private static final long serialVersionUID = -4341712269787906650L;
        private double a;
        private double b;

        public Double() {
            this.setSize(0.0, 0.0);
        }

        public Double(double d, double d2) {
            this.setSize(d, d2);
        }

        @Override
        public double getHeight() {
            return this.b;
        }

        @Override
        public double getWidth() {
            return this.a;
        }

        @Override
        public void setSize(double d, double d2) {
            this.a = d;
            this.b = d2;
        }

        public String toString() {
            return String.format(Locale.US, "%s[width=%f, height=%f]", this.getClass().getName(), this.a, this.b);
        }

        public boolean equals(Object object) {
            if (!(object instanceof java.awt.geom.Dimension2D)) {
                return false;
            }
            object = (java.awt.geom.Dimension2D)object;
            return this.getWidth() == ((java.awt.geom.Dimension2D)object).getWidth() && this.getHeight() == ((java.awt.geom.Dimension2D)object).getHeight();
        }

        public int hashCode() {
            long l = java.lang.Double.doubleToLongBits(this.getWidth());
            return (int)(l ^= java.lang.Double.doubleToLongBits(this.getHeight()) * 31L) ^ (int)(l >> 32);
        }
    }
}

