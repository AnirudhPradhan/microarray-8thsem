/*
 * Decompiled with CFR 0.152.
 */
package crossovers;

import algcore.AlgorithmIndividual;
import algcore.Crossover;
import algcore.TriGen;
import algutils.AlgorithmRandomUtilities;
import crossovers.CrossoverStrategy;
import java.util.LinkedList;
import java.util.List;
import strcrossover.OnePointStrategy;

public class OnePointCrossover
implements Crossover {
    private CrossoverStrategy onePoint = new OnePointStrategy();

    @Override
    public List<AlgorithmIndividual> cross(int numberOfChildren, List<AlgorithmIndividual> selectedPopulation) {
        TriGen TRIGEN = TriGen.getInstance();
        LinkedList<AlgorithmIndividual> r = new LinkedList<AlgorithmIndividual>();
        int[] remainderCrossings = this.computeRemainderCrossings(numberOfChildren);
        for (int i = 0; i < remainderCrossings[0]; ++i) {
            AlgorithmIndividual[] parents = this.getReproductivePair(selectedPopulation);
            AlgorithmIndividual father = parents[0];
            AlgorithmIndividual mother = parents[1];
            AlgorithmIndividual[] children = this.onePoint.cross(father, mother, TRIGEN.getIndividualClassName());
            r.add(children[0]);
            r.add(children[1]);
        }
        if (remainderCrossings[1] == 1) {
            AlgorithmIndividual[] parents = this.getReproductivePair(selectedPopulation);
            AlgorithmIndividual father = parents[0];
            AlgorithmIndividual mother = parents[1];
            AlgorithmIndividual[] children = this.onePoint.cross(father, mother, TRIGEN.getIndividualClassName());
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

    private AlgorithmIndividual[] getReproductivePair(List<AlgorithmIndividual> population) {
        AlgorithmRandomUtilities randomSupport = AlgorithmRandomUtilities.getInstance();
        randomSupport.newBag(population.size());
        AlgorithmIndividual[] r = new AlgorithmIndividual[2];
        int fatherIndex = randomSupport.extractAmarble();
        int motherIndex = randomSupport.extractAmarble();
        r[0] = population.get(fatherIndex);
        r[1] = population.get(motherIndex);
        return r;
    }
}

