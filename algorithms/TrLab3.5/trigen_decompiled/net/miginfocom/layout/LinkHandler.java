/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.layout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public final class LinkHandler {
    public static final int X = 0;
    public static final int Y = 1;
    public static final int WIDTH = 2;
    public static final int HEIGHT = 3;
    public static final int X2 = 4;
    public static final int Y2 = 5;
    private static final ArrayList<WeakReference<Object>> LAYOUTS = new ArrayList(4);
    private static final ArrayList<HashMap<String, int[]>> VALUES = new ArrayList(4);
    private static final ArrayList<HashMap<String, int[]>> VALUES_TEMP = new ArrayList(4);

    private LinkHandler() {
    }

    public static synchronized Integer getValue(Object object, String string, int n) {
        Integer n2 = null;
        boolean bl = true;
        for (int i = LAYOUTS.size() - 1; i >= 0; --i) {
            Object t = LAYOUTS.get(i).get();
            if (n2 == null && t == object) {
                int[] nArray = VALUES_TEMP.get(i).get(string);
                n2 = bl && nArray != null && nArray[n] != -2147471302 ? new Integer(nArray[n]) : ((nArray = VALUES.get(i).get(string)) != null && nArray[n] != -2147471302 ? new Integer(nArray[n]) : null);
                bl = false;
            }
            if (t != null) continue;
            LAYOUTS.remove(i);
            VALUES.remove(i);
            VALUES_TEMP.remove(i);
        }
        return n2;
    }

    public static synchronized boolean setBounds(Object object, String string, int n, int n2, int n3, int n4) {
        return LinkHandler.setBounds(object, string, n, n2, n3, n4, false, false);
    }

    static synchronized boolean setBounds(Object object, String string, int n, int n2, int n3, int n4, boolean bl, boolean bl2) {
        Object object2;
        for (int i = LAYOUTS.size() - 1; i >= 0; --i) {
            object2 = LAYOUTS.get(i).get();
            if (object2 != object) continue;
            HashMap<String, int[]> hashMap = (bl ? VALUES_TEMP : VALUES).get(i);
            int[] nArray = hashMap.get(string);
            if (nArray == null || nArray[0] != n || nArray[1] != n2 || nArray[2] != n3 || nArray[3] != n4) {
                int n5;
                if (nArray == null || !bl2) {
                    hashMap.put(string, new int[]{n, n2, n3, n4, n + n3, n2 + n4});
                    return true;
                }
                boolean bl3 = false;
                if (n != -2147471302) {
                    if (nArray[0] == -2147471302 || n < nArray[0]) {
                        nArray[0] = n;
                        nArray[2] = nArray[4] - n;
                        bl3 = true;
                    }
                    if (n3 != -2147471302) {
                        n5 = n + n3;
                        if (nArray[4] == -2147471302 || n5 > nArray[4]) {
                            nArray[4] = n5;
                            nArray[2] = n5 - nArray[0];
                            bl3 = true;
                        }
                    }
                }
                if (n2 != -2147471302) {
                    if (nArray[1] == -2147471302 || n2 < nArray[1]) {
                        nArray[1] = n2;
                        nArray[3] = nArray[5] - n2;
                        bl3 = true;
                    }
                    if (n4 != -2147471302) {
                        n5 = n2 + n4;
                        if (nArray[5] == -2147471302 || n5 > nArray[5]) {
                            nArray[5] = n5;
                            nArray[3] = n5 - nArray[1];
                            bl3 = true;
                        }
                    }
                }
                return bl3;
            }
            return false;
        }
        LAYOUTS.add(new WeakReference<Object>(object));
        int[] nArray = new int[]{n, n2, n3, n4, n + n3, n2 + n4};
        object2 = new HashMap(4);
        if (bl) {
            ((HashMap)object2).put((String)string, (int[])nArray);
        }
        VALUES_TEMP.add((HashMap<String, int[]>)object2);
        object2 = new HashMap(4);
        if (!bl) {
            ((HashMap)object2).put(string, nArray);
        }
        VALUES.add((HashMap<String, int[]>)object2);
        return true;
    }

    public static synchronized void clearWeakReferencesNow() {
        LAYOUTS.clear();
    }

    public static synchronized boolean clearBounds(Object object, String string) {
        for (int i = LAYOUTS.size() - 1; i >= 0; --i) {
            Object t = LAYOUTS.get(i).get();
            if (t != object) continue;
            return VALUES.get(i).remove(string) != null;
        }
        return false;
    }

    static synchronized void clearTemporaryBounds(Object object) {
        for (int i = LAYOUTS.size() - 1; i >= 0; --i) {
            Object t = LAYOUTS.get(i).get();
            if (t != object) continue;
            VALUES_TEMP.get(i).clear();
            return;
        }
    }
}

