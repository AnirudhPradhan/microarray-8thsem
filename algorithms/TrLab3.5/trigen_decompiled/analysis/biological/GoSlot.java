/*
 * Decompiled with CFR 0.152.
 */
package analysis.biological;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoSlot {
    private static final Logger LOG = LoggerFactory.getLogger(GoSlot.class);
    private int totalTerms;
    private int count;
    private double concentration;
    private double partialScore;
    private double minExponent;
    private boolean haveBonus;

    public GoSlot(int totalTerms) {
        this.totalTerms = totalTerms;
        this.count = 0;
        this.concentration = 0.0;
    }

    public void countAndSetConcentration(double value) {
        ++this.count;
        double auxNum = this.count;
        double auxDen = this.totalTerms;
        this.concentration = auxNum / auxDen;
    }

    public boolean haveBonus() {
        return this.haveBonus;
    }

    public void setHaveBonus(boolean haveBonus) {
        this.haveBonus = haveBonus;
    }

    public double getMinExponent() {
        return this.minExponent;
    }

    public void setMinExponent(double exponent) {
        if (exponent < this.minExponent) {
            this.minExponent = exponent;
        }
    }

    public int getCount() {
        return this.count;
    }

    public double getPartialScore() {
        return this.partialScore;
    }

    public void setPartialScore(double partialScore) {
        this.partialScore = partialScore;
    }

    public void addBonus(double bonus) {
        this.partialScore += bonus;
    }

    public double getConcentration() {
        return this.concentration;
    }

    public String toString() {
        String r = "";
        return r;
    }

    public String getReport(String sep) {
        String r = "";
        r = this.count + sep + this.concentration + sep + this.partialScore;
        return r;
    }
}

