/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.filters;

import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Filter
implements Iterable<Command<?>>,
Iterator<Command<?>> {
    private final Queue<Command<?>> a = new LinkedList();
    private final Iterator<Command<?>> b;

    public Filter(Iterable<Command<?>> iterable) {
        this.b = iterable.iterator();
    }

    @Override
    public Iterator<Command<?>> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        this.a();
        return !this.a.isEmpty();
    }

    private void a() {
        while (this.a.isEmpty() && this.b.hasNext()) {
            Object object = this.b.next();
            if ((object = this.filter((Command<?>)object)) == null) continue;
            this.a.addAll((Collection<Command<?>>)object);
        }
    }

    @Override
    public Command<?> next() {
        this.a();
        return this.a.poll();
    }

    @Override
    public void remove() {
    }

    protected abstract List<Command<?>> filter(Command<?> var1);
}

