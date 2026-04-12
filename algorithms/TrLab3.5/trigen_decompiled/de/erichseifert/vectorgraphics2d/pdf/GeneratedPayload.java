/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.pdf;

import de.erichseifert.vectorgraphics2d.pdf.Payload;
import java.io.IOException;

public abstract class GeneratedPayload
extends Payload {
    public GeneratedPayload(boolean bl) {
        super(bl);
    }

    @Override
    public byte[] getBytes() {
        try {
            for (byte by : this.generatePayload()) {
                super.write(by);
            }
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        return super.getBytes();
    }

    @Override
    public void write(int n) throws IOException {
        throw new UnsupportedOperationException("Payload will be calculated and is read only.");
    }

    protected abstract byte[] generatePayload();
}

