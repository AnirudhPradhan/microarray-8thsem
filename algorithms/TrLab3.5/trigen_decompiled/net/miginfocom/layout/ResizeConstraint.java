/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.layout;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import net.miginfocom.layout.LayoutUtil;

final class ResizeConstraint
implements Externalizable {
    static final Float WEIGHT_100 = new Float(100.0f);
    Float grow = null;
    int growPrio = 100;
    Float shrink = WEIGHT_100;
    int shrinkPrio = 100;

    public ResizeConstraint() {
    }

    ResizeConstraint(int n, Float f, int n2, Float f2) {
        this.shrinkPrio = n;
        this.shrink = f;
        this.growPrio = n2;
        this.grow = f2;
    }

    private Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(objectInput));
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        if (this.getClass() == ResizeConstraint.class) {
            LayoutUtil.writeAsXML(objectOutput, this);
        }
    }
}

