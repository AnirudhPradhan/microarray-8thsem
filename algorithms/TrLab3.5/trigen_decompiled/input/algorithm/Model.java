/*
 * Decompiled with CFR 0.152.
 */
package input.algorithm;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private String name;
    private Map<String, String> individuals;
    private Map<String, FitnessClass> fitness;
    private Map<String, String> dataHierarchies;
    private Map<String, String> stoppingCriteria;
    private Map<String, String> solutionCriteria;
    private Map<String, String> initialPops;
    private Map<String, String> selections;
    private Map<String, String> crossovers;
    private Map<String, String> mutations;

    public Model(String name) {
        this.name = name;
        this.individuals = new HashMap<String, String>();
        this.fitness = new HashMap<String, FitnessClass>();
        this.dataHierarchies = new HashMap<String, String>();
        this.stoppingCriteria = new HashMap<String, String>();
        this.solutionCriteria = new HashMap<String, String>();
        this.initialPops = new HashMap<String, String>();
        this.selections = new HashMap<String, String>();
        this.crossovers = new HashMap<String, String>();
        this.mutations = new HashMap<String, String>();
    }

    public void putIndividual(String name, String className) {
        this.individuals.put(name, className);
    }

    public void putFitness(String name, String className) {
        FitnessClass fc = new FitnessClass(className);
        this.fitness.put(name, fc);
    }

    public void putTransformationInFitness(String transformationName, String trasnformationClassName, String fitnessName) {
        FitnessClass fc = this.fitness.get(fitnessName);
        fc.putTransformation(transformationName, trasnformationClassName);
    }

    public void putDataHierarchy(String name, String className) {
        this.dataHierarchies.put(name, className);
    }

    public void putStoppingCriterion(String name, String className) {
        this.stoppingCriteria.put(name, className);
    }

    public void putSolutionCriterion(String name, String className) {
        this.solutionCriteria.put(name, className);
    }

    public void putInitialPop(String name, String className) {
        this.initialPops.put(name, className);
    }

    public void putSelection(String name, String className) {
        this.selections.put(name, className);
    }

    public void putCrossover(String name, String className) {
        this.crossovers.put(name, className);
    }

    public void putMutation(String name, String className) {
        this.mutations.put(name, className);
    }

    public String getName() {
        return this.name;
    }

    public Map<String, String> getIndividuals() {
        return this.individuals;
    }

    public Map<String, FitnessClass> getFitness() {
        return this.fitness;
    }

    public Map<String, String> getDataHierarchies() {
        return this.dataHierarchies;
    }

    public Map<String, String> getStoppingCriteria() {
        return this.stoppingCriteria;
    }

    public Map<String, String> getSolutionCriteria() {
        return this.solutionCriteria;
    }

    public Map<String, String> getInitialPops() {
        return this.initialPops;
    }

    public Map<String, String> getSelections() {
        return this.selections;
    }

    public Map<String, String> getCrossovers() {
        return this.crossovers;
    }

    public Map<String, String> getMutations() {
        return this.mutations;
    }

    public String toString() {
        String r = "";
        r = this.name.toUpperCase() + "\n" + "\nIndividuals: " + this.individuals.toString() + "\nFitness: " + this.fitness.toString() + "\nData Hierarchies: " + this.dataHierarchies.toString() + "\nStopping Criteria: " + this.stoppingCriteria.toString() + "\nSolution Criteria: " + this.solutionCriteria.toString() + "\nInitial Populations: " + this.initialPops.toString() + "\nSelections: " + this.selections.toString() + "\nCrossovers: " + this.crossovers.toString() + "\nMutations: " + this.mutations.toString();
        return r;
    }

    public class FitnessClass {
        private String fitnessClassName;
        private Map<String, String> transformations;

        public FitnessClass(String fitnessClassName) {
            this.fitnessClassName = fitnessClassName;
            this.transformations = new HashMap<String, String>();
        }

        public String getFitnessClassName() {
            return this.fitnessClassName;
        }

        public Map<String, String> getTransformations() {
            return this.transformations;
        }

        public void putTransformation(String name, String className) {
            this.transformations.put(name, className);
        }

        public String toString() {
            String r = "";
            r = this.fitnessClassName + "--" + this.transformations;
            return r;
        }
    }
}

