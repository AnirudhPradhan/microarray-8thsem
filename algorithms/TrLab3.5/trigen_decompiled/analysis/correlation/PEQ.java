/*
 * Decompiled with CFR 0.152.
 */
package analysis.correlation;

import analysis.Solution;
import analysis.correlation.CorrelationAnalysis;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PEQ {
    private static final Logger LOG = LoggerFactory.getLogger(PEQ.class);
    private static final String TYPE = "pearson";
    private static final String CONFIG = "g-c-t";
    private CorrelationAnalysis correlation;
    private List<Solution> solutions;

    public PEQ(CorrelationAnalysis correlation, List<Solution> solutions) {
        this.correlation = correlation;
        this.solutions = solutions;
    }

    public void computePeq() {
        for (Solution sol : this.solutions) {
            this.computeOneSolution(sol);
        }
    }

    private void computeOneSolution(Solution sol) {
        double peq = this.computeMember(sol, CONFIG);
        sol.putValue("peq", peq);
    }

    private double computeMember(Solution sol, String config) {
        double co = 0.0;
        co = this.correlation.getIndex(sol.getTricluster(), TYPE, config);
        return co;
    }
}

