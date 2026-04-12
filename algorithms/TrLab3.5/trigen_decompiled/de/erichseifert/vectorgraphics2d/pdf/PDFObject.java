/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.pdf;

import de.erichseifert.vectorgraphics2d.pdf.Payload;
import java.util.LinkedHashMap;
import java.util.Map;

public class PDFObject {
    public final int id;
    public final int version;
    public final Map<String, Object> dict = new LinkedHashMap<String, Object>();
    public final Payload payload;

    public PDFObject(int n, int n2, Map<String, Object> map, Payload payload) {
        this.id = n;
        this.version = n2;
        this.payload = payload;
        if (map != null) {
            this.dict.putAll(map);
        }
    }
}

