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
import java.util.ArrayList;
import java.util.HashMap;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ConstraintParser;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.LinkHandler;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.layout.UnitConverter;

public final class UnitValue
implements Serializable {
    private static final HashMap<String, Integer> UNIT_MAP = new HashMap(32);
    private static final ArrayList<UnitConverter> CONVERTERS = new ArrayList();
    public static final int STATIC = 100;
    public static final int ADD = 101;
    public static final int SUB = 102;
    public static final int MUL = 103;
    public static final int DIV = 104;
    public static final int MIN = 105;
    public static final int MAX = 106;
    public static final int MID = 107;
    public static final int PIXEL = 0;
    public static final int LPX = 1;
    public static final int LPY = 2;
    public static final int MM = 3;
    public static final int CM = 4;
    public static final int INCH = 5;
    public static final int PERCENT = 6;
    public static final int PT = 7;
    public static final int SPX = 8;
    public static final int SPY = 9;
    public static final int ALIGN = 12;
    public static final int MIN_SIZE = 13;
    public static final int PREF_SIZE = 14;
    public static final int MAX_SIZE = 15;
    public static final int BUTTON = 16;
    public static final int LINK_X = 18;
    public static final int LINK_Y = 19;
    public static final int LINK_W = 20;
    public static final int LINK_H = 21;
    public static final int LINK_X2 = 22;
    public static final int LINK_Y2 = 23;
    public static final int LINK_XPOS = 24;
    public static final int LINK_YPOS = 25;
    public static final int LOOKUP = 26;
    public static final int LABEL_ALIGN = 27;
    private static final int IDENTITY = -1;
    static final UnitValue ZERO;
    static final UnitValue TOP;
    static final UnitValue LEADING;
    static final UnitValue LEFT;
    static final UnitValue CENTER;
    static final UnitValue TRAILING;
    static final UnitValue RIGHT;
    static final UnitValue BOTTOM;
    static final UnitValue LABEL;
    static final UnitValue INF;
    static final UnitValue BASELINE_IDENTITY;
    private final transient float value;
    private final transient int unit;
    private final transient int oper;
    private final transient String unitStr;
    private transient String linkId = null;
    private final transient boolean isHor;
    private final transient UnitValue[] subUnits;
    private static final float[] SCALE;
    private static final long serialVersionUID = 1L;

    public UnitValue(float f) {
        this(f, null, 0, true, 100, null, null, f + "px");
    }

    public UnitValue(float f, int n, String string) {
        this(f, null, n, true, 100, null, null, string);
    }

    UnitValue(float f, String string, boolean bl, int n, String string2) {
        this(f, string, -1, bl, n, null, null, string2);
    }

    UnitValue(boolean bl, int n, UnitValue unitValue, UnitValue unitValue2, String string) {
        this(0.0f, "", -1, bl, n, unitValue, unitValue2, string);
        if (unitValue == null || unitValue2 == null) {
            throw new IllegalArgumentException("Sub units is null!");
        }
    }

    private UnitValue(float f, String string, int n, boolean bl, int n2, UnitValue unitValue, UnitValue unitValue2, String string2) {
        UnitValue[] unitValueArray;
        if (n2 < 100 || n2 > 107) {
            throw new IllegalArgumentException("Unknown Operation: " + n2);
        }
        if (n2 >= 101 && n2 <= 107 && (unitValue == null || unitValue2 == null)) {
            throw new IllegalArgumentException(n2 + " Operation may not have null sub-UnitValues.");
        }
        this.value = f;
        this.oper = n2;
        this.isHor = bl;
        this.unitStr = string;
        int n3 = this.unit = string != null ? this.parseUnitString() : n;
        if (unitValue != null && unitValue2 != null) {
            UnitValue[] unitValueArray2 = new UnitValue[2];
            unitValueArray2[0] = unitValue;
            unitValueArray = unitValueArray2;
            unitValueArray2[1] = unitValue2;
        } else {
            unitValueArray = null;
        }
        this.subUnits = unitValueArray;
        LayoutUtil.putCCString(this, string2);
    }

    public final int getPixels(float f, ContainerWrapper containerWrapper, ComponentWrapper componentWrapper) {
        return Math.round(this.getPixelsExact(f, containerWrapper, componentWrapper));
    }

    public final float getPixelsExact(float f, ContainerWrapper containerWrapper, ComponentWrapper componentWrapper) {
        if (containerWrapper == null) {
            return 1.0f;
        }
        if (this.oper == 100) {
            switch (this.unit) {
                case 0: {
                    return this.value;
                }
                case 1: 
                case 2: {
                    return containerWrapper.getPixelUnitFactor(this.unit == 1) * this.value;
                }
                case 3: 
                case 4: 
                case 5: 
                case 7: {
                    Float f2;
                    float f3 = SCALE[this.unit - 3];
                    Float f4 = f2 = this.isHor ? PlatformDefaults.getHorizontalScaleFactor() : PlatformDefaults.getVerticalScaleFactor();
                    if (f2 != null) {
                        f3 *= f2.floatValue();
                    }
                    return (float)(this.isHor ? containerWrapper.getHorizontalScreenDPI() : containerWrapper.getVerticalScreenDPI()) * this.value / f3;
                }
                case 6: {
                    return this.value * f * 0.01f;
                }
                case 8: 
                case 9: {
                    return (float)(this.unit == 8 ? containerWrapper.getScreenWidth() : containerWrapper.getScreenHeight()) * this.value * 0.01f;
                }
                case 12: {
                    Integer n = LinkHandler.getValue(containerWrapper.getLayout(), "visual", this.isHor ? 0 : 1);
                    Integer n2 = LinkHandler.getValue(containerWrapper.getLayout(), "visual", this.isHor ? 2 : 3);
                    if (n == null || n2 == null) {
                        return 0.0f;
                    }
                    return this.value * ((float)Math.max(0, n2) - f) + (float)n.intValue();
                }
                case 13: {
                    if (componentWrapper == null) {
                        return 0.0f;
                    }
                    return this.isHor ? (float)componentWrapper.getMinimumWidth(componentWrapper.getHeight()) : (float)componentWrapper.getMinimumHeight(componentWrapper.getWidth());
                }
                case 14: {
                    if (componentWrapper == null) {
                        return 0.0f;
                    }
                    return this.isHor ? (float)componentWrapper.getPreferredWidth(componentWrapper.getHeight()) : (float)componentWrapper.getPreferredHeight(componentWrapper.getWidth());
                }
                case 15: {
                    if (componentWrapper == null) {
                        return 0.0f;
                    }
                    return this.isHor ? (float)componentWrapper.getMaximumWidth(componentWrapper.getHeight()) : (float)componentWrapper.getMaximumHeight(componentWrapper.getWidth());
                }
                case 16: {
                    return PlatformDefaults.getMinimumButtonWidth().getPixels(f, containerWrapper, componentWrapper);
                }
                case 18: 
                case 19: 
                case 20: 
                case 21: 
                case 22: 
                case 23: 
                case 24: 
                case 25: {
                    Integer n = LinkHandler.getValue(containerWrapper.getLayout(), this.getLinkTargetId(), this.unit - (this.unit >= 24 ? 24 : 18));
                    if (n == null) {
                        return 0.0f;
                    }
                    if (this.unit == 24) {
                        return containerWrapper.getScreenLocationX() + n;
                    }
                    if (this.unit == 25) {
                        return containerWrapper.getScreenLocationY() + n;
                    }
                    return n.intValue();
                }
                case 26: {
                    float f5 = this.lookup(f, containerWrapper, componentWrapper);
                    if (f5 != -8.765431E7f) {
                        return f5;
                    }
                }
                case 27: {
                    return PlatformDefaults.getLabelAlignPercentage() * f;
                }
            }
            throw new IllegalArgumentException("Unknown/illegal unit: " + this.unit + ", unitStr: " + this.unitStr);
        }
        if (this.subUnits != null && this.subUnits.length == 2) {
            float f6 = this.subUnits[0].getPixelsExact(f, containerWrapper, componentWrapper);
            float f7 = this.subUnits[1].getPixelsExact(f, containerWrapper, componentWrapper);
            switch (this.oper) {
                case 101: {
                    return f6 + f7;
                }
                case 102: {
                    return f6 - f7;
                }
                case 103: {
                    return f6 * f7;
                }
                case 104: {
                    return f6 / f7;
                }
                case 105: {
                    return f6 < f7 ? f6 : f7;
                }
                case 106: {
                    return f6 > f7 ? f6 : f7;
                }
                case 107: {
                    return (f6 + f7) * 0.5f;
                }
            }
        }
        throw new IllegalArgumentException("Internal: Unknown Oper: " + this.oper);
    }

    private float lookup(float f, ContainerWrapper containerWrapper, ComponentWrapper componentWrapper) {
        float f2 = -8.765431E7f;
        for (int i = CONVERTERS.size() - 1; i >= 0; --i) {
            f2 = CONVERTERS.get(i).convertToPixels(this.value, this.unitStr, this.isHor, f, containerWrapper, componentWrapper);
            if (f2 == -8.765431E7f) continue;
            return f2;
        }
        return PlatformDefaults.convertToPixels(this.value, this.unitStr, this.isHor, f, containerWrapper, componentWrapper);
    }

    private int parseUnitString() {
        int n = this.unitStr.length();
        if (n == 0) {
            return this.isHor ? PlatformDefaults.getDefaultHorizontalUnit() : PlatformDefaults.getDefaultVerticalUnit();
        }
        Integer n2 = UNIT_MAP.get(this.unitStr);
        if (n2 != null) {
            return n2;
        }
        if (this.unitStr.equals("lp")) {
            return this.isHor ? 1 : 2;
        }
        if (this.unitStr.equals("sp")) {
            return this.isHor ? 8 : 9;
        }
        if (this.lookup(0.0f, null, null) != -8.765431E7f) {
            return 26;
        }
        int n3 = this.unitStr.indexOf(46);
        if (n3 != -1) {
            this.linkId = this.unitStr.substring(0, n3);
            String string = this.unitStr.substring(n3 + 1);
            if (string.equals("x")) {
                return 18;
            }
            if (string.equals("y")) {
                return 19;
            }
            if (string.equals("w") || string.equals("width")) {
                return 20;
            }
            if (string.equals("h") || string.equals("height")) {
                return 21;
            }
            if (string.equals("x2")) {
                return 22;
            }
            if (string.equals("y2")) {
                return 23;
            }
            if (string.equals("xpos")) {
                return 24;
            }
            if (string.equals("ypos")) {
                return 25;
            }
        }
        throw new IllegalArgumentException("Unknown keyword: " + this.unitStr);
    }

    final boolean isLinked() {
        return this.linkId != null;
    }

    final boolean isLinkedDeep() {
        if (this.subUnits == null) {
            return this.linkId != null;
        }
        for (int i = 0; i < this.subUnits.length; ++i) {
            if (!this.subUnits[i].isLinkedDeep()) continue;
            return true;
        }
        return false;
    }

    final String getLinkTargetId() {
        return this.linkId;
    }

    final UnitValue getSubUnitValue(int n) {
        return this.subUnits[n];
    }

    final int getSubUnitCount() {
        return this.subUnits != null ? this.subUnits.length : 0;
    }

    public final UnitValue[] getSubUnits() {
        return this.subUnits;
    }

    public final int getUnit() {
        return this.unit;
    }

    public final String getUnitString() {
        return this.unitStr;
    }

    public final int getOperation() {
        return this.oper;
    }

    public final float getValue() {
        return this.value;
    }

    public final boolean isHorizontal() {
        return this.isHor;
    }

    public final String toString() {
        return this.getClass().getName() + ". Value=" + this.value + ", unit=" + this.unit + ", unitString: " + this.unitStr + ", oper=" + this.oper + ", isHor: " + this.isHor;
    }

    public final String getConstraintString() {
        return LayoutUtil.getCCString(this);
    }

    public final int hashCode() {
        return (int)(this.value * 12345.0f) + (this.oper >>> 5) + this.unit >>> 17;
    }

    public static final synchronized void addGlobalUnitConverter(UnitConverter unitConverter) {
        if (unitConverter == null) {
            throw new NullPointerException();
        }
        CONVERTERS.add(unitConverter);
    }

    public static final synchronized boolean removeGlobalUnitConverter(String string) {
        return CONVERTERS.remove(string);
    }

    public static final synchronized UnitConverter[] getGlobalUnitConverters() {
        return CONVERTERS.toArray(new UnitConverter[CONVERTERS.size()]);
    }

    public static final int getDefaultUnit() {
        return PlatformDefaults.getDefaultHorizontalUnit();
    }

    public static final void setDefaultUnit(int n) {
        PlatformDefaults.setDefaultHorizontalUnit(n);
        PlatformDefaults.setDefaultVerticalUnit(n);
    }

    private Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.getClass() == UnitValue.class) {
            LayoutUtil.writeAsXML(objectOutputStream, this);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(objectInputStream));
    }

    static {
        UNIT_MAP.put("px", new Integer(0));
        UNIT_MAP.put("lpx", new Integer(1));
        UNIT_MAP.put("lpy", new Integer(2));
        UNIT_MAP.put("%", new Integer(6));
        UNIT_MAP.put("cm", new Integer(4));
        UNIT_MAP.put("in", new Integer(5));
        UNIT_MAP.put("spx", new Integer(8));
        UNIT_MAP.put("spy", new Integer(9));
        UNIT_MAP.put("al", new Integer(12));
        UNIT_MAP.put("mm", new Integer(3));
        UNIT_MAP.put("pt", new Integer(7));
        UNIT_MAP.put("min", new Integer(13));
        UNIT_MAP.put("minimum", new Integer(13));
        UNIT_MAP.put("p", new Integer(14));
        UNIT_MAP.put("pref", new Integer(14));
        UNIT_MAP.put("max", new Integer(15));
        UNIT_MAP.put("maximum", new Integer(15));
        UNIT_MAP.put("button", new Integer(16));
        UNIT_MAP.put("label", new Integer(27));
        ZERO = new UnitValue(0.0f, null, 0, true, 100, null, null, "0px");
        TOP = new UnitValue(0.0f, null, 6, false, 100, null, null, "top");
        LEADING = new UnitValue(0.0f, null, 6, true, 100, null, null, "leading");
        LEFT = new UnitValue(0.0f, null, 6, true, 100, null, null, "left");
        CENTER = new UnitValue(50.0f, null, 6, true, 100, null, null, "center");
        TRAILING = new UnitValue(100.0f, null, 6, true, 100, null, null, "trailing");
        RIGHT = new UnitValue(100.0f, null, 6, true, 100, null, null, "right");
        BOTTOM = new UnitValue(100.0f, null, 6, false, 100, null, null, "bottom");
        LABEL = new UnitValue(0.0f, null, 27, false, 100, null, null, "label");
        INF = new UnitValue(2097051.0f, null, 0, true, 100, null, null, "inf");
        BASELINE_IDENTITY = new UnitValue(0.0f, null, -1, false, 100, null, null, "baseline");
        SCALE = new float[]{25.4f, 2.54f, 1.0f, 0.0f, 72.0f};
        LayoutUtil.setDelegate(UnitValue.class, new PersistenceDelegate(){

            protected Expression instantiate(Object object, Encoder encoder) {
                UnitValue unitValue = (UnitValue)object;
                String string = unitValue.getConstraintString();
                if (string == null) {
                    throw new IllegalStateException("Design time must be on to use XML persistence. See LayoutUtil.");
                }
                return new Expression(object, ConstraintParser.class, "parseUnitValueOrAlign", new Object[]{unitValue.getConstraintString(), unitValue.isHorizontal() ? Boolean.TRUE : Boolean.FALSE, null});
            }
        });
    }
}

