/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.layout;

import java.util.HashMap;
import net.miginfocom.layout.BoundSize;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.InCellGapProvider;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.UnitValue;

public final class PlatformDefaults {
    private static int DEF_H_UNIT = 1;
    private static int DEF_V_UNIT = 2;
    private static InCellGapProvider GAP_PROVIDER = null;
    private static volatile int MOD_COUNT = 0;
    private static final UnitValue LPX4 = new UnitValue(4.0f, 1, null);
    private static final UnitValue LPX6 = new UnitValue(6.0f, 1, null);
    private static final UnitValue LPX7 = new UnitValue(7.0f, 1, null);
    private static final UnitValue LPX9 = new UnitValue(9.0f, 1, null);
    private static final UnitValue LPX10 = new UnitValue(10.0f, 1, null);
    private static final UnitValue LPX11 = new UnitValue(11.0f, 1, null);
    private static final UnitValue LPX12 = new UnitValue(12.0f, 1, null);
    private static final UnitValue LPX14 = new UnitValue(14.0f, 1, null);
    private static final UnitValue LPX16 = new UnitValue(16.0f, 1, null);
    private static final UnitValue LPX18 = new UnitValue(18.0f, 1, null);
    private static final UnitValue LPX20 = new UnitValue(20.0f, 1, null);
    private static final UnitValue LPY4 = new UnitValue(4.0f, 2, null);
    private static final UnitValue LPY6 = new UnitValue(6.0f, 2, null);
    private static final UnitValue LPY7 = new UnitValue(7.0f, 2, null);
    private static final UnitValue LPY9 = new UnitValue(9.0f, 2, null);
    private static final UnitValue LPY10 = new UnitValue(10.0f, 2, null);
    private static final UnitValue LPY11 = new UnitValue(11.0f, 2, null);
    private static final UnitValue LPY12 = new UnitValue(12.0f, 2, null);
    private static final UnitValue LPY14 = new UnitValue(14.0f, 2, null);
    private static final UnitValue LPY16 = new UnitValue(16.0f, 2, null);
    private static final UnitValue LPY18 = new UnitValue(18.0f, 2, null);
    private static final UnitValue LPY20 = new UnitValue(20.0f, 2, null);
    public static final int WINDOWS_XP = 0;
    public static final int MAC_OSX = 1;
    public static final int GNOME = 2;
    private static int CUR_PLAF = 0;
    private static final UnitValue[] PANEL_INS = new UnitValue[4];
    private static final UnitValue[] DIALOG_INS = new UnitValue[4];
    private static String BUTTON_FORMAT = null;
    private static final HashMap<String, UnitValue> HOR_DEFS = new HashMap(32);
    private static final HashMap<String, UnitValue> VER_DEFS = new HashMap(32);
    private static BoundSize DEF_VGAP = null;
    private static BoundSize DEF_HGAP = null;
    static BoundSize RELATED_X = null;
    static BoundSize RELATED_Y = null;
    static BoundSize UNRELATED_X = null;
    static BoundSize UNRELATED_Y = null;
    private static UnitValue BUTT_WIDTH = null;
    private static Float horScale = null;
    private static Float verScale = null;
    public static final int BASE_FONT_SIZE = 100;
    public static final int BASE_SCALE_FACTOR = 101;
    public static final int BASE_REAL_PIXEL = 102;
    private static int LP_BASE = 101;
    private static Integer BASE_DPI_FORCED = null;
    private static int BASE_DPI = 96;
    private static boolean dra = true;

    public static int getCurrentPlatform() {
        String string = System.getProperty("os.name");
        if (string.startsWith("Mac OS")) {
            return 1;
        }
        if (string.startsWith("Linux")) {
            return 2;
        }
        return 0;
    }

    private PlatformDefaults() {
    }

