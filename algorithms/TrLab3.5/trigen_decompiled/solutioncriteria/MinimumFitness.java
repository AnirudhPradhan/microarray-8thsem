/*
 * Decompiled with CFR 0.152.
 */
package solutioncriteria;

import algcore.AlgorithmIndividual;
import algcore.SolutionCriterion;
import java.util.Collections;
import java.util.List;

public class MinimumFitness
implements SolutionCriterion {
    @Override
    public AlgorithmIndividual chooseTheBest(List<AlgorithmIndividual> population) {
        AlgorithmIndividual mejor = Collections.min(population);
        return mejor;
    }
}

