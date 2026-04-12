/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.util;

import de.erichseifert.gral.util.SerializationWrapper;
import java.awt.BasicStroke;

public class SerializableBasicStroke
implements SerializationWrapper<BasicStroke> {
    private static final long serialVersionUID = -9087891720495398485L;
    private final float a;
    private final int b;
    private final int c;
    private final float d;
    private final float[] e;
    private final float f;

    public SerializableBasicStroke(BasicStroke basicStroke) {
        this.a = basicStroke.getLineWidth();
        this.b = basicStroke.getEndCap();
        this.c = basicStroke.getLineJoin();
        this.d = basicStroke.getMiterLimit();
        this.e = basicStroke.getDashArray();
        this.f = basicStroke.getDashPhase();
    }

    @Override
    public BasicStroke unwrap() {
        return new BasicStroke(this.a, this.b, this.c, this.d, this.e, this.f);
    }
}

