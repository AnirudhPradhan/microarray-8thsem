/*
 * Decompiled with CFR 0.152.
 */
package net.miginfocom.layout;

import java.beans.Beans;
import java.beans.ExceptionListener;
import java.beans.Introspector;
import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.util.IdentityHashMap;
import java.util.TreeSet;
import java.util.WeakHashMap;
import net.miginfocom.layout.BoundSize;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.layout.ResizeConstraint;
import net.miginfocom.layout.UnitValue;

public final class LayoutUtil {
    static final int INF = 2097051;
    static final int NOT_SET = -2147471302;
    public static final int MIN = 0;
    public static final int PREF = 1;
    public static final int MAX = 2;
    private static WeakHashMap<Object, String> CR_MAP = null;
    private static WeakHashMap<Object, Boolean> DT_MAP = null;
    private static int eSz = 0;
    private static int globalDebugMillis = 0;
    private static ByteArrayOutputStream writeOutputStream = null;
    private static byte[] readBuf = null;
    private static final IdentityHashMap<Object, Object> SER_MAP = new IdentityHashMap(2);

    private LayoutUtil() {
    }

    public static String getVersion() {
        return "3.7.4";
    }

    public static int getGlobalDebugMillis() {
        return globalDebugMillis;
    }

    public static void setGlobalDebugMillis(int n) {
        globalDebugMillis = n;
    }

    public static void setDesignTime(ContainerWrapper containerWrapper, boolean bl) {
        if (DT_MAP == null) {
            DT_MAP = new WeakHashMap();
        }
        DT_MAP.put(containerWrapper != null ? containerWrapper.getComponent() : null, new Boolean(bl));
    }

    public static boolean isDesignTime(ContainerWrapper containerWrapper) {
        if (DT_MAP == null) {
            return Beans.isDesignTime();
        }
        if (containerWrapper != null && !DT_MAP.containsKey(containerWrapper.getComponent())) {
            containerWrapper = null;
        }
        Boolean bl = DT_MAP.get(containerWrapper != null ? containerWrapper.getComponent() : null);
        return bl != null && bl != false;
    }

    public static int getDesignTimeEmptySize() {
        return eSz;
    }

    public static void setDesignTimeEmptySize(int n) {
        eSz = n;
    }

    static void putCCString(Object object, String string) {
        if (string != null && object != null && LayoutUtil.isDesignTime(null)) {
            if (CR_MAP == null) {
                CR_MAP = new WeakHashMap(64);
            }
            CR_MAP.put(object, string);
        }
    }

