/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.io.data;

import de.erichseifert.gral.io.AbstractIOFactory;
import de.erichseifert.gral.io.data.DataReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

public final class DataReaderFactory
extends AbstractIOFactory<DataReader> {
    private static DataReaderFactory a;

    private DataReaderFactory() throws IOException {
        super("datareaders.properties");
    }

    public static DataReaderFactory getInstance() {
        if (a == null) {
            try {
                a = new DataReaderFactory();
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }
        return a;
    }

    @Override
    public final DataReader get(String string) {
        DataReader dataReader = null;
        Object object = this.getTypeClass(string);
        try {
            if (object != null) {
                object = ((Class)object).getDeclaredConstructor(String.class);
                dataReader = (DataReader)((Constructor)object).newInstance(string);
            }
        }
        catch (SecurityException securityException) {
            object = securityException;
            securityException.printStackTrace();
        }
        catch (NoSuchMethodException noSuchMethodException) {
            object = noSuchMethodException;
            noSuchMethodException.printStackTrace();
        }
        catch (IllegalArgumentException illegalArgumentException) {
            object = illegalArgumentException;
            illegalArgumentException.printStackTrace();
        }
        catch (InstantiationException instantiationException) {
            object = instantiationException;
            instantiationException.printStackTrace();
        }
        catch (IllegalAccessException illegalAccessException) {
            object = illegalAccessException;
            illegalAccessException.printStackTrace();
        }
        catch (InvocationTargetException invocationTargetException) {
            object = invocationTargetException;
            invocationTargetException.printStackTrace();
        }
        if (dataReader == null) {
            throw new IllegalArgumentException(MessageFormat.format("Unsupported MIME type: {0}", string));
        }
        return dataReader;
    }
}

