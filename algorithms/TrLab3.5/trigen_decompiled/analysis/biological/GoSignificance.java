/*
 * Decompiled with CFR 0.152.
 */
package analysis.biological;

import analysis.biological.GoInterval;
import analysis.biological.GoLevels;
import analysis.biological.GoSlot;
import analysis.biological.GoStudy;
import analysis.biological.GoTerm;
import analysis.biological.PAtermOrder;
import analysis.biological.PtermOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoSignificance {
    private static final Logger LOG = LoggerFactory.getLogger(GoSignificance.class);
    private static GoSignificance singleton = new GoSignificance();
    private double THRESHOLD = 1.0E-40;
    private double BASE = 10.0;
    private double STEP = 1.0;
    private double BONUS = 0.0;
    private double DIF = 10.0;
    private List<GoInterval> INTERVALS;

    private GoSignificance() {
        this.buildIntervals();
    }

    public static GoSignificance getInstance() {
        return singleton;
    }

    public void setConfiguration(double threshold, double base, double step, double dif, double bonus) {
        this.THRESHOLD = threshold;
        this.BASE = base;
        this.STEP = step;
        this.DIF = dif;
        this.BONUS = bonus;
    }

    public String toString() {
        String r = "";
        for (GoInterval in : this.INTERVALS) {
            r = r + in + "\n";
        }
        return r;
    }

    public double getTHRESHOLD() {
        return this.THRESHOLD;
    }

    public double getBASE() {
        return this.BASE;
    }

    public double getSTEP() {
        return this.STEP;
    }

    public double getDIF() {
        return this.DIF;
    }

    public double getBONUS() {
        return this.BONUS;
    }

    public List<GoInterval> getINTERVALS() {
        return this.INTERVALS;
    }

    private void buildIntervals() {
        this.INTERVALS = new ArrayList<GoInterval>();
        int level = 1;
        double sup = 1.0;
        double inf = this.STEP / Math.pow(this.BASE, level);
        double levelWeight = 1.0;
        while (inf >= this.THRESHOLD) {
            this.INTERVALS.add(new GoInterval(level, levelWeight, inf, sup));
            sup = inf;
            inf = this.STEP / Math.pow(this.BASE, ++level);
            levelWeight += this.DIF;
        }
        sup = this.STEP / Math.pow(this.BASE, level - 1);
        inf = 0.0;
        this.INTERVALS.add(new GoInterval(level, levelWeight, inf, sup));
        Collections.sort(this.INTERVALS);
    }

    public GoLevels getGoLevels(GoStudy goStudy) {
        GoLevels goLevels = new GoLevels();
        this.buildGoLevels(goStudy.getGoTerms(), goLevels);
        return goLevels;
    }

    private void buildGoLevels(List<GoTerm> goTerms, GoLevels goLevels) {
        ArrayList<GoSlot> paSlots = new ArrayList<GoSlot>(this.INTERVALS.size());
        for (int i = 0; i < this.INTERVALS.size(); ++i) {
            paSlots.add(new GoSlot(goTerms.size()));
        }
        this.fillLevels(paSlots, goTerms, "pa");
        goLevels.setPa(paSlots);
        ArrayList<GoSlot> pSlots = new ArrayList<GoSlot>(this.INTERVALS.size());
        for (int i = 0; i < this.INTERVALS.size(); ++i) {
            pSlots.add(new GoSlot(goTerms.size()));
        }
        this.fillLevels(pSlots, goTerms, "p");
        goLevels.setP(pSlots);
    }

    private void fillLevels(List<GoSlot> slots, List<GoTerm> goTerms, String type) {
        switch (type) {
            case "pa": {
                Collections.sort(goTerms, new PAtermOrder());
                this.fill(slots, goTerms, "pa");
                break;
            }
            case "p": {
                Collections.sort(goTerms, new PtermOrder());
                this.fill(slots, goTerms, "p");
            }
        }
    }

    private void fill(List<GoSlot> slots, List<GoTerm> terms, String type) {
        int index = 0;
        for (GoTerm item : terms) {
            double value = 0.0;
            int indexOfItem = terms.indexOf(item);
            switch (type) {
                case "pa": {
                    value = item.getpAdjusted();
                    break;
                }
                case "p": {
                    value = item.getP();
                }
            }
            boolean enc = false;
            int i = index;
            while (i < slots.size() && !enc) {
                GoSlot slot = slots.get(i);
                GoInterval interval = this.INTERVALS.get(i);
                enc = interval.isIntoInterval(value);
                if (enc) {
                    index = i;
                    slot.countAndSetConcentration(value);
                    double exponent = Math.getExponent(value);
                    slot.setMinExponent(exponent);
                    if (indexOfItem != 0) continue;
                    slot.setHaveBonus(true);
                    continue;
                }
                ++i;
            }
            if (enc) continue;
            LOG.info("value = " + value + " ignored");
        }
    }

    public double[] computeAllGoSig(GoLevels significances) {
        double[] r = new double[4];
        double[] pa = this.computeGoSig(significances.getPa());
        double[] p = this.computeGoSig(significances.getP());
        r[0] = pa[0];
        r[1] = pa[1];
        r[2] = p[0];
        r[3] = p[1];
        return r;
    }

    public double[] computeGoSig(List<GoSlot> slots) {
        double maxValue;
        double[] r = new double[2];
        for (int i = 0; i < this.INTERVALS.size(); ++i) {
            GoSlot slot = slots.get(i);
            GoInterval interval = this.INTERVALS.get(i);
            double partial = slot.getConcentration() * interval.getLevelWeight() * (double)interval.getLevel();
            double bonus = 0.0;
            if (slot.haveBonus()) {
                bonus = this.bonusFunction(interval.getLevel(), slot.getMinExponent(), this.BONUS);
            }
            slot.setPartialScore(partial += bonus);
            r[0] = r[0] + partial;
        }
        int maxLevel = this.INTERVALS.get(0).getLevel();
        double maxScore = this.INTERVALS.get(0).getLevelWeight();
        double maxPercentage = 1.0;
        double higherExponent = -41.0;
        r[1] = maxValue = this.function1(maxPercentage) * this.function2(maxLevel, maxScore) + this.bonusFunction(maxLevel, higherExponent, this.BONUS);
        return r;
    }

    private double bonusFunction(int level, double exponent, double bonus) {
        double r = (double)level + bonus;
        return r;
    }

    private double function1(double percentage) {
        double r = percentage;
        return r;
    }

    private double function2(int level, double score) {
        double x;
        double f;
        double r = f = (x = (double)level * score);
        return r;
    }
}

