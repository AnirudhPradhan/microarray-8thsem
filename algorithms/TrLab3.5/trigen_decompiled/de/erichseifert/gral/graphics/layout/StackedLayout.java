/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.graphics.layout;

import de.erichseifert.gral.graphics.Container;
import de.erichseifert.gral.graphics.Dimension2D;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.graphics.Orientation;
import de.erichseifert.gral.graphics.layout.AbstractOrientedLayout;
import java.awt.geom.Dimension2D;
import java.awt.geom.RectangularShape;
import java.io.Serializable;
import java.util.Iterator;

public class StackedLayout
extends AbstractOrientedLayout {
    private static final long serialVersionUID = -3183337606556363756L;
    private final Constraints a = new Constraints(true, 0.5, 0.5);

    public StackedLayout(Orientation orientation) {
        this(orientation, 0.0, 0.0);
    }

    public StackedLayout(Orientation orientation, double d, double d2) {
        super(orientation, d, d2);
    }

    @Override
    public void layout(Container container) {
        Iterator iterator = this.getPreferredSize(container);
        Object object2 = container.getBounds();
        Insets2D insets2D = container.getInsets();
        double d = ((RectangularShape)object2).getMinX() + insets2D.getLeft();
        double d2 = ((RectangularShape)object2).getMinY() + insets2D.getTop();
        double d3 = ((RectangularShape)object2).getWidth() - insets2D.getLeft() - insets2D.getRight();
        double d4 = ((RectangularShape)object2).getHeight() - insets2D.getTop() - insets2D.getBottom();
        int n = 0;
        if (this.getOrientation() == Orientation.HORIZONTAL) {
            d += Math.max(((RectangularShape)object2).getWidth() - ((Dimension2D)((Object)iterator)).getWidth(), 0.0) * this.a.getAlignmentX();
            for (Object object2 : container) {
                double d5;
                double d6;
                if (n++ > 0) {
                    d += this.getGapX();
                }
                Dimension2D dimension2D = object2.getPreferredSize();
                Constraints constraints = this.a((Drawable)object2, container);
                if (constraints.isStrechted()) {
                    d6 = d4;
                    d5 = d2;
                } else {
                    d6 = Math.min(dimension2D.getHeight(), d4);
                    d5 = d2 + (d4 - d6) * constraints.getAlignmentY();
                }
                object2.setBounds(d, d5, dimension2D.getWidth(), d6);
                d += dimension2D.getWidth();
            }
            return;
        }
        if (this.getOrientation() == Orientation.VERTICAL) {
            d2 += Math.max(((RectangularShape)object2).getHeight() - ((Dimension2D)((Object)iterator)).getHeight(), 0.0) * this.a.getAlignmentY();
            for (Object object2 : container) {
                double d7;
                double d8;
                if (n++ > 0) {
                    d2 += this.getGapY();
                }
                Dimension2D dimension2D = object2.getPreferredSize();
                Constraints constraints = this.a((Drawable)object2, container);
                if (constraints.isStrechted()) {
                    d8 = d3;
                    d7 = d;
                } else {
                    d8 = Math.min(dimension2D.getWidth(), d3);
                    d7 = d + (d3 - d8) * constraints.getAlignmentX();
                }
                object2.setBounds(d7, d2, d8, dimension2D.getHeight());
                d2 += dimension2D.getHeight();
            }
        }
    }

    @Override
    public Dimension2D getPreferredSize(Container iterator) {
        Insets2D insets2D = iterator.getInsets();
        double d = insets2D.getLeft();
        double d2 = insets2D.getTop();
        int n = 0;
        if (this.getOrientation() == Orientation.HORIZONTAL) {
            double d3 = 0.0;
            iterator = iterator.iterator();
            while (iterator.hasNext()) {
                Object object = (Drawable)iterator.next();
                if (n++ > 0) {
                    d += this.getGapX();
                }
                object = object.getPreferredSize();
                d += ((Dimension2D)object).getWidth();
                d3 = Math.max(d2, ((Dimension2D)object).getHeight());
            }
            d2 += d3;
        } else if (this.getOrientation() == Orientation.VERTICAL) {
            double d4 = 0.0;
            iterator = iterator.iterator();
            while (iterator.hasNext()) {
                Object object = (Drawable)iterator.next();
                if (n++ > 0) {
                    d2 += this.getGapY();
                }
                object = object.getPreferredSize();
                d4 = Math.max(d4, ((Dimension2D)object).getWidth());
                d2 += ((Dimension2D)object).getHeight();
            }
            d += d4;
        }
        Dimension2D.Double double_ = new Dimension2D.Double(d += insets2D.getRight(), d2 += insets2D.getBottom());
        return double_;
    }

    private Constraints a(Drawable object, Container container) {
        if ((object = container.getConstraints((Drawable)object)) == null || !(object instanceof Constraints)) {
            object = this.a;
        }
        return (Constraints)object;
    }

    public static class Constraints
    implements Serializable {
        private static final long serialVersionUID = -3375316557720116460L;
        private final boolean a;
        private final double b;
        private final double c;

        public Constraints(boolean bl, double d, double d2) {
            this.a = bl;
            this.b = d;
            this.c = d2;
        }

        public boolean isStrechted() {
            return this.a;
        }

        public double getAlignmentX() {
            return this.b;
        }

        public double getAlignmentY() {
            return this.c;
        }
    }
}

