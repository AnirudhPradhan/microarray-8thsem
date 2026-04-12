/*
 * Decompiled with CFR 0.152.
 */
package strfitness;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmDataset;
import algcore.AlgorithmIndividual;
import fitnessfunctions.FitnessStrategy;
import java.util.Collection;
import java.util.Iterator;

public class StrMsr3d
implements FitnessStrategy {
    @Override
    public double calculate(AlgorithmIndividual individual) {
        double residuo = 0.0;
        int t = individual.getTimeSize();
        int g = individual.getGeneSize();
        int c2 = individual.getSampleSize();
        residuo = this.partialResidue(individual) / (double)(t * g * c2);
        return residuo;
    }

    private double partialResidue(AlgorithmIndividual individual) {
        double rp = 0.0;
        Collection<Integer> lt = individual.getTimes();
        Collection<Integer> lg = individual.getGenes();
        Collection<Integer> lc = individual.getSamples();
        double[][][] valores = this.buildArrayOfValues(lt, lg, lc);
        Auxiliar aux1 = this.buildMeansVerticalPlanesRowsAndTotal(valores, lt.size(), lg.size(), lc.size());
        double[] medias_planos_verticales = aux1.planos;
        double[][] medias_filas = aux1.segmentos;
        double media_total = aux1.total;
        Auxiliar aux2 = this.buildMeansHorizontalPlanesAndSteps(valores, lt.size(), lg.size(), lc.size());
        double[] medias_planos_horizontales = aux2.planos;
        double[][] medias_travesanyos = aux2.segmentos;
        Auxiliar aux3 = this.buildMeansTransversePlanesAndColumns(valores, lt.size(), lg.size(), lc.size());
        double[] medias_planos_transversales = aux3.planos;
        double[][] medias_columnas = aux3.segmentos;
        for (int t = 0; t < lt.size(); ++t) {
            for (int g = 0; g < lg.size(); ++g) {
                for (int c2 = 0; c2 < lc.size(); ++c2) {
                    double aux = valores[t][g][c2] + medias_planos_horizontales[g] + medias_planos_transversales[c2] + medias_planos_verticales[t] - medias_travesanyos[g][c2] - medias_filas[t][g] - medias_columnas[t][c2] - media_total;
                    rp += aux * aux;
                }
            }
        }
        return rp;
    }

    private double[][][] buildArrayOfValues(Collection<Integer> lt, Collection<Integer> lg, Collection<Integer> lc) {
        AlgorithmDataset DATOS = AlgorithmConfiguration.getInstance().getData();
        double[][][] aux = new double[lt.size()][lg.size()][lc.size()];
        int i = 0;
        int j = 0;
        int k = 0;
        for (int vi : lt) {
            Iterator<Integer> i2 = lg.iterator();
            j = 0;
            while (i2.hasNext()) {
                int vj = i2.next();
                Iterator<Integer> i3 = lc.iterator();
                k = 0;
                while (i3.hasNext()) {
                    int vk = i3.next();
                    aux[i][j][k] = DATOS.getValue(vj, vk, vi);
                    ++k;
                }
                ++j;
            }
            ++i;
        }
        return aux;
    }

    private Auxiliar buildMeansVerticalPlanesRowsAndTotal(double[][][] values, int nTimes, int nGenes, int nSamples) {
        double media_total = 0.0;
        double[] medias_plano = new double[nTimes];
        double[][] medias_segmento = new double[nTimes][nGenes];
        int denominador_plano = nGenes * nSamples;
        int denominador_segmento = nSamples;
        for (int t = 0; t < medias_plano.length; ++t) {
            double suma_plano = 0.0;
            for (int i = 0; i < nGenes; ++i) {
                double suma_segmento = 0.0;
                for (int j = 0; j < nSamples; ++j) {
                    suma_plano += values[t][i][j];
                    suma_segmento += values[t][i][j];
                }
                medias_segmento[t][i] = suma_segmento / (double)denominador_segmento;
            }
            media_total += suma_plano;
            medias_plano[t] = suma_plano / (double)denominador_plano;
        }
        return new Auxiliar(medias_plano, medias_segmento, media_total /= (double)(nTimes * nGenes * nSamples));
    }

    private Auxiliar buildMeansHorizontalPlanesAndSteps(double[][][] values, int nTimes, int nGenes, int nSamples) {
        double[] medias_plano = new double[nGenes];
        double[][] medias_segmento = new double[nGenes][nSamples];
        int denominador_plano = nTimes * nSamples;
        int denominador_segmento = nTimes;
        for (int g = 0; g < medias_plano.length; ++g) {
            double suma_plano = 0.0;
            for (int i = 0; i < nSamples; ++i) {
                double suma_segmento = 0.0;
                for (int j = 0; j < nTimes; ++j) {
                    suma_plano += values[j][g][i];
                    suma_segmento += values[j][g][i];
                }
                medias_segmento[g][i] = suma_segmento / (double)denominador_segmento;
            }
            medias_plano[g] = suma_plano / (double)denominador_plano;
        }
        return new Auxiliar(medias_plano, medias_segmento, 0.0);
    }

    private Auxiliar buildMeansTransversePlanesAndColumns(double[][][] values, int nTimes, int nGenes, int nSamples) {
        double[] medias_plano = new double[nSamples];
        double[][] medias_segmento = new double[nTimes][nSamples];
        int denominador_plano = nGenes * nTimes;
        int denominador_segmento = nGenes;
        for (int c2 = 0; c2 < medias_plano.length; ++c2) {
            double suma_plano = 0.0;
            for (int i = 0; i < nTimes; ++i) {
                double suma_segmento = 0.0;
                for (int j = 0; j < nGenes; ++j) {
                    suma_plano += values[i][j][c2];
                    suma_segmento += values[i][j][c2];
                }
                medias_segmento[i][c2] = suma_segmento / (double)denominador_segmento;
            }
            medias_plano[c2] = suma_plano / (double)denominador_plano;
        }
        return new Auxiliar(medias_plano, medias_segmento, 0.0);
    }

    private class Auxiliar {
        public double[] planos;
        public double[][] segmentos;
        public double total;

        public Auxiliar(double[] planes, double[][] segments, double total) {
            this.planos = planes;
            this.segmentos = segments;
            this.total = total;
        }
    }
}

