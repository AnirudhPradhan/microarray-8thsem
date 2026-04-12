/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots;

import de.erichseifert.gral.plots.points.PointData;
import de.erichseifert.gral.util.PointND;

public class DataPoint {
    public final PointData data;
    public final PointND<Double> position;

    public DataPoint(PointData pointData, PointND<Double> pointND) {
        this.data = pointData;
        this.position = pointND;
    }
}

