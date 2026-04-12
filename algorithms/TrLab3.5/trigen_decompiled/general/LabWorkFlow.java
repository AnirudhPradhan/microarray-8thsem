/*
 * Decompiled with CFR 0.152.
 */
package general;

import input.InputFacade;
import input.laboratory.Options;
import input.laboratory.WrongOptionsException;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class LabWorkFlow {
    public Options loadOptions(String pathToOptions) throws FileNotFoundException, IOException, WrongOptionsException {
        Options options = InputFacade.buildOptions(pathToOptions);
        return options;
    }
}

