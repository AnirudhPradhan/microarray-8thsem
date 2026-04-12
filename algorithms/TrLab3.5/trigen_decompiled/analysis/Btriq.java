/*
 * Decompiled with CFR 0.152.
 */
package analysis;

import analysis.Solution;
import analysis.TRIQ;
import analysis.biological.BIOQ;
import analysis.correlation.PEQ;
import analysis.correlation.SPQ;
import analysis.graphical.GRQ;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Btriq
implements TRIQ {
    private static final Logger LOG = LoggerFactory.getLogger(Btriq.class);
    private List<Solution> solutions;
    private GRQ grq;
    private PEQ peq;
    private SPQ spq;
    private BIOQ bioq;
    private double wGrq;
    private double wPeq;
    private double wSpq;
    private double wBioq;

    public Btriq(List<Solution> solutions, GRQ grq, PEQ peq, SPQ spq, BIOQ bioq, double wGrq, double wPeq, double wSpq, double wBioq) {
        this.solutions = solutions;
        this.grq = grq;
        this.peq = peq;
        this.spq = spq;
        this.bioq = bioq;
        this.wGrq = wGrq;
        this.wPeq = wPeq;
        this.wSpq = wSpq;
        this.wBioq = wBioq;
    }

    @Override
    public void computeTRIQ() throws IOException, InterruptedException {
        LOG.debug("Computing GRQ");
        this.grq.computeGrq();
        LOG.debug("Computing PEQ");
        this.peq.computePeq();
        LOG.debug("Computing SPQ");
        this.spq.computeSpq();
        LOG.debug("Computing BIOQ");
        this.bioq.computeBioq();
        this.compute();
    }

    private void compute() {
        for (Solution sol : this.solutions) {
            this.computeOneSolution(sol);
        }
    }

    private void computeOneSolution(Solution sol) {
        double triq = 0.0;
        double triqn = 0.0;
        double grq = sol.getValue("grq");
        double peq = sol.getValue("peq");
        double spq = sol.getValue("spq");
        double bioq = sol.getValue("bioq");
        double bioqn = sol.getValue("bioqn");
        double wgrq = grq * this.wGrq;
        double wpeq = peq * this.wPeq;
        double wspq = spq * this.wSpq;
        double wbioq = bioq * this.wBioq;
        double wbioqn = bioqn * this.wBioq;
        double num = wgrq + wpeq + wspq + wbioq;
        double numn = wgrq + wpeq + wspq + wbioqn;
        double den = this.wGrq + this.wPeq + this.wSpq + this.wBioq;
        double denn = this.wGrq + this.wPeq + this.wSpq + this.wBioq;
        triq = num / den;
        triqn = numn / denn;
        sol.putValue("triq", triq);
        sol.putValue("triqn", triqn);
    }
}

