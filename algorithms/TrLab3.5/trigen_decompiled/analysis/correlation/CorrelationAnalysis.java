/*
 * Decompiled with CFR 0.152.
 */
package analysis.correlation;

import general.Tricluster;
import java.util.List;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.TextUtilities;

public class CorrelationAnalysis {
    private static final Logger LOG = LoggerFactory.getLogger(CorrelationAnalysis.class);
    private static final String CORR_1 = "g-c-t";
    private static final String CORR_2 = "g-t-c";
    private static final String CORR_3 = "c-g-t";
    private static final String CORR_4 = "c-t-g";
    private static final String CORR_5 = "t-g-c";
    private static final String CORR_6 = "t-c-g";
    private double[][][] dataset;

    public CorrelationAnalysis(double[][][] dataset) {
        this.dataset = dataset;
    }

    public double[][] getAllIndexes(Tricluster tricluster) {
        double[][] r = new double[2][6];
        double[][] table1 = this.getVariables(tricluster, CORR_1);
        double pr1 = this.getCoeffMean(table1, "pearson");
        double sp1 = this.getCoeffMean(table1, "spearman");
        r[0][0] = pr1;
        r[1][0] = sp1;
        double[][] table2 = this.getVariables(tricluster, CORR_2);
        double pr2 = this.getCoeffMean(table2, "pearson");
        double sp2 = this.getCoeffMean(table2, "spearman");
        r[0][1] = pr2;
        r[1][1] = sp2;
        double[][] table3 = this.getVariables(tricluster, CORR_3);
        double pr3 = this.getCoeffMean(table3, "pearson");
        double sp3 = this.getCoeffMean(table3, "spearman");
        r[0][2] = pr3;
        r[1][2] = sp3;
        double[][] table4 = this.getVariables(tricluster, CORR_4);
        double pr4 = this.getCoeffMean(table4, "pearson");
        double sp4 = this.getCoeffMean(table4, "spearman");
        r[0][3] = pr4;
        r[1][3] = sp4;
        double[][] table5 = this.getVariables(tricluster, CORR_5);
        double pr5 = this.getCoeffMean(table5, "pearson");
        double sp5 = this.getCoeffMean(table5, "spearman");
        r[0][4] = pr5;
        r[1][4] = sp5;
        double[][] table6 = this.getVariables(tricluster, CORR_6);
        double pr6 = this.getCoeffMean(table6, "pearson");
        double sp6 = this.getCoeffMean(table6, "spearman");
        r[0][5] = pr6;
        r[1][5] = sp6;
        return r;
    }

    public double[] getIndex(Tricluster tricluster, String config) {
        double[][] table = this.getVariables(tricluster, config);
        double pr = this.getCoeffMean(table, "pearson");
        double sp = this.getCoeffMean(table, "spearman");
        double[] r = new double[]{pr, sp};
        return r;
    }

    public double getIndex(Tricluster tricluster, String coefficient, String config) {
        double[][] table = this.getVariables(tricluster, config);
        double r = this.getCoeffMean(table, coefficient);
        return r;
    }

    private double getCoeffMean(double[][] table, String coefficient) {
        double[] topDiagonal;
        double r = 0.0;
        double[][] cor = null;
        switch (coefficient) {
            case "spearman": {
                SpearmansCorrelation spearman = new SpearmansCorrelation();
                cor = spearman.computeCorrelationMatrix(table).getData();
                break;
            }
            case "pearson": {
                PearsonsCorrelation pearson = new PearsonsCorrelation();
                cor = pearson.computeCorrelationMatrix(table).getData();
            }
        }
        double[] values = topDiagonal = CorrelationAnalysis.getTopDiagonal(cor);
        for (int i = 0; i < values.length; ++i) {
            if (!Double.isNaN(values[i])) continue;
            values[i] = 0.0;
        }
        this.absValues(values);
        r = StatUtils.mean(values);
        return r;
    }

    private void absValues(double[] vector) {
        for (int i = 0; i < vector.length; ++i) {
            vector[i] = Math.abs(vector[i]);
        }
    }

    private static double[] getTopDiagonal(double[][] table) {
        int len = table.length;
        int size = (len - 1) * len / 2;
        double[] r = new double[size];
        int index = 0;
        for (int i = 0; i < len - 1; ++i) {
            for (int j = i + 1; j < len; ++j) {
                r[index] = table[i][j];
                ++index;
            }
        }
        return r;
    }

    private double[][] getVariables(Tricluster tricluster, String configuration) {
        double[][] r = null;
        List<Integer>[] cfg = this.getVariableFactorVector(tricluster, configuration);
        List<Integer> variable = cfg[0];
        List<Integer> factor = cfg[1];
        List<Integer> vector = cfg[2];
        r = new double[vector.size()][variable.size() * factor.size()];
        int j = 0;
        int i = 0;
        for (Integer var : variable) {
            for (Integer fac : factor) {
                i = 0;
                for (Integer vec : vector) {
                    r[i][j] = this.getValue(var, fac, vec, configuration);
                    ++i;
                }
                ++j;
            }
        }
        return r;
    }

    private double getValue(int var, int fac, int vec, String cnf) {
        double value = 0.0;
        List<String> config = TextUtilities.splitElements(cnf, "-");
        String cvar = config.get(0);
        String cfac = config.get(1);
        String cvec = config.get(2);
        int gi = 0;
        int ci = 0;
        int ti = 0;
        switch (cvar) {
            case "g": {
                gi = var;
                break;
            }
            case "c": 
            case "s": {
                ci = var;
                break;
            }
            case "t": {
                ti = var;
            }
        }
        switch (cfac) {
            case "g": {
                gi = fac;
                break;
            }
            case "c": 
            case "s": {
                ci = fac;
                break;
            }
            case "t": {
                ti = fac;
            }
        }
        switch (cvec) {
            case "g": {
                gi = vec;
                break;
            }
            case "c": 
            case "s": {
                ci = vec;
                break;
            }
            case "t": {
                ti = vec;
            }
        }
        value = this.dataset[gi][ci][ti];
        return value;
    }

    private List<Integer>[] getVariableFactorVector(Tricluster tricluster, String configuration) {
        List[] r = new List[3];
        List<String> config = TextUtilities.splitElements(configuration, "-");
        String cvar = config.get(0);
        String cfac = config.get(1);
        String cvec = config.get(2);
        switch (cvar) {
            case "g": {
                r[0] = tricluster.getGenes();
                break;
            }
            case "c": 
            case "s": {
                r[0] = tricluster.getSamples();
                break;
            }
            case "t": {
                r[0] = tricluster.getTimes();
            }
        }
        switch (cfac) {
            case "g": {
                r[1] = tricluster.getGenes();
                break;
            }
            case "c": 
            case "s": {
                r[1] = tricluster.getSamples();
                break;
            }
            case "t": {
                r[1] = tricluster.getTimes();
            }
        }
        switch (cvec) {
            case "g": {
                r[2] = tricluster.getGenes();
                break;
            }
            case "c": 
            case "s": {
                r[2] = tricluster.getSamples();
                break;
            }
            case "t": {
                r[2] = tricluster.getTimes();
            }
        }
        return r;
    }
}

