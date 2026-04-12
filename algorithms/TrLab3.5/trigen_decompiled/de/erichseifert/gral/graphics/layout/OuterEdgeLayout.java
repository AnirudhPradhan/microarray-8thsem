/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.graphics.layout;

import de.erichseifert.gral.graphics.Container;
import de.erichseifert.gral.graphics.Dimension2D;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.graphics.Location;
import de.erichseifert.gral.graphics.layout.AbstractLayout;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

public class OuterEdgeLayout
extends AbstractLayout {
    private static final long serialVersionUID = -2238929452967312857L;

    public OuterEdgeLayout(double d) {
        super(d, d);
    }

    public OuterEdgeLayout() {
        this(0.0);
    }

    @Override
    public void layout(Container object) {
        Object object2 = OuterEdgeLayout.a((Container)object);
        Drawable drawable = object2.get((Object)Location.NORTH);
        Drawable drawable2 = object2.get((Object)Location.NORTH_EAST);
        Drawable drawable3 = object2.get((Object)Location.EAST);
        Drawable drawable4 = object2.get((Object)Location.SOUTH_EAST);
        Drawable drawable5 = object2.get((Object)Location.SOUTH);
        Drawable drawable6 = object2.get((Object)Location.SOUTH_WEST);
        Drawable drawable7 = object2.get((Object)Location.WEST);
        Drawable drawable8 = object2.get((Object)Location.NORTH_WEST);
        object2 = object2.get((Object)Location.CENTER);
        double d = OuterEdgeLayout.a(drawable8, drawable7, drawable6);
        double d2 = OuterEdgeLayout.a(drawable2, drawable3, drawable4);
        double d3 = OuterEdgeLayout.b(drawable8, drawable, drawable2);
        double d4 = OuterEdgeLayout.b(drawable6, drawable5, drawable4);
        double d5 = d2 > 0.0 ? this.getGapX() : 0.0;
        double d6 = d > 0.0 ? this.getGapX() : 0.0;
        double d7 = d3 > 0.0 ? this.getGapY() : 0.0;
        double d8 = d4 > 0.0 ? this.getGapY() : 0.0;
        Rectangle2D rectangle2D = object.getBounds();
        if ((object = object.getInsets()) == null) {
            object = new Insets2D.Double();
        }
        double d9 = rectangle2D.getMinX() + ((Insets2D)object).getLeft() - d6 - d;
        double d10 = rectangle2D.getMinX() + ((Insets2D)object).getLeft();
        double d11 = rectangle2D.getMaxX() - ((Insets2D)object).getRight() + d5;
        double d12 = rectangle2D.getMinY() + ((Insets2D)object).getTop() - d7 - d3;
        double d13 = rectangle2D.getMinY() + ((Insets2D)object).getTop();
        double d14 = rectangle2D.getMaxY() - ((Insets2D)object).getBottom() + d8;
        OuterEdgeLayout.a(drawable8, d9, d12, d, d3);
        OuterEdgeLayout.a(drawable, d10, d12, rectangle2D.getWidth() - ((Insets2D)object).getHorizontal(), d3);
        OuterEdgeLayout.a(drawable2, d11, d12, d2, d3);
        OuterEdgeLayout.a(drawable3, d11, d13, d2, rectangle2D.getHeight() - ((Insets2D)object).getVertical());
        OuterEdgeLayout.a(drawable4, d11, d14, d2, d4);
        OuterEdgeLayout.a(drawable5, d10, d14, rectangle2D.getWidth() - ((Insets2D)object).getHorizontal(), d4);
        OuterEdgeLayout.a(drawable6, d9, d14, d, d4);
        OuterEdgeLayout.a(drawable7, d9, d13, d, rectangle2D.getHeight() - ((Insets2D)object).getVertical());
        OuterEdgeLayout.a((Drawable)object2, d10 + this.getGapX(), d13 + this.getGapY(), rectangle2D.getWidth() - ((Insets2D)object).getHorizontal() - 2.0 * this.getGapX(), rectangle2D.getHeight() - ((Insets2D)object).getVertical() - 2.0 * this.getGapY());
    }

    @Override
    public Dimension2D getPreferredSize(Container object) {
        Object object2 = OuterEdgeLayout.a((Container)object);
        object2 = object2.get((Object)Location.CENTER);
        if ((object = object.getInsets()) == null) {
            object = new Insets2D.Double();
        }
        double d = object2.getWidth() + ((Insets2D)object).getHorizontal() + 2.0 * this.getGapX();
        double d2 = object2.getHeight() + ((Insets2D)object).getVertical() + 2.0 * this.getGapY();
        return new Dimension2D.Double(d, d2);
    }

    private static Map<Location, Drawable> a(Container container) {
        HashMap<Location, Drawable> hashMap = new HashMap<Location, Drawable>();
        for (Drawable drawable : container) {
            Object object = container.getConstraints(drawable);
            if (!(object instanceof Location)) continue;
            hashMap.put((Location)((Object)object), drawable);
        }
        return hashMap;
    }

    private static double a(Drawable ... drawableArray) {
        double d = 0.0;
        for (int i = 0; i < 3; ++i) {
            Drawable drawable = drawableArray[i];
            if (drawable == null) continue;
            d = Math.max(d, drawable.getPreferredSize().getWidth());
        }
        return d;
    }

    private static double b(Drawable ... drawableArray) {
        double d = 0.0;
        for (int i = 0; i < 3; ++i) {
            Drawable drawable = drawableArray[i];
            if (drawable == null) continue;
            d = Math.max(d, drawable.getPreferredSize().getHeight());
        }
        return d;
    }

    private static void a(Drawable drawable, double d, double d2, double d3, double d4) {
        if (drawable == null) {
            return;
        }
        drawable.setBounds(d, d2, d3, d4);
    }
}

