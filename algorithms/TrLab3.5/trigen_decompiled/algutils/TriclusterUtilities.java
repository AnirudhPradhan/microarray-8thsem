/*
 * Decompiled with CFR 0.152.
 */
package algutils;

import algcore.AlgorithmDataset;
import algcore.AlgorithmIndividual;
import algutils.AlgorithmRandomUtilities;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TriclusterUtilities {
    private static TriclusterUtilities singleton = new TriclusterUtilities();

    private TriclusterUtilities() {
    }

    public static TriclusterUtilities getInstance() {
        return singleton;
    }

    public List<double[][][]> original(AlgorithmIndividual tricluster, AlgorithmDataset dataset) {
        LinkedList<double[][][]> res = new LinkedList<double[][][]>();
        Collection<Integer> genes = tricluster.getGenes();
        Collection<Integer> condiciones = tricluster.getSamples();
        Collection<Integer> tiempos = tricluster.getTimes();
        double[][][] cgt = this.buildCube(condiciones, genes, tiempos, dataset, 1);
        res.add(cgt);
        double[][][] tgc = this.buildCube(tiempos, genes, condiciones, dataset, 2);
        res.add(tgc);
        double[][][] gtc = this.buildCube(genes, tiempos, condiciones, dataset, 3);
        res.add(gtc);
        return res;
    }

    public double[][][] buildGTCView(AlgorithmIndividual tricluster, AlgorithmDataset dataset) {
        return this.buildCube(tricluster.getGenes(), tricluster.getTimes(), tricluster.getSamples(), dataset, 3);
    }

    private double[][][] buildCube(Collection<Integer> series, Collection<Integer> xAxis, Collection<Integer> graphics, AlgorithmDataset dataset, int type) {
        double[][][] res = new double[series.size()][xAxis.size()][graphics.size()];
        Iterator<Integer> igr = graphics.iterator();
        int i = 0;
        while (igr.hasNext()) {
            int j = 0;
            int grafico = igr.next();
            Iterator<Integer> ieje = xAxis.iterator();
            while (ieje.hasNext()) {
                int k = 0;
                int ejex = ieje.next();
                for (int serie : series) {
                    double valor = 0.0;
                    valor = type == 1 ? dataset.getValue(ejex, serie, grafico) : (type == 2 ? dataset.getValue(ejex, grafico, serie) : dataset.getValue(serie, grafico, ejex));
                    res[k][j][i] = valor;
                    ++k;
                }
                ++j;
            }
            ++i;
        }
        return res;
    }

    public int[] getTensorLimits(int yCoor, int xCoor, int tCoor, int yLen, int xLen, int tLen, int nR, int nC, int nT) {
        int[] res = new int[6];
        int[] ylim = this.getCoordinateInterval(yCoor, yLen, nR);
        int[] xlim = this.getCoordinateInterval(xCoor, xLen, nC);
        int[] tlim = this.getCoordinateInterval(tCoor, tLen, nT);
        res[0] = ylim[0];
        res[1] = ylim[1];
        res[2] = xlim[0];
        res[3] = xlim[1];
        res[4] = tlim[0];
        res[5] = tlim[1];
        return res;
    }

    public int[] getTensor2DLimits(int yCoor, int xCoor, int yLen, int xLen, int nR, int nC) {
        int[] res = new int[4];
        int[] ylim = this.getCoordinateInterval(yCoor, yLen, nR);
        int[] xlim = this.getCoordinateInterval(xCoor, xLen, nC);
        res[0] = ylim[0];
        res[1] = ylim[1];
        res[2] = xlim[0];
        res[3] = xlim[1];
        return res;
    }

    public int[] getCoordinateInterval(int centralValue, int intervalLenth, int maxValue) {
        boolean dispDerecha;
        int[] res = new int[2];
        int numeroIzquierda = centralValue + 1;
        int numeroDerecha = maxValue - (centralValue + 1);
        int reparto1 = intervalLenth / 2;
        int reparto2 = intervalLenth - reparto1;
        boolean dispIzquierda = reparto1 <= numeroIzquierda;
        boolean bl = dispDerecha = reparto2 <= numeroDerecha;
        if (!dispIzquierda && dispDerecha) {
            res[0] = 0;
            res[1] = centralValue + reparto2 + (reparto1 - numeroIzquierda);
        } else if (dispIzquierda && !dispDerecha) {
            res[0] = centralValue - (reparto1 - 1) - (reparto2 - numeroDerecha);
            res[1] = maxValue - 1;
        } else if (dispIzquierda && dispDerecha) {
            res[0] = centralValue - (reparto1 - 1);
            res[1] = centralValue + reparto2;
        }
        return res;
    }

    public Collection<Integer> getIntervalComponent(int centralValue, int intervalLenth, int maxValue) {
        int[] componentlim = this.getCoordinateInterval(centralValue, intervalLenth, maxValue);
        LinkedList<Integer> res = new LinkedList<Integer>();
        for (int i = componentlim[0]; i <= componentlim[1]; ++i) {
            res.add(new Integer(i));
        }
        return res;
    }

    public Collection<Integer> getDispersedRandomComponent(int numberOfCoordinates, Set<Integer> bagOfCoordinates) {
        LinkedList<Integer> res = new LinkedList<Integer>();
        AlgorithmRandomUtilities ALEATORIOS = AlgorithmRandomUtilities.getInstance();
        ALEATORIOS.newBag();
        ALEATORIOS.putMarbles(bagOfCoordinates);
        int[] randomCoordinates = ALEATORIOS.extractNmarbles(numberOfCoordinates);
        for (int i = 0; i < randomCoordinates.length; ++i) {
            res.add(new Integer(randomCoordinates[i]));
        }
        return res;
    }

    public AlgorithmIndividual[] getReproductivePair(List<AlgorithmIndividual> population) {
        AlgorithmRandomUtilities randomSupport = AlgorithmRandomUtilities.getInstance();
        randomSupport.newBag(population.size());
        AlgorithmIndividual[] r = new AlgorithmIndividual[2];
        int fatherIndex = randomSupport.extractAmarble();
        int motherIndex = randomSupport.extractAmarble();
        r[0] = population.get(fatherIndex);
        r[1] = population.get(motherIndex);
        return r;
    }

    public int[] getRandomChromosome(Collection<Integer> father, Collection<Integer> mother, int minL, int maxL) {
        AlgorithmRandomUtilities ALEAT = AlgorithmRandomUtilities.getInstance();
        ALEAT.newBag();
        ALEAT.putMarbles(father);
        ALEAT.putMarbles(mother);
        int minimo = 0;
        int maximo = ALEAT.bagSize();
        minimo = minL;
        if (maximo > maxL) {
            maximo = maxL;
        }
        int tam = ALEAT.getFromInterval(minimo, maximo);
        return ALEAT.extractNmarbles(tam);
    }

    public Collection<Integer>[] buildOnePointComponents(Collection<Integer> fatherComponent, Collection<Integer> motherComponent, int minL, int maxL, int datasetComponentSize) {
        Collection[] childrensComponents = new Collection[2];
        int[] cutIndexes = this.getIndexesFromComponent(fatherComponent.size(), motherComponent.size(), minL);
        List<Integer> sonComponent = this.componentByIndexes(fatherComponent.toArray(new Integer[0]), motherComponent.toArray(new Integer[0]), 0, cutIndexes[0], cutIndexes[1], motherComponent.size());
        this.chekSizes(sonComponent, minL, maxL, datasetComponentSize);
        Collections.sort(sonComponent);
        List<Integer> daughterComponent = this.componentByIndexes(fatherComponent.toArray(new Integer[0]), motherComponent.toArray(new Integer[0]), cutIndexes[0], fatherComponent.size(), 0, cutIndexes[1]);
        this.chekSizes(daughterComponent, minL, maxL, datasetComponentSize);
        Collections.sort(daughterComponent);
        childrensComponents[0] = sonComponent;
        childrensComponents[1] = daughterComponent;
        return childrensComponents;
    }

    private List<Integer> componentByIndexes(Integer[] fatherComponent, Integer[] motherComponent, int lowerFather, int upperFather, int lowerMother, int upperMother) {
        Integer n;
        int j;
        LinkedList<Integer> r = new LinkedList<Integer>();
        for (j = lowerFather; j < upperFather; ++j) {
            n = new Integer(fatherComponent[j]);
            if (r.contains(n)) continue;
            r.add(n);
        }
        for (j = lowerMother; j < upperMother; ++j) {
            n = new Integer(motherComponent[j]);
            if (r.contains(n)) continue;
            r.add(n);
        }
        return r;
    }

    private void chekSizes(List<Integer> component, int minL, int maxL, int datasetComponentSize) {
        int bola;
        int i;
        AlgorithmRandomUtilities rndUtils = AlgorithmRandomUtilities.getInstance();
        int difMin = minL - component.size();
        int difMax = component.size() - maxL;
        if (difMin > 0) {
            rndUtils.newBag(datasetComponentSize);
            for (i = 0; i < difMin; ++i) {
                bola = rndUtils.extractAmarble();
                Integer coordenada = new Integer(bola);
                while (component.contains(coordenada)) {
                    bola = rndUtils.extractAmarble();
                    coordenada = new Integer(bola);
                }
                component.add(coordenada);
            }
        }
        if (difMax > 0) {
            rndUtils.newBag(component.size());
            for (i = 0; i < difMax; ++i) {
                bola = rndUtils.extractAmarble();
                component.remove(bola);
            }
        }
    }

    private int[] getIndexesFromComponent(int fatherSize, int motherSize, int minL) {
        int[] res = new int[2];
        int indicePadre = 0;
        int indiceMadre = 0;
        if (minL != 1) {
            if (fatherSize == motherSize) {
                double porcentaje = AlgorithmRandomUtilities.getInstance().getPercentage();
                indicePadre = (int)(porcentaje * (double)fatherSize);
                indiceMadre = (int)(porcentaje * (double)motherSize);
                res[0] = indicePadre;
                res[1] = indiceMadre;
            } else {
                int tamHija;
                int tamHijo;
                double porcentaje = AlgorithmRandomUtilities.getInstance().getPercentage();
                boolean correcto = true;
                if (porcentaje != 0.0) {
                    indicePadre = (int)(porcentaje * (double)fatherSize);
                    indiceMadre = (int)(porcentaje * (double)motherSize);
                    tamHijo = indicePadre + (motherSize - indiceMadre);
                    tamHija = fatherSize - indicePadre + indiceMadre;
                    if (tamHijo < minL || tamHija < minL) {
                        correcto = false;
                    }
                }
                while (porcentaje == 0.0 || !correcto) {
                    porcentaje = AlgorithmRandomUtilities.getInstance().getPercentage();
                    correcto = true;
                    if (porcentaje == 0.0) continue;
                    indicePadre = (int)(porcentaje * (double)fatherSize);
                    indiceMadre = (int)(porcentaje * (double)motherSize);
                    tamHijo = indicePadre + (motherSize - indiceMadre);
                    tamHija = fatherSize - indicePadre + indiceMadre;
                    if (tamHijo >= minL && tamHija >= minL) continue;
                    correcto = false;
                }
                res[0] = indicePadre;
                res[1] = indiceMadre;
            }
        } else {
            double porcentaje = AlgorithmRandomUtilities.getInstance().getPercentage();
            indicePadre = (int)(porcentaje * (double)fatherSize);
            indiceMadre = (int)(porcentaje * (double)motherSize);
            res[0] = indicePadre;
            res[1] = indiceMadre;
        }
        return res;
    }

    public Collection<Integer>[] buildTimeSeriesComponents(Collection<Integer> fatherComponent, Collection<Integer> motherComponent, int minL, int maxL) {
        Collection[] childrensComponents = new Collection[2];
        HashSet<Integer> f = new HashSet<Integer>(fatherComponent);
        HashSet<Integer> m = new HashSet<Integer>(motherComponent);
        HashSet<Integer> intersection = new HashSet<Integer>(f);
        intersection.retainAll(m);
        if (!intersection.isEmpty()) {
            LinkedList<Integer> aux = new LinkedList<Integer>(intersection);
            Collections.sort(aux);
            this.checkTimeSeriesLimits(aux, minL, maxL);
            childrensComponents[0] = aux;
            childrensComponents[1] = aux;
        } else {
            ArrayList<Integer> lf = new ArrayList<Integer>(fatherComponent);
            ArrayList<Integer> lm = new ArrayList<Integer>(motherComponent);
            int from = Math.min((Integer)lf.get(lf.size() - 1), (Integer)lm.get(lm.size() - 1)) + 1;
            int to = Math.max((Integer)lf.get(0), (Integer)lm.get(0)) - 1;
            if (to - from < 0) {
                LinkedList<Integer> aux = new LinkedList<Integer>(lf);
                aux.addAll(lm);
                Collections.sort(aux);
                this.checkTimeSeriesLimits(aux, minL, maxL);
                childrensComponents[0] = aux;
                childrensComponents[1] = aux;
            } else {
                LinkedList<Integer> aux = new LinkedList<Integer>();
                for (int i = from; i <= to; ++i) {
                    aux.add(new Integer(i));
                }
                this.checkTimeSeriesLimits(aux, minL, maxL);
                childrensComponents[0] = aux;
                childrensComponents[1] = aux;
            }
        }
        return childrensComponents;
    }

    private void checkTimeSeriesLimits(List<Integer> ts, int minL, int maxL) {
        block3: {
            block2: {
                if (ts.size() >= minL) break block2;
                int from = ts.get(ts.size() - 1) + 1;
                int to = from + (minL - ts.size() - 1);
                for (int i = from; i <= to; ++i) {
                    ts.add(new Integer(i));
                }
                break block3;
            }
            if (ts.size() <= maxL) break block3;
            int n = ts.size() - maxL;
            for (int i = 0; i < n; ++i) {
                ((LinkedList)ts).removeLast();
            }
        }
    }

    public AlgorithmIndividual buildIndividual(Collection<Integer> g, Collection<Integer> c2, Collection<Integer> t, String individualClassName, String entryLog) {
        AlgorithmIndividual individuo = null;
        try {
            individuo = (AlgorithmIndividual)Class.forName(individualClassName).newInstance();
            individuo.initialize(g, c2, t);
            individuo.addEntry(entryLog);
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return individuo;
    }

    public String populationToString(List<AlgorithmIndividual> population) {
        String r = "";
        int i = 1;
        for (AlgorithmIndividual ind : population) {
            r = r + "[" + i + "] " + this.individualToString(ind);
            if (i < population.size()) {
                r = r + "\n";
            }
            ++i;
        }
        return r;
    }

    public String individualToString(AlgorithmIndividual individual) {
        String r = "";
        r = r + " X{" + individual.getSampleSize() + "} " + individual.getSamples() + "  Y {" + individual.getGeneSize() + "} " + individual.getGenes();
        return r;
    }
}

