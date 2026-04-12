/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.StateCommand;

public class SetHintCommand
extends StateCommand<Object> {
    private final Object a;

    public SetHintCommand(Object object, Object object2) {
        super(object2);
        this.a = object;
    }

    public Object getKey() {
        return this.a;
    }

    @Override
    public String toString() {
        return String.format(null, "%s[key=%s, value=%s]", this.getClass().getName(), this.getKey(), this.getValue());
    }
}

