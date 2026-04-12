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

public class TimeSeriesInsertLeftStrategy
implements MutationStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(TimeSeriesInsertLeftStrategy.class);

    @Override
    public boolean alter(AlgorithmIndividual individual) {
        int abialableTimes;
        AlgorithmConfiguration config = AlgorithmConfiguration.getInstance();
        AlgorithmRandomUtilities randSupport = AlgorithmRandomUtilities.getInstance();
        boolean res = false;
        int maxSize = config.getMaxT() - individual.getTimeSize();
        if (maxSize > 0 && (abialableTimes = individual.getTime(0)) > 0) {
            int border = randSupport.getFromInterval(1, Math.min(maxSize, abialableTimes));
            for (int i = 0; i < border; ++i) {
                individual.putTime(abialableTimes - (i + 1));
            }
            res = true;
        }
        return res;
    }
}

