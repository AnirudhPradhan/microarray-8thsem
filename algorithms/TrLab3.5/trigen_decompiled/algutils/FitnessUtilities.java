/*
 * Decompiled with CFR 0.152.
 */
package algutils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.stat.StatUtils;

public class FitnessUtilities {
    private static FitnessUtilities singleton = new FitnessUtilities();

    private FitnessUtilities() {
    }

    public static FitnessUtilities getInstance() {
        return singleton;
    }

    public double[] meanOfViews(List<Double> gCdifferences, List<Double> gTdifferences, List<Double> tGdifferences) {
        double[] res = new double[]{StatUtils.mean(this.fromListToArray(gCdifferences)), StatUtils.mean(this.fromListToArray(gTdifferences)), StatUtils.mean(this.fromListToArray(tGdifferences))};
        return res;
    }

    private double[] fromListToArray(List<Double> listOfDouble) {
        double[] res = new double[listOfDouble.size()];
        Iterator<Double> it = listOfDouble.iterator();
        int i = 0;
        while (it.hasNext()) {
            res[i] = it.next();
            ++i;
        }
        return res;
    }

    public double[][][] buildAnglesCubeT1(double[][][] transformation) {
        int ngraficos = transformation[0][0].length;
        int nseries = transformation.length;
        int nejex = transformation[0].length;
        double[][][] angulos = new double[nseries][ngraficos][nejex - 1];
        for (int grafico = 0; grafico < ngraficos; ++grafico) {
            for (int serie = 0; serie < nseries; ++serie) {
                for (int ejex = 0; ejex < nejex - 1; ++ejex) {
                    double rotacion;
                    double x1 = ejex;
                    double x2 = ejex + 1;
                    double y1 = 0.0;
                    double y2 = 0.0;
                    y1 = transformation[serie][ejex][grafico];
                    y2 = transformation[serie][ejex + 1][grafico];
                    double pendiente = (y2 - y1) / (x2 - x1);
                    double atan = Math.atan(pendiente);
                    angulos[serie][grafico][ejex] = rotacion = this.angleTransformationT1(atan);
                }
            }
        }
        return angulos;
    }

    private double angleTransformationT1(double angle) {
        double r = angle;
        if (angle < 0.0) {
            r = angle + Math.PI * 2;
        }
        return r;
    }

    public double[][][] buildAnglesCubeT2(double[][][] transformacion) {
        int ngraficos = transformacion[0][0].length;
        int nseries = transformacion.length;
        int nejex = transformacion[0].length;
        double[][][] angulos = new double[nseries][ngraficos][nejex - 1];
        for (int grafico = 0; grafico < ngraficos; ++grafico) {
            for (int serie = 0; serie < nseries; ++serie) {
                for (int ejex = 0; ejex < nejex - 1; ++ejex) {
                    double rotacion;
                    double x1 = ejex;
                    double x2 = ejex + 1;
                    double y1 = 0.0;
                    double y2 = 0.0;
                    y1 = transformacion[serie][ejex][grafico];
                    y2 = transformacion[serie][ejex + 1][grafico];
                    double pendiente = (y2 - y1) / (x2 - x1);
                    double atan = Math.atan(pendiente);
                    angulos[serie][grafico][ejex] = rotacion = this.angleTransformationT2(atan);
                }
            }
        }
        return angulos;
    }

    private double angleTransformationT2(double angle) {
        return Math.abs(angle);
    }

    public List<Double> buildTableOfDifferencies(double[][][] angles) {
        ArrayList<Double> res = new ArrayList<Double>();
        for (int i = 0; i < angles.length; ++i) {
            for (int j = 0; j < angles[i].length; ++j) {
                double dif = 0.0;
                for (int columna = j + 1; columna < angles[i].length; ++columna) {
                    dif = this.computeDifference(angles, i, j, i, columna);
                    res.add(new Double(dif));
                }
                for (int fila = i + 1; fila < angles.length; ++fila) {
                    dif = this.computeDifference(angles, i, j, fila, j);
                    res.add(new Double(dif));
                }
            }
        }
        return res;
    }

