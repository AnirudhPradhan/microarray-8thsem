/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.axes;

import de.erichseifert.gral.plots.axes.AxisListener;
import de.erichseifert.gral.util.MathUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Axis
implements Serializable {
    private static final long serialVersionUID = 5355772833362614591L;
    private transient Set<AxisListener> a = new HashSet<AxisListener>();
    private Number b;
    private Number c;
    private boolean d;

    private Axis(boolean bl) {
        this.d = bl;
    }

    public Axis() {
        this(true);
    }

    public Axis(Number number, Number number2) {
        this(false);
        this.b = number;
        this.c = number2;
    }

    public void addAxisListener(AxisListener axisListener) {
        this.a.add(axisListener);
    }

    public void removeAxisListener(AxisListener axisListener) {
        this.a.remove(axisListener);
    }

    public Number getMin() {
        return this.b;
    }

    public void setMin(Number number) {
        this.setRange(number, this.getMax());
    }

    public Number getMax() {
        return this.c;
    }

    public void setMax(Number number) {
        this.setRange(this.getMin(), number);
    }

    public double getRange() {
        return this.getMax().doubleValue() - this.getMin().doubleValue();
    }

    public void setRange(Number serializable, Number number) {
        if (this.getMin() != null && this.getMin().equals(serializable) && this.getMax() != null && this.getMax().equals(number)) {
            return;
        }
        this.b = serializable;
        this.c = number;
        Number number2 = number;
        number = serializable;
        serializable = this;
        for (AxisListener axisListener : ((Axis)serializable).a) {
            axisListener.rangeChanged((Axis)serializable, number, number2);
        }
    }

    public Number getPosition(Number number) {
        if (number == null) {
            return null;
        }
        double d = (number.doubleValue() - this.getMin().doubleValue()) / this.getRange();
        return d;
    }

    public boolean isAutoscaled() {
        return this.d;
    }

    public void setAutoscaled(boolean bl) {
        if (this.d != bl) {
            this.d = bl;
        }
    }

    public boolean isValid() {
        return MathUtils.isCalculatable(this.b) && MathUtils.isCalculatable(this.c);
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.a = new HashSet<AxisListener>();
    }
}

