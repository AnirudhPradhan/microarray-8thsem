/*
 * Decompiled with CFR 0.152.
 */
package analysis;

import analysis.Solution;
import analysis.TRIQ;
import analysis.correlation.PEQ;
import analysis.correlation.SPQ;
import analysis.graphical.GRQ;
import java.util.List;

public class Ctriq
implements TRIQ {
    private List<Solution> solutions;
    private GRQ grq;
    private PEQ peq;
    private SPQ spq;
    private double wGrq;
    private double wPeq;
    private double wSpq;

    public Ctriq(List<Solution> solutions, GRQ grq, PEQ peq, SPQ spq, double wGrq, double wPeq, double wSpq) {
        this.solutions = solutions;
        this.grq = grq;
        this.peq = peq;
        this.spq = spq;
        this.wGrq = wGrq;
        this.wPeq = wPeq;
        this.wSpq = wSpq;
    }

    @Override
    public void computeTRIQ() {
        this.grq.computeGrq();
        this.peq.computePeq();
        this.spq.computeSpq();
        this.compute();
    }

    private void compute() {
        for (Solution sol : this.solutions) {
            this.computeOneSolution(sol);
        }
    }

    private void computeOneSolution(Solution sol) {
        double triq = 0.0;
        double grq = sol.getValue("grq");
        double peq = sol.getValue("peq");
        double spq = sol.getValue("spq");
        double wgrq = grq * this.wGrq;
        double wpeq = peq * this.wPeq;
        double wspq = spq * this.wSpq;
        double num = wgrq + wpeq + wspq;
        double den = this.wGrq + this.wPeq + this.wSpq;
        triq = num / den;
        sol.putValue("triq", triq);
    }
}

