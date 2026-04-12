/*
 * Decompiled with CFR 0.152.
 */
package strmutation;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmIndividual;
import algutils.AlgorithmRandomUtilities;
import mutations.MutationStrategy;

public class ConditionRemovalStrategy
implements MutationStrategy {
    @Override
    public boolean alter(AlgorithmIndividual individual) {
        AlgorithmConfiguration config = AlgorithmConfiguration.getInstance();
        AlgorithmRandomUtilities randomSupport = AlgorithmRandomUtilities.getInstance();
        boolean res = true;
        if (individual.getSampleSize() > config.getMinC()) {
            randomSupport.newBag();
            randomSupport.putMarbles(individual.getSamples());
            int bola = randomSupport.extractAmarble();
            individual.deleteSample(bola);
        } else {
            res = false;
        }
        return res;
    }
}

