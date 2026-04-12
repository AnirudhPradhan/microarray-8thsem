/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.StateCommand;
import java.awt.Font;

public class SetFontCommand
extends StateCommand<Font> {
    public SetFontCommand(Font font) {
        super(font);
    }
}

