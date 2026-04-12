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

public class GridInsertPerimeterStrategy
implements MutationStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(GridInsertPerimeterStrategy.class);

    @Override
    public boolean alter(AlgorithmIndividual individual) {
        boolean res = false;
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
        if (directions[0] || directions[1] || directions[2] || directions[3]) {
            int loops = 0;
            int chunks = 0;
            int insert = 0;
            if (xSize == ySize && directions[0] && directions[1] && directions[2] && directions[3]) {
                loops = xSize / 2;
                chunks = xSize % 2;
                if (loops == 0) {
                    insert = AlgorithmRandomUtilities.getInstance().getFromInterval(1, maxInserts);
                } else {
                    int loopInsertion = AlgorithmRandomUtilities.getInstance().getFromInterval(1, loops);
                    int chunkInsertion = 0;
                    if (chunks != 0) {
                        chunkInsertion = AlgorithmRandomUtilities.getInstance().getFromInterval(1, chunks);
                    }
                    insert = loopInsertion * 4 + chunkInsertion;
                }
            } else {
                insert = AlgorithmRandomUtilities.getInstance().getFromInterval(1, maxInserts);
            }
            if (insert > 0) {
                res = true;
            }
            while (insert > 0) {
                if (directions[0] && insert > 0) {
                    individual.putSample(individualLeftX - 1);
                    --individualLeftX;
                    --insert;
                }
                if (directions[1] && insert > 0) {
                    individual.putSample(individualRightX + 1);
                    ++individualRightX;
                    --insert;
                }
                if (directions[2] && insert > 0) {
                    individual.putGene(individualTopY - 1);
                    --individualTopY;
                    --insert;
                }
                if (!directions[3] || insert <= 0) continue;
                individual.putGene(individualDownY + 1);
                ++individualDownY;
                --insert;
            }
        }
        return res;
    }
}

