/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.WeakHashMap;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.BoundSize;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.DimConstraint;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.LayoutCallback;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.LinkHandler;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.layout.ResizeConstraint;
import net.miginfocom.layout.UnitValue;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class Grid {
    public static final boolean TEST_GAPS = true;
    private static final Float[] GROW_100 = new Float[]{ResizeConstraint.WEIGHT_100};
    private static final DimConstraint DOCK_DIM_CONSTRAINT = new DimConstraint();
    private static final int MAX_GRID = 30000;
    private static final int MAX_DOCK_GRID = Short.MAX_VALUE;
    private static final ResizeConstraint GAP_RC_CONST;
    private static final ResizeConstraint GAP_RC_CONST_PUSH;
    private final LC lc;
    private final ContainerWrapper container;
    private final LinkedHashMap<Integer, Cell> grid = new LinkedHashMap();
    private HashMap<Integer, BoundSize> wrapGapMap = null;
    private final TreeSet<Integer> rowIndexes = new TreeSet();
    private final TreeSet<Integer> colIndexes = new TreeSet();
    private final AC rowConstr;
    private final AC colConstr;
    private FlowSizeSpec colFlowSpecs = null;
    private FlowSizeSpec rowFlowSpecs = null;
    private ArrayList<LinkedDimGroup>[] colGroupLists;
    private ArrayList<LinkedDimGroup>[] rowGroupLists;
    private int[] width = null;
    private int[] height = null;
    private ArrayList<int[]> debugRects = null;
    private HashMap<String, Boolean> linkTargetIDs = null;
    private final int dockOffY;
    private final int dockOffX;
    private final Float[] pushXs;
    private final Float[] pushYs;
    private final ArrayList<LayoutCallback> callbackList;
    private static WeakHashMap[] PARENT_ROWCOL_SIZES_MAP;
    private static WeakHashMap<Object, LinkedHashMap<Integer, Cell>> PARENT_GRIDPOS_MAP;

    /*
     * WARNING - void declaration
     */
    public Grid(ContainerWrapper containerWrapper, LC lC, AC aC, AC aC2, Map<ComponentWrapper, CC> map, ArrayList<LayoutCallback> arrayList) {
        int n;
        Object object;
        Object object2;
        int n2;
        Iterator<Cell> iterator;
        this.lc = lC;
        this.rowConstr = aC;
        this.colConstr = aC2;
        this.container = containerWrapper;
        this.callbackList = arrayList;
        int n3 = lC.getWrapAfter() != 0 ? lC.getWrapAfter() : (lC.isFlowX() ? aC2 : aC).getConstaints().length;
        ComponentWrapper[] componentWrapperArray = containerWrapper.getComponents();
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        int[] nArray = new int[2];
        ArrayList<int[]> arrayList2 = new ArrayList<int[]>(2);
        Object[] objectArray = (lC.isFlowX() ? aC : aC2).getConstaints();
        int n4 = 0;
        int n5 = 0;
        int[] nArray2 = null;
        LinkHandler.clearTemporaryBounds(containerWrapper.getLayout());
        int n6 = 0;
        while (n6 < componentWrapperArray.length) {
            boolean bl5;
            int n7;
            iterator = componentWrapperArray[n6];
            CC object32 = Grid.getCC((ComponentWrapper)((Object)iterator), map);
            this.addLinkIDs(object32);
            int n8 = iterator.isVisible() ? -1 : (n2 = object32.getHideMode() != -1 ? object32.getHideMode() : lC.getHideMode());
            if (n2 == 3) {
                this.setLinkedBounds((ComponentWrapper)((Object)iterator), object32, iterator.getX(), iterator.getY(), iterator.getWidth(), iterator.getHeight(), object32.isExternal());
                ++n6;
                continue;
            }
            if (object32.getHorizontal().getSizeGroup() != null) {
                ++n4;
            }
            if (object32.getVertical().getSizeGroup() != null) {
                ++n5;
            }
            UnitValue[] unitValueArray = this.getPos((ComponentWrapper)((Object)iterator), object32);
            BoundSize[] boundSizeArray = this.getCallbackSize((ComponentWrapper)((Object)iterator));
            if (unitValueArray != null || object32.isExternal()) {
                object2 = new CompWrap((ComponentWrapper)((Object)iterator), object32, n2, unitValueArray, boundSizeArray);
                object = this.grid.get(null);
                if (object == null) {
                    this.grid.put(null, new Cell((CompWrap)object2));
                } else {
                    ((Cell)object).compWraps.add(object2);
                }
                if (!object32.isBoundsInGrid() || object32.isExternal()) {
                    this.setLinkedBounds((ComponentWrapper)((Object)iterator), object32, iterator.getX(), iterator.getY(), iterator.getWidth(), iterator.getHeight(), object32.isExternal());
                    ++n6;
                    continue;
                }
            }
            if (object32.getDockSide() != -1) {
                if (nArray2 == null) {
                    nArray2 = new int[]{-32767, -32767, Short.MAX_VALUE, Short.MAX_VALUE};
                }
                this.addDockingCell(nArray2, object32.getDockSide(), new CompWrap((ComponentWrapper)((Object)iterator), object32, n2, unitValueArray, boundSizeArray));
                ++n6;
                continue;
            }
            object2 = object32.getFlowX();
            object = null;
            if (object32.isNewline()) {
                this.wrap(nArray, object32.getNewlineGapSize());
            } else if (bl4) {
                this.wrap(nArray, null);
            }
            bl4 = false;
            boolean bl6 = lC.isNoGrid() || ((DimConstraint)LayoutUtil.getIndexSafe(objectArray, lC.isFlowX() ? nArray[1] : nArray[0])).isNoGrid();
            int n9 = object32.getCellX();
            int n10 = object32.getCellY();
            if (!(n9 >= 0 && n10 >= 0 || bl6 || object32.getSkip() != 0)) {
                while (!this.isCellFree(nArray[1], nArray[0], arrayList2)) {
                    if (Math.abs(this.increase(nArray, 1)) < n3) continue;
                    this.wrap(nArray, null);
                }
            } else {
                if (n9 >= 0 && n10 >= 0) {
                    if (n10 >= 0) {
                        nArray[0] = n9;
                        nArray[1] = n10;
                    } else if (lC.isFlowX()) {
                        nArray[0] = n9;
                    } else {
                        nArray[1] = n9;
                    }
                }
                object = this.getCell(nArray[1], nArray[0]);
            }
            int n11 = object32.getSkip();
            for (n7 = 0; n7 < n11; ++n7) {
                do {
                    if (Math.abs(this.increase(nArray, 1)) < n3) continue;
                    this.wrap(nArray, null);
                } while (!this.isCellFree(nArray[1], nArray[0], arrayList2));
            }
            if (object == null) {
                n7 = Math.min(bl6 && lC.isFlowX() ? 2097051 : object32.getSpanX(), 30000 - nArray[0]);
                n11 = Math.min(bl6 && !lC.isFlowX() ? 2097051 : object32.getSpanY(), 30000 - nArray[1]);
                object = new Cell(n7, n11, object2 != null ? ((Boolean)object2).booleanValue() : lC.isFlowX());
                this.setCell(nArray[1], nArray[0], (Cell)object);
                if (n7 > 1 || n11 > 1) {
                    arrayList2.add(new int[]{nArray[0], nArray[1], n7, n11});
                }
            }
            n7 = 0;
            boolean bl7 = false;
            boolean bl8 = bl5 = (lC.isFlowX() ? object32.getSpanX() : object32.getSpanY()) == 2097051;
            for (n11 = bl6 ? 2097051 : object32.getSplit() - 1; n11 >= 0 && n6 < componentWrapperArray.length; --n11) {
                void var23_38;
                ComponentWrapper componentWrapper = componentWrapperArray[n6];
                CC cC = Grid.getCC(componentWrapper, map);
                this.addLinkIDs(cC);
                boolean bl9 = componentWrapper.isVisible();
                int n12 = bl9 ? -1 : (n2 = cC.getHideMode() != -1 ? cC.getHideMode() : lC.getHideMode());
                if (cC.isExternal() || n2 == 3) {
                    ++n6;
                    ++n11;
                    continue;
                }
                bl2 |= (bl9 || n2 > 1) && cC.getPushX() != null;
                bl3 |= (bl9 || n2 > 1) && cC.getPushY() != null;
                if (cC != object32) {
                    if (cC.isNewline() || !cC.isBoundsInGrid() || cC.getDockSide() != -1) break;
                    if (n11 > 0 && cC.getSkip() > 0) {
                        bl7 = true;
                        break;
                    }
                    UnitValue[] unitValueArray2 = this.getPos(componentWrapper, cC);
                    boundSizeArray = this.getCallbackSize(componentWrapper);
                }
                CompWrap compWrap = new CompWrap(componentWrapper, cC, n2, (UnitValue[])var23_38, boundSizeArray);
                ((Cell)object).compWraps.add(compWrap);
                ((Cell)object).hasTagged = (byte)(((Cell)object).hasTagged | (cC.getTag() != null ? 1 : 0));
                bl |= ((Cell)object).hasTagged;
                if (cC != object32) {
                    if (cC.getHorizontal().getSizeGroup() != null) {
                        ++n4;
                    }
                    if (cC.getVertical().getSizeGroup() != null) {
                        ++n5;
                    }
                }
                ++n6;
                if (!cC.isWrap() && (!bl5 || n11 != 0)) continue;
                if (cC.isWrap()) {
                    this.wrap(nArray, cC.getWrapGapSize());
                } else {
                    bl4 = true;
                }
                n7 = 1;
                break;
            }
            if (n7 != 0 || bl6) continue;
            int n13 = lC.isFlowX() ? ((Cell)object).spanx : ((Cell)object).spany;
            if (Math.abs(lC.isFlowX() ? nArray[0] : nArray[1]) + n13 >= n3) {
                bl4 = true;
                continue;
            }
            this.increase(nArray, bl7 ? n13 - 1 : n13);
        }
        if (n4 > 0 || n5 > 0) {
            HashMap<String, int[]> hashMap = n4 > 0 ? new HashMap<String, int[]>(n4) : null;
            iterator = n5 > 0 ? new HashMap(n5) : null;
            ArrayList<Object> n15 = new ArrayList<Object>(Math.max(n4, n5));
            for (Cell cell : this.grid.values()) {
                for (int i = 0; i < cell.compWraps.size(); ++i) {
                    object2 = (CompWrap)cell.compWraps.get(i);
                    object = ((CompWrap)object2).cc.getHorizontal().getSizeGroup();
                    String string = ((CompWrap)object2).cc.getVertical().getSizeGroup();
                    if (object == null && string == null) continue;
                    if (object != null && hashMap != null) {
                        Grid.addToSizeGroup(hashMap, (String)object, ((CompWrap)object2).horSizes);
                    }
                    if (string != null && iterator != null) {
                        Grid.addToSizeGroup(iterator, string, ((CompWrap)object2).verSizes);
                    }
                    n15.add(object2);
                }
            }
            for (n2 = 0; n2 < n15.size(); ++n2) {
                CompWrap compWrap = (CompWrap)n15.get(n2);
                if (hashMap != null) {
                    compWrap.setSizes((int[])hashMap.get(compWrap.cc.getHorizontal().getSizeGroup()), true);
                }
                if (iterator == null) continue;
                compWrap.setSizes((int[])((HashMap)((Object)iterator)).get(compWrap.cc.getVertical().getSizeGroup()), false);
            }
        }
        if (n4 > 0 || n5 > 0) {
            HashMap<String, int[]> hashMap = n4 > 0 ? new HashMap<String, int[]>(n4) : null;
            iterator = n5 > 0 ? new HashMap(n5) : null;
            ArrayList<Object> arrayList3 = new ArrayList<Object>(Math.max(n4, n5));
            for (Cell cell : this.grid.values()) {
                for (int i = 0; i < cell.compWraps.size(); ++i) {
                    object2 = (CompWrap)cell.compWraps.get(i);
                    object = ((CompWrap)object2).cc.getHorizontal().getSizeGroup();
                    String string = ((CompWrap)object2).cc.getVertical().getSizeGroup();
                    if (object == null && string == null) continue;
                    if (object != null && hashMap != null) {
                        Grid.addToSizeGroup(hashMap, (String)object, ((CompWrap)object2).horSizes);
                    }
                    if (string != null && iterator != null) {
                        Grid.addToSizeGroup(iterator, string, ((CompWrap)object2).verSizes);
                    }
                    arrayList3.add(object2);
                }
            }
            for (n2 = 0; n2 < arrayList3.size(); ++n2) {
                CompWrap compWrap = (CompWrap)arrayList3.get(n2);
                if (hashMap != null) {
                    compWrap.setSizes((int[])hashMap.get(compWrap.cc.getHorizontal().getSizeGroup()), true);
                }
                if (iterator == null) continue;
                compWrap.setSizes((int[])((HashMap)((Object)iterator)).get(compWrap.cc.getVertical().getSizeGroup()), false);
            }
        }
        if (bl) {
            Grid.sortCellsByPlatform(this.grid.values(), containerWrapper);
        }
        n6 = LayoutUtil.isLeftToRight(lC, containerWrapper) ? 1 : 0;
        for (Cell cell : this.grid.values()) {
            ArrayList arrayList4 = cell.compWraps;
            int n14 = arrayList4.size() - 1;
            for (int i = 0; i <= n14; ++i) {
                object2 = (CompWrap)arrayList4.get(i);
                object = i > 0 ? ((CompWrap)arrayList4.get(i - 1)).comp : null;
                ComponentWrapper componentWrapper = i < n14 ? ((CompWrap)arrayList4.get(i + 1)).comp : null;
                String string = Grid.getCC(((CompWrap)object2).comp, map).getTag();
                CC cC = object != null ? Grid.getCC((ComponentWrapper)object, map) : null;
                CC cC2 = componentWrapper != null ? Grid.getCC(componentWrapper, map) : null;
                ((CompWrap)object2).calcGaps((ComponentWrapper)object, cC, componentWrapper, cC2, string, cell.flowx, n6 != 0);
            }
        }
        this.dockOffX = Grid.getDockInsets(this.colIndexes);
        this.dockOffY = Grid.getDockInsets(this.rowIndexes);
        int n15 = aC.getCount();
        for (n = 0; n < n15; ++n) {
            this.rowIndexes.add(new Integer(n));
        }
        int n16 = aC2.getCount();
        for (n = 0; n < n16; ++n) {
            this.colIndexes.add(new Integer(n));
        }
        this.colGroupLists = this.divideIntoLinkedGroups(false);
        this.rowGroupLists = this.divideIntoLinkedGroups(true);
        this.pushXs = bl2 || lC.isFillX() ? this.getDefaultPushWeights(false) : null;
        Object object3 = this.pushYs = bl3 || lC.isFillY() ? this.getDefaultPushWeights(true) : null;
        if (LayoutUtil.isDesignTime(containerWrapper)) {
            Grid.saveGrid(containerWrapper, this.grid);
        }
    }

    private static CC getCC(ComponentWrapper componentWrapper, Map<ComponentWrapper, CC> map) {
        CC cC = map.get(componentWrapper);
        return cC != null ? cC : new CC();
    }

    private void addLinkIDs(CC cC) {
        String[] stringArray = cC.getLinkTargets();
        for (int i = 0; i < stringArray.length; ++i) {
            if (this.linkTargetIDs == null) {
                this.linkTargetIDs = new HashMap();
            }
            this.linkTargetIDs.put(stringArray[i], null);
        }
    }

    public void invalidateContainerSize() {
        this.colFlowSpecs = null;
    }

    public boolean layout(int[] nArray, UnitValue unitValue, UnitValue unitValue2, boolean bl, boolean bl2) {
        Object object;
        if (bl) {
            this.debugRects = new ArrayList();
        }
        this.checkSizeCalcs();
        this.resetLinkValues(true, true);
        this.layoutInOneDim(nArray[2], unitValue, false, this.pushXs);
        this.layoutInOneDim(nArray[3], unitValue2, true, this.pushYs);
        HashMap<String, Integer> hashMap = null;
        HashMap<String, Integer> hashMap2 = null;
        int n = this.container.getComponentCount();
        boolean bl3 = false;
        if (n > 0) {
            block0: for (int i = 0; i < (this.linkTargetIDs != null ? 2 : 1); ++i) {
                boolean bl4;
                int n2 = 0;
                do {
                    bl4 = false;
                    Iterator<Cell> iterator = this.grid.values().iterator();
                    while (iterator.hasNext()) {
                        ArrayList arrayList = iterator.next().compWraps;
                        int n3 = arrayList.size();
                        for (int j = 0; j < n3; ++j) {
                            object = (CompWrap)arrayList.get(j);
                            if (i == 0) {
                                if (!(bl4 |= this.doAbsoluteCorrections((CompWrap)object, nArray))) {
                                    if (((CompWrap)object).cc.getHorizontal().getEndGroup() != null) {
                                        hashMap = Grid.addToEndGroup(hashMap, ((CompWrap)object).cc.getHorizontal().getEndGroup(), ((CompWrap)object).x + ((CompWrap)object).w);
                                    }
                                    if (((CompWrap)object).cc.getVertical().getEndGroup() != null) {
                                        hashMap2 = Grid.addToEndGroup(hashMap2, ((CompWrap)object).cc.getVertical().getEndGroup(), ((CompWrap)object).y + ((CompWrap)object).h);
                                    }
                                }
                                if (this.linkTargetIDs != null && (this.linkTargetIDs.containsKey("visual") || this.linkTargetIDs.containsKey("container"))) {
                                    bl3 = true;
                                }
                            }
                            if (this.linkTargetIDs != null && i != 1) continue;
                            if (((CompWrap)object).cc.getHorizontal().getEndGroup() != null) {
                                ((CompWrap)object).w = (Integer)hashMap.get(((CompWrap)object).cc.getHorizontal().getEndGroup()) - ((CompWrap)object).x;
                            }
                            if (((CompWrap)object).cc.getVertical().getEndGroup() != null) {
                                ((CompWrap)object).h = (Integer)hashMap2.get(((CompWrap)object).cc.getVertical().getEndGroup()) - ((CompWrap)object).y;
                            }
                            ((CompWrap)object).x += nArray[0];
                            ((CompWrap)object).y += nArray[1];
                            bl3 |= ((CompWrap)object).transferBounds(bl2 && !bl3);
                            if (this.callbackList == null) continue;
                            for (int k = 0; k < this.callbackList.size(); ++k) {
                                this.callbackList.get(k).correctBounds(((CompWrap)object).comp);
                            }
                        }
                    }
                    this.clearGroupLinkBounds();
                    if (++n2 <= (n << 3) + 10) continue;
                    System.err.println("Unstable cyclic dependency in absolute linked values!");
                    continue block0;
                } while (bl4);
            }
        }
        if (bl) {
            Collection<Cell> collection = this.grid.values();
            Iterator<Cell> iterator = collection.iterator();
            while (iterator.hasNext()) {
                ArrayList arrayList = iterator.next().compWraps;
                int n4 = arrayList.size();
                for (int i = 0; i < n4; ++i) {
                    CompWrap compWrap = (CompWrap)arrayList.get(i);
                    LinkedDimGroup linkedDimGroup = Grid.getGroupContaining(this.colGroupLists, compWrap);
                    object = Grid.getGroupContaining(this.rowGroupLists, compWrap);
                    if (linkedDimGroup == null || object == null) continue;
                    this.debugRects.add(new int[]{linkedDimGroup.lStart + nArray[0] - (linkedDimGroup.fromEnd ? linkedDimGroup.lSize : 0), ((LinkedDimGroup)object).lStart + nArray[1] - (((LinkedDimGroup)object).fromEnd ? ((LinkedDimGroup)object).lSize : 0), linkedDimGroup.lSize, ((LinkedDimGroup)object).lSize});
                }
            }
        }
        return bl3;
    }

    public void paintDebug() {
        if (this.debugRects != null) {
            this.container.paintDebugOutline();
            ArrayList<int[]> arrayList = new ArrayList<int[]>();
            int n = this.debugRects.size();
            for (int i = 0; i < n; ++i) {
                int[] nArray = this.debugRects.get(i);
                if (arrayList.contains(nArray)) continue;
                this.container.paintDebugCell(nArray[0], nArray[1], nArray[2], nArray[3]);
                arrayList.add(nArray);
            }
            Iterator<Cell> iterator = this.grid.values().iterator();
            while (iterator.hasNext()) {
                ArrayList arrayList2 = iterator.next().compWraps;
                int n2 = arrayList2.size();
                for (int i = 0; i < n2; ++i) {
                    ((CompWrap)arrayList2.get(i)).comp.paintDebugOutline();
                }
            }
        }
    }

    public ContainerWrapper getContainer() {
        return this.container;
    }

    public final int[] getWidth() {
        this.checkSizeCalcs();
        return this.width;
    }

    public final int[] getHeight() {
        this.checkSizeCalcs();
        return this.height;
    }

    private void checkSizeCalcs() {
        if (this.colFlowSpecs == null) {
            this.colFlowSpecs = this.calcRowsOrColsSizes(true);
            this.rowFlowSpecs = this.calcRowsOrColsSizes(false);
            this.width = this.getMinPrefMaxSumSize(true);
            this.height = this.getMinPrefMaxSumSize(false);
            if (this.linkTargetIDs == null) {
                this.resetLinkValues(false, true);
            } else {
                this.layout(new int[4], null, null, false, false);
                this.resetLinkValues(false, false);
            }
            this.adjustSizeForAbsolute(true);
            this.adjustSizeForAbsolute(false);
        }
    }

    private final UnitValue[] getPos(ComponentWrapper componentWrapper, CC cC) {
        UnitValue[] unitValueArray = null;
        if (this.callbackList != null) {
            for (int i = 0; i < this.callbackList.size() && unitValueArray == null; ++i) {
                unitValueArray = this.callbackList.get(i).getPosition(componentWrapper);
            }
        }
        UnitValue[] unitValueArray2 = cC.getPos();
        if (unitValueArray == null || unitValueArray2 == null) {
            return unitValueArray != null ? unitValueArray : unitValueArray2;
        }
        for (int i = 0; i < 4; ++i) {
            UnitValue unitValue = unitValueArray[i];
            if (unitValue == null) continue;
            unitValueArray2[i] = unitValue;
        }
        return unitValueArray2;
    }

    private final BoundSize[] getCallbackSize(ComponentWrapper componentWrapper) {
        if (this.callbackList != null) {
            for (int i = 0; i < this.callbackList.size(); ++i) {
                BoundSize[] boundSizeArray = this.callbackList.get(i).getSize(componentWrapper);
                if (boundSizeArray == null) continue;
                return boundSizeArray;
            }
        }
        return null;
    }

    private static final int getDockInsets(TreeSet<Integer> treeSet) {
        int n = 0;
        Iterator<Integer> iterator = treeSet.iterator();
        while (iterator.hasNext() && iterator.next() < -30000) {
            ++n;
        }
        return n;
    }

    private boolean setLinkedBounds(ComponentWrapper componentWrapper, CC cC, int n, int n2, int n3, int n4, boolean bl) {
        String string;
        String string2 = string = cC.getId() != null ? cC.getId() : componentWrapper.getLinkId();
        if (string == null) {
            return false;
        }
        String string3 = null;
        int n5 = string.indexOf(46);
        if (n5 != -1) {
            string3 = string.substring(0, n5);
            string = string.substring(n5 + 1);
        }
        Object object = this.container.getLayout();
        boolean bl2 = false;
        if (bl || this.linkTargetIDs != null && this.linkTargetIDs.containsKey(string)) {
            bl2 = LinkHandler.setBounds(object, string, n, n2, n3, n4, !bl, false);
        }
        if (string3 != null && (bl || this.linkTargetIDs != null && this.linkTargetIDs.containsKey(string3))) {
            if (this.linkTargetIDs == null) {
                this.linkTargetIDs = new HashMap(4);
            }
            this.linkTargetIDs.put(string3, Boolean.TRUE);
            bl2 |= LinkHandler.setBounds(object, string3, n, n2, n3, n4, !bl, true);
        }
        return bl2;
    }

    private final int increase(int[] nArray, int n) {
        return this.lc.isFlowX() ? nArray[0] + n : nArray[1] + n;
    }

    private final void wrap(int[] nArray, BoundSize boundSize) {
        boolean bl = this.lc.isFlowX();
        nArray[0] = bl ? 0 : nArray[0] + 1;
        int n = nArray[1] = bl ? nArray[1] + 1 : 0;
        if (boundSize != null) {
            if (this.wrapGapMap == null) {
                this.wrapGapMap = new HashMap(8);
            }
            this.wrapGapMap.put(new Integer(nArray[bl ? 1 : 0]), boundSize);
        }
        if (bl) {
            this.rowIndexes.add(new Integer(nArray[1]));
        } else {
            this.colIndexes.add(new Integer(nArray[0]));
        }
    }

    private static void sortCellsByPlatform(Collection<Cell> collection, ContainerWrapper containerWrapper) {
        String string = PlatformDefaults.getButtonOrder();
        String string2 = string.toLowerCase();
        int n = PlatformDefaults.convertToPixels(1.0f, "u", true, 0.0f, containerWrapper, null);
        if (n == -87654312) {
            throw new IllegalArgumentException("'unrelated' not recognized by PlatformDefaults!");
        }
        int[] nArray = new int[]{n, n, -2147471302};
        int[] nArray2 = new int[]{0, 0, -2147471302};
        for (Cell cell : collection) {
            if (!cell.hasTagged) continue;
            CompWrap compWrap = null;
            boolean bl = false;
            boolean bl2 = false;
            ArrayList<CompWrap> arrayList = new ArrayList<CompWrap>(cell.compWraps.size());
            int n2 = string2.length();
            for (int i = 0; i < n2; ++i) {
                char c2 = string2.charAt(i);
                if (c2 == '+' || c2 == '_') {
                    bl = true;
                    if (c2 != '+') continue;
                    bl2 = true;
                    continue;
                }
                String string3 = PlatformDefaults.getTagForChar(c2);
                if (string3 == null) continue;
                int n3 = cell.compWraps.size();
                for (int j = 0; j < n3; ++j) {
                    CompWrap compWrap2 = (CompWrap)cell.compWraps.get(j);
                    if (!string3.equals(compWrap2.cc.getTag())) continue;
                    if (Character.isUpperCase(string.charAt(i))) {
                        int n4 = PlatformDefaults.getMinimumButtonWidth().getPixels(0.0f, containerWrapper, compWrap2.comp);
                        if (n4 > compWrap2.horSizes[0]) {
                            ((CompWrap)compWrap2).horSizes[0] = n4;
                        }
                        Grid.correctMinMax(compWrap2.horSizes);
                    }
                    arrayList.add(compWrap2);
                    if (bl) {
                        (compWrap != null ? compWrap : compWrap2).mergeGapSizes(nArray, cell.flowx, compWrap == null);
                        if (bl2) {
                            compWrap2.forcedPushGaps = 1;
                            bl = false;
                            bl2 = false;
                        }
                    }
                    if (c2 == 'u') {
                        bl = true;
                    }
                    compWrap = compWrap2;
                }
            }
            if (arrayList.size() > 0) {
                CompWrap compWrap3 = (CompWrap)arrayList.get(arrayList.size() - 1);
                if (bl) {
                    compWrap3.mergeGapSizes(nArray, cell.flowx, false);
                    if (bl2) {
                        compWrap3.forcedPushGaps |= 2;
                    }
                }
                if (compWrap3.cc.getHorizontal().getGapAfter() == null) {
                    compWrap3.setGaps(nArray2, 3);
                }
                if ((compWrap3 = (CompWrap)arrayList.get(0)).cc.getHorizontal().getGapBefore() == null) {
                    compWrap3.setGaps(nArray2, 1);
                }
            }
            if (cell.compWraps.size() == arrayList.size()) {
                cell.compWraps.clear();
            } else {
                cell.compWraps.removeAll(arrayList);
            }
            cell.compWraps.addAll(arrayList);
        }
    }

    private Float[] getDefaultPushWeights(boolean bl) {
        ArrayList<LinkedDimGroup>[] arrayListArray = bl ? this.rowGroupLists : this.colGroupLists;
        Float[] floatArray = GROW_100;
        int n = 0;
        int n2 = 1;
        while (n < arrayListArray.length) {
            ArrayList<LinkedDimGroup> arrayList = arrayListArray[n];
            Float f = null;
            for (int i = 0; i < arrayList.size(); ++i) {
                LinkedDimGroup linkedDimGroup = arrayList.get(i);
                for (int j = 0; j < linkedDimGroup._compWraps.size(); ++j) {
                    Float f2;
                    int n3;
                    CompWrap compWrap = (CompWrap)linkedDimGroup._compWraps.get(j);
                    int n4 = compWrap.comp.isVisible() ? -1 : (n3 = compWrap.cc.getHideMode() != -1 ? compWrap.cc.getHideMode() : this.lc.getHideMode());
                    Float f3 = n3 < 2 ? (bl ? compWrap.cc.getPushY() : compWrap.cc.getPushX()) : (f2 = null);
                    if (f != null && (f2 == null || !(f2.floatValue() > f.floatValue()))) continue;
                    f = f2;
                }
            }
            if (f != null) {
                if (floatArray == GROW_100) {
                    floatArray = new Float[(arrayListArray.length << 1) + 1];
                }
                floatArray[n2] = f;
            }
            ++n;
            n2 += 2;
        }
        return floatArray;
    }

    private void clearGroupLinkBounds() {
        if (this.linkTargetIDs == null) {
            return;
        }
        for (Map.Entry<String, Boolean> entry : this.linkTargetIDs.entrySet()) {
            if (entry.getValue() != Boolean.TRUE) continue;
            LinkHandler.clearBounds(this.container.getLayout(), entry.getKey());
        }
    }

    private void resetLinkValues(boolean bl, boolean bl2) {
        Object object = this.container.getLayout();
        if (bl2) {
            LinkHandler.clearTemporaryBounds(object);
        }
        boolean bl3 = !this.hasDocks();
        int n = bl ? this.lc.getWidth().constrain(this.container.getWidth(), Grid.getParentSize(this.container, true), this.container) : 0;
        int n2 = bl ? this.lc.getHeight().constrain(this.container.getHeight(), Grid.getParentSize(this.container, false), this.container) : 0;
        int n3 = LayoutUtil.getInsets(this.lc, 0, bl3).getPixels(0.0f, this.container, null);
        int n4 = LayoutUtil.getInsets(this.lc, 1, bl3).getPixels(0.0f, this.container, null);
        int n5 = n - n3 - LayoutUtil.getInsets(this.lc, 2, bl3).getPixels(0.0f, this.container, null);
        int n6 = n2 - n4 - LayoutUtil.getInsets(this.lc, 3, bl3).getPixels(0.0f, this.container, null);
        LinkHandler.setBounds(object, "visual", n3, n4, n5, n6, true, false);
        LinkHandler.setBounds(object, "container", 0, 0, n, n2, true, false);
    }

    private static LinkedDimGroup getGroupContaining(ArrayList<LinkedDimGroup>[] arrayListArray, CompWrap compWrap) {
        for (int i = 0; i < arrayListArray.length; ++i) {
            ArrayList<LinkedDimGroup> arrayList = arrayListArray[i];
            int n = arrayList.size();
            for (int j = 0; j < n; ++j) {
                ArrayList arrayList2 = arrayList.get(j)._compWraps;
                int n2 = arrayList2.size();
                for (int k = 0; k < n2; ++k) {
                    if (arrayList2.get(k) != compWrap) continue;
                    return arrayList.get(j);
                }
            }
        }
        return null;
    }

    private boolean doAbsoluteCorrections(CompWrap compWrap, int[] nArray) {
        boolean bl = false;
        int[] nArray2 = this.getAbsoluteDimBounds(compWrap, nArray[2], true);
        if (nArray2 != null) {
            compWrap.setDimBounds(nArray2[0], nArray2[1], true);
        }
        if ((nArray2 = this.getAbsoluteDimBounds(compWrap, nArray[3], false)) != null) {
            compWrap.setDimBounds(nArray2[0], nArray2[1], false);
        }
        if (this.linkTargetIDs != null) {
            bl = this.setLinkedBounds(compWrap.comp, compWrap.cc, compWrap.x, compWrap.y, compWrap.w, compWrap.h, false);
        }
        return bl;
    }

    private void adjustSizeForAbsolute(boolean bl) {
        int[] nArray = bl ? this.width : this.height;
        Cell cell = this.grid.get(null);
        if (cell == null || cell.compWraps.size() == 0) {
            return;
        }
        ArrayList arrayList = cell.compWraps;
        int n = 0;
        int n2 = cell.compWraps.size();
        for (int i = 0; i < n2 + 3; ++i) {
            boolean bl2 = false;
            for (int j = 0; j < n2; ++j) {
                CompWrap compWrap = (CompWrap)arrayList.get(j);
                int[] nArray2 = this.getAbsoluteDimBounds(compWrap, 0, bl);
                int n3 = nArray2[0] + nArray2[1];
                if (n < n3) {
                    n = n3;
                }
                if (this.linkTargetIDs == null) continue;
                bl2 |= this.setLinkedBounds(compWrap.comp, compWrap.cc, nArray2[0], nArray2[0], nArray2[1], nArray2[1], false);
            }
            if (!bl2) break;
            n = 0;
            this.clearGroupLinkBounds();
        }
        if (nArray[0] < (n += LayoutUtil.getInsets(this.lc, bl ? 3 : 2, !this.hasDocks()).getPixels(0.0f, this.container, null))) {
            nArray[0] = n;
        }
        if (nArray[1] < n) {
            nArray[1] = n;
        }
    }

    private int[] getAbsoluteDimBounds(CompWrap compWrap, int n, boolean bl) {
        UnitValue unitValue;
        int n2;
        if (compWrap.cc.isExternal()) {
            if (bl) {
                return new int[]{compWrap.comp.getX(), compWrap.comp.getWidth()};
            }
            return new int[]{compWrap.comp.getY(), compWrap.comp.getHeight()};
        }
        int[] nArray = this.lc.isVisualPadding() ? compWrap.comp.getVisualPadding() : null;
        UnitValue[] unitValueArray = compWrap.cc.getPadding();
        if (compWrap.pos == null && nArray == null && unitValueArray == null) {
            return null;
        }
        int n3 = bl ? compWrap.x : compWrap.y;
        int n4 = n2 = bl ? compWrap.w : compWrap.h;
        if (compWrap.pos != null) {
            UnitValue unitValue2 = compWrap.pos != null ? compWrap.pos[bl ? 0 : 1] : (unitValue = null);
            UnitValue unitValue3 = compWrap.pos != null ? compWrap.pos[bl ? 2 : 3] : null;
            int n5 = compWrap.getSize(0, bl);
            int n6 = compWrap.getSize(2, bl);
            n2 = Math.min(Math.max(compWrap.getSize(1, bl), n5), n6);
            if (unitValue != null) {
                n3 = unitValue.getPixels(unitValue.getUnit() == 12 ? (float)n2 : (float)n, this.container, compWrap.comp);
                if (unitValue3 != null) {
                    n2 = Math.min(Math.max((bl ? compWrap.x + compWrap.w : compWrap.y + compWrap.h) - n3, n5), n6);
                }
            }
            if (unitValue3 != null) {
                if (unitValue != null) {
                    n2 = Math.min(Math.max(unitValue3.getPixels(n, this.container, compWrap.comp) - n3, n5), n6);
                } else {
                    n3 = unitValue3.getPixels(n, this.container, compWrap.comp) - n2;
                }
            }
        }
        if (unitValueArray != null) {
            unitValue = unitValueArray[bl ? 1 : 0];
            int n7 = unitValue != null ? unitValue.getPixels(n, this.container, compWrap.comp) : 0;
            n3 += n7;
            unitValue = unitValueArray[bl ? 3 : 2];
            n2 += -n7 + (unitValue != null ? unitValue.getPixels(n, this.container, compWrap.comp) : 0);
        }
        if (nArray != null) {
            int n8 = nArray[bl ? 1 : 0];
            n3 += n8;
            n2 += -n8 + nArray[bl ? 3 : 2];
        }
        return new int[]{n3, n2};
    }

    private void layoutInOneDim(int n, UnitValue unitValue, boolean bl, Float[] floatArray) {
        int n2;
        boolean bl2 = !(!bl ? LayoutUtil.isLeftToRight(this.lc, this.container) : this.lc.isTopToBottom());
        DimConstraint[] dimConstraintArray = (bl ? this.rowConstr : this.colConstr).getConstaints();
        FlowSizeSpec flowSizeSpec = bl ? this.rowFlowSpecs : this.colFlowSpecs;
        ArrayList<LinkedDimGroup>[] arrayListArray = bl ? this.rowGroupLists : this.colGroupLists;
        int[] nArray = LayoutUtil.calculateSerial(flowSizeSpec.sizes, flowSizeSpec.resConstsInclGaps, floatArray, 1, n);
        if (LayoutUtil.isDesignTime(this.container)) {
            TreeSet<Integer> treeSet = bl ? this.rowIndexes : this.colIndexes;
            int[] nArray2 = new int[treeSet.size()];
            int n3 = 0;
            for (Integer n4 : treeSet) {
                nArray2[n3++] = n4;
            }
            Grid.putSizesAndIndexes(this.container.getComponent(), nArray, nArray2, bl);
        }
        int n5 = n2 = unitValue != null ? Math.round(unitValue.getPixels(n - LayoutUtil.sum(nArray), this.container, null)) : 0;
        if (bl2) {
            n2 = n - n2;
        }
        for (int i = 0; i < arrayListArray.length; ++i) {
            ArrayList<LinkedDimGroup> arrayList = arrayListArray[i];
            int n6 = i - (bl ? this.dockOffY : this.dockOffX);
            int n7 = i << 1;
            int n8 = n7 + 1;
            n2 += bl2 ? -nArray[n7] : nArray[n7];
            DimConstraint dimConstraint = n6 >= 0 ? dimConstraintArray[n6 >= dimConstraintArray.length ? dimConstraintArray.length - 1 : n6] : DOCK_DIM_CONSTRAINT;
            int n9 = nArray[n8];
            for (int j = 0; j < arrayList.size(); ++j) {
                LinkedDimGroup linkedDimGroup = arrayList.get(j);
                int n10 = n9;
                if (linkedDimGroup.span > 1) {
                    n10 = LayoutUtil.sum(nArray, n8, Math.min((linkedDimGroup.span << 1) - 1, nArray.length - n8 - 1));
                }
                linkedDimGroup.layout(dimConstraint, n2, n10, linkedDimGroup.span);
            }
            n2 += bl2 ? -n9 : n9;
        }
    }

    private static void addToSizeGroup(HashMap<String, int[]> hashMap, String string, int[] nArray) {
        int[] nArray2 = hashMap.get(string);
        if (nArray2 == null) {
            hashMap.put(string, new int[]{nArray[0], nArray[1], nArray[2]});
        } else {
            nArray2[0] = Math.max(nArray[0], nArray2[0]);
            nArray2[1] = Math.max(nArray[1], nArray2[1]);
            nArray2[2] = Math.min(nArray[2], nArray2[2]);
        }
    }

    private static HashMap<String, Integer> addToEndGroup(HashMap<String, Integer> hashMap, String string, int n) {
        if (string != null) {
            Integer n2;
            if (hashMap == null) {
                hashMap = new HashMap(2);
            }
            if ((n2 = hashMap.get(string)) == null || n > n2) {
                hashMap.put(string, new Integer(n));
            }
        }
        return hashMap;
    }

    private FlowSizeSpec calcRowsOrColsSizes(boolean bl) {
        Object object;
        Object object2;
        int n;
        BoundSize boundSize;
        ArrayList<LinkedDimGroup>[] arrayListArray = bl ? this.colGroupLists : this.rowGroupLists;
        Float[] floatArray = bl ? this.pushXs : this.pushYs;
        int n2 = bl ? this.container.getWidth() : this.container.getHeight();
        BoundSize boundSize2 = boundSize = bl ? this.lc.getWidth() : this.lc.getHeight();
        if (!boundSize.isUnset()) {
            n2 = boundSize.constrain(n2, Grid.getParentSize(this.container, bl), this.container);
        }
        DimConstraint[] dimConstraintArray = (bl ? this.colConstr : this.rowConstr).getConstaints();
        TreeSet<Integer> treeSet = bl ? this.colIndexes : this.rowIndexes;
        int[][] nArrayArray = new int[treeSet.size()][];
        HashMap<String, int[]> hashMap = new HashMap<String, int[]>(2);
        DimConstraint[] dimConstraintArray2 = new DimConstraint[treeSet.size()];
        Iterator<Integer> iterator = treeSet.iterator();
        for (n = 0; n < nArrayArray.length; ++n) {
            int n3 = iterator.next();
            object2 = new int[3];
            dimConstraintArray2[n] = n3 >= -30000 && n3 <= 30000 ? dimConstraintArray[n3 >= dimConstraintArray.length ? dimConstraintArray.length - 1 : n3] : DOCK_DIM_CONSTRAINT;
            object = arrayListArray[n];
            int[] nArray = new int[]{Grid.getTotalGroupsSizeParallel(object, 0, false), Grid.getTotalGroupsSizeParallel((ArrayList<LinkedDimGroup>)object, 1, false), 2097051};
            Grid.correctMinMax(nArray);
            BoundSize boundSize3 = dimConstraintArray2[n].getSize();
            for (int i = 0; i <= 2; ++i) {
                int n4 = nArray[i];
                UnitValue unitValue = boundSize3.getSize(i);
                if (unitValue != null) {
                    int n5 = unitValue.getUnit();
                    n4 = n5 == 14 ? nArray[1] : (n5 == 13 ? nArray[0] : (n5 == 15 ? nArray[2] : unitValue.getPixels(n2, this.container, null)));
                } else if (n3 >= -30000 && n3 <= 30000 && n4 == 0) {
                    n4 = LayoutUtil.isDesignTime(this.container) ? LayoutUtil.getDesignTimeEmptySize() : 0;
                }
                object2[i] = n4;
            }
            Grid.correctMinMax(object2);
            Grid.addToSizeGroup(hashMap, dimConstraintArray2[n].getSizeGroup(), object2);
            nArrayArray[n] = object2;
        }
        if (hashMap.size() > 0) {
            for (n = 0; n < nArrayArray.length; ++n) {
                if (dimConstraintArray2[n].getSizeGroup() == null) continue;
                nArrayArray[n] = (int[])hashMap.get(dimConstraintArray2[n].getSizeGroup());
            }
        }
        ResizeConstraint[] resizeConstraintArray = Grid.getRowResizeConstraints(dimConstraintArray2);
        boolean[] blArray = new boolean[dimConstraintArray2.length + 1];
        object2 = this.getRowGaps(dimConstraintArray2, n2, bl, blArray);
        object = Grid.mergeSizesGapsAndResConstrs(resizeConstraintArray, blArray, nArrayArray, (int[][])object2);
        this.adjustMinPrefForSpanningComps(dimConstraintArray2, floatArray, (FlowSizeSpec)object, arrayListArray);
        return object;
    }

    private static int getParentSize(ComponentWrapper componentWrapper, boolean bl) {
        ContainerWrapper containerWrapper = componentWrapper.getParent();
        return containerWrapper != null ? (bl ? componentWrapper.getWidth() : componentWrapper.getHeight()) : 0;
    }

    private int[] getMinPrefMaxSumSize(boolean bl) {
        int[][] nArray = bl ? this.colFlowSpecs.sizes : this.rowFlowSpecs.sizes;
        int[] nArray2 = new int[3];
        BoundSize boundSize = bl ? this.lc.getWidth() : this.lc.getHeight();
        for (int i = 0; i < nArray.length; ++i) {
            if (nArray[i] == null) continue;
            int[] nArray3 = nArray[i];
            for (int j = 0; j <= 2; ++j) {
                if (boundSize.getSize(j) != null) {
                    if (i != 0) continue;
                    nArray2[j] = boundSize.getSize(j).getPixels(Grid.getParentSize(this.container, bl), this.container, null);
                    continue;
                }
                int n = nArray3[j];
                if (n != -2147471302) {
                    if (j == 1) {
                        int n2 = nArray3[2];
                        if (n2 != -2147471302 && n2 < n) {
                            n = n2;
                        }
                        if ((n2 = nArray3[0]) > n) {
                            n = n2;
                        }
                    }
                    int n3 = j;
                    nArray2[n3] = nArray2[n3] + n;
                }
                if (nArray3[2] != -2147471302 && nArray2[2] <= 2097051) continue;
                nArray2[2] = 2097051;
            }
        }
        Grid.correctMinMax(nArray2);
        return nArray2;
    }

    private static ResizeConstraint[] getRowResizeConstraints(DimConstraint[] dimConstraintArray) {
        ResizeConstraint[] resizeConstraintArray = new ResizeConstraint[dimConstraintArray.length];
        for (int i = 0; i < resizeConstraintArray.length; ++i) {
            resizeConstraintArray[i] = dimConstraintArray[i].resize;
        }
        return resizeConstraintArray;
    }

    private static ResizeConstraint[] getComponentResizeConstraints(ArrayList<CompWrap> arrayList, boolean bl) {
        ResizeConstraint[] resizeConstraintArray = new ResizeConstraint[arrayList.size()];
        for (int i = 0; i < resizeConstraintArray.length; ++i) {
            CC cC = arrayList.get(i).cc;
            resizeConstraintArray[i] = cC.getDimConstraint((boolean)bl).resize;
            int n = cC.getDockSide();
            if (!(bl ? n == 0 || n == 2 : n == 1 || n == 3)) continue;
            ResizeConstraint resizeConstraint = resizeConstraintArray[i];
            resizeConstraintArray[i] = new ResizeConstraint(resizeConstraint.shrinkPrio, resizeConstraint.shrink, resizeConstraint.growPrio, ResizeConstraint.WEIGHT_100);
        }
        return resizeConstraintArray;
    }

    private static boolean[] getComponentGapPush(ArrayList<CompWrap> arrayList, boolean bl) {
        boolean[] blArray = new boolean[arrayList.size() + 1];
        for (int i = 0; i < blArray.length; ++i) {
            boolean bl2;
            boolean bl3 = bl2 = i > 0 ? arrayList.get(i - 1).isPushGap(bl, false) : false;
            if (!bl2 && i < blArray.length - 1) {
                bl2 = arrayList.get(i).isPushGap(bl, true);
            }
            blArray[i] = bl2;
        }
        return blArray;
    }

    private int[][] getRowGaps(DimConstraint[] dimConstraintArray, int n, boolean bl, boolean[] blArray) {
        BoundSize boundSize;
        BoundSize boundSize2 = boundSize = bl ? this.lc.getGridGapX() : this.lc.getGridGapY();
        if (boundSize == null) {
            boundSize = bl ? PlatformDefaults.getGridGapX() : PlatformDefaults.getGridGapY();
        }
        int[] nArray = boundSize.getPixelSizes(n, this.container, null);
        boolean bl2 = !this.hasDocks();
        UnitValue unitValue = LayoutUtil.getInsets(this.lc, bl ? 1 : 0, bl2);
        UnitValue unitValue2 = LayoutUtil.getInsets(this.lc, bl ? 3 : 2, bl2);
        int[][] nArrayArray = new int[dimConstraintArray.length + 1][];
        int n2 = 0;
        for (int i = 0; i < nArrayArray.length; ++i) {
            BoundSize boundSize3;
            boolean bl3;
            DimConstraint dimConstraint = i > 0 ? dimConstraintArray[i - 1] : null;
            DimConstraint dimConstraint2 = i < dimConstraintArray.length ? dimConstraintArray[i] : null;
            boolean bl4 = dimConstraint == DOCK_DIM_CONSTRAINT || dimConstraint == null;
            boolean bl5 = bl3 = dimConstraint2 == DOCK_DIM_CONSTRAINT || dimConstraint2 == null;
            if (bl4 && bl3) continue;
            BoundSize boundSize4 = boundSize3 = this.wrapGapMap == null || bl == this.lc.isFlowX() ? null : this.wrapGapMap.get(new Integer(n2++));
            if (boundSize3 == null) {
                int n3;
                int[] nArray2;
                int[] nArray3 = dimConstraint != null ? dimConstraint.getRowGaps(this.container, null, n, false) : null;
                int[] nArray4 = nArray2 = dimConstraint2 != null ? dimConstraint2.getRowGaps(this.container, null, n, true) : null;
                if (bl4 && nArray2 == null && unitValue != null) {
                    n3 = unitValue.getPixels(n, this.container, null);
                    nArrayArray[i] = new int[]{n3, n3, n3};
                } else if (bl3 && nArray3 == null && unitValue != null) {
                    n3 = unitValue2.getPixels(n, this.container, null);
                    nArrayArray[i] = new int[]{n3, n3, n3};
                } else {
                    int[] nArray5;
                    if (nArray2 != nArray3) {
                        nArray5 = Grid.mergeSizes(nArray2, nArray3);
                    } else {
                        int[] nArray6 = new int[3];
                        nArray6[0] = nArray[0];
                        nArray6[1] = nArray[1];
                        nArray5 = nArray6;
                        nArray6[2] = nArray[2];
                    }
                    nArrayArray[i] = nArray5;
                }
                if ((dimConstraint == null || !dimConstraint.isGapAfterPush()) && (dimConstraint2 == null || !dimConstraint2.isGapBeforePush())) continue;
                blArray[i] = true;
                continue;
            }
            nArrayArray[i] = boundSize3.isUnset() ? new int[]{nArray[0], nArray[1], nArray[2]} : boundSize3.getPixelSizes(n, this.container, null);
            blArray[i] = boundSize3.getGapPush();
        }
        return nArrayArray;
    }

    private static int[][] getGaps(ArrayList<CompWrap> arrayList, boolean bl) {
        int n = arrayList.size();
        int[][] nArrayArray = new int[n + 1][];
        nArrayArray[0] = arrayList.get(0).getGaps(bl, true);
        for (int i = 0; i < n; ++i) {
            int[] nArray = arrayList.get(i).getGaps(bl, false);
            int[] nArray2 = i < n - 1 ? arrayList.get(i + 1).getGaps(bl, true) : null;
            nArrayArray[i + 1] = Grid.mergeSizes(nArray, nArray2);
        }
        return nArrayArray;
    }

    private boolean hasDocks() {
        return this.dockOffX > 0 || this.dockOffY > 0 || this.rowIndexes.last() > 30000 || this.colIndexes.last() > 30000;
    }

    private void adjustMinPrefForSpanningComps(DimConstraint[] dimConstraintArray, Float[] floatArray, FlowSizeSpec flowSizeSpec, ArrayList<LinkedDimGroup>[] arrayListArray) {
        for (int i = arrayListArray.length - 1; i >= 0; --i) {
            ArrayList<LinkedDimGroup> arrayList = arrayListArray[i];
            for (int j = 0; j < arrayList.size(); ++j) {
                LinkedDimGroup linkedDimGroup = arrayList.get(j);
                if (linkedDimGroup.span == 1) continue;
                int[] nArray = linkedDimGroup.getMinPrefMax();
                for (int k = 0; k <= 1; ++k) {
                    int n;
                    int n2;
                    int n3 = nArray[k];
                    if (n3 == -2147471302) continue;
                    int n4 = 0;
                    int n5 = (i << 1) + 1;
                    int n6 = Math.min(linkedDimGroup.span << 1, flowSizeSpec.sizes.length - n5) - 1;
                    for (n2 = n5; n2 < n5 + n6; ++n2) {
                        n = flowSizeSpec.sizes[n2][k];
                        if (n == -2147471302) continue;
                        n4 += n;
                    }
                    if (n4 >= n3) continue;
                    n = 0;
                    for (n2 = 0; n2 < 4 && n < n3; ++n2) {
                        n = flowSizeSpec.expandSizes(dimConstraintArray, floatArray, n3, n5, n6, k, n2);
                    }
                }
            }
        }
    }

    private ArrayList<LinkedDimGroup>[] divideIntoLinkedGroups(boolean bl) {
        boolean bl2 = !(!bl ? LayoutUtil.isLeftToRight(this.lc, this.container) : this.lc.isTopToBottom());
        TreeSet<Integer> treeSet = bl ? this.rowIndexes : this.colIndexes;
        TreeSet<Integer> treeSet2 = bl ? this.colIndexes : this.rowIndexes;
        DimConstraint[] dimConstraintArray = (bl ? this.rowConstr : this.colConstr).getConstaints();
        ArrayList[] arrayListArray = new ArrayList[treeSet.size()];
        int n = 0;
        for (int n2 : treeSet) {
            DimConstraint dimConstraint = n2 >= -30000 && n2 <= 30000 ? dimConstraintArray[n2 >= dimConstraintArray.length ? dimConstraintArray.length - 1 : n2] : DOCK_DIM_CONSTRAINT;
            ArrayList<Object> arrayList = new ArrayList<Object>(2);
            arrayListArray[n++] = arrayList;
            for (int n3 : treeSet2) {
                Object object;
                int n4;
                boolean bl3;
                int n5;
                Cell cell = bl ? this.getCell(n2, n3) : this.getCell(n3, n2);
                if (cell == null || cell.compWraps.size() == 0) continue;
                int n6 = n5 = bl ? cell.spany : cell.spanx;
                if (n5 > 1) {
                    n5 = Grid.convertSpanToSparseGrid(n2, n5, treeSet);
                }
                boolean bl4 = bl3 = cell.flowx == bl;
                if (!bl3 && cell.compWraps.size() > 1 || n5 > 1) {
                    n4 = bl3 ? 1 : 0;
                    object = new LinkedDimGroup("p," + n3, n5, n4, !bl, bl2);
                    ((LinkedDimGroup)object).setCompWraps(cell.compWraps);
                    arrayList.add(object);
                    continue;
                }
                for (n4 = 0; n4 < cell.compWraps.size(); ++n4) {
                    int n7;
                    object = (CompWrap)cell.compWraps.get(n4);
                    boolean bl5 = bl && this.lc.isTopToBottom() && dimConstraint.getAlignOrDefault(!bl) == UnitValue.BASELINE_IDENTITY;
                    boolean bl6 = bl && ((CompWrap)object).isBaselineAlign(bl5);
                    String string = bl6 ? "baseline" : null;
                    boolean bl7 = false;
                    int n8 = arrayList.size() - 1;
                    for (n7 = 0; n7 <= n8; ++n7) {
                        LinkedDimGroup linkedDimGroup = (LinkedDimGroup)arrayList.get(n7);
                        if (linkedDimGroup.linkCtx != string && (string == null || !string.equals(linkedDimGroup.linkCtx))) continue;
                        linkedDimGroup.addCompWrap((CompWrap)object);
                        bl7 = true;
                        break;
                    }
                    if (bl7) continue;
                    n7 = bl6 ? 2 : 1;
                    LinkedDimGroup linkedDimGroup = new LinkedDimGroup(string, 1, n7, !bl, bl2);
                    linkedDimGroup.addCompWrap((CompWrap)object);
                    arrayList.add(linkedDimGroup);
                }
            }
        }
        return arrayListArray;
    }

    private static int convertSpanToSparseGrid(int n, int n2, TreeSet<Integer> treeSet) {
        int n3 = n + n2;
        int n4 = 1;
        for (int n5 : treeSet) {
            if (n5 <= n) continue;
            if (n5 >= n3) break;
            ++n4;
        }
        return n4;
    }

    private final boolean isCellFree(int n, int n2, ArrayList<int[]> arrayList) {
        if (this.getCell(n, n2) != null) {
            return false;
        }
        for (int i = 0; i < arrayList.size(); ++i) {
            int[] nArray = arrayList.get(i);
            if (nArray[0] > n2 || nArray[1] > n || nArray[0] + nArray[2] <= n2 || nArray[1] + nArray[3] <= n) continue;
            return false;
        }
        return true;
    }

    private Cell getCell(int n, int n2) {
        return this.grid.get(new Integer((n << 16) + n2));
    }

    private void setCell(int n, int n2, Cell cell) {
        if (n2 < 0 || n2 > 30000 || n < 0 || n > 30000) {
            throw new IllegalArgumentException("Cell position out of bounds. row: " + n + ", col: " + n2);
        }
        this.rowIndexes.add(new Integer(n));
        this.colIndexes.add(new Integer(n2));
        this.grid.put(new Integer((n << 16) + n2), cell);
    }

    private void addDockingCell(int[] nArray, int n, CompWrap compWrap) {
        int n2;
        int n3;
        int n4 = 1;
        int n5 = 1;
        switch (n) {
            case 0: 
            case 2: {
                int n6;
                if (n == 0) {
                    int n7 = nArray[0];
                    n6 = n7;
                    nArray[0] = n7 + 1;
                } else {
                    int n8 = nArray[2];
                    n6 = n8;
                    nArray[2] = n8 - 1;
                }
                n3 = n6;
                n2 = nArray[1];
                n4 = nArray[3] - nArray[1] + 1;
                this.colIndexes.add(new Integer(nArray[3]));
                break;
            }
            case 1: 
            case 3: {
                int n9;
                if (n == 1) {
                    int n10 = nArray[1];
                    n9 = n10;
                    nArray[1] = n10 + 1;
                } else {
                    int n11 = nArray[3];
                    n9 = n11;
                    nArray[3] = n11 - 1;
                }
                n2 = n9;
                n3 = nArray[0];
                n5 = nArray[2] - nArray[0] + 1;
                this.rowIndexes.add(new Integer(nArray[2]));
                break;
            }
            default: {
                throw new IllegalArgumentException("Internal error 123.");
            }
        }
        this.rowIndexes.add(new Integer(n3));
        this.colIndexes.add(new Integer(n2));
        this.grid.put(new Integer((n3 << 16) + n2), new Cell(compWrap, n4, n5, n4 > 1));
    }

    private static void layoutBaseline(ContainerWrapper containerWrapper, ArrayList<CompWrap> arrayList, DimConstraint dimConstraint, int n, int n2, int n3, int n4) {
        int[] nArray = Grid.getBaselineAboveBelow(arrayList, n3, true);
        int n5 = nArray[0] + nArray[1];
        CC cC = arrayList.get(0).cc;
        UnitValue unitValue = cC.getVertical().getAlign();
        if (n4 == 1 && unitValue == null) {
            unitValue = dimConstraint.getAlignOrDefault(false);
        }
        if (unitValue == UnitValue.BASELINE_IDENTITY) {
            unitValue = UnitValue.CENTER;
        }
        int n6 = n + nArray[0] + (unitValue != null ? Math.max(0, unitValue.getPixels(n2 - n5, containerWrapper, null)) : 0);
        int n7 = arrayList.size();
        for (int i = 0; i < n7; ++i) {
            CompWrap compWrap = arrayList.get(i);
            compWrap.y += n6;
            if (compWrap.y + compWrap.h <= n + n2) continue;
            compWrap.h = n + n2 - compWrap.y;
        }
    }

    private static void layoutSerial(ContainerWrapper containerWrapper, ArrayList<CompWrap> arrayList, DimConstraint dimConstraint, int n, int n2, boolean bl, int n3, boolean bl2) {
        FlowSizeSpec flowSizeSpec = Grid.mergeSizesGapsAndResConstrs(Grid.getComponentResizeConstraints(arrayList, bl), Grid.getComponentGapPush(arrayList, bl), Grid.getComponentSizes(arrayList, bl), Grid.getGaps(arrayList, bl));
        Float[] floatArray = dimConstraint.isFill() ? GROW_100 : null;
        int[] nArray = LayoutUtil.calculateSerial(flowSizeSpec.sizes, flowSizeSpec.resConstsInclGaps, floatArray, 1, n2);
        Grid.setCompWrapBounds(containerWrapper, nArray, arrayList, dimConstraint.getAlignOrDefault(bl), n, n2, bl, bl2);
    }

    private static void setCompWrapBounds(ContainerWrapper containerWrapper, int[] nArray, ArrayList<CompWrap> arrayList, UnitValue unitValue, int n, int n2, boolean bl, boolean bl2) {
        int n3;
        int n4 = LayoutUtil.sum(nArray);
        CC cC = arrayList.get(0).cc;
        UnitValue unitValue2 = Grid.correctAlign(cC, unitValue, bl, bl2);
        int n5 = n;
        int n6 = n2 - n4;
        if (n6 > 0 && unitValue2 != null) {
            n3 = Math.min(n6, Math.max(0, unitValue2.getPixels(n6, containerWrapper, null)));
            n5 += bl2 ? -n3 : n3;
        }
        int n7 = 0;
        int n8 = arrayList.size();
        for (n3 = 0; n3 < n8; ++n3) {
            CompWrap compWrap = arrayList.get(n3);
            if (bl2) {
                compWrap.setDimBounds((n5 -= nArray[n7++]) - nArray[n7], nArray[n7], bl);
                n5 -= nArray[n7++];
                continue;
            }
            compWrap.setDimBounds(n5 += nArray[n7++], nArray[n7], bl);
            n5 += nArray[n7++];
        }
    }

    private static void layoutParallel(ContainerWrapper containerWrapper, ArrayList<CompWrap> arrayList, DimConstraint dimConstraint, int n, int n2, boolean bl, boolean bl2) {
        int[][] nArrayArray = new int[arrayList.size()][];
        for (int i = 0; i < nArrayArray.length; ++i) {
            CompWrap compWrap = arrayList.get(i);
            DimConstraint dimConstraint2 = compWrap.cc.getDimConstraint(bl);
            ResizeConstraint[] resizeConstraintArray = new ResizeConstraint[]{compWrap.isPushGap(bl, true) ? GAP_RC_CONST_PUSH : GAP_RC_CONST, dimConstraint2.resize, compWrap.isPushGap(bl, false) ? GAP_RC_CONST_PUSH : GAP_RC_CONST};
            int[][] nArrayArray2 = new int[][]{compWrap.getGaps(bl, true), bl ? compWrap.horSizes : compWrap.verSizes, compWrap.getGaps(bl, false)};
            Float[] floatArray = dimConstraint.isFill() ? GROW_100 : null;
            nArrayArray[i] = LayoutUtil.calculateSerial(nArrayArray2, resizeConstraintArray, floatArray, 1, n2);
        }
        UnitValue unitValue = dimConstraint.getAlignOrDefault(bl);
        Grid.setCompWrapBounds(containerWrapper, nArrayArray, arrayList, unitValue, n, n2, bl, bl2);
    }

    private static void setCompWrapBounds(ContainerWrapper containerWrapper, int[][] nArray, ArrayList<CompWrap> arrayList, UnitValue unitValue, int n, int n2, boolean bl, boolean bl2) {
        for (int i = 0; i < nArray.length; ++i) {
            CompWrap compWrap = arrayList.get(i);
            UnitValue unitValue2 = Grid.correctAlign(compWrap.cc, unitValue, bl, bl2);
            int[] nArray2 = nArray[i];
            int n3 = nArray2[0];
            int n4 = nArray2[1];
            int n5 = nArray2[2];
            int n6 = bl2 ? n - n3 : n + n3;
            int n7 = n2 - n4 - n3 - n5;
            if (n7 > 0 && unitValue2 != null) {
                int n8 = Math.min(n7, Math.max(0, unitValue2.getPixels(n7, containerWrapper, null)));
                n6 += bl2 ? -n8 : n8;
            }
            compWrap.setDimBounds(bl2 ? n6 - n4 : n6, n4, bl);
        }
    }

    private static UnitValue correctAlign(CC cC, UnitValue unitValue, boolean bl, boolean bl2) {
        UnitValue unitValue2 = (bl ? cC.getHorizontal() : cC.getVertical()).getAlign();
        if (unitValue2 == null) {
            unitValue2 = unitValue;
        }
        if (unitValue2 == UnitValue.BASELINE_IDENTITY) {
            unitValue2 = UnitValue.CENTER;
        }
        if (bl2) {
            if (unitValue2 == UnitValue.LEFT) {
                unitValue2 = UnitValue.RIGHT;
            } else if (unitValue2 == UnitValue.RIGHT) {
                unitValue2 = UnitValue.LEFT;
            }
        }
        return unitValue2;
    }

    private static int[] getBaselineAboveBelow(ArrayList<CompWrap> arrayList, int n, boolean bl) {
        int n2 = Short.MIN_VALUE;
        int n3 = Short.MIN_VALUE;
        int n4 = arrayList.size();
        for (int i = 0; i < n4; ++i) {
            CompWrap compWrap = arrayList.get(i);
            int n5 = compWrap.getSize(n, false);
            if (n5 >= 2097051) {
                return new int[]{1048525, 1048525};
            }
            int n6 = compWrap.getBaseline(n);
            int n7 = n6 + compWrap.getGapBefore(n, false);
            n2 = Math.max(n7, n2);
            n3 = Math.max(n5 - n6 + compWrap.getGapAfter(n, false), n3);
            if (!bl) continue;
            compWrap.setDimBounds(-n6, n5, false);
        }
        return new int[]{n2, n3};
    }

    private static int getTotalSizeParallel(ArrayList<CompWrap> arrayList, int n, boolean bl) {
        int n2 = n == 2 ? 2097051 : 0;
        int n3 = arrayList.size();
        for (int i = 0; i < n3; ++i) {
            CompWrap compWrap = arrayList.get(i);
            int n4 = compWrap.getSizeInclGaps(n, bl);
            if (n4 >= 2097051) {
                return 2097051;
            }
            if (!(n == 2 ? n4 < n2 : n4 > n2)) continue;
            n2 = n4;
        }
        return Grid.constrainSize(n2);
    }

    private static final int getTotalSizeSerial(ArrayList<CompWrap> arrayList, int n, boolean bl) {
        int n2 = 0;
        int n3 = arrayList.size();
        int n4 = 0;
        for (int i = 0; i < n3; ++i) {
            CompWrap compWrap = arrayList.get(i);
            int n5 = compWrap.getGapBefore(n, bl);
            if (n5 > n4) {
                n2 += n5 - n4;
            }
            n2 += compWrap.getSize(n, bl);
            n4 = compWrap.getGapAfter(n, bl);
            if ((n2 += n4) < 2097051) continue;
            return 2097051;
        }
        return Grid.constrainSize(n2);
    }

    private static final int getTotalGroupsSizeParallel(ArrayList<LinkedDimGroup> arrayList, int n, boolean bl) {
        int n2 = n == 2 ? 2097051 : 0;
        int n3 = arrayList.size();
        for (int i = 0; i < n3; ++i) {
            LinkedDimGroup linkedDimGroup = arrayList.get(i);
            if (!bl && linkedDimGroup.span != 1) continue;
            int n4 = linkedDimGroup.getMinPrefMax()[n];
            if (n4 >= 2097051) {
                return 2097051;
            }
            if (!(n == 2 ? n4 < n2 : n4 > n2)) continue;
            n2 = n4;
        }
        return Grid.constrainSize(n2);
    }

    private static int[][] getComponentSizes(ArrayList<CompWrap> arrayList, boolean bl) {
        int[][] nArrayArray = new int[arrayList.size()][];
        for (int i = 0; i < nArrayArray.length; ++i) {
            CompWrap compWrap = arrayList.get(i);
            nArrayArray[i] = bl ? compWrap.horSizes : compWrap.verSizes;
        }
        return nArrayArray;
    }

    private static FlowSizeSpec mergeSizesGapsAndResConstrs(ResizeConstraint[] resizeConstraintArray, boolean[] blArray, int[][] nArray, int[][] nArray2) {
        int[][] nArrayArray = new int[(nArray.length << 1) + 1][];
        ResizeConstraint[] resizeConstraintArray2 = new ResizeConstraint[nArrayArray.length];
        nArrayArray[0] = nArray2[0];
        int n = 0;
        int n2 = 1;
        while (n < nArray.length) {
            resizeConstraintArray2[n2] = resizeConstraintArray[n];
            nArrayArray[n2] = nArray[n];
            nArrayArray[n2 + 1] = nArray2[n + 1];
            if (nArrayArray[n2 - 1] != null) {
                ResizeConstraint resizeConstraint = resizeConstraintArray2[n2 - 1] = blArray[n < blArray.length ? n : blArray.length - 1] ? GAP_RC_CONST_PUSH : GAP_RC_CONST;
            }
            if (n == nArray.length - 1 && nArrayArray[n2 + 1] != null) {
                resizeConstraintArray2[n2 + 1] = blArray[n + 1 < blArray.length ? n + 1 : blArray.length - 1] ? GAP_RC_CONST_PUSH : GAP_RC_CONST;
            }
            ++n;
            n2 += 2;
        }
        for (n = 0; n < nArrayArray.length; ++n) {
            if (nArrayArray[n] != null) continue;
            nArrayArray[n] = new int[3];
        }
        return new FlowSizeSpec(nArrayArray, resizeConstraintArray2);
    }

    private static final int[] mergeSizes(int[] nArray, int[] nArray2) {
        if (nArray == null) {
            return nArray2;
        }
        if (nArray2 == null) {
            return nArray;
        }
        int[] nArray3 = new int[nArray.length];
        for (int i = 0; i < nArray3.length; ++i) {
            nArray3[i] = Grid.mergeSizes(nArray[i], nArray2[i], true);
        }
        return nArray3;
    }

    private static final int mergeSizes(int n, int n2, boolean bl) {
        if (n == -2147471302 || n == n2) {
            return n2;
        }
        if (n2 == -2147471302) {
            return n;
        }
        return bl != n > n2 ? n2 : n;
    }

    private static final int constrainSize(int n) {
        return n > 0 ? (n < 2097051 ? n : 2097051) : 0;
    }

    private static final void correctMinMax(int[] nArray) {
        if (nArray[0] > nArray[2]) {
            nArray[0] = nArray[2];
        }
        if (nArray[1] < nArray[0]) {
            nArray[1] = nArray[0];
        }
        if (nArray[1] > nArray[2]) {
            nArray[1] = nArray[2];
        }
    }

    private static Float[] extractSubArray(DimConstraint[] dimConstraintArray, Float[] floatArray, int n, int n2) {
        if (floatArray == null || floatArray.length < n + n2) {
            Float[] floatArray2 = new Float[n2];
            for (int i = n + n2 - 1; i >= 0; i -= 2) {
                int n3 = i >> 1;
                if (dimConstraintArray[n3] == DOCK_DIM_CONSTRAINT) continue;
                floatArray2[i - n] = ResizeConstraint.WEIGHT_100;
                return floatArray2;
            }
            return floatArray2;
        }
        Float[] floatArray3 = new Float[n2];
        for (int i = 0; i < n2; ++i) {
            floatArray3[i] = floatArray[n + i];
        }
        return floatArray3;
    }

    private static synchronized void putSizesAndIndexes(Object object, int[] nArray, int[] nArray2, boolean bl) {
        if (PARENT_ROWCOL_SIZES_MAP == null) {
            PARENT_ROWCOL_SIZES_MAP = new WeakHashMap[]{new WeakHashMap(4), new WeakHashMap(4)};
        }
        PARENT_ROWCOL_SIZES_MAP[bl ? 0 : 1].put(object, new int[][]{nArray2, nArray});
    }

    static synchronized int[][] getSizesAndIndexes(Object object, boolean bl) {
        if (PARENT_ROWCOL_SIZES_MAP == null) {
            return null;
        }
        return (int[][])PARENT_ROWCOL_SIZES_MAP[bl ? 0 : 1].get(object);
    }

    private static synchronized void saveGrid(ComponentWrapper componentWrapper, LinkedHashMap<Integer, Cell> linkedHashMap) {
        if (PARENT_GRIDPOS_MAP == null) {
            PARENT_GRIDPOS_MAP = new WeakHashMap();
        }
        PARENT_GRIDPOS_MAP.put(componentWrapper.getComponent(), linkedHashMap);
    }

    static synchronized HashMap<Object, int[]> getGridPositions(Object object) {
        if (PARENT_GRIDPOS_MAP == null) {
            return null;
        }
        LinkedHashMap<Integer, Cell> linkedHashMap = PARENT_GRIDPOS_MAP.get(object);
        if (linkedHashMap == null) {
            return null;
        }
        HashMap<Object, int[]> hashMap = new HashMap<Object, int[]>();
        for (Map.Entry<Integer, Cell> entry : linkedHashMap.entrySet()) {
            Cell cell = entry.getValue();
            Integer n = entry.getKey();
            if (n == null) continue;
            int n2 = n;
            int n3 = n2 & 0xFFFF;
            int n4 = n2 >> 16;
            for (CompWrap compWrap : cell.compWraps) {
                hashMap.put(compWrap.comp.getComponent(), new int[]{n3, n4, cell.spanx, cell.spany});
            }
        }
        return hashMap;
    }

    static {
        DOCK_DIM_CONSTRAINT.setGrowPriority(0);
        GAP_RC_CONST = new ResizeConstraint(200, ResizeConstraint.WEIGHT_100, 50, null);
        GAP_RC_CONST_PUSH = new ResizeConstraint(200, ResizeConstraint.WEIGHT_100, 50, ResizeConstraint.WEIGHT_100);
        PARENT_ROWCOL_SIZES_MAP = null;
        PARENT_GRIDPOS_MAP = null;
    }

    private static final class FlowSizeSpec {
        private final int[][] sizes;
        private final ResizeConstraint[] resConstsInclGaps;

        private FlowSizeSpec(int[][] nArray, ResizeConstraint[] resizeConstraintArray) {
            this.sizes = nArray;
            this.resConstsInclGaps = resizeConstraintArray;
        }

        private int expandSizes(DimConstraint[] dimConstraintArray, Float[] floatArray, int n, int n2, int n3, int n4, int n5) {
            int n6;
            int[] nArray;
            ResizeConstraint[] resizeConstraintArray = new ResizeConstraint[n3];
            int[][] nArrayArray = new int[n3][];
            for (int i = 0; i < n3; ++i) {
                nArray = this.sizes[i + n2];
                nArrayArray[i] = new int[]{nArray[n4], nArray[1], nArray[2]};
                if (n5 <= 1 && i % 2 == 0) {
                    n6 = i + n2 - 1 >> 1;
                    DimConstraint dimConstraint = (DimConstraint)LayoutUtil.getIndexSafe(dimConstraintArray, n6);
                    BoundSize boundSize = dimConstraint.getSize();
                    if (n4 == 0 && boundSize.getMin() != null && boundSize.getMin().getUnit() != 13 || n4 == 1 && boundSize.getPreferred() != null && boundSize.getPreferred().getUnit() != 14) continue;
                }
                resizeConstraintArray[i] = (ResizeConstraint)LayoutUtil.getIndexSafe(this.resConstsInclGaps, i + n2);
            }
            Float[] floatArray2 = n5 == 1 || n5 == 3 ? Grid.extractSubArray(dimConstraintArray, floatArray, n2, n3) : null;
            nArray = LayoutUtil.calculateSerial(nArrayArray, resizeConstraintArray, floatArray2, 1, n);
            n6 = 0;
            for (int i = 0; i < n3; ++i) {
                int n7;
                this.sizes[i + n2][n4] = n7 = nArray[i];
                n6 += n7;
            }
            return n6;
        }
    }

    private static final class CompWrap {
        private final ComponentWrapper comp;
        private final CC cc;
        private final UnitValue[] pos;
        private int[][] gaps;
        private final int[] horSizes = new int[3];
        private final int[] verSizes = new int[3];
        private int x = -2147471302;
        private int y = -2147471302;
        private int w = -2147471302;
        private int h = -2147471302;
        private int forcedPushGaps = 0;

        private CompWrap(ComponentWrapper componentWrapper, CC cC, int n, UnitValue[] unitValueArray, BoundSize[] boundSizeArray) {
            this.comp = componentWrapper;
            this.cc = cC;
            this.pos = unitValueArray;
            if (n <= 0) {
                BoundSize boundSize = boundSizeArray != null && boundSizeArray[0] != null ? boundSizeArray[0] : cC.getHorizontal().getSize();
                BoundSize boundSize2 = boundSizeArray != null && boundSizeArray[1] != null ? boundSizeArray[1] : cC.getVertical().getSize();
                int n2 = -1;
                int n3 = -1;
                if (this.comp.getWidth() > 0 && this.comp.getHeight() > 0) {
                    n3 = this.comp.getHeight();
                    n2 = this.comp.getWidth();
                }
                for (int i = 0; i <= 2; ++i) {
                    this.horSizes[i] = this.getSize(boundSize, i, true, n3);
                    this.verSizes[i] = this.getSize(boundSize2, i, false, n2 > 0 ? n2 : this.horSizes[i]);
                }
                Grid.correctMinMax(this.horSizes);
                Grid.correctMinMax(this.verSizes);
            }
            if (n > 1) {
                this.gaps = new int[4][];
                for (int i = 0; i < this.gaps.length; ++i) {
                    this.gaps[i] = new int[3];
                }
            }
        }

        private final int getSize(BoundSize boundSize, int n, boolean bl, int n2) {
            if (boundSize == null || boundSize.getSize(n) == null) {
                switch (n) {
                    case 0: {
                        return bl ? this.comp.getMinimumWidth(n2) : this.comp.getMinimumHeight(n2);
                    }
                    case 1: {
                        return bl ? this.comp.getPreferredWidth(n2) : this.comp.getPreferredHeight(n2);
                    }
                }
                return bl ? this.comp.getMaximumWidth(n2) : this.comp.getMaximumHeight(n2);
            }
            ContainerWrapper containerWrapper = this.comp.getParent();
            return boundSize.getSize(n).getPixels(bl ? (float)containerWrapper.getWidth() : (float)containerWrapper.getHeight(), containerWrapper, this.comp);
        }

        private final void calcGaps(ComponentWrapper componentWrapper, CC cC, ComponentWrapper componentWrapper2, CC cC2, String string, boolean bl, boolean bl2) {
            BoundSize boundSize;
            ContainerWrapper containerWrapper = this.comp.getParent();
            int n = containerWrapper.getWidth();
            int n2 = containerWrapper.getHeight();
            BoundSize boundSize2 = componentWrapper != null ? (bl ? cC.getHorizontal() : cC.getVertical()).getGapAfter() : (boundSize = null);
            BoundSize boundSize3 = componentWrapper2 != null ? (bl ? cC2.getHorizontal() : cC2.getVertical()).getGapBefore() : null;
            this.mergeGapSizes(this.cc.getVertical().getComponentGaps(containerWrapper, this.comp, boundSize, bl ? null : componentWrapper, string, n2, 0, bl2), false, true);
            this.mergeGapSizes(this.cc.getHorizontal().getComponentGaps(containerWrapper, this.comp, boundSize, bl ? componentWrapper : null, string, n, 1, bl2), true, true);
            this.mergeGapSizes(this.cc.getVertical().getComponentGaps(containerWrapper, this.comp, boundSize3, bl ? null : componentWrapper2, string, n2, 2, bl2), false, false);
            this.mergeGapSizes(this.cc.getHorizontal().getComponentGaps(containerWrapper, this.comp, boundSize3, bl ? componentWrapper2 : null, string, n, 3, bl2), true, false);
        }

        private final void setDimBounds(int n, int n2, boolean bl) {
            if (bl) {
                this.x = n;
                this.w = n2;
            } else {
                this.y = n;
                this.h = n2;
            }
        }

        private final boolean isPushGap(boolean bl, boolean bl2) {
            if (bl && ((bl2 ? 1 : 2) & this.forcedPushGaps) != 0) {
                return true;
            }
            DimConstraint dimConstraint = this.cc.getDimConstraint(bl);
            BoundSize boundSize = bl2 ? dimConstraint.getGapBefore() : dimConstraint.getGapAfter();
            return boundSize != null && boundSize.getGapPush();
        }

        private final boolean transferBounds(boolean bl) {
            BoundSize boundSize;
            this.comp.setBounds(this.x, this.y, this.w, this.h);
            return bl && this.w != this.horSizes[1] && (boundSize = this.cc.getVertical().getSize()).getPreferred() == null && this.comp.getPreferredHeight(-1) != this.verSizes[1];
        }

        private final void setSizes(int[] nArray, boolean bl) {
            if (nArray == null) {
                return;
            }
            int[] nArray2 = bl ? this.horSizes : this.verSizes;
            nArray2[0] = nArray[0];
            nArray2[1] = nArray[1];
            nArray2[2] = nArray[2];
        }

        private void setGaps(int[] nArray, int n) {
            if (this.gaps == null) {
                this.gaps = new int[][]{null, null, null, null};
            }
            this.gaps[n] = nArray;
        }

        private final void mergeGapSizes(int[] nArray, boolean bl, boolean bl2) {
            if (this.gaps == null) {
                this.gaps = new int[][]{null, null, null, null};
            }
            if (nArray == null) {
                return;
            }
            int n = this.getGapIx(bl, bl2);
            int[] nArray2 = this.gaps[n];
            if (nArray2 == null) {
                nArray2 = new int[]{0, 0, 2097051};
                this.gaps[n] = nArray2;
            }
            nArray2[0] = Math.max(nArray[0], nArray2[0]);
            nArray2[1] = Math.max(nArray[1], nArray2[1]);
            nArray2[2] = Math.min(nArray[2], nArray2[2]);
        }

        private final int getGapIx(boolean bl, boolean bl2) {
            return bl ? (bl2 ? 1 : 3) : (bl2 ? 0 : 2);
        }

        private final int getSizeInclGaps(int n, boolean bl) {
            return this.filter(n, this.getGapBefore(n, bl) + this.getSize(n, bl) + this.getGapAfter(n, bl));
        }

        private final int getSize(int n, boolean bl) {
            return this.filter(n, bl ? this.horSizes[n] : this.verSizes[n]);
        }

        private final int getGapBefore(int n, boolean bl) {
            int[] nArray = this.getGaps(bl, true);
            return nArray != null ? this.filter(n, nArray[n]) : 0;
        }

        private final int getGapAfter(int n, boolean bl) {
            int[] nArray = this.getGaps(bl, false);
            return nArray != null ? this.filter(n, nArray[n]) : 0;
        }

        private final int[] getGaps(boolean bl, boolean bl2) {
            return this.gaps[this.getGapIx(bl, bl2)];
        }

        private final int filter(int n, int n2) {
            if (n2 == -2147471302) {
                return n != 2 ? 0 : 2097051;
            }
            return Grid.constrainSize(n2);
        }

        private final boolean isBaselineAlign(boolean bl) {
            Float f = this.cc.getVertical().getGrow();
            if (f != null && f.intValue() != 0) {
                return false;
            }
            UnitValue unitValue = this.cc.getVertical().getAlign();
            return (unitValue != null ? unitValue == UnitValue.BASELINE_IDENTITY : bl) && this.comp.hasBaseline();
        }

        private final int getBaseline(int n) {
            return this.comp.getBaseline(this.getSize(n, true), this.getSize(n, false));
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class LinkedDimGroup {
        private static final int TYPE_SERIAL = 0;
        private static final int TYPE_PARALLEL = 1;
        private static final int TYPE_BASELINE = 2;
        private final String linkCtx;
        private final int span;
        private final int linkType;
        private final boolean isHor;
        private final boolean fromEnd;
        private ArrayList<CompWrap> _compWraps = new ArrayList(4);
        private int[] sizes = null;
        private int lStart = 0;
        private int lSize = 0;

        private LinkedDimGroup(String string, int n, int n2, boolean bl, boolean bl2) {
            this.linkCtx = string;
            this.span = n;
            this.linkType = n2;
            this.isHor = bl;
            this.fromEnd = bl2;
        }

        private void addCompWrap(CompWrap compWrap) {
            this._compWraps.add(compWrap);
            this.sizes = null;
        }

        private void setCompWraps(ArrayList<CompWrap> arrayList) {
            if (this._compWraps != arrayList) {
                this._compWraps = arrayList;
                this.sizes = null;
            }
        }

        private void layout(DimConstraint dimConstraint, int n, int n2, int n3) {
            this.lStart = n;
            this.lSize = n2;
            if (this._compWraps.size() == 0) {
                return;
            }
            ContainerWrapper containerWrapper = this._compWraps.get(0).comp.getParent();
            if (this.linkType == 1) {
                Grid.layoutParallel(containerWrapper, this._compWraps, dimConstraint, n, n2, this.isHor, this.fromEnd);
            } else if (this.linkType == 2) {
                Grid.layoutBaseline(containerWrapper, this._compWraps, dimConstraint, n, n2, 1, n3);
            } else {
                Grid.layoutSerial(containerWrapper, this._compWraps, dimConstraint, n, n2, this.isHor, n3, this.fromEnd);
            }
        }

        private int[] getMinPrefMax() {
            if (this.sizes == null && this._compWraps.size() > 0) {
                this.sizes = new int[3];
                for (int i = 0; i <= 1; ++i) {
                    if (this.linkType == 1) {
                        this.sizes[i] = Grid.getTotalSizeParallel(this._compWraps, i, this.isHor);
                        continue;
                    }
                    if (this.linkType == 2) {
                        int[] nArray = Grid.getBaselineAboveBelow(this._compWraps, i, false);
                        this.sizes[i] = nArray[0] + nArray[1];
                        continue;
                    }
                    this.sizes[i] = Grid.getTotalSizeSerial(this._compWraps, i, this.isHor);
                }
                this.sizes[2] = 2097051;
            }
            return this.sizes;
        }
    }

    private static class Cell {
        private final int spanx;
        private final int spany;
        private final boolean flowx;
        private final ArrayList<CompWrap> compWraps = new ArrayList(1);
        private boolean hasTagged = false;

        private Cell(CompWrap compWrap) {
            this(compWrap, 1, 1, true);
        }

        private Cell(int n, int n2, boolean bl) {
            this(null, n, n2, bl);
        }

        private Cell(CompWrap compWrap, int n, int n2, boolean bl) {
            if (compWrap != null) {
                this.compWraps.add(compWrap);
            }
            this.spanx = n;
            this.spany = n2;
            this.flowx = bl;
        }
    }
}

