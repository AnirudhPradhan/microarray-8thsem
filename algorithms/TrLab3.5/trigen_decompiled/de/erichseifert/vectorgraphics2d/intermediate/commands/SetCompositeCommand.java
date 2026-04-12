/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.StateCommand;
import java.awt.Composite;

public class SetCompositeCommand
extends StateCommand<Composite> {
    public SetCompositeCommand(Composite composite) {
        super(composite);
    }
}

