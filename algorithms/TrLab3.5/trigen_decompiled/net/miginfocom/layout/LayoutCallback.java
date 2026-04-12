/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.layout;

import net.miginfocom.layout.BoundSize;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.UnitValue;

public abstract class LayoutCallback {
    public UnitValue[] getPosition(ComponentWrapper componentWrapper) {
        return null;
    }

    public BoundSize[] getSize(ComponentWrapper componentWrapper) {
        return null;
    }

    public void correctBounds(ComponentWrapper componentWrapper) {
    }
}

