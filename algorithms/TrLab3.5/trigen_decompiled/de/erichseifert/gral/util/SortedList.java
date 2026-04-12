/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SortedList<T extends Comparable<T>>
extends AbstractList<T> {
    private final List<T> a;

    public SortedList(int n) {
        this.a = new ArrayList<T>(n);
    }

    public SortedList(Collection<? extends T> object) {
        this(object.size());
        object = object.iterator();
        while (object.hasNext()) {
            Comparable comparable = (Comparable)object.next();
            this.add(comparable);
        }
    }

    public SortedList() {
        this(10);
    }

    @Override
    public T get(int n) {
        return (T)((Comparable)this.a.get(n));
    }

    @Override
    public int size() {
        return this.a.size();
    }

    @Override
    public boolean add(T t) {
        if (this.a.isEmpty()) {
            this.a.add(t);
            return true;
        }
        int n = Collections.binarySearch(this.a, t);
        if (n < 0) {
            n = -n - 1;
        }
        this.a.add(n, t);
        return true;
    }

    @Override
    public T remove(int n) {
        return (T)((Comparable)this.a.remove(n));
    }

    @Override
    public int indexOf(Object object) {
        try {
            return Collections.binarySearch(this.a, (Comparable)object);
        }
        catch (NullPointerException nullPointerException) {
            return -1;
        }
        catch (ClassCastException classCastException) {
            return -1;
        }
    }
}