    public static void setPlatform(int n) {
        switch (n) {
            case 0: {
                PlatformDefaults.setRelatedGap(LPX4, LPY4);
                PlatformDefaults.setUnrelatedGap(LPX7, LPY9);
                PlatformDefaults.setParagraphGap(LPX14, LPY14);
                PlatformDefaults.setIndentGap(LPX9, LPY9);
                PlatformDefaults.setGridCellGap(LPX4, LPY4);
                PlatformDefaults.setMinimumButtonWidth(new UnitValue(75.0f, 1, null));
                PlatformDefaults.setButtonOrder("L_E+U+YNBXOCAH_R");
                PlatformDefaults.setDialogInsets(LPY11, LPX11, LPY11, LPX11);
                PlatformDefaults.setPanelInsets(LPY7, LPX7, LPY7, LPX7);
                break;
            }
            case 1: {
                PlatformDefaults.setRelatedGap(LPX4, LPY4);
                PlatformDefaults.setUnrelatedGap(LPX7, LPY9);
                PlatformDefaults.setParagraphGap(LPX14, LPY14);
                PlatformDefaults.setIndentGap(LPX10, LPY10);
                PlatformDefaults.setGridCellGap(LPX4, LPY4);
                PlatformDefaults.setMinimumButtonWidth(new UnitValue(68.0f, 1, null));
                PlatformDefaults.setButtonOrder("L_HE+U+NYBXCOA_R");
                PlatformDefaults.setDialogInsets(LPY14, LPX20, LPY20, LPX20);
                PlatformDefaults.setPanelInsets(LPY16, LPX16, LPY16, LPX16);
                break;
            }
            case 2: {
                PlatformDefaults.setRelatedGap(LPX6, LPY6);
                PlatformDefaults.setUnrelatedGap(LPX12, LPY12);
                PlatformDefaults.setParagraphGap(LPX18, LPY18);
                PlatformDefaults.setIndentGap(LPX12, LPY12);
                PlatformDefaults.setGridCellGap(LPX6, LPY6);
                PlatformDefaults.setMinimumButtonWidth(new UnitValue(85.0f, 1, null));
                PlatformDefaults.setButtonOrder("L_HE+UNYACBXIO_R");
                PlatformDefaults.setDialogInsets(LPY12, LPX12, LPY12, LPX12);
                PlatformDefaults.setPanelInsets(LPY6, LPX6, LPY6, LPX6);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown platform: " + n);
            }
        }
        CUR_PLAF = n;
        BASE_DPI = BASE_DPI_FORCED != null ? BASE_DPI_FORCED : PlatformDefaults.getPlatformDPI(n);
    }

    private static int getPlatformDPI(int n) {
        switch (n) {
            case 0: 
            case 2: {
                return 96;
            }
            case 1: {
                try {
                    return System.getProperty("java.version").compareTo("1.6") < 0 ? 72 : 96;
                }
                catch (Throwable throwable) {
                    return 72;
                }
            }
        }
        throw new IllegalArgumentException("Unknown platform: " + n);
    }

    public static int getPlatform() {
        return CUR_PLAF;
    }

    public static int getDefaultDPI() {
        return BASE_DPI;
    }

    public static void setDefaultDPI(Integer n) {
        BASE_DPI = n != null ? n : PlatformDefaults.getPlatformDPI(CUR_PLAF);
        BASE_DPI_FORCED = n;
    }

    public static Float getHorizontalScaleFactor() {
        return horScale;
    }

    public static void setHorizontalScaleFactor(Float f) {
        if (!LayoutUtil.equals(horScale, f)) {
            horScale = f;
            ++MOD_COUNT;
        }
    }

    public static Float getVerticalScaleFactor() {
        return verScale;
    }

    public static void setVerticalScaleFactor(Float f) {
        if (!LayoutUtil.equals(verScale, f)) {
            verScale = f;
            ++MOD_COUNT;
        }
    }

    public static int getLogicalPixelBase() {
        return LP_BASE;
    }

