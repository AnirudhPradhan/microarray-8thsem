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
import net.miginfocom.layout.ConstraintParser;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.UnitValue;

public final class LC
implements Externalizable {
    private int wrapAfter = 2097051;
    private Boolean leftToRight = null;
    private UnitValue[] insets = null;
    private UnitValue alignX = null;
    private UnitValue alignY = null;
    private BoundSize gridGapX = null;
    private BoundSize gridGapY = null;
    private BoundSize width = BoundSize.NULL_SIZE;
    private BoundSize height = BoundSize.NULL_SIZE;
    private BoundSize packW = BoundSize.NULL_SIZE;
    private BoundSize packH = BoundSize.NULL_SIZE;
    private float pwAlign = 0.5f;
    private float phAlign = 1.0f;
    private int debugMillis = 0;
    private int hideMode = 0;
    private boolean noCache = false;
    private boolean flowX = true;
    private boolean fillX = false;
    private boolean fillY = false;
    private boolean topToBottom = true;
    private boolean noGrid = false;
    private boolean visualPadding = true;

    public boolean isNoCache() {
        return this.noCache;
    }

    public void setNoCache(boolean bl) {
        this.noCache = bl;
    }

    public final UnitValue getAlignX() {
        return this.alignX;
    }

    public final void setAlignX(UnitValue unitValue) {
        this.alignX = unitValue;
    }

    public final UnitValue getAlignY() {
        return this.alignY;
    }

    public final void setAlignY(UnitValue unitValue) {
        this.alignY = unitValue;
    }

    public final int getDebugMillis() {
        return this.debugMillis;
    }

    public final void setDebugMillis(int n) {
        this.debugMillis = n;
    }

    public final boolean isFillX() {
        return this.fillX;
    }

    public final void setFillX(boolean bl) {
        this.fillX = bl;
    }

    public final boolean isFillY() {
        return this.fillY;
    }

    public final void setFillY(boolean bl) {
        this.fillY = bl;
    }

    public final boolean isFlowX() {
        return this.flowX;
    }

    public final void setFlowX(boolean bl) {
        this.flowX = bl;
    }

    public final BoundSize getGridGapX() {
        return this.gridGapX;
    }

    public final void setGridGapX(BoundSize boundSize) {
        this.gridGapX = boundSize;
    }

    public final BoundSize getGridGapY() {
        return this.gridGapY;
    }

    public final void setGridGapY(BoundSize boundSize) {
        this.gridGapY = boundSize;
    }

    public final int getHideMode() {
        return this.hideMode;
    }

    public final void setHideMode(int n) {
        if (n < 0 || n > 3) {
            throw new IllegalArgumentException("Wrong hideMode: " + n);
        }
        this.hideMode = n;
    }

    public final UnitValue[] getInsets() {
        UnitValue[] unitValueArray;
        if (this.insets != null) {
            UnitValue[] unitValueArray2 = new UnitValue[4];
            unitValueArray2[0] = this.insets[0];
            unitValueArray2[1] = this.insets[1];
            unitValueArray2[2] = this.insets[2];
            unitValueArray = unitValueArray2;
            unitValueArray2[3] = this.insets[3];
        } else {
            unitValueArray = null;
        }
        return unitValueArray;
    }

    public final void setInsets(UnitValue[] unitValueArray) {
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
        this.insets = unitValueArray2;
    }

    public final Boolean getLeftToRight() {
        return this.leftToRight;
    }

    public final void setLeftToRight(Boolean bl) {
        this.leftToRight = bl;
    }

    public final boolean isNoGrid() {
        return this.noGrid;
    }

    public final void setNoGrid(boolean bl) {
        this.noGrid = bl;
    }

    public final boolean isTopToBottom() {
        return this.topToBottom;
    }

    public final void setTopToBottom(boolean bl) {
        this.topToBottom = bl;
    }

    public final boolean isVisualPadding() {
        return this.visualPadding;
    }

    public final void setVisualPadding(boolean bl) {
        this.visualPadding = bl;
    }

    public final int getWrapAfter() {
        return this.wrapAfter;
    }

    public final void setWrapAfter(int n) {
        this.wrapAfter = n;
    }

    public final BoundSize getPackWidth() {
        return this.packW;
    }

    public final void setPackWidth(BoundSize boundSize) {
        this.packW = boundSize != null ? boundSize : BoundSize.NULL_SIZE;
    }

    public final BoundSize getPackHeight() {
        return this.packH;
    }

    public final void setPackHeight(BoundSize boundSize) {
        this.packH = boundSize != null ? boundSize : BoundSize.NULL_SIZE;
    }

    public final float getPackHeightAlign() {
        return this.phAlign;
    }

    public final void setPackHeightAlign(float f) {
        this.phAlign = Math.max(0.0f, Math.min(1.0f, f));
    }

    public final float getPackWidthAlign() {
        return this.pwAlign;
    }

    public final void setPackWidthAlign(float f) {
        this.pwAlign = Math.max(0.0f, Math.min(1.0f, f));
    }

    public final BoundSize getWidth() {
        return this.width;
    }

    public final void setWidth(BoundSize boundSize) {
        this.width = boundSize != null ? boundSize : BoundSize.NULL_SIZE;
    }

    public final BoundSize getHeight() {
        return this.height;
    }

    public final void setHeight(BoundSize boundSize) {
        this.height = boundSize != null ? boundSize : BoundSize.NULL_SIZE;
    }

    public final LC pack() {
        return this.pack("pref", "pref");
    }

    public final LC pack(String string, String string2) {
        this.setPackWidth(string != null ? ConstraintParser.parseBoundSize(string, false, false) : BoundSize.NULL_SIZE);
        this.setPackHeight(string2 != null ? ConstraintParser.parseBoundSize(string2, false, false) : BoundSize.NULL_SIZE);
        return this;
    }

    public final LC packAlign(float f, float f2) {
        this.setPackWidthAlign(f);
        this.setPackHeightAlign(f2);
        return this;
    }

    public final LC wrap() {
        this.setWrapAfter(0);
        return this;
    }

    public final LC wrapAfter(int n) {
        this.setWrapAfter(n);
        return this;
    }

    public final LC noCache() {
        this.setNoCache(true);
        return this;
    }

    public final LC flowY() {
        this.setFlowX(false);
        return this;
    }

    public final LC flowX() {
        this.setFlowX(true);
        return this;
    }

    public final LC fill() {
        this.setFillX(true);
        this.setFillY(true);
        return this;
    }

    public final LC fillX() {
        this.setFillX(true);
        return this;
    }

    public final LC fillY() {
        this.setFillY(true);
        return this;
    }

    public final LC leftToRight(boolean bl) {
        this.setLeftToRight(bl ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public final LC rightToLeft() {
        this.setLeftToRight(Boolean.FALSE);
        return this;
    }

    public final LC bottomToTop() {
        this.setTopToBottom(false);
        return this;
    }

    public final LC topToBottom() {
        this.setTopToBottom(true);
        return this;
    }

    public final LC noGrid() {
        this.setNoGrid(true);
        return this;
    }

    public final LC noVisualPadding() {
        this.setVisualPadding(false);
        return this;
    }

    public final LC insetsAll(String string) {
        UnitValue unitValue = ConstraintParser.parseUnitValue(string, true);
        UnitValue unitValue2 = ConstraintParser.parseUnitValue(string, false);
        this.insets = new UnitValue[]{unitValue2, unitValue, unitValue2, unitValue};
        return this;
    }

    public final LC insets(String string) {
        this.insets = ConstraintParser.parseInsets(string, true);
        return this;
    }

    public final LC insets(String string, String string2, String string3, String string4) {
        this.insets = new UnitValue[]{ConstraintParser.parseUnitValue(string, false), ConstraintParser.parseUnitValue(string2, true), ConstraintParser.parseUnitValue(string3, false), ConstraintParser.parseUnitValue(string4, true)};
        return this;
    }

    public final LC alignX(String string) {
        this.setAlignX(ConstraintParser.parseUnitValueOrAlign(string, true, null));
        return this;
    }

    public final LC alignY(String string) {
        this.setAlignY(ConstraintParser.parseUnitValueOrAlign(string, false, null));
        return this;
    }

    public final LC align(String string, String string2) {
        if (string != null) {
            this.alignX(string);
        }
        if (string2 != null) {
            this.alignY(string2);
        }
        return this;
    }

    public final LC gridGapX(String string) {
        this.setGridGapX(ConstraintParser.parseBoundSize(string, true, true));
        return this;
    }

    public final LC gridGapY(String string) {
        this.setGridGapY(ConstraintParser.parseBoundSize(string, true, false));
        return this;
    }

    public final LC gridGap(String string, String string2) {
        if (string != null) {
            this.gridGapX(string);
        }
        if (string2 != null) {
            this.gridGapY(string2);
        }
        return this;
    }

    public final LC debug(int n) {
        this.setDebugMillis(n);
        return this;
    }

    public final LC hideMode(int n) {
        this.setHideMode(n);
        return this;
    }

    public final LC minWidth(String string) {
        this.setWidth(LayoutUtil.derive(this.getWidth(), ConstraintParser.parseUnitValue(string, true), null, null));
        return this;
    }

    public final LC width(String string) {
        this.setWidth(ConstraintParser.parseBoundSize(string, false, true));
        return this;
    }

    public final LC maxWidth(String string) {
        this.setWidth(LayoutUtil.derive(this.getWidth(), null, null, ConstraintParser.parseUnitValue(string, true)));
        return this;
    }

    public final LC minHeight(String string) {
        this.setHeight(LayoutUtil.derive(this.getHeight(), ConstraintParser.parseUnitValue(string, false), null, null));
        return this;
    }

    public final LC height(String string) {
        this.setHeight(ConstraintParser.parseBoundSize(string, false, false));
        return this;
    }

    public final LC maxHeight(String string) {
        this.setHeight(LayoutUtil.derive(this.getHeight(), null, null, ConstraintParser.parseUnitValue(string, false)));
        return this;
    }

    private Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(objectInput));
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        if (this.getClass() == LC.class) {
            LayoutUtil.writeAsXML(objectOutput, this);
        }
    }
}

