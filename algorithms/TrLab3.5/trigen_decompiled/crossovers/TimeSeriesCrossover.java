/*
 * Decompiled with CFR 0.152.
 */
package crossovers;

import algcore.AlgorithmIndividual;
import algcore.Crossover;
import algcore.TriGen;
import algutils.TriclusterUtilities;
import crossovers.CrossoverStrategy;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import strcrossover.TimeSeriesStrategy;

public class TimeSeriesCrossover
implements Crossover {
    private static final Logger LOG = LoggerFactory.getLogger(TimeSeriesCrossover.class);
    private CrossoverStrategy timeSeries = new TimeSeriesStrategy();

    @Override
    public List<AlgorithmIndividual> cross(int numberOfChildren, List<AlgorithmIndividual> selectedPopulation) {
        TriGen TRIGEN = TriGen.getInstance();
        LinkedList<AlgorithmIndividual> r = new LinkedList<AlgorithmIndividual>();
        int[] remainderCrossings = this.computeRemainderCrossings(numberOfChildren);
        for (int i = 0; i < remainderCrossings[0]; ++i) {
            AlgorithmIndividual[] parents = TriclusterUtilities.getInstance().getReproductivePair(selectedPopulation);
            AlgorithmIndividual father = parents[0];
            AlgorithmIndividual mother = parents[1];
            AlgorithmIndividual[] children = this.timeSeries.cross(father, mother, TRIGEN.getIndividualClassName());
            r.add(children[0]);
            r.add(children[1]);
        }
        if (remainderCrossings[1] == 1) {
            AlgorithmIndividual[] parents = TriclusterUtilities.getInstance().getReproductivePair(selectedPopulation);
            AlgorithmIndividual father = parents[0];
            AlgorithmIndividual mother = parents[1];
            AlgorithmIndividual[] children = this.timeSeries.cross(father, mother, TRIGEN.getIndividualClassName());
            r.add(children[0]);
        }
        return r;
    }

    private int[] computeRemainderCrossings(int nChildren) {
        int[] r = new int[2];
        int nCrossings = nChildren / 2;
        int remainder = nChildren % 2;
        r[0] = nCrossings;
        r[1] = remainder;
        return r;
    }
}