    public static void setLogicalPixelBase(int n) {
        if (LP_BASE != n) {
            if (n < 100 || n > 101) {
                throw new IllegalArgumentException("Unrecognized base: " + n);
            }
            LP_BASE = n;
            ++MOD_COUNT;
        }
    }

    public static void setRelatedGap(UnitValue unitValue, UnitValue unitValue2) {
        PlatformDefaults.setUnitValue(new String[]{"r", "rel", "related"}, unitValue, unitValue2);
        RELATED_X = new BoundSize(unitValue, unitValue, null, "rel:rel");
        RELATED_Y = new BoundSize(unitValue2, unitValue2, null, "rel:rel");
    }

    public static void setUnrelatedGap(UnitValue unitValue, UnitValue unitValue2) {
        PlatformDefaults.setUnitValue(new String[]{"u", "unrel", "unrelated"}, unitValue, unitValue2);
        UNRELATED_X = new BoundSize(unitValue, unitValue, null, "unrel:unrel");
        UNRELATED_Y = new BoundSize(unitValue2, unitValue2, null, "unrel:unrel");
    }

    public static void setParagraphGap(UnitValue unitValue, UnitValue unitValue2) {
        PlatformDefaults.setUnitValue(new String[]{"p", "para", "paragraph"}, unitValue, unitValue2);
    }

    public static void setIndentGap(UnitValue unitValue, UnitValue unitValue2) {
        PlatformDefaults.setUnitValue(new String[]{"i", "ind", "indent"}, unitValue, unitValue2);
    }

    public static void setGridCellGap(UnitValue unitValue, UnitValue unitValue2) {
        if (unitValue != null) {
            DEF_HGAP = new BoundSize(unitValue, unitValue, null, null);
        }
        if (unitValue2 != null) {
            DEF_VGAP = new BoundSize(unitValue2, unitValue2, null, null);
        }
        ++MOD_COUNT;
    }

    public static void setMinimumButtonWidth(UnitValue unitValue) {
        BUTT_WIDTH = unitValue;
        ++MOD_COUNT;
    }

    public static UnitValue getMinimumButtonWidth() {
        return BUTT_WIDTH;
    }

    public static UnitValue getUnitValueX(String string) {
        return HOR_DEFS.get(string);
    }

    public static UnitValue getUnitValueY(String string) {
        return VER_DEFS.get(string);
    }

    public static final void setUnitValue(String[] stringArray, UnitValue unitValue, UnitValue unitValue2) {
        for (int i = 0; i < stringArray.length; ++i) {
            String string = stringArray[i].toLowerCase().trim();
            if (unitValue != null) {
                HOR_DEFS.put(string, unitValue);
            }
            if (unitValue2 == null) continue;
            VER_DEFS.put(string, unitValue2);
        }
        ++MOD_COUNT;
    }

    static final int convertToPixels(float f, String string, boolean bl, float f2, ContainerWrapper containerWrapper, ComponentWrapper componentWrapper) {
        UnitValue unitValue = (bl ? HOR_DEFS : VER_DEFS).get(string);
        return unitValue != null ? Math.round(f * (float)unitValue.getPixels(f2, containerWrapper, componentWrapper)) : -87654312;
    }

    public static final String getButtonOrder() {
        return BUTTON_FORMAT;
    }

    public static final void setButtonOrder(String string) {
        BUTTON_FORMAT = string;
        ++MOD_COUNT;
    }

    static final String getTagForChar(char c2) {
        switch (c2) {
            case 'o': {
                return "ok";
            }
            case 'c': {
                return "cancel";
            }
            case 'h': {
                return "help";
            }
            case 'e': {
                return "help2";
            }
            case 'y': {
                return "yes";
            }
            case 'n': {
                return "no";
            }
            case 'a': {
                return "apply";
            }
            case 'x': {
                return "next";
            }
            case 'b': {
                return "back";
            }
            case 'i': {
                return "finish";
            }
            case 'l': {
                return "left";
            }
            case 'r': {
                return "right";
            }
            case 'u': {
                return "other";
            }
        }
        return null;
    }

