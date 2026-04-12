/*
 * Decompiled with CFR 0.152.
 */
package general;

import input.algorithm.Control;
import input.algorithm.InvalidImplementationException;
import input.algorithm.WrongContolException;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface AlgorithmWorkFlow {
    public Control loadControl(String var1) throws FileNotFoundException, IOException, WrongContolException, InvalidImplementationException;
}

