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

public class TimeSeriesInsertBothStrategy
implements MutationStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(TimeSeriesInsertBothStrategy.class);

    @Override
    public boolean alter(AlgorithmIndividual individual) {
        AlgorithmConfiguration config = AlgorithmConfiguration.getInstance();
        AlgorithmRandomUtilities randSupport = AlgorithmRandomUtilities.getInstance();
        boolean res = false;
        int maxSize = config.getMaxT() - individual.getTimeSize();
        if (maxSize > 0) {
            int abialableLeft = individual.getTime(0);
            int rightTime = individual.getTime(individual.getTimeSize() - 1);
            int abialableRight = config.getData().getTimeSize() - rightTime - 1;
            int abialableTimes = Math.min(abialableLeft, abialableRight);
            if (abialableTimes > 0) {
                int len = randSupport.getFromInterval(2, Math.min(maxSize, abialableTimes * 2));
                if (len % 2 != 0) {
                    --len;
                }
                for (int i = 0; i < len / 2; ++i) {
                    individual.putTime(abialableLeft - (i + 1));
                    individual.putTime(rightTime + (i + 1));
                }
                res = true;
            }
        }
        return res;
    }
}

