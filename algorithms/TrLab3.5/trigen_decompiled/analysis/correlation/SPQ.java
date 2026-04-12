/*
 * Decompiled with CFR 0.152.
 */
package analysis.correlation;

import analysis.Solution;
import analysis.correlation.CorrelationAnalysis;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SPQ {
    private static final Logger LOG = LoggerFactory.getLogger(SPQ.class);
    private static final String TYPE = "spearman";
    private static final String CONFIG = "g-c-t";
    private CorrelationAnalysis correlation;
    private List<Solution> solutions;

    public SPQ(CorrelationAnalysis correlation, List<Solution> solutions) {
        this.correlation = correlation;
        this.solutions = solutions;
    }

    public void computeSpq() {
        for (Solution sol : this.solutions) {
            this.computeOneSolution(sol);
        }
    }

    private void computeOneSolution(Solution sol) {
        double spq = this.computeMember(sol, CONFIG);
        sol.putValue("spq", spq);
    }

    private double computeMember(Solution sol, String config) {
        double co = 0.0;
        co = this.correlation.getIndex(sol.getTricluster(), TYPE, config);
        return co;
    }
}

