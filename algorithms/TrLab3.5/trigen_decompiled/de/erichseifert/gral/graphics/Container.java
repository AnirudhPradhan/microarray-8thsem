/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.graphics;

import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.graphics.layout.Layout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public interface Container
extends Iterable<Drawable> {
    public Insets2D getInsets();

    public void setInsets(Insets2D var1);

    public Rectangle2D getBounds();

    public void setBounds(Rectangle2D var1);

    public Layout getLayout();

    public void layout();

    public void setLayout(Layout var1);

    public void add(Drawable var1);

    public void add(Drawable var1, Object var2);

    public boolean contains(Drawable var1);

    public List<Drawable> getDrawablesAt(Point2D var1);

    public List<Drawable> getDrawables();

    public Object getConstraints(Drawable var1);

    public void remove(Drawable var1);

    public int size();
}

