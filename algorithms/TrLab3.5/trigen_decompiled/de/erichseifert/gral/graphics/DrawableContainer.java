/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.graphics;

import de.erichseifert.gral.graphics.AbstractDrawable;
import de.erichseifert.gral.graphics.Container;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.graphics.layout.Layout;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DrawableContainer
extends AbstractDrawable
implements Container {
    private static final long serialVersionUID = 3741045651357559308L;
    private final Insets2D a = new Insets2D.Double();
    private Layout b;
    private final Queue<Drawable> c = new ConcurrentLinkedQueue<Drawable>();
    private final Map<Drawable, Object> d = new HashMap<Drawable, Object>();

    public DrawableContainer() {
        this(null);
    }

    public DrawableContainer(Layout layout) {
        this.b = layout;
    }

    @Override
    public void draw(DrawingContext drawingContext) {
        this.drawComponents(drawingContext);
    }

    protected void drawComponents(DrawingContext drawingContext) {
        for (Drawable drawable : this) {
            drawable.draw(drawingContext);
        }
    }

    @Override
    public void add(Drawable drawable) {
        this.add(drawable, null);
    }

    @Override
    public void add(Drawable drawable, Object object) {
        if (drawable == this) {
            throw new IllegalArgumentException("A container cannot be added to itself.");
        }
        this.d.put(drawable, object);
        this.c.add(drawable);
        this.layout();
    }

    @Override
    public boolean contains(Drawable drawable) {
        return this.c.contains(drawable);
    }

    @Override
    public List<Drawable> getDrawablesAt(Point2D point2D) {
        return DrawableContainer.a(this, point2D, new LinkedList<Drawable>());
    }

    @Override
    public List<Drawable> getDrawables() {
        ArrayList<Drawable> arrayList = new ArrayList<Drawable>(this.c.size());
        arrayList.addAll(this.c);
        return arrayList;
    }

    private static List<Drawable> a(Container object, Point2D point2D, LinkedList<Drawable> linkedList) {
        if (object instanceof Drawable && object.getBounds().contains(point2D)) {
            linkedList.addFirst((Drawable)object);
        }
        object = object.iterator();
        while (object.hasNext()) {
            Drawable drawable = (Drawable)object.next();
            if (drawable instanceof Container) {
                DrawableContainer.a((Container)((Object)drawable), point2D, linkedList);
                continue;
            }
            if (drawable == null || !drawable.getBounds().contains(point2D)) continue;
            linkedList.addFirst(drawable);
        }
        return linkedList;
    }

    @Override
    public Object getConstraints(Drawable drawable) {
        return this.d.get(drawable);
    }

    @Override
    public void remove(Drawable drawable) {
        this.c.remove(drawable);
        this.d.remove(drawable);
        this.layout();
    }

    @Override
    public Insets2D getInsets() {
        Insets2D.Double double_ = new Insets2D.Double();
        ((Insets2D)double_).setInsets(this.a);
        return double_;
    }

    @Override
    public void setInsets(Insets2D insets2D) {
        if (insets2D == this.a || this.a.equals(insets2D)) {
            return;
        }
        this.a.setInsets(insets2D);
        this.layout();
    }

    @Override
    public Layout getLayout() {
        return this.b;
    }

    @Override
    public void setLayout(Layout layout) {
        this.b = layout;
        this.layout();
    }

    @Override
    public void layout() {
        Layout layout = this.getLayout();
        if (layout != null) {
            layout.layout(this);
        }
    }

    @Override
    public Iterator<Drawable> iterator() {
        return this.c.iterator();
    }

    @Override
    public int size() {
        return this.c.size();
    }

    @Override
    public void setBounds(Rectangle2D rectangle2D) {
        super.setBounds(rectangle2D);
        this.layout();
    }

    @Override
    public void setBounds(double d, double d2, double d3, double d4) {
        super.setBounds(d, d2, d3, d4);
        this.layout();
    }

    @Override
    public Dimension2D getPreferredSize() {
        Layout layout = this.getLayout();
        if (layout != null) {
            return layout.getPreferredSize(this);
        }
        return super.getPreferredSize();
    }
}

