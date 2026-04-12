/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style.markers;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import org.knowm.xchart.style.markers.Marker;

public class TriangleDown
extends Marker {
    @Override
    public void paint(Graphics2D g, double xOffset, double yOffset, int markerSize) {
        g.setStroke(this.stroke);
        double halfSize = (double)markerSize / 2.0;
        Path2D.Double path = new Path2D.Double();
        path.moveTo(xOffset - halfSize, 1.0 + yOffset - halfSize);
        path.lineTo(xOffset, 1.0 + yOffset - halfSize + (double)markerSize);
        path.lineTo(xOffset - halfSize + (double)markerSize, 1.0 + yOffset - halfSize);
        path.closePath();
        g.fill(path);
    }
}

