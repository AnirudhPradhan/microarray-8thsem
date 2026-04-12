/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.filters;

import de.erichseifert.vectorgraphics2d.intermediate.commands.AffineTransformCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;
import de.erichseifert.vectorgraphics2d.intermediate.commands.SetHintCommand;
import de.erichseifert.vectorgraphics2d.intermediate.commands.StateCommand;
import de.erichseifert.vectorgraphics2d.intermediate.filters.Filter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class OptimizeFilter
extends Filter {
    private final Queue<Command<?>> a = new LinkedList();

    public OptimizeFilter(Iterable<Command<?>> iterable) {
        super(iterable);
    }

    @Override
    public boolean hasNext() {
        return super.hasNext();
    }

    @Override
    public Command<?> next() {
        if (this.a.isEmpty()) {
            return super.next();
        }
        return this.a.poll();
    }

    @Override
    protected List<Command<?>> filter(Command<?> command) {
        Command<?> command2 = command;
        if (!(command2 instanceof StateCommand && !(command2 instanceof AffineTransformCommand) && !(command2 instanceof SetHintCommand))) {
            return Arrays.asList(command);
        }
        command2 = this.a.iterator();
        Class<?> clazz = command.getClass();
        while (command2.hasNext()) {
            if (!clazz.equals(((Command)command2.next()).getClass())) continue;
            command2.remove();
        }
        this.a.add(command);
        return null;
    }
}

