/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.colors;

import java.awt.Paint;

public interface ColorMapper {
    public Paint get(Number var1);

    public Mode getMode();

    public static enum Mode {
        OMIT,
        REPEAT,
        CIRCULAR;

    }
}

