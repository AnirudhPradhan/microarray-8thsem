/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style.markers;

import org.knowm.xchart.style.markers.Circle;
import org.knowm.xchart.style.markers.Diamond;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.None;
import org.knowm.xchart.style.markers.Square;
import org.knowm.xchart.style.markers.TriangleDown;
import org.knowm.xchart.style.markers.TriangleUp;

public interface SeriesMarkers {
    public static final Marker NONE = new None();
    public static final Marker CIRCLE = new Circle();
    public static final Marker DIAMOND = new Diamond();
    public static final Marker SQUARE = new Square();
    public static final Marker TRIANGLE_DOWN = new TriangleDown();
    public static final Marker TRIANGLE_UP = new TriangleUp();

    public Marker[] getSeriesMarkers();
}

