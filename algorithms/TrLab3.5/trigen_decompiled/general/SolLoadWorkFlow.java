/*
 * Decompiled with CFR 0.152.
 */
package general;

import general.LabWorkFlow;
import input.algorithm.InvalidImplementationException;
import input.algorithm.WrongContolException;
import input.laboratory.AnalysisResources;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class SolLoadWorkFlow
extends LabWorkFlow {
    public abstract AnalysisResources loadSolution(String var1) throws FileNotFoundException, IOException, WrongContolException, InvalidImplementationException;
}

