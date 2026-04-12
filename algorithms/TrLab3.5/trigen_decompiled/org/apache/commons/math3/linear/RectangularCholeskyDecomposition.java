/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.linear;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;

public class RectangularCholeskyDecomposition {
    private final RealMatrix root;
    private int rank;

    public RectangularCholeskyDecomposition(RealMatrix matrix) throws NonPositiveDefiniteMatrixException {
        this(matrix, 0.0);
    }

    public RectangularCholeskyDecomposition(RealMatrix matrix, double small) throws NonPositiveDefiniteMatrixException {
        int order = matrix.getRowDimension();
        double[][] c2 = matrix.getData();
        double[][] b2 = new double[order][order];
        int[] index = new int[order];
        for (int i = 0; i < order; ++i) {
            index[i] = i;
        }
        int r = 0;
        boolean loop = true;
        while (loop) {
            double sqrt;
            int ir;
            int swapR = r;
            for (int i = r + 1; i < order; ++i) {
                int ii = index[i];
                int isr = index[swapR];
                if (!(c2[ii][ii] > c2[isr][isr])) continue;
                swapR = i;
            }
            if (swapR != r) {
                int tmpIndex = index[r];
                index[r] = index[swapR];
                index[swapR] = tmpIndex;
                double[] tmpRow = b2[r];
                b2[r] = b2[swapR];
                b2[swapR] = tmpRow;
            }
            if (c2[ir = index[r]][ir] <= small) {
                if (r == 0) {
                    throw new NonPositiveDefiniteMatrixException(c2[ir][ir], ir, small);
                }
                for (int i = r; i < order; ++i) {
                    if (!(c2[index[i]][index[i]] < -small)) continue;
                    throw new NonPositiveDefiniteMatrixException(c2[index[i]][index[i]], i, small);
                }
                loop = false;
                continue;
            }
            b2[r][r] = sqrt = FastMath.sqrt(c2[ir][ir]);
            double inverse = 1.0 / sqrt;
            double inverse2 = 1.0 / c2[ir][ir];
            for (int i = r + 1; i < order; ++i) {
                double e;
                int ii = index[i];
                b2[i][r] = e = inverse * c2[ii][ir];
                double[] dArray = c2[ii];
                int n = ii;
                dArray[n] = dArray[n] - c2[ii][ir] * c2[ii][ir] * inverse2;
                for (int j = r + 1; j < i; ++j) {
                    double f;
                    int ij = index[j];
                    c2[ii][ij] = f = c2[ii][ij] - e * b2[j][r];
                    c2[ij][ii] = f;
                }
            }
            loop = ++r < order;
        }
        this.rank = r;
        this.root = MatrixUtils.createRealMatrix(order, r);
        for (int i = 0; i < order; ++i) {
            for (int j = 0; j < r; ++j) {
                this.root.setEntry(index[i], j, b2[i][j]);
            }
        }
    }

    public RealMatrix getRootMatrix() {
        return this.root;
    }

    public int getRank() {
        return this.rank;
    }
}

