/*
 * Decompiled with CFR 0.152.
 */
package strcrossover;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmIndividual;
import algcore.TriGen;
import algutils.AlgorithmRandomUtilities;
import algutils.TriclusterUtilities;
import crossovers.CrossoverStrategy;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GridStrategy
implements CrossoverStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(GridStrategy.class);

    @Override
    public AlgorithmIndividual[] cross(AlgorithmIndividual father, AlgorithmIndividual mother, String individualClassName) {
        AlgorithmIndividual[] r = new AlgorithmIndividual[2];
        father.addEntry("father [" + (TriGen.getInstance().getOngoingGenerationIndex() + 1) + "]");
        mother.addEntry("mother [" + (TriGen.getInstance().getOngoingGenerationIndex() + 1) + "]");
        LinkedList<Integer> child1_X = new LinkedList<Integer>();
        LinkedList<Integer> child2_X = new LinkedList<Integer>();
        LinkedList<Integer> child1_Y = new LinkedList<Integer>();
        LinkedList<Integer> child2_Y = new LinkedList<Integer>();
        Collection<Integer>[] child_times = TriclusterUtilities.getInstance().buildOnePointComponents(father.getTimes(), mother.getTimes(), AlgorithmConfiguration.getInstance().getMinT(), AlgorithmConfiguration.getInstance().getMaxT(), AlgorithmConfiguration.getInstance().getData().getTimeSize());
        Collection<Integer> child1_T = child_times[0];
        Collection<Integer> child2_T = child_times[1];
        Collection<Integer> fX = father.getSamples();
        HashSet<Integer> fsX = new HashSet<Integer>(fX);
        Collection<Integer> mX = mother.getSamples();
        HashSet<Integer> msX = new HashSet<Integer>(mX);
        HashSet<Integer> intersectionX = new HashSet<Integer>(fsX);
        intersectionX.retainAll(msX);
        Collection<Integer> fY = father.getGenes();
        HashSet<Integer> fsY = new HashSet<Integer>(fY);
        Collection<Integer> mY = mother.getGenes();
        HashSet<Integer> msY = new HashSet<Integer>(mY);
        HashSet<Integer> intersectionY = new HashSet<Integer>(fsY);
        intersectionY.retainAll(msY);
        if (intersectionX.isEmpty() && intersectionY.isEmpty()) {
            Set<Integer> auxBag = AlgorithmConfiguration.getInstance().getData().getSamplesBag();
            auxBag.removeAll(father.getSamples());
            auxBag.removeAll(mother.getSamples());
            int c1_center_X = 0;
            int c2_center_X = 0;
            if (auxBag.size() > 1) {
                AlgorithmRandomUtilities.getInstance().newBag();
                AlgorithmRandomUtilities.getInstance().putMarbles(auxBag);
                c1_center_X = AlgorithmRandomUtilities.getInstance().extractAmarble();
                c2_center_X = AlgorithmRandomUtilities.getInstance().extractAmarble();
            } else {
                c1_center_X = AlgorithmRandomUtilities.getInstance().getFromInterval(0, AlgorithmConfiguration.getInstance().getData().getSampleSize() - 1);
                c2_center_X = AlgorithmRandomUtilities.getInstance().getFromInterval(0, AlgorithmConfiguration.getInstance().getData().getSampleSize() - 1);
            }
            auxBag = AlgorithmConfiguration.getInstance().getData().getGenesBag();
            auxBag.removeAll(father.getGenes());
            auxBag.removeAll(mother.getGenes());
            int c1_center_Y = 0;
            int c2_center_Y = 0;
            if (auxBag.size() > 1) {
                AlgorithmRandomUtilities.getInstance().newBag();
                AlgorithmRandomUtilities.getInstance().putMarbles(auxBag);
                c1_center_Y = AlgorithmRandomUtilities.getInstance().extractAmarble();
                c2_center_Y = AlgorithmRandomUtilities.getInstance().extractAmarble();
            } else {
                c1_center_Y = AlgorithmRandomUtilities.getInstance().getFromInterval(0, AlgorithmConfiguration.getInstance().getData().getGenSize() - 1);
                c2_center_Y = AlgorithmRandomUtilities.getInstance().getFromInterval(0, AlgorithmConfiguration.getInstance().getData().getGenSize() - 1);
            }
            this.buid_coordinates(child1_X, child1_Y, child2_X, child2_Y, c1_center_X, c1_center_Y, c2_center_X, c2_center_Y);
        } else if (intersectionX.isEmpty() && !intersectionY.isEmpty()) {
            int c1_center_Y;
            int c2_center_Y = c1_center_Y = ((Integer)intersectionY.toArray()[0]).intValue();
            if (intersectionY.size() > 1) {
                AlgorithmRandomUtilities.getInstance().newBag();
                AlgorithmRandomUtilities.getInstance().putMarbles((Set<Integer>)intersectionY);
                c1_center_Y = AlgorithmRandomUtilities.getInstance().extractAmarble();
                c2_center_Y = AlgorithmRandomUtilities.getInstance().extractAmarble();
            }
            Set<Integer> auxBag = AlgorithmConfiguration.getInstance().getData().getSamplesBag();
            auxBag.removeAll(father.getSamples());
            auxBag.removeAll(mother.getSamples());
            int c1_center_X = 0;
            int c2_center_X = 0;
            if (auxBag.size() > 1) {
                AlgorithmRandomUtilities.getInstance().newBag();
                AlgorithmRandomUtilities.getInstance().putMarbles(auxBag);
                c1_center_X = AlgorithmRandomUtilities.getInstance().extractAmarble();
                c2_center_X = AlgorithmRandomUtilities.getInstance().extractAmarble();
            } else {
                c1_center_X = AlgorithmRandomUtilities.getInstance().getFromInterval(0, AlgorithmConfiguration.getInstance().getData().getSampleSize() - 1);
                c2_center_X = AlgorithmRandomUtilities.getInstance().getFromInterval(0, AlgorithmConfiguration.getInstance().getData().getSampleSize() - 1);
            }
            this.buid_coordinates(child1_X, child1_Y, child2_X, child2_Y, c1_center_X, c1_center_Y, c2_center_X, c2_center_Y);
        } else if (!intersectionX.isEmpty() && intersectionY.isEmpty()) {
            int c1_center_X;
            int c2_center_X = c1_center_X = ((Integer)intersectionX.toArray()[0]).intValue();
            if (intersectionX.size() > 1) {
                AlgorithmRandomUtilities.getInstance().newBag();
                AlgorithmRandomUtilities.getInstance().putMarbles((Set<Integer>)intersectionX);
                c1_center_X = AlgorithmRandomUtilities.getInstance().extractAmarble();
                c2_center_X = AlgorithmRandomUtilities.getInstance().extractAmarble();
            }
            Set<Integer> auxBag = AlgorithmConfiguration.getInstance().getData().getGenesBag();
            auxBag.removeAll(father.getGenes());
            auxBag.removeAll(mother.getGenes());
            int c1_center_Y = 0;
            int c2_center_Y = 0;
            if (auxBag.size() > 1) {
                AlgorithmRandomUtilities.getInstance().newBag();
                AlgorithmRandomUtilities.getInstance().putMarbles(auxBag);
                c1_center_Y = AlgorithmRandomUtilities.getInstance().extractAmarble();
                c2_center_Y = AlgorithmRandomUtilities.getInstance().extractAmarble();
            } else {
                c1_center_Y = AlgorithmRandomUtilities.getInstance().getFromInterval(0, AlgorithmConfiguration.getInstance().getData().getGenSize() - 1);
                c2_center_Y = AlgorithmRandomUtilities.getInstance().getFromInterval(0, AlgorithmConfiguration.getInstance().getData().getGenSize() - 1);
            }
            this.buid_coordinates(child1_X, child1_Y, child2_X, child2_Y, c1_center_X, c1_center_Y, c2_center_X, c2_center_Y);
        } else {
            int c1_center_Y;
            int c1_center_X;
            int c2_center_X = c1_center_X = ((Integer)intersectionX.toArray()[0]).intValue();
            if (intersectionX.size() > 1) {
                AlgorithmRandomUtilities.getInstance().newBag();
                AlgorithmRandomUtilities.getInstance().putMarbles((Set<Integer>)intersectionX);
                c1_center_X = AlgorithmRandomUtilities.getInstance().extractAmarble();
                c2_center_X = AlgorithmRandomUtilities.getInstance().extractAmarble();
            }
            int c2_center_Y = c1_center_Y = ((Integer)intersectionY.toArray()[0]).intValue();
            if (intersectionY.size() > 1) {
                AlgorithmRandomUtilities.getInstance().newBag();
                AlgorithmRandomUtilities.getInstance().putMarbles((Set<Integer>)intersectionY);
                c1_center_Y = AlgorithmRandomUtilities.getInstance().extractAmarble();
                c2_center_Y = AlgorithmRandomUtilities.getInstance().extractAmarble();
            }
            this.buid_coordinates(child1_X, child1_Y, child2_X, child2_Y, c1_center_X, c1_center_Y, c2_center_X, c2_center_Y);
        }
        r[0] = TriclusterUtilities.getInstance().buildIndividual(child1_Y, child1_X, child1_T, individualClassName, "from crossover [" + (TriGen.getInstance().getOngoingGenerationIndex() + 1) + "]");
        r[1] = TriclusterUtilities.getInstance().buildIndividual(child2_Y, child2_X, child2_T, individualClassName, "from crossover [" + (TriGen.getInstance().getOngoingGenerationIndex() + 1) + "]");
        return r;
    }

    private void buid_coordinates(Collection<Integer> child1_X, Collection<Integer> child1_Y, Collection<Integer> child2_X, Collection<Integer> child2_Y, int c1_center_X, int c1_center_Y, int c2_center_X, int c2_center_Y) {
        int x;
        int y;
        int c1_tamX = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinC(), AlgorithmConfiguration.getInstance().getMaxC());
        int c1_tamY = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinG(), AlgorithmConfiguration.getInstance().getMaxG());
        int[] child1_borders = TriclusterUtilities.getInstance().getTensor2DLimits(c1_center_Y, c1_center_X, c1_tamY, c1_tamX, AlgorithmConfiguration.getInstance().getData().getGenSize(), AlgorithmConfiguration.getInstance().getData().getSampleSize());
        int c2_tamX = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinC(), AlgorithmConfiguration.getInstance().getMaxC());
        int c2_tamY = AlgorithmRandomUtilities.getInstance().getFromInterval(AlgorithmConfiguration.getInstance().getMinG(), AlgorithmConfiguration.getInstance().getMaxG());
        int[] child2_borders = TriclusterUtilities.getInstance().getTensor2DLimits(c2_center_Y, c2_center_X, c2_tamY, c2_tamX, AlgorithmConfiguration.getInstance().getData().getGenSize(), AlgorithmConfiguration.getInstance().getData().getSampleSize());
        for (y = child1_borders[0]; y <= child1_borders[1]; ++y) {
            child1_Y.add(new Integer(y));
        }
        for (x = child1_borders[2]; x <= child1_borders[3]; ++x) {
            child1_X.add(new Integer(x));
        }
        for (y = child2_borders[0]; y <= child2_borders[1]; ++y) {
            child2_Y.add(new Integer(y));
        }
        for (x = child2_borders[2]; x <= child2_borders[3]; ++x) {
            child2_X.add(new Integer(x));
        }
    }
}

