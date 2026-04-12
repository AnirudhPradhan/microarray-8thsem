/*
 * Decompiled with CFR 0.152.
 */
package analysis.biological;

public class GoTerm {
    private String Id;
    private String Name;
    private double p;
    private double pAdjusted;
    private double pMin;
    private int popTerm;
    private int studyTerm;
    private int ps;

    public GoTerm(String id, String name, double p, double pAdjusted, double pMin, int popTerm, int studyTerm) {
        this.Id = id;
        this.Name = name;
        this.p = p;
        this.pAdjusted = pAdjusted;
        this.pMin = pMin;
        this.popTerm = popTerm;
        this.studyTerm = studyTerm;
        this.ps = popTerm - studyTerm;
    }

    public String getId() {
        return this.Id;
    }

    public String getName() {
        return this.Name;
    }

    public double getP() {
        return this.p;
    }

    public double getpAdjusted() {
        return this.pAdjusted;
    }

    public double getpMin() {
        return this.pMin;
    }

    public int getPopTerm() {
        return this.popTerm;
    }

    public int getStudyTerm() {
        return this.studyTerm;
    }

    public int getPs() {
        return this.ps;
    }

    public String toString() {
        return this.Id + "\t" + this.pAdjusted + "\t" + this.p + "\t" + this.Name + "\t" + this.pMin + "\t" + this.popTerm + "\t" + this.studyTerm + "\t" + this.ps;
    }
}

