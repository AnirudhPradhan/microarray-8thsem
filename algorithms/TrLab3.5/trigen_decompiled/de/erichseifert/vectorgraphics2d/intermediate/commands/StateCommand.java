/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;

public abstract class StateCommand<T>
extends Command<T> {
    public StateCommand(T t) {
        super(t);
    }
}

