/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.graphics.layout;

import de.erichseifert.gral.graphics.Container;
import de.erichseifert.gral.graphics.Dimension2D;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.graphics.layout.AbstractLayout;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

public class TableLayout
extends AbstractLayout {
    private static final long serialVersionUID = -6738742507926295041L;
    private final int a;

    public TableLayout(int n, double d, double d2) {
        super(d, d2);
        if (n <= 0) {
            throw new IllegalArgumentException("Invalid number of columns.");
        }
        this.a = n;
    }

    public TableLayout(int n) {
        this(n, 0.0, 0.0);
    }

    private a[] a(Container container) {
        Object object2;
        a[] aArray = new a[2];
        a[] aArray2 = aArray;
        aArray[0] = new a();
        aArray2[1] = new a();
        aArray2[0].b = this.a;
        aArray2[1].b = (int)Math.ceil((double)container.size() / (double)this.a);
        int n = 0;
        for (Object object2 : container) {
            Integer n2 = n % aArray2[0].b;
            Integer n3 = n / aArray2[0].b;
            Double d = aArray2[0].a.get(n2);
            Double d2 = aArray2[1].a.get(n3);
            object2 = object2.getPreferredSize();
            aArray2[0].a.put(n2, TableLayout.a(object2.getWidth(), d));
            aArray2[1].a.put(n3, TableLayout.a(object2.getHeight(), d2));
            ++n;
        }
        Rectangle2D rectangle2D = container.getBounds();
        object2 = container.getInsets();
        if (object2 == null) {
            object2 = new Insets2D.Double();
        }
        aArray2[0].d = object2.getLeft() + object2.getRight();
        aArray2[1].d = object2.getTop() + object2.getBottom();
        aArray2[0].e = Math.max((double)(aArray2[0].b - 1) * this.getGapX(), 0.0);
        aArray2[1].e = Math.max((double)(aArray2[1].b - 1) * this.getGapY(), 0.0);
        double d = Math.max(rectangle2D.getWidth() - aArray2[0].d - aArray2[0].e, 0.0);
        double d3 = Math.max(rectangle2D.getHeight() - aArray2[1].d - aArray2[1].e, 0.0);
        aArray2[0].f = aArray2[0].b > 0 ? d / (double)aArray2[0].b : 0.0;
        aArray2[1].f = aArray2[1].b > 0 ? d3 / (double)aArray2[1].b : 0.0;
        object2 = aArray2;
        for (int i = 0; i < 2; ++i) {
            a a2 = object2[i];
            object2[i].c = 0.0;
            a2.g = 0.0;
            int n4 = 0;
            for (double d4 : a2.a.values()) {
                a2.c += d4;
                if (d4 >= a2.f) {
                    a2.g += d4 - a2.f;
                    continue;
                }
                ++n4;
            }
            if (n4 <= 0) continue;
            a2.g /= (double)n4;
        }
        return aArray2;
    }

    @Override
    public void layout(Container object) {
        a[] aArray = this.a((Container)object);
        Rectangle2D rectangle2D = object.getBounds();
        Insets2D insets2D = object.getInsets();
        if (insets2D == null) {
            insets2D = new Insets2D.Double();
        }
        Integer n = aArray[0].b - 1;
        int n2 = 0;
        double d = rectangle2D.getX() + insets2D.getLeft();
        double d2 = rectangle2D.getY() + insets2D.getTop();
        object = object.iterator();
        while (object.hasNext()) {
            Drawable drawable = (Drawable)object.next();
            Integer n3 = n2 % aArray[0].b;
            Integer n4 = n2 / aArray[0].b;
            double d3 = aArray[0].a.get(n3);
            double d4 = aArray[1].a.get(n4);
            double d5 = Math.max(aArray[0].f - aArray[0].g, d3);
            double d6 = Math.max(aArray[1].f - aArray[1].g, d4);
            if (drawable != null) {
                drawable.setBounds(d, d2, d5, d6);
            }
            if (n3.equals(n)) {
                d = rectangle2D.getX() + insets2D.getLeft();
                d2 += d6 + this.getGapY();
            } else {
                d += d5 + this.getGapX();
            }
            ++n2;
        }
    }

    @Override
    public Dimension2D getPreferredSize(Container aArray) {
        aArray = this.a((Container)aArray);
        return new Dimension2D.Double(aArray[0].c + aArray[0].e + aArray[0].d, aArray[1].c + aArray[1].e + aArray[1].d);
    }

    public int getColumns() {
        return this.a;
    }

    private static <T extends Comparable<T>> T a(T t, T t2) {
        if (t == null || t2 == null) {
            if (t == null) {
                return t2;
            }
            return t;
        }
        if (t.compareTo(t2) >= 0) {
            return t;
        }
        return t2;
    }

    private static final class a {
        public final Map<Integer, Double> a = new HashMap<Integer, Double>();
        public int b;
        public double c;
        public double d;
        public double e;
        public double f;
        public double g;
    }
}

