/*
 * Decompiled with CFR 0.152.
 */
package input.datasets;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.InTextFile;
import utils.TextUtilities;

public class Common
implements Serializable {
    private static final long serialVersionUID = -6939460461628804453L;
    private static final Logger LOG = LoggerFactory.getLogger(Common.class);
    private String datasetID;
    private String datasetName;
    private char datasetType;
    private int geneSize;
    private int sampleSize;
    private int timeSize;
    private int defMinG;
    private int defMaxG;
    private int defMinC;
    private int defMaxC;
    private int defMinT;
    private int defMaxT;
    private double[][][] dataset;

    public Common(String datasetID, String datasetName, String datasetType, String geneSize, String sampleSize, String timeSize, String[] paths, String sep, String defMinG, String defMaxG, String defMinC, String defMaxC, String defMinT, String defMaxT) {
        this.datasetID = datasetID;
        this.datasetName = datasetName;
        this.datasetType = datasetType.charAt(0);
        this.geneSize = Integer.parseInt(geneSize);
        this.sampleSize = Integer.parseInt(sampleSize);
        this.timeSize = Integer.parseInt(timeSize);
        this.dataset = this.buindDatasetFromFile(paths, sep);
        this.defMinG = Integer.parseInt(defMinG);
        this.defMaxG = Integer.parseInt(defMaxG);
        this.defMinC = Integer.parseInt(defMinC);
        this.defMaxC = Integer.parseInt(defMaxC);
        this.defMinT = Integer.parseInt(defMinT);
        this.defMaxT = Integer.parseInt(defMaxT);
    }

    public String getDatasetID() {
        return this.datasetID;
    }

    public String getDatasetName() {
        return this.datasetName;
    }

    public char getDatasetType() {
        return this.datasetType;
    }

    public int getGeneSize() {
        return this.geneSize;
    }

    public int getSampleSize() {
        return this.sampleSize;
    }

    public int getTimeSize() {
        return this.timeSize;
    }

    public double[][][] getDataset() {
        return this.dataset;
    }

    public int getDefMinG() {
        return this.defMinG;
    }

    public int getDefMaxG() {
        return this.defMaxG;
    }

    public int getDefMinC() {
        return this.defMinC;
    }

    public int getDefMaxC() {
        return this.defMaxC;
    }

    public int getDefMinT() {
        return this.defMinT;
    }

    public int getDefMaxT() {
        return this.defMaxT;
    }

    public String toString() {
        String r = "";
        r = "\nID = " + this.datasetID + "\nName = " + this.datasetName + "\nType = " + this.datasetType + "\nGenes = " + this.geneSize + "\nSamples = " + this.sampleSize + "\nTimes = " + this.timeSize + "\ndefMinG =" + this.defMinG + "\ndefMaxG =" + this.defMaxG + "\ndefMinC =" + this.defMinC + "\ndefMaxC =" + this.defMaxC + "\ndefMinT =" + this.defMinT + "\ndefMaxT =" + this.defMaxT + "\nel(0,0,0) =" + this.dataset[0][0][0] + "\nel(0,S,0) =" + this.dataset[0][this.sampleSize - 1][0] + "\nel(G,0,0) =" + this.dataset[this.geneSize - 1][0][0] + "\nel(G,S,0) =" + this.dataset[this.geneSize - 1][this.sampleSize - 1][0] + "\nel(0,0,T) =" + this.dataset[0][0][this.timeSize - 1] + "\nel(0,S,T) =" + this.dataset[0][this.sampleSize - 1][this.timeSize - 1] + "\nel(G,0,T) =" + this.dataset[this.geneSize - 1][0][this.timeSize - 1] + "\nel(G,S,T) =" + this.dataset[this.geneSize - 1][this.sampleSize - 1][this.timeSize - 1];
        return r;
    }

    private double[][][] buindDatasetFromFile(String[] paths, String sep) {
        double[][][] r = new double[this.geneSize][this.sampleSize][this.timeSize];
        for (int t = 0; t < this.timeSize; ++t) {
            String path = paths[t];
            try {
                InTextFile f = new InTextFile(path);
                int cont = 1;
                int g = 0;
                for (String it : f) {
                    List<Double> v = TextUtilities.stringToDouble(it, sep, cont);
                    this.insertValues(v, g, t, r);
                    ++g;
                    ++cont;
                }
                f.close();
                continue;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return r;
    }

    private void insertValues(List<Double> v, int g, int t, double[][][] r) {
        int s = 0;
        for (Double it : v) {
            r[g][s][t] = it;
            ++s;
        }
    }
}

