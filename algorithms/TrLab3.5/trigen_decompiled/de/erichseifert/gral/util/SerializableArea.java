/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.util;

import de.erichseifert.gral.util.SerializableShape;
import de.erichseifert.gral.util.SerializationWrapper;
import java.awt.geom.Area;

public class SerializableArea
implements SerializationWrapper<Area> {
    private static final long serialVersionUID = -2861579645195882742L;
    private final SerializableShape a;

    public SerializableArea(Area area) {
        this.a = new SerializableShape(area);
    }

    @Override
    public Area unwrap() {
        return new Area(this.a.unwrap());
    }
}

