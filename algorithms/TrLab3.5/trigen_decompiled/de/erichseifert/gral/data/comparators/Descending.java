/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data.comparators;

import de.erichseifert.gral.data.comparators.DataComparator;

public class Descending
extends DataComparator {
    private static final long serialVersionUID = 6834583772451304014L;

    public Descending(int n) {
        super(n);
    }

    @Override
    public int compare(Comparable<?>[] object, Comparable<?>[] object2) {
        object = object[this.getColumn()];
        object2 = object2[this.getColumn()];
        if (object == null && object2 == null) {
            return 0;
        }
        if (object == null) {
            return -1;
        }
        if (object2 == null) {
            return 1;
        }
        return object2.compareTo(object);
    }
}

