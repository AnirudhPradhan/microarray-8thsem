/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.util;

import de.erichseifert.gral.util.SerializationWrapper;
import java.awt.geom.Point2D;

public class SerializablePoint2D
implements SerializationWrapper<Point2D> {
    private static final long serialVersionUID = -8849270838795846599L;
    private final double a;
    private final double b;
    private final boolean c;

    public SerializablePoint2D(Point2D point2D) {
        this.a = point2D.getX();
        this.b = point2D.getY();
        this.c = point2D instanceof Point2D.Double;
    }

    @Override
    public Point2D unwrap() {
        if (this.c) {
            return new Point2D.Double(this.a, this.b);
        }
        return new Point2D.Float((float)this.a, (float)this.b);
    }
}

