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
import strmutation.CondicionInsertionStrategy;
import strmutation.ConditionChangeStrategy;
import strmutation.ConditionRemovalStrategy;
import strmutation.GenChangeStrategy;
import strmutation.GenInsertionStrategy;
import strmutation.GenRemovalStrategy;
import strmutation.TimeSeriesInsertBothStrategy;
import strmutation.TimeSeriesInsertLeftStrategy;
import strmutation.TimeSeriesInsertRightStrategy;
import strmutation.TimeSeriesRemoveBothStrategy;
import strmutation.TimeSeriesRemoveLeftStrategy;
import strmutation.TimeSeriesRemoveRightStrategy;

public class TimeSeriesRelaxedMutation
implements Mutation {
    private static final Logger LOG = LoggerFactory.getLogger(TimeSeriesRelaxedMutation.class);
    private MutationStrategy insertG = new GenInsertionStrategy();
    private MutationStrategy removeG = new GenRemovalStrategy();
    private MutationStrategy changeG = new GenChangeStrategy();
    private MutationStrategy insertC = new CondicionInsertionStrategy();
    private MutationStrategy removeC = new ConditionRemovalStrategy();
    private MutationStrategy changeC = new ConditionChangeStrategy();
    private MutationStrategy insertL = new TimeSeriesInsertLeftStrategy();
    private MutationStrategy insertR = new TimeSeriesInsertRightStrategy();
    private MutationStrategy insertB = new TimeSeriesInsertBothStrategy();
    private MutationStrategy removeL = new TimeSeriesRemoveLeftStrategy();
    private MutationStrategy removeR = new TimeSeriesRemoveRightStrategy();
    private MutationStrategy removeB = new TimeSeriesRemoveBothStrategy();

    @Override
    public void mutate(AlgorithmIndividual individual) {
        AlgorithmRandomUtilities randomSupport = AlgorithmRandomUtilities.getInstance();
        int op = randomSupport.getFromInterval(1, 12);
        if (op == 1) {
            this.insertG.alter(individual);
        } else if (op == 2) {
            this.removeG.alter(individual);
        } else if (op == 3) {
            this.changeG.alter(individual);
        } else if (op == 4) {
            this.insertC.alter(individual);
        } else if (op == 5) {
            this.removeC.alter(individual);
        } else if (op == 6) {
            this.changeC.alter(individual);
        } else if (op == 7) {
            this.insertL.alter(individual);
        } else if (op == 8) {
            this.insertR.alter(individual);
        } else if (op == 9) {
            this.insertB.alter(individual);
        } else if (op == 10) {
            this.removeL.alter(individual);
        } else if (op == 11) {
            this.removeR.alter(individual);
        } else {
            this.removeB.alter(individual);
        }
    }
}

