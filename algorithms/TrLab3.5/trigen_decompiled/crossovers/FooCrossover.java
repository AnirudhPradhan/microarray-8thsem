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
import strcrossover.FooCrossOverStrategy;

public class FooCrossover
implements Crossover {
    private CrossoverStrategy foo = new FooCrossOverStrategy();

    @Override
    public List<AlgorithmIndividual> cross(int numberOfChildren, List<AlgorithmIndividual> selectedPopulation) {
        LinkedList<AlgorithmIndividual> r = new LinkedList<AlgorithmIndividual>();
        TriGen trigen = TriGen.getInstance();
        for (int i = 0; i < numberOfChildren; ++i) {
            AlgorithmIndividual[] padres = this.getReproductivePair(selectedPopulation);
            AlgorithmIndividual padre = padres[0];
            AlgorithmIndividual madre = padres[1];
            AlgorithmIndividual[] hijos = this.foo.cross(padre, madre, trigen.getIndividualClassName());
            r.add(hijos[0]);
        }
        return r;
    }

    private AlgorithmIndividual[] getReproductivePair(List<AlgorithmIndividual> population) {
        AlgorithmRandomUtilities ALEATORIOS = AlgorithmRandomUtilities.getInstance();
        ALEATORIOS.newBag(population.size());
        AlgorithmIndividual[] r = new AlgorithmIndividual[2];
        int indPadre = ALEATORIOS.extractAmarble();
        int indMadre = ALEATORIOS.extractAmarble();
        r[0] = population.get(indPadre);
        r[1] = population.get(indMadre);
        return r;
    }
}

