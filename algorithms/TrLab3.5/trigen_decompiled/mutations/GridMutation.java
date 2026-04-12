/*
 * Decompiled with CFR 0.152.
 */
package mutations;

import algcore.AlgorithmIndividual;
import algcore.Mutation;
import algutils.AlgorithmRandomUtilities;
import mutations.MutationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import strmutation.GridInsertBorderStrategy;
import strmutation.GridInsertPerimeterStrategy;
import strmutation.GridRemoveBorderStrategy;
import strmutation.GridRemovePerimeterStrategy;
import strmutation.TimeChangeStrategy;
import strmutation.TimeInsertionStrategy;
import strmutation.TimeRemovalStrategy;

public class GridMutation
implements Mutation {
    private static final Logger LOG = LoggerFactory.getLogger(GridMutation.class);
    private MutationStrategy insertPerimeter = new GridInsertPerimeterStrategy();
    private MutationStrategy insertBorder = new GridInsertBorderStrategy();
    private MutationStrategy removePerimeter = new GridRemovePerimeterStrategy();
    private MutationStrategy removeBorder = new GridRemoveBorderStrategy();
    private MutationStrategy insertTime = new TimeInsertionStrategy();
    private MutationStrategy removeTime = new TimeRemovalStrategy();
    private MutationStrategy changeTime = new TimeChangeStrategy();

    @Override
    public void mutate(AlgorithmIndividual individual) {
        int op = AlgorithmRandomUtilities.getInstance().getFromInterval(1, 4);
        if (op == 1) {
            this.insertPerimeter.alter(individual);
        } else if (op == 2) {
            this.insertBorder.alter(individual);
        } else if (op == 3) {
            this.removePerimeter.alter(individual);
        } else {
            this.removeBorder.alter(individual);
        }
        op = AlgorithmRandomUtilities.getInstance().getFromInterval(1, 3);
        if (op == 1) {
            this.insertTime.alter(individual);
        } else if (op == 2) {
            this.removeTime.alter(individual);
        } else {
            this.changeTime.alter(individual);
        }
    }
}

