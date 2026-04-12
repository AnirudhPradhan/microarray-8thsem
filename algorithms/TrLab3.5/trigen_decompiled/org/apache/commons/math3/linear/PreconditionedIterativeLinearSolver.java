/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.IterativeLinearSolver;
import org.apache.commons.math3.linear.NonSquareOperatorException;
import org.apache.commons.math3.linear.RealLinearOperator;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.IterationManager;
import org.apache.commons.math3.util.MathUtils;

public abstract class PreconditionedIterativeLinearSolver
extends IterativeLinearSolver {
    public PreconditionedIterativeLinearSolver(int maxIterations) {
        super(maxIterations);
    }

    public PreconditionedIterativeLinearSolver(IterationManager manager) throws NullArgumentException {
        super(manager);
    }

    public RealVector solve(RealLinearOperator a2, RealLinearOperator m, RealVector b2, RealVector x0) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException {
        MathUtils.checkNotNull(x0);
        return this.solveInPlace(a2, m, b2, x0.copy());
    }

    public RealVector solve(RealLinearOperator a2, RealVector b2) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException {
        MathUtils.checkNotNull(a2);
        ArrayRealVector x = new ArrayRealVector(a2.getColumnDimension());
        ((RealVector)x).set(0.0);
        return this.solveInPlace(a2, null, b2, x);
    }

    public RealVector solve(RealLinearOperator a2, RealVector b2, RealVector x0) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException {
        MathUtils.checkNotNull(x0);
        return this.solveInPlace(a2, null, b2, x0.copy());
    }

    protected static void checkParameters(RealLinearOperator a2, RealLinearOperator m, RealVector b2, RealVector x0) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException {
        PreconditionedIterativeLinearSolver.checkParameters(a2, b2, x0);
        if (m != null) {
            if (m.getColumnDimension() != m.getRowDimension()) {
                throw new NonSquareOperatorException(m.getColumnDimension(), m.getRowDimension());
            }
            if (m.getRowDimension() != a2.getRowDimension()) {
                throw new DimensionMismatchException(m.getRowDimension(), a2.getRowDimension());
            }
        }
    }

    public RealVector solve(RealLinearOperator a2, RealLinearOperator m, RealVector b2) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException {
        MathUtils.checkNotNull(a2);
        ArrayRealVector x = new ArrayRealVector(a2.getColumnDimension());
        return this.solveInPlace(a2, m, b2, x);
    }

    public abstract RealVector solveInPlace(RealLinearOperator var1, RealLinearOperator var2, RealVector var3, RealVector var4) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException;

    public RealVector solveInPlace(RealLinearOperator a2, RealVector b2, RealVector x0) throws NullArgumentException, NonSquareOperatorException, DimensionMismatchException, MaxCountExceededException {
        return this.solveInPlace(a2, null, b2, x0);
    }
}

