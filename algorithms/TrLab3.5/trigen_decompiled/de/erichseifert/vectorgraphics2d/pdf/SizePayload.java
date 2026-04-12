/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.pdf;

import de.erichseifert.vectorgraphics2d.pdf.GeneratedPayload;
import de.erichseifert.vectorgraphics2d.pdf.PDFObject;
import de.erichseifert.vectorgraphics2d.util.DataUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SizePayload
extends GeneratedPayload {
    private final PDFObject a;
    private final String b;

    public SizePayload(PDFObject pDFObject, String string, boolean bl) {
        super(bl);
        this.a = pDFObject;
        this.b = string;
    }

    @Override
    protected byte[] generatePayload() {
        try {
            this.a.payload.close();
            String string = DataUtils.format(this.a.payload.getBytes().length);
            return string.getBytes(this.b);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RuntimeException(unsupportedEncodingException);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }
}

