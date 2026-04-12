/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.ode.nonstiff;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayFieldVector;
import org.apache.commons.math3.linear.FieldDecompositionSolver;
import org.apache.commons.math3.linear.FieldLUDecomposition;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class AdamsNordsieckTransformer {
    private static final Map<Integer, AdamsNordsieckTransformer> CACHE = new HashMap<Integer, AdamsNordsieckTransformer>();
    private final Array2DRowRealMatrix update;
    private final double[] c1;

    private AdamsNordsieckTransformer(int nSteps) {
        FieldMatrix<BigFraction> bigP = this.buildP(nSteps);
        FieldDecompositionSolver<BigFraction> pSolver = new FieldLUDecomposition<BigFraction>(bigP).getSolver();
        Object[] u = new BigFraction[nSteps];
        Arrays.fill(u, BigFraction.ONE);
        BigFraction[] bigC1 = (BigFraction[])pSolver.solve(new ArrayFieldVector((FieldElement[])u, false)).toArray();
        FieldElement[][] shiftedP = (BigFraction[][])bigP.getData();
        for (int i = shiftedP.length - 1; i > 0; --i) {
            shiftedP[i] = shiftedP[i - 1];
        }
        shiftedP[0] = new BigFraction[nSteps];
        Arrays.fill(shiftedP[0], BigFraction.ZERO);
        FieldMatrix<BigFraction> bigMSupdate = pSolver.solve(new Array2DRowFieldMatrix(shiftedP, false));
        this.update = MatrixUtils.bigFractionMatrixToRealMatrix(bigMSupdate);
        this.c1 = new double[nSteps];
        for (int i = 0; i < nSteps; ++i) {
            this.c1[i] = bigC1[i].doubleValue();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static AdamsNordsieckTransformer getInstance(int nSteps) {
        Map<Integer, AdamsNordsieckTransformer> map = CACHE;
        synchronized (map) {
            AdamsNordsieckTransformer t = CACHE.get(nSteps);
            if (t == null) {
                t = new AdamsNordsieckTransformer(nSteps);
                CACHE.put(nSteps, t);
            }
            return t;
        }
    }

    public int getNSteps() {
        return this.c1.length;
    }

    private FieldMatrix<BigFraction> buildP(int nSteps) {
        FieldElement[][] pData = new BigFraction[nSteps][nSteps];
        for (int i = 0; i < pData.length; ++i) {
            int factor;
            BigFraction[] pI = pData[i];
            int aj = factor = -(i + 1);
            for (int j = 0; j < pI.length; ++j) {
                pI[j] = new BigFraction(aj * (j + 2));
                aj *= factor;
            }
        }
        return new Array2DRowFieldMatrix(pData, false);
    }

    public Array2DRowRealMatrix initializeHighOrderDerivatives(double h, double[] t, double[][] y, double[][] yDot) {
        double[][] a2 = new double[2 * (y.length - 1)][this.c1.length];
        double[][] b2 = new double[2 * (y.length - 1)][y[0].length];
        double[] y0 = y[0];
        double[] yDot0 = yDot[0];
        for (int i = 1; i < y.length; ++i) {
            double di = t[i] - t[0];
            double ratio = di / h;
            double dikM1Ohk = 1.0 / h;
            double[] aI = a2[2 * i - 2];
            double[] aDotI = a2[2 * i - 1];
            for (int j = 0; j < aI.length; ++j) {
                aI[j] = di * (dikM1Ohk *= ratio);
                aDotI[j] = (double)(j + 2) * dikM1Ohk;
            }
            double[] yI = y[i];
            double[] yDotI = yDot[i];
            double[] bI = b2[2 * i - 2];
            double[] bDotI = b2[2 * i - 1];
            for (int j = 0; j < yI.length; ++j) {
                bI[j] = yI[j] - y0[j] - di * yDot0[j];
                bDotI[j] = yDotI[j] - yDot0[j];
            }
        }
        QRDecomposition decomposition = new QRDecomposition(new Array2DRowRealMatrix(a2, false));
        RealMatrix x = decomposition.getSolver().solve(new Array2DRowRealMatrix(b2, false));
        return new Array2DRowRealMatrix(x.getData(), false);
    }

    public Array2DRowRealMatrix updateHighOrderDerivativesPhase1(Array2DRowRealMatrix highOrder) {
        return this.update.multiply(highOrder);
    }

    public void updateHighOrderDerivativesPhase2(double[] start, double[] end, Array2DRowRealMatrix highOrder) {
        double[][] data = highOrder.getDataRef();
        for (int i = 0; i < data.length; ++i) {
            double[] dataI = data[i];
            double c1I = this.c1[i];
            for (int j = 0; j < dataI.length; ++j) {
                int n = j;
                dataI[n] = dataI[n] + c1I * (start[j] - end[j]);
            }
        }
    }
}

