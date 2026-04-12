/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.style;

import java.awt.BasicStroke;
import java.awt.Color;
import org.knowm.xchart.style.markers.Marker;

public final class SeriesColorMarkerLineStyle {
    private final Color color;
    private final Marker marker;
    private final BasicStroke stroke;

    public SeriesColorMarkerLineStyle(Color color, Marker marker, BasicStroke stroke) {
        this.color = color;
        this.marker = marker;
        this.stroke = stroke;
    }

    public Color getColor() {
        return this.color;
    }

    public Marker getMarker() {
        return this.marker;
    }

    public BasicStroke getStroke() {
        return this.stroke;
    }
}

