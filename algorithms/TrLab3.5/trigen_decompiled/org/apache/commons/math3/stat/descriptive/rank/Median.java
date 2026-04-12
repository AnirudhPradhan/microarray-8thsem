/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.stat.descriptive.rank;

import java.io.Serializable;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

public class Median
extends Percentile
implements Serializable {
    private static final long serialVersionUID = -3961477041290915687L;

    public Median() {
        super(50.0);
    }

    public Median(Median original) throws NullArgumentException {
        super(original);
    }
}

