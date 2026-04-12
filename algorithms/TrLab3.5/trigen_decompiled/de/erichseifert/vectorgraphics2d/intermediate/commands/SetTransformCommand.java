/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.StateCommand;
import java.awt.geom.AffineTransform;

public class SetTransformCommand
extends StateCommand<AffineTransform> {
    public SetTransformCommand(AffineTransform affineTransform) {
        super(new AffineTransform(affineTransform));
    }
}

