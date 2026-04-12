/*
 * Decompiled with CFR 0.152.
 */
package org.slf4j.helpers;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.slf4j.Marker;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class BasicMarker
implements Marker {
    private static final long serialVersionUID = 1803952589649545191L;
    private final String name;
    private List<Marker> refereceList;
    private static String OPEN = "[ ";
    private static String CLOSE = " ]";
    private static String SEP = ", ";

    BasicMarker(String name) {
        if (name == null) {
            throw new IllegalArgumentException("A marker name cannot be null");
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public synchronized void add(Marker reference) {
        if (reference == null) {
            throw new IllegalArgumentException("A null value cannot be added to a Marker as reference.");
        }
        if (this.contains(reference)) {
            return;
        }
        if (reference.contains(this)) {
            return;
        }
        if (this.refereceList == null) {
            this.refereceList = new Vector<Marker>();
        }
        this.refereceList.add(reference);
    }

    @Override
    public synchronized boolean hasReferences() {
        return this.refereceList != null && this.refereceList.size() > 0;
    }

    @Override
    public boolean hasChildren() {
        return this.hasReferences();
    }

    @Override
    public synchronized Iterator<Marker> iterator() {
        if (this.refereceList != null) {
            return this.refereceList.iterator();
        }
        return Collections.EMPTY_LIST.iterator();
    }

    @Override
    public synchronized boolean remove(Marker referenceToRemove) {
        if (this.refereceList == null) {
            return false;
        }
        int size = this.refereceList.size();
        for (int i = 0; i < size; ++i) {
            Marker m = this.refereceList.get(i);
            if (!((Object)referenceToRemove).equals(m)) continue;
            this.refereceList.remove(i);
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(Marker other) {
        if (other == null) {
            throw new IllegalArgumentException("Other cannot be null");
        }
        if (this.equals(other)) {
            return true;
        }
        if (this.hasReferences()) {
            for (int i = 0; i < this.refereceList.size(); ++i) {
                Marker ref = this.refereceList.get(i);
                if (!ref.contains(other)) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Other cannot be null");
        }
        if (this.name.equals(name)) {
            return true;
        }
        if (this.hasReferences()) {
            for (int i = 0; i < this.refereceList.size(); ++i) {
                Marker ref = this.refereceList.get(i);
                if (!ref.contains(name)) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Marker)) {
            return false;
        }
        Marker other = (Marker)obj;
        return this.name.equals(other.getName());
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public String toString() {
        if (!this.hasReferences()) {
            return this.getName();
        }
        Iterator<Marker> it = this.iterator();
        StringBuffer sb = new StringBuffer(this.getName());
        sb.append(' ').append(OPEN);
        while (it.hasNext()) {
            Marker reference = it.next();
            sb.append(reference.getName());
            if (!it.hasNext()) continue;
            sb.append(SEP);
        }
        sb.append(CLOSE);
        return sb.toString();
    }
}

