/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.colors;

import de.erichseifert.gral.plots.colors.ColorMapper;
import de.erichseifert.gral.plots.colors.ScaledContinuousColorMapper;
import de.erichseifert.gral.util.MathUtils;
import java.awt.Color;
import java.awt.Paint;

public class RainbowColors
extends ScaledContinuousColorMapper {
    private static final long serialVersionUID = 6096507341747323265L;

    @Override
    public Paint get(double d) {
        Double d2 = this.scale(d);
        if (!MathUtils.isCalculatable(d2 = this.applyMode(d2, 0.0, 1.0))) {
            return null;
        }
        float f = d2.floatValue();
        return Color.getHSBColor(f, 1.0f, 1.0f);
    }

    @Override
    public void setMode(ColorMapper.Mode mode) {
        super.setMode(mode);
    }
}

