/*
 * Decompiled with CFR 0.152.
 */
package input.algorithm;

public class Implementation {
    private String individual;
    private String fitness;
    private String dataHierarchy;
    private String stoppingCriterion;
    private String solutionCriterion;
    private String initialPop;
    private String selection;
    private String crossover;
    private String mutation;

    public Implementation(String individual, String fitness, String dataHierarchy, String stoppingCriterion, String solutionCriterion, String initialPop, String selection, String crossover, String mutation) {
        this.individual = individual;
        this.fitness = fitness;
        this.dataHierarchy = dataHierarchy;
        this.stoppingCriterion = stoppingCriterion;
        this.solutionCriterion = solutionCriterion;
        this.initialPop = initialPop;
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
    }

    public String getIndividual() {
        return this.individual;
    }

    public String getFitness() {
        return this.fitness;
    }

    public String getDataHierarchy() {
        return this.dataHierarchy;
    }

    public String getStoppingCriterion() {
        return this.stoppingCriterion;
    }

    public String getSolutionCriterion() {
        return this.solutionCriterion;
    }

    public String getInitialPop() {
        return this.initialPop;
    }

    public String getSelection() {
        return this.selection;
    }

    public String getCrossover() {
        return this.crossover;
    }

    public String getMutation() {
        return this.mutation;
    }

    public String toString() {
        String r = "";
        r = "Individual = " + this.individual + "\nFitness = " + this.fitness + "\nData Hierarchy = " + this.dataHierarchy + "\nStopping Criterion = " + this.stoppingCriterion + "\nSolution Criterion = " + this.solutionCriterion + "\nInitial Population = " + this.initialPop + "\nSelection = " + this.selection + "\nCrossover = " + this.crossover + "\nMutation = " + this.mutation;
        return r;
    }
}

