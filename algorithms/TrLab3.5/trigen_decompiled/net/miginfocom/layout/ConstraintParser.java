/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.BoundSize;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.DimConstraint;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.layout.ResizeConstraint;
import net.miginfocom.layout.UnitValue;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class ConstraintParser {
    private ConstraintParser() {
    }

    public static LC parseLayoutConstraint(String string) {
        int n;
        String string2;
        int n2;
        LC lC = new LC();
        if (string.length() == 0) {
            return lC;
        }
        String[] stringArray = ConstraintParser.toTrimmedTokens(string, ',');
        for (n2 = 0; n2 < stringArray.length; ++n2) {
            string2 = stringArray[n2];
            if (string2 == null || (n = string2.length()) != 3 && n != 11) continue;
            if (string2.equals("ltr") || string2.equals("rtl") || string2.equals("lefttoright") || string2.equals("righttoleft")) {
                lC.setLeftToRight(string2.charAt(0) == 'l' ? Boolean.TRUE : Boolean.FALSE);
                stringArray[n2] = null;
            }
            if (!string2.equals("ttb") && !string2.equals("btt") && !string2.equals("toptobottom") && !string2.equals("bottomtotop")) continue;
            lC.setTopToBottom(string2.charAt(0) == 't');
            stringArray[n2] = null;
        }
        for (n2 = 0; n2 < stringArray.length; ++n2) {
            string2 = stringArray[n2];
            if (string2 == null || string2.length() == 0) continue;
            try {
                UnitValue unitValue;
                Object[] objectArray;
                n = -1;
                char c2 = string2.charAt(0);
                if (c2 == 'w' || c2 == 'h') {
                    boolean bl;
                    n = ConstraintParser.startsWithLenient(string2, "wrap", -1, true);
                    if (n > -1) {
                        String string3 = string2.substring(n).trim();
                        lC.setWrapAfter(string3.length() != 0 ? Integer.parseInt(string3) : 0);
                        continue;
                    }
                    boolean bl2 = bl = c2 == 'w';
                    if (bl && (string2.startsWith("w ") || string2.startsWith("width "))) {
                        objectArray = string2.substring(string2.charAt(1) == ' ' ? 2 : 6).trim();
                        lC.setWidth(ConstraintParser.parseBoundSize((String)objectArray, false, true));
                        continue;
                    }
                    if (!bl && (string2.startsWith("h ") || string2.startsWith("height "))) {
                        objectArray = string2.substring(string2.charAt(1) == ' ' ? 2 : 7).trim();
                        lC.setHeight(ConstraintParser.parseBoundSize((String)objectArray, false, false));
                        continue;
                    }
                    if (string2.length() > 5) {
                        objectArray = string2.substring(5).trim();
                        if (string2.startsWith("wmin ")) {
                            lC.minWidth((String)objectArray);
                            continue;
                        }
                        if (string2.startsWith("wmax ")) {
                            lC.maxWidth((String)objectArray);
                            continue;
                        }
                        if (string2.startsWith("hmin ")) {
                            lC.minHeight((String)objectArray);
                            continue;
                        }
                        if (string2.startsWith("hmax ")) {
                            lC.maxHeight((String)objectArray);
                            continue;
                        }
                    }
                    if (string2.startsWith("hidemode ")) {
                        lC.setHideMode(Integer.parseInt(string2.substring(9)));
                        continue;
                    }
                }
                if (c2 == 'g') {
                    if (string2.startsWith("gapx ")) {
                        lC.setGridGapX(ConstraintParser.parseBoundSize(string2.substring(5).trim(), true, true));
                        continue;
                    }
                    if (string2.startsWith("gapy ")) {
                        lC.setGridGapY(ConstraintParser.parseBoundSize(string2.substring(5).trim(), true, false));
                        continue;
                    }
                    if (string2.startsWith("gap ")) {
                        String[] stringArray2 = ConstraintParser.toTrimmedTokens(string2.substring(4).trim(), ' ');
                        lC.setGridGapX(ConstraintParser.parseBoundSize(stringArray2[0], true, true));
                        lC.setGridGapY(stringArray2.length > 1 ? ConstraintParser.parseBoundSize(stringArray2[1], true, false) : lC.getGridGapX());
                        continue;
                    }
                }
                if (c2 == 'd' && (n = ConstraintParser.startsWithLenient(string2, "debug", 5, true)) > -1) {
                    String string4 = string2.substring(n).trim();
                    lC.setDebugMillis(string4.length() > 0 ? Integer.parseInt(string4) : 1000);
                    continue;
                }
                if (c2 == 'n') {
                    if (string2.equals("nogrid")) {
                        lC.setNoGrid(true);
                        continue;
                    }
                    if (string2.equals("nocache")) {
                        lC.setNoCache(true);
                        continue;
                    }
                    if (string2.equals("novisualpadding")) {
                        lC.setVisualPadding(false);
                        continue;
                    }
                }
                if (c2 == 'f') {
                    if (string2.equals("fill") || string2.equals("fillx") || string2.equals("filly")) {
                        lC.setFillX(string2.length() == 4 || string2.charAt(4) == 'x');
                        lC.setFillY(string2.length() == 4 || string2.charAt(4) == 'y');
                        continue;
                    }
                    if (string2.equals("flowy")) {
                        lC.setFlowX(false);
                        continue;
                    }
                    if (string2.equals("flowx")) {
                        lC.setFlowX(true);
                        continue;
                    }
                }
                if (c2 == 'i' && (n = ConstraintParser.startsWithLenient(string2, "insets", 3, true)) > -1) {
                    String string5 = string2.substring(n).trim();
                    objectArray = ConstraintParser.parseInsets(string5, true);
                    LayoutUtil.putCCString(objectArray, string5);
                    lC.setInsets((UnitValue[])objectArray);
                    continue;
                }
                if (c2 == 'a') {
                    n = ConstraintParser.startsWithLenient(string2, new String[]{"aligny", "ay"}, new int[]{6, 2}, true);
                    if (n > -1) {
                        UnitValue unitValue2 = ConstraintParser.parseUnitValueOrAlign(string2.substring(n).trim(), false, null);
                        if (unitValue2 == UnitValue.BASELINE_IDENTITY) {
                            throw new IllegalArgumentException("'baseline' can not be used to align the whole component group.");
                        }
                        lC.setAlignY(unitValue2);
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, new String[]{"alignx", "ax"}, new int[]{6, 2}, true);
                    if (n > -1) {
                        lC.setAlignX(ConstraintParser.parseUnitValueOrAlign(string2.substring(n).trim(), true, null));
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, "align", 2, true);
                    if (n > -1) {
                        String[] stringArray3 = ConstraintParser.toTrimmedTokens(string2.substring(n).trim(), ' ');
                        lC.setAlignX(ConstraintParser.parseUnitValueOrAlign(stringArray3[0], true, null));
                        lC.setAlignY(stringArray3.length > 1 ? ConstraintParser.parseUnitValueOrAlign(stringArray3[1], false, null) : lC.getAlignX());
                        continue;
                    }
                }
                if (c2 == 'p') {
                    if (string2.startsWith("packalign ")) {
                        String[] stringArray4 = ConstraintParser.toTrimmedTokens(string2.substring(10).trim(), ' ');
                        lC.setPackWidthAlign(stringArray4[0].length() > 0 ? Float.parseFloat(stringArray4[0]) : 0.5f);
                        if (stringArray4.length > 1) {
                            lC.setPackHeightAlign(Float.parseFloat(stringArray4[1]));
                        }
                        continue;
                    }
                    if (string2.startsWith("pack ") || string2.equals("pack")) {
                        String string6 = string2.substring(4).trim();
                        objectArray = ConstraintParser.toTrimmedTokens(string6.length() > 0 ? string6 : "pref pref", ' ');
                        lC.setPackWidth(ConstraintParser.parseBoundSize((String)objectArray[0], false, true));
                        if (objectArray.length > 1) {
                            lC.setPackHeight(ConstraintParser.parseBoundSize((String)objectArray[1], false, false));
                        }
                        continue;
                    }
                }
                if (lC.getAlignX() == null && (unitValue = ConstraintParser.parseAlignKeywords(string2, true)) != null) {
                    lC.setAlignX(unitValue);
                    continue;
                }
                UnitValue unitValue3 = ConstraintParser.parseAlignKeywords(string2, false);
                if (unitValue3 != null) {
                    lC.setAlignY(unitValue3);
                    continue;
                }
                throw new IllegalArgumentException("Unknown Constraint: '" + string2 + "'\n");
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("Illegal Constraint: '" + string2 + "'\n" + exception.getMessage());
            }
        }
        return lC;
    }

    public static AC parseRowConstraints(String string) {
        return ConstraintParser.parseAxisConstraint(string, false);
    }

    public static AC parseColumnConstraints(String string) {
        return ConstraintParser.parseAxisConstraint(string, true);
    }

    private static AC parseAxisConstraint(String string, boolean bl) {
        if ((string = string.trim()).length() == 0) {
            return new AC();
        }
        string = string.toLowerCase();
        ArrayList<String> arrayList = ConstraintParser.getRowColAndGapsTrimmed(string);
        BoundSize[] boundSizeArray = new BoundSize[(arrayList.size() >> 1) + 1];
        int n = 0;
        int n2 = arrayList.size();
        int n3 = 0;
        while (n < n2) {
            boundSizeArray[n3] = ConstraintParser.parseBoundSize(arrayList.get(n), true, bl);
            n += 2;
            ++n3;
        }
        DimConstraint[] dimConstraintArray = new DimConstraint[arrayList.size() >> 1];
        n2 = 0;
        n3 = 0;
        while (n2 < dimConstraintArray.length) {
            if (n3 >= boundSizeArray.length - 1) {
                n3 = boundSizeArray.length - 2;
            }
            dimConstraintArray[n2] = ConstraintParser.parseDimConstraint(arrayList.get((n2 << 1) + 1), boundSizeArray[n3], boundSizeArray[n3 + 1], bl);
            ++n2;
            ++n3;
        }
        AC aC = new AC();
        aC.setConstaints(dimConstraintArray);
        return aC;
    }

    private static DimConstraint parseDimConstraint(String string, BoundSize boundSize, BoundSize boundSize2, boolean bl) {
        DimConstraint dimConstraint = new DimConstraint();
        dimConstraint.setGapBefore(boundSize);
        dimConstraint.setGapAfter(boundSize2);
        String[] stringArray = ConstraintParser.toTrimmedTokens(string, ',');
        for (int i = 0; i < stringArray.length; ++i) {
            String string2 = stringArray[i];
            try {
                if (string2.length() == 0) continue;
                if (string2.equals("fill")) {
                    dimConstraint.setFill(true);
                    continue;
                }
                if (string2.equals("nogrid")) {
                    dimConstraint.setNoGrid(true);
                    continue;
                }
                int n = -1;
                char c2 = string2.charAt(0);
                if (c2 == 's') {
                    n = ConstraintParser.startsWithLenient(string2, new String[]{"sizegroup", "sg"}, new int[]{5, 2}, true);
                    if (n > -1) {
                        dimConstraint.setSizeGroup(string2.substring(n).trim());
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, new String[]{"shrinkprio", "shp"}, new int[]{10, 3}, true);
                    if (n > -1) {
                        dimConstraint.setShrinkPriority(Integer.parseInt(string2.substring(n).trim()));
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, "shrink", 6, true);
                    if (n > -1) {
                        dimConstraint.setShrink(ConstraintParser.parseFloat(string2.substring(n).trim(), ResizeConstraint.WEIGHT_100));
                        continue;
                    }
                }
                if (c2 == 'g') {
                    n = ConstraintParser.startsWithLenient(string2, new String[]{"growpriority", "gp"}, new int[]{5, 2}, true);
                    if (n > -1) {
                        dimConstraint.setGrowPriority(Integer.parseInt(string2.substring(n).trim()));
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, "grow", 4, true);
                    if (n > -1) {
                        dimConstraint.setGrow(ConstraintParser.parseFloat(string2.substring(n).trim(), ResizeConstraint.WEIGHT_100));
                        continue;
                    }
                }
                if (c2 == 'a' && (n = ConstraintParser.startsWithLenient(string2, "align", 2, true)) > -1) {
                    dimConstraint.setAlign(ConstraintParser.parseUnitValueOrAlign(string2.substring(n).trim(), bl, null));
                    continue;
                }
                UnitValue unitValue = ConstraintParser.parseAlignKeywords(string2, bl);
                if (unitValue != null) {
                    dimConstraint.setAlign(unitValue);
                    continue;
                }
                dimConstraint.setSize(ConstraintParser.parseBoundSize(string2, false, bl));
                continue;
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("Illegal contraint: '" + string2 + "'\n" + exception.getMessage());
            }
        }
        return dimConstraint;
    }

    public static Map<ComponentWrapper, CC> parseComponentConstraints(Map<ComponentWrapper, String> map) {
        HashMap<ComponentWrapper, CC> hashMap = new HashMap<ComponentWrapper, CC>();
        for (Map.Entry<ComponentWrapper, String> entry : map.entrySet()) {
            hashMap.put(entry.getKey(), ConstraintParser.parseComponentConstraint(entry.getValue()));
        }
        return hashMap;
    }

    public static CC parseComponentConstraint(String string) {
        CC cC = new CC();
        if (string.length() == 0) {
            return cC;
        }
        String[] stringArray = ConstraintParser.toTrimmedTokens(string, ',');
        for (int i = 0; i < stringArray.length; ++i) {
            String string2 = stringArray[i];
            try {
                UnitValue unitValue;
                char c2;
                if (string2.length() == 0) continue;
                int n = -1;
                char c3 = string2.charAt(0);
                if (c3 == 'n') {
                    if (string2.equals("north")) {
                        cC.setDockSide(0);
                        continue;
                    }
                    if (string2.equals("newline")) {
                        cC.setNewline(true);
                        continue;
                    }
                    if (string2.startsWith("newline ")) {
                        String string3 = string2.substring(7).trim();
                        cC.setNewlineGapSize(ConstraintParser.parseBoundSize(string3, true, true));
                        continue;
                    }
                }
                if (c3 == 'f' && (string2.equals("flowy") || string2.equals("flowx"))) {
                    cC.setFlowX(string2.charAt(4) == 'x' ? Boolean.TRUE : Boolean.FALSE);
                    continue;
                }
                if (c3 == 's') {
                    n = ConstraintParser.startsWithLenient(string2, "skip", 4, true);
                    if (n > -1) {
                        String string4 = string2.substring(n).trim();
                        cC.setSkip(string4.length() != 0 ? Integer.parseInt(string4) : 1);
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, "split", 5, true);
                    if (n > -1) {
                        String string5 = string2.substring(n).trim();
                        cC.setSplit(string5.length() > 0 ? Integer.parseInt(string5) : 2097051);
                        continue;
                    }
                    if (string2.equals("south")) {
                        cC.setDockSide(2);
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, new String[]{"spany", "sy"}, new int[]{5, 2}, true);
                    if (n > -1) {
                        cC.setSpanY(ConstraintParser.parseSpan(string2.substring(n).trim()));
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, new String[]{"spanx", "sx"}, new int[]{5, 2}, true);
                    if (n > -1) {
                        cC.setSpanX(ConstraintParser.parseSpan(string2.substring(n).trim()));
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, "span", 4, true);
                    if (n > -1) {
                        String[] stringArray2 = ConstraintParser.toTrimmedTokens(string2.substring(n).trim(), ' ');
                        cC.setSpanX(stringArray2[0].length() > 0 ? Integer.parseInt(stringArray2[0]) : 2097051);
                        cC.setSpanY(stringArray2.length > 1 ? Integer.parseInt(stringArray2[1]) : 1);
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, "shrinkx", 7, true);
                    if (n > -1) {
                        cC.getHorizontal().setShrink(ConstraintParser.parseFloat(string2.substring(n).trim(), ResizeConstraint.WEIGHT_100));
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, "shrinky", 7, true);
                    if (n > -1) {
                        cC.getVertical().setShrink(ConstraintParser.parseFloat(string2.substring(n).trim(), ResizeConstraint.WEIGHT_100));
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, "shrink", 6, false);
                    if (n > -1) {
                        String[] stringArray3 = ConstraintParser.toTrimmedTokens(string2.substring(n).trim(), ' ');
                        cC.getHorizontal().setShrink(ConstraintParser.parseFloat(string2.substring(n).trim(), ResizeConstraint.WEIGHT_100));
                        if (stringArray3.length > 1) {
                            cC.getVertical().setShrink(ConstraintParser.parseFloat(string2.substring(n).trim(), ResizeConstraint.WEIGHT_100));
                        }
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, new String[]{"shrinkprio", "shp"}, new int[]{10, 3}, true);
                    if (n > -1) {
                        String string6 = string2.substring(n).trim();
                        if (string6.startsWith("x") || string6.startsWith("y")) {
                            (string6.startsWith("x") ? cC.getHorizontal() : cC.getVertical()).setShrinkPriority(Integer.parseInt(string6.substring(2)));
                            continue;
                        }
                        String[] stringArray4 = ConstraintParser.toTrimmedTokens(string6, ' ');
                        cC.getHorizontal().setShrinkPriority(Integer.parseInt(stringArray4[0]));
                        if (stringArray4.length <= 1) continue;
                        cC.getVertical().setShrinkPriority(Integer.parseInt(stringArray4[1]));
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, new String[]{"sizegroupx", "sizegroupy", "sgx", "sgy"}, new int[]{9, 9, 2, 2}, true);
                    if (n > -1) {
                        String string7 = string2.substring(n).trim();
                        char c4 = string2.charAt(n - 1);
                        if (c4 != 'y') {
                            cC.getHorizontal().setSizeGroup(string7);
                        }
                        if (c4 != 'x') {
                            cC.getVertical().setSizeGroup(string7);
                        }
                        continue;
                    }
                }
                if (c3 == 'g') {
                    n = ConstraintParser.startsWithLenient(string2, "growx", 5, true);
                    if (n > -1) {
                        cC.getHorizontal().setGrow(ConstraintParser.parseFloat(string2.substring(n).trim(), ResizeConstraint.WEIGHT_100));
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, "growy", 5, true);
                    if (n > -1) {
                        cC.getVertical().setGrow(ConstraintParser.parseFloat(string2.substring(n).trim(), ResizeConstraint.WEIGHT_100));
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, "grow", 4, false);
                    if (n > -1) {
                        String[] stringArray5 = ConstraintParser.toTrimmedTokens(string2.substring(n).trim(), ' ');
                        cC.getHorizontal().setGrow(ConstraintParser.parseFloat(stringArray5[0], ResizeConstraint.WEIGHT_100));
                        cC.getVertical().setGrow(ConstraintParser.parseFloat(stringArray5.length > 1 ? stringArray5[1] : "", ResizeConstraint.WEIGHT_100));
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, new String[]{"growprio", "gp"}, new int[]{8, 2}, true);
                    if (n > -1) {
                        int n2;
                        String string8 = string2.substring(n).trim();
                        int n3 = n2 = string8.length() > 0 ? (int)string8.charAt(0) : 32;
                        if (n2 == 120 || n2 == 121) {
                            (n2 == 120 ? cC.getHorizontal() : cC.getVertical()).setGrowPriority(Integer.parseInt(string8.substring(2)));
                            continue;
                        }
                        String[] stringArray6 = ConstraintParser.toTrimmedTokens(string8, ' ');
                        cC.getHorizontal().setGrowPriority(Integer.parseInt(stringArray6[0]));
                        if (stringArray6.length <= 1) continue;
                        cC.getVertical().setGrowPriority(Integer.parseInt(stringArray6[1]));
                        continue;
                    }
                    if (string2.startsWith("gap")) {
                        BoundSize[] boundSizeArray = ConstraintParser.parseGaps(string2);
                        if (boundSizeArray[0] != null) {
                            cC.getVertical().setGapBefore(boundSizeArray[0]);
                        }
                        if (boundSizeArray[1] != null) {
                            cC.getHorizontal().setGapBefore(boundSizeArray[1]);
                        }
                        if (boundSizeArray[2] != null) {
                            cC.getVertical().setGapAfter(boundSizeArray[2]);
                        }
                        if (boundSizeArray[3] != null) {
                            cC.getHorizontal().setGapAfter(boundSizeArray[3]);
                        }
                        continue;
                    }
                }
                if (c3 == 'a') {
                    n = ConstraintParser.startsWithLenient(string2, new String[]{"aligny", "ay"}, new int[]{6, 2}, true);
                    if (n > -1) {
                        cC.getVertical().setAlign(ConstraintParser.parseUnitValueOrAlign(string2.substring(n).trim(), false, null));
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, new String[]{"alignx", "ax"}, new int[]{6, 2}, true);
                    if (n > -1) {
                        cC.getHorizontal().setAlign(ConstraintParser.parseUnitValueOrAlign(string2.substring(n).trim(), true, null));
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, "align", 2, true);
                    if (n > -1) {
                        String[] stringArray7 = ConstraintParser.toTrimmedTokens(string2.substring(n).trim(), ' ');
                        cC.getHorizontal().setAlign(ConstraintParser.parseUnitValueOrAlign(stringArray7[0], true, null));
                        if (stringArray7.length > 1) {
                            cC.getVertical().setAlign(ConstraintParser.parseUnitValueOrAlign(stringArray7[1], false, null));
                        }
                        continue;
                    }
                }
                if ((c3 == 'x' || c3 == 'y') && string2.length() > 2 && ((c2 = string2.charAt(1)) == ' ' || c2 == '2' && string2.charAt(2) == ' ')) {
                    if (cC.getPos() == null) {
                        cC.setPos(new UnitValue[4]);
                    } else if (!cC.isBoundsInGrid()) {
                        throw new IllegalArgumentException("Cannot combine 'position' with 'x/y/x2/y2' keywords.");
                    }
                    int n4 = (c3 == 'x' ? 0 : 1) + (c2 == '2' ? 2 : 0);
                    UnitValue[] unitValueArray = cC.getPos();
                    unitValueArray[n4] = ConstraintParser.parseUnitValue(string2.substring(2).trim(), null, c3 == 'x');
                    cC.setPos(unitValueArray);
                    cC.setBoundsInGrid(true);
                    continue;
                }
                if (c3 == 'c' && (n = ConstraintParser.startsWithLenient(string2, "cell", 4, true)) > -1) {
                    String[] stringArray8 = ConstraintParser.toTrimmedTokens(string2.substring(n).trim(), ' ');
                    if (stringArray8.length < 2) {
                        throw new IllegalArgumentException("At least two integers must follow " + string2);
                    }
                    cC.setCellX(Integer.parseInt(stringArray8[0]));
                    cC.setCellY(Integer.parseInt(stringArray8[1]));
                    if (stringArray8.length > 2) {
                        cC.setSpanX(Integer.parseInt(stringArray8[2]));
                    }
                    if (stringArray8.length > 3) {
                        cC.setSpanY(Integer.parseInt(stringArray8[3]));
                    }
                    continue;
                }
                if (c3 == 'p') {
                    n = ConstraintParser.startsWithLenient(string2, "pos", 3, true);
                    if (n > -1) {
                        if (cC.getPos() != null && cC.isBoundsInGrid()) {
                            throw new IllegalArgumentException("Can not combine 'pos' with 'x/y/x2/y2' keywords.");
                        }
                        String[] stringArray9 = ConstraintParser.toTrimmedTokens(string2.substring(n).trim(), ' ');
                        UnitValue[] unitValueArray = new UnitValue[4];
                        for (int j = 0; j < stringArray9.length; ++j) {
                            unitValueArray[j] = ConstraintParser.parseUnitValue(stringArray9[j], null, j % 2 == 0);
                        }
                        if (unitValueArray[0] == null && unitValueArray[2] == null || unitValueArray[1] == null && unitValueArray[3] == null) {
                            throw new IllegalArgumentException("Both x and x2 or y and y2 can not be null!");
                        }
                        cC.setPos(unitValueArray);
                        cC.setBoundsInGrid(false);
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, "pad", 3, true);
                    if (n > -1) {
                        UnitValue[] unitValueArray = ConstraintParser.parseInsets(string2.substring(n).trim(), false);
                        cC.setPadding(new UnitValue[]{unitValueArray[0], unitValueArray.length > 1 ? unitValueArray[1] : null, unitValueArray.length > 2 ? unitValueArray[2] : null, unitValueArray.length > 3 ? unitValueArray[3] : null});
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, "pushx", 5, true);
                    if (n > -1) {
                        cC.setPushX(ConstraintParser.parseFloat(string2.substring(n).trim(), ResizeConstraint.WEIGHT_100));
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, "pushy", 5, true);
                    if (n > -1) {
                        cC.setPushY(ConstraintParser.parseFloat(string2.substring(n).trim(), ResizeConstraint.WEIGHT_100));
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, "push", 4, false);
                    if (n > -1) {
                        String[] stringArray10 = ConstraintParser.toTrimmedTokens(string2.substring(n).trim(), ' ');
                        cC.setPushX(ConstraintParser.parseFloat(stringArray10[0], ResizeConstraint.WEIGHT_100));
                        cC.setPushY(ConstraintParser.parseFloat(stringArray10.length > 1 ? stringArray10[1] : "", ResizeConstraint.WEIGHT_100));
                        continue;
                    }
                }
                if (c3 == 't' && (n = ConstraintParser.startsWithLenient(string2, "tag", 3, true)) > -1) {
                    cC.setTag(string2.substring(n).trim());
                    continue;
                }
                if (c3 == 'w' || c3 == 'h') {
                    String string9;
                    boolean bl;
                    if (string2.equals("wrap")) {
                        cC.setWrap(true);
                        continue;
                    }
                    if (string2.startsWith("wrap ")) {
                        String string10 = string2.substring(5).trim();
                        cC.setWrapGapSize(ConstraintParser.parseBoundSize(string10, true, true));
                        continue;
                    }
                    boolean bl2 = bl = c3 == 'w';
                    if (bl && (string2.startsWith("w ") || string2.startsWith("width "))) {
                        String string11 = string2.substring(string2.charAt(1) == ' ' ? 2 : 6).trim();
                        cC.getHorizontal().setSize(ConstraintParser.parseBoundSize(string11, false, true));
                        continue;
                    }
                    if (!bl && (string2.startsWith("h ") || string2.startsWith("height "))) {
                        String string12 = string2.substring(string2.charAt(1) == ' ' ? 2 : 7).trim();
                        cC.getVertical().setSize(ConstraintParser.parseBoundSize(string12, false, false));
                        continue;
                    }
                    if ((string2.startsWith("wmin ") || string2.startsWith("wmax ") || string2.startsWith("hmin ") || string2.startsWith("hmax ")) && (string9 = string2.substring(5).trim()).length() > 0) {
                        UnitValue unitValue2 = ConstraintParser.parseUnitValue(string9, null, bl);
                        boolean bl3 = string2.charAt(3) == 'n';
                        DimConstraint dimConstraint = bl ? cC.getHorizontal() : cC.getVertical();
                        dimConstraint.setSize(new BoundSize(bl3 ? unitValue2 : dimConstraint.getSize().getMin(), dimConstraint.getSize().getPreferred(), bl3 ? dimConstraint.getSize().getMax() : unitValue2, string9));
                        continue;
                    }
                    if (string2.equals("west")) {
                        cC.setDockSide(1);
                        continue;
                    }
                    if (string2.startsWith("hidemode ")) {
                        cC.setHideMode(Integer.parseInt(string2.substring(9)));
                        continue;
                    }
                }
                if (c3 == 'i' && string2.startsWith("id ")) {
                    cC.setId(string2.substring(3).trim());
                    int n5 = cC.getId().indexOf(46);
                    if (n5 != 0 && n5 != cC.getId().length() - 1) continue;
                    throw new IllegalArgumentException("Dot must not be first or last!");
                }
                if (c3 == 'e') {
                    if (string2.equals("east")) {
                        cC.setDockSide(3);
                        continue;
                    }
                    if (string2.equals("external")) {
                        cC.setExternal(true);
                        continue;
                    }
                    n = ConstraintParser.startsWithLenient(string2, new String[]{"endgroupx", "endgroupy", "egx", "egy"}, new int[]{-1, -1, -1, -1}, true);
                    if (n > -1) {
                        String string13 = string2.substring(n).trim();
                        char c5 = string2.charAt(n - 1);
                        DimConstraint dimConstraint = c5 == 'x' ? cC.getHorizontal() : cC.getVertical();
                        dimConstraint.setEndGroup(string13);
                        continue;
                    }
                }
                if (c3 == 'd') {
                    if (string2.equals("dock north")) {
                        cC.setDockSide(0);
                        continue;
                    }
                    if (string2.equals("dock west")) {
                        cC.setDockSide(1);
                        continue;
                    }
                    if (string2.equals("dock south")) {
                        cC.setDockSide(2);
                        continue;
                    }
                    if (string2.equals("dock east")) {
                        cC.setDockSide(3);
                        continue;
                    }
                    if (string2.equals("dock center")) {
                        cC.getHorizontal().setGrow(new Float(100.0f));
                        cC.getVertical().setGrow(new Float(100.0f));
                        cC.setPushX(new Float(100.0f));
                        cC.setPushY(new Float(100.0f));
                        continue;
                    }
                }
                if ((unitValue = ConstraintParser.parseAlignKeywords(string2, true)) != null) {
                    cC.getHorizontal().setAlign(unitValue);
                    continue;
                }
                UnitValue unitValue3 = ConstraintParser.parseAlignKeywords(string2, false);
                if (unitValue3 != null) {
                    cC.getVertical().setAlign(unitValue3);
                    continue;
                }
                throw new IllegalArgumentException("Unknown keyword.");
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("Illegal Constraint: '" + string2 + "'\n" + exception.getMessage());
            }
        }
        return cC;
    }

    public static UnitValue[] parseInsets(String string, boolean bl) {
        if (string.length() == 0 || string.equals("dialog") || string.equals("panel")) {
            if (!bl) {
                throw new IllegalAccessError("Insets now allowed: " + string + "\n");
            }
            boolean bl2 = string.startsWith("p");
            UnitValue[] unitValueArray = new UnitValue[4];
            for (int i = 0; i < 4; ++i) {
                unitValueArray[i] = bl2 ? PlatformDefaults.getPanelInsets(i) : PlatformDefaults.getDialogInsets(i);
            }
            return unitValueArray;
        }
        String[] stringArray = ConstraintParser.toTrimmedTokens(string, ' ');
        UnitValue[] unitValueArray = new UnitValue[4];
        for (int i = 0; i < 4; ++i) {
            UnitValue unitValue = ConstraintParser.parseUnitValue(stringArray[i < stringArray.length ? i : stringArray.length - 1], UnitValue.ZERO, i % 2 == 1);
            unitValueArray[i] = unitValue != null ? unitValue : PlatformDefaults.getPanelInsets(i);
        }
        return unitValueArray;
    }

    private static BoundSize[] parseGaps(String string) {
        BoundSize[] boundSizeArray = new BoundSize[4];
        int n = ConstraintParser.startsWithLenient(string, "gaptop", -1, true);
        if (n > -1) {
            string = string.substring(n).trim();
            boundSizeArray[0] = ConstraintParser.parseBoundSize(string, true, false);
            return boundSizeArray;
        }
        n = ConstraintParser.startsWithLenient(string, "gapleft", -1, true);
        if (n > -1) {
            string = string.substring(n).trim();
            boundSizeArray[1] = ConstraintParser.parseBoundSize(string, true, true);
            return boundSizeArray;
        }
        n = ConstraintParser.startsWithLenient(string, "gapbottom", -1, true);
        if (n > -1) {
            string = string.substring(n).trim();
            boundSizeArray[2] = ConstraintParser.parseBoundSize(string, true, false);
            return boundSizeArray;
        }
        n = ConstraintParser.startsWithLenient(string, "gapright", -1, true);
        if (n > -1) {
            string = string.substring(n).trim();
            boundSizeArray[3] = ConstraintParser.parseBoundSize(string, true, true);
            return boundSizeArray;
        }
        n = ConstraintParser.startsWithLenient(string, "gapbefore", -1, true);
        if (n > -1) {
            string = string.substring(n).trim();
            boundSizeArray[1] = ConstraintParser.parseBoundSize(string, true, true);
            return boundSizeArray;
        }
        n = ConstraintParser.startsWithLenient(string, "gapafter", -1, true);
        if (n > -1) {
            string = string.substring(n).trim();
            boundSizeArray[3] = ConstraintParser.parseBoundSize(string, true, true);
            return boundSizeArray;
        }
        n = ConstraintParser.startsWithLenient(string, new String[]{"gapx", "gapy"}, null, true);
        if (n > -1) {
            boolean bl = string.charAt(3) == 'x';
            String[] stringArray = ConstraintParser.toTrimmedTokens(string.substring(n).trim(), ' ');
            boundSizeArray[bl ? 1 : 0] = ConstraintParser.parseBoundSize(stringArray[0], true, bl);
            if (stringArray.length > 1) {
                boundSizeArray[bl ? 3 : 2] = ConstraintParser.parseBoundSize(stringArray[1], true, !bl);
            }
            return boundSizeArray;
        }
        n = ConstraintParser.startsWithLenient(string, "gap ", 1, true);
        if (n > -1) {
            String[] stringArray = ConstraintParser.toTrimmedTokens(string.substring(n).trim(), ' ');
            boundSizeArray[1] = ConstraintParser.parseBoundSize(stringArray[0], true, true);
            if (stringArray.length > 1) {
                boundSizeArray[3] = ConstraintParser.parseBoundSize(stringArray[1], true, false);
                if (stringArray.length > 2) {
                    boundSizeArray[0] = ConstraintParser.parseBoundSize(stringArray[2], true, true);
                    if (stringArray.length > 3) {
                        boundSizeArray[2] = ConstraintParser.parseBoundSize(stringArray[3], true, false);
                    }
                }
            }
            return boundSizeArray;
        }
        throw new IllegalArgumentException("Unknown Gap part: '" + string + "'");
    }

    private static int parseSpan(String string) {
        return string.length() > 0 ? Integer.parseInt(string) : 2097051;
    }

    private static Float parseFloat(String string, Float f) {
        return string.length() > 0 ? new Float(Float.parseFloat(string)) : f;
    }

    public static BoundSize parseBoundSize(String string, boolean bl, boolean bl2) {
        if (string.length() == 0 || string.equals("null") || string.equals("n")) {
            return null;
        }
        String string2 = string;
        boolean bl3 = false;
        if (string.endsWith("push")) {
            bl3 = true;
            int n = string.length();
            if ((string = string.substring(0, n - (string.endsWith(":push") ? 5 : 4))).length() == 0) {
                return new BoundSize(null, null, null, true, string2);
            }
        }
        String[] stringArray = ConstraintParser.toTrimmedTokens(string, ':');
        String string3 = stringArray[0];
        if (stringArray.length == 1) {
            boolean bl4 = string3.endsWith("!");
            if (bl4) {
                string3 = string3.substring(0, string3.length() - 1);
            }
            UnitValue unitValue = ConstraintParser.parseUnitValue(string3, null, bl2);
            return new BoundSize(bl || bl4 ? unitValue : null, unitValue, bl4 ? unitValue : null, bl3, string2);
        }
        if (stringArray.length == 2) {
            return new BoundSize(ConstraintParser.parseUnitValue(string3, null, bl2), ConstraintParser.parseUnitValue(stringArray[1], null, bl2), null, bl3, string2);
        }
        if (stringArray.length == 3) {
            return new BoundSize(ConstraintParser.parseUnitValue(string3, null, bl2), ConstraintParser.parseUnitValue(stringArray[1], null, bl2), ConstraintParser.parseUnitValue(stringArray[2], null, bl2), bl3, string2);
        }
        throw new IllegalArgumentException("Min:Preferred:Max size section must contain 0, 1 or 2 colons. '" + string2 + "'");
    }

    public static UnitValue parseUnitValueOrAlign(String string, boolean bl, UnitValue unitValue) {
        if (string.length() == 0) {
            return unitValue;
        }
        UnitValue unitValue2 = ConstraintParser.parseAlignKeywords(string, bl);
        if (unitValue2 != null) {
            return unitValue2;
        }
        return ConstraintParser.parseUnitValue(string, unitValue, bl);
    }

    public static UnitValue parseUnitValue(String string, boolean bl) {
        return ConstraintParser.parseUnitValue(string, null, bl);
    }

    private static UnitValue parseUnitValue(String string, UnitValue unitValue, boolean bl) {
        boolean bl2;
        if (string == null || string.length() == 0) {
            return unitValue;
        }
        String string2 = string;
        char c2 = string.charAt(0);
        if (c2 == '(' && string.charAt(string.length() - 1) == ')') {
            string = string.substring(1, string.length() - 1);
        }
        if (c2 == 'n' && (string.equals("null") || string.equals("n"))) {
            return null;
        }
        if (c2 == 'i' && string.equals("inf")) {
            return UnitValue.INF;
        }
        int n = ConstraintParser.getOper(string);
        boolean bl3 = bl2 = n == 101 || n == 102 || n == 103 || n == 104;
        if (n != 100) {
            Object object;
            String[] stringArray;
            Object object2;
            if (!bl2) {
                object2 = string.substring(4, string.length() - 1).trim();
                stringArray = ConstraintParser.toTrimmedTokens((String)object2, ',');
                if (stringArray.length == 1) {
                    return ConstraintParser.parseUnitValue((String)object2, null, bl);
                }
            } else {
                int n2 = n == 101 ? 43 : (n == 102 ? 45 : (n == 103 ? 42 : 47));
                stringArray = ConstraintParser.toTrimmedTokens(string, (char)n2);
                if (stringArray.length > 2) {
                    object = stringArray[stringArray.length - 1];
                    String string3 = string.substring(0, string.length() - ((String)object).length() - 1);
                    stringArray = new String[]{string3, object};
                }
            }
            if (stringArray.length != 2) {
                throw new IllegalArgumentException("Malformed UnitValue: '" + string + "'");
            }
            object2 = ConstraintParser.parseUnitValue(stringArray[0], null, bl);
            object = ConstraintParser.parseUnitValue(stringArray[1], null, bl);
            if (object2 == null || object == null) {
                throw new IllegalArgumentException("Malformed UnitValue. Must be two sub-values: '" + string + "'");
            }
            return new UnitValue(bl, n, (UnitValue)object2, (UnitValue)object, string2);
        }
        try {
            String[] stringArray = ConstraintParser.getNumTextParts(string);
            float f = stringArray[0].length() > 0 ? Float.parseFloat(stringArray[0]) : 1.0f;
            return new UnitValue(f, stringArray[1], bl, n, string2);
        }
        catch (Exception exception) {
            throw new IllegalArgumentException("Malformed UnitValue: '" + string + "'");
        }
    }

    static UnitValue parseAlignKeywords(String string, boolean bl) {
        if (ConstraintParser.startsWithLenient(string, "center", 1, false) != -1) {
            return UnitValue.CENTER;
        }
        if (bl) {
            if (ConstraintParser.startsWithLenient(string, "left", 1, false) != -1) {
                return UnitValue.LEFT;
            }
            if (ConstraintParser.startsWithLenient(string, "right", 1, false) != -1) {
                return UnitValue.RIGHT;
            }
            if (ConstraintParser.startsWithLenient(string, "leading", 4, false) != -1) {
                return UnitValue.LEADING;
            }
            if (ConstraintParser.startsWithLenient(string, "trailing", 5, false) != -1) {
                return UnitValue.TRAILING;
            }
            if (ConstraintParser.startsWithLenient(string, "label", 5, false) != -1) {
                return UnitValue.LABEL;
            }
        } else {
            if (ConstraintParser.startsWithLenient(string, "baseline", 4, false) != -1) {
                return UnitValue.BASELINE_IDENTITY;
            }
            if (ConstraintParser.startsWithLenient(string, "top", 1, false) != -1) {
                return UnitValue.TOP;
            }
            if (ConstraintParser.startsWithLenient(string, "bottom", 1, false) != -1) {
                return UnitValue.BOTTOM;
            }
        }
        return null;
    }

    private static String[] getNumTextParts(String string) {
        int n = string.length();
        for (int i = 0; i < n; ++i) {
            char c2 = string.charAt(i);
            if (c2 == ' ') {
                throw new IllegalArgumentException("Space in UnitValue: '" + string + "'");
            }
            if (c2 >= '0' && c2 <= '9' || c2 == '.' || c2 == '-') continue;
            return new String[]{string.substring(0, i).trim(), string.substring(i).trim()};
        }
        return new String[]{string, ""};
    }

    private static int getOper(String string) {
        int n = string.length();
        if (n < 3) {
            return 100;
        }
        if (n > 5 && string.charAt(3) == '(' && string.charAt(n - 1) == ')') {
            if (string.startsWith("min(")) {
                return 105;
            }
            if (string.startsWith("max(")) {
                return 106;
            }
            if (string.startsWith("mid(")) {
                return 107;
            }
        }
        for (int i = 0; i < 2; ++i) {
            int n2 = 0;
            for (int j = n - 1; j > 0; --j) {
                char c2 = string.charAt(j);
                if (c2 == ')') {
                    ++n2;
                    continue;
                }
                if (c2 == '(') {
                    --n2;
                    continue;
                }
                if (n2 != 0) continue;
                if (i == 0) {
                    if (c2 == '+') {
                        return 101;
                    }
                    if (c2 != '-') continue;
                    return 102;
                }
                if (c2 == '*') {
                    return 103;
                }
                if (c2 != '/') continue;
                return 104;
            }
        }
        return 100;
    }

    private static int startsWithLenient(String string, String[] stringArray, int[] nArray, boolean bl) {
        for (int i = 0; i < stringArray.length; ++i) {
            int n = nArray != null ? nArray[i] : -1;
            int n2 = ConstraintParser.startsWithLenient(string, stringArray[i], n, bl);
            if (n2 <= -1) continue;
            return n2;
        }
        return -1;
    }

    private static int startsWithLenient(String string, String string2, int n, boolean bl) {
        int n2;
        if (string.charAt(0) != string2.charAt(0)) {
            return -1;
        }
        if (n == -1) {
            n = string2.length();
        }
        if ((n2 = string.length()) < n) {
            return -1;
        }
        int n3 = string2.length();
        int n4 = 0;
        for (int i = 0; i < n3; ++i) {
            while (n4 < n2 && (string.charAt(n4) == ' ' || string.charAt(n4) == '_')) {
                ++n4;
            }
            if (n4 >= n2 || string.charAt(n4) != string2.charAt(i)) {
                return !(i < n || !bl && n4 < n2 || n4 < n2 && string.charAt(n4 - 1) != ' ') ? n4 : -1;
            }
            ++n4;
        }
        return n4 >= n2 || bl || string.charAt(n4) == ' ' ? n4 : -1;
    }

    private static String[] toTrimmedTokens(String string, char c2) {
        int n;
        int n2 = 0;
        int n3 = string.length();
        boolean bl = c2 == ' ';
        int n4 = 0;
        for (int i = 0; i < n3; ++i) {
            n = string.charAt(i);
            if (n == 40) {
                ++n4;
            } else if (n == 41) {
                --n4;
            } else if (n4 == 0 && n == c2) {
                ++n2;
                while (bl && i < n3 - 1 && string.charAt(i + 1) == ' ') {
                    ++i;
                }
            }
            if (n4 >= 0) continue;
            throw new IllegalArgumentException("Unbalanced parentheses: '" + string + "'");
        }
        if (n4 != 0) {
            throw new IllegalArgumentException("Unbalanced parentheses: '" + string + "'");
        }
        if (n2 == 0) {
            return new String[]{string.trim()};
        }
        String[] stringArray = new String[n2 + 1];
        n = 0;
        int n5 = 0;
        n4 = 0;
        for (int i = 0; i < n3; ++i) {
            char c3 = string.charAt(i);
            if (c3 == '(') {
                ++n4;
                continue;
            }
            if (c3 == ')') {
                --n4;
                continue;
            }
            if (n4 != 0 || c3 != c2) continue;
            stringArray[n5++] = string.substring(n, i).trim();
            n = i + 1;
            while (bl && i < n3 - 1 && string.charAt(i + 1) == ' ') {
                ++i;
            }
        }
        stringArray[n5++] = string.substring(n, n3).trim();
        return stringArray;
    }

    private static final ArrayList<String> getRowColAndGapsTrimmed(String string) {
        if (string.indexOf(124) != -1) {
            string = string.replaceAll("\\|", "][");
        }
        ArrayList<String> arrayList = new ArrayList<String>(Math.max(string.length() >> 3, 3));
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = string.length();
        for (int i = 0; i < n4; ++i) {
            char c2 = string.charAt(i);
            if (c2 == '[') {
                ++n;
            } else {
                if (c2 != ']') continue;
                ++n2;
            }
            if (n != n2 && n - 1 != n2) break;
            arrayList.add(string.substring(n3, i).trim());
            n3 = i + 1;
        }
        if (n != n2) {
            throw new IllegalArgumentException("'[' and ']' mismatch in row/column format string: " + string);
        }
        if (n == 0) {
            arrayList.add("");
            arrayList.add(string);
            arrayList.add("");
        } else if (arrayList.size() % 2 == 0) {
            arrayList.add(string.substring(n3, string.length()));
        }
        return arrayList;
    }

    public static final String prepare(String string) {
        return string != null ? string.trim().toLowerCase() : "";
    }
}

