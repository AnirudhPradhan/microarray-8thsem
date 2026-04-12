/*
 * Decompiled with CFR 0.152.
 */
package general;

public class GST
implements Comparable<GST> {
    private int g;
    private int s;
    private int t;

    public GST(int g, int s, int t) {
        this.g = g;
        this.s = s;
        this.t = t;
    }

    public int getG() {
        return this.g;
    }

    public int getS() {
        return this.s;
    }

    public int getT() {
        return this.t;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + this.g;
        result = 31 * result + this.s;
        result = 31 * result + this.t;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        GST other = (GST)obj;
        if (this.g != other.g) {
            return false;
        }
        if (this.s != other.s) {
            return false;
        }
        return this.t == other.t;
    }

    @Override
    public int compareTo(GST o) {
        int r = 0;
        int og = o.getG();
        int os = o.getS();
        int ot = o.getT();
        r = Integer.compare(this.g, og);
        if (r == 0 && (r = Integer.compare(this.s, os)) == 0) {
            r = Integer.compare(this.t, ot);
        }
        return r;
    }

    public String toString() {
        return "[" + this.g + "," + this.s + "," + this.t + "]";
    }
}

