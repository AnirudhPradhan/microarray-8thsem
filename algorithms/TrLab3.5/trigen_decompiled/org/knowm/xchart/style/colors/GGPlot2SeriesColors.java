/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style.colors;

import java.awt.Color;
import org.knowm.xchart.style.colors.SeriesColors;

public class GGPlot2SeriesColors
implements SeriesColors {
    public static Color RED = new Color(248, 118, 109, 255);
    public static Color YELLOW_GREEN = new Color(163, 165, 0, 255);
    public static Color GREEN = new Color(0, 191, 125, 255);
    public static Color BLUE = new Color(0, 176, 246, 255);
    public static Color PURPLE = new Color(231, 107, 243, 255);
    private final Color[] seriesColors = new Color[]{RED, YELLOW_GREEN, GREEN, BLUE, PURPLE};

    @Override
    public Color[] getSeriesColors() {
        return this.seriesColors;
    }
}

