/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.AdamsIntegrator;
import org.apache.commons.math3.ode.sampling.NordsieckStepInterpolator;
import org.apache.commons.math3.util.FastMath;

public class AdamsBashforthIntegrator
extends AdamsIntegrator {
    private static final String METHOD_NAME = "Adams-Bashforth";

    public AdamsBashforthIntegrator(int nSteps, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) throws NumberIsTooSmallException {
        super(METHOD_NAME, nSteps, nSteps, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public AdamsBashforthIntegrator(int nSteps, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) throws IllegalArgumentException {
        super(METHOD_NAME, nSteps, nSteps, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        this.sanityChecks(equations, t);
        this.setEquations(equations);
        boolean forward = t > equations.getTime();
        double[] y0 = equations.getCompleteState();
        double[] y = (double[])y0.clone();
        double[] yDot = new double[y.length];
        NordsieckStepInterpolator interpolator = new NordsieckStepInterpolator();
        interpolator.reinitialize(y, forward, equations.getPrimaryMapper(), equations.getSecondaryMappers());
        this.initIntegration(equations.getTime(), y0, t);
        this.start(equations.getTime(), y, t);
        interpolator.reinitialize(this.stepStart, this.stepSize, this.scaled, this.nordsieck);
        interpolator.storeTime(this.stepStart);
        int lastRow = this.nordsieck.getRowDimension() - 1;
        double hNew = this.stepSize;
        interpolator.rescale(hNew);
        this.isLastStep = false;
        do {
            boolean filteredNextIsLast;
            double error = 10.0;
            while (error >= 1.0) {
                this.stepSize = hNew;
                error = 0.0;
                for (int i = 0; i < this.mainSetDimension; ++i) {
                    double yScale = FastMath.abs(y[i]);
                    double tol = this.vecAbsoluteTolerance == null ? this.scalAbsoluteTolerance + this.scalRelativeTolerance * yScale : this.vecAbsoluteTolerance[i] + this.vecRelativeTolerance[i] * yScale;
                    double ratio = this.nordsieck.getEntry(lastRow, i) / tol;
                    error += ratio * ratio;
                }
                if (!((error = FastMath.sqrt(error / (double)this.mainSetDimension)) >= 1.0)) continue;
                double factor = this.computeStepGrowShrinkFactor(error);
                hNew = this.filterStep(this.stepSize * factor, forward, false);
                interpolator.rescale(hNew);
            }
            double stepEnd = this.stepStart + this.stepSize;
            interpolator.shift();
            interpolator.setInterpolatedTime(stepEnd);
            ExpandableStatefulODE expandable = this.getExpandable();
            EquationsMapper primary = expandable.getPrimaryMapper();
            primary.insertEquationData(interpolator.getInterpolatedState(), y);
            int index = 0;
            for (EquationsMapper secondary : expandable.getSecondaryMappers()) {
                secondary.insertEquationData(interpolator.getInterpolatedSecondaryState(index), y);
                ++index;
            }
            this.computeDerivatives(stepEnd, y, yDot);
            double[] predictedScaled = new double[y0.length];
            for (int j = 0; j < y0.length; ++j) {
                predictedScaled[j] = this.stepSize * yDot[j];
            }
            Array2DRowRealMatrix nordsieckTmp = this.updateHighOrderDerivativesPhase1(this.nordsieck);
            this.updateHighOrderDerivativesPhase2(this.scaled, predictedScaled, nordsieckTmp);
            interpolator.reinitialize(stepEnd, this.stepSize, predictedScaled, nordsieckTmp);
            interpolator.storeTime(stepEnd);
            this.stepStart = this.acceptStep(interpolator, y, yDot, t);
            this.scaled = predictedScaled;
            this.nordsieck = nordsieckTmp;
            interpolator.reinitialize(stepEnd, this.stepSize, this.scaled, this.nordsieck);
            if (this.isLastStep) continue;
            interpolator.storeTime(this.stepStart);
            if (this.resetOccurred) {
                this.start(this.stepStart, y, t);
                interpolator.reinitialize(this.stepStart, this.stepSize, this.scaled, this.nordsieck);
            }
            double factor = this.computeStepGrowShrinkFactor(error);
            double scaledH = this.stepSize * factor;
            double nextT = this.stepStart + scaledH;
            boolean nextIsLast = forward ? nextT >= t : nextT <= t;
            hNew = this.filterStep(scaledH, forward, nextIsLast);
            double filteredNextT = this.stepStart + hNew;
            boolean bl = forward ? filteredNextT >= t : (filteredNextIsLast = filteredNextT <= t);
            if (filteredNextIsLast) {
                hNew = t - this.stepStart;
            }
            interpolator.rescale(hNew);
        } while (!this.isLastStep);
        equations.setTime(this.stepStart);
        equations.setCompleteState(y);
        this.resetInternalState();
    }
}

