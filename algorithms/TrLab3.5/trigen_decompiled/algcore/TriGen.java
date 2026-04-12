/*
 * Decompiled with CFR 0.152.
 */
package algcore;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmIndividual;
import algcore.ConcurrentEvaluator;
import algcore.Crossover;
import algcore.DataHierarchy;
import algcore.FitnessFunction;
import algcore.InitialPop;
import algcore.Mutation;
import algcore.Selection;
import algcore.SolutionCriterion;
import algcore.StoppingCriterion;
import alginterface.TriGenGuiTask;
import algutils.AlgorithmRandomUtilities;
import algutils.TriclusterUtilities;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.TextUtilities;

public class TriGen {
    private static final Logger LOG = LoggerFactory.getLogger(TriGen.class);
    private static TriGen singleton = new TriGen();
    private String individualClassName;
    private InitialPop initialPop;
    private FitnessFunction fitnessFunction;
    private Selection selection;
    private Crossover crossover;
    private Mutation mutation;
    private SolutionCriterion solutionCriterion;
    private StoppingCriterion stoppingCriterion;
    private List<AlgorithmIndividual> solutions;
    private int ongoingSolutionIndex;
    private int ongoingGenerationIndex;
    private int ongoingEvaluatedIndividualIndex;
    private TriGenGuiTask task;
    private boolean canceled;

    private TriGen() {
    }

    public static TriGen getInstance() {
        return singleton;
    }

    public void set(String indClassName, InitialPop initPop, FitnessFunction fitness, Selection selection, Crossover crossover, Mutation mutation, SolutionCriterion solCriterion, StoppingCriterion stopCriterion) {
        this.individualClassName = indClassName;
        this.initialPop = initPop;
        this.fitnessFunction = fitness;
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
        this.solutionCriterion = solCriterion;
        this.stoppingCriterion = stopCriterion;
        this.solutions = new LinkedList<AlgorithmIndividual>();
        this.ongoingSolutionIndex = 0;
        this.ongoingGenerationIndex = 0;
    }

    public List<AlgorithmIndividual> runAlgorithm() {
        AlgorithmConfiguration PARAM = AlgorithmConfiguration.getInstance();
        DataHierarchy JER = PARAM.getDataHierarchy();
        LOG.debug("\n");
        this.canceled = false;
        double rop = 0.0;
        double top = PARAM.getN() * (4 + 4 * PARAM.getG());
        double com = 0.0;
        this.checkTaskState(com);
        String scom = "";
        DecimalFormat format = TextUtilities.getDecimalFormat('.', "0.0");
        List<AlgorithmIndividual> population = null;
        List<AlgorithmIndividual> parents = null;
        List<AlgorithmIndividual> children = null;
        List<AlgorithmIndividual> mutatedChildren = null;
        this.ongoingSolutionIndex = 0;
        while (this.stoppingCriterion.checkCriterion() && !this.canceled) {
            com = rop / top * 100.0;
            this.checkTaskState(com);
            scom = format.format((Object)com);
            LOG.info("\nSolution " + (this.ongoingSolutionIndex + 1) + ": Completed = " + scom + "%, Hierarchy=" + format.format((Object)JER.getPercentage()) + "%\n");
            this.ongoingGenerationIndex = 0;
            if (!this.canceled) {
                population = this.produceInitialPopulation();
                LOG.debug("\n>>>>> Initial population: \n");
                LOG.debug(TriclusterUtilities.getInstance().populationToString(population) + "\n");
                LOG.debug("\n<<<<< Initial population: \n");
            }
            rop += 1.0;
            while (this.ongoingGenerationIndex < PARAM.getG() && !this.canceled) {
                com = rop / top * 100.0;
                this.checkTaskState(com);
                scom = format.format((Object)com);
                LOG.info("[" + (this.ongoingSolutionIndex + 1) + "," + (this.ongoingGenerationIndex + 1) + "] Completed = " + scom + "% Hierarchy=" + format.format((Object)JER.getPercentage()) + "%\n");
                if (!this.canceled) {
                    LOG.debug("--evaluation");
                    this.evaluate(population);
                }
                rop += 1.0;
                if (!this.canceled) {
                    LOG.debug("--selection");
                    parents = this.select(population);
                }
                rop += 1.0;
                if (!this.canceled) {
                    LOG.debug("--crossover");
                    children = this.crossover(population, parents);
                }
                rop += 1.0;
                if (!this.canceled) {
                    LOG.debug("--mutation\n");
                    mutatedChildren = this.mutate(children);
                }
                rop += 1.0;
                if (!this.canceled) {
                    population.clear();
                    population.addAll(parents);
                    population.addAll(mutatedChildren);
                }
                ++this.ongoingGenerationIndex;
            }
            com = rop / top * 100.0;
            this.checkTaskState(com);
            scom = format.format((Object)com);
            if (!this.canceled) {
                LOG.debug("--results evaluation");
                this.evaluate(population);
            }
            com = (rop += 1.0) / top * 100.0;
            this.checkTaskState(com);
            scom = format.format((Object)com);
            AlgorithmIndividual best = null;
            if (!this.canceled) {
                LOG.debug("--solution criterion");
                best = this.chooseTheBest(population);
            }
            rop += 1.0;
            if (!this.canceled) {
                this.solutions.add(best);
            }
            com = rop / top * 100.0;
            this.checkTaskState(com);
            scom = format.format((Object)com);
            if (!this.canceled) {
                LOG.debug("--update data hierarchy\n");
                JER.update(best);
            }
            LOG.debug("\n>>>>>\n");
            LOG.debug("Best: " + TriclusterUtilities.getInstance().individualToString(best) + "\n");
            LOG.debug("\n");
            LOG.debug("Hierarchy: \n");
            LOG.debug(JER.toString());
            LOG.debug("<<<<<\n");
            rop += 1.0;
            ++this.ongoingSolutionIndex;
        }
        com = 100.0;
        this.checkTaskState(com);
        LOG.info("\n");
        if (!this.canceled) {
            LOG.info("Completed = " + com + "%");
        } else {
            LOG.info("Cancelled");
        }
        LOG.info("\n");
        return this.solutions;
    }

