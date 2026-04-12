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

public class GridInsertBorderStrategy
implements MutationStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(GridInsertBorderStrategy.class);

    @Override
    public boolean alter(AlgorithmIndividual individual) {
        boolean res;
        block23: {
            res = false;
            int individualLeftX = individual.getSample(0);
            int individualRightX = individual.getSample(individual.getSampleSize() - 1);
            int individualTopY = individual.getGene(0);
            int individualDownY = individual.getGene(individual.getGeneSize() - 1);
            int xSize = AlgorithmConfiguration.getInstance().getMaxC() - individual.getSampleSize();
            int ySize = AlgorithmConfiguration.getInstance().getMaxG() - individual.getGeneSize();
            int leftBorderX = individualLeftX - xSize;
            int rightBorderX = individualRightX + xSize;
            int topBorderY = individualTopY - ySize;
            int downBorderY = individualDownY + ySize;
            boolean[] directions = new boolean[]{false, false, false, false};
            int maxInserts = Integer.MAX_VALUE;
            if (xSize > 0) {
                if (leftBorderX > 0) {
                    directions[0] = true;
                    if (xSize < maxInserts) {
                        maxInserts = xSize;
                    }
                }
                if (rightBorderX < AlgorithmConfiguration.getInstance().getData().getSampleSize() - 1) {
                    directions[1] = true;
                    if (xSize < maxInserts) {
                        maxInserts = xSize;
                    }
                }
            }
            if (ySize > 0) {
                if (topBorderY > 0) {
                    directions[2] = true;
                    if (ySize < maxInserts) {
                        maxInserts = ySize;
                    }
                }
                if (downBorderY < AlgorithmConfiguration.getInstance().getData().getGenSize() - 1) {
                    directions[3] = true;
                    if (ySize < maxInserts) {
                        maxInserts = ySize;
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
            int insert = AlgorithmRandomUtilities.getInstance().getFromInterval(1, maxInserts);
            if (insert > 0) {
                res = true;
                if (selectedDirection == 0) {
                    for (int i = 0; i < insert; ++i) {
                        individual.putSample(individualLeftX - 1);
                        --individualLeftX;
                    }
                } else if (selectedDirection == 1) {
                    for (int i = 0; i < insert; ++i) {
                        individual.putSample(individualRightX + 1);
                        ++individualRightX;
                    }
                } else if (selectedDirection == 2) {
                    for (int i = 0; i < insert; ++i) {
                        individual.putGene(individualTopY - 1);
                        --individualTopY;
                    }
                } else if (selectedDirection == 3) {
                    for (int i = 0; i < insert; ++i) {
                        individual.putGene(individualDownY + 1);
                        ++individualDownY;
                    }
                }
            }
        }
        return res;
    }
}

