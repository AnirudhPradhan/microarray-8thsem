/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.filters;

import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;
import de.erichseifert.vectorgraphics2d.intermediate.commands.Group;
import de.erichseifert.vectorgraphics2d.intermediate.filters.Filter;
import java.util.Arrays;
import java.util.List;

public abstract class GroupingFilter
extends Filter {
    private Group a;

    public GroupingFilter(Iterable<Command<?>> iterable) {
        super(iterable);
    }

    @Override
    public boolean hasNext() {
        return this.a != null || super.hasNext();
    }

    @Override
    public Command<?> next() {
        if (this.a == null) {
            return super.next();
        }
        Group group = this.a;
        this.a = null;
        return group;
    }

    @Override
    protected List<Command<?>> filter(Command<?> command) {
        boolean bl = this.isGrouped(command);
        if (bl) {
            if (this.a == null) {
                this.a = new Group();
            }
            this.a.add(command);
            return null;
        }
        return Arrays.asList(command);
    }

    protected abstract boolean isGrouped(Command<?> var1);
}

