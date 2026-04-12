/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.style.markers;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.style.markers.Marker;

public class Square
extends Marker {
    @Override
    public void paint(Graphics2D g, double xOffset, double yOffset, int markerSize) {
        g.setStroke(this.stroke);
        double halfSize = (double)markerSize / 2.0;
        Rectangle2D.Double square = new Rectangle2D.Double(xOffset - halfSize, yOffset - halfSize, markerSize, markerSize);
        g.fill(square);
    }
}

