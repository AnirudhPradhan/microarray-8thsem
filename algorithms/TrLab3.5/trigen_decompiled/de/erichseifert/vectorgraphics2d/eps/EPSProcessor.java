/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.eps;

import de.erichseifert.vectorgraphics2d.Document;
import de.erichseifert.vectorgraphics2d.Processor;
import de.erichseifert.vectorgraphics2d.SizedDocument;
import de.erichseifert.vectorgraphics2d.eps.EPSDocument;
import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;
import de.erichseifert.vectorgraphics2d.intermediate.filters.FillPaintedShapeAsImageFilter;
import de.erichseifert.vectorgraphics2d.intermediate.filters.Filter;
import de.erichseifert.vectorgraphics2d.util.PageSize;

public class EPSProcessor
implements Processor {
    @Override
    public Document process(Iterable<Command<?>> iterator, PageSize object) {
        iterator = new FillPaintedShapeAsImageFilter((Iterable<Command<?>>)((Object)iterator));
        object = new EPSDocument((PageSize)object);
        iterator = ((Filter)iterator).iterator();
        while (iterator.hasNext()) {
            Command<?> command = iterator.next();
            ((EPSDocument)object).handle(command);
        }
        ((SizedDocument)object).close();
        return object;
    }
}

