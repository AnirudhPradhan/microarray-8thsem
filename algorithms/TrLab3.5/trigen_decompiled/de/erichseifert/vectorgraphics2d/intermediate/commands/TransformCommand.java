/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.vectorgraphics2d.intermediate.commands;

import de.erichseifert.vectorgraphics2d.intermediate.commands.AffineTransformCommand;
import java.awt.geom.AffineTransform;

public class TransformCommand
extends AffineTransformCommand {
    private final AffineTransform a;

    public TransformCommand(AffineTransform affineTransform) {
        super(affineTransform);
        this.a = new AffineTransform(affineTransform);
    }

    public AffineTransform getTransform() {
        return this.a;
    }
}

