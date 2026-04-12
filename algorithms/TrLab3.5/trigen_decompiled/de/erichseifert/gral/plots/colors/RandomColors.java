/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.colors;

import de.erichseifert.gral.plots.colors.IndexedColorMapper;
import de.erichseifert.gral.util.MathUtils;
import java.awt.Color;
import java.awt.Paint;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class RandomColors
extends IndexedColorMapper {
    private static final long serialVersionUID = -4518470000665474457L;
    private final Map<Integer, Color> a;
    private final Random b = new Random();
    private final float[] c;

    public RandomColors() {
        this.a = new LinkedHashMap<Integer, Color>();
        this.c = new float[]{0.0f, 1.0f, 0.75f, 0.25f, 0.25f, 0.75f};
    }

    public RandomColors(long l) {
        this();
        this.b.setSeed(l);
    }

    @Override
    public Paint get(int n) {
        Color color;
        boolean bl;
        Integer n2 = n;
        if (this.a.containsKey(n2)) {
            return this.a.get(n2);
        }
        block0: do {
            Serializable serializable = this;
            Object object = ((RandomColors)serializable).getColorVariance();
            float f = object[0] + object[1] * ((RandomColors)serializable).b.nextFloat();
            float f2 = object[2] + object[3] * ((RandomColors)serializable).b.nextFloat();
            float f3 = object[4] + object[5] * ((RandomColors)serializable).b.nextFloat();
            color = Color.getHSBColor(f, MathUtils.limit(f2, 0.0f, 1.0f), MathUtils.limit(f3, 0.0f, 1.0f));
            bl = true;
            Iterator<Color> iterator = this.a.values().iterator();
            for (int i = 0; i < 4 && iterator.hasNext(); ++i) {
                double d;
                serializable = iterator.next();
                object = serializable;
                serializable = color;
                double d2 = (double)(((Color)serializable).getRed() + ((Color)object).getRed()) / 256.0 / 2.0;
                double d3 = (double)(((Color)serializable).getRed() - ((Color)object).getRed()) / 256.0;
                double d4 = (double)(((Color)serializable).getGreen() - ((Color)object).getGreen()) / 256.0;
                double d5 = (double)(((Color)serializable).getBlue() - ((Color)object).getBlue()) / 256.0;
                double d6 = (d2 + 2.0) * d3 * d3 + d4 * 4.0 * d4 + (3.0 - d2) * d5 * d5;
                if (!(d / 9.0 < 0.09)) continue;
                bl = false;
                continue block0;
            }
        } while (!bl);
        this.a.put(n2, color);
        return color;
    }

    public float[] getColorVariance() {
        return this.c;
    }

    public void setColorVariance(float[] fArray) {
        System.arraycopy(fArray, 0, this.c, 0, 6);
    }
}

