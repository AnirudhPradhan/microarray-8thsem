/*
 * Decompiled with CFR 0.152.
 */
package strmutation;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmIndividual;
import algutils.AlgorithmRandomUtilities;
import mutations.MutationStrategy;

public class TimeRemovalStrategy
implements MutationStrategy {
    @Override
    public boolean alter(AlgorithmIndividual individual) {
        AlgorithmConfiguration PARAM = AlgorithmConfiguration.getInstance();
        AlgorithmRandomUtilities ALEATORIOS = AlgorithmRandomUtilities.getInstance();
        boolean res = true;
        if (individual.getTimeSize() > PARAM.getMinT()) {
            ALEATORIOS.newBag();
            ALEATORIOS.putMarbles(individual.getTimes());
            int bola = ALEATORIOS.extractAmarble();
            individual.deleteTime(bola);
        } else {
            res = false;
        }
        return res;
    }
}

