/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d;

import de.erichseifert.vectorgraphics2d.Document;
import de.erichseifert.vectorgraphics2d.util.PageSize;

public abstract class SizedDocument
implements Document {
    private final PageSize a;

    public SizedDocument(PageSize pageSize) {
        this.a = pageSize;
    }

    public PageSize getPageSize() {
        return this.a;
    }

    @Override
    public void close() {
    }
}

