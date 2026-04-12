/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.pdf;

import de.erichseifert.vectorgraphics2d.util.FlateEncodeStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

public class Payload
extends OutputStream {
    private final ByteArrayOutputStream a = new ByteArrayOutputStream();
    private OutputStream b = this.a;
    private final boolean c;
    private boolean d;

    public Payload(boolean bl) {
        this.c = bl;
        this.d = true;
    }

    public byte[] getBytes() {
        return this.a.toByteArray();
    }

    public boolean isStream() {
        return this.c;
    }

    @Override
    public void write(int n) throws IOException {
        this.b.write(n);
        this.d = false;
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.b.close();
    }

    public void addFilter(Class<FlateEncodeStream> serializable) {
        if (!this.d) {
            throw new IllegalStateException("Cannot add filter after writing to payload.");
        }
        try {
            this.b = serializable.getConstructor(OutputStream.class).newInstance(this.b);
            return;
        }
        catch (InstantiationException instantiationException) {
            serializable = instantiationException;
            instantiationException.printStackTrace();
            return;
        }
        catch (IllegalAccessException illegalAccessException) {
            serializable = illegalAccessException;
            illegalAccessException.printStackTrace();
            return;
        }
        catch (InvocationTargetException invocationTargetException) {
            serializable = invocationTargetException;
            invocationTargetException.printStackTrace();
            return;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            serializable = noSuchMethodException;
            noSuchMethodException.printStackTrace();
            return;
        }
    }
}

