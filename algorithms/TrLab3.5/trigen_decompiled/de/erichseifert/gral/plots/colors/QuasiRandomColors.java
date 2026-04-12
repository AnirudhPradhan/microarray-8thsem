/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.colors;

import de.erichseifert.gral.plots.colors.IndexedColorMapper;
import de.erichseifert.gral.util.HaltonSequence;
import de.erichseifert.gral.util.MathUtils;
import java.awt.Color;
import java.awt.Paint;
import java.util.HashMap;
import java.util.Map;

public class QuasiRandomColors
extends IndexedColorMapper {
    private static final long serialVersionUID = 3320256963368776894L;
    private final HaltonSequence a = new HaltonSequence(3);
    private final HaltonSequence b = new HaltonSequence(5);
    private final HaltonSequence c = new HaltonSequence(2);
    private final Map<Integer, Color> d = new HashMap<Integer, Color>();
    private float[] e = new float[]{0.0f, 1.0f, 0.75f, 0.25f, 0.25f, 0.75f};

    @Override
    public Paint get(int n) {
        Integer n2 = n;
        if (this.d.containsKey(n2)) {
            return this.d.get(n2);
        }
        float[] fArray = this.getColorVariance();
        float f = fArray[0] + fArray[1] * this.a.next().floatValue();
        float f2 = fArray[2] + fArray[3] * this.b.next().floatValue();
        float f3 = fArray[4] + fArray[5] * this.c.next().floatValue();
        Color color = Color.getHSBColor(f, MathUtils.limit(f2, 0.0f, 1.0f), MathUtils.limit(f3, 0.0f, 1.0f));
        this.d.put(n2, color);
        return color;
    }

    public float[] getColorVariance() {
        return this.e;
    }

    public void setColorVariance(float[] fArray) {
        this.e = fArray;
    }
}

