/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.layout;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import net.miginfocom.layout.BoundSize;
import net.miginfocom.layout.ConstraintParser;
import net.miginfocom.layout.DimConstraint;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.UnitValue;

public final class AC
implements Externalizable {
    private final ArrayList<DimConstraint> cList = new ArrayList(8);
    private transient int curIx = 0;

    public AC() {
        this.cList.add(new DimConstraint());
    }

    public final DimConstraint[] getConstaints() {
        return this.cList.toArray(new DimConstraint[this.cList.size()]);
    }

    public final void setConstaints(DimConstraint[] dimConstraintArray) {
        if (dimConstraintArray == null || dimConstraintArray.length < 1) {
            dimConstraintArray = new DimConstraint[]{new DimConstraint()};
        }
        this.cList.clear();
        this.cList.ensureCapacity(dimConstraintArray.length);
        for (int i = 0; i < dimConstraintArray.length; ++i) {
            this.cList.add(dimConstraintArray[i]);
        }
    }

    public int getCount() {
        return this.cList.size();
    }

    public final AC count(int n) {
        this.makeSize(n);
        return this;
    }

    public final AC noGrid() {
        return this.noGrid(this.curIx);
    }

    public final AC noGrid(int ... nArray) {
        for (int i = nArray.length - 1; i >= 0; --i) {
            int n = nArray[i];
            this.makeSize(n);
            this.cList.get(n).setNoGrid(true);
        }
        return this;
    }

    public final AC index(int n) {
        this.makeSize(n);
        this.curIx = n;
        return this;
    }

    public final AC fill() {
        return this.fill(this.curIx);
    }

    public final AC fill(int ... nArray) {
        for (int i = nArray.length - 1; i >= 0; --i) {
            int n = nArray[i];
            this.makeSize(n);
            this.cList.get(n).setFill(true);
        }
        return this;
    }

    public final AC sizeGroup() {
        return this.sizeGroup("", this.curIx);
    }

    public final AC sizeGroup(String string) {
        return this.sizeGroup(string, this.curIx);
    }

    public final AC sizeGroup(String string, int ... nArray) {
        for (int i = nArray.length - 1; i >= 0; --i) {
            int n = nArray[i];
            this.makeSize(n);
            this.cList.get(n).setSizeGroup(string);
        }
        return this;
    }

    public final AC size(String string) {
        return this.size(string, this.curIx);
    }

    public final AC size(String string, int ... nArray) {
        BoundSize boundSize = ConstraintParser.parseBoundSize(string, false, true);
        for (int i = nArray.length - 1; i >= 0; --i) {
            int n = nArray[i];
            this.makeSize(n);
            this.cList.get(n).setSize(boundSize);
        }
        return this;
    }

    public final AC gap() {
        ++this.curIx;
        return this;
    }

    public final AC gap(String string) {
        return this.gap(string, this.curIx++);
    }

    public final AC gap(String string, int ... nArray) {
        BoundSize boundSize = string != null ? ConstraintParser.parseBoundSize(string, true, true) : null;
        for (int i = nArray.length - 1; i >= 0; --i) {
            int n = nArray[i];
            this.makeSize(n);
            if (boundSize == null) continue;
            this.cList.get(n).setGapAfter(boundSize);
        }
        return this;
    }

    public final AC align(String string) {
        return this.align(string, this.curIx);
    }

    public final AC align(String string, int ... nArray) {
        UnitValue unitValue = ConstraintParser.parseAlignKeywords(string, true);
        if (unitValue == null) {
            unitValue = ConstraintParser.parseAlignKeywords(string, false);
        }
        for (int i = nArray.length - 1; i >= 0; --i) {
            int n = nArray[i];
            this.makeSize(n);
            this.cList.get(n).setAlign(unitValue);
        }
        return this;
    }

    public final AC growPrio(int n) {
        return this.growPrio(n, this.curIx);
    }

    public final AC growPrio(int n, int ... nArray) {
        for (int i = nArray.length - 1; i >= 0; --i) {
            int n2 = nArray[i];
            this.makeSize(n2);
            this.cList.get(n2).setGrowPriority(n);
        }
        return this;
    }

    public final AC grow() {
        return this.grow(1.0f, this.curIx);
    }

    public final AC grow(float f) {
        return this.grow(f, this.curIx);
    }

    public final AC grow(float f, int ... nArray) {
        Float f2 = new Float(f);
        for (int i = nArray.length - 1; i >= 0; --i) {
            int n = nArray[i];
            this.makeSize(n);
            this.cList.get(n).setGrow(f2);
        }
        return this;
    }

    public final AC shrinkPrio(int n) {
        return this.shrinkPrio(n, this.curIx);
    }

    public final AC shrinkPrio(int n, int ... nArray) {
        for (int i = nArray.length - 1; i >= 0; --i) {
            int n2 = nArray[i];
            this.makeSize(n2);
            this.cList.get(n2).setShrinkPriority(n);
        }
        return this;
    }

    public final AC shrink() {
        return this.shrink(100.0f, this.curIx);
    }

    public final AC shrink(float f) {
        return this.shrink(f, this.curIx);
    }

    public final AC shrink(float f, int ... nArray) {
        Float f2 = new Float(f);
        for (int i = nArray.length - 1; i >= 0; --i) {
            int n = nArray[i];
            this.makeSize(n);
            this.cList.get(n).setShrink(f2);
        }
        return this;
    }

    public final AC shrinkWeight(float f) {
        return this.shrink(f);
    }

    public final AC shrinkWeight(float f, int ... nArray) {
        return this.shrink(f, nArray);
    }

    private void makeSize(int n) {
        if (this.cList.size() <= n) {
            this.cList.ensureCapacity(n);
            for (int i = this.cList.size(); i <= n; ++i) {
                this.cList.add(new DimConstraint());
            }
        }
    }

    private Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(objectInput));
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        if (this.getClass() == AC.class) {
            LayoutUtil.writeAsXML(objectOutput, this);
        }
    }
}

