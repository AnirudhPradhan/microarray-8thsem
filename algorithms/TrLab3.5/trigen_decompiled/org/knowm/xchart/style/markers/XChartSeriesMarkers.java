/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style.markers;

import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class XChartSeriesMarkers
implements SeriesMarkers {
    private final Marker[] seriesMarkers = new Marker[]{CIRCLE, DIAMOND, SQUARE, TRIANGLE_DOWN, TRIANGLE_UP};

    @Override
    public Marker[] getSeriesMarkers() {
        return this.seriesMarkers;
    }
}

