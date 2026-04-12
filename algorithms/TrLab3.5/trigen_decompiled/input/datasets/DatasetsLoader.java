/*
 * Decompiled with CFR 0.152.
 */
package input.datasets;

import input.datasets.Assorted;
import input.datasets.Biological;
import input.datasets.Common;
import input.datasets.Real;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import utils.TextUtilities;

public class DatasetsLoader {
    private static final Logger LOG = LoggerFactory.getLogger(DatasetsLoader.class);
    private String xmlPath;
    private Document xmlDoc;
    private Element root;
    private String rPath;

    public DatasetsLoader(String resourcesPath) {
        this.rPath = TextUtilities.getRootPathWithSlash(resourcesPath);
        this.xmlPath = resourcesPath;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            this.xmlDoc = builder.parse(new File(this.xmlPath));
            this.root = this.xmlDoc.getDocumentElement();
        }
        catch (ParserConfigurationException e) {
            LOG.error("Parser error");
            e.printStackTrace();
        }
        catch (SAXException e) {
            LOG.error("XML format error");
            e.printStackTrace();
        }
        catch (IOException e) {
            LOG.error("File error");
            e.printStackTrace();
        }
    }

    public Map<String, String> getAllDatasetsNames() {
        HashMap<String, String> r = new HashMap<String, String>();
        NodeList datasetNodes = this.root.getElementsByTagName("dataset");
        for (int i = 0; i < datasetNodes.getLength(); ++i) {
            Node currentDatasetNode = datasetNodes.item(i);
            if (currentDatasetNode.getNodeType() != 1) continue;
            NamedNodeMap attrib = currentDatasetNode.getAttributes();
            String id = attrib.getNamedItem("id").getNodeValue();
            String name = attrib.getNamedItem("name").getNodeValue();
            r.put(name, id);
        }
        return r;
    }

    public Common getResourcesByName(String dataset) {
        String genesPath;
        Real r = null;
        NodeList datasetNodes = this.root.getElementsByTagName("dataset");
        Node datasetNode = null;
        boolean found = false;
        for (int i = 0; i < datasetNodes.getLength() && !found; ++i) {
            NamedNodeMap attrib;
            String name;
            Node currentDatasetNode = datasetNodes.item(i);
            if (currentDatasetNode.getNodeType() != 1 || !(name = (attrib = currentDatasetNode.getAttributes()).getNamedItem("name").getNodeValue()).equalsIgnoreCase(dataset)) continue;
            found = true;
            datasetNode = currentDatasetNode;
        }
        NamedNodeMap att = datasetNode.getAttributes();
        String dID = att.getNamedItem("id").getNodeValue();
        String gS = att.getNamedItem("geneSize").getNodeValue();
        String cS = att.getNamedItem("sampleSize").getNodeValue();
        String tS = att.getNamedItem("timeSize").getNodeValue();
        String or = att.getNamedItem("organism").getNodeValue();
        String ty = att.getNamedItem("type").getNodeValue();
        String minG = att.getNamedItem("minG").getNodeValue();
        String maxG = att.getNamedItem("maxG").getNodeValue();
        String minC = att.getNamedItem("minC").getNodeValue();
        String maxC = att.getNamedItem("maxC").getNodeValue();
        String minT = att.getNamedItem("minT").getNodeValue();
        String maxT = att.getNamedItem("maxT").getNodeValue();
        NodeList nodeResources = ((Element)datasetNode).getElementsByTagName("resources");
        Node rsc = nodeResources.item(0);
        String sp = rsc.getAttributes().getNamedItem("separator").getNodeValue();
        NodeList datasetResources = rsc.getChildNodes();
        String[] paths = new String[Integer.parseInt(tS)];
        int j = 0;
        for (int i = 0; i < datasetResources.getLength(); ++i) {
            Node n = datasetResources.item(i);
            if (n.getNodeType() != 1) continue;
            paths[j] = TextUtilities.appendToPath(this.rPath, dID, n.getFirstChild().getNodeValue());
            ++j;
        }
        if (ty.equalsIgnoreCase("e")) {
            NodeList gn = ((Element)datasetNode).getElementsByTagName("genes");
            genesPath = TextUtilities.appendToPath(this.rPath, dID, gn.item(0).getFirstChild().getNodeValue());
            NodeList sm = ((Element)datasetNode).getElementsByTagName("samples");
            String samplesPath = TextUtilities.appendToPath(this.rPath, dID, sm.item(0).getFirstChild().getNodeValue());
            NodeList tm = ((Element)datasetNode).getElementsByTagName("times");
            String timesPath = TextUtilities.appendToPath(this.rPath, dID, tm.item(0).getFirstChild().getNodeValue());
            r = new Assorted(dID, dataset, ty, gS, cS, tS, paths, sp, minG, maxG, minC, maxC, minT, maxT, genesPath, samplesPath, timesPath);
        } else if (ty.equalsIgnoreCase("b")) {
            NodeList gn = ((Element)datasetNode).getElementsByTagName("genes");
            genesPath = TextUtilities.appendToPath(this.rPath, dID, gn.item(0).getFirstChild().getNodeValue());
            NodeList sm = ((Element)datasetNode).getElementsByTagName("samples");
            String samplesPath = TextUtilities.appendToPath(this.rPath, dID, sm.item(0).getFirstChild().getNodeValue());
            NodeList tm = ((Element)datasetNode).getElementsByTagName("times");
            String timesPath = TextUtilities.appendToPath(this.rPath, dID, tm.item(0).getFirstChild().getNodeValue());
            r = new Biological(dID, dataset, ty, gS, cS, tS, paths, sp, minG, maxG, minC, maxC, minT, maxT, genesPath, samplesPath, timesPath, or);
        }
        return r;
    }

    public void writeRecord(String id, String name, char type, int geneSize, int sampleSize, int timeSize, int minG, int maxG, int minC, int maxC, int minT, int maxT, String organism, String description, String sep, String[] dataFileNames, String geneFileName, String sampleFileName, String timeFileName) throws TransformerException {
        Element newDataset = this.xmlDoc.createElement("dataset");
        newDataset.setAttribute("id", id);
        newDataset.setAttribute("name", name);
        newDataset.setAttribute("type", "" + type);
        newDataset.setAttribute("geneSize", "" + geneSize);
        newDataset.setAttribute("sampleSize", "" + sampleSize);
        newDataset.setAttribute("timeSize", "" + timeSize);
        newDataset.setAttribute("minG", "" + minG);
        newDataset.setAttribute("maxG", "" + maxG);
        newDataset.setAttribute("minC", "" + minC);
        newDataset.setAttribute("maxC", "" + maxC);
        newDataset.setAttribute("minT", "" + minT);
        newDataset.setAttribute("maxT", "" + maxT);
        newDataset.setAttribute("organism", organism);
        newDataset.setAttribute("description", description);
        this.root.appendChild(newDataset);
        Element newResources = this.xmlDoc.createElement("resources");
        newResources.setAttribute("separator", sep);
        newDataset.appendChild(newResources);
        for (int i = 0; i < dataFileNames.length; ++i) {
            Element newResource = this.xmlDoc.createElement("resource");
            newResource.appendChild(this.xmlDoc.createTextNode(dataFileNames[i]));
            newResources.appendChild(newResource);
        }
        Element newGenes = this.xmlDoc.createElement("genes");
        newGenes.appendChild(this.xmlDoc.createTextNode(geneFileName));
        newDataset.appendChild(newGenes);
        Element newSamples = this.xmlDoc.createElement("samples");
        newSamples.appendChild(this.xmlDoc.createTextNode(sampleFileName));
        newDataset.appendChild(newSamples);
        Element newTimes = this.xmlDoc.createElement("times");
        newTimes.appendChild(this.xmlDoc.createTextNode(timeFileName));
        newDataset.appendChild(newTimes);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(this.xmlDoc);
        StreamResult result = new StreamResult(new File(this.xmlPath));
        transformer.transform(source, result);
    }
}

