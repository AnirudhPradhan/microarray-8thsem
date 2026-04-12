/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.colors;

import de.erichseifert.gral.plots.colors.AbstractColorMapper;
import de.erichseifert.gral.plots.colors.IndexedColorMapper;
import java.awt.Paint;

public class SingleColor
extends IndexedColorMapper {
    private static final long serialVersionUID = -3377452532555792998L;
    private Paint a;

    public SingleColor(Paint paint) {
        this.a = paint;
    }

    @Override
    public Paint get(int n) {
        return this.getColor();
    }

    public Paint getColor() {
        return this.a;
    }

    public void setColor(Paint paint) {
        this.a = paint;
    }

    public boolean equals(Object object) {
        if (!(object instanceof SingleColor)) {
            return false;
        }
        object = (SingleColor)object;
        return this.a.equals(((SingleColor)object).a) && this.getMode() == ((AbstractColorMapper)object).getMode();
    }

    public int hashCode() {
        long l = this.getColor().hashCode();
        return (int)(l ^= (long)(this.getMode().hashCode() * 31)) ^ (int)(l >> 32);
    }
}

