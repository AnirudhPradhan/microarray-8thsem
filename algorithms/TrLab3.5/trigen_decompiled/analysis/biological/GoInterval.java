/*
 * Decompiled with CFR 0.152.
 */
package analysis.biological;

import java.text.DecimalFormat;
import utils.TextUtilities;

public class GoInterval
implements Comparable<GoInterval> {
    private int level;
    private double levelWeight;
    private double inf;
    private double sup;

    public GoInterval(int level, double levelWeight, double inf, double sup) {
        this.level = level;
        this.levelWeight = levelWeight;
        this.inf = inf;
        this.sup = sup;
    }

    public int getLevel() {
        return this.level;
    }

    public double getLevelWeight() {
        return this.levelWeight;
    }

    public double getInf() {
        return this.inf;
    }

    public double getSup() {
        return this.sup;
    }

    public boolean isIntoInterval(double value) {
        return value <= this.sup && value > this.inf;
    }

    public String toString() {
        DecimalFormat f = TextUtilities.getDecimalFormat('.', "0.0E00");
        return this.level + "    " + this.levelWeight + "    " + "(" + f.format((Object)this.inf) + "," + f.format((Object)this.sup) + "]";
    }

    public String getReportString(String sep) {
        DecimalFormat f = TextUtilities.getDecimalFormat('.', "0.0E00");
        return this.level + sep + this.levelWeight + sep + "(" + f.format((Object)this.inf) + "," + f.format((Object)this.sup) + "]" + sep;
    }

    @Override
    public int compareTo(GoInterval o) {
        double i1 = this.getInf();
        double i2 = o.getInf();
        return Double.compare(i1, i2);
    }
}