    private List<AlgorithmIndividual> produceInitialPopulation() {
        return this.initialPop.produceInitialPop();
    }

    private void evaluate(List<AlgorithmIndividual> population) {
        DecimalFormat f = TextUtilities.getDecimalFormat('.', "0.000");
        AlgorithmConfiguration c2 = AlgorithmConfiguration.getInstance();
        if (!c2.isConcurrency()) {
            this.ongoingEvaluatedIndividualIndex = 1;
            for (AlgorithmIndividual ind : population) {
                if (!ind.canSkipEvaluation()) {
                    this.fitnessFunction.evaluate(ind);
                    ind.addEntry("evaluated FF = " + f.format(ind.getFitnessFunctionValue()) + " [" + (this.ongoingGenerationIndex + 1) + "]");
                    ind.setEvaluated(true);
                    ind.setAltered(false);
                }
                ++this.ongoingEvaluatedIndividualIndex;
            }
        } else {
            ExecutorService pool = Executors.newFixedThreadPool(c2.getThreads());
            LinkedList<ConcurrentEvaluator> concurrentTask = new LinkedList<ConcurrentEvaluator>();
            int i = 0;
            for (AlgorithmIndividual ind : population) {
                if (ind.canSkipEvaluation()) continue;
                concurrentTask.add(new ConcurrentEvaluator(this.fitnessFunction, ind, i));
                ind.addEntry("evaluated [" + (this.ongoingGenerationIndex + 1) + "]");
                ind.setEvaluated(true);
                ind.setAltered(false);
                ++i;
            }
            try {
                pool.invokeAll(concurrentTask);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            pool.shutdown();
        }
    }

    private List<AlgorithmIndividual> select(List<AlgorithmIndividual> population) {
        AlgorithmConfiguration config = AlgorithmConfiguration.getInstance();
        int numberOfSelections = (int)(config.getSel() * (float)config.getI());
        return this.selection.select(numberOfSelections, population);
    }

    private List<AlgorithmIndividual> crossover(List<AlgorithmIndividual> basePopulation, List<AlgorithmIndividual> selectedPopulation) {
        AlgorithmConfiguration config = AlgorithmConfiguration.getInstance();
        int numberOfChildren = config.getI() - selectedPopulation.size();
        return this.crossover.cross(numberOfChildren, selectedPopulation);
    }

    private List<AlgorithmIndividual> mutate(List<AlgorithmIndividual> population) {
        AlgorithmRandomUtilities randomSupport = AlgorithmRandomUtilities.getInstance();
        AlgorithmConfiguration config = AlgorithmConfiguration.getInstance();
        for (AlgorithmIndividual ind : population) {
            double porcentaje = randomSupport.getPercentage();
            boolean mutateIndividual = porcentaje < (double)config.getMut();
            if (!mutateIndividual) continue;
            this.mutation.mutate(ind);
            ind.setAltered(true);
            ind.addEntry("mutated [" + (this.ongoingGenerationIndex + 1) + "]");
        }
        return population;
    }

    private AlgorithmIndividual chooseTheBest(List<AlgorithmIndividual> population) {
        return this.solutionCriterion.chooseTheBest(population);
    }

    public List<AlgorithmIndividual> getSolutions() {
        return this.solutions;
    }

    public String getIndividualClassName() {
        return this.individualClassName;
    }

    public int getOngoingSolutionIndex() {
        return this.ongoingSolutionIndex;
    }

    public int getOngoingGenerationIndex() {
        return this.ongoingGenerationIndex;
    }

    public int getOngoingEvaluatedIndividualIndex() {
        return this.ongoingEvaluatedIndividualIndex;
    }

    public void setTask(TriGenGuiTask task) {
        this.task = task;
    }

    public boolean isCancelled() {
        return this.canceled;
    }

    public void cancel() {
        this.canceled = true;
    }

    private void checkTaskState(double v) {
        if (this.task != null) {
            if (v > 100.0) {
                v = 100.0;
            }
            this.task.setProgress(v);
            if (this.task.isCancelled()) {
                this.canceled = true;
            }
        }
    }
}

