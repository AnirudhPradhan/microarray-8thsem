/*
 * Decompiled with CFR 0.152.
 */
package analysis;

import analysis.Solution;
import analysis.SolutionOrder;
import analysis.TRIQ;
import input.datasets.Common;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Experiment {
    private static final Logger LOG = LoggerFactory.getLogger(Experiment.class);
    private Common resources;
    private List<Solution> solutions;
    private char analysisType;
    private String experimentName;
    private TRIQ triqAnalysis;
    private Map<String, Object> expVariables;

    public Experiment(List<Solution> solutions, char type, String experimentName, Common resources, TRIQ triqAnalysis) {
        this.solutions = solutions;
        this.resources = resources;
        this.analysisType = type;
        this.experimentName = experimentName;
        this.triqAnalysis = triqAnalysis;
        this.expVariables = new HashMap<String, Object>();
        this.intializeVariables();
    }

    private void intializeVariables() {
        double init = -1.0;
        this.expVariables.put("bestsolution", "");
        this.expVariables.put("besttriq", new Double(init));
        this.expVariables.put("mean", new Double(init));
        this.expVariables.put("stdev", new Double(init));
        this.expVariables.put("bestsolutionn", "");
        this.expVariables.put("besttriqn", new Double(init));
        this.expVariables.put("meann", new Double(init));
        this.expVariables.put("stdevn", new Double(init));
    }

    public char getAnalysisType() {
        return this.analysisType;
    }

    public String getExperimentName() {
        return this.experimentName;
    }

    public List<Solution> getSolutions() {
        return this.solutions;
    }

    public String getDatasetName() {
        return this.resources.getDatasetName();
    }

    public Common getDataset() {
        return this.resources;
    }

    public Object getValue(String variable) {
        return this.expVariables.get(variable);
    }

    public void computeAnalysis() throws IOException, InterruptedException {
        this.computeAnalysisSolutionLevel();
        this.computeAnalysisExperimentLevel();
    }

    public void computeAnalysisSolutionLevel() throws IOException, InterruptedException {
        this.triqAnalysis.computeTRIQ();
    }

    public void computeAnalysisExperimentLevel() {
        Collections.sort(this.solutions, new SolutionOrder("triq", "g-l"));
        this.expVariables.put("bestsolution", this.solutions.get(0));
        double value = this.solutions.get(0).getValue("triq");
        this.expVariables.put("besttriq", new Double(value));
        double[] triqs = this.getAllValues("triq");
        double mean = StatUtils.mean(triqs);
        this.expVariables.put("mean", new Double(mean));
        StandardDeviation sd = new StandardDeviation();
        double stddev = sd.evaluate(triqs);
        this.expVariables.put("stdev", new Double(stddev));
        if (this.analysisType == 'b') {
            Collections.sort(this.solutions, new SolutionOrder("triqn", "g-l"));
            this.expVariables.put("bestsolutionn", this.solutions.get(0));
            double valuen = this.solutions.get(0).getValue("triqn");
            this.expVariables.put("besttriqn", new Double(valuen));
            double[] triqsn = this.getAllValues("triqn");
            double meann = StatUtils.mean(triqsn);
            this.expVariables.put("meann", new Double(meann));
            StandardDeviation sdn = new StandardDeviation();
            double stddevn = sdn.evaluate(triqsn);
            this.expVariables.put("stdevn", new Double(stddevn));
        }
        Collections.sort(this.solutions);
    }

    private double[] getAllValues(String variable) {
        double[] r = new double[this.solutions.size()];
        int i = 0;
        for (Solution sol : this.solutions) {
            r[i] = sol.getValue(variable);
            ++i;
        }
        return r;
    }
}

