/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.axes;

import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.plots.DataPoint;
import de.erichseifert.gral.util.PointND;
import java.awt.Shape;

public class Tick
extends DataPoint {
    public final TickType type;
    public final PointND<Double> normal;
    public final Drawable drawable;
    public final Shape shape;
    public final String label;

    public Tick(TickType tickType, PointND<Double> pointND, PointND<Double> pointND2, Drawable drawable, Shape shape, String string) {
        super(null, pointND);
        this.type = tickType;
        this.normal = pointND2;
        this.drawable = drawable;
        this.shape = shape;
        this.label = string;
    }

    public static enum TickType {
        MAJOR,
        MINOR,
        CUSTOM;

    }
}

