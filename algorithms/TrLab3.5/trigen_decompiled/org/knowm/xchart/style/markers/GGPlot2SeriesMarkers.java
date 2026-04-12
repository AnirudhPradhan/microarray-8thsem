/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style.markers;

import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class GGPlot2SeriesMarkers
implements SeriesMarkers {
    private final Marker[] seriesMarkers = new Marker[]{CIRCLE, DIAMOND};

    @Override
    public Marker[] getSeriesMarkers() {
        return this.seriesMarkers;
    }
}

