/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.layout;

import java.util.HashMap;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.BoundSize;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.DimConstraint;
import net.miginfocom.layout.Grid;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.layout.UnitValue;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class IDEUtil {
    public static final UnitValue ZERO = UnitValue.ZERO;
    public static final UnitValue TOP = UnitValue.TOP;
    public static final UnitValue LEADING = UnitValue.LEADING;
    public static final UnitValue LEFT = UnitValue.LEFT;
    public static final UnitValue CENTER = UnitValue.CENTER;
    public static final UnitValue TRAILING = UnitValue.TRAILING;
    public static final UnitValue RIGHT = UnitValue.RIGHT;
    public static final UnitValue BOTTOM = UnitValue.BOTTOM;
    public static final UnitValue LABEL = UnitValue.LABEL;
    public static final UnitValue INF = UnitValue.INF;
    public static final UnitValue BASELINE_IDENTITY = UnitValue.BASELINE_IDENTITY;
    private static final String[] X_Y_STRINGS = new String[]{"x", "y", "x2", "y2"};

    public String getIDEUtilVersion() {
        return "1.0";
    }

    public static HashMap<Object, int[]> getGridPositions(Object object) {
        return Grid.getGridPositions(object);
    }

    public static int[][] getRowSizes(Object object) {
        return Grid.getSizesAndIndexes(object, true);
    }

    public static int[][] getColumnSizes(Object object) {
        return Grid.getSizesAndIndexes(object, false);
    }

    public static final String getConstraintString(AC aC, boolean bl, boolean bl2) {
        StringBuffer stringBuffer = new StringBuffer(32);
        DimConstraint[] dimConstraintArray = aC.getConstaints();
        BoundSize boundSize = bl2 ? PlatformDefaults.getGridGapX() : PlatformDefaults.getGridGapY();
        for (int i = 0; i < dimConstraintArray.length; ++i) {
            DimConstraint dimConstraint = dimConstraintArray[i];
            IDEUtil.addRowDimConstraintString(dimConstraint, stringBuffer, bl);
            if (i >= dimConstraintArray.length - 1) continue;
            BoundSize boundSize2 = dimConstraint.getGapAfter();
            if (boundSize2 == boundSize || boundSize2 == null) {
                boundSize2 = dimConstraintArray[i + 1].getGapBefore();
            }
            if (boundSize2 != null) {
                String string = IDEUtil.getBS(boundSize2);
                if (bl) {
                    stringBuffer.append(".gap(\"").append(string).append("\")");
                    continue;
                }
                stringBuffer.append(string);
                continue;
            }
            if (!bl) continue;
            stringBuffer.append(".gap()");
        }
        return stringBuffer.toString();
    }

    private static final void addRowDimConstraintString(DimConstraint dimConstraint, StringBuffer stringBuffer, boolean bl) {
        UnitValue unitValue;
        String string;
        String string2;
        Float f;
        int n;
        Float f2;
        int n2 = dimConstraint.getGrowPriority();
        int n3 = stringBuffer.length();
        BoundSize boundSize = dimConstraint.getSize();
        if (!boundSize.isUnset()) {
            if (bl) {
                stringBuffer.append(".size(\"").append(IDEUtil.getBS(boundSize)).append("\")");
            } else {
                stringBuffer.append(',').append(IDEUtil.getBS(boundSize));
            }
        }
        if (n2 != 100) {
            if (bl) {
                stringBuffer.append(".growPrio(").append(n2).append("\")");
            } else {
                stringBuffer.append(",growprio ").append(n2);
            }
        }
        if ((f2 = dimConstraint.getGrow()) != null) {
            String string3;
            String string4 = string3 = f2.floatValue() != 100.0f ? IDEUtil.floatToString(f2.floatValue(), bl) : "";
            if (bl) {
                if (string3.length() == 0) {
                    stringBuffer.append(".grow()");
                } else {
                    stringBuffer.append(".grow(\"").append(string3).append("\")");
                }
            } else {
                stringBuffer.append(",grow").append(string3.length() > 0 ? " " + string3 : "");
            }
        }
        if ((n = dimConstraint.getShrinkPriority()) != 100) {
            if (bl) {
                stringBuffer.append(".shrinkPrio(").append(n).append("\")");
            } else {
                stringBuffer.append(",shrinkprio ").append(n);
            }
        }
        if ((f = dimConstraint.getShrink()) != null && f.intValue() != 100) {
            string2 = IDEUtil.floatToString(f.floatValue(), bl);
            if (bl) {
                stringBuffer.append(".shrink(\"").append(string2).append("\")");
            } else {
                stringBuffer.append(",shrink ").append(string2);
            }
        }
        if ((string2 = dimConstraint.getEndGroup()) != null) {
            if (bl) {
                stringBuffer.append(".endGroup(\"").append(string2).append("\")");
            } else {
                stringBuffer.append(",endgroup ").append(string2);
            }
        }
        if ((string = dimConstraint.getSizeGroup()) != null) {
            if (bl) {
                stringBuffer.append(".sizeGroup(\"").append(string).append("\")");
            } else {
                stringBuffer.append(",sizegroup ").append(string);
            }
        }
        if ((unitValue = dimConstraint.getAlign()) != null) {
            if (bl) {
                stringBuffer.append(".align(\"").append(IDEUtil.getUV(unitValue)).append("\")");
            } else {
                String string5 = IDEUtil.getUV(unitValue);
                String string6 = string5.equals("top") || string5.equals("bottom") || string5.equals("left") || string5.equals("label") || string5.equals("leading") || string5.equals("center") || string5.equals("trailing") || string5.equals("right") || string5.equals("baseline") ? "" : "align ";
                stringBuffer.append(',').append(string6).append(string5);
            }
        }
        if (dimConstraint.isNoGrid()) {
            if (bl) {
                stringBuffer.append(".noGrid()");
            } else {
                stringBuffer.append(",nogrid");
            }
        }
        if (dimConstraint.isFill()) {
            if (bl) {
                stringBuffer.append(".fill()");
            } else {
                stringBuffer.append(",fill");
            }
        }
        if (!bl) {
            if (stringBuffer.length() > n3) {
                stringBuffer.setCharAt(n3, '[');
                stringBuffer.append(']');
            } else {
                stringBuffer.append("[]");
            }
        }
    }

    private static final void addComponentDimConstraintString(DimConstraint dimConstraint, StringBuffer stringBuffer, boolean bl, boolean bl2, boolean bl3) {
        String string;
        String string2;
        int n;
        Object object;
        Float f;
        int n2 = dimConstraint.getGrowPriority();
        if (n2 != 100) {
            if (bl) {
                stringBuffer.append(bl2 ? ".growPrioX(" : ".growPrioY(").append(n2).append(')');
            } else {
                stringBuffer.append(bl2 ? ",growpriox " : ",growprioy ").append(n2);
            }
        }
        if (!bl3 && (f = dimConstraint.getGrow()) != null) {
            Object object2 = object = f.floatValue() != 100.0f ? IDEUtil.floatToString(f.floatValue(), bl) : "";
            if (bl) {
                stringBuffer.append(bl2 ? ".growX(" : ".growY(").append((String)object).append(')');
            } else {
                stringBuffer.append(bl2 ? ",growx" : ",growy").append(((String)object).length() > 0 ? " " + (String)object : "");
            }
        }
        if ((n = dimConstraint.getShrinkPriority()) != 100) {
            if (bl) {
                stringBuffer.append(bl2 ? ".shrinkPrioX(" : ".shrinkPrioY(").append(n).append(')');
            } else {
                stringBuffer.append(bl2 ? ",shrinkpriox " : ",shrinkprioy ").append(n);
            }
        }
        if ((object = dimConstraint.getShrink()) != null && ((Float)object).intValue() != 100) {
            string2 = IDEUtil.floatToString(((Float)object).floatValue(), bl);
            if (bl) {
                stringBuffer.append(bl2 ? ".shrinkX(" : ".shrinkY(").append(string2).append(')');
            } else {
                stringBuffer.append(bl2 ? ",shrinkx " : ",shrinky ").append(string2);
            }
        }
        if ((string2 = dimConstraint.getEndGroup()) != null) {
            if (bl) {
                stringBuffer.append(bl2 ? ".endGroupX(\"" : ".endGroupY(\"").append(string2).append("\")");
            } else {
                stringBuffer.append(bl2 ? ",endgroupx " : ",endgroupy ").append(string2);
            }
        }
        if ((string = dimConstraint.getSizeGroup()) != null) {
            if (bl) {
                stringBuffer.append(bl2 ? ".sizeGroupX(\"" : ".sizeGroupY(\"").append(string).append("\")");
            } else {
                stringBuffer.append(bl2 ? ",sizegroupx " : ",sizegroupy ").append(string);
            }
        }
        IDEUtil.appendBoundSize(dimConstraint.getSize(), stringBuffer, bl2, bl);
        UnitValue unitValue = dimConstraint.getAlign();
        if (unitValue != null) {
            if (bl) {
                stringBuffer.append(bl2 ? ".alignX(\"" : ".alignY(\"").append(IDEUtil.getUV(unitValue)).append("\")");
            } else {
                stringBuffer.append(bl2 ? ",alignx " : ",aligny ").append(IDEUtil.getUV(unitValue));
            }
        }
        BoundSize boundSize = dimConstraint.getGapBefore();
        BoundSize boundSize2 = dimConstraint.getGapAfter();
        if (boundSize != null || boundSize2 != null) {
            if (bl) {
                stringBuffer.append(bl2 ? ".gapX(\"" : ".gapY(\"").append(IDEUtil.getBS(boundSize)).append("\", \"").append(IDEUtil.getBS(boundSize2)).append("\")");
            } else {
                stringBuffer.append(bl2 ? ",gapx " : ",gapy ").append(IDEUtil.getBS(boundSize));
                if (boundSize2 != null) {
                    stringBuffer.append(' ').append(IDEUtil.getBS(boundSize2));
                }
            }
        }
    }

    private static void appendBoundSize(BoundSize boundSize, StringBuffer stringBuffer, boolean bl, boolean bl2) {
        if (!boundSize.isUnset()) {
            if (boundSize.getPreferred() == null) {
                if (boundSize.getMin() == null) {
                    if (bl2) {
                        stringBuffer.append(bl ? ".maxWidth(\"" : ".maxHeight(\"").append(IDEUtil.getUV(boundSize.getMax())).append("\")");
                    } else {
                        stringBuffer.append(bl ? ",wmax " : ",hmax ").append(IDEUtil.getUV(boundSize.getMax()));
                    }
                } else if (boundSize.getMax() == null) {
                    if (bl2) {
                        stringBuffer.append(bl ? ".minWidth(\"" : ".minHeight(\"").append(IDEUtil.getUV(boundSize.getMin())).append("\")");
                    } else {
                        stringBuffer.append(bl ? ",wmin " : ",hmin ").append(IDEUtil.getUV(boundSize.getMin()));
                    }
                } else if (bl2) {
                    stringBuffer.append(bl ? ".width(\"" : ".height(\"").append(IDEUtil.getUV(boundSize.getMin())).append("::").append(IDEUtil.getUV(boundSize.getMax())).append("\")");
                } else {
                    stringBuffer.append(bl ? ",width " : ",height ").append(IDEUtil.getUV(boundSize.getMin())).append("::").append(IDEUtil.getUV(boundSize.getMax()));
                }
            } else if (bl2) {
                stringBuffer.append(bl ? ".width(\"" : ".height(\"").append(IDEUtil.getBS(boundSize)).append("\")");
            } else {
                stringBuffer.append(bl ? ",width " : ",height ").append(IDEUtil.getBS(boundSize));
            }
        }
    }

    public static final String getConstraintString(CC cC, boolean bl) {
        String string;
        int n;
        int n2;
        int n3;
        int n4;
        String string2;
        String string3;
        UnitValue[] unitValueArray;
        UnitValue[] unitValueArray2;
        Boolean bl2;
        StringBuffer stringBuffer = new StringBuffer(16);
        if (cC.isNewline()) {
            stringBuffer.append(bl ? ".newline()" : ",newline");
        }
        if (cC.isExternal()) {
            stringBuffer.append(bl ? ".external()" : ",external");
        }
        if ((bl2 = cC.getFlowX()) != null) {
            if (bl) {
                stringBuffer.append(bl2 != false ? ".flowX()" : ".flowY()");
            } else {
                stringBuffer.append(bl2 != false ? ",flowx" : ",flowy");
            }
        }
        if ((unitValueArray2 = cC.getPadding()) != null) {
            stringBuffer.append(bl ? ".pad(\"" : ",pad ");
            for (int i = 0; i < unitValueArray2.length; ++i) {
                stringBuffer.append(IDEUtil.getUV(unitValueArray2[i])).append(i < unitValueArray2.length - 1 ? " " : "");
            }
            if (bl) {
                stringBuffer.append("\")");
            }
        }
        if ((unitValueArray = cC.getPos()) != null) {
            int n5;
            if (cC.isBoundsInGrid()) {
                for (n5 = 0; n5 < 4; ++n5) {
                    if (unitValueArray[n5] == null) continue;
                    if (bl) {
                        stringBuffer.append('.').append(X_Y_STRINGS[n5]).append("(\"").append(IDEUtil.getUV(unitValueArray[n5])).append("\")");
                        continue;
                    }
                    stringBuffer.append(',').append(X_Y_STRINGS[n5]).append(IDEUtil.getUV(unitValueArray[n5]));
                }
            } else {
                stringBuffer.append(bl ? ".pos(\"" : ",pos ");
                n5 = unitValueArray[2] != null || unitValueArray[3] != null ? 4 : 2;
                for (int i = 0; i < n5; ++i) {
                    stringBuffer.append(IDEUtil.getUV(unitValueArray[i])).append(i < n5 - 1 ? " " : "");
                }
                if (bl) {
                    stringBuffer.append("\")");
                }
            }
        }
        if ((string3 = cC.getId()) != null) {
            if (bl) {
                stringBuffer.append(".id(\"").append(string3).append("\")");
            } else {
                stringBuffer.append(",id ").append(string3);
            }
        }
        if ((string2 = cC.getTag()) != null) {
            if (bl) {
                stringBuffer.append(".tag(\"").append(string2).append("\")");
            } else {
                stringBuffer.append(",tag ").append(string2);
            }
        }
        if ((n4 = cC.getHideMode()) >= 0) {
            if (bl) {
                stringBuffer.append(".hideMode(").append(n4).append(')');
            } else {
                stringBuffer.append(",hideMode ").append(n4);
            }
        }
        if ((n3 = cC.getSkip()) > 0) {
            if (bl) {
                stringBuffer.append(".skip(").append(n3).append(')');
            } else {
                stringBuffer.append(",skip ").append(n3);
            }
        }
        if ((n2 = cC.getSplit()) > 1) {
            String string4;
            String string5 = string4 = n2 == 2097051 ? "" : String.valueOf(n2);
            if (bl) {
                stringBuffer.append(".split(").append(string4).append(')');
            } else {
                stringBuffer.append(",split ").append(string4);
            }
        }
        int n6 = cC.getCellX();
        int n7 = cC.getCellY();
        int n8 = cC.getSpanX();
        int n9 = cC.getSpanY();
        if (n6 >= 0 && n7 >= 0) {
            if (bl) {
                stringBuffer.append(".cell(").append(n6).append(", ").append(n7);
                if (n8 > 1 || n9 > 1) {
                    stringBuffer.append(", ").append(n8).append(", ").append(n9);
                }
                stringBuffer.append(')');
            } else {
                stringBuffer.append(",cell ").append(n6).append(' ').append(n7);
                if (n8 > 1 || n9 > 1) {
                    stringBuffer.append(' ').append(n8).append(' ').append(n9);
                }
            }
        } else if (n8 > 1 || n9 > 1) {
            if (n8 > 1 && n9 > 1) {
                stringBuffer.append(bl ? ".span(" : ",span ").append(n8).append(bl ? ", " : " ").append(n9);
            } else if (n8 > 1) {
                stringBuffer.append(bl ? ".spanX(" : ",spanx ").append(n8 == 2097051 ? "" : String.valueOf(n8));
            } else if (n9 > 1) {
                stringBuffer.append(bl ? ".spanY(" : ",spany ").append(n9 == 2097051 ? "" : String.valueOf(n9));
            }
            if (bl) {
                stringBuffer.append(')');
            }
        }
        Float f = cC.getPushX();
        Float f2 = cC.getPushY();
        if (f != null || f2 != null) {
            if (f != null && f2 != null) {
                stringBuffer.append(bl ? ".push(" : ",push ");
                if ((double)f.floatValue() != 100.0 || (double)f2.floatValue() != 100.0) {
                    stringBuffer.append(f).append(bl ? ", " : " ").append(f2);
                }
            } else if (f != null) {
                stringBuffer.append(bl ? ".pushX(" : ",pushx ").append(f.floatValue() == 100.0f ? "" : String.valueOf(f));
            } else if (f2 != null) {
                stringBuffer.append(bl ? ".pushY(" : ",pushy ").append(f2.floatValue() == 100.0f ? "" : String.valueOf(f2));
            }
            if (bl) {
                stringBuffer.append(')');
            }
        }
        if ((n = cC.getDockSide()) >= 0) {
            String string6 = CC.DOCK_SIDES[n];
            if (bl) {
                stringBuffer.append(".dock").append(Character.toUpperCase(string6.charAt(0))).append(string6.substring(1)).append("()");
            } else {
                stringBuffer.append(",").append(string6);
            }
        }
        boolean bl3 = cC.getHorizontal().getGrow() != null && cC.getHorizontal().getGrow().intValue() == 100 && cC.getVertical().getGrow() != null && cC.getVertical().getGrow().intValue() == 100;
        IDEUtil.addComponentDimConstraintString(cC.getHorizontal(), stringBuffer, bl, true, bl3);
        IDEUtil.addComponentDimConstraintString(cC.getVertical(), stringBuffer, bl, false, bl3);
        if (bl3) {
            stringBuffer.append(bl ? ".grow()" : ",grow");
        }
        if (cC.isWrap()) {
            stringBuffer.append(bl ? ".wrap()" : ",wrap");
        }
        return (string = stringBuffer.toString()).length() == 0 || string.charAt(0) != ',' ? string : string.substring(1);
    }

    public static final String getConstraintString(LC lC, boolean bl) {
        String string;
        int n;
        int n2;
        int n3;
        String string2;
        Object object;
        Boolean bl2;
        StringBuffer stringBuffer = new StringBuffer(16);
        if (!lC.isFlowX()) {
            stringBuffer.append(bl ? ".flowY()" : ",flowy");
        }
        boolean bl3 = lC.isFillX();
        boolean bl4 = lC.isFillY();
        if (bl3 || bl4) {
            if (bl3 == bl4) {
                stringBuffer.append(bl ? ".fill()" : ",fill");
            } else {
                stringBuffer.append(bl ? (bl3 ? ".fillX()" : ".fillY()") : (bl3 ? ",fillx" : ",filly"));
            }
        }
        if ((bl2 = lC.getLeftToRight()) != null) {
            if (bl) {
                stringBuffer.append(".leftToRight(").append(bl2).append(')');
            } else {
                stringBuffer.append(bl2 != false ? ",ltr" : ",rtl");
            }
        }
        if (!lC.getPackWidth().isUnset() || !lC.getPackHeight().isUnset()) {
            if (bl) {
                object = IDEUtil.getBS(lC.getPackWidth());
                string2 = IDEUtil.getBS(lC.getPackHeight());
                stringBuffer.append(".pack(");
                if (((String)object).equals("pref") && string2.equals("pref")) {
                    stringBuffer.append(')');
                } else {
                    stringBuffer.append('\"').append((String)object).append("\", \"").append(string2).append("\")");
                }
            } else {
                stringBuffer.append(",pack");
                object = IDEUtil.getBS(lC.getPackWidth()) + " " + IDEUtil.getBS(lC.getPackHeight());
                if (!((String)object).equals("pref pref")) {
                    stringBuffer.append(' ').append((String)object);
                }
            }
        }
        if (lC.getPackWidthAlign() != 0.5f || lC.getPackHeightAlign() != 1.0f) {
            if (bl) {
                stringBuffer.append(".packAlign(").append(IDEUtil.floatToString(lC.getPackWidthAlign(), bl)).append(", ").append(IDEUtil.floatToString(lC.getPackHeightAlign(), bl)).append(')');
            } else {
                stringBuffer.append(",packalign ").append(IDEUtil.floatToString(lC.getPackWidthAlign(), bl)).append(' ').append(IDEUtil.floatToString(lC.getPackHeightAlign(), bl));
            }
        }
        if (!lC.isTopToBottom()) {
            stringBuffer.append(bl ? ".bottomToTop()" : ",btt");
        }
        if ((object = lC.getInsets()) != null) {
            string2 = LayoutUtil.getCCString(object);
            if (string2 != null) {
                if (bl) {
                    stringBuffer.append(".insets(\"").append(string2).append("\")");
                } else {
                    stringBuffer.append(",insets ").append(string2);
                }
            } else {
                stringBuffer.append(bl ? ".insets(\"" : ",insets ");
                for (int i = 0; i < ((Object)object).length; ++i) {
                    stringBuffer.append(IDEUtil.getUV((UnitValue)object[i])).append(i < ((Object)object).length - 1 ? " " : "");
                }
                if (bl) {
                    stringBuffer.append("\")");
                }
            }
        }
        if (lC.isNoGrid()) {
            stringBuffer.append(bl ? ".noGrid()" : ",nogrid");
        }
        if (!lC.isVisualPadding()) {
            stringBuffer.append(bl ? ".noVisualPadding()" : ",novisualpadding");
        }
        if ((n3 = lC.getHideMode()) > 0) {
            if (bl) {
                stringBuffer.append(".hideMode(").append(n3).append(')');
            } else {
                stringBuffer.append(",hideMode ").append(n3);
            }
        }
        IDEUtil.appendBoundSize(lC.getWidth(), stringBuffer, true, bl);
        IDEUtil.appendBoundSize(lC.getHeight(), stringBuffer, false, bl);
        UnitValue unitValue = lC.getAlignX();
        UnitValue unitValue2 = lC.getAlignY();
        if (unitValue != null || unitValue2 != null) {
            if (unitValue != null && unitValue2 != null) {
                stringBuffer.append(bl ? ".align(\"" : ",align ").append(IDEUtil.getUV(unitValue)).append(' ').append(IDEUtil.getUV(unitValue2));
            } else if (unitValue != null) {
                stringBuffer.append(bl ? ".alignX(\"" : ",alignx ").append(IDEUtil.getUV(unitValue));
            } else if (unitValue2 != null) {
                stringBuffer.append(bl ? ".alignY(\"" : ",aligny ").append(IDEUtil.getUV(unitValue2));
            }
            if (bl) {
                stringBuffer.append("\")");
            }
        }
        BoundSize boundSize = lC.getGridGapX();
        BoundSize boundSize2 = lC.getGridGapY();
        if (boundSize != null || boundSize2 != null) {
            if (boundSize != null && boundSize2 != null) {
                stringBuffer.append(bl ? ".gridGap(\"" : ",gap ").append(IDEUtil.getBS(boundSize)).append(' ').append(IDEUtil.getBS(boundSize2));
            } else if (boundSize != null) {
                stringBuffer.append(bl ? ".gridGapX(\"" : ",gapx ").append(IDEUtil.getBS(boundSize));
            } else if (boundSize2 != null) {
                stringBuffer.append(bl ? ".gridGapY(\"" : ",gapy ").append(IDEUtil.getBS(boundSize2));
            }
            if (bl) {
                stringBuffer.append("\")");
            }
        }
        if ((n2 = lC.getWrapAfter()) != 2097051) {
            String string3;
            String string4 = string3 = n2 > 0 ? String.valueOf(n2) : "";
            if (bl) {
                stringBuffer.append(".wrap(").append(string3).append(')');
            } else {
                stringBuffer.append(",wrap ").append(string3);
            }
        }
        if ((n = lC.getDebugMillis()) > 0) {
            if (bl) {
                stringBuffer.append(".debug(").append(n).append(')');
            } else {
                stringBuffer.append(",debug ").append(n);
            }
        }
        return (string = stringBuffer.toString()).length() == 0 || string.charAt(0) != ',' ? string : string.substring(1);
    }

    private static String getUV(UnitValue unitValue) {
        return unitValue != null ? unitValue.getConstraintString() : "null";
    }

    private static String getBS(BoundSize boundSize) {
        return boundSize != null ? boundSize.getConstraintString() : "null";
    }

    private static final String floatToString(float f, boolean bl) {
        String string = String.valueOf(f);
        return string.endsWith(".0") ? string.substring(0, string.length() - 2) : string + (bl ? "f" : "");
    }
}

