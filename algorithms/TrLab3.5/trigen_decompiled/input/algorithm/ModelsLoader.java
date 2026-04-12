/*
 * Decompiled with CFR 0.152.
 */
package input.algorithm;

import input.algorithm.Implementation;
import input.algorithm.InvalidImplementationException;
import input.algorithm.Model;
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ModelsLoader {
    private static final Logger LOG = LoggerFactory.getLogger(ModelsLoader.class);
    private static final String NONE = "none";
    private static final String MODEL_XML_TAG = "model";
    private static final String NAME_XML_TAG = "name";
    private static final String INDIVIDUAL_XML_TAG = "individual";
    private static final String INDIVIDUALS_XML_TAG = "individuals";
    private static final String FITNESSFUNCTIONS_XML_TAG = "fitnessfunctions";
    private static final String FITNESS_XML_TAG = "fitness";
    private static final String DATAHIERARCHIES_XML_TAG = "datahierarchies";
    private static final String STOPPINGCRITERIA_XML_TAG = "stoppingcriteria";
    private static final String SOLUTIONCRITERIA_XML_TAG = "solutioncriteria";
    private static final String INITIALPOPS_XML_TAG = "initialpops";
    private static final String SELECTIONS_XML_TAG = "selections";
    private static final String CROSSOVERS_XML_TAG = "crossovers";
    private static final String MUTATIONS_XML_TAG = "mutations";
    private Element root;

    public ModelsLoader() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL model_url = cl.getResource("models.xml");
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xmlDoc = builder.parse(model_url.openStream());
            this.root = xmlDoc.getDocumentElement();
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

    public Implementation checkImplementation(String ind, String fit, String dat, String sto, String sol, String ini, String sel, String cro, String mut) throws InvalidImplementationException {
        Implementation r = null;
        String errMessage = "";
        String tail = "";
        boolean impFound = false;
        NodeList nLmodel = this.root.getElementsByTagName(MODEL_XML_TAG);
        for (int i = 0; i < nLmodel.getLength() && !impFound; ++i) {
            Node currNmodel = nLmodel.item(i);
            if (currNmodel.getNodeType() != 1) continue;
            Element mod = (Element)currNmodel;
            String[] fitTr = null;
            String indImp = "";
            String dhImp = "";
            String stpCri = "";
            String solCri = "";
            String iniPop = "";
            String select = "";
            String cross = "";
            String mute = "";
            boolean correctImp = true;
            indImp = this.checkElementByName(mod, INDIVIDUALS_XML_TAG, ind);
            if (indImp.equalsIgnoreCase(NONE)) {
                correctImp = false;
                tail = tail + "Individual\n";
            }
            if ((fitTr = this.checkFitnessAndTransformation(mod.getElementsByTagName(FITNESSFUNCTIONS_XML_TAG), fit))[0].equalsIgnoreCase(NONE)) {
                correctImp = false;
                tail = tail + "Fitness\n";
            }
            if ((dhImp = this.checkElementByName(mod, DATAHIERARCHIES_XML_TAG, dat)).equalsIgnoreCase(NONE)) {
                correctImp = false;
                tail = tail + "Data Hierarchy\n";
            }
            if ((stpCri = this.checkElementByName(mod, STOPPINGCRITERIA_XML_TAG, sto)).equalsIgnoreCase(NONE)) {
                correctImp = false;
                tail = tail + "Stopping Criterion\n";
            }
            if ((solCri = this.checkElementByName(mod, SOLUTIONCRITERIA_XML_TAG, sol)).equalsIgnoreCase(NONE)) {
                correctImp = false;
                tail = tail + "Solution Criterion\n";
            }
            if ((iniPop = this.checkElementByName(mod, INITIALPOPS_XML_TAG, ini)).equalsIgnoreCase(NONE)) {
                correctImp = false;
                tail = tail + "Initial Population\n";
            }
            if ((select = this.checkElementByName(mod, SELECTIONS_XML_TAG, sel)).equalsIgnoreCase(NONE)) {
                correctImp = false;
                tail = tail + "Selection\n";
            }
            if ((cross = this.checkElementByName(mod, CROSSOVERS_XML_TAG, cro)).equalsIgnoreCase(NONE)) {
                correctImp = false;
                tail = tail + "Crossover\n";
            }
            if ((mute = this.checkElementByName(mod, MUTATIONS_XML_TAG, mut)).equalsIgnoreCase(NONE)) {
                correctImp = false;
                tail = tail + "Mutation\n";
            }
            if (correctImp) {
                r = new Implementation(indImp, fitTr[0], dhImp, stpCri, solCri, iniPop, select, cross, mute);
                impFound = true;
                continue;
            }
            String modN = mod.getAttribute(NAME_XML_TAG);
            errMessage = errMessage + "\nInvalid Implementation for " + modN + " model, failed elements:\n" + tail;
            tail = "";
        }
        if (!impFound) {
            throw new InvalidImplementationException(errMessage);
        }
        return r;
    }

    public Model[] getAvailableModels() {
        NodeList nLmodel = this.root.getElementsByTagName(MODEL_XML_TAG);
        Model[] r = new Model[nLmodel.getLength()];
        for (int i = 0; i < nLmodel.getLength(); ++i) {
            Node currNmodel = nLmodel.item(i);
            if (currNmodel.getNodeType() != 1) continue;
            Element mod = (Element)currNmodel;
            Model n = new Model(mod.getAttribute(NAME_XML_TAG));
            NodeList els = mod.getChildNodes();
            for (int j = 0; j < els.getLength(); ++j) {
                Node currEl = els.item(j);
                if (currEl.getNodeType() != 1) continue;
                this.putElements(currEl.getNodeName(), n, mod);
            }
            r[i] = n;
        }
        return r;
    }

    private void putElements(String element, Model curr, Element model) {
        NodeList aux1 = model.getElementsByTagName(element);
        Node aux2 = aux1.item(0);
        NodeList listOfelements = aux2.getChildNodes();
        for (int i = 0; i < listOfelements.getLength(); ++i) {
            Node currN = listOfelements.item(i);
            if (currN.getNodeType() != 1) continue;
            Element currEl = (Element)currN;
            String item = currEl.getAttribute(NAME_XML_TAG);
            String cl = currEl.getFirstChild().getNodeValue();
            if (element.equalsIgnoreCase(INDIVIDUALS_XML_TAG)) {
                curr.putIndividual(item, cl);
                continue;
            }
            if (element.equalsIgnoreCase(FITNESSFUNCTIONS_XML_TAG)) {
                curr.putFitness(item, cl.trim());
                continue;
            }
            if (element.equalsIgnoreCase(DATAHIERARCHIES_XML_TAG)) {
                curr.putDataHierarchy(item, cl);
                continue;
            }
            if (element.equalsIgnoreCase(STOPPINGCRITERIA_XML_TAG)) {
                curr.putStoppingCriterion(item, cl);
                continue;
            }
            if (element.equalsIgnoreCase(SOLUTIONCRITERIA_XML_TAG)) {
                curr.putSolutionCriterion(item, cl);
                continue;
            }
            if (element.equalsIgnoreCase(INITIALPOPS_XML_TAG)) {
                curr.putInitialPop(item, cl);
                continue;
            }
            if (element.equalsIgnoreCase(SELECTIONS_XML_TAG)) {
                curr.putSelection(item, cl);
                continue;
            }
            if (element.equalsIgnoreCase(CROSSOVERS_XML_TAG)) {
                curr.putCrossover(item, cl);
                continue;
            }
            if (!element.equalsIgnoreCase(MUTATIONS_XML_TAG)) continue;
            curr.putMutation(item, cl);
        }
    }

    private String checkElementByName(Element model, String element, String name) {
        String r = NONE;
        NodeList aux1 = model.getElementsByTagName(element);
        Node aux2 = aux1.item(0);
        NodeList listOfelements = aux2.getChildNodes();
        if (listOfelements.getLength() == 1) {
            Element el = (Element)aux2;
            String currName = el.getAttribute(NAME_XML_TAG);
            if (currName.equalsIgnoreCase(name)) {
                r = el.getFirstChild().getNodeValue();
            }
        } else {
            for (int i = 0; i < listOfelements.getLength(); ++i) {
                Element currEl;
                String currName;
                Node currN = listOfelements.item(i);
                if (currN.getNodeType() != 1 || !(currName = (currEl = (Element)currN).getAttribute(NAME_XML_TAG)).equalsIgnoreCase(name)) continue;
                r = currEl.getFirstChild().getNodeValue();
            }
        }
        return r;
    }

    private String[] checkFitnessAndTransformation(NodeList list, String fit) {
        String[] r = new String[]{NONE, NONE};
        Node node = list.item(0);
        NodeList listOfelements = ((Element)node).getElementsByTagName(FITNESS_XML_TAG);
        for (int i = 0; i < listOfelements.getLength(); ++i) {
            Element el;
            String name;
            Node currN = listOfelements.item(i);
            if (currN.getNodeType() != 1 || !fit.equalsIgnoreCase(name = (el = (Element)currN).getAttribute(NAME_XML_TAG))) continue;
            r[0] = currN.getFirstChild().getNodeValue().trim();
        }
        return r;
    }
}

