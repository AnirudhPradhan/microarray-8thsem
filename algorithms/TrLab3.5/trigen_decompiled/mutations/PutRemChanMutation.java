/*
 * Decompiled with CFR 0.152.
 */
package mutations;

import algcore.AlgorithmIndividual;
import algcore.Mutation;
import algutils.AlgorithmRandomUtilities;
import mutations.MutationStrategy;
import strmutation.CondicionInsertionStrategy;
import strmutation.ConditionChangeStrategy;
import strmutation.ConditionRemovalStrategy;
import strmutation.GenChangeStrategy;
import strmutation.GenInsertionStrategy;
import strmutation.GenRemovalStrategy;
import strmutation.TimeChangeStrategy;
import strmutation.TimeInsertionStrategy;
import strmutation.TimeRemovalStrategy;

public class PutRemChanMutation
implements Mutation {
    private MutationStrategy quitar_tiempo = new TimeRemovalStrategy();
    private MutationStrategy quitar_gen = new GenRemovalStrategy();
    private MutationStrategy quitar_condicion = new ConditionRemovalStrategy();
    private MutationStrategy poner_tiempo = new TimeInsertionStrategy();
    private MutationStrategy poner_gen = new GenInsertionStrategy();
    private MutationStrategy poner_condicion = new CondicionInsertionStrategy();
    private MutationStrategy cambiar_gen = new GenChangeStrategy();
    private MutationStrategy cambiar_condicion = new ConditionChangeStrategy();
    private MutationStrategy cambiar_tiempo = new TimeChangeStrategy();

    @Override
    public void mutate(AlgorithmIndividual individual) {
        AlgorithmRandomUtilities randomSupport = AlgorithmRandomUtilities.getInstance();
        this.applyAlteration(individual, randomSupport.getFromInterval(1, 9));
    }

    private void applyAlteration(AlgorithmIndividual individual, int type) {
        if (type == 1) {
            this.quitar_gen.alter(individual);
        } else if (type == 2) {
            this.quitar_condicion.alter(individual);
        } else if (type == 3) {
            this.quitar_tiempo.alter(individual);
        } else if (type == 4) {
            this.poner_gen.alter(individual);
        } else if (type == 5) {
            this.poner_condicion.alter(individual);
        } else if (type == 6) {
            this.poner_tiempo.alter(individual);
        } else if (type == 7) {
            this.cambiar_gen.alter(individual);
        } else if (type == 8) {
            this.cambiar_condicion.alter(individual);
        } else if (type == 9) {
            this.cambiar_tiempo.alter(individual);
        }
    }
}

