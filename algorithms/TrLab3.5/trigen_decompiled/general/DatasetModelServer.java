/*
 * Decompiled with CFR 0.152.
 */
package general;

import input.algorithm.Model;
import input.algorithm.ModelsLoader;
import input.datasets.Common;
import input.datasets.DatasetsLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.transform.TransformerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SystemUtilities;

public class DatasetModelServer {
    private static final Logger LOG = LoggerFactory.getLogger(DatasetModelServer.class);
    private static DatasetsLoader datasetServer;
    private static ModelsLoader modelServer;
    private static final DatasetModelServer singleton;

    private DatasetModelServer() {
    }

    public static DatasetModelServer getInstance() {
        return singleton;
    }

    public void updateServers() {
        datasetServer = new DatasetsLoader(SystemUtilities.getResourcesXmlPath());
        modelServer = new ModelsLoader();
    }

    public void updateDatasetServer() {
        datasetServer = new DatasetsLoader(SystemUtilities.getResourcesXmlPath());
    }

    public void updateModelServer() {
        modelServer = new ModelsLoader();
    }

    public DatasetsLoader getDatasetServer() {
        return datasetServer;
    }

    public ModelsLoader getModelServer() {
        return modelServer;
    }

    public Common getDatasetByName(String datasetName) {
        return datasetServer.getResourcesByName(datasetName);
    }

    public boolean isRepeated(String datasetName) {
        boolean enc = false;
        String[] items = this.getDatasetItems();
        for (int i = 0; i < items.length && !enc; ++i) {
            String aux = items[i];
            if (!aux.equalsIgnoreCase(datasetName)) continue;
            enc = true;
        }
        return enc;
    }

    public String getAnewID() {
        String r = "";
        String[] ids = this.getDatasetIDs();
        int max = 0;
        for (int i = 0; i < ids.length; ++i) {
            int aux = Integer.parseInt(ids[i]);
            if (aux <= max) continue;
            max = aux;
        }
        Integer aux1 = new Integer(++max);
        int digits = aux1.toString().length();
        int zeros = 4 - digits;
        for (int i = 0; i < zeros; ++i) {
            r = r + "0";
        }
        r = r + max;
        LOG.debug("Max = " + max);
        LOG.debug("Digits = " + digits);
        LOG.debug("Zeros = " + zeros);
        LOG.debug("ID = " + r);
        return r;
    }

    public String[] getDatasetIDs() {
        Map<String, String> datasets = datasetServer.getAllDatasetsNames();
        Set<String> keys1 = datasets.keySet();
        String[] dataset_ids = new String[keys1.size()];
        int i = 0;
        for (String k : keys1) {
            dataset_ids[i] = datasets.get(k);
            ++i;
        }
        return dataset_ids;
    }

    public String[] getDatasetItems() {
        Map<String, String> datasets = datasetServer.getAllDatasetsNames();
        Set<String> keys1 = datasets.keySet();
        String[] dataset_items = new String[keys1.size()];
        int i = 0;
        Iterator<String> iterator = keys1.iterator();
        while (iterator.hasNext()) {
            String k;
            dataset_items[i] = k = iterator.next();
            ++i;
        }
        return dataset_items;
    }

    public String[] getFitnessItems() {
        Model[] models = modelServer.getAvailableModels();
        Map<String, Model.FitnessClass> fit = models[0].getFitness();
        Set<String> keys = fit.keySet();
        String[] fit_items = new String[keys.size()];
        int i = 0;
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String k;
            fit_items[i] = k = iterator.next();
            ++i;
        }
        return fit_items;
    }

    public List<String[]> getImplementationItems() {
        ArrayList<String[]> r = new ArrayList<String[]>(9);
        Model[] models = modelServer.getAvailableModels();
        Map<String, Model.FitnessClass> fit = models[0].getFitness();
        Set<String> keys = fit.keySet();
        String[] fit_items = new String[keys.size()];
        int i = 0;
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String k2;
            fit_items[i] = k2 = iterator.next();
            ++i;
        }
        r.add(fit_items);
        Map<String, String> aux = models[0].getIndividuals();
        keys = aux.keySet();
        String[] ind_items = new String[keys.size()];
        i = 0;
        Iterator<String> iterator2 = keys.iterator();
        while (iterator2.hasNext()) {
            String string;
            ind_items[i] = string = iterator2.next();
            ++i;
        }
        r.add(ind_items);
        aux = models[0].getDataHierarchies();
        keys = aux.keySet();
        String[] hie_items = new String[keys.size()];
        i = 0;
        Iterator<String> iterator3 = keys.iterator();
        while (iterator3.hasNext()) {
            String string;
            hie_items[i] = string = iterator3.next();
            ++i;
        }
        r.add(hie_items);
        aux = models[0].getStoppingCriteria();
        keys = aux.keySet();
        String[] stringArray = new String[keys.size()];
        i = 0;
        Iterator<String> iterator4 = keys.iterator();
        while (iterator4.hasNext()) {
            String string;
            stringArray[i] = string = iterator4.next();
            ++i;
        }
        r.add(stringArray);
        aux = models[0].getSolutionCriteria();
        keys = aux.keySet();
        String[] stringArray2 = new String[keys.size()];
        i = 0;
        Iterator<String> iterator5 = keys.iterator();
        while (iterator5.hasNext()) {
            String string;
            stringArray2[i] = string = iterator5.next();
            ++i;
        }
        r.add(stringArray2);
        aux = models[0].getInitialPops();
        keys = aux.keySet();
        String[] stringArray3 = new String[keys.size()];
        i = 0;
        Iterator<String> iterator6 = keys.iterator();
        while (iterator6.hasNext()) {
            String string;
            stringArray3[i] = string = iterator6.next();
            ++i;
        }
        r.add(stringArray3);
        aux = models[0].getSelections();
        keys = aux.keySet();
        String[] stringArray4 = new String[keys.size()];
        i = 0;
        Iterator<String> iterator7 = keys.iterator();
        while (iterator7.hasNext()) {
            String string;
            stringArray4[i] = string = iterator7.next();
            ++i;
        }
        r.add(stringArray4);
        aux = models[0].getCrossovers();
        keys = aux.keySet();
        String[] stringArray5 = new String[keys.size()];
        i = 0;
        Iterator<String> iterator8 = keys.iterator();
        while (iterator8.hasNext()) {
            String k9;
            stringArray5[i] = k9 = iterator8.next();
            ++i;
        }
        r.add(stringArray5);
        aux = models[0].getMutations();
        keys = aux.keySet();
        String[] stringArray6 = new String[keys.size()];
        i = 0;
        Iterator<String> iterator9 = keys.iterator();
        while (iterator9.hasNext()) {
            String k10;
            stringArray6[i] = k10 = iterator9.next();
            ++i;
        }
        r.add(stringArray6);
        return r;
    }

    public void writeNewDataset(String id, String name, char type, int geneSize, int sampleSize, int timeSize, int minG, int maxG, int minC, int maxC, int minT, int maxT, String organism, String description, String sep, String[] dataFileNames, String geneFileName, String sampleFileName, String timeFileName) throws TransformerException {
        datasetServer.writeRecord(id, name, type, geneSize, sampleSize, timeSize, minG, maxG, minC, maxC, minT, maxT, organism, description, sep, dataFileNames, geneFileName, sampleFileName, timeFileName);
    }

    static {
        singleton = new DatasetModelServer();
        datasetServer = new DatasetsLoader(SystemUtilities.getResourcesXmlPath());
        modelServer = new ModelsLoader();
    }
}

