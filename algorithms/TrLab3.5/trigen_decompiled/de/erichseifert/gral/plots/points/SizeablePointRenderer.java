/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.plots.points;

import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointData;
import de.erichseifert.gral.util.DataUtils;
import de.erichseifert.gral.util.MathUtils;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class SizeablePointRenderer
extends DefaultPointRenderer2D {
    private static final long serialVersionUID = 3276439387457161307L;
    private int a = 2;

    public int getColumn() {
        return this.a;
    }

    public void setColumn(int n) {
        this.a = n;
    }

    @Override
    public Shape getPointShape(PointData object) {
        Shape shape = this.getShape();
        object = ((PointData)object).row;
        int n = this.getColumn();
        if (n >= ((Row)object).size() || n < 0 || !((Row)object).isColumnNumeric(n)) {
            return shape;
        }
        double d = DataUtils.getValueOrDefault((Number)(object = (Number)((Object)((Row)object).get(n))), Double.NaN);
        if (!MathUtils.isCalculatable(d) || d <= 0.0) {
            return null;
        }
        if (d != 1.0) {
            object = AffineTransform.getScaleInstance(d, d);
            shape = ((AffineTransform)object).createTransformedShape(shape);
        }
        return shape;
    }
}

