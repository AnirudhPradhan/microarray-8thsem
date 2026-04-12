/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.graphics.layout;

import de.erichseifert.gral.graphics.Orientation;
import de.erichseifert.gral.graphics.layout.Layout;

public interface OrientedLayout
extends Layout {
    public Orientation getOrientation();

    public void setOrientation(Orientation var1);
}

