/*
 * Decompiled with CFR 0.152.
 */
package input;

import java.io.File;

public class Parameters {
    private File outFolder;
    private String outName;

    public Parameters() {
        this.outFolder = new File(System.getProperty("user.home"));
        this.outName = "";
    }

    public Parameters(File outFolder, String outName) {
        this.outFolder = outFolder;
        this.outName = outName;
    }

    public File getOutFolder() {
        return this.outFolder;
    }

    public String getOutName() {
        return this.outName;
    }

    public void setOutFolder(File outFolder) {
        this.outFolder = outFolder;
    }

    public void setOutName(String outName) {
        this.outName = outName;
    }

    public String toString() {
        String r = "";
        r = "\nOut Folder = " + this.outFolder.getAbsolutePath() + "\nOut Name = " + this.outName;
        return r;
    }
}

