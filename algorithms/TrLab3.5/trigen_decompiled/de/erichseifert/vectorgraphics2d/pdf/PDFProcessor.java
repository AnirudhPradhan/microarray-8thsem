/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.pdf;

import de.erichseifert.vectorgraphics2d.Document;
import de.erichseifert.vectorgraphics2d.Processor;
import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;
import de.erichseifert.vectorgraphics2d.intermediate.filters.AbsoluteToRelativeTransformsFilter;
import de.erichseifert.vectorgraphics2d.intermediate.filters.FillPaintedShapeAsImageFilter;
import de.erichseifert.vectorgraphics2d.intermediate.filters.StateChangeGroupingFilter;
import de.erichseifert.vectorgraphics2d.pdf.PDFDocument;
import de.erichseifert.vectorgraphics2d.util.PageSize;

public class PDFProcessor
implements Processor {
    private final boolean a;

    public PDFProcessor(boolean bl) {
        this.a = bl;
    }

    @Override
    public Document process(Iterable<Command<?>> iterator, PageSize object) {
        iterator = new AbsoluteToRelativeTransformsFilter((Iterable<Command<?>>)((Object)iterator));
        iterator = new FillPaintedShapeAsImageFilter((Iterable<Command<?>>)((Object)iterator));
        iterator = new StateChangeGroupingFilter((Iterable<Command<?>>)((Object)iterator));
        object = new PDFDocument((PageSize)object, this.isCompressed());
        iterator = iterator.iterator();
        while (iterator.hasNext()) {
            Command<Object> command = iterator.next();
            ((PDFDocument)object).handle(command);
        }
        ((PDFDocument)object).close();
        return object;
    }

    public boolean isCompressed() {
        return this.a;
    }
}

