/*
 * Decompiled with CFR 0.152.
 */
package strinitialpop;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmIndividual;
import algutils.AlgorithmRandomUtilities;
import algutils.TriclusterUtilities;
import initialpops.InitialPopStrategy;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Tensor2DStrategy
implements InitialPopStrategy {
    @Override
    public List<AlgorithmIndividual> generateIndividuals(int numberOfIndividuals, String individualClassName) {
        LinkedList<AlgorithmIndividual> l = new LinkedList<AlgorithmIndividual>();
        for (int i = 0; i < numberOfIndividuals; ++i) {
            int coordenadaGen = AlgorithmRandomUtilities.getInstance().getFromInterval(0, AlgorithmConfiguration.getInstance().getData().getGenSize() - 1);
            int coordenadaCondicion = AlgorithmRandomUtilities.getInstance().getFromInterval(0, AlgorithmConfiguration.getInstance().getData().getSampleSize() - 1);
            int tamG = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinG(), AlgorithmConfiguration.getInstance().getMaxG());
            int tamC = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinC(), AlgorithmConfiguration.getInstance().getMaxC());
            int[] radios = TriclusterUtilities.getInstance().getTensor2DLimits(coordenadaGen, coordenadaCondicion, tamG, tamC, AlgorithmConfiguration.getInstance().getData().getGenSize(), AlgorithmConfiguration.getInstance().getData().getSampleSize());
            LinkedList<Integer> genes = new LinkedList<Integer>();
            for (int g = radios[0]; g <= radios[1]; ++g) {
                genes.add(new Integer(g));
            }
            LinkedList<Integer> condiciones = new LinkedList<Integer>();
            for (int c2 = radios[2]; c2 <= radios[3]; ++c2) {
                condiciones.add(new Integer(c2));
            }
            int nTime = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinT(), AlgorithmConfiguration.getInstance().getMaxT());
            Collection<Integer> tiempos = TriclusterUtilities.getInstance().getDispersedRandomComponent(nTime, AlgorithmConfiguration.getInstance().getData().getTimesBag());
            AlgorithmIndividual newIndividual = TriclusterUtilities.getInstance().buildIndividual(genes, condiciones, tiempos, individualClassName, "from initial population: tensor 2D");
            l.add(newIndividual);
        }
        return l;
    }
}