    public double[][] buildTableOfAngles(double[][][] transformation) {
        int ngraficos = transformation[0][0].length;
        int nseries = transformation.length;
        int nejex = transformation[0].length;
        double sumX = 0.0;
        double sumXX = 0.0;
        for (int i = 0; i < nejex; ++i) {
            sumX += (double)(i + 1);
            sumXX += (double)((i + 1) * (i + 1));
        }
        double[][] angulos = new double[nseries][ngraficos];
        for (int grafico = 0; grafico < ngraficos; ++grafico) {
            for (int serie = 0; serie < nseries; ++serie) {
                double rotacion;
                double mrmc = this.computeSlope(serie, grafico, transformation, sumX, sumXX);
                double atan = Math.atan(mrmc);
                angulos[serie][grafico] = rotacion = this.angleTransformationT1(atan);
            }
        }
        return angulos;
    }

    public double[][] buildTableOfAnglesT2(double[][][] transformation) {
        int ngraficos = transformation[0][0].length;
        int nseries = transformation.length;
        int nejex = transformation[0].length;
        double sumX = 0.0;
        double sumXX = 0.0;
        for (int i = 0; i < nejex; ++i) {
            sumX += (double)(i + 1);
            sumXX += (double)((i + 1) * (i + 1));
        }
        double[][] angulos = new double[nseries][ngraficos];
        for (int grafico = 0; grafico < ngraficos; ++grafico) {
            for (int serie = 0; serie < nseries; ++serie) {
                double rotacion;
                double mrmc = this.computeSlope(serie, grafico, transformation, sumX, sumXX);
                double atan = Math.atan(mrmc);
                angulos[serie][grafico] = rotacion = this.angleTransformationT2(atan);
            }
        }
        return angulos;
    }

    public List<Double> buildTableOfDifferencies(double[][] angles) {
        ArrayList<Double> res = new ArrayList<Double>();
        for (int i = 0; i < angles.length; ++i) {
            for (int j = 0; j < angles[i].length; ++j) {
                double anguloB;
                double anguloA = angles[i][j];
                double dif = 0.0;
                for (int fila = i + 1; fila < angles.length; ++fila) {
                    anguloB = angles[fila][j];
                    dif = Math.max(anguloA, anguloB) - Math.min(anguloA, anguloB);
                    res.add(new Double(dif));
                }
                for (int columna = j + 1; columna < angles[i].length; ++columna) {
                    anguloB = angles[i][columna];
                    dif = Math.max(anguloA, anguloB) - Math.min(anguloA, anguloB);
                    res.add(new Double(dif));
                }
            }
        }
        return res;
    }

    public double[] getTotalsArray(List<Double> gCdifferencies, List<Double> gTdifferencies, List<Double> tGdifferencies) {
        ArrayList<Double> completo = new ArrayList<Double>();
        completo.addAll(gCdifferencies);
        completo.addAll(gTdifferencies);
        completo.addAll(tGdifferencies);
        double[] res = new double[completo.size()];
        Iterator itcompleto = completo.iterator();
        int i = 0;
        while (itcompleto.hasNext()) {
            Double v = (Double)itcompleto.next();
            res[i] = v;
            ++i;
        }
        return res;
    }

    private double computeSlope(int series, int graphic, double[][][] transformation, double xSum, double xxSum) {
        double res = 0.0;
        int nejex = transformation[0].length;
        double n = nejex;
        double sumXY = 0.0;
        double sumY = 0.0;
        int x = 1;
        for (int ejex = 0; ejex < nejex; ++ejex) {
            sumY += transformation[series][ejex][graphic];
            sumXY += (double)x * transformation[series][ejex][graphic];
            ++x;
        }
        res = (n * sumXY - xSum * sumY) / (n * xxSum - xSum * xSum);
        return res;
    }

    private double computeDifference(double[][][] angles, int aRow, int aColumn, int bRow, int bColumn) {
        double res = 0.0;
        double[] diferencias = new double[angles[0][0].length];
        for (int i = 0; i < angles[0][0].length; ++i) {
            double max = Math.max(angles[aRow][aColumn][i], angles[bRow][bColumn][i]);
            double min = Math.min(angles[aRow][aColumn][i], angles[bRow][bColumn][i]);
            diferencias[i] = max - min;
        }
        res = StatUtils.mean(diferencias);
        return res;
    }

    public double applyAtanRotation(double valor) {
        double atan = Math.atan(valor);
        if (atan < 0.0) {
            atan += Math.PI * 2;
        }
        return atan;
    }
}

