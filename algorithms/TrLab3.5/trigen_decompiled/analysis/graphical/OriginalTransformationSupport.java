/*
 * Decompiled with CFR 0.152.
 */
package analysis.graphical;

import analysis.graphical.Transformation;
import general.Tricluster;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.TextUtilities;

public class OriginalTransformationSupport
implements Transformation {
    private static final Logger LOG = LoggerFactory.getLogger(OriginalTransformationSupport.class);
    private static final String C1 = "g-c-t";
    private static final String C2 = "g-t-c";
    private static final String C3 = "c-g-t";
    private static final String C4 = "c-t-g";
    private static final String C5 = "t-g-c";
    private static final String C6 = "t-c-g";
    private double[][][] dataset;

    public OriginalTransformationSupport(double[][][] dataset) {
        this.dataset = dataset;
    }

    @Override
    public List<double[][][]> transform(Tricluster tricluster) {
        ArrayList<double[][][]> r = new ArrayList<double[][][]>(6);
        double[][][] t1 = this.getCube(tricluster, C1);
        double[][][] t2 = this.getCube(tricluster, C2);
        double[][][] t3 = this.getCube(tricluster, C3);
        double[][][] t4 = this.getCube(tricluster, C4);
        double[][][] t5 = this.getCube(tricluster, C5);
        double[][][] t6 = this.getCube(tricluster, C6);
        r.add(t1);
        r.add(t2);
        r.add(t3);
        r.add(t4);
        r.add(t5);
        r.add(t6);
        return r;
    }

    private double[][][] getCube(Tricluster tricluster, String configuration) {
        double[][][] r = null;
        List<Integer>[] cfg = this.getXaxisOutputPanel(tricluster, configuration);
        List<Integer> xaxis = cfg[0];
        List<Integer> output = cfg[1];
        List<Integer> panel = cfg[2];
        r = new double[output.size()][xaxis.size()][panel.size()];
        int i = 0;
        int j = 0;
        int k = 0;
        for (Integer pan : panel) {
            i = 0;
            for (Integer out : output) {
                j = 0;
                for (Integer x : xaxis) {
                    r[i][j][k] = this.getValue(x, out, pan, configuration);
                    ++j;
                }
                ++i;
            }
            ++k;
        }
        return r;
    }

    private double getValue(int x, int o, int p, String cnf) {
        double value = 0.0;
        List<String> config = TextUtilities.splitElements(cnf, "-");
        String xaxis = config.get(0);
        String output = config.get(1);
        String panel = config.get(2);
        int gi = 0;
        int ci = 0;
        int ti = 0;
        switch (xaxis) {
            case "g": {
                gi = x;
                break;
            }
            case "c": 
            case "s": {
                ci = x;
                break;
            }
            case "t": {
                ti = x;
            }
        }
        switch (output) {
            case "g": {
                gi = o;
                break;
            }
            case "c": 
            case "s": {
                ci = o;
                break;
            }
            case "t": {
                ti = o;
            }
        }
        switch (panel) {
            case "g": {
                gi = p;
                break;
            }
            case "c": 
            case "s": {
                ci = p;
                break;
            }
            case "t": {
                ti = p;
            }
        }
        value = this.dataset[gi][ci][ti];
        return value;
    }

    private List<Integer>[] getXaxisOutputPanel(Tricluster tricluster, String configuration) {
        List[] r = new List[3];
        List<String> config = TextUtilities.splitElements(configuration, "-");
        String xaxis = config.get(0);
        String output = config.get(1);
        String panel = config.get(2);
        switch (xaxis) {
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
        switch (output) {
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
        switch (panel) {
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

