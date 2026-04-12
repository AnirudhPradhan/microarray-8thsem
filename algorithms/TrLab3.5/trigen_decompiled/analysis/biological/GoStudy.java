/*
 * Decompiled with CFR 0.152.
 */
package analysis.biological;

import analysis.biological.GoTerm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoStudy {
    private static final Logger LOG = LoggerFactory.getLogger(GoStudy.class);
    private List<GoTerm> goTerms;
    private int popTotal;
    private int studyTotal;
    private String studyName;

    public GoStudy(String studyName) {
        this.studyName = studyName;
        this.goTerms = new ArrayList<GoTerm>();
    }

    public void addGoTerm(GoTerm goterm) {
        this.goTerms.add(goterm);
    }

    public List<GoTerm> getGoTerms() {
        return this.goTerms;
    }

    public int getPopTotal() {
        return this.popTotal;
    }

    public void setPopTotal(int popTotal) {
        this.popTotal = popTotal;
    }

    public int getStudyTotal() {
        return this.studyTotal;
    }

    public void setStudyTotal(int studyTotal) {
        this.studyTotal = studyTotal;
    }

    public String getStudyName() {
        return this.studyName;
    }

    public String toString() {
        String r = "GO study " + this.studyName + " :\n" + "pop total=" + this.popTotal + "\n" + "study total=" + this.studyTotal + "\n" + "Go terms (" + this.goTerms.size() + ") :\n";
        String terms = "";
        for (GoTerm t : this.goTerms) {
            terms = terms + t.toString() + "\n";
        }
        return r + terms;
    }

    public double[] getVariable(String type) {
        double[] r = new double[this.goTerms.size()];
        int i = 0;
        for (GoTerm go : this.goTerms) {
            switch (type) {
                case "pa": {
                    r[i] = go.getpAdjusted();
                    break;
                }
                case "p": {
                    r[i] = go.getP();
                    break;
                }
                case "ps": {
                    r[i] = go.getPs();
                }
            }
            ++i;
        }
        return r;
    }

    public double[] getOrderedVariable(String type) {
        double[] r = new double[this.goTerms.size()];
        int i = 0;
        for (GoTerm go : this.goTerms) {
            switch (type) {
                case "pa": {
                    r[i] = go.getpAdjusted();
                    break;
                }
                case "p": {
                    r[i] = go.getP();
                }
            }
            ++i;
        }
        Arrays.sort(r);
        return r;
    }
}

