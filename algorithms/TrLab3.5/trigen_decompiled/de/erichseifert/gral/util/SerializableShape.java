/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.util;

import de.erichseifert.gral.util.GeometryUtils;
import de.erichseifert.gral.util.SerializationWrapper;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.List;

public class SerializableShape
implements SerializationWrapper<Shape> {
    private static final long serialVersionUID = -8849270838795846599L;
    private final List<GeometryUtils.PathSegment> a;
    private final boolean b;

    public SerializableShape(Shape shape) {
        this.a = GeometryUtils.getSegments(shape);
        this.b = !(shape instanceof Path2D.Float);
    }

    @Override
    public Shape unwrap() {
        return GeometryUtils.getShape(this.a, this.b);
    }
}

