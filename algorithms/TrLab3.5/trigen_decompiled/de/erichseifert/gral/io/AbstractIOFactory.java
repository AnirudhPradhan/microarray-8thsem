/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.io;

import de.erichseifert.gral.io.IOCapabilities;
import de.erichseifert.gral.io.IOFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public abstract class AbstractIOFactory<T>
implements IOFactory<T> {
    private final Map<String, Class<? extends T>> a = new HashMap<String, Class<? extends T>>();

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected AbstractIOFactory(String object) throws IOException {
        Enumeration<URL> enumeration = this.getClass().getClassLoader().getResources((String)object);
        if (!enumeration.hasMoreElements()) {
            throw new IOException(MessageFormat.format("Property file not found: {0}", object));
        }
        object = new Properties();
        block4: while (true) {
            Object uRL;
            block9: {
                InputStream inputStream;
                if (!enumeration.hasMoreElements()) {
                    return;
                }
                uRL = enumeration.nextElement();
                InputStream inputStream2 = null;
                try {
                    inputStream = ((URL)uRL).openStream();
                    ((Properties)object).load(inputStream);
                    if (inputStream == null) break block9;
                }
                catch (Throwable throwable) {
                    if (inputStream2 != null) {
                        inputStream2.close();
                    }
                    throw throwable;
                }
                inputStream.close();
            }
            uRL = ((Properties)object).entrySet().iterator();
            while (true) {
                Class<?> clazz;
                if (!uRL.hasNext()) continue block4;
                Map.Entry entry = (Map.Entry)uRL.next();
                String string = (String)entry.getKey();
                String string2 = (String)entry.getValue();
                try {
                    clazz = Class.forName(string2);
                }
                catch (ClassNotFoundException classNotFoundException) {
                    throw new IOException(classNotFoundException);
                }
                this.a.put(string, clazz);
            }
            break;
        }
    }

    @Override
    public IOCapabilities getCapabilities(String string) {
        Iterator iterator = this.a.get(string);
        try {
            Object object = ((Class)((Object)iterator)).getMethod("getCapabilities", new Class[0]);
            iterator = (Set)((Method)object).invoke(iterator, new Object[0]);
            iterator = iterator.iterator();
            while (iterator.hasNext()) {
                object = (IOCapabilities)iterator.next();
                if (!((IOCapabilities)object).getMimeType().equals(string)) continue;
                return object;
            }
        }
        catch (SecurityException securityException) {
            SecurityException securityException2 = securityException;
            securityException.printStackTrace();
        }
        catch (NoSuchMethodException noSuchMethodException) {
            NoSuchMethodException noSuchMethodException2 = noSuchMethodException;
            noSuchMethodException.printStackTrace();
        }
        catch (IllegalArgumentException illegalArgumentException) {
            IllegalArgumentException illegalArgumentException2 = illegalArgumentException;
            illegalArgumentException.printStackTrace();
        }
        catch (IllegalAccessException illegalAccessException) {
            IllegalAccessException illegalAccessException2 = illegalAccessException;
            illegalAccessException.printStackTrace();
        }
        catch (InvocationTargetException invocationTargetException) {
            InvocationTargetException invocationTargetException2 = invocationTargetException;
            invocationTargetException.printStackTrace();
        }
        return null;
    }

    @Override
    public List<IOCapabilities> getCapabilities() {
        ArrayList<IOCapabilities> arrayList = new ArrayList<IOCapabilities>(this.a.size());
        for (String string : this.a.keySet()) {
            IOCapabilities object = this.getCapabilities(string);
            if (object == null) continue;
            arrayList.add(object);
        }
        return arrayList;
    }

    @Override
    public String[] getSupportedFormats() {
        String[] stringArray = new String[this.a.size()];
        this.a.keySet().toArray(stringArray);
        return stringArray;
    }

    @Override
    public boolean isFormatSupported(String string) {
        return this.a.containsKey(string);
    }

    protected Class<? extends T> getTypeClass(String string) {
        return this.a.get(string);
    }

    @Override
    public T get(String string) {
        return null;
    }
}

