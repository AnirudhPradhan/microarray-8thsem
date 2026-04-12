/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style.colors;

import java.awt.Color;
import org.knowm.xchart.style.colors.SeriesColors;

public class PrinterFriendlySeriesColors
implements SeriesColors {
    public static Color RED = new Color(228, 26, 28, 180);
    public static Color GREEN = new Color(55, 126, 184, 180);
    public static Color BLUE = new Color(77, 175, 74, 180);
    public static Color PURPLE = new Color(152, 78, 163, 180);
    public static Color ORANGE = new Color(255, 127, 0, 180);
    private final Color[] seriesColors = new Color[]{RED, GREEN, BLUE, PURPLE, ORANGE};

    @Override
    public Color[] getSeriesColors() {
        return this.seriesColors;
    }
}

