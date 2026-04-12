/*
 * Decompiled with CFR 0.152.
 */
package strcrossover;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmDataset;
import algcore.AlgorithmIndividual;
import algcore.TriGen;
import algutils.AlgorithmRandomUtilities;
import crossovers.CrossoverStrategy;
import java.util.Collection;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnePointStrategy
implements CrossoverStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(OnePointStrategy.class);

    @Override
    public AlgorithmIndividual[] cross(AlgorithmIndividual father, AlgorithmIndividual mother, String individualClassName) {
        AlgorithmIndividual[] r = new AlgorithmIndividual[2];
        father.addEntry("father [" + (TriGen.getInstance().getOngoingGenerationIndex() + 1) + "]");
        mother.addEntry("mother [" + (TriGen.getInstance().getOngoingGenerationIndex() + 1) + "]");
        int[] aux = this.getCrossoverIndexes(father, mother);
        int indGenesPadre = aux[0];
        int indGenesMadre = aux[1];
        int indCondicionesPadre = aux[2];
        int indCondicionesMadre = aux[3];
        int indTiemposPadre = aux[4];
        int indTiemposMadre = aux[5];
        Collection<Integer> genesHijo = this.buildComponent(father, mother, indGenesPadre, indGenesMadre, 1, 1);
        Collection<Integer> genesHija = this.buildComponent(father, mother, indGenesPadre, indGenesMadre, 1, 2);
        Collection<Integer> condicionesHijo = this.buildComponent(father, mother, indCondicionesPadre, indCondicionesMadre, 2, 1);
        Collection<Integer> condicionesHija = this.buildComponent(father, mother, indCondicionesPadre, indCondicionesMadre, 2, 2);
        Collection<Integer> tiemposHijo = this.buildComponent(father, mother, indTiemposPadre, indTiemposMadre, 3, 1);
        Collection<Integer> tiemposHija = this.buildComponent(father, mother, indTiemposPadre, indTiemposMadre, 3, 2);
        AlgorithmIndividual hijo = this.buildIndividual(genesHijo, condicionesHijo, tiemposHijo, individualClassName);
        AlgorithmIndividual hija = this.buildIndividual(genesHija, condicionesHija, tiemposHija, individualClassName);
        r[0] = hijo;
        r[1] = hija;
        return r;
    }

    private AlgorithmIndividual buildIndividual(Collection<Integer> g, Collection<Integer> c2, Collection<Integer> t, String individualClassName) {
        AlgorithmIndividual individuo = null;
        try {
            individuo = (AlgorithmIndividual)Class.forName(individualClassName).newInstance();
            individuo.initialize(g, c2, t);
            individuo.addEntry("from crossover [" + (TriGen.getInstance().getOngoingGenerationIndex() + 1) + "]");
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

    private Collection<Integer> buildComponent(AlgorithmIndividual father, AlgorithmIndividual mother, int fatherIndex, int motherIndex, int componentType, int descendantType) {
        Integer n;
        int v;
        int j;
        AlgorithmConfiguration PARAM = AlgorithmConfiguration.getInstance();
        AlgorithmDataset DATOS = PARAM.getData();
        AlgorithmRandomUtilities ALEATORIOS = AlgorithmRandomUtilities.getInstance();
        LinkedList<Integer> componente = new LinkedList<Integer>();
        int minimo = 0;
        int maximo = 0;
        if (componentType == 1) {
            minimo = PARAM.getMinG();
            maximo = PARAM.getMaxG();
        } else if (componentType == 2) {
            minimo = PARAM.getMinC();
            maximo = PARAM.getMaxC();
        } else if (componentType == 3) {
            minimo = PARAM.getMinT();
            maximo = PARAM.getMaxT();
        }
        int infPadre = 0;
        int supPadre = 0;
        int infMadre = 0;
        int supMadre = 0;
        if (descendantType == 1) {
            infPadre = 0;
            supPadre = fatherIndex;
            infMadre = motherIndex;
            if (componentType == 1) {
                supMadre = mother.getGeneSize();
            } else if (componentType == 2) {
                supMadre = mother.getSampleSize();
            } else if (componentType == 3) {
                supMadre = mother.getTimeSize();
            }
        } else if (descendantType == 2) {
            infPadre = fatherIndex;
            if (componentType == 1) {
                supPadre = father.getGeneSize();
            } else if (componentType == 2) {
                supPadre = father.getSampleSize();
            } else if (componentType == 3) {
                supPadre = father.getTimeSize();
            }
            infMadre = 0;
            supMadre = motherIndex;
        }
        for (j = infPadre; j < supPadre; ++j) {
            v = 0;
            if (componentType == 1) {
                v = father.getGene(j);
            } else if (componentType == 2) {
                v = father.getSample(j);
            } else if (componentType == 3) {
                v = father.getTime(j);
            }
            n = new Integer(v);
            if (componente.contains(n)) continue;
            componente.add(n);
        }
        for (j = infMadre; j < supMadre; ++j) {
            v = 0;
            if (componentType == 1) {
                v = mother.getGene(j);
            } else if (componentType == 2) {
                v = mother.getSample(j);
            } else if (componentType == 3) {
                v = mother.getTime(j);
            }
            n = new Integer(v);
            if (componente.contains(n)) continue;
            componente.add(n);
        }
        int difMin = minimo - componente.size();
        int difMax = componente.size() - maximo;
        if (difMin > 0) {
            int tam = 0;
            if (componentType == 1) {
                tam = DATOS.getGenSize();
            } else if (componentType == 2) {
                tam = DATOS.getSampleSize();
            } else if (componentType == 3) {
                tam = DATOS.getTimeSize();
            }
            ALEATORIOS.newBag(tam);
            for (int i = 0; i < difMin; ++i) {
                int bola = ALEATORIOS.extractAmarble();
                Integer coordenada = new Integer(bola);
                while (componente.contains(coordenada)) {
                    bola = ALEATORIOS.extractAmarble();
                    coordenada = new Integer(bola);
                }
                componente.add(coordenada);
            }
        }
        if (difMax > 0) {
            ALEATORIOS.newBag(componente.size());
            for (int i = 0; i < difMax; ++i) {
                int bola = ALEATORIOS.extractAmarble();
                componente.remove((Object)bola);
            }
        }
        return componente;
    }

    private int[] getCrossoverIndexes(AlgorithmIndividual father, AlgorithmIndividual mother) {
        int[] r = new int[6];
        int[] g = this.getIndexesFromComponent(father.getGenes(), mother.getGenes(), 1);
        int[] c2 = this.getIndexesFromComponent(father.getSamples(), mother.getSamples(), 2);
        int[] t = this.getIndexesFromComponent(father.getTimes(), mother.getTimes(), 3);
        r[0] = g[0];
        r[1] = g[1];
        r[2] = c2[0];
        r[3] = c2[1];
        r[4] = t[0];
        r[5] = t[1];
        return r;
    }

    private int[] getIndexesFromComponent(Collection<Integer> fatherComponent, Collection<Integer> motherComponent, int componentType) {
        AlgorithmConfiguration PARAM = AlgorithmConfiguration.getInstance();
        AlgorithmRandomUtilities ALEATORIOS = AlgorithmRandomUtilities.getInstance();
        int[] res = new int[2];
        int minimo = 0;
        if (componentType == 1) {
            minimo = PARAM.getMinG();
        } else if (componentType == 2) {
            minimo = PARAM.getMinC();
        } else if (componentType == 3) {
            minimo = PARAM.getMinT();
        }
        int indicePadre = 0;
        int indiceMadre = 0;
        int tamPadre = fatherComponent.size();
        int tamMadre = motherComponent.size();
        if (minimo != 1) {
            if (tamPadre == tamMadre) {
                double porcentaje = ALEATORIOS.getPercentage();
                indicePadre = (int)(porcentaje * (double)tamPadre);
                indiceMadre = (int)(porcentaje * (double)tamMadre);
                res[0] = indicePadre;
                res[1] = indiceMadre;
            } else {
                int tamHija;
                int tamHijo;
                double porcentaje = ALEATORIOS.getPercentage();
                boolean correcto = true;
                if (porcentaje != 0.0) {
                    indicePadre = (int)(porcentaje * (double)tamPadre);
                    indiceMadre = (int)(porcentaje * (double)tamMadre);
                    tamHijo = indicePadre + (tamMadre - indiceMadre);
                    tamHija = tamPadre - indicePadre + indiceMadre;
                    if (tamHijo < minimo || tamHija < minimo) {
                        correcto = false;
                    }
                }
                while (porcentaje == 0.0 || !correcto) {
                    porcentaje = ALEATORIOS.getPercentage();
                    correcto = true;
                    if (porcentaje == 0.0) continue;
                    indicePadre = (int)(porcentaje * (double)tamPadre);
                    indiceMadre = (int)(porcentaje * (double)tamMadre);
                    tamHijo = indicePadre + (tamMadre - indiceMadre);
                    tamHija = tamPadre - indicePadre + indiceMadre;
                    if (tamHijo >= minimo && tamHija >= minimo) continue;
                    correcto = false;
                }
                res[0] = indicePadre;
                res[1] = indiceMadre;
            }
        } else {
            double porcentaje = ALEATORIOS.getPercentage();
            indicePadre = (int)(porcentaje * (double)tamPadre);
            indiceMadre = (int)(porcentaje * (double)tamMadre);
            res[0] = indicePadre;
            res[1] = indiceMadre;
        }
        return res;
    }
}

