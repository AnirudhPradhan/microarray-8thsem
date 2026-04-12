/*
 * Decompiled with CFR 0.152.
 */
package algutils;

import algcore.Crossover;
import algcore.DataHierarchy;
import algcore.FitnessFunction;
import algcore.InitialPop;
import algcore.Mutation;
import algcore.Selection;
import algcore.SolutionCriterion;
import algcore.StoppingCriterion;
import algcore.TriGen;
import input.algorithm.Implementation;

public class TriGenBuilder {
    private static TriGenBuilder singleton = new TriGenBuilder();

    private TriGenBuilder() {
    }

    public static TriGenBuilder getInstance() {
        return singleton;
    }

    public void buildTriGen(Implementation implementation) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        InitialPop initialPop = (InitialPop)Class.forName(implementation.getInitialPop()).newInstance();
        FitnessFunction fitnessFunction = (FitnessFunction)Class.forName(implementation.getFitness()).newInstance();
        Selection selection = (Selection)Class.forName(implementation.getSelection()).newInstance();
        Crossover crossover = (Crossover)Class.forName(implementation.getCrossover()).newInstance();
        Mutation mutation = (Mutation)Class.forName(implementation.getMutation()).newInstance();
        SolutionCriterion solCriterion = (SolutionCriterion)Class.forName(implementation.getSolutionCriterion()).newInstance();
        StoppingCriterion stopCriterion = (StoppingCriterion)Class.forName(implementation.getStoppingCriterion()).newInstance();
        TriGen alg = TriGen.getInstance();
        alg.set(implementation.getIndividual(), initialPop, fitnessFunction, selection, crossover, mutation, solCriterion, stopCriterion);
    }

    public DataHierarchy buildDataHierarchyV2(String type, int geneSize, int sampleSize, int timeSize) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        DataHierarchy j = (DataHierarchy)Class.forName(type).newInstance();
        j.initialize(geneSize, sampleSize, timeSize);
        return j;
    }
}

