/*
 * Decompiled with CFR 0.152.
 */
package strmutation;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmIndividual;
import algutils.AlgorithmRandomUtilities;
import java.util.ArrayList;
import mutations.MutationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GridRemoveBorderStrategy
implements MutationStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(GridRemoveBorderStrategy.class);

    @Override
    public boolean alter(AlgorithmIndividual individual) {
        boolean res;
        block23: {
            res = false;
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
            if (!directions[0] && !directions[1] && !directions[2] && !directions[3]) break block23;
            AlgorithmRandomUtilities.getInstance().newBag();
            ArrayList<Integer> marbles = new ArrayList<Integer>(4);
            for (int i = 0; i < 4; ++i) {
                if (!directions[i]) continue;
                marbles.add(new Integer(i));
            }
            AlgorithmRandomUtilities.getInstance().putMarbles(marbles);
            int selectedDirection = AlgorithmRandomUtilities.getInstance().extractAmarble();
            int removals = AlgorithmRandomUtilities.getInstance().getFromInterval(1, maxRemovals);
            if (removals > 0) {
                res = true;
                if (selectedDirection == 0) {
                    for (int i = 0; i < removals; ++i) {
                        individual.deleteSample(individualLeftX);
                        ++individualLeftX;
                    }
                } else if (selectedDirection == 1) {
                    for (int i = 0; i < removals; ++i) {
                        individual.deleteSample(individualRightX);
                        --individualRightX;
                    }
                } else if (selectedDirection == 2) {
                    for (int i = 0; i < removals; ++i) {
                        individual.deleteGene(individualTopY);
                        ++individualTopY;
                    }
                } else if (selectedDirection == 3) {
                    for (int i = 0; i < removals; ++i) {
                        individual.deleteGene(individualDownY);
                        --individualDownY;
                    }
                }
            }
        }
        return res;
    }
}

