/*
 * Decompiled with CFR 0.152.
 */
package input.algorithm;

import input.Parameters;
import input.algorithm.Implementation;
import input.algorithm.InvalidImplementationException;
import input.algorithm.ModelsLoader;
import input.algorithm.WrongContolException;
import input.datasets.Common;
import java.io.File;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.TextUtilities;

public class Control
extends Parameters {
    private static final Logger LOG = LoggerFactory.getLogger(Control.class);
    private static final int MIN_N = 1;
    private static final int MAX_N = 10000000;
    private static final int MIN_G = 1;
    private static final int MAX_G = 10000000;
    private static final int MIN_I = 3;
    private static final int MAX_I = 10000000;
    private static final double MIN_ALE = 0.0;
    private static final double MAX_ALE = 1.0;
    private static final double MIN_SEL = 0.0;
    private static final double MAX_SEL = 1.0;
    private static final double MIN_MUT = 0.0;
    private static final double MAX_MUT = 1.0;
    private static final double MIN_WF = 0.0;
    private static final double MAX_WF = 1.0;
    private static final double MIN_WG = 0.0;
    private static final double MAX_WG = 1.0;
    private static final double MIN_WC = 0.0;
    private static final double MAX_WC = 1.0;
    private static final double MIN_WT = 0.0;
    private static final double MAX_WT = 1.0;
    private static final double MIN_WOG = 0.0;
    private static final double MAX_WOG = 1.0;
    private static final double MIN_WOC = 0.0;
    private static final double MAX_WOC = 1.0;
    private static final double MIN_WOT = 0.0;
    private static final double MAX_WOT = 1.0;
    private static final int MIN_MING = 2;
    private static final int MIN_MINC = 2;
    private static final int MIN_MINT = 2;
    private static final int MIN_THREADS = 0;
    private static final int MAX_THREADS = 12;
    private int n;
    private int g;
    private int i;
    private double ale;
    private double sel;
    private double mut;
    private double wf;
    private double wg;
    private double wc;
    private double wt;
    private double wog;
    private double woc;
    private double wot;
    private int minG;
    private int minC;
    private int minT;
    private int maxG;
    private int maxC;
    private int maxT;
    private int threads;
    private Implementation implementation;
    private Common dataset;
    private String individualMethod;
    private String fitnessMethod;
    private String dataHierarchyMethod;
    private String stoppingCriterionMethod;
    private String solutionCriterionMethod;
    private String initialPopMethod;
    private String selectionMethod;
    private String crossoverMethod;
    private String mutationMethod;

    public Control(Common dataset, Properties prop) throws WrongContolException, InvalidImplementationException {
        super(new File(TextUtilities.getRootPathWithSlash(prop.getProperty("out"))), TextUtilities.getFileName(prop.getProperty("out")));
        this.specificCheckings(dataset, prop);
    }

    @Override
    public String toString() {
        String r = super.toString();
        r = r + "\nDataset = " + this.dataset.getDatasetName() + " (ID=" + this.dataset.getDatasetID() + ")" + "\nN = " + this.n + "\nG = " + this.g + "\nI = " + this.i + "\nAle = " + this.ale + "\nSel = " + this.sel + "\nMut = " + this.mut + "\nWf = " + this.wf + "\nWg = " + this.wg + "\nWc = " + this.wc + "\nWt = " + this.wt + "\nWOg = " + this.wog + "\nWOc = " + this.woc + "\nWOt = " + this.wot + "\nminG = " + this.minG + "\nminC = " + this.minC + "\nminT = " + this.minT + "\nmaxG = " + this.maxG + "\nmaxC = " + this.maxC + "\nmaxT = " + this.maxT + "\nthreads = " + this.threads + "\n" + this.implementation.toString() + "\nIndividual method = " + this.individualMethod + "\nFitness method = " + this.fitnessMethod + "\nData hierarchy method = " + this.dataHierarchyMethod + "\nStopping criterion method = " + this.stoppingCriterionMethod + "\nSolution criterion method = " + this.solutionCriterionMethod + "\nInitial population method = " + this.initialPopMethod + "\nSelection method = " + this.selectionMethod + "\nCrossover method = " + this.crossoverMethod + "\nMutation method = " + this.mutationMethod;
        return r;
    }

    public int getN() {
        return this.n;
    }

    public int getG() {
        return this.g;
    }

    public int getI() {
        return this.i;
    }

    public double getAle() {
        return this.ale;
    }

    public double getSel() {
        return this.sel;
    }

    public double getMut() {
        return this.mut;
    }

    public double getWf() {
        return this.wf;
    }

    public double getWg() {
        return this.wg;
    }

    public double getWc() {
        return this.wc;
    }

    public double getWt() {
        return this.wt;
    }

    public double getWog() {
        return this.wog;
    }

    public double getWoc() {
        return this.woc;
    }

    public double getWot() {
        return this.wot;
    }

    public int getMinG() {
        return this.minG;
    }

    public int getMinC() {
        return this.minC;
    }

    public int getMinT() {
        return this.minT;
    }

    public int getMaxG() {
        return this.maxG;
    }

    public int getMaxC() {
        return this.maxC;
    }

    public int getMaxT() {
        return this.maxT;
    }

    public int getThreads() {
        return this.threads;
    }

    public Implementation getImplementation() {
        return this.implementation;
    }

    public Common getDataset() {
        return this.dataset;
    }

    public String getIndividualMethod() {
        return this.individualMethod;
    }

    public String getFitnessMethod() {
        return this.fitnessMethod;
    }

    public String getDataHierarchyMethod() {
        return this.dataHierarchyMethod;
    }

    public String getStoppingCriterionMethod() {
        return this.stoppingCriterionMethod;
    }

    public String getSolutionCriterionMethod() {
        return this.solutionCriterionMethod;
    }

    public String getInitialPopMethod() {
        return this.initialPopMethod;
    }

    public String getSelectionMethod() {
        return this.selectionMethod;
    }

    public String getCrossoverMethod() {
        return this.crossoverMethod;
    }

    public String getMutationMethod() {
        return this.mutationMethod;
    }

    private void specificCheckings(Common dataset, Properties prop) throws WrongContolException, InvalidImplementationException {
        this.n = Integer.parseInt(prop.getProperty("N"));
        this.g = Integer.parseInt(prop.getProperty("G"));
        this.i = Integer.parseInt(prop.getProperty("I"));
        this.ale = Double.parseDouble(prop.getProperty("Ale"));
        this.sel = Double.parseDouble(prop.getProperty("Sel"));
        this.mut = Double.parseDouble(prop.getProperty("Mut"));
        this.wf = Double.parseDouble(prop.getProperty("Wf"));
        this.wg = Double.parseDouble(prop.getProperty("Wg"));
        this.wc = Double.parseDouble(prop.getProperty("Wc"));
        this.wt = Double.parseDouble(prop.getProperty("Wt"));
        this.wog = Double.parseDouble(prop.getProperty("WOg"));
        this.woc = Double.parseDouble(prop.getProperty("WOc"));
        this.wot = Double.parseDouble(prop.getProperty("WOt"));
        String a_minG = prop.getProperty("minG");
        String a_maxG = prop.getProperty("maxG");
        String a_minC = prop.getProperty("minC");
        String a_maxC = prop.getProperty("maxC");
        String a_minT = prop.getProperty("minT");
        String a_maxT = prop.getProperty("maxT");
        this.minG = a_minG != null ? Integer.parseInt(a_minG) : dataset.getDefMinG();
        this.maxG = a_maxG != null ? Integer.parseInt(a_maxG) : dataset.getDefMaxG();
        this.minC = a_minC != null ? Integer.parseInt(a_minC) : dataset.getDefMinC();
        this.maxC = a_maxC != null ? Integer.parseInt(a_maxC) : dataset.getDefMaxC();
        this.minT = a_minT != null ? Integer.parseInt(a_minT) : dataset.getDefMinT();
        this.maxT = a_maxT != null ? Integer.parseInt(a_maxT) : dataset.getDefMaxT();
        this.threads = Integer.parseInt(prop.getProperty("threads"));
        String errMessage = "";
        boolean error = false;
        if (!(errMessage = errMessage + this.checkInterval("N", this.n, 1, 10000000)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("G", this.g, 1, 10000000)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("I", this.i, 3, 10000000)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("Ale", this.ale, 0.0, 1.0)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("Sel", this.sel, 0.0, 1.0)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("Mut", this.mut, 0.0, 1.0)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("Wf", this.wf, 0.0, 1.0)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("Wg", this.wg, 0.0, 1.0)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("Wc", this.wc, 0.0, 1.0)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("Wt", this.wt, 0.0, 1.0)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("WOg", this.wog, 0.0, 1.0)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("WOc", this.woc, 0.0, 1.0)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("WOt", this.wot, 0.0, 1.0)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkMin("minG", this.minG, 2)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkMin("minC", this.minC, 2)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkMin("minT", this.minT, 2)).equalsIgnoreCase("")) {
            error = true;
        }
        if (this.maxG == -1) {
            this.maxG = dataset.getGeneSize();
        }
        if (this.maxC == -1) {
            this.maxC = dataset.getSampleSize();
        }
        if (this.maxT == -1) {
            this.maxT = dataset.getTimeSize();
        }
        if (!(errMessage = errMessage + this.checkMax("maxG", this.maxG, dataset.getGeneSize())).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkMax("maxC", this.maxC, dataset.getSampleSize())).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkMax("maxT", this.maxT, dataset.getTimeSize())).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkMaxMin("Gene Sizes", this.maxG, this.minG)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkMaxMin("Sample Sizes", this.maxC, this.minC)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkMaxMin("Time Sizes", this.maxT, this.minT)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("threads", this.threads, 0, 12)).equalsIgnoreCase("")) {
            error = true;
        }
        if (error) {
            throw new WrongContolException(errMessage);
        }
        ModelsLoader ml = new ModelsLoader();
        this.individualMethod = prop.getProperty("individual");
        this.fitnessMethod = prop.getProperty("fitness");
        this.dataHierarchyMethod = prop.getProperty("datahierarchy");
        this.stoppingCriterionMethod = prop.getProperty("stoppingcriterion");
        this.solutionCriterionMethod = prop.getProperty("solutioncriterion");
        this.initialPopMethod = prop.getProperty("initialpop");
        this.selectionMethod = prop.getProperty("selection");
        this.crossoverMethod = prop.getProperty("crossover");
        this.mutationMethod = prop.getProperty("mutation");
        this.implementation = ml.checkImplementation(this.individualMethod, this.fitnessMethod, this.dataHierarchyMethod, this.stoppingCriterionMethod, this.solutionCriterionMethod, this.initialPopMethod, this.selectionMethod, this.crossoverMethod, this.mutationMethod);
        this.dataset = dataset;
    }

    private String checkMax(String parameter, int value, int max) {
        String r = "";
        if (value > max) {
            r = parameter + " must be lower or equal to " + max + "\n";
        }
        return r;
    }

    private String checkMin(String parameter, int value, int min) {
        String r = "";
        if (value < min) {
            r = parameter + " must be greater or equal to " + min + "\n";
        }
        return r;
    }

    private String checkMaxMin(String parameter, int max, int min) {
        String r = "";
        if (min > max) {
            r = parameter + " : min size (" + min + ") is greater than max size (" + max + ")\n";
        }
        return r;
    }

    private String checkInterval(String parameter, int value, int min, int max) {
        String r = "";
        if (value < min || value > max) {
            r = parameter + " must be set to [" + min + "," + max + "]\n";
        }
        return r;
    }

    private String checkInterval(String parameter, double value, double min, double max) {
        String r = "";
        if (value < min || value > max) {
            r = parameter + " must be set to [" + min + "," + max + "]\n";
        }
        return r;
    }
}