    static synchronized void setDelegate(Class clazz, PersistenceDelegate persistenceDelegate) {
        try {
            Introspector.getBeanInfo(clazz, 3).getBeanDescriptor().setValue("persistenceDelegate", persistenceDelegate);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    static String getCCString(Object object) {
        return CR_MAP != null ? CR_MAP.get(object) : null;
    }

    static void throwCC() {
        throw new IllegalStateException("setStoreConstraintData(true) must be set for strings to be saved.");
    }

    static int[] calculateSerial(int[][] nArray, ResizeConstraint[] resizeConstraintArray, Float[] floatArray, int n, int n2) {
        int n3;
        float[] fArray = new float[nArray.length];
        float f = 0.0f;
        for (n3 = 0; n3 < nArray.length; ++n3) {
            if (nArray[n3] == null) continue;
            float f2 = nArray[n3][n] != -2147471302 ? (float)nArray[n3][n] : 0.0f;
            int n4 = LayoutUtil.getBrokenBoundary(f2, nArray[n3][0], nArray[n3][2]);
            if (n4 != -2147471302) {
                f2 = n4;
            }
            f += f2;
            fArray[n3] = f2;
        }
        n3 = Math.round(f);
        if (n3 != n2 && resizeConstraintArray != null) {
            boolean bl = n3 < n2;
            TreeSet<Integer> treeSet = new TreeSet<Integer>();
            for (int i = 0; i < nArray.length; ++i) {
                ResizeConstraint resizeConstraint = (ResizeConstraint)LayoutUtil.getIndexSafe(resizeConstraintArray, i);
                if (resizeConstraint == null) continue;
                treeSet.add(new Integer(bl ? resizeConstraint.growPrio : resizeConstraint.shrinkPrio));
            }
            Integer[] integerArray = treeSet.toArray(new Integer[treeSet.size()]);
            for (int i = 0; i <= (bl && floatArray != null ? 1 : 0); ++i) {
                for (int j = integerArray.length - 1; j >= 0; --j) {
                    int n5;
                    int n6 = integerArray[j];
                    float f3 = 0.0f;
                    Float[] floatArray2 = new Float[nArray.length];
                    for (n5 = 0; n5 < nArray.length; ++n5) {
                        int n7;
                        ResizeConstraint resizeConstraint;
                        if (nArray[n5] == null || (resizeConstraint = (ResizeConstraint)LayoutUtil.getIndexSafe(resizeConstraintArray, n5)) == null) continue;
                        int n8 = n7 = bl ? resizeConstraint.growPrio : resizeConstraint.shrinkPrio;
                        if (n6 != n7) continue;
                        floatArray2[n5] = bl ? (i == 0 || resizeConstraint.grow != null ? resizeConstraint.grow : floatArray[n5 < floatArray.length ? n5 : floatArray.length - 1]) : resizeConstraint.shrink;
                        if (floatArray2[n5] == null) continue;
                        f3 += floatArray2[n5].floatValue();
                    }
                    if (!(f3 > 0.0f)) continue;
                    do {
                        float f4 = (float)n2 - f;
                        n5 = 0;
                        float f5 = 0.0f;
                        for (int k = 0; k < nArray.length && f3 > 1.0E-4f; ++k) {
                            int n9;
                            Float f6 = floatArray2[k];
                            if (f6 == null) continue;
                            float f7 = f4 * f6.floatValue() / f3;
                            float f8 = fArray[k] + f7;
                            if (nArray[k] != null && (n9 = LayoutUtil.getBrokenBoundary(f8, nArray[k][0], nArray[k][2])) != -2147471302) {
                                floatArray2[k] = null;
                                n5 = 1;
                                f5 += f6.floatValue();
                                f8 = n9;
                                f7 = f8 - fArray[k];
                            }
                            fArray[k] = f8;
                            f += f7;
                        }
                        f3 -= f5;
                    } while (n5 != 0);
                }
            }
        }
        return LayoutUtil.roundSizes(fArray);
    }

    static Object getIndexSafe(Object[] objectArray, int n) {
        return objectArray != null ? objectArray[n < objectArray.length ? n : objectArray.length - 1] : null;
    }

    private static int getBrokenBoundary(float f, int n, int n2) {
        if (n != -2147471302) {
            if (f < (float)n) {
                return new Integer(n);
            }
        } else if (f < 0.0f) {
            return new Integer(0);
        }
        if (n2 != -2147471302 && f > (float)n2) {
            return new Integer(n2);
        }
        return -2147471302;
    }

    static int sum(int[] nArray, int n, int n2) {
        int n3 = 0;
        int n4 = n + n2;
        for (int i = n; i < n4; ++i) {
            n3 += nArray[i];
        }
        return n3;
    }

    static int sum(int[] nArray) {
        return LayoutUtil.sum(nArray, 0, nArray.length);
    }

    public static int getSizeSafe(int[] nArray, int n) {
        if (nArray == null || nArray[n] == -2147471302) {
            return n == 2 ? 2097051 : 0;
        }
        return nArray[n];
    }

    static BoundSize derive(BoundSize boundSize, UnitValue unitValue, UnitValue unitValue2, UnitValue unitValue3) {
        if (boundSize == null || boundSize.isUnset()) {
            return new BoundSize(unitValue, unitValue2, unitValue3, null);
        }
        return new BoundSize(unitValue != null ? unitValue : boundSize.getMin(), unitValue2 != null ? unitValue2 : boundSize.getPreferred(), unitValue3 != null ? unitValue3 : boundSize.getMax(), boundSize.getGapPush(), null);
    }

    public static final boolean isLeftToRight(LC lC, ContainerWrapper containerWrapper) {
        if (lC != null && lC.getLeftToRight() != null) {
            return lC.getLeftToRight();
        }
        return containerWrapper == null || containerWrapper.isLeftToRight();
    }

    static int[] roundSizes(float[] fArray) {
        int[] nArray = new int[fArray.length];
        float f = 0.0f;
        for (int i = 0; i < nArray.length; ++i) {
            int n = (int)(f + 0.5f);
            nArray[i] = (int)((f += fArray[i]) + 0.5f) - n;
        }
        return nArray;
    }

    static final boolean equals(Object object, Object object2) {
        return object == object2 || object != null && object2 != null && object.equals(object2);
    }

    static final UnitValue getInsets(LC lC, int n, boolean bl) {
        UnitValue[] unitValueArray = lC.getInsets();
        return unitValueArray != null && unitValueArray[n] != null ? unitValueArray[n] : (bl ? PlatformDefaults.getPanelInsets(n) : UnitValue.ZERO);
    }

    static void writeXMLObject(OutputStream outputStream, Object object, ExceptionListener exceptionListener) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(LayoutUtil.class.getClassLoader());
        XMLEncoder xMLEncoder = new XMLEncoder(outputStream);
        if (exceptionListener != null) {
            xMLEncoder.setExceptionListener(exceptionListener);
        }
        xMLEncoder.writeObject(object);
        xMLEncoder.close();
        Thread.currentThread().setContextClassLoader(classLoader);
    }

    public static synchronized void writeAsXML(ObjectOutput objectOutput, Object object) throws IOException {
        if (writeOutputStream == null) {
            writeOutputStream = new ByteArrayOutputStream(16384);
        }
        writeOutputStream.reset();
        LayoutUtil.writeXMLObject(writeOutputStream, object, new ExceptionListener(){

            public void exceptionThrown(Exception exception) {
                exception.printStackTrace();
            }
        });
        byte[] byArray = writeOutputStream.toByteArray();
        objectOutput.writeInt(byArray.length);
        objectOutput.write(byArray);
    }

    public static synchronized Object readAsXML(ObjectInput objectInput) throws IOException {
        if (readBuf == null) {
            readBuf = new byte[16384];
        }
        Thread thread = Thread.currentThread();
        ClassLoader classLoader = null;
        try {
            classLoader = thread.getContextClassLoader();
            thread.setContextClassLoader(LayoutUtil.class.getClassLoader());
        }
        catch (SecurityException securityException) {
            // empty catch block
        }
        Object object = null;
        try {
            int n = objectInput.readInt();
            if (n > readBuf.length) {
                readBuf = new byte[n];
            }
            objectInput.readFully(readBuf, 0, n);
            object = new XMLDecoder(new ByteArrayInputStream(readBuf, 0, n)).readObject();
        }
        catch (EOFException eOFException) {
            // empty catch block
        }
        if (classLoader != null) {
            thread.setContextClassLoader(classLoader);
        }
        return object;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setSerializedObject(Object object, Object object2) {
        IdentityHashMap<Object, Object> identityHashMap = SER_MAP;
        synchronized (identityHashMap) {
            SER_MAP.put(object, object2);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Object getSerializedObject(Object object) {
        IdentityHashMap<Object, Object> identityHashMap = SER_MAP;
        synchronized (identityHashMap) {
            return SER_MAP.remove(object);
        }
    }
}

