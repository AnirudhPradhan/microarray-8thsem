/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.points;

import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import java.util.Collections;
import java.util.List;

public class PointData {
    public final List<Axis> axes;
    public final List<? extends AxisRenderer> axisRenderers;
    public final Row row;
    public final int col;

    public PointData(List<Axis> list, List<? extends AxisRenderer> list2, Row row, int n) {
        this.axes = Collections.unmodifiableList(list);
        this.axisRenderers = Collections.unmodifiableList(list2);
        this.row = row;
        this.col = n;
    }
}

