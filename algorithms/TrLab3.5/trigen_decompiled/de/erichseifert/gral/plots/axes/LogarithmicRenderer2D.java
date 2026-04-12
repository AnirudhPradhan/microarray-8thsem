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

public class LogarithmicRenderer2D
extends AbstractAxisRenderer2D {
    private static final long serialVersionUID = 6360029510782348529L;

    @Override
    public double worldToView(Axis axis, Number number, boolean bl) {
        LogarithmicRenderer2D.a(axis);
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
        double d4 = d > 0.0 ? Math.log10(d) : 0.0;
        double d5 = d2 > 0.0 ? Math.log10(d2) : 1.0;
        return (Math.log10(d3) - d4) * this.getShapeLength() / (d5 - d4);
    }

    @Override
    public Number viewToWorld(Axis axis, double d, boolean bl) {
        LogarithmicRenderer2D.a(axis);
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
        double d4 = d2 > 0.0 ? Math.log10(d2) : 0.0;
        double d5 = d3 > 0.0 ? Math.log10(d3) : 1.0;
        return Math.pow(10.0, d * (d5 - d4) / this.getShapeLength() + d4);
    }

    @Override
    public List<Tick> getTicks(Axis axis) {
        LogarithmicRenderer2D.a(axis);
        return super.getTicks(axis);
    }

    @Override
    protected void createTicks(List<Tick> list, Axis axis, double d, double d2, Set<Double> set, boolean n) {
        double d3;
        double d4 = n != 0 ? 1.0 : this.getTickSpacing().doubleValue();
        n = this.getMinorTicksCount();
        double d5 = d3 = n > 0 ? d4 / (double)(n + 1) : d4;
        if (d == 0.0) {
            d = 1.0;
        }
        double d6 = MathUtils.magnitude(10.0, d);
        double d7 = MathUtils.magnitude(10.0, d2);
        double d8 = MathUtils.ceil(d, d6 * d4);
        int n2 = (int)Math.floor(10.0 / d3);
        int n3 = (int)Math.floor((d8 - d) / (d6 * d3));
        int n4 = 0;
        block0: for (double d9 = d6; d9 <= d7; d9 *= 10.0) {
            double d10 = d9 * d3;
            double d11 = MathUtils.ceil(d9, d10);
            for (int i = 0; i < n2; ++i) {
                double d12 = d11 + (double)i * d10;
                if (d12 < d) continue;
                if (d12 > d2) continue block0;
                Object object = Tick.TickType.MINOR;
                if ((n4++ - n3) % (n + 1) == 0) {
                    object = Tick.TickType.MAJOR;
                }
                object = this.getTick((Tick.TickType)((Object)object), axis, d12);
                if (((Tick)object).position == null || set.contains(d12)) continue;
                list.add((Tick)object);
                set.add(d12);
            }
        }
    }

    private static void a(Axis axis) {
        if (axis.getMin().doubleValue() < 0.0 || axis.getMax().doubleValue() < 0.0) {
            throw new IllegalStateException("Axis bounds must be greater than or equal to zero for logarithmic axes.");
        }
    }
}

