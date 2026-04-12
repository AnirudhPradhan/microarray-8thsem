/*
 * Decompiled with CFR 0.152.
 */
package general;

import input.algorithm.InvalidImplementationException;
import input.algorithm.WrongContolException;
import input.laboratory.OPTsolBatch;
import java.io.IOException;

public abstract class OptListLoadWorkflow {
    public abstract OPTsolBatch listLoad(String var1, String var2) throws IOException, WrongContolException, InvalidImplementationException;
}

