/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d;

import de.erichseifert.vectorgraphics2d.Document;
import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;
import de.erichseifert.vectorgraphics2d.util.PageSize;

public interface Processor {
    public Document process(Iterable<Command<?>> var1, PageSize var2);
}

