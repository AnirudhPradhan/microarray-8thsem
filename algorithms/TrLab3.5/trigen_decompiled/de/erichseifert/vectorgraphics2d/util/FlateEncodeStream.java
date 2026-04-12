/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.util;

import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

public class FlateEncodeStream
extends DeflaterOutputStream {
    public FlateEncodeStream(OutputStream outputStream) {
        super(outputStream);
    }
}

