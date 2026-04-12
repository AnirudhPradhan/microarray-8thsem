/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.graphics;

import java.awt.Graphics2D;

public class DrawingContext {
    private final Graphics2D a;
    private final Quality b;
    private final Target c;

    public DrawingContext(Graphics2D graphics2D) {
        this(graphics2D, Quality.NORMAL, Target.BITMAP);
    }

    public DrawingContext(Graphics2D graphics2D, Quality quality, Target target) {
        this.a = graphics2D;
        this.b = quality;
        this.c = target;
    }

    public Graphics2D getGraphics() {
        return this.a;
    }

    public Quality getQuality() {
        return this.b;
    }

    public Target getTarget() {
        return this.c;
    }

    public static enum Target {
        BITMAP,
        VECTOR;

    }

    public static enum Quality {
        DRAFT,
        NORMAL,
        QUALITY;

    }
}

