/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.StateCommand;
import java.awt.geom.AffineTransform;

public abstract class AffineTransformCommand
extends StateCommand<AffineTransform> {
    public AffineTransformCommand(AffineTransform affineTransform) {
        super(affineTransform);
    }
}

