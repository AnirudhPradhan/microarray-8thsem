/*
 * Decompiled with CFR 0.152.
 */
package analysis.graphical;

import analysis.Solution;
import analysis.graphical.GRQevaluator;
import analysis.graphical.MslEvaluator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GRQ {
    private static final Logger LOG = LoggerFactory.getLogger(GRQ.class);
    private GRQevaluator method;
    private List<Solution> solutions;

    public GRQ(GRQevaluator method, List<Solution> solutions) {
        this.method = method;
        this.solutions = solutions;
    }

    public void setMethod(MslEvaluator method) {
        this.method = method;
    }

    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }

    public void computeGrq() {
        for (Solution sol : this.solutions) {
            this.computeOneSolution(sol);
        }
    }

    private void computeOneSolution(Solution sol) {
        double multi = this.method.computeFitness(sol.getTricluster());
        double nmulti = multi / this.method.getHighestValue();
        double grq = 1.0 - nmulti;
        sol.putValue("grq", grq);
    }
}

