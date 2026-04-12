/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.colors;

import de.erichseifert.gral.plots.colors.ColorMapper;
import de.erichseifert.gral.plots.colors.ScaledContinuousColorMapper;
import de.erichseifert.gral.util.MathUtils;
import java.awt.Color;
import java.awt.Paint;

public class HeatMap
extends ScaledContinuousColorMapper {
    private static final long serialVersionUID = -5398973874608239179L;
    private static final Color[] a = new Color[]{new Color(0.0f, 0.0f, 0.0f), new Color(0.0f, 0.0f, 1.0f), new Color(1.0f, 0.0f, 0.0f), new Color(1.0f, 1.0f, 0.0f), new Color(1.0f, 1.0f, 1.0f)};

    @Override
    public Paint get(double d) {
        Object object = this.scale(d);
        if (!MathUtils.isCalculatable((Number)(object = this.applyMode((Double)object, 0.0, 1.0)))) {
            return null;
        }
        double d2 = (Double)object;
        double d3 = 1.0 - d2;
        double d4 = d3 * d3;
        double d5 = d2 * d2;
        object = new double[]{d4 * d4, d2 * 4.0 * d4 * d3, d5 * 6.0 * d4, d2 * 4.0 * d5 * d3, d5 * d5};
        double d6 = 0.0;
        double d7 = 0.0;
        double d8 = 0.0;
        double d9 = 0.0;
        for (int i = 0; i < 5; ++i) {
            d6 += object[i] * (double)a[i].getRed();
            d7 += object[i] * (double)a[i].getGreen();
            d8 += object[i] * (double)a[i].getBlue();
            d9 += object[i] * (double)a[i].getAlpha();
        }
        return new Color((float)MathUtils.limit(d6, 0.0, 255.0) / 255.0f, (float)MathUtils.limit(d7, 0.0, 255.0) / 255.0f, (float)MathUtils.limit(d8, 0.0, 255.0) / 255.0f, (float)MathUtils.limit(d9, 0.0, 255.0) / 255.0f);
    }

    @Override
    public void setMode(ColorMapper.Mode mode) {
        super.setMode(mode);
    }
}

