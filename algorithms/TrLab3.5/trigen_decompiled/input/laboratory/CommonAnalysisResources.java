/*
 * Decompiled with CFR 0.152.
 */
package input.laboratory;

import general.Tricluster;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommonAnalysisResources {
    private char analysisType;
    private String experimentAlias;
    private List<Tricluster> solutions;

    public CommonAnalysisResources(char analysisType, String experimentAlias, List<Tricluster> solutions) {
        this.analysisType = analysisType;
        this.experimentAlias = experimentAlias;
        this.solutions = solutions;
    }

    public CommonAnalysisResources(char analysisType, String experimentAlias) {
        this.analysisType = analysisType;
        this.experimentAlias = experimentAlias;
        this.solutions = new ArrayList<Tricluster>();
    }

    public void loadOneSolution(Collection<Integer> genes, Collection<Integer> samples, Collection<Integer> times) {
        Tricluster tri = new Tricluster();
        ArrayList<Integer> lgenes = new ArrayList<Integer>(genes.size());
        ArrayList<Integer> lsamples = new ArrayList<Integer>(samples.size());
        ArrayList<Integer> ltimes = new ArrayList<Integer>(times.size());
        for (Integer g : genes) {
            lgenes.add(new Integer(g));
        }
        for (Integer s : samples) {
            lsamples.add(new Integer(s));
        }
        for (Integer t : times) {
            ltimes.add(new Integer(t));
        }
        tri.loadCoordinates(lgenes, lsamples, ltimes);
        this.solutions.add(tri);
    }

    public char getAnalysisType() {
        return this.analysisType;
    }

    public String getExperimentAlias() {
        return this.experimentAlias;
    }

    public List<Tricluster> getSolutions() {
        return this.solutions;
    }

    public String toString() {
        String r = "";
        r = r + "Analysis type = " + this.analysisType + "\nExperiment alias = " + this.experimentAlias + "\nFirst solution = " + this.solutions.get(0) + "\nLast solution = " + this.solutions.get(this.solutions.size() - 1);
        return r;
    }
}

