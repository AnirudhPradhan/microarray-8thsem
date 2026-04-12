/*
 * Decompiled with CFR 0.152.
 */
package input.laboratory;

import input.Parameters;
import input.laboratory.WrongOptionsException;
import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SystemUtilities;
import utils.TextUtilities;

public class Options
extends Parameters {
    private static final Logger LOG = LoggerFactory.getLogger(Options.class);
    private static final double MIN_EVAL = 0.0;
    private static final double MAX_EVAL = 1.0E24;
    private static final boolean DEFAULT_GORESOURCES = false;
    private static final String GORESOURCES_YES = "yes";
    private static final String GORESOURCES_NO = "no";
    private static final String DEFAULT = "default";
    private static final String DEFAULT_MTC = "Bonferroni";
    private static final String BONFERRONI = "Bonferroni";
    private static final String NONE = "None";
    private static final String WESTFALL = "Westfall-Young-Single-Step";
    private static final String DEFAULT_CALCULATION = "Term-For-Term";
    private static final String TFT = "Term-For-Term";
    private static final String PCU = "Parent-Child-Union";
    private static final String PCI = "Parent-Child-Intersection";
    private static final String DEFAULT_DOT = "";
    private static final double MIN_DOT = 0.0;
    private static final double MAX_DOT = 0.5;
    private static final String DEFAULT_FILTER = "";
    private static final boolean DEFAULT_IGNORE = false;
    private static final String IGNORE_YES = "yes";
    private static final String IGNORE_NO = "no";
    private static final boolean DEFAULT_ANNOTATION = false;
    private static final String ANNOTATION_YES = "yes";
    private static final String ANNOTATION_NO = "no";
    private static final String DEFAULT_STEPS = "";
    private static final int MIN_STEPS = 0;
    private static final int MAX_STEPS = 10000;
    private String goMtc;
    private String goCalculation;
    private String goDot;
    private String goFilter;
    private boolean goIgnore;
    private boolean goAnnotation;
    private boolean goResources;
    private String goResamplingSteps;
    private double threshold;
    private double base;
    private double step;
    private double difference;
    private double bonus;
    private double cgraphical;
    private double cpearson;
    private double cspearman;
    private double graphical;
    private double pearson;
    private double spearman;
    private double biological;

    public Options() throws WrongOptionsException {
        this.specificCheckings(SystemUtilities.getLaboratoryProperties());
    }

    public Options(File outFolder, String outName) throws WrongOptionsException {
        super(outFolder, outName);
        this.specificCheckings(SystemUtilities.getLaboratoryProperties());
    }

    public Options(Properties prop, File outFolder, String outName) throws WrongOptionsException {
        super(outFolder, outName);
        this.specificCheckings(prop);
    }

    public Options(Properties prop) throws WrongOptionsException {
        super(new File(TextUtilities.getRootPathWithSlash(prop.getProperty("out"))), TextUtilities.getFileName(prop.getProperty("out")));
        this.specificCheckings(prop);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void specificCheckings(Properties prop) throws WrongOptionsException {
        boolean error;
        String errMessage;
        String auxGoResources;
        String auxGoAnnotation;
        String auxGoIgnore;
        block57: {
            this.goMtc = prop.getProperty("mtc");
            this.goCalculation = prop.getProperty("calculation");
            this.goDot = prop.getProperty("dot");
            this.goFilter = prop.getProperty("filter");
            auxGoIgnore = prop.getProperty("ignore");
            auxGoAnnotation = prop.getProperty("annotation");
            this.goResamplingSteps = prop.getProperty("resamplingsteps");
            auxGoResources = prop.getProperty("go_resources");
            this.cgraphical = Double.parseDouble(prop.getProperty("CGRQ"));
            this.cpearson = Double.parseDouble(prop.getProperty("CPEQ"));
            this.cspearman = Double.parseDouble(prop.getProperty("CSPQ"));
            this.graphical = Double.parseDouble(prop.getProperty("GRQ"));
            this.pearson = Double.parseDouble(prop.getProperty("PEQ"));
            this.spearman = Double.parseDouble(prop.getProperty("SPQ"));
            this.biological = Double.parseDouble(prop.getProperty("BIOQ"));
            this.threshold = Double.parseDouble(prop.getProperty("threshold"));
            this.base = Double.parseDouble(prop.getProperty("base"));
            this.step = Double.parseDouble(prop.getProperty("step"));
            this.difference = Double.parseDouble(prop.getProperty("difference"));
            this.bonus = Double.parseDouble(prop.getProperty("bonus"));
            errMessage = "";
            error = false;
            try {
                if (!this.goDot.equalsIgnoreCase(DEFAULT)) {
                    double dot = Double.parseDouble(this.goDot);
                    if (!(errMessage = errMessage + this.checkInterval("dot", dot, 0.0, 0.5)).equalsIgnoreCase("")) {
                        error = true;
                    }
                } else {
                    this.goDot = "";
                }
                if (!this.goResamplingSteps.equalsIgnoreCase(DEFAULT)) {
                    int rsteps = Integer.parseInt(this.goResamplingSteps);
                    if (!(errMessage = errMessage + this.checkInterval("resamplingsteps", rsteps, 0.0, 10000.0)).equalsIgnoreCase("")) {
                        error = true;
                    }
                    break block57;
                }
                this.goResamplingSteps = "";
            }
            catch (NumberFormatException e) {
                try {
                    errMessage = errMessage + "Wrong input format number for dot and-or resamplingsteps paths\n";
                }
                catch (Throwable throwable) {
                    errMessage = errMessage + this.checkOptions("mtc", this.goMtc, DEFAULT, "Bonferroni", NONE, WESTFALL);
                    if (!errMessage.equalsIgnoreCase("")) {
                        error = true;
                    }
                    if (this.goMtc.equalsIgnoreCase(DEFAULT)) {
                        this.goMtc = "Bonferroni";
                    }
                    if (!(errMessage = errMessage + this.checkOptions("calculation", this.goCalculation, DEFAULT, "Term-For-Term", PCU, PCI)).equalsIgnoreCase("")) {
                        error = true;
                    }
                    if (this.goCalculation.equalsIgnoreCase(DEFAULT)) {
                        this.goCalculation = "Term-For-Term";
                    }
                    if (!(errMessage = errMessage + this.checkOptions("ignore", auxGoIgnore, DEFAULT, "yes", "no")).equalsIgnoreCase("")) {
                        error = true;
                    }
                    this.goIgnore = auxGoIgnore.equalsIgnoreCase(DEFAULT) ? false : !auxGoIgnore.equalsIgnoreCase("no");
                    if (this.goFilter.equalsIgnoreCase(DEFAULT)) {
                        this.goFilter = "";
                    }
                    if (!(errMessage = errMessage + this.checkOptions("annotation", auxGoAnnotation, DEFAULT, "yes", "no")).equalsIgnoreCase("")) {
                        error = true;
                    }
                    this.goAnnotation = auxGoAnnotation.equalsIgnoreCase(DEFAULT) ? false : !auxGoAnnotation.equalsIgnoreCase("no");
                    if (!(errMessage = errMessage + this.checkOptions("go_resources", auxGoResources, DEFAULT, "yes", "no")).equalsIgnoreCase("")) {
                        error = true;
                    }
                    this.goResources = auxGoResources.equalsIgnoreCase(DEFAULT) ? false : !auxGoResources.equalsIgnoreCase("no");
                    if (!(errMessage = errMessage + this.checkInterval("CGRQ", this.cgraphical, 0.0, 1.0E24)).equalsIgnoreCase("")) {
                        error = true;
                    }
                    if (!(errMessage = errMessage + this.checkInterval("CPEQ", this.cpearson, 0.0, 1.0E24)).equalsIgnoreCase("")) {
                        error = true;
                    }
                    if (!(errMessage = errMessage + this.checkInterval("CSPQ", this.cspearman, 0.0, 1.0E24)).equalsIgnoreCase("")) {
                        error = true;
                    }
                    if (!(errMessage = errMessage + this.checkInterval("GRQ", this.graphical, 0.0, 1.0E24)).equalsIgnoreCase("")) {
                        error = true;
                    }
                    if (!(errMessage = errMessage + this.checkInterval("PEQ", this.pearson, 0.0, 1.0E24)).equalsIgnoreCase("")) {
                        error = true;
                    }
                    if (!(errMessage = errMessage + this.checkInterval("SPQ", this.spearman, 0.0, 1.0E24)).equalsIgnoreCase("")) {
                        error = true;
                    }
                    if (!(errMessage = errMessage + this.checkInterval("BIOQ", this.biological, 0.0, 1.0E24)).equalsIgnoreCase("")) {
                        error = true;
                    }
                    if (error) {
                        throw new WrongOptionsException(errMessage);
                    }
                    throw throwable;
                }
                errMessage = errMessage + this.checkOptions("mtc", this.goMtc, DEFAULT, "Bonferroni", NONE, WESTFALL);
                if (!errMessage.equalsIgnoreCase("")) {
                    error = true;
                }
                if (this.goMtc.equalsIgnoreCase(DEFAULT)) {
                    this.goMtc = "Bonferroni";
                }
                if (!(errMessage = errMessage + this.checkOptions("calculation", this.goCalculation, DEFAULT, "Term-For-Term", PCU, PCI)).equalsIgnoreCase("")) {
                    error = true;
                }
                if (this.goCalculation.equalsIgnoreCase(DEFAULT)) {
                    this.goCalculation = "Term-For-Term";
                }
                if (!(errMessage = errMessage + this.checkOptions("ignore", auxGoIgnore, DEFAULT, "yes", "no")).equalsIgnoreCase("")) {
                    error = true;
                }
                this.goIgnore = auxGoIgnore.equalsIgnoreCase(DEFAULT) ? false : !auxGoIgnore.equalsIgnoreCase("no");
                if (this.goFilter.equalsIgnoreCase(DEFAULT)) {
                    this.goFilter = "";
                }
                if (!(errMessage = errMessage + this.checkOptions("annotation", auxGoAnnotation, DEFAULT, "yes", "no")).equalsIgnoreCase("")) {
                    error = true;
                }
                this.goAnnotation = auxGoAnnotation.equalsIgnoreCase(DEFAULT) ? false : !auxGoAnnotation.equalsIgnoreCase("no");
                if (!(errMessage = errMessage + this.checkOptions("go_resources", auxGoResources, DEFAULT, "yes", "no")).equalsIgnoreCase("")) {
                    error = true;
                }
                this.goResources = auxGoResources.equalsIgnoreCase(DEFAULT) ? false : !auxGoResources.equalsIgnoreCase("no");
                if (!(errMessage = errMessage + this.checkInterval("CGRQ", this.cgraphical, 0.0, 1.0E24)).equalsIgnoreCase("")) {
                    error = true;
                }
                if (!(errMessage = errMessage + this.checkInterval("CPEQ", this.cpearson, 0.0, 1.0E24)).equalsIgnoreCase("")) {
                    error = true;
                }
                if (!(errMessage = errMessage + this.checkInterval("CSPQ", this.cspearman, 0.0, 1.0E24)).equalsIgnoreCase("")) {
                    error = true;
                }
                if (!(errMessage = errMessage + this.checkInterval("GRQ", this.graphical, 0.0, 1.0E24)).equalsIgnoreCase("")) {
                    error = true;
                }
                if (!(errMessage = errMessage + this.checkInterval("PEQ", this.pearson, 0.0, 1.0E24)).equalsIgnoreCase("")) {
                    error = true;
                }
                if (!(errMessage = errMessage + this.checkInterval("SPQ", this.spearman, 0.0, 1.0E24)).equalsIgnoreCase("")) {
                    error = true;
                }
                if (!(errMessage = errMessage + this.checkInterval("BIOQ", this.biological, 0.0, 1.0E24)).equalsIgnoreCase("")) {
                    error = true;
                }
                if (error) {
                    throw new WrongOptionsException(errMessage);
                }
            }
        }
        errMessage = errMessage + this.checkOptions("mtc", this.goMtc, DEFAULT, "Bonferroni", NONE, WESTFALL);
        if (!errMessage.equalsIgnoreCase("")) {
            error = true;
        }
        if (this.goMtc.equalsIgnoreCase(DEFAULT)) {
            this.goMtc = "Bonferroni";
        }
        if (!(errMessage = errMessage + this.checkOptions("calculation", this.goCalculation, DEFAULT, "Term-For-Term", PCU, PCI)).equalsIgnoreCase("")) {
            error = true;
        }
        if (this.goCalculation.equalsIgnoreCase(DEFAULT)) {
            this.goCalculation = "Term-For-Term";
        }
        if (!(errMessage = errMessage + this.checkOptions("ignore", auxGoIgnore, DEFAULT, "yes", "no")).equalsIgnoreCase("")) {
            error = true;
        }
        this.goIgnore = auxGoIgnore.equalsIgnoreCase(DEFAULT) ? false : !auxGoIgnore.equalsIgnoreCase("no");
        if (this.goFilter.equalsIgnoreCase(DEFAULT)) {
            this.goFilter = "";
        }
        if (!(errMessage = errMessage + this.checkOptions("annotation", auxGoAnnotation, DEFAULT, "yes", "no")).equalsIgnoreCase("")) {
            error = true;
        }
        this.goAnnotation = auxGoAnnotation.equalsIgnoreCase(DEFAULT) ? false : !auxGoAnnotation.equalsIgnoreCase("no");
        if (!(errMessage = errMessage + this.checkOptions("go_resources", auxGoResources, DEFAULT, "yes", "no")).equalsIgnoreCase("")) {
            error = true;
        }
        this.goResources = auxGoResources.equalsIgnoreCase(DEFAULT) ? false : !auxGoResources.equalsIgnoreCase("no");
        if (!(errMessage = errMessage + this.checkInterval("CGRQ", this.cgraphical, 0.0, 1.0E24)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("CPEQ", this.cpearson, 0.0, 1.0E24)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("CSPQ", this.cspearman, 0.0, 1.0E24)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("GRQ", this.graphical, 0.0, 1.0E24)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("PEQ", this.pearson, 0.0, 1.0E24)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("SPQ", this.spearman, 0.0, 1.0E24)).equalsIgnoreCase("")) {
            error = true;
        }
        if (!(errMessage = errMessage + this.checkInterval("BIOQ", this.biological, 0.0, 1.0E24)).equalsIgnoreCase("")) {
            error = true;
        }
        if (error) {
            throw new WrongOptionsException(errMessage);
        }
    }

    @Override
    public String toString() {
        String r = super.toString();
        r = r + "\nMtc = " + this.goMtc + "\ncalculation = " + this.goCalculation + "\ndot = " + this.goDot + "\nfilter = " + this.goFilter + "\nignore = " + this.goIgnore + "\nannotation = " + this.goAnnotation + "\nresamplingsteps = " + this.goResamplingSteps + "\ngo resources = " + this.goResources + "\nCGRQ = " + this.cgraphical + "\nCPEQ = " + this.cpearson + "\nCSPQ = " + this.cspearman + "\nGRQ = " + this.graphical + "\nPEQ = " + this.pearson + "\nSPQ = " + this.spearman + "\nBIOQ = " + this.biological + "\nthreshold = " + this.threshold + "\nbase = " + this.base + "\nstep = " + this.step + "\ndifference = " + this.difference + "\nbonus = " + this.bonus;
        return r;
    }

    public String getGoMtc() {
        return this.goMtc;
    }

    public String getGoCalculation() {
        return this.goCalculation;
    }

    public String getGoDot() {
        return this.goDot;
    }

    public String getGoFilter() {
        return this.goFilter;
    }

    public boolean isGoIgnore() {
        return this.goIgnore;
    }

    public boolean isGoAnnotation() {
        return this.goAnnotation;
    }

    public String getGOrsteps() {
        return this.goResamplingSteps;
    }

    public double getGraphical() {
        return this.graphical;
    }

    public double getPearson() {
        return this.pearson;
    }

    public double getSpearman() {
        return this.spearman;
    }

    public double getBiological() {
        return this.biological;
    }

    public double getThreshold() {
        return this.threshold;
    }

    public double getBase() {
        return this.base;
    }

    public double getStep() {
        return this.step;
    }

    public double getDifference() {
        return this.difference;
    }

    public double getBonus() {
        return this.bonus;
    }

    public boolean isGoResources() {
        return this.goResources;
    }

    public double getCgrq() {
        return this.cgraphical;
    }

    public double getCpeq() {
        return this.cpearson;
    }

    public double getCspq() {
        return this.cspearman;
    }

    private String checkOptions(String parameter, String value, String ... options) {
        String r = "";
        boolean found = false;
        for (int i = 0; i < options.length; ++i) {
            if (!options[i].equals(value)) continue;
            found = true;
        }
        if (!found) {
            r = parameter + " must be set to " + Arrays.toString(options) + "\n";
        }
        return r;
    }

    private String checkInterval(String parameter, double value, double min, double max) {
        String r = "";
        if (value < min || value > max) {
            r = parameter + " must be set to [" + min + "," + max + "]\n";
        }
        return r;
    }
}

