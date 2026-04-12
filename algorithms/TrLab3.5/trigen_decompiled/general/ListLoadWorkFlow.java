/*
 * Decompiled with CFR 0.152.
 */
package general;

import general.LabWorkFlow;
import input.algorithm.InvalidImplementationException;
import input.algorithm.WrongContolException;
import input.laboratory.CommonAnalysisResources;
import java.io.IOException;
import java.util.List;

public abstract class ListLoadWorkFlow
extends LabWorkFlow {
    public abstract List<CommonAnalysisResources> listLoad(String var1, char var2) throws IOException, WrongContolException, InvalidImplementationException;
}

