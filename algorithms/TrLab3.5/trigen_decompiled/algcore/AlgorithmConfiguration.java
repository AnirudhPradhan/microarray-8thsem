/*
 * Decompiled with CFR 0.152.
 */
package algcore;

import algcore.AlgorithmDataset;
import algcore.DataHierarchy;

public class AlgorithmConfiguration {
    private static AlgorithmConfiguration singleton = new AlgorithmConfiguration();
    private boolean concurrency = false;
    private int threads;
    private String reportString;
    private AlgorithmDataset data;
    private DataHierarchy dataHierarchy;
    private int n;
    private int g;
    private int i;
    private float ale;
    private float sel;
    private float mut;
    private double wf;
    private float wg;
    private float wc;
    private float wt;
    private float wog;
    private float woc;
    private float wot;
    private int minG;
    private int minC;
    private int minT;
    private int maxG;
    private int maxC;
    private int maxT;

    private AlgorithmConfiguration() {
    }

    public static AlgorithmConfiguration getInstance() {
        return singleton;
    }

    public boolean isConcurrency() {
        return this.concurrency;
    }

    public int getThreads() {
        return this.threads;
    }

    public String getReportString() {
        return this.reportString;
    }

    public AlgorithmDataset getData() {
        return this.data;
    }

    public DataHierarchy getDataHierarchy() {
        return this.dataHierarchy;
    }

    public int getN() {
        return this.n;
    }

    public int getG() {
        return this.g;
    }

    public int getI() {
        return this.i;
    }

    public float getAle() {
        return this.ale;
    }

    public float getSel() {
        return this.sel;
    }

    public float getMut() {
        return this.mut;
    }

    public double getWf() {
        return this.wf;
    }

    public float getWg() {
        return this.wg;
    }

    public float getWc() {
        return this.wc;
    }

    public float getWt() {
        return this.wt;
    }

    public float getWog() {
        return this.wog;
    }

    public float getWoc() {
        return this.woc;
    }

    public float getWot() {
        return this.wot;
    }

    public int getMinG() {
        return this.minG;
    }

    public int getMinC() {
        return this.minC;
    }

    public int getMinT() {
        return this.minT;
    }

    public int getMaxG() {
        return this.maxG;
    }

    public int getMaxC() {
        return this.maxC;
    }

    public int getMaxT() {
        return this.maxT;
    }

    public void setConcurrency(boolean concurrency) {
        this.concurrency = concurrency;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public void setReportString(String reportString) {
        this.reportString = reportString;
    }

    public void setData(AlgorithmDataset data) {
        this.data = data;
    }

    public void setDataHierarchy(DataHierarchy dataHierarchy) {
        this.dataHierarchy = dataHierarchy;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setAle(float ale) {
        this.ale = ale;
    }

    public void setSel(float sel) {
        this.sel = sel;
    }

    public void setMut(float mut) {
        this.mut = mut;
    }

    public void setWf(double wf) {
        this.wf = wf;
    }

    public void setWg(float wg) {
        this.wg = wg;
    }

    public void setWc(float wc) {
        this.wc = wc;
    }

    public void setWt(float wt) {
        this.wt = wt;
    }

    public void setWog(float wog) {
        this.wog = wog;
    }

    public void setWoc(float woc) {
        this.woc = woc;
    }

    public void setWot(float wot) {
        this.wot = wot;
    }

    public void setMinG(int minG) {
        this.minG = minG;
    }

    public void setMinC(int minC) {
        this.minC = minC;
    }

    public void setMinT(int minT) {
        this.minT = minT;
    }

    public void setMaxG(int maxG) {
        this.maxG = maxG;
    }

    public void setMaxC(int maxC) {
        this.maxC = maxC;
    }

    public void setMaxT(int maxT) {
        this.maxT = maxT;
    }
}

