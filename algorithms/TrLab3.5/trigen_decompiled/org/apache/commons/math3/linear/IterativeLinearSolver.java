/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.NonSquareOperatorException;
import org.apache.commons.math3.linear.RealLinearOperator;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.IterationManager;
import org.apache.commons.math3.util.MathUtils;

public abstract class IterativeLinearSolver {
    private final IterationManager manager;

    public IterativeLinearSolver(int maxIterations) {
        this.manager = new IterationManager(maxIterations);
    }

    public IterativeLinearSolver(IterationManager manager) throws NullArgumentException {
        MathUtils.checkNotNull(manager);
        this.manager = manager;
    }

    protected static void checkParameters(RealLinearOperator a2, RealVector b2, RealVector x0) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException {
        MathUtils.checkNotNull(a2);
        MathUtils.checkNotNull(b2);
        MathUtils.checkNotNull(x0);
        if (a2.getRowDimension() != a2.getColumnDimension()) {
            throw new NonSquareOperatorException(a2.getRowDimension(), a2.getColumnDimension());
        }
        if (b2.getDimension() != a2.getRowDimension()) {
            throw new DimensionMismatchException(b2.getDimension(), a2.getRowDimension());
        }
        if (x0.getDimension() != a2.getColumnDimension()) {
            throw new DimensionMismatchException(x0.getDimension(), a2.getColumnDimension());
        }
    }

    public IterationManager getIterationManager() {
        return this.manager;
    }

    public RealVector solve(RealLinearOperator a2, RealVector b2) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException {
        MathUtils.checkNotNull(a2);
        ArrayRealVector x = new ArrayRealVector(a2.getColumnDimension());
        ((RealVector)x).set(0.0);
        return this.solveInPlace(a2, b2, x);
    }

    public RealVector solve(RealLinearOperator a2, RealVector b2, RealVector x0) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException {
        MathUtils.checkNotNull(x0);
        return this.solveInPlace(a2, b2, x0.copy());
    }

    public abstract RealVector solveInPlace(RealLinearOperator var1, RealVector var2, RealVector var3) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException;
}

