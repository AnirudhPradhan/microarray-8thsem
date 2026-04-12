/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.colors;

import de.erichseifert.gral.plots.colors.ColorMapper;
import de.erichseifert.gral.plots.colors.ScaledContinuousColorMapper;
import de.erichseifert.gral.util.GraphicsUtils;
import de.erichseifert.gral.util.MathUtils;
import java.awt.Color;
import java.awt.Paint;

public class Grayscale
extends ScaledContinuousColorMapper {
    private static final long serialVersionUID = -1005712209663359529L;

    @Override
    public Paint get(double d) {
        Object object = this.scale(d);
        if (!MathUtils.isCalculatable((Number)(object = this.applyMode((Double)object, 0.0, 1.0)))) {
            return null;
        }
        double d2 = 100.0 * (Double)object;
        object = GraphicsUtils.luv2rgb(new double[]{d2, 0.0, 0.0}, null);
        return new Color((float)MathUtils.limit((double)object[0], 0.0, 1.0), (float)MathUtils.limit((double)object[1], 0.0, 1.0), (float)MathUtils.limit((double)object[2], 0.0, 1.0));
    }

    @Override
    public void setMode(ColorMapper.Mode mode) {
        super.setMode(mode);
    }
}

