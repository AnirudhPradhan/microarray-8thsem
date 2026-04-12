/*
 * Decompiled with CFR 0.152.
 */
package algcore;

import algcore.AlgorithmIndividual;
import java.util.List;

public interface SolutionCriterion {
    public AlgorithmIndividual chooseTheBest(List<AlgorithmIndividual> var1);
}

