/*
 * Decompiled with CFR 0.152.
 */
package utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.FileSystems;
import java.util.Locale;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.TextUtilities;

public class SystemUtilities {
    private static final Logger LOG = LoggerFactory.getLogger(SystemUtilities.class);
    private static final String ASSOCIATIONS_FOLDER_NAME = "associations";
    private static final String TERMS_FOLDER_NAME = "terms";
    private static final String RESOURCES_FOLDER_NAME = "resources";
    private static final String RESOURCES_XML_NAME = "resources.xml";
    private static final String SYS_FILE_NAME = "system.properties";
    private static final String ALG_FILE_NAME = "algorithm.properties";
    private static final String LAB_FILE_NAME = "laboratory.properties";
    private static final String GEN_FILE_NAME = "general.properties";
    private static final String GUI_FILE_NAME = "gui.properties";
    private static final String ES_FILE_NAME = "es.properties";
    private static final String EN_FILE_NAME = "en.properties";
    private static String SEP = FileSystems.getDefault().getSeparator();
    private static Properties SYS = new Properties();
    private static Properties ALG = new Properties();
    private static Properties LAB = new Properties();
    private static Properties GEN = new Properties();
    private static Properties GUI = new Properties();
    private static Properties ES = new Properties();
    private static Properties EN = new Properties();
    private static SystemUtilities singleton;

    private SystemUtilities() {
    }

    public static SystemUtilities getInstance() {
        return singleton;
    }

    public static Properties getSystemProperties() {
        return SYS;
    }

    public static String getSystemProperty(String key) {
        return SYS.getProperty(key);
    }

    public static Properties getAlgorithmProperties() {
        return ALG;
    }

    public static String getAlgorithmProperty(String key) {
        return ALG.getProperty(key);
    }

    public static Properties getLaboratoryProperties() {
        return LAB;
    }

    public static String getLaboratoryProperty(String key) {
        return LAB.getProperty(key);
    }

    public static Properties getGeneralProperties() {
        return GEN;
    }

    public static String getGeneralProperty(String key) {
        return GEN.getProperty(key);
    }

    public static Properties getGuiProperties() {
        return GUI;
    }

    public static String getGuiProperty(String key) {
        return GUI.getProperty(key);
    }

    public static Properties getLabelProperties() {
        Properties r = EN;
        if (Locale.getDefault().getLanguage().equals(new Locale("es").getLanguage())) {
            r = ES;
        }
        return r;
    }

    public static void changeLocale() {
        String loc = SYS.getProperty("default_locale");
        if (!loc.equalsIgnoreCase("default")) {
            Locale.setDefault(new Locale(loc));
        }
    }

    public static String getLabelProperty(String key) {
        Properties l_prop = SystemUtilities.getLabelProperties();
        return l_prop.getProperty(key);
    }

    public static String getGOappPath() {
        String r = "";
        String goAppNAme = SystemUtilities.getSystemProperties().getProperty("go_app_path");
        if (SystemUtilities.isDeveloping()) {
            String aux = TextUtilities.getCorrectPathFromHome(SystemUtilities.getSystemProperties().getProperty("resources_root"));
            r = TextUtilities.getRootPathWithSlash(aux);
            r = r + goAppNAme;
        } else {
            r = SystemUtilities.appendToAppPath(goAppNAme);
        }
        return r;
    }

    public static String getGOassociationPath(String organism) {
        String r = "";
        String goAssocName = SystemUtilities.getSystemProperties().getProperty(organism);
        if (SystemUtilities.isDeveloping()) {
            r = r + TextUtilities.getCorrectPathFromHome(SystemUtilities.getSystemProperties().getProperty("resources_root"));
            r = r + SEP + ASSOCIATIONS_FOLDER_NAME + SEP + goAssocName;
        } else {
            r = SystemUtilities.appendToAppPath(RESOURCES_FOLDER_NAME, ASSOCIATIONS_FOLDER_NAME, goAssocName);
        }
        return r;
    }

    public static String getGOtermsPath() {
        String r = "";
        String goTermsName = SystemUtilities.getSystemProperties().getProperty("term.complete");
        if (SystemUtilities.isDeveloping()) {
            r = r + TextUtilities.getCorrectPathFromHome(SystemUtilities.getSystemProperties().getProperty("resources_root"));
            r = r + SEP + TERMS_FOLDER_NAME + SEP + goTermsName;
        } else {
            r = SystemUtilities.appendToAppPath(RESOURCES_FOLDER_NAME, TERMS_FOLDER_NAME, goTermsName);
        }
        return r;
    }

    public static String getResourcesFolderPath() {
        String r = "";
        r = SystemUtilities.isDeveloping() ? TextUtilities.getCorrectPathFromHome(SystemUtilities.getSystemProperties().getProperty("resources_root")) : SystemUtilities.appendToAppPath(RESOURCES_FOLDER_NAME);
        return r;
    }

    public static String getResourcesXmlPath() {
        String r = "";
        r = SystemUtilities.isDeveloping() ? TextUtilities.getCorrectPathFromHome(SystemUtilities.getSystemProperties().getProperty("resources_path")) : SystemUtilities.appendToAppPath(RESOURCES_FOLDER_NAME, RESOURCES_XML_NAME);
        return r;
    }

    public static String getFileSep() {
        return SEP;
    }

    public static String getAppPath() {
        File auxfile = new File(SystemUtilities.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String appPathAux = auxfile.getParent();
        String appPath = null;
        try {
            appPath = URLDecoder.decode(appPathAux, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LOG.info("appPathAux = " + appPathAux);
        LOG.info("appPath = " + appPath);
        return appPath;
    }

    private static boolean isDeveloping() {
        String dev = SystemUtilities.getSystemProperties().getProperty("dev");
        return Boolean.parseBoolean(dev);
    }

    private static String appendToAppPath(String ... name) {
        String r = "";
        r = SystemUtilities.getAppPath() + SEP;
        for (int i = 0; i < name.length; ++i) {
            r = i == name.length - 1 ? r + name[i] : r + name[i] + SEP;
        }
        return r;
    }

    static {
        URL url_sys = ClassLoader.getSystemResource(SYS_FILE_NAME);
        URL url_alg = ClassLoader.getSystemResource(ALG_FILE_NAME);
        URL url_lab = ClassLoader.getSystemResource(LAB_FILE_NAME);
        URL url_gen = ClassLoader.getSystemResource(GEN_FILE_NAME);
        URL url_gui = ClassLoader.getSystemResource(GUI_FILE_NAME);
        URL url_es = ClassLoader.getSystemResource(ES_FILE_NAME);
        URL url_en = ClassLoader.getSystemResource(EN_FILE_NAME);
        try {
            SYS.load(url_sys.openStream());
            ALG.load(url_alg.openStream());
            LAB.load(url_lab.openStream());
            GEN.load(url_gen.openStream());
            GUI.load(url_gui.openStream());
            ES.load(url_es.openStream());
            EN.load(url_en.openStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        singleton = new SystemUtilities();
    }
}

