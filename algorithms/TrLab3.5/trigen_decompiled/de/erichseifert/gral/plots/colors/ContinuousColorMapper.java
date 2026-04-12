/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.colors;

import de.erichseifert.gral.plots.colors.AbstractColorMapper;
import de.erichseifert.gral.plots.colors.ColorMapper;
import de.erichseifert.gral.util.MathUtils;
import java.awt.Paint;

public abstract class ContinuousColorMapper
extends AbstractColorMapper<Double> {
    private static final long serialVersionUID = 4616781244057993699L;

    public abstract Paint get(double var1);

    @Override
    public Paint get(Number number) {
        return this.get(number.doubleValue());
    }

    @Override
    protected Double applyMode(Double d, Double d2, Double d3) {
        if (d >= d2 && d <= d3) {
            return d;
        }
        ColorMapper.Mode mode = this.getMode();
        if (mode == ColorMapper.Mode.REPEAT) {
            return MathUtils.limit(d, d2, d3);
        }
        if (mode == ColorMapper.Mode.CIRCULAR) {
            double d4;
            double d5 = d3 - d2;
            double d6 = d % d5;
            if (d4 < 0.0) {
                d6 += d5;
            }
            return d6 + d2;
        }
        return null;
    }
}

