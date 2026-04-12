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
import net.miginfocom.layout.ResizeConstraint;
import net.miginfocom.layout.UnitValue;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class CC
implements Externalizable {
    private static final BoundSize DEF_GAP = BoundSize.NULL_SIZE;
    static final String[] DOCK_SIDES = new String[]{"north", "west", "south", "east"};
    private int dock = -1;
    private UnitValue[] pos = null;
    private UnitValue[] padding = null;
    private Boolean flowX = null;
    private int skip = 0;
    private int split = 1;
    private int spanX = 1;
    private int spanY = 1;
    private int cellX = -1;
    private int cellY = 0;
    private String tag = null;
    private String id = null;
    private int hideMode = -1;
    private DimConstraint hor = new DimConstraint();
    private DimConstraint ver = new DimConstraint();
    private BoundSize newline = null;
    private BoundSize wrap = null;
    private boolean boundsInGrid = true;
    private boolean external = false;
    private Float pushX = null;
    private Float pushY = null;
    private static final String[] EMPTY_ARR = new String[0];
    private transient String[] linkTargets = null;

    String[] getLinkTargets() {
        if (this.linkTargets == null) {
            ArrayList<String> arrayList = new ArrayList<String>(2);
            if (this.pos != null) {
                for (int i = 0; i < this.pos.length; ++i) {
                    this.addLinkTargetIDs(arrayList, this.pos[i]);
                }
            }
            this.linkTargets = arrayList.size() == 0 ? EMPTY_ARR : arrayList.toArray(new String[arrayList.size()]);
        }
        return this.linkTargets;
    }

    private void addLinkTargetIDs(ArrayList<String> arrayList, UnitValue unitValue) {
        if (unitValue != null) {
            String string = unitValue.getLinkTargetId();
            if (string != null) {
                arrayList.add(string);
            } else {
                for (int i = unitValue.getSubUnitCount() - 1; i >= 0; --i) {
                    UnitValue unitValue2 = unitValue.getSubUnitValue(i);
                    if (!unitValue2.isLinkedDeep()) continue;
                    this.addLinkTargetIDs(arrayList, unitValue2);
                }
            }
        }
    }

    public final CC endGroupX(String string) {
        this.hor.setEndGroup(string);
        return this;
    }

    public final CC sizeGroupX(String string) {
        this.hor.setSizeGroup(string);
        return this;
    }

    public final CC minWidth(String string) {
        this.hor.setSize(LayoutUtil.derive(this.hor.getSize(), ConstraintParser.parseUnitValue(string, true), null, null));
        return this;
    }

    public final CC width(String string) {
        this.hor.setSize(ConstraintParser.parseBoundSize(string, false, true));
        return this;
    }

    public final CC maxWidth(String string) {
        this.hor.setSize(LayoutUtil.derive(this.hor.getSize(), null, null, ConstraintParser.parseUnitValue(string, true)));
        return this;
    }

    public final CC gapX(String string, String string2) {
        if (string != null) {
            this.hor.setGapBefore(ConstraintParser.parseBoundSize(string, true, true));
        }
        if (string2 != null) {
            this.hor.setGapAfter(ConstraintParser.parseBoundSize(string2, true, true));
        }
        return this;
    }

    public final CC alignX(String string) {
        this.hor.setAlign(ConstraintParser.parseUnitValueOrAlign(string, true, null));
        return this;
    }

    public final CC growPrioX(int n) {
        this.hor.setGrowPriority(n);
        return this;
    }

    public final CC growPrio(int ... nArray) {
        switch (nArray.length) {
            default: {
                throw new IllegalArgumentException("Illegal argument count: " + nArray.length);
            }
            case 2: {
                this.growPrioY(nArray[1]);
            }
            case 1: 
        }
        this.growPrioX(nArray[0]);
        return this;
    }

    public final CC growX() {
        this.hor.setGrow(ResizeConstraint.WEIGHT_100);
        return this;
    }

    public final CC growX(float f) {
        this.hor.setGrow(new Float(f));
        return this;
    }

    public final CC grow(float ... fArray) {
        switch (fArray.length) {
            default: {
                throw new IllegalArgumentException("Illegal argument count: " + fArray.length);
            }
            case 2: {
                this.growY(Float.valueOf(fArray[1]));
            }
            case 1: 
        }
        this.growX(fArray[0]);
        return this;
    }

    public final CC shrinkPrioX(int n) {
        this.hor.setShrinkPriority(n);
        return this;
    }

    public final CC shrinkPrio(int ... nArray) {
        switch (nArray.length) {
            default: {
                throw new IllegalArgumentException("Illegal argument count: " + nArray.length);
            }
            case 2: {
                this.shrinkPrioY(nArray[1]);
            }
            case 1: 
        }
        this.shrinkPrioX(nArray[0]);
        return this;
    }

    public final CC shrinkX(float f) {
        this.hor.setShrink(new Float(f));
        return this;
    }

    public final CC shrink(float ... fArray) {
        switch (fArray.length) {
            default: {
                throw new IllegalArgumentException("Illegal argument count: " + fArray.length);
            }
            case 2: {
                this.shrinkY(fArray[1]);
            }
            case 1: 
        }
        this.shrinkX(fArray[0]);
        return this;
    }

    public final CC endGroupY(String string) {
        this.ver.setEndGroup(string);
        return this;
    }

    public final CC endGroup(String ... stringArray) {
        switch (stringArray.length) {
            default: {
                throw new IllegalArgumentException("Illegal argument count: " + stringArray.length);
            }
            case 2: {
                this.endGroupY(stringArray[1]);
            }
            case 1: 
        }
        this.endGroupX(stringArray[0]);
        return this;
    }

    public final CC sizeGroupY(String string) {
        this.ver.setSizeGroup(string);
        return this;
    }

    public final CC sizeGroup(String ... stringArray) {
        switch (stringArray.length) {
            default: {
                throw new IllegalArgumentException("Illegal argument count: " + stringArray.length);
            }
            case 2: {
                this.sizeGroupY(stringArray[1]);
            }
            case 1: 
        }
        this.sizeGroupX(stringArray[0]);
        return this;
    }

    public final CC minHeight(String string) {
        this.ver.setSize(LayoutUtil.derive(this.ver.getSize(), ConstraintParser.parseUnitValue(string, false), null, null));
        return this;
    }

    public final CC height(String string) {
        this.ver.setSize(ConstraintParser.parseBoundSize(string, false, false));
        return this;
    }

    public final CC maxHeight(String string) {
        this.ver.setSize(LayoutUtil.derive(this.ver.getSize(), null, null, ConstraintParser.parseUnitValue(string, false)));
        return this;
    }

    public final CC gapY(String string, String string2) {
        if (string != null) {
            this.ver.setGapBefore(ConstraintParser.parseBoundSize(string, true, false));
        }
        if (string2 != null) {
            this.ver.setGapAfter(ConstraintParser.parseBoundSize(string2, true, false));
        }
        return this;
    }

    public final CC alignY(String string) {
        this.ver.setAlign(ConstraintParser.parseUnitValueOrAlign(string, false, null));
        return this;
    }

    public final CC growPrioY(int n) {
        this.ver.setGrowPriority(n);
        return this;
    }

    public final CC growY() {
        this.ver.setGrow(ResizeConstraint.WEIGHT_100);
        return this;
    }

    public final CC growY(Float f) {
        this.ver.setGrow(f);
        return this;
    }

    public final CC shrinkPrioY(int n) {
        this.ver.setShrinkPriority(n);
        return this;
    }

    public final CC shrinkY(float f) {
        this.ver.setShrink(new Float(f));
        return this;
    }

    public final CC hideMode(int n) {
        this.setHideMode(n);
        return this;
    }

    public final CC id(String string) {
        this.setId(string);
        return this;
    }

    public final CC tag(String string) {
        this.setTag(string);
        return this;
    }

    public final CC cell(int ... nArray) {
        switch (nArray.length) {
            default: {
                throw new IllegalArgumentException("Illegal argument count: " + nArray.length);
            }
            case 4: {
                this.setSpanY(nArray[3]);
            }
            case 3: {
                this.setSpanX(nArray[2]);
            }
            case 2: {
                this.setCellY(nArray[1]);
            }
            case 1: 
        }
        this.setCellX(nArray[0]);
        return this;
    }

    public final CC span(int ... nArray) {
        if (nArray == null || nArray.length == 0) {
            this.setSpanX(2097051);
            this.setSpanY(1);
        } else if (nArray.length == 1) {
            this.setSpanX(nArray[0]);
            this.setSpanY(1);
        } else {
            this.setSpanX(nArray[0]);
            this.setSpanY(nArray[1]);
        }
        return this;
    }

    public final CC gap(String ... stringArray) {
        switch (stringArray.length) {
            default: {
                throw new IllegalArgumentException("Illegal argument count: " + stringArray.length);
            }
            case 4: {
                this.gapBottom(stringArray[3]);
            }
            case 3: {
                this.gapTop(stringArray[2]);
            }
            case 2: {
                this.gapRight(stringArray[1]);
            }
            case 1: 
        }
        this.gapLeft(stringArray[0]);
        return this;
    }

    public final CC gapBefore(String string) {
        this.hor.setGapBefore(ConstraintParser.parseBoundSize(string, true, true));
        return this;
    }

    public final CC gapAfter(String string) {
        this.hor.setGapAfter(ConstraintParser.parseBoundSize(string, true, true));
        return this;
    }

    public final CC gapTop(String string) {
        this.ver.setGapBefore(ConstraintParser.parseBoundSize(string, true, false));
        return this;
    }

    public final CC gapLeft(String string) {
        this.hor.setGapBefore(ConstraintParser.parseBoundSize(string, true, true));
        return this;
    }

    public final CC gapBottom(String string) {
        this.ver.setGapAfter(ConstraintParser.parseBoundSize(string, true, false));
        return this;
    }

    public final CC gapRight(String string) {
        this.hor.setGapAfter(ConstraintParser.parseBoundSize(string, true, true));
        return this;
    }

    public final CC spanY() {
        return this.spanY(2097051);
    }

    public final CC spanY(int n) {
        this.setSpanY(n);
        return this;
    }

    public final CC spanX() {
        return this.spanX(2097051);
    }

    public final CC spanX(int n) {
        this.setSpanX(n);
        return this;
    }

    public final CC push() {
        return this.pushX().pushY();
    }

    public final CC push(Float f, Float f2) {
        return this.pushX(f).pushY(f2);
    }

    public final CC pushY() {
        return this.pushY(ResizeConstraint.WEIGHT_100);
    }

    public final CC pushY(Float f) {
        this.setPushY(f);
        return this;
    }

    public final CC pushX() {
        return this.pushX(ResizeConstraint.WEIGHT_100);
    }

    public final CC pushX(Float f) {
        this.setPushX(f);
        return this;
    }

    public final CC split(int n) {
        this.setSplit(n);
        return this;
    }

    public final CC split() {
        this.setSplit(2097051);
        return this;
    }

    public final CC skip(int n) {
        this.setSkip(n);
        return this;
    }

    public final CC skip() {
        this.setSkip(1);
        return this;
    }

    public final CC external() {
        this.setExternal(true);
        return this;
    }

    public final CC flowX() {
        this.setFlowX(Boolean.TRUE);
        return this;
    }

    public final CC flowY() {
        this.setFlowX(Boolean.FALSE);
        return this;
    }

    public final CC grow() {
        this.growX();
        this.growY();
        return this;
    }

    public final CC newline() {
        this.setNewline(true);
        return this;
    }

    public final CC newline(String string) {
        BoundSize boundSize = ConstraintParser.parseBoundSize(string, true, this.flowX != null && this.flowX == false);
        if (boundSize != null) {
            this.setNewlineGapSize(boundSize);
        } else {
            this.setNewline(true);
        }
        return this;
    }

    public final CC wrap() {
        this.setWrap(true);
        return this;
    }

    public final CC wrap(String string) {
        BoundSize boundSize = ConstraintParser.parseBoundSize(string, true, this.flowX != null && this.flowX == false);
        if (boundSize != null) {
            this.setWrapGapSize(boundSize);
        } else {
            this.setWrap(true);
        }
        return this;
    }

    public final CC dockNorth() {
        this.setDockSide(0);
        return this;
    }

    public final CC dockWest() {
        this.setDockSide(1);
        return this;
    }

    public final CC dockSouth() {
        this.setDockSide(2);
        return this;
    }

    public final CC dockEast() {
        this.setDockSide(3);
        return this;
    }

    public final CC x(String string) {
        return this.corrPos(string, 0);
    }

    public final CC y(String string) {
        return this.corrPos(string, 1);
    }

    public final CC x2(String string) {
        return this.corrPos(string, 2);
    }

    public final CC y2(String string) {
        return this.corrPos(string, 3);
    }

    private final CC corrPos(String string, int n) {
        UnitValue[] unitValueArray = this.getPos();
        if (unitValueArray == null) {
            unitValueArray = new UnitValue[4];
        }
        unitValueArray[n] = ConstraintParser.parseUnitValue(string, n % 2 == 0);
        this.setPos(unitValueArray);
        this.setBoundsInGrid(true);
        return this;
    }

    public final CC pos(String string, String string2) {
        UnitValue[] unitValueArray = this.getPos();
        if (unitValueArray == null) {
            unitValueArray = new UnitValue[4];
        }
        unitValueArray[0] = ConstraintParser.parseUnitValue(string, true);
        unitValueArray[1] = ConstraintParser.parseUnitValue(string2, false);
        this.setPos(unitValueArray);
        this.setBoundsInGrid(false);
        return this;
    }

    public final CC pos(String string, String string2, String string3, String string4) {
        this.setPos(new UnitValue[]{ConstraintParser.parseUnitValue(string, true), ConstraintParser.parseUnitValue(string2, false), ConstraintParser.parseUnitValue(string3, true), ConstraintParser.parseUnitValue(string4, false)});
        this.setBoundsInGrid(false);
        return this;
    }

    public final CC pad(int n, int n2, int n3, int n4) {
        this.setPadding(new UnitValue[]{new UnitValue(n), new UnitValue(n2), new UnitValue(n3), new UnitValue(n4)});
        return this;
    }

    public final CC pad(String string) {
        this.setPadding(string != null ? ConstraintParser.parseInsets(string, false) : null);
        return this;
    }

    public DimConstraint getHorizontal() {
        return this.hor;
    }

    public void setHorizontal(DimConstraint dimConstraint) {
        this.hor = dimConstraint != null ? dimConstraint : new DimConstraint();
    }

    public DimConstraint getVertical() {
        return this.ver;
    }

    public void setVertical(DimConstraint dimConstraint) {
        this.ver = dimConstraint != null ? dimConstraint : new DimConstraint();
    }

    public DimConstraint getDimConstraint(boolean bl) {
        return bl ? this.hor : this.ver;
    }

    public UnitValue[] getPos() {
        UnitValue[] unitValueArray;
        if (this.pos != null) {
            UnitValue[] unitValueArray2 = new UnitValue[4];
            unitValueArray2[0] = this.pos[0];
            unitValueArray2[1] = this.pos[1];
            unitValueArray2[2] = this.pos[2];
            unitValueArray = unitValueArray2;
            unitValueArray2[3] = this.pos[3];
        } else {
            unitValueArray = null;
        }
        return unitValueArray;
    }

    public void setPos(UnitValue[] unitValueArray) {
        UnitValue[] unitValueArray2;
        if (unitValueArray != null) {
            UnitValue[] unitValueArray3 = new UnitValue[4];
            unitValueArray3[0] = unitValueArray[0];
            unitValueArray3[1] = unitValueArray[1];
            unitValueArray3[2] = unitValueArray[2];
            unitValueArray2 = unitValueArray3;
            unitValueArray3[3] = unitValueArray[3];
        } else {
            unitValueArray2 = null;
        }
        this.pos = unitValueArray2;
        this.linkTargets = null;
    }

    public boolean isBoundsInGrid() {
        return this.boundsInGrid;
    }

    void setBoundsInGrid(boolean bl) {
        this.boundsInGrid = bl;
    }

    public int getCellX() {
        return this.cellX;
    }

    public void setCellX(int n) {
        this.cellX = n;
    }

    public int getCellY() {
        return this.cellX < 0 ? -1 : this.cellY;
    }

    public void setCellY(int n) {
        if (n < 0) {
            this.cellX = -1;
        }
        this.cellY = n < 0 ? 0 : n;
    }

    public int getDockSide() {
        return this.dock;
    }

    public void setDockSide(int n) {
        if (n < -1 || n > 3) {
            throw new IllegalArgumentException("Illegal dock side: " + n);
        }
        this.dock = n;
    }

    public boolean isExternal() {
        return this.external;
    }

    public void setExternal(boolean bl) {
        this.external = bl;
    }

    public Boolean getFlowX() {
        return this.flowX;
    }

    public void setFlowX(Boolean bl) {
        this.flowX = bl;
    }

    public int getHideMode() {
        return this.hideMode;
    }

    public void setHideMode(int n) {
        if (n < -1 || n > 3) {
            throw new IllegalArgumentException("Wrong hideMode: " + n);
        }
        this.hideMode = n;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String string) {
        this.id = string;
    }

    public UnitValue[] getPadding() {
        UnitValue[] unitValueArray;
        if (this.padding != null) {
            UnitValue[] unitValueArray2 = new UnitValue[4];
            unitValueArray2[0] = this.padding[0];
            unitValueArray2[1] = this.padding[1];
            unitValueArray2[2] = this.padding[2];
            unitValueArray = unitValueArray2;
            unitValueArray2[3] = this.padding[3];
        } else {
            unitValueArray = null;
        }
        return unitValueArray;
    }

    public void setPadding(UnitValue[] unitValueArray) {
        UnitValue[] unitValueArray2;
        if (unitValueArray != null) {
            UnitValue[] unitValueArray3 = new UnitValue[4];
            unitValueArray3[0] = unitValueArray[0];
            unitValueArray3[1] = unitValueArray[1];
            unitValueArray3[2] = unitValueArray[2];
            unitValueArray2 = unitValueArray3;
            unitValueArray3[3] = unitValueArray[3];
        } else {
            unitValueArray2 = null;
        }
        this.padding = unitValueArray2;
    }

    public int getSkip() {
        return this.skip;
    }

    public void setSkip(int n) {
        this.skip = n;
    }

    public int getSpanX() {
        return this.spanX;
    }

    public void setSpanX(int n) {
        this.spanX = n;
    }

    public int getSpanY() {
        return this.spanY;
    }

    public void setSpanY(int n) {
        this.spanY = n;
    }

    public Float getPushX() {
        return this.pushX;
    }

    public void setPushX(Float f) {
        this.pushX = f;
    }

    public Float getPushY() {
        return this.pushY;
    }

    public void setPushY(Float f) {
        this.pushY = f;
    }

    public int getSplit() {
        return this.split;
    }

    public void setSplit(int n) {
        this.split = n;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String string) {
        this.tag = string;
    }

    public boolean isWrap() {
        return this.wrap != null;
    }

    public void setWrap(boolean bl) {
        this.wrap = bl ? (this.wrap == null ? DEF_GAP : this.wrap) : null;
    }

    public BoundSize getWrapGapSize() {
        return this.wrap == DEF_GAP ? null : this.wrap;
    }

    public void setWrapGapSize(BoundSize boundSize) {
        this.wrap = boundSize == null ? (this.wrap != null ? DEF_GAP : null) : boundSize;
    }

    public boolean isNewline() {
        return this.newline != null;
    }

    public void setNewline(boolean bl) {
        this.newline = bl ? (this.newline == null ? DEF_GAP : this.newline) : null;
    }

    public BoundSize getNewlineGapSize() {
        return this.newline == DEF_GAP ? null : this.newline;
    }

    public void setNewlineGapSize(BoundSize boundSize) {
        this.newline = boundSize == null ? (this.newline != null ? DEF_GAP : null) : boundSize;
    }

    private Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(objectInput));
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        if (this.getClass() == CC.class) {
            LayoutUtil.writeAsXML(objectOutput, this);
        }
    }
}

