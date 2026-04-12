/*
 * Decompiled with CFR 0.152.
 */
package crossovers;

import algcore.AlgorithmIndividual;

public interface CrossoverStrategy {
    public AlgorithmIndividual[] cross(AlgorithmIndividual var1, AlgorithmIndividual var2, String var3);
}

