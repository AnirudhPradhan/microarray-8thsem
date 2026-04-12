/*
 * Decompiled with CFR 0.152.
 */
package algentrypoint;

import algcore.AlgorithmConfiguration;
import algcore.AlgorithmIndividual;
import algcore.DataHierarchy;
import algcore.TriGen;
import algentrypoint.FootBridge;
import algutils.LegacyWrongInput;
import algutils.TriGenBuilder;
import general.Parser;
import input.algorithm.Control;
import input.algorithm.InvalidImplementationException;
import input.algorithm.WrongContolException;
import input.laboratory.AnalysisResources;
import input.laboratory.WrongOptionsException;
import input.workflows.TriGenRun;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.List;
import labentrypoint.Facade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SystemUtilities;
import utils.TextUtilities;

public class Console {
    private static final Logger LOG = LoggerFactory.getLogger(Console.class);

    public static void main(String[] args) throws FileNotFoundException, LegacyWrongInput {
        Control control = null;
        try {
            TriGenRun w = new TriGenRun();
            control = w.loadControl(args[0]);
            FootBridge.buildParameters(control);
            AlgorithmConfiguration config = AlgorithmConfiguration.getInstance();
            LOG.debug("Run configuration:\n" + config.getReportString() + "\n");
            LOG.debug("Implementation:\n" + control.getImplementation() + "\n\n");
            TriGen TRIGEN = TriGen.getInstance();
            TriGenBuilder.getInstance().buildTriGen(control.getImplementation());
            Calendar now1 = Calendar.getInstance();
            long t1 = now1.getTimeInMillis();
            LOG.info("Executing algoritm:\n");
            List<AlgorithmIndividual> r = TRIGEN.runAlgorithm();
            Calendar now2 = Calendar.getInstance();
            long t2 = now2.getTimeInMillis();
            String timeString = TextUtilities.getTimeString(t1, t2);
            DataHierarchy hierarchy = config.getDataHierarchy();
            AnalysisResources lr = new AnalysisResources(control, t2 - t1);
            lr.loadLegacyDataHierarchy(hierarchy.getGenHierarchy(), hierarchy.getSampleHierarchy(), hierarchy.getTimeHierarchy());
            for (AlgorithmIndividual tri : r) {
                lr.loadOneSolution(tri.getGenes(), tri.getSamples(), tri.getTimes());
                lr.loadOneSlog(tri.getRegisterReport(";"));
            }
            LOG.info("\n" + timeString + "\n");
            LOG.info("\nBuilding solutions\n");
            File solDir = new File(control.getOutFolder() + SystemUtilities.getFileSep() + control.getOutName());
            solDir.mkdirs();
            Parser.buildSolutionFile(lr, solDir.getAbsolutePath());
            Facade.buildCompleteResultsFiles(lr, solDir.getAbsolutePath());
            LOG.info("\nDONE!");
        }
        catch (InvalidImplementationException | WrongContolException | IOException e) {
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (WrongOptionsException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

