/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.graphics;

import java.io.Serializable;
import java.util.Locale;

public abstract class Insets2D
implements Serializable {
    private static final long serialVersionUID = 8685228413052838087L;

    public abstract double getTop();

    public abstract double getLeft();

    public abstract double getBottom();

    public abstract double getRight();

    public double getHorizontal() {
        return this.getRight() + this.getLeft();
    }

    public double getVertical() {
        return this.getTop() + this.getBottom();
    }

    public abstract void setInsets(Insets2D var1);

    public abstract void setInsets(double var1, double var3, double var5, double var7);

    public static class Double
    extends Insets2D {
        private static final long serialVersionUID = -6637052175330595647L;
        private double a;
        private double b;
        private double c;
        private double d;

        public Double() {
            this(0.0);
        }

        public Double(double d) {
            this(d, d, d, d);
        }

        public Double(double d, double d2, double d3, double d4) {
            this.setInsets(d, d2, d3, d4);
        }

        @Override
        public double getTop() {
            return this.a;
        }

        @Override
        public double getLeft() {
            return this.b;
        }

        @Override
        public double getBottom() {
            return this.c;
        }

        @Override
        public double getRight() {
            return this.d;
        }

        @Override
        public void setInsets(Insets2D insets2D) {
            if (insets2D == null) {
                return;
            }
            this.setInsets(insets2D.getTop(), insets2D.getLeft(), insets2D.getBottom(), insets2D.getRight());
        }

        @Override
        public void setInsets(double d, double d2, double d3, double d4) {
            this.a = d;
            this.b = d2;
            this.c = d3;
            this.d = d4;
        }

        public String toString() {
            return String.format(Locale.US, "%s[top=%f, left=%f, bottom=%f, right=%f]", this.getClass().getName(), this.a, this.b, this.c, this.d);
        }

        public boolean equals(Object object) {
            if (!(object instanceof Insets2D)) {
                return false;
            }
            object = (Insets2D)object;
            return this.getTop() == ((Insets2D)object).getTop() && this.getLeft() == ((Insets2D)object).getLeft() && this.getBottom() == ((Insets2D)object).getBottom() && this.getRight() == ((Insets2D)object).getRight();
        }

        public int hashCode() {
            long l = java.lang.Double.doubleToLongBits(this.getTop());
            l += java.lang.Double.doubleToLongBits(this.getLeft()) * 37L;
            l += java.lang.Double.doubleToLongBits(this.getBottom()) * 43L;
            return (int)(l += java.lang.Double.doubleToLongBits(this.getRight()) * 47L) ^ (int)(l >> 32);
        }
    }
}

