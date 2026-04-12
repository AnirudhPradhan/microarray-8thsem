/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.fitting.leastsquares;

import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.fitting.leastsquares.AbstractEvaluation;
import org.apache.commons.math3.fitting.leastsquares.DenseWeightedEvaluation;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresAdapter;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.fitting.leastsquares.MultivariateJacobianFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.AbstractOptimizationProblem;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.PointVectorValuePair;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Incrementor;
import org.apache.commons.math3.util.Pair;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class LeastSquaresFactory {
    private LeastSquaresFactory() {
    }

    public static LeastSquaresProblem create(MultivariateJacobianFunction model, RealVector observed, RealVector start, ConvergenceChecker<LeastSquaresProblem.Evaluation> checker, int maxEvaluations, int maxIterations) {
        return new LocalLeastSquaresProblem(model, observed, start, checker, maxEvaluations, maxIterations);
    }

    public static LeastSquaresProblem create(MultivariateJacobianFunction model, RealVector observed, RealVector start, RealMatrix weight, ConvergenceChecker<LeastSquaresProblem.Evaluation> checker, int maxEvaluations, int maxIterations) {
        return LeastSquaresFactory.weightMatrix(LeastSquaresFactory.create(model, observed, start, checker, maxEvaluations, maxIterations), weight);
    }

    public static LeastSquaresProblem create(MultivariateVectorFunction model, MultivariateMatrixFunction jacobian, double[] observed, double[] start, RealMatrix weight, ConvergenceChecker<LeastSquaresProblem.Evaluation> checker, int maxEvaluations, int maxIterations) {
        return LeastSquaresFactory.create(LeastSquaresFactory.model(model, jacobian), new ArrayRealVector(observed, false), new ArrayRealVector(start, false), weight, checker, maxEvaluations, maxIterations);
    }

    public static LeastSquaresProblem weightMatrix(LeastSquaresProblem problem, RealMatrix weights) {
        final RealMatrix weightSquareRoot = LeastSquaresFactory.squareRoot(weights);
        return new LeastSquaresAdapter(problem){

            public LeastSquaresProblem.Evaluation evaluate(RealVector point) {
                return new DenseWeightedEvaluation(super.evaluate(point), weightSquareRoot);
            }
        };
    }

    public static LeastSquaresProblem weightDiagonal(LeastSquaresProblem problem, RealVector weights) {
        return LeastSquaresFactory.weightMatrix(problem, new DiagonalMatrix(weights.toArray()));
    }

    public static LeastSquaresProblem countEvaluations(LeastSquaresProblem problem, final Incrementor counter) {
        return new LeastSquaresAdapter(problem){

            public LeastSquaresProblem.Evaluation evaluate(RealVector point) {
                counter.incrementCount();
                return super.evaluate(point);
            }
        };
    }

    public static ConvergenceChecker<LeastSquaresProblem.Evaluation> evaluationChecker(final ConvergenceChecker<PointVectorValuePair> checker) {
        return new ConvergenceChecker<LeastSquaresProblem.Evaluation>(){

            @Override
            public boolean converged(int iteration, LeastSquaresProblem.Evaluation previous, LeastSquaresProblem.Evaluation current) {
                return checker.converged(iteration, new PointVectorValuePair(previous.getPoint().toArray(), previous.getResiduals().toArray(), false), new PointVectorValuePair(current.getPoint().toArray(), current.getResiduals().toArray(), false));
            }
        };
    }

    private static RealMatrix squareRoot(RealMatrix m) {
        if (m instanceof DiagonalMatrix) {
            int dim = m.getRowDimension();
            DiagonalMatrix sqrtM = new DiagonalMatrix(dim);
            for (int i = 0; i < dim; ++i) {
                sqrtM.setEntry(i, i, FastMath.sqrt(m.getEntry(i, i)));
            }
            return sqrtM;
        }
        EigenDecomposition dec = new EigenDecomposition(m);
        return dec.getSquareRoot();
    }

    public static MultivariateJacobianFunction model(final MultivariateVectorFunction value, final MultivariateMatrixFunction jacobian) {
        return new MultivariateJacobianFunction(){

            @Override
            public Pair<RealVector, RealMatrix> value(RealVector point) {
                double[] pointArray = point.toArray();
                return new Pair<RealVector, RealMatrix>(new ArrayRealVector(value.value(pointArray), false), new Array2DRowRealMatrix(jacobian.value(pointArray), false));
            }
        };
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class LocalLeastSquaresProblem
    extends AbstractOptimizationProblem<LeastSquaresProblem.Evaluation>
    implements LeastSquaresProblem {
        private RealVector target;
        private MultivariateJacobianFunction model;
        private RealVector start;

        LocalLeastSquaresProblem(MultivariateJacobianFunction model, RealVector target, RealVector start, ConvergenceChecker<LeastSquaresProblem.Evaluation> checker, int maxEvaluations, int maxIterations) {
            super(maxEvaluations, maxIterations, checker);
            this.target = target;
            this.model = model;
            this.start = start;
        }

        @Override
        public int getObservationSize() {
            return this.target.getDimension();
        }

        @Override
        public int getParameterSize() {
            return this.start.getDimension();
        }

        @Override
        public RealVector getStart() {
            return this.start == null ? null : this.start.copy();
        }

        @Override
        public LeastSquaresProblem.Evaluation evaluate(RealVector point) {
            Pair<RealVector, RealMatrix> value = this.model.value(point);
            return new UnweightedEvaluation(value.getFirst(), value.getSecond(), this.target, point.copy());
        }

        private static class UnweightedEvaluation
        extends AbstractEvaluation {
            private final RealVector point;
            private final RealMatrix jacobian;
            private final RealVector residuals;

            private UnweightedEvaluation(RealVector values, RealMatrix jacobian, RealVector target, RealVector point) {
                super(target.getDimension());
                this.jacobian = jacobian;
                this.point = point;
                this.residuals = target.subtract(values);
            }

            public RealMatrix getJacobian() {
                return this.jacobian;
            }

            public RealVector getPoint() {
                return this.point;
            }

            public RealVector getResiduals() {
                return this.residuals;
            }
        }
    }
}

