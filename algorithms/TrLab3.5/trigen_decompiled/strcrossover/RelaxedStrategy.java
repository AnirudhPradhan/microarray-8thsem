/*
 * Decompiled with CFR 0.152.
 */
package strcrossover;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmIndividual;
import algcore.TriGen;
import algutils.TriclusterUtilities;
import crossovers.CrossoverStrategy;
import java.util.LinkedList;

public class RelaxedStrategy
implements CrossoverStrategy {
    @Override
    public AlgorithmIndividual[] cross(AlgorithmIndividual father, AlgorithmIndividual mother, String individualClassName) {
        AlgorithmIndividual[] r = new AlgorithmIndividual[1];
        father.addEntry("father [" + (TriGen.getInstance().getOngoingGenerationIndex() + 1) + "]");
        mother.addEntry("mother [" + (TriGen.getInstance().getOngoingGenerationIndex() + 1) + "]");
        int[] g = TriclusterUtilities.getInstance().getRandomChromosome(father.getGenes(), mother.getGenes(), AlgorithmConfiguration.getInstance().getMinG(), AlgorithmConfiguration.getInstance().getMaxG());
        int[] c2 = TriclusterUtilities.getInstance().getRandomChromosome(father.getSamples(), mother.getSamples(), AlgorithmConfiguration.getInstance().getMinC(), AlgorithmConfiguration.getInstance().getMaxC());
        int[] t = TriclusterUtilities.getInstance().getRandomChromosome(father.getTimes(), mother.getTimes(), AlgorithmConfiguration.getInstance().getMinC(), AlgorithmConfiguration.getInstance().getMaxC());
        r[0] = this.buildIndividual(g, c2, t, individualClassName);
        return r;
    }

    private AlgorithmIndividual buildIndividual(int[] g, int[] c2, int[] t, String individualClassName) {
        AlgorithmIndividual individuo = null;
        try {
            individuo = (AlgorithmIndividual)Class.forName(individualClassName).newInstance();
            LinkedList<Integer> genes = new LinkedList<Integer>();
            for (int i = 0; i < g.length; ++i) {
                genes.add(new Integer(g[i]));
            }
            LinkedList<Integer> condiciones = new LinkedList<Integer>();
            for (int i = 0; i < c2.length; ++i) {
                condiciones.add(new Integer(c2[i]));
            }
            LinkedList<Integer> tiempos = new LinkedList<Integer>();
            for (int i = 0; i < t.length; ++i) {
                tiempos.add(new Integer(t[i]));
            }
            individuo.initialize(genes, condiciones, tiempos);
            individuo.addEntry("from crossover [" + (TriGen.getInstance().getOngoingGenerationIndex() + 1) + "]");
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return individuo;
    }
}

