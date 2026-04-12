/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.graphics.layout;

import de.erichseifert.gral.graphics.Orientation;
import de.erichseifert.gral.graphics.layout.AbstractLayout;
import de.erichseifert.gral.graphics.layout.OrientedLayout;

public abstract class AbstractOrientedLayout
extends AbstractLayout
implements OrientedLayout {
    private Orientation a;

    public AbstractOrientedLayout(Orientation orientation, double d, double d2) {
        super(d, d2);
        this.a = orientation;
    }

    @Override
    public Orientation getOrientation() {
        return this.a;
    }

    @Override
    public void setOrientation(Orientation orientation) {
        this.a = orientation;
    }
}

