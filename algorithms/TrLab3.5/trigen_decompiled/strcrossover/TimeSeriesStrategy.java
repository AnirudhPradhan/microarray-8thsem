/*
 * Decompiled with CFR 0.152.
 */
package strcrossover;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmIndividual;
import algcore.TriGen;
import algutils.TriclusterUtilities;
import crossovers.CrossoverStrategy;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeSeriesStrategy
implements CrossoverStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(TimeSeriesStrategy.class);

    @Override
    public AlgorithmIndividual[] cross(AlgorithmIndividual father, AlgorithmIndividual mother, String individualClassName) {
        AlgorithmConfiguration config = AlgorithmConfiguration.getInstance();
        AlgorithmIndividual[] r = new AlgorithmIndividual[2];
        father.addEntry("father [" + (TriGen.getInstance().getOngoingGenerationIndex() + 1) + "]");
        mother.addEntry("mother [" + (TriGen.getInstance().getOngoingGenerationIndex() + 1) + "]");
        Collection<Integer>[] childGenes = TriclusterUtilities.getInstance().buildOnePointComponents(father.getGenes(), mother.getGenes(), config.getMinG(), config.getMaxG(), config.getData().getGenSize());
        Collection<Integer>[] childConditions = TriclusterUtilities.getInstance().buildOnePointComponents(father.getSamples(), mother.getSamples(), config.getMinC(), config.getMaxC(), config.getData().getSampleSize());
        Collection<Integer>[] childTimes = TriclusterUtilities.getInstance().buildTimeSeriesComponents(father.getTimes(), mother.getTimes(), config.getMinT(), config.getMaxT());
        r[0] = TriclusterUtilities.getInstance().buildIndividual(childGenes[0], childConditions[0], childTimes[0], individualClassName, "from crossover [" + (TriGen.getInstance().getOngoingGenerationIndex() + 1) + "]");
        r[1] = TriclusterUtilities.getInstance().buildIndividual(childGenes[1], childConditions[1], childTimes[1], individualClassName, "from crossover [" + (TriGen.getInstance().getOngoingGenerationIndex() + 1) + "]");
        return r;
    }
}

