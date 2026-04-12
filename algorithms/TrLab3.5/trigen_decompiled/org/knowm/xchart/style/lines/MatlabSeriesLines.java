/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style.lines;

import java.awt.BasicStroke;
import org.knowm.xchart.style.lines.SeriesLines;

public class MatlabSeriesLines
implements SeriesLines {
    private final BasicStroke[] seriesLines = new BasicStroke[]{SOLID, DASH_DASH, DOT_DOT};

    @Override
    public BasicStroke[] getSeriesLines() {
        return this.seriesLines;
    }
}

