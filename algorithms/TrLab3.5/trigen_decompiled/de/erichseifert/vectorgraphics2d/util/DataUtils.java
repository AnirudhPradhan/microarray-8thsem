/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class DataUtils {
    protected DataUtils() {
        throw new UnsupportedOperationException();
    }

    public static <K, V> Map<K, V> map(K[] KArray, V[] VArray) {
        if (KArray.length != VArray.length) {
            throw new IllegalArgumentException("Cannot create a Map: The number of keys and values differs.");
        }
        LinkedHashMap<K, V> linkedHashMap = new LinkedHashMap<K, V>(KArray.length);
        for (int i = 0; i < KArray.length; ++i) {
            K k = KArray[i];
            V v = VArray[i];
            linkedHashMap.put(k, v);
        }
        return linkedHashMap;
    }

    public static String join(String string, List<?> object) {
        if (object == null || object.size() == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(object.size() * 3);
        int n = 0;
        object = object.iterator();
        while (object.hasNext()) {
            Object e = object.next();
            if (string.length() > 0 && n++ > 0) {
                stringBuilder.append(string);
            }
            stringBuilder.append(DataUtils.format(e));
        }
        return stringBuilder.toString();
    }

    public static String join(String string, Object[] objectArray) {
        if (objectArray == null || objectArray.length == 0) {
            return "";
        }
        return DataUtils.join(string, Arrays.asList(objectArray));
    }

    public static String join(String string, double[] dArray) {
        if (dArray == null || dArray.length == 0) {
            return "";
        }
        ArrayList<Double> arrayList = new ArrayList<Double>(dArray.length);
        int n = dArray.length;
        for (int i = 0; i < n; ++i) {
            Double d = dArray[i];
            arrayList.add(d);
        }
        return DataUtils.join(string, arrayList);
    }

    public static String join(String string, float[] fArray) {
        if (fArray == null || fArray.length == 0) {
            return "";
        }
        ArrayList<Float> arrayList = new ArrayList<Float>(fArray.length);
        int n = fArray.length;
        for (int i = 0; i < n; ++i) {
            Float f = Float.valueOf(fArray[i]);
            arrayList.add(f);
        }
        return DataUtils.join(string, arrayList);
    }

    public static int max(int ... nArray) {
        int n = nArray[0];
        for (int i = 1; i < nArray.length; ++i) {
            if (nArray[i] <= n) continue;
            n = nArray[i];
        }
        return n;
    }

    public static void transfer(InputStream inputStream, OutputStream outputStream, int n) throws IOException {
        int n2;
        byte[] byArray = new byte[n];
        while ((n2 = inputStream.read(byArray)) != -1) {
            outputStream.write(byArray, 0, n2);
        }
    }

    public static String format(Number object) {
        object = object instanceof Double || object instanceof Float ? Double.toString(((Number)object).doubleValue()).replaceAll("\\.0+$", "").replaceAll("(\\.[0-9]*[1-9])0+$", "$1") : object.toString();
        return object;
    }

    public static String format(Object object) {
        if (object instanceof Number) {
            return DataUtils.format((Number)object);
        }
        return object.toString();
    }

    public static List<Float> asList(float[] fArray) {
        int n = fArray != null ? fArray.length : 0;
        ArrayList<Float> arrayList = new ArrayList<Float>(n);
        if (fArray != null) {
            int n2 = fArray.length;
            for (int i = 0; i < n2; ++i) {
                Float f = Float.valueOf(fArray[i]);
                arrayList.add(f);
            }
        }
        return arrayList;
    }

    public static List<Double> asList(double[] dArray) {
        int n = dArray != null ? dArray.length : 0;
        ArrayList<Double> arrayList = new ArrayList<Double>(n);
        if (dArray != null) {
            int n2 = dArray.length;
            for (int i = 0; i < n2; ++i) {
                Double d = dArray[i];
                arrayList.add(d);
            }
        }
        return arrayList;
    }

    public static String stripTrailing(String string, String string2) {
        return string.replaceAll("(" + Pattern.quote(string2) + ")+$", "");
    }
}

