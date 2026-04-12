/*
 * Decompiled with CFR 0.152.
 */
package analysis;

import analysis.Btriq;
import analysis.Ctriq;
import analysis.Experiment;
import analysis.Solution;
import analysis.biological.BIOQ;
import analysis.correlation.CorrelationAnalysis;
import analysis.correlation.PEQ;
import analysis.correlation.SPQ;
import analysis.graphical.GRQ;
import analysis.graphical.MslEvaluator;
import general.Tricluster;
import input.datasets.Biological;
import input.datasets.Common;
import input.laboratory.AnalysisResources;
import input.laboratory.CommonAnalysisResources;
import input.laboratory.Options;
import input.laboratory.ReducedAnalysisResources;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.WorkFlowUtilities;

public class AnalysisFactory {
    private static final Logger LOG = LoggerFactory.getLogger(AnalysisFactory.class);

    public static List<Experiment> getAllExperiments(List<CommonAnalysisResources> resources, Options options) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        ArrayList<Experiment> experiments = new ArrayList<Experiment>();
        for (CommonAnalysisResources rs : resources) {
            Experiment e;
            if (rs instanceof AnalysisResources) {
                AnalysisResources ar = (AnalysisResources)rs;
                e = AnalysisFactory.getExperiment(rs.getSolutions(), options, ar.getControl().getDataset(), ar.getExperimentAlias());
                experiments.add(e);
                continue;
            }
            if (!(rs instanceof ReducedAnalysisResources)) continue;
            ReducedAnalysisResources rd = (ReducedAnalysisResources)rs;
            e = AnalysisFactory.getExperiment(rd.getSolutions(), options, rd.getDataset(), rd.getExperimentAlias());
            experiments.add(e);
        }
        return experiments;
    }

    public static Experiment getExperiment(List<Tricluster> triclusters, Options options, Common dataset, String experimentName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Experiment r = null;
        char type = WorkFlowUtilities.getExperimentTypeFromDataset(dataset);
        List<Solution> solutions = AnalysisFactory.buildSolutions(triclusters);
        MslEvaluator method = new MslEvaluator();
        method.loadParameters(dataset.getDataset());
        GRQ grqAnalysis = new GRQ(method, solutions);
        CorrelationAnalysis correlation = new CorrelationAnalysis(dataset.getDataset());
        PEQ peqAnalysis = new PEQ(correlation, solutions);
        SPQ spqAnalysis = new SPQ(correlation, solutions);
        if (type == 'b') {
            BIOQ bioqAnalysis = new BIOQ(solutions, (Biological)dataset, options);
            Btriq triqAnalysis = new Btriq(solutions, grqAnalysis, peqAnalysis, spqAnalysis, bioqAnalysis, options.getGraphical(), options.getPearson(), options.getSpearman(), options.getBiological());
            r = new Experiment(solutions, type, experimentName, dataset, triqAnalysis);
        } else {
            Ctriq triqAnalysis = new Ctriq(solutions, grqAnalysis, peqAnalysis, spqAnalysis, options.getCgrq(), options.getCpeq(), options.getCspq());
            r = new Experiment(solutions, type, experimentName, dataset, triqAnalysis);
        }
        return r;
    }

    public static Experiment getOPTexperiment(List<Tricluster> triclusters, Options options, Biological dataset, String experimentName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Experiment r = null;
        char type = WorkFlowUtilities.getExperimentTypeFromDataset(dataset);
        List<Solution> solutions = AnalysisFactory.buildSolutions(triclusters);
        MslEvaluator method = new MslEvaluator();
        method.loadParameters(dataset.getDataset());
        GRQ grqAnalysis = new GRQ(method, solutions);
        CorrelationAnalysis correlation = new CorrelationAnalysis(dataset.getDataset());
        PEQ peqAnalysis = new PEQ(correlation, solutions);
        SPQ spqAnalysis = new SPQ(correlation, solutions);
        BIOQ bioqAnalysis = new BIOQ(solutions, dataset, options);
        Btriq triqAnalysis = new Btriq(solutions, grqAnalysis, peqAnalysis, spqAnalysis, bioqAnalysis, options.getGraphical(), options.getPearson(), options.getSpearman(), options.getBiological());
        r = new Experiment(solutions, type, experimentName, dataset, triqAnalysis);
        return r;
    }

    private static List<Solution> buildSolutions(List<Tricluster> triclusters) {
        LinkedList<Solution> r = new LinkedList<Solution>();
        int index = 1;
        for (Tricluster tri : triclusters) {
            r.add(new Solution(index, tri));
            ++index;
        }
        return r;
    }
}

