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

    public static String getSignificanceReportP(List<GoStudy> list) {
        return SignificanceReports.getSignificanceReport(list, ";", "p");
    }

    public static String getSignificanceReportPA(List<GoStudy> list) {
        return SignificanceReports.getSignificanceReport(list, ";", "pa");
    }

    public static String getSignificanceReport(List<GoStudy> list, String string, String string2) {
        Object object;
        Object object2 = "";
        GoSignificance goSignificance = GoSignificance.getInstance();
        List<GoInterval> list2 = goSignificance.getINTERVALS();
        String string3 = LEVEL_HEADER_TAG + string + LEVELWEIGHT_HEADER_TAG + string + INTERVAL_HEADER_TAG + string;
        Object object3 = "";
        ArrayList<GoLevels> arrayList = new ArrayList<GoLevels>(list.size());
        for (GoStudy object4 : list) {
            LOG.debug(object4.getStudyName());
            GoLevels goLevels = goSignificance.getGoLevels(object4);
            goSignificance.computeAllGoSig(goLevels);
            arrayList.add(goLevels);
            object = object4.getStudyName();
            object3 = (String)object3 + (String)object + ITEMS_HEADER_TAG + string + (String)object + CONCENTRATION_HEADER_TAG + string + (String)object + PARTIAL_HEADER_TAG + string;
        }
        string3 = string3 + (String)object3;
        object2 = string3 + "\n";
        int n = 0;
        for (GoInterval goInterval : list2) {
            object = goInterval.getReportString(string);
            String string4 = "";
            for (GoStudy goStudy : list) {
                GoSlot goSlot = ((GoLevels)arrayList.get(list.indexOf(goStudy))).getSlotsByType(string2).get(n);
                string4 = goSlot.getReport(string);
                object = (String)object + string4 + string;
            }
            object2 = (String)object2 + (String)object + "\n";
            ++n;
        }
        return object2;
    }

    public static String getSignificanceReportPA(Experiment experiment) {
        return SignificanceReports.getSignificanceReport(experiment, ";", "pa");
    }

    public static String getSignificanceReportP(Experiment experiment) {
        return SignificanceReports.getSignificanceReport(experiment, ";", "p");
    }

    public static String getSignificanceReport(Experiment experiment, String string, String string2) {
        Object object = "";
        List<Solution> list = experiment.getSolutions();
        List<GoInterval> list2 = GoSignificance.getInstance().getINTERVALS();
        String string3 = LEVEL_HEADER_TAG + string + LEVELWEIGHT_HEADER_TAG + string + INTERVAL_HEADER_TAG + string;
        Object object2 = "";
        for (Solution object3 : list) {
            String string4 = object3.getName();
            object2 = (String)object2 + string4 + ITEMS_HEADER_TAG + string + string4 + CONCENTRATION_HEADER_TAG + string + string4 + PARTIAL_HEADER_TAG + string;
        }
        string3 = string3 + (String)object2;
        object = string3 + "\n";
        int n = 0;
        for (GoInterval goInterval : list2) {
            Object object3 = goInterval.getReportString(string);
            String string5 = "";
            for (Solution solution : list) {
                List<GoSlot> list3 = solution.getGoSlots(string2);
                if (list3 == null || n >= list3.size()) {
                    string5 = "";
                } else {
                    GoSlot goSlot = list3.get(n);
                    string5 = goSlot.getReport(string);
                }
                object3 = (String)object3 + string5 + string;
            }
            object = (String)object + (String)object3 + "\n";
            ++n;
        }
        return object;
    }
}

