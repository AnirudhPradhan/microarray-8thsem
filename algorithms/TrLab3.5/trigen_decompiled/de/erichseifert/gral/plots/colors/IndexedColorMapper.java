/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.colors;

import de.erichseifert.gral.plots.colors.AbstractColorMapper;
import de.erichseifert.gral.plots.colors.ColorMapper;
import de.erichseifert.gral.util.MathUtils;
import java.awt.Paint;

public abstract class IndexedColorMapper
extends AbstractColorMapper<Integer> {
    private static final long serialVersionUID = 553890535328678411L;

    public abstract Paint get(int var1);

    @Override
    public Paint get(Number number) {
        return this.get(number.intValue());
    }

    @Override
    protected Integer applyMode(Integer n, Integer n2, Integer n3) {
        if (n >= n2 && n <= n3) {
            return n;
        }
        ColorMapper.Mode mode = this.getMode();
        if (mode == ColorMapper.Mode.REPEAT) {
            return MathUtils.limit(n, n2, n3);
        }
        if (mode == ColorMapper.Mode.CIRCULAR) {
            int n4 = n3 - n2 + 1;
            int n5 = n % n4;
            if (n5 < 0) {
                n5 += n4;
            }
            return n5 + n2;
        }
        return null;
    }
}

