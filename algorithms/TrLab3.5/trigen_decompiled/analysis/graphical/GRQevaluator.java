/*
 * Decompiled with CFR 0.152.
 */
package analysis.graphical;

import general.Tricluster;

public interface GRQevaluator {
    public void loadParameters(double[][][] var1);

    public double computeFitness(Tricluster var1);

    public double getHighestValue();
}

