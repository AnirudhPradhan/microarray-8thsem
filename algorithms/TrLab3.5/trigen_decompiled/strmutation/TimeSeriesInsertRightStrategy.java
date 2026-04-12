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

public class TimeSeriesInsertRightStrategy
implements MutationStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(TimeSeriesInsertRightStrategy.class);

    @Override
    public boolean alter(AlgorithmIndividual individual) {
        int abialableTimes;
        AlgorithmConfiguration config = AlgorithmConfiguration.getInstance();
        AlgorithmRandomUtilities randSupport = AlgorithmRandomUtilities.getInstance();
        boolean res = false;
        int maxSize = config.getMaxT() - individual.getTimeSize();
        int rightTime = individual.getTime(individual.getTimeSize() - 1);
        if (maxSize > 0 && (abialableTimes = config.getData().getTimeSize() - rightTime - 1) > 0) {
            int border = randSupport.getFromInterval(1, Math.min(maxSize, abialableTimes));
            for (int i = 0; i < border; ++i) {
                individual.putTime(rightTime + (i + 1));
            }
            res = true;
        }
        return res;
    }
}

