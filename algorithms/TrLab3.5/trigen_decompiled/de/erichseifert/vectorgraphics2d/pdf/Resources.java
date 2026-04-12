/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.pdf;

import de.erichseifert.vectorgraphics2d.pdf.PDFObject;
import de.erichseifert.vectorgraphics2d.util.DataUtils;
import de.erichseifert.vectorgraphics2d.util.GraphicsUtils;
import java.awt.Font;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Resources
extends PDFObject {
    private static final String[] a = new String[]{"PDF", "Text", "ImageB", "ImageC", "ImageI"};
    private final Map<Font, String> b;
    private final Map<PDFObject, String> c;
    private final Map<Double, String> d;
    private final AtomicInteger e = new AtomicInteger();
    private final AtomicInteger f = new AtomicInteger();
    private final AtomicInteger g = new AtomicInteger();

    public Resources(int n, int n2) {
        super(n, n2, null, null);
        this.b = new HashMap<Font, String>();
        this.c = new HashMap<PDFObject, String>();
        this.d = new HashMap<Double, String>();
        this.dict.put("ProcSet", a);
    }

    private static <T> String a(Map<T, String> map, T t, String string, AtomicInteger atomicInteger) {
        String string2 = map.get(t);
        if (string2 == null) {
            string2 = String.format("%s%d", string, atomicInteger.getAndIncrement());
            map.put(t, string2);
        }
        return string2;
    }

    public String getId(Font object) {
        LinkedHashMap<String, Map<String, Object>> linkedHashMap = (LinkedHashMap<String, Map<String, Object>>)this.dict.get("Font");
        if (linkedHashMap == null) {
            linkedHashMap = new LinkedHashMap<String, Map<String, Object>>();
            this.dict.put("Font", linkedHashMap);
        }
        object = GraphicsUtils.getPhysicalFont((Font)object);
        String string = Resources.a(this.b, object, "Fnt", this.e);
        object = ((Font)object).getPSName();
        String string2 = "WinAnsiEncoding";
        linkedHashMap.put(string, DataUtils.map(new String[]{"Type", "Subtype", "Encoding", "BaseFont"}, new Object[]{"Font", "TrueType", string2, object}));
        return string;
    }

    public String getId(PDFObject pDFObject) {
        LinkedHashMap<String, PDFObject> linkedHashMap = (LinkedHashMap<String, PDFObject>)this.dict.get("XObject");
        if (linkedHashMap == null) {
            linkedHashMap = new LinkedHashMap<String, PDFObject>();
            this.dict.put("XObject", linkedHashMap);
        }
        String string = Resources.a(this.c, pDFObject, "Img", this.f);
        linkedHashMap.put(string, pDFObject);
        return string;
    }

    public String getId(Double d) {
        LinkedHashMap<String, Map<String, Object>> linkedHashMap = (LinkedHashMap<String, Map<String, Object>>)this.dict.get("ExtGState");
        if (linkedHashMap == null) {
            linkedHashMap = new LinkedHashMap<String, Map<String, Object>>();
            this.dict.put("ExtGState", linkedHashMap);
        }
        String string = Resources.a(this.d, d, "Trp", this.g);
        linkedHashMap.put(string, DataUtils.map(new String[]{"Type", "ca", "CA"}, new Object[]{"ExtGState", d, d}));
        return string;
    }
}

