/*
 * Decompiled with CFR 0.152.
 */
package strmutation;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmDataset;
import algcore.AlgorithmIndividual;
import algutils.AlgorithmRandomUtilities;
import mutations.MutationStrategy;

public class TimeChangeStrategy
implements MutationStrategy {
    @Override
    public boolean alter(AlgorithmIndividual individual) {
        AlgorithmConfiguration PARAM = AlgorithmConfiguration.getInstance();
        AlgorithmRandomUtilities ALEATORIOS = AlgorithmRandomUtilities.getInstance();
        AlgorithmDataset DATOS = PARAM.getData();
        boolean res = true;
        if (individual.getTimeSize() > PARAM.getMinT()) {
            ALEATORIOS.newBag();
            ALEATORIOS.putMarbles(individual.getTimes());
            int bola = ALEATORIOS.extractAmarble();
            individual.deleteTime(bola);
            if (individual.getTimeSize() < PARAM.getMaxT()) {
                ALEATORIOS.newBag();
                ALEATORIOS.putMarbles(DATOS.getTimesBag(), individual.getTimes());
                bola = ALEATORIOS.extractAmarble();
                individual.putTime(bola);
            } else {
                res = false;
            }
        } else {
            res = false;
        }
        return res;
    }
}

