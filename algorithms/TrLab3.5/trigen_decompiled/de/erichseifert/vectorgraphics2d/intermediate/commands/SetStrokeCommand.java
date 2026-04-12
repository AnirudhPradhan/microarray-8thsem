/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.StateCommand;
import java.awt.Stroke;

public class SetStrokeCommand
extends StateCommand<Stroke> {
    public SetStrokeCommand(Stroke stroke) {
        super(stroke);
    }
}

