/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.StateCommand;
import java.awt.Paint;

public class SetPaintCommand
extends StateCommand<Paint> {
    public SetPaintCommand(Paint paint) {
        super(paint);
    }
}

