/*
 * Decompiled with CFR 0.152.
 */
package algcore;

import algcore.AlgorithmIndividual;
import java.util.List;

public interface Crossover {
    public List<AlgorithmIndividual> cross(int var1, List<AlgorithmIndividual> var2);
}

