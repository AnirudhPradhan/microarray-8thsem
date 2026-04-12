/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.layout;

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.UnitValue;

public class BoundSize
implements Serializable {
    public static final BoundSize NULL_SIZE = new BoundSize(null, null);
    public static final BoundSize ZERO_PIXEL = new BoundSize(UnitValue.ZERO, "0px");
    private final transient UnitValue min;
    private final transient UnitValue pref;
    private final transient UnitValue max;
    private final transient boolean gapPush;
    private static final long serialVersionUID = 1L;

    public BoundSize(UnitValue unitValue, String string) {
        this(unitValue, unitValue, unitValue, string);
    }

    public BoundSize(UnitValue unitValue, UnitValue unitValue2, UnitValue unitValue3, String string) {
        this(unitValue, unitValue2, unitValue3, false, string);
    }

    public BoundSize(UnitValue unitValue, UnitValue unitValue2, UnitValue unitValue3, boolean bl, String string) {
        this.min = unitValue;
        this.pref = unitValue2;
        this.max = unitValue3;
        this.gapPush = bl;
        LayoutUtil.putCCString(this, string);
    }

    public final UnitValue getMin() {
        return this.min;
    }

    public final UnitValue getPreferred() {
        return this.pref;
    }

    public final UnitValue getMax() {
        return this.max;
    }

    public boolean getGapPush() {
        return this.gapPush;
    }

    public boolean isUnset() {
        return this == ZERO_PIXEL || this.pref == null && this.min == null && this.max == null && !this.gapPush;
    }

    public int constrain(int n, float f, ContainerWrapper containerWrapper) {
        if (this.max != null) {
            n = Math.min(n, this.max.getPixels(f, containerWrapper, containerWrapper));
        }
        if (this.min != null) {
            n = Math.max(n, this.min.getPixels(f, containerWrapper, containerWrapper));
        }
        return n;
    }

    final UnitValue getSize(int n) {
        switch (n) {
            case 0: {
                return this.min;
            }
            case 1: {
                return this.pref;
            }
            case 2: {
                return this.max;
            }
        }
        throw new IllegalArgumentException("Unknown size: " + n);
    }

    final int[] getPixelSizes(float f, ContainerWrapper containerWrapper, ComponentWrapper componentWrapper) {
        return new int[]{this.min != null ? this.min.getPixels(f, containerWrapper, componentWrapper) : 0, this.pref != null ? this.pref.getPixels(f, containerWrapper, componentWrapper) : 0, this.max != null ? this.max.getPixels(f, containerWrapper, componentWrapper) : 2097051};
    }

    String getConstraintString() {
        String string = LayoutUtil.getCCString(this);
        if (string != null) {
            return string;
        }
        if (this.min == this.pref && this.pref == this.max) {
            return this.min != null ? this.min.getConstraintString() + "!" : "null";
        }
        StringBuilder stringBuilder = new StringBuilder(16);
        if (this.min != null) {
            stringBuilder.append(this.min.getConstraintString()).append(':');
        }
        if (this.pref != null) {
            if (this.min == null && this.max != null) {
                stringBuilder.append(":");
            }
            stringBuilder.append(this.pref.getConstraintString());
        } else if (this.min != null) {
            stringBuilder.append('n');
        }
        if (this.max != null) {
            stringBuilder.append(stringBuilder.length() == 0 ? "::" : ":").append(this.max.getConstraintString());
        }
        if (this.gapPush) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(':');
            }
            stringBuilder.append("push");
        }
        return stringBuilder.toString();
    }

    void checkNotLinked() {
        if (this.min != null && this.min.isLinkedDeep() || this.pref != null && this.pref.isLinkedDeep() || this.max != null && this.max.isLinkedDeep()) {
            throw new IllegalArgumentException("Size may not contain links");
        }
    }

    protected Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.getClass() == BoundSize.class) {
            LayoutUtil.writeAsXML(objectOutputStream, this);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(objectInputStream));
    }

    static {
        LayoutUtil.setDelegate(BoundSize.class, new PersistenceDelegate(){

            protected Expression instantiate(Object object, Encoder encoder) {
                BoundSize boundSize = (BoundSize)object;
                return new Expression(object, BoundSize.class, "new", new Object[]{boundSize.getMin(), boundSize.getPreferred(), boundSize.getMax(), boundSize.getGapPush(), boundSize.getConstraintString()});
            }
        });
    }
}

