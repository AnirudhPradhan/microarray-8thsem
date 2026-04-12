/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.legends;

import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.graphics.Drawable;

public interface LegendSymbolRenderer {
    public Drawable getSymbol(Row var1);
}

