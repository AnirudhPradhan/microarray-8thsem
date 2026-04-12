/*
 * Decompiled with CFR 0.152.
 */
package analysis.reports;

import analysis.Experiment;
import analysis.Solution;
import analysis.biological.GoInterval;
import analysis.biological.GoLevels;
import analysis.biological.GoSignificance;
import analysis.biological.GoSlot;
import analysis.biological.GoStudy;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignificanceReports {
    private static final Logger LOG = LoggerFactory.getLogger(SignificanceReports.class);
    private static final String LEVEL_HEADER_TAG = "LEVEL";
    private static final String LEVELWEIGHT_HEADER_TAG = "WEIGHT";
    private static final String INTERVAL_HEADER_TAG = "INTERVAL";
    private static final String ITEMS_HEADER_TAG = "TERMS";
    private static final String CONCENTRATION_HEADER_TAG = "CONCENTRATION";
    private static final String PARTIAL_HEADER_TAG = "PARTIAL";

    public static String getSignificanceReportP(List<GoStudy> studies) {
        return SignificanceReports.getSignificanceReport(studies, ";", "p");
    }

    public static String getSignificanceReportPA(List<GoStudy> studies) {
        return SignificanceReports.getSignificanceReport(studies, ";", "pa");
    }

    public static String getSignificanceReport(List<GoStudy> studies, String sep, String type) {
        String r = "";
        GoSignificance significance = GoSignificance.getInstance();
        List<GoInterval> intervals = significance.getINTERVALS();
        String header = LEVEL_HEADER_TAG + sep + LEVELWEIGHT_HEADER_TAG + sep + INTERVAL_HEADER_TAG + sep;
        String headerSufix = "";
        ArrayList<GoLevels> levels = new ArrayList<GoLevels>(studies.size());
        for (GoStudy std : studies) {
            LOG.debug(std.getStudyName());
            GoLevels goLevels = significance.getGoLevels(std);
            significance.computeAllGoSig(goLevels);
            levels.add(goLevels);
            String solName = std.getStudyName();
            headerSufix = headerSufix + solName + ITEMS_HEADER_TAG + sep + solName + CONCENTRATION_HEADER_TAG + sep + solName + PARTIAL_HEADER_TAG + sep;
        }
        header = header + headerSufix;
        r = header + "\n";
        int i = 0;
        for (GoInterval in : intervals) {
            String prefix = in.getReportString(sep);
            String sufix = "";
            for (GoStudy std : studies) {
                GoSlot slot = ((GoLevels)levels.get(studies.indexOf(std))).getSlotsByType(type).get(i);
                sufix = slot.getReport(sep);
                prefix = prefix + sufix + sep;
            }
            r = r + prefix + "\n";
            ++i;
        }
        return r;
    }

    public static String getSignificanceReportPA(Experiment exp) {
        return SignificanceReports.getSignificanceReport(exp, ";", "pa");
    }

    public static String getSignificanceReportP(Experiment exp) {
        return SignificanceReports.getSignificanceReport(exp, ";", "p");
    }

    public static String getSignificanceReport(Experiment exp, String sep, String type) {
        String r = "";
        List<Solution> solutions = exp.getSolutions();
        List<GoInterval> intervals = GoSignificance.getInstance().getINTERVALS();
        String header = LEVEL_HEADER_TAG + sep + LEVELWEIGHT_HEADER_TAG + sep + INTERVAL_HEADER_TAG + sep;
        String headerSufix = "";
        for (Solution sol : solutions) {
            String solName = sol.getName();
            headerSufix = headerSufix + solName + ITEMS_HEADER_TAG + sep + solName + CONCENTRATION_HEADER_TAG + sep + solName + PARTIAL_HEADER_TAG + sep;
        }
        header = header + headerSufix;
        r = header + "\n";
        int i = 0;
        for (GoInterval in : intervals) {
            String prefix = in.getReportString(sep);
            String sufix = "";
            for (Solution sol : solutions) {
                List<GoSlot> slots = sol.getGoSlots(type);
                if (slots == null || i >= slots.size()) {
                    sufix = "";
                } else {
                    GoSlot slot = slots.get(i);
                    sufix = slot.getReport(sep);
                }
                prefix = prefix + sufix + sep;
            }
            r = r + prefix + "\n";
            ++i;
        }
        return r;
    }
}
