/*
 * Decompiled with CFR 0.152.
 */
package org.slf4j.helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.spi.MDCAdapter;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class BasicMDCAdapter
implements MDCAdapter {
    private InheritableThreadLocal<Map<String, String>> inheritableThreadLocal = new InheritableThreadLocal();
    static boolean IS_JDK14 = BasicMDCAdapter.isJDK14();

    static boolean isJDK14() {
        try {
            String javaVersion = System.getProperty("java.version");
            return javaVersion.startsWith("1.4");
        }
        catch (SecurityException se) {
            return false;
        }
    }

    @Override
    public void put(String key, String val) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        Map<String, String> map = (Map<String, String>)this.inheritableThreadLocal.get();
        if (map == null) {
            map = Collections.synchronizedMap(new HashMap());
            this.inheritableThreadLocal.set(map);
        }
        map.put(key, val);
    }

    @Override
    public String get(String key) {
        Map Map2 = (Map)this.inheritableThreadLocal.get();
        if (Map2 != null && key != null) {
            return (String)Map2.get(key);
        }
        return null;
    }

    @Override
    public void remove(String key) {
        Map map = (Map)this.inheritableThreadLocal.get();
        if (map != null) {
            map.remove(key);
        }
    }

    @Override
    public void clear() {
        Map map = (Map)this.inheritableThreadLocal.get();
        if (map != null) {
            map.clear();
            if (BasicMDCAdapter.isJDK14()) {
                this.inheritableThreadLocal.set(null);
            } else {
                this.inheritableThreadLocal.remove();
            }
        }
    }

    public Set<String> getKeys() {
        Map map = (Map)this.inheritableThreadLocal.get();
        if (map != null) {
            return map.keySet();
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Map<String, String> getCopyOfContextMap() {
        Map oldMap = (Map)this.inheritableThreadLocal.get();
        if (oldMap != null) {
            Map<String, String> newMap = Collections.synchronizedMap(new HashMap());
            Map map = oldMap;
            synchronized (map) {
                newMap.putAll(oldMap);
            }
            return newMap;
        }
        return null;
    }

    @Override
    public void setContextMap(Map<String, String> contextMap) {
        Map<String, String> map = Collections.synchronizedMap(new HashMap<String, String>(contextMap));
        this.inheritableThreadLocal.set(map);
    }
}

