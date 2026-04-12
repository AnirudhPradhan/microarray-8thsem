/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.util;

import java.util.HashMap;
import java.util.Map;

public abstract class DataUtils {
    private DataUtils() {
        throw new UnsupportedOperationException();
    }

    public static <K, V> Map<K, V> map(K[] KArray, V[] VArray) {
        if (KArray.length != VArray.length) {
            throw new IllegalArgumentException("Could not create the map because the number of keys and values differs.");
        }
        HashMap<K, V> hashMap = new HashMap<K, V>();
        for (int i = 0; i < KArray.length; ++i) {
            K k = KArray[i];
            V v = VArray[i];
            hashMap.put(k, v);
        }
        return hashMap;
    }

    public static double getValueOrDefault(Number number, double d) {
        if (number == null) {
            return d;
        }
        return number.doubleValue();
    }
}

