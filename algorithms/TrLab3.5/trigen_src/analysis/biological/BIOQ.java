/*
 * Decompiled with CFR 0.152.
 */
package analysis.biological;

import analysis.Solution;
import analysis.biological.BiologicalAnalysis;
import analysis.biological.GoLevels;
import analysis.biological.GoSignificance;
import analysis.biological.GoStudy;
import general.Tricluster;
import input.datasets.Biological;
import input.laboratory.Options;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import labutils.Conversions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BIOQ {
    private static final Logger LOG = LoggerFactory.getLogger(BIOQ.class);
    private List<Solution> solutions;
    private Biological resources;
    private Options options;

    public BIOQ(List<Solution> solutions, Biological resources, Options options) {
        this.solutions = solutions;
        this.resources = resources;
        this.options = options;
    }

    public void computeBioq() throws IOException, InterruptedException {
        List<Tricluster> triclusters = Conversions.fromSolutionsToTriclusters(this.solutions);
        BiologicalAnalysis goApp = new BiologicalAnalysis(this.resources, this.options);
        List<GoStudy> studies = goApp.getAnalysis(triclusters, this.options.isGoResources());
        GoSignificance significance = GoSignificance.getInstance();
        significance.setConfiguration(this.options.getThreshold(), this.options.getBase(), this.options.getStep(), this.options.getDifference(), this.options.getBase());
        for (Solution sol : this.solutions) {
            this.computeOneSolution(sol, studies, significance);
        }
    }

    private void computeOneSolution(Solution sol, List<GoStudy> studies, GoSignificance significance) {
        if (studies == null || studies.isEmpty()) {
            GoLevels emptyLevels = new GoLevels();
            emptyLevels.setPa(new ArrayList<>());
            emptyLevels.setP(new ArrayList<>());
            sol.setGoLevels(emptyLevels);
            sol.setGoStudy(null);
            return;
        }
        GoStudy goStudy = studies.get(sol.getIndex() - 1);
        GoLevels goLevels = significance.getGoLevels(goStudy);
        sol.setGoLevels(goLevels);
        sol.setGoStudy(goStudy);
        double[] sig = significance.computeAllGoSig(goLevels);
        double maxpasig = sig[1];
        double pasig = sig[0];
        double npasig = pasig / maxpasig;
        sol.putValue("bioq", npasig);
        double maxpsig = sig[3];
        double psig = sig[2];
        double npsig = psig / maxpsig;
        sol.putValue("bioqn", npsig);
    }
}
