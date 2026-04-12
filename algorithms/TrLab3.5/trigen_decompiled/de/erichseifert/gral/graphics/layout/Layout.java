/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.graphics.layout;

import de.erichseifert.gral.graphics.Container;
import java.awt.geom.Dimension2D;
import java.io.Serializable;

public interface Layout
extends Serializable {
    public double getGapX();

    public void setGapX(double var1);

    public double getGapY();

    public void setGapY(double var1);

    public void layout(Container var1);

    public Dimension2D getPreferredSize(Container var1);
}

