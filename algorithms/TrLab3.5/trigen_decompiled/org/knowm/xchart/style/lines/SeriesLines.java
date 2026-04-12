/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style.lines;

import java.awt.BasicStroke;
import org.knowm.xchart.style.lines.NoneStroke;

public interface SeriesLines {
    public static final BasicStroke NONE = new NoneStroke();
    public static final BasicStroke SOLID = new BasicStroke(2.0f, 1, 0);
    public static final BasicStroke DASH_DOT = new BasicStroke(2.0f, 0, 0, 10.0f, new float[]{3.0f, 1.0f}, 0.0f);
    public static final BasicStroke DASH_DASH = new BasicStroke(2.0f, 0, 0, 10.0f, new float[]{3.0f, 3.0f}, 0.0f);
    public static final BasicStroke DOT_DOT = new BasicStroke(2.0f, 1, 1, 10.0f, new float[]{2.0f}, 0.0f);

    public BasicStroke[] getSeriesLines();
}

