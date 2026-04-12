/*
 * Decompiled with CFR 0.152.
 */
package strcrossover;

import algcore.AlgorithmIndividual;
import algutils.TriclusterUtilities;
import crossovers.CrossoverStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FooCrossOverStrategy
implements CrossoverStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(FooCrossOverStrategy.class);

    @Override
    public AlgorithmIndividual[] cross(AlgorithmIndividual father, AlgorithmIndividual mother, String individualClassName) {
        AlgorithmIndividual son1;
        AlgorithmIndividual[] r = new AlgorithmIndividual[2];
        r[0] = son1 = TriclusterUtilities.getInstance().buildIndividual(father.getGenes(), father.getSamples(), father.getTimes(), individualClassName, "from crossover: foo");
        return r;
    }
}