    public static BoundSize getGridGapX() {
        return DEF_HGAP;
    }

    public static BoundSize getGridGapY() {
        return DEF_VGAP;
    }

    public static UnitValue getDialogInsets(int n) {
        return DIALOG_INS[n];
    }

    public static void setDialogInsets(UnitValue unitValue, UnitValue unitValue2, UnitValue unitValue3, UnitValue unitValue4) {
        if (unitValue != null) {
            PlatformDefaults.DIALOG_INS[0] = unitValue;
        }
        if (unitValue2 != null) {
            PlatformDefaults.DIALOG_INS[1] = unitValue2;
        }
        if (unitValue3 != null) {
            PlatformDefaults.DIALOG_INS[2] = unitValue3;
        }
        if (unitValue4 != null) {
            PlatformDefaults.DIALOG_INS[3] = unitValue4;
        }
        ++MOD_COUNT;
    }

    public static UnitValue getPanelInsets(int n) {
        return PANEL_INS[n];
    }

    public static void setPanelInsets(UnitValue unitValue, UnitValue unitValue2, UnitValue unitValue3, UnitValue unitValue4) {
        if (unitValue != null) {
            PlatformDefaults.PANEL_INS[0] = unitValue;
        }
        if (unitValue2 != null) {
            PlatformDefaults.PANEL_INS[1] = unitValue2;
        }
        if (unitValue3 != null) {
            PlatformDefaults.PANEL_INS[2] = unitValue3;
        }
        if (unitValue4 != null) {
            PlatformDefaults.PANEL_INS[3] = unitValue4;
        }
        ++MOD_COUNT;
    }

    public static float getLabelAlignPercentage() {
        return CUR_PLAF == 1 ? 1.0f : 0.0f;
    }

    static BoundSize getDefaultComponentGap(ComponentWrapper componentWrapper, ComponentWrapper componentWrapper2, int n, String string, boolean bl) {
        if (GAP_PROVIDER != null) {
            return GAP_PROVIDER.getDefaultGap(componentWrapper, componentWrapper2, n, string, bl);
        }
        if (componentWrapper2 == null) {
            return null;
        }
        return n == 2 || n == 4 ? RELATED_X : RELATED_Y;
    }

    public static InCellGapProvider getGapProvider() {
        return GAP_PROVIDER;
    }

    public static void setGapProvider(InCellGapProvider inCellGapProvider) {
        GAP_PROVIDER = inCellGapProvider;
    }

    public static int getModCount() {
        return MOD_COUNT;
    }

    public void invalidate() {
        ++MOD_COUNT;
    }

    public static final int getDefaultHorizontalUnit() {
        return DEF_H_UNIT;
    }

    public static final void setDefaultHorizontalUnit(int n) {
        if (n < 0 || n > 27) {
            throw new IllegalArgumentException("Illegal Unit: " + n);
        }
        if (DEF_H_UNIT != n) {
            DEF_H_UNIT = n;
            ++MOD_COUNT;
        }
    }

    public static final int getDefaultVerticalUnit() {
        return DEF_V_UNIT;
    }

    public static final void setDefaultVerticalUnit(int n) {
        if (n < 0 || n > 27) {
            throw new IllegalArgumentException("Illegal Unit: " + n);
        }
        if (DEF_V_UNIT != n) {
            DEF_V_UNIT = n;
            ++MOD_COUNT;
        }
    }

    public static boolean getDefaultRowAlignmentBaseline() {
        return dra;
    }

    public static void setDefaultRowAlignmentBaseline(boolean bl) {
        dra = bl;
    }

    static {
        PlatformDefaults.setPlatform(PlatformDefaults.getCurrentPlatform());
        MOD_COUNT = 0;
    }
}

