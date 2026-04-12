/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style.lines;

import java.awt.BasicStroke;
import org.knowm.xchart.style.lines.SeriesLines;

public class GGPlot2SeriesLines
implements SeriesLines {
    private final BasicStroke[] seriesLines = new BasicStroke[]{SOLID, DOT_DOT, DASH_DASH};

    @Override
    public BasicStroke[] getSeriesLines() {
        return this.seriesLines;
    }
}

