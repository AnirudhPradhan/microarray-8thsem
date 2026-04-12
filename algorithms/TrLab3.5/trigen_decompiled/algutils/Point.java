/*
 * Decompiled with CFR 0.152.
 */
package algutils;

public class Point
implements Comparable<Point> {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + this.x;
        result = 31 * result + this.y;
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
        Point other = (Point)obj;
        if (this.x != other.x) {
            return false;
        }
        return this.y == other.y;
    }

    public String toString() {
        return "{x=" + this.x + ", y=" + this.y + "}";
    }

    @Override
    public int compareTo(Point otro) {
        int res = 0;
        res = this.x > otro.x ? 1 : (this.y < otro.y ? -1 : (this.y > otro.y ? 1 : (this.y < otro.y ? -1 : 0)));
        return res;
    }
}

