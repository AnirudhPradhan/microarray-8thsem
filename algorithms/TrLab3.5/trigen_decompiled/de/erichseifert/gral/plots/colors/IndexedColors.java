/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.colors;

import de.erichseifert.gral.plots.colors.ColorMapper;
import de.erichseifert.gral.plots.colors.IndexedColorMapper;
import de.erichseifert.gral.util.MathUtils;
import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IndexedColors
extends IndexedColorMapper {
    private static final long serialVersionUID = -8072979842165455075L;
    private final List<Color> a = new ArrayList<Color>();

    public IndexedColors(Color color, Color ... colorArray) {
        this.a.add(color);
        this.a.addAll(Arrays.asList(colorArray));
    }

    @Override
    public Paint get(int n) {
        Integer n2 = this.applyMode(n, 0, this.a.size() - 1);
        if (!MathUtils.isCalculatable(n2)) {
            return null;
        }
        return this.a.get(n2);
    }

    public List<Color> getColors() {
        return Collections.unmodifiableList(this.a);
    }

    @Override
    public void setMode(ColorMapper.Mode mode) {
        super.setMode(mode);
    }
}

