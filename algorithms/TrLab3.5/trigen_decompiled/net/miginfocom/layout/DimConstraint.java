/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.layout;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import net.miginfocom.layout.BoundSize;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.layout.ResizeConstraint;
import net.miginfocom.layout.UnitValue;

public final class DimConstraint
implements Externalizable {
    final ResizeConstraint resize = new ResizeConstraint();
    private String sizeGroup = null;
    private BoundSize size = BoundSize.NULL_SIZE;
    private BoundSize gapBefore = null;
    private BoundSize gapAfter = null;
    private UnitValue align = null;
    private String endGroup = null;
    private boolean fill = false;
    private boolean noGrid = false;

    public int getGrowPriority() {
        return this.resize.growPrio;
    }

    public void setGrowPriority(int n) {
        this.resize.growPrio = n;
    }

    public Float getGrow() {
        return this.resize.grow;
    }

    public void setGrow(Float f) {
        this.resize.grow = f;
    }

    public int getShrinkPriority() {
        return this.resize.shrinkPrio;
    }

    public void setShrinkPriority(int n) {
        this.resize.shrinkPrio = n;
    }

    public Float getShrink() {
        return this.resize.shrink;
    }

    public void setShrink(Float f) {
        this.resize.shrink = f;
    }

    public UnitValue getAlignOrDefault(boolean bl) {
        if (this.align != null) {
            return this.align;
        }
        if (bl) {
            return UnitValue.LEADING;
        }
        return this.fill || !PlatformDefaults.getDefaultRowAlignmentBaseline() ? UnitValue.CENTER : UnitValue.BASELINE_IDENTITY;
    }

    public UnitValue getAlign() {
        return this.align;
    }

    public void setAlign(UnitValue unitValue) {
        this.align = unitValue;
    }

    public BoundSize getGapAfter() {
        return this.gapAfter;
    }

    public void setGapAfter(BoundSize boundSize) {
        this.gapAfter = boundSize;
    }

    boolean hasGapAfter() {
        return this.gapAfter != null && !this.gapAfter.isUnset();
    }

    boolean isGapAfterPush() {
        return this.gapAfter != null && this.gapAfter.getGapPush();
    }

    public BoundSize getGapBefore() {
        return this.gapBefore;
    }

    public void setGapBefore(BoundSize boundSize) {
        this.gapBefore = boundSize;
    }

    boolean hasGapBefore() {
        return this.gapBefore != null && !this.gapBefore.isUnset();
    }

    boolean isGapBeforePush() {
        return this.gapBefore != null && this.gapBefore.getGapPush();
    }

    public BoundSize getSize() {
        return this.size;
    }

    public void setSize(BoundSize boundSize) {
        if (boundSize != null) {
            boundSize.checkNotLinked();
        }
        this.size = boundSize;
    }

    public String getSizeGroup() {
        return this.sizeGroup;
    }

    public void setSizeGroup(String string) {
        this.sizeGroup = string;
    }

    public String getEndGroup() {
        return this.endGroup;
    }

    public void setEndGroup(String string) {
        this.endGroup = string;
    }

    public boolean isFill() {
        return this.fill;
    }

    public void setFill(boolean bl) {
        this.fill = bl;
    }

    public boolean isNoGrid() {
        return this.noGrid;
    }

    public void setNoGrid(boolean bl) {
        this.noGrid = bl;
    }

    int[] getRowGaps(ContainerWrapper containerWrapper, BoundSize boundSize, int n, boolean bl) {
        BoundSize boundSize2;
        BoundSize boundSize3 = boundSize2 = bl ? this.gapBefore : this.gapAfter;
        if (boundSize2 == null || boundSize2.isUnset()) {
            boundSize2 = boundSize;
        }
        if (boundSize2 == null || boundSize2.isUnset()) {
            return null;
        }
        int[] nArray = new int[3];
        for (int i = 0; i <= 2; ++i) {
            UnitValue unitValue = boundSize2.getSize(i);
            nArray[i] = unitValue != null ? unitValue.getPixels(n, containerWrapper, null) : -2147471302;
        }
        return nArray;
    }

    int[] getComponentGaps(ContainerWrapper containerWrapper, ComponentWrapper componentWrapper, BoundSize boundSize, ComponentWrapper componentWrapper2, String string, int n, int n2, boolean bl) {
        boolean bl2;
        BoundSize boundSize2 = n2 < 2 ? this.gapBefore : this.gapAfter;
        boolean bl3 = bl2 = boundSize2 != null && boundSize2.getGapPush();
        if ((boundSize2 == null || boundSize2.isUnset()) && (boundSize == null || boundSize.isUnset()) && componentWrapper != null) {
            boundSize2 = PlatformDefaults.getDefaultComponentGap(componentWrapper, componentWrapper2, n2 + 1, string, bl);
        }
        if (boundSize2 == null) {
            int[] nArray;
            if (bl2) {
                int[] nArray2 = new int[3];
                nArray2[0] = 0;
                nArray2[1] = 0;
                nArray = nArray2;
                nArray2[2] = -2147471302;
            } else {
                nArray = null;
            }
            return nArray;
        }
        int[] nArray = new int[3];
        for (int i = 0; i <= 2; ++i) {
            UnitValue unitValue = boundSize2.getSize(i);
            nArray[i] = unitValue != null ? unitValue.getPixels(n, containerWrapper, null) : -2147471302;
        }
        return nArray;
    }

    private Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(objectInput));
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        if (this.getClass() == DimConstraint.class) {
            LayoutUtil.writeAsXML(objectOutput, this);
        }
    }
}

