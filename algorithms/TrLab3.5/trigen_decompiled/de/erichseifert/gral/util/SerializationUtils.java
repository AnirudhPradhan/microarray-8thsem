/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.util;

import de.erichseifert.gral.util.SerializableArea;
import de.erichseifert.gral.util.SerializableBasicStroke;
import de.erichseifert.gral.util.SerializablePoint2D;
import de.erichseifert.gral.util.SerializableShape;
import de.erichseifert.gral.util.SerializationWrapper;
import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.io.Serializable;

public abstract class SerializationUtils {
    private SerializationUtils() {
        throw new UnsupportedOperationException();
    }

    public static Serializable wrap(Object object) {
        if (object == null || object instanceof Serializable) {
            return (Serializable)object;
        }
        if (object instanceof BasicStroke) {
            object = (BasicStroke)object;
            return new SerializableBasicStroke((BasicStroke)object);
        }
        if (object instanceof Point2D.Double || object instanceof Point2D.Float) {
            object = (Point2D)object;
            return new SerializablePoint2D((Point2D)object);
        }
        if (object instanceof Area) {
            object = (Area)object;
            return new SerializableArea((Area)object);
        }
        if (object instanceof Shape) {
            object = (Shape)object;
            return new SerializableShape((Shape)object);
        }
        throw new IllegalArgumentException(String.format("Failed to make value of type %s serializable.", object.getClass().getName()));
    }

    public static Object unwrap(Serializable serializable) {
        if (serializable instanceof SerializationWrapper) {
            serializable = (SerializationWrapper)serializable;
            return serializable.unwrap();
        }
        return serializable;
    }
}

