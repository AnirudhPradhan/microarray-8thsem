/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;
import java.util.LinkedList;
import java.util.List;

public class Group
extends Command<List<Command<?>>> {
    public Group() {
        super(new LinkedList());
    }

    public void add(Command<?> command) {
        List list = (List)this.getValue();
        list.add(command);
    }
}

