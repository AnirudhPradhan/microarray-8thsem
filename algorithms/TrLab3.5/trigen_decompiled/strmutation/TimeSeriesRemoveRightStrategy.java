/*
 * Decompiled with CFR 0.152.
 */
package strmutation;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmIndividual;
import algutils.AlgorithmRandomUtilities;
import mutations.MutationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeSeriesRemoveRightStrategy
implements MutationStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(TimeSeriesRemoveRightStrategy.class);

    @Override
    public boolean alter(AlgorithmIndividual individual) {
        AlgorithmConfiguration config = AlgorithmConfiguration.getInstance();
        AlgorithmRandomUtilities randSupport = AlgorithmRandomUtilities.getInstance();
        boolean res = false;
        int minSize = individual.getTimeSize() - config.getMinT();
        if (minSize > 0) {
            int border = randSupport.getFromInterval(1, minSize);
            int rightTime = individual.getTime(individual.getTimeSize() - 1);
            for (int i = 0; i < border; ++i) {
                individual.deleteTime(rightTime - i);
            }
            res = true;
        }
        return res;
    }
}

