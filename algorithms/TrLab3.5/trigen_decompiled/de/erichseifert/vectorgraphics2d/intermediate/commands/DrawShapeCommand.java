/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.Command;
import de.erichseifert.vectorgraphics2d.util.GraphicsUtils;
import java.awt.Shape;

public class DrawShapeCommand
extends Command<Shape> {
    public DrawShapeCommand(Shape shape) {
        super(GraphicsUtils.clone(shape));
    }
}

