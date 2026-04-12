/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.io.plots;

import de.erichseifert.gral.io.AbstractIOFactory;
import de.erichseifert.gral.io.plots.DrawableWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

public final class DrawableWriterFactory
extends AbstractIOFactory<DrawableWriter> {
    private static DrawableWriterFactory a;

    private DrawableWriterFactory() throws IOException {
        super("drawablewriters.properties");
    }

    public static DrawableWriterFactory getInstance() {
        if (a == null) {
            try {
                a = new DrawableWriterFactory();
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }
        return a;
    }

    @Override
    public final DrawableWriter get(String string) {
        DrawableWriter drawableWriter = null;
        Object object = this.getTypeClass(string);
        try {
            if (object != null) {
                object = ((Class)object).getDeclaredConstructor(String.class);
                drawableWriter = (DrawableWriter)((Constructor)object).newInstance(string);
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
        if (drawableWriter == null) {
            throw new IllegalArgumentException(MessageFormat.format("Unsupported MIME type: {0}", string));
        }
        return drawableWriter;
    }
}

