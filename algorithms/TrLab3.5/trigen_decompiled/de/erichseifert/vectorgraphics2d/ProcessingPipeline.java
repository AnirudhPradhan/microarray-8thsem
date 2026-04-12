/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d;

import de.erichseifert.vectorgraphics2d.Document;
import de.erichseifert.vectorgraphics2d.Processor;
import de.erichseifert.vectorgraphics2d.VectorGraphics2D;
import de.erichseifert.vectorgraphics2d.util.PageSize;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class ProcessingPipeline
extends VectorGraphics2D {
    private final PageSize a;

    public ProcessingPipeline(double d, double d2, double d3, double d4) {
        this.a = new PageSize(d, d2, d3, d4);
    }

    public PageSize getPageSize() {
        return this.a;
    }

    protected abstract Processor getProcessor();

    public void writeTo(OutputStream outputStream) throws IOException {
        Document document = this.getProcessor().process(this.getCommands(), this.getPageSize());
        document.write(outputStream);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] getBytes() {
        Object object = new ByteArrayOutputStream();
        try {
            this.writeTo((OutputStream)object);
        }
        catch (IOException iOException) {
            try {
                throw new RuntimeException(iOException);
            }
            catch (Throwable throwable) {
                try {
                    ((ByteArrayOutputStream)object).close();
                    throw throwable;
                }
                catch (IOException iOException2) {
                    object = iOException2;
                    iOException2.printStackTrace();
                }
                throw throwable;
            }
        }
        try {
            ((ByteArrayOutputStream)object).close();
            return ((ByteArrayOutputStream)object).toByteArray();
        }
        catch (IOException iOException) {
            IOException iOException2 = iOException;
            iOException.printStackTrace();
            return ((ByteArrayOutputStream)object).toByteArray();
        }
    }
}

