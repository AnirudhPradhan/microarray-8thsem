/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public interface ChartPart {
    public Rectangle2D getBounds();

    public void paint(Graphics2D var1);
}

