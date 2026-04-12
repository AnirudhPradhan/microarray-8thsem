/*
 * Decompiled with CFR 0.152.
 */
package analysis.biological;

import analysis.Solution;
import analysis.biological.BiologicalAnalysis;
import analysis.biological.GoLevels;
import analysis.biological.GoSignificance;
import analysis.biological.GoSlot;
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

    public BIOQ(List<Solution> list, Biological biological, Options options) {
        this.solutions = list;
        this.resources = biological;
        this.options = options;
    }

    public void computeBioq() throws IOException, InterruptedException {
        List<Tricluster> list = Conversions.fromSolutionsToTriclusters(this.solutions);
        BiologicalAnalysis biologicalAnalysis = new BiologicalAnalysis(this.resources, this.options);
        List<GoStudy> list2 = biologicalAnalysis.getAnalysis(list, this.options.isGoResources());
        GoSignificance goSignificance = GoSignificance.getInstance();
        goSignificance.setConfiguration(this.options.getThreshold(), this.options.getBase(), this.options.getStep(), this.options.getDifference(), this.options.getBase());
        for (Solution solution : this.solutions) {
            this.computeOneSolution(solution, list2, goSignificance);
        }
    }

    private void computeOneSolution(Solution solution, List<GoStudy> list, GoSignificance goSignificance) {
        if (list == null || list.isEmpty()) {
            GoLevels goLevels = new GoLevels();
            goLevels.setPa(new ArrayList<GoSlot>());
            goLevels.setP(new ArrayList<GoSlot>());
            solution.setGoLevels(goLevels);
            solution.setGoStudy(null);
            return;
        }
        GoStudy goStudy = list.get(solution.getIndex() - 1);
        GoLevels goLevels = goSignificance.getGoLevels(goStudy);
        solution.setGoLevels(goLevels);
        solution.setGoStudy(goStudy);
        double[] dArray = goSignificance.computeAllGoSig(goLevels);
        double d = dArray[1];
        double d2 = dArray[0];
        double d3 = d2 / d;
        solution.putValue("bioq", d3);
        double d4 = dArray[3];
        double d5 = dArray[2];
        double d6 = d5 / d4;
        solution.putValue("bioqn", d6);
    }
}

