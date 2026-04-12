/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.svg;

import de.erichseifert.vectorgraphics2d.Document;
import de.erichseifert.vectorgraphics2d.Processor;
import de.erichseifert.vectorgraphics2d.SizedDocument;
import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;
import de.erichseifert.vectorgraphics2d.intermediate.filters.FillPaintedShapeAsImageFilter;
import de.erichseifert.vectorgraphics2d.intermediate.filters.StateChangeGroupingFilter;
import de.erichseifert.vectorgraphics2d.svg.SVGDocument;
import de.erichseifert.vectorgraphics2d.util.PageSize;

public class SVGProcessor
implements Processor {
    @Override
    public Document process(Iterable<Command<?>> iterator, PageSize object) {
        iterator = new FillPaintedShapeAsImageFilter((Iterable<Command<?>>)((Object)iterator));
        iterator = new StateChangeGroupingFilter((Iterable<Command<?>>)((Object)iterator));
        object = new SVGDocument((PageSize)object);
        iterator = iterator.iterator();
        while (iterator.hasNext()) {
            Command<Object> command = iterator.next();
            ((SVGDocument)object).handle(command);
        }
        ((SizedDocument)object).close();
        return object;
    }
}

