/*
 * Decompiled with CFR 0.152.
 */
package algcore;

import java.util.Set;

public interface AlgorithmDataset {
    public int getGenSize();

    public int getSampleSize();

    public int getTimeSize();

    public double getValue(int var1, int var2, int var3);

    public Set<Integer> getGenesBag();

    public Set<Integer> getSamplesBag();

    public Set<Integer> getTimesBag();
}

