/*
 * Decompiled with CFR 0.152.
 */
package input.workflows;

import general.OptListLoadWorkflow;
import general.Tricluster;
import input.InputFacade;
import input.algorithm.InvalidImplementationException;
import input.algorithm.WrongContolException;
import input.datasets.Biological;
import input.laboratory.OPTsolBatch;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ReadTable;
import utils.TextUtilities;

public class LabOptListRun
extends OptListLoadWorkflow {
    private static final Logger LOG = LoggerFactory.getLogger(LabOptListRun.class);

    @Override
    public OPTsolBatch listLoad(String pathToTriclustersFolder, String datasetName) throws IOException, WrongContolException, InvalidImplementationException {
        Biological dataset = (Biological)InputFacade.buildDataset(datasetName);
        File optFolder = new File(pathToTriclustersFolder);
        File[] optFileList = optFolder.listFiles();
        ArrayList<String> optPaths = new ArrayList<String>(optFileList.length);
        for (File path : optFileList) {
            LOG.debug(path.getAbsolutePath());
            if (path.isHidden()) continue;
            optPaths.add(path.getAbsolutePath());
        }
        Collections.sort(optPaths);
        ArrayList<Tricluster> tl = new ArrayList<Tricluster>(optPaths.size());
        for (String path : optPaths) {
            ReadTable rt = new ReadTable(path, ";");
            List<List<String>> data = rt.getTable();
            Tricluster tr = this.processOneTricluster(data, dataset);
            tl.add(tr);
        }
        OPTsolBatch r = new OPTsolBatch(tl, dataset);
        return r;
    }

    private Tricluster processOneTricluster(List<List<String>> inputData, Biological dataset) {
        Tricluster r = new Tricluster();
        List<String> firstLine = inputData.get(0);
        List<List<String>> rest = inputData.subList(1, inputData.size());
        List<Integer> g = this.getTrGenes(rest);
        List<Integer>[] st = this.getSamplesTimesLists(firstLine, dataset);
        r.loadCoordinates(g, st[0], st[1]);
        return r;
    }

    private List<Integer> getTrGenes(List<List<String>> rest) {
        String check = "";
        ArrayList<Integer> r = new ArrayList<Integer>();
        int nr = 0;
        for (List<String> row : rest) {
            int gi = Integer.parseInt(row.get(0));
            check = nr == rest.size() - 1 ? check + row.get(1) : check + row.get(1) + ", ";
            r.add(new Integer(gi - 1));
            ++nr;
        }
        Collections.sort(r);
        return r;
    }

    private List<Integer>[] getSamplesTimesLists(List<String> firstLine, Biological dataset) {
        List[] r = new List[2];
        firstLine.remove(0);
        firstLine.remove(0);
        HashSet<String> inputSamples = new HashSet<String>();
        HashSet<String> inputTimes = new HashSet<String>();
        for (String el : firstLine) {
            List<String> t = TextUtilities.splitElements(el, "_");
            inputSamples.add(t.get(0));
            inputTimes.add(t.get(1));
        }
        List<Integer> trSamples = this.getTrGenesSamples(inputSamples, dataset.getSampleNames());
        List<Integer> trTimes = this.getTrTimes(inputTimes, dataset.getTimeNames());
        r[0] = trSamples;
        r[1] = trTimes;
        return r;
    }

    private List<Integer> getTrGenesSamples(Set<String> samples, String[] genesOrSampleNames) {
        ArrayList<Integer> r = new ArrayList<Integer>(samples.size());
        for (String e : samples) {
            boolean enc = false;
            int index = 0;
            for (int i = 0; i < genesOrSampleNames.length && !enc; ++i) {
                if (!genesOrSampleNames[i].equalsIgnoreCase(e)) continue;
                index = i;
                enc = true;
            }
            r.add(new Integer(index));
        }
        Collections.sort(r);
        return r;
    }

    private List<Integer> getTrTimes(Set<String> times, String[] timeNames) {
        ArrayList<Integer> r = new ArrayList<Integer>(times.size());
        for (String e : times) {
            boolean enc = false;
            int index = 0;
            for (int i = 0; i < timeNames.length && !enc; ++i) {
                if (!timeNames[i].contains(e)) continue;
                index = i;
                enc = true;
            }
            r.add(new Integer(index));
        }
        Collections.sort(r);
        return r;
    }

    private void checkLists(List<Integer> genes, List<Integer> samples, List<Integer> times, Biological dataset) {
        LOG.debug("O g [" + genes.size() + "] = " + this.matchElementNames(genes, dataset.getGeneNames()));
        LOG.debug("O s [" + samples.size() + "] = " + this.matchElementNames(samples, dataset.getSampleNames()));
        LOG.debug("O t [" + times.size() + "] = " + this.matchElementNames(times, dataset.getTimeNames()));
    }

    private List<String> matchElementNames(List<Integer> elementIndexes, String[] elementNames) {
        ArrayList<String> elements = new ArrayList<String>(elementIndexes.size());
        for (Integer genIndex : elementIndexes) {
            elements.add(elementNames[genIndex]);
        }
        return elements;
    }
}

