/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.StateCommand;
import java.awt.Color;

public class SetXORModeCommand
extends StateCommand<Color> {
    public SetXORModeCommand(Color color) {
        super(color);
    }
}

