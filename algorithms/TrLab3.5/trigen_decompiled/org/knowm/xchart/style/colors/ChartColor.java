/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style.colors;

import java.awt.Color;

public enum ChartColor {
    BLACK(new Color(0, 0, 0)),
    DARK_GREY(new Color(130, 130, 130)),
    GREY(new Color(210, 210, 210)),
    LIGHT_GREY(new Color(230, 230, 230)),
    WHITE(new Color(255, 255, 255));

    Color color;

    public static Color getAWTColor(ChartColor chartColor) {
        return chartColor.color;
    }

    private ChartColor(Color color) {
        this.color = color;
    }
}

