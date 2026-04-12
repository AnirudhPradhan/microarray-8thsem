/*
 * Decompiled with CFR 0.152.
 */
package analysis.graphical;

import analysis.graphical.GRQevaluator;
import analysis.graphical.OriginalTransformationSupport;
import general.Tricluster;
import java.util.ArrayList;
import java.util.List;
import labutils.Conversions;
import org.apache.commons.math3.stat.StatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MslEvaluator
implements GRQevaluator {
    private static final Logger LOG = LoggerFactory.getLogger(MslEvaluator.class);
    private static final double HIGHEST_VALUE = Math.PI * 2;
    private double[][][] dataset;

    @Override
    public void loadParameters(double[][][] dataset) {
        this.dataset = dataset;
    }

    @Override
    public double getHighestValue() {
        return Math.PI * 2;
    }

    @Override
    public double computeFitness(Tricluster tricluster) {
        double r = 0.0;
        OriginalTransformationSupport transformer = new OriginalTransformationSupport(this.dataset);
        List<double[][][]> trs = transformer.transform(tricluster);
        ArrayList<Double> wholeDiff = new ArrayList<Double>();
        ArrayList<Double> wholeMeans = new ArrayList<Double>();
        double[][][] ang0 = this.buildAnglesCube(trs.get(0));
        List<Double> diff0 = this.buildDifferencesTable(ang0);
        wholeDiff.addAll(diff0);
        double gctMean = StatUtils.mean(Conversions.fromListOfDoubleToArray(diff0));
        wholeMeans.add(new Double(gctMean));
        double[][][] ang1 = this.buildAnglesCube(trs.get(1));
        List<Double> diff1 = this.buildDifferencesTable(ang1);
        wholeDiff.addAll(diff1);
        double gtcMean = StatUtils.mean(Conversions.fromListOfDoubleToArray(diff1));
        wholeMeans.add(new Double(gtcMean));
        double[][][] ang4 = this.buildAnglesCube(trs.get(4));
        List<Double> diff4 = this.buildDifferencesTable(ang4);
        wholeDiff.addAll(diff4);
        double tgcMean = StatUtils.mean(Conversions.fromListOfDoubleToArray(diff4));
        wholeMeans.add(new Double(tgcMean));
        double[] means = Conversions.fromListOfDoubleToArray(wholeMeans);
        r = StatUtils.mean(means);
        return r;
    }

    private double[][][] buildAnglesCube(double[][][] tr) {
        int npanels = tr[0][0].length;
        int noutlines = tr.length;
        int naxis = tr[0].length;
        double[][][] angles = new double[noutlines][npanels][naxis - 1];
        for (int panel = 0; panel < npanels; ++panel) {
            for (int outline = 0; outline < noutlines; ++outline) {
                for (int xpoint = 0; xpoint < naxis - 1; ++xpoint) {
                    double spin;
                    double x1 = xpoint;
                    double x2 = xpoint + 1;
                    double y1 = 0.0;
                    double y2 = 0.0;
                    y1 = tr[outline][xpoint][panel];
                    y2 = tr[outline][xpoint + 1][panel];
                    double gradient = (y2 - y1) / (x2 - x1);
                    double atan = Math.atan(gradient);
                    angles[outline][panel][xpoint] = spin = MslEvaluator.angleTransformation(atan);
                }
            }
        }
        return angles;
    }

    private static double angleTransformation(double angle) {
        double r = angle;
        if (angle < 0.0) {
            r = angle + Math.PI * 2;
        }
        return r;
    }

    private List<Double> buildDifferencesTable(double[][][] angles) {
        ArrayList<Double> r = new ArrayList<Double>();
        for (int i = 0; i < angles.length; ++i) {
            for (int j = 0; j < angles[i].length; ++j) {
                double diff = 0.0;
                for (int column = j + 1; column < angles[i].length; ++column) {
                    diff = this.computeDifference(angles, i, j, i, column);
                    r.add(new Double(diff));
                }
                for (int row = i + 1; row < angles.length; ++row) {
                    diff = this.computeDifference(angles, i, j, row, j);
                    r.add(new Double(diff));
                }
            }
        }
        return r;
    }

    private double computeDifference(double[][][] angles, int aRow, int aColumn, int bRow, int bColumn) {
        double r = 0.0;
        double[] differences = new double[angles[0][0].length];
        for (int i = 0; i < angles[0][0].length; ++i) {
            double max = Math.max(angles[aRow][aColumn][i], angles[bRow][bColumn][i]);
            double min = Math.min(angles[aRow][aColumn][i], angles[bRow][bColumn][i]);
            differences[i] = max - min;
        }
        r = StatUtils.mean(differences);
        return r;
    }
}

