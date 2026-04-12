/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.colors;

import de.erichseifert.gral.plots.colors.ColorMapper;
import de.erichseifert.gral.plots.colors.ScaledContinuousColorMapper;
import de.erichseifert.gral.util.MathUtils;
import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LinearGradient
extends ScaledContinuousColorMapper {
    private static final long serialVersionUID = 4256873420364549677L;
    private final List<Color> a = new ArrayList<Color>();

    public LinearGradient(Color color, Color ... colorArray) {
        this.a.add(color);
        this.a.addAll(Arrays.asList(colorArray));
    }

    @Override
    public Paint get(double d) {
        int n;
        Double d2 = this.scale(d);
        if (!MathUtils.isCalculatable(d2 = this.applyMode(d2, 0.0, 1.0))) {
            return null;
        }
        double d3 = d2;
        double d4 = MathUtils.limit(d3 * (double)(n = this.a.size() - 1), 0.0, (double)n);
        if (d4 == 0.0) {
            return this.a.get(0);
        }
        if (d4 == (double)n) {
            return this.a.get(n);
        }
        double d5 = d4 - (double)((int)d4);
        Color color = this.a.get((int)d4);
        if (d5 == 0.0) {
            return color;
        }
        double d6 = 1.0 - d5;
        Color color2 = this.a.get((int)d4 + 1);
        double d7 = d6 * (double)color.getRed() + d5 * (double)color2.getRed();
        double d8 = d6 * (double)color.getGreen() + d5 * (double)color2.getGreen();
        double d9 = d6 * (double)color.getBlue() + d5 * (double)color2.getBlue();
        double d10 = d6 * (double)color.getAlpha() + d5 * (double)color2.getAlpha();
        return new Color((int)Math.round(d7), (int)Math.round(d8), (int)Math.round(d9), (int)Math.round(d10));
    }

    @Override
    public void setMode(ColorMapper.Mode mode) {
        super.setMode(mode);
    }

    public List<Color> getColors() {
        return Collections.unmodifiableList(this.a);
    }
}

