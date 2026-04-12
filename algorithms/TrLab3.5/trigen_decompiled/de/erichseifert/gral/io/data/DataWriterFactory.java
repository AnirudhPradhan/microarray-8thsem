/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.io.data;

import de.erichseifert.gral.io.AbstractIOFactory;
import de.erichseifert.gral.io.data.DataWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

public final class DataWriterFactory
extends AbstractIOFactory<DataWriter> {
    private static DataWriterFactory a;

    private DataWriterFactory() throws IOException {
        super("datawriters.properties");
    }

    public static DataWriterFactory getInstance() {
        if (a == null) {
            try {
                a = new DataWriterFactory();
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }
        return a;
    }

    @Override
    public final DataWriter get(String string) {
        DataWriter dataWriter = null;
        Object object = this.getTypeClass(string);
        try {
            if (object != null) {
                object = ((Class)object).getDeclaredConstructor(String.class);
                dataWriter = (DataWriter)((Constructor)object).newInstance(string);
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
        if (dataWriter == null) {
            throw new IllegalArgumentException(MessageFormat.format("Unsupported MIME type: {0}", string));
        }
        return dataWriter;
    }
}

