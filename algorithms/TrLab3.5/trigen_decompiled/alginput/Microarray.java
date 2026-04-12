/*
 * Decompiled with CFR 0.152.
 */
package alginput;

import algcore.AlgorithmDataset;
import java.util.HashSet;
import java.util.Set;

public class Microarray
implements AlgorithmDataset {
    private double[][][] rawData;

    public Microarray(double[][][] rawData) {
        this.rawData = rawData;
    }

    @Override
    public int getGenSize() {
        return this.rawData.length;
    }

    @Override
    public int getSampleSize() {
        return this.rawData[0].length;
    }

    @Override
    public int getTimeSize() {
        return this.rawData[0][0].length;
    }

    @Override
    public double getValue(int g, int c2, int t) {
        return this.rawData[g][c2][t];
    }

    public String toString() {
        String aux4 = "";
        for (int g = 0; g < this.getGenSize(); ++g) {
            for (int t = 0; t < this.getTimeSize(); ++t) {
                for (int c2 = 0; c2 < this.getSampleSize(); ++c2) {
                    boolean cond1 = c2 == this.getSampleSize() - 1;
                    boolean cond2 = t == this.getTimeSize() - 1;
                    String cad = "" + this.rawData[g][c2][t];
                    cad = !cond1 && !cond2 ? cad + " , " : (cond1 && cond2 ? cad + "\n" : (cond1 && !cond2 ? cad + " || " : cad + " , "));
                    aux4 = aux4 + cad;
                }
            }
        }
        return aux4;
    }

    @Override
    public Set<Integer> getGenesBag() {
        HashSet<Integer> res = new HashSet<Integer>();
        for (int i = 0; i < this.getGenSize(); ++i) {
            res.add(new Integer(i));
        }
        return res;
    }

    @Override
    public Set<Integer> getSamplesBag() {
        HashSet<Integer> res = new HashSet<Integer>();
        for (int i = 0; i < this.getSampleSize(); ++i) {
            res.add(new Integer(i));
        }
        return res;
    }

    @Override
    public Set<Integer> getTimesBag() {
        HashSet<Integer> res = new HashSet<Integer>();
        for (int i = 0; i < this.getTimeSize(); ++i) {
            res.add(new Integer(i));
        }
        return res;
    }
}

