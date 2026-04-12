/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.colors;

import de.erichseifert.gral.plots.colors.ColorMapper;
import java.io.Serializable;

public abstract class AbstractColorMapper<T extends Number>
implements ColorMapper,
Serializable {
    private static final long serialVersionUID = 8456831369409589441L;
    private ColorMapper.Mode a = ColorMapper.Mode.REPEAT;

    @Override
    public ColorMapper.Mode getMode() {
        return this.a;
    }

    protected void setMode(ColorMapper.Mode mode) {
        this.a = mode;
    }

    protected abstract T applyMode(T var1, T var2, T var3);
}

