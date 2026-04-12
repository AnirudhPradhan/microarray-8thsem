/*
 * Decompiled with CFR 0.152.
 */
package algcore;

import algcore.AlgorithmIndividual;
import algcore.FitnessFunction;
import java.util.concurrent.Callable;

public class ConcurrentEvaluator
implements Callable<Boolean> {
    private FitnessFunction fitnessFunction;
    private AlgorithmIndividual individual;
    private int individualIndex;

    public ConcurrentEvaluator(FitnessFunction fitnessFunction, AlgorithmIndividual individual, int individualIndex) {
        this.fitnessFunction = fitnessFunction;
        this.individual = individual;
        this.individualIndex = individualIndex;
    }

    @Override
    public Boolean call() throws Exception {
        this.fitnessFunction.evaluate(this.individual);
        return new Boolean(true);
    }
}

