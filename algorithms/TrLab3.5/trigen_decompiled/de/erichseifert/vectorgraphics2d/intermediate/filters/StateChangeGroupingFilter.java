/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.filters;

import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;
import de.erichseifert.vectorgraphics2d.intermediate.commands.StateCommand;
import de.erichseifert.vectorgraphics2d.intermediate.filters.GroupingFilter;

public class StateChangeGroupingFilter
extends GroupingFilter {
    public StateChangeGroupingFilter(Iterable<Command<?>> iterable) {
        super(iterable);
    }

    @Override
    protected boolean isGrouped(Command<?> command) {
        return command instanceof StateCommand;
    }
}

