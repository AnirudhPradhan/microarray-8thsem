/*
 * Decompiled with CFR 0.152.
 */
package algentrypoint;

import algcore.AlgorithmConfiguration;
import algcore.DataHierarchy;
import alginput.Microarray;
import algutils.LegacyWrongInput;
import algutils.TriGenBuilder;
import input.algorithm.Control;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class FootBridge {
    public static void buildParameters(Control control) throws IOException, LegacyWrongInput, NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        AlgorithmConfiguration PARAM = AlgorithmConfiguration.getInstance();
        PARAM.setData(new Microarray(control.getDataset().getDataset()));
        PARAM.setN(control.getN());
        PARAM.setG(control.getG());
        PARAM.setI(control.getI());
        PARAM.setSel((float)control.getSel());
        PARAM.setAle((float)control.getAle());
        PARAM.setMut((float)control.getMut());
        PARAM.setWf(control.getWf());
        PARAM.setWg((float)control.getWg());
        PARAM.setWc((float)control.getWc());
        PARAM.setWt((float)control.getWt());
        PARAM.setWog((float)control.getWog());
        PARAM.setWoc((float)control.getWoc());
        PARAM.setWot((float)control.getWot());
        PARAM.setMinG(control.getMinG());
        PARAM.setMinC(control.getMinC());
        PARAM.setMinT(control.getMinT());
        PARAM.setMaxG(control.getMaxG());
        PARAM.setMaxC(control.getMaxC());
        PARAM.setMaxT(control.getMaxT());
        int nt = control.getThreads();
        boolean con = false;
        if (nt > 0) {
            con = true;
        }
        PARAM.setConcurrency(con);
        PARAM.setThreads(nt);
        DataHierarchy jerarquia = TriGenBuilder.getInstance().buildDataHierarchyV2(control.getImplementation().getDataHierarchy(), control.getDataset().getGeneSize(), control.getDataset().getSampleSize(), control.getDataset().getTimeSize());
        PARAM.setDataHierarchy(jerarquia);
        String aux = "";
        aux = aux + "dataset = " + control.getDataset().getDatasetName() + "\n";
        aux = aux + "concurrence = " + PARAM.isConcurrency() + "\n";
        aux = aux + "threads = " + PARAM.getThreads() + "\n";
        aux = aux + "N   = " + PARAM.getN() + "\n";
        aux = aux + "G   = " + PARAM.getG() + "\n";
        aux = aux + "I   = " + PARAM.getI() + "\n";
        aux = aux + "Sel = " + PARAM.getSel() + "\n";
        aux = aux + "Mut = " + PARAM.getMut() + "\n";
        aux = aux + "Ale = " + PARAM.getAle() + "\n";
        aux = aux + "Wf  = " + PARAM.getWf() + "\n";
        aux = aux + "Wg  = " + PARAM.getWg() + "\n";
        aux = aux + "Wc  = " + PARAM.getWc() + "\n";
        aux = aux + "Wt  = " + PARAM.getWt() + "\n";
        aux = aux + "WOg = " + PARAM.getWog() + "\n";
        aux = aux + "WOc = " + PARAM.getWoc() + "\n";
        aux = aux + "WOt = " + PARAM.getWot() + "\n";
        aux = aux + "minG = " + PARAM.getMinG() + "\n";
        aux = aux + "minC = " + PARAM.getMinC() + "\n";
        aux = aux + "minT = " + PARAM.getMinT() + "\n";
        aux = aux + "maxG = " + PARAM.getMaxG() + "\n";
        aux = aux + "maxC = " + PARAM.getMaxC() + "\n";
        aux = aux + "maxT = " + PARAM.getMaxT() + "\n";
        aux = aux + "data        = " + PARAM.getData().getClass().getName() + "\n";
        aux = aux + "hierarchy   = " + PARAM.getDataHierarchy().getClass().getName() + "\n";
        PARAM.setReportString(aux);
    }
}

