/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.style;

import java.awt.BasicStroke;
import java.awt.Color;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.style.markers.Marker;

public class SeriesColorMarkerLineStyleCycler {
    private final Color[] seriesColorList;
    private final Marker[] seriesMarkerList;
    private final BasicStroke[] seriesLineStyleList;
    private int colorCounter = 0;
    private int markerCounter = 0;
    private int strokeCounter = 0;

    public SeriesColorMarkerLineStyleCycler(Color[] seriesColorList, Marker[] seriesMarkerList, BasicStroke[] seriesLineStyleList) {
        this.seriesColorList = seriesColorList;
        this.seriesMarkerList = seriesMarkerList;
        this.seriesLineStyleList = seriesLineStyleList;
    }

    public SeriesColorMarkerLineStyle getNextSeriesColorMarkerLineStyle() {
        if (this.colorCounter >= this.seriesColorList.length) {
            this.colorCounter = 0;
            ++this.strokeCounter;
        }
        Color seriesColor = this.seriesColorList[this.colorCounter++];
        if (this.strokeCounter >= this.seriesLineStyleList.length) {
            this.strokeCounter = 0;
        }
        BasicStroke seriesLineStyle = this.seriesLineStyleList[this.strokeCounter];
        if (this.markerCounter >= this.seriesMarkerList.length) {
            this.markerCounter = 0;
        }
        Marker marker = this.seriesMarkerList[this.markerCounter++];
        return new SeriesColorMarkerLineStyle(seriesColor, marker, seriesLineStyle);
    }
}

