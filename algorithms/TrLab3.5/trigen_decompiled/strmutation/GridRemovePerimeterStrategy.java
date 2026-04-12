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

public class GridRemovePerimeterStrategy
implements MutationStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(GridRemovePerimeterStrategy.class);

    @Override
    public boolean alter(AlgorithmIndividual individual) {
        boolean res = false;
        int individualLeftX = individual.getSample(0);
        int individualRightX = individual.getSample(individual.getSampleSize() - 1);
        int individualTopY = individual.getGene(0);
        int individualDownY = individual.getGene(individual.getGeneSize() - 1);
        int xSize = individual.getSampleSize() - AlgorithmConfiguration.getInstance().getMinC();
        int ySize = individual.getGeneSize() - AlgorithmConfiguration.getInstance().getMinG();
        int leftBorderX = individualLeftX + xSize;
        int rightBorderX = individualRightX - xSize;
        int topBorderY = individualTopY + ySize;
        int downBorderY = individualDownY - ySize;
        boolean[] directions = new boolean[]{false, false, false, false};
        int maxRemovals = Integer.MAX_VALUE;
        if (xSize > 0) {
            if (leftBorderX > individualLeftX) {
                directions[0] = true;
                if (xSize < maxRemovals) {
                    maxRemovals = xSize;
                }
            }
            if (rightBorderX < individualRightX) {
                directions[1] = true;
                if (xSize < maxRemovals) {
                    maxRemovals = xSize;
                }
            }
        }
        if (ySize > 0) {
            if (topBorderY > individualTopY) {
                directions[2] = true;
                if (ySize < maxRemovals) {
                    maxRemovals = ySize;
                }
            }
            if (downBorderY < individualDownY) {
                directions[3] = true;
                if (ySize < maxRemovals) {
                    maxRemovals = ySize;
                }
            }
        }
        if (directions[0] || directions[1] || directions[2] || directions[3]) {
            int loops = 0;
            int chunks = 0;
            int removals = 0;
            if (xSize == ySize && directions[0] && directions[1] && directions[2] && directions[3]) {
                loops = xSize / 2;
                chunks = xSize % 2;
                if (loops == 0) {
                    removals = AlgorithmRandomUtilities.getInstance().getFromInterval(1, maxRemovals);
                } else {
                    int loopInsertion = AlgorithmRandomUtilities.getInstance().getFromInterval(1, loops);
                    int chunkInsertion = 0;
                    if (chunks != 0) {
                        chunkInsertion = AlgorithmRandomUtilities.getInstance().getFromInterval(1, chunks);
                    }
                    removals = loopInsertion * 4 + chunkInsertion;
                }
            } else {
                removals = AlgorithmRandomUtilities.getInstance().getFromInterval(1, maxRemovals);
            }
            if (removals > 0) {
                res = true;
            }
            while (removals > 0) {
                if (directions[0] && removals > 0) {
                    individual.deleteSample(individualLeftX);
                    ++individualLeftX;
                    --removals;
                }
                if (directions[1] && removals > 0) {
                    individual.deleteSample(individualRightX);
                    --individualRightX;
                    --removals;
                }
                if (directions[2] && removals > 0) {
                    individual.deleteGene(individualTopY);
                    ++individualTopY;
                    --removals;
                }
                if (!directions[3] || removals <= 0) continue;
                individual.deleteGene(individualDownY);
                --individualDownY;
                --removals;
            }
        }
        return res;
    }
}

