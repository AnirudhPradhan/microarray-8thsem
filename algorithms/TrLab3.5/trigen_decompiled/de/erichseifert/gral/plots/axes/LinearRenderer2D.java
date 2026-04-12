/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.axes;

import de.erichseifert.gral.plots.axes.AbstractAxisRenderer2D;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.axes.Tick;
import de.erichseifert.gral.util.MathUtils;
import java.util.List;
import java.util.Set;

public class LinearRenderer2D
extends AbstractAxisRenderer2D {
    private static final long serialVersionUID = -1257582880797196423L;

    @Override
    public double worldToView(Axis axis, Number number, boolean bl) {
        double d = axis.getMin().doubleValue();
        double d2 = axis.getMax().doubleValue();
        double d3 = number.doubleValue();
        if (!bl) {
            if (d3 <= d) {
                return 0.0;
            }
            if (d3 >= d2) {
                return this.getShapeLength();
            }
        }
        return (d3 - d) / (d2 - d) * this.getShapeLength();
    }

    @Override
    public Number viewToWorld(Axis axis, double d, boolean bl) {
        double d2 = axis.getMin().doubleValue();
        double d3 = axis.getMax().doubleValue();
        if (!bl) {
            if (d <= 0.0) {
                return d2;
            }
            if (d >= this.getShapeLength()) {
                return d3;
            }
        }
        return d / this.getShapeLength() * (d3 - d2) + d2;
    }

    @Override
    protected void createTicks(List<Tick> list, Axis axis, double d, double d2, Set<Double> set, boolean bl) {
        double d3;
        double d4;
        int n = 3;
        if (bl) {
            d4 = d2 - d;
            d3 = MathUtils.magnitude(10.0, d4 / 4.0);
            if (d4 / d3 > 8.0) {
                d3 *= 2.0;
                n = 1;
            }
            if (d4 / d3 > 8.0) {
                d3 *= 2.5;
                n = 4;
            }
        } else {
            d3 = this.getTickSpacing().doubleValue();
            n = this.getMinorTicksCount();
        }
        d4 = d3;
        if (n > 0) {
            d4 = d3 / (double)(n + 1);
        }
        double d5 = MathUtils.ceil(d, d3);
        double d6 = MathUtils.ceil(d, d4);
        int n2 = (int)Math.ceil((d2 - d) / d4);
        int n3 = (int)((d5 - d) / d4);
        for (int i = 0; i < n2; ++i) {
            double d7 = d6 + (double)i * d4;
            if (set.contains(d7)) continue;
            Object object = Tick.TickType.MINOR;
            if ((i - n3) % (n + 1) == 0) {
                object = Tick.TickType.MAJOR;
            }
            object = this.getTick((Tick.TickType)((Object)object), axis, d7);
            if (((Tick)object).position == null) continue;
            list.add((Tick)object);
            set.add(d7);
        }
    }
}

