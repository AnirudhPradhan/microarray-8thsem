/*
 * Decompiled with CFR 0.152.
 */
package utils;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SystemUtilities;

public class TextUtilities {
    private static final Logger LOG = LoggerFactory.getLogger(TextUtilities.class);

    public static String getFileName(String path) {
        File aux = new File(path);
        return aux.getName();
    }

    public static String removeExtensionFromPath(String path) {
        List<String> aux = TextUtilities.splitElements(path, ".");
        return aux.get(0);
    }

    public static String getFileExtension(String path) {
        String nameWith = TextUtilities.getFileName(path);
        List<String> aux = TextUtilities.splitElements(nameWith, ".");
        return aux.get(1);
    }

    public static String getFileNameWithoutExtension(String path) {
        String nameWith = TextUtilities.getFileName(path);
        List<String> aux = TextUtilities.splitElements(nameWith, ".");
        return aux.get(0);
    }

    public static String getRootPathWithSlash(String path) {
        File aux = new File(path);
        return aux.getParentFile().getAbsolutePath() + SystemUtilities.getFileSep();
    }

    public static String removeExtensionFile(String path) {
        String parentWithSlash = TextUtilities.getRootPathWithSlash(path);
        String nameWithOut = TextUtilities.getFileNameWithoutExtension(path);
        return parentWithSlash + nameWithOut;
    }

    public static String getCorrectPathFromHome(String path) {
        FileSystem fs = FileSystems.getDefault();
        String homePath = System.getProperty("user.home");
        String r = fs.getPath(homePath, path).toString();
        return r;
    }

    public static String appendToPath(String source, String ... tail) {
        FileSystem fs = FileSystems.getDefault();
        String r = fs.getPath(source, tail).toString();
        return r;
    }

    public static List<String> splitElements(String str, String sep) {
        ArrayList<String> v = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(str, sep);
        while (st.hasMoreTokens()) {
            v.add(st.nextToken());
        }
        v.trimToSize();
        return v;
    }

    public static List<String> splitElementsWithSep(String str, String sep) {
        ArrayList<String> v = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(str, sep, true);
        while (st.hasMoreTokens()) {
            v.add(st.nextToken());
        }
        v.trimToSize();
        return v;
    }

    public static List<Double> stringToDouble(String s, String sep) {
        ArrayList<Double> r = new ArrayList<Double>();
        List<String> c2 = TextUtilities.splitElements(s, sep);
        for (String iv : c2) {
            r.add(new Double(iv));
        }
        r.trimToSize();
        return r;
    }

    public static List<Double> stringToDouble(String s, String sep, int count) {
        ArrayList<Double> r = new ArrayList<Double>();
        List<String> c2 = TextUtilities.splitElements(s, sep);
        for (String iv : c2) {
            Double e;
            if (iv.equalsIgnoreCase("NA")) {
                e = new Double(0.0);
                LOG.info("NA in gene " + count + " = 0.0");
            } else {
                e = new Double(iv);
            }
            r.add(e);
        }
        r.trimToSize();
        return r;
    }

    public static int getIndexFromGOfileName(String name) {
        int r = -1;
        String snum = "";
        for (int i = 0; i < name.length(); ++i) {
            char c2 = name.charAt(i);
            if (!Character.isDigit(c2)) continue;
            snum = snum + c2;
        }
        r = Integer.parseInt(snum);
        return r;
    }

    public static DecimalFormat getDecimalFormat(char separator, String decimals) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(separator);
        DecimalFormat format = new DecimalFormat(decimals);
        format.setDecimalFormatSymbols(symbols);
        return format;
    }

    public static DecimalFormat getDecimalFormat(char separator) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(separator);
        DecimalFormat format = new DecimalFormat();
        format.setDecimalFormatSymbols(symbols);
        return format;
    }

    public static String vectorOfIntToString(int[] vector) {
        return TextUtilities.vectorOfIntToString(vector, vector.length, ",");
    }

    public static String vectorOfIntToString(int[] vector, int limit, String sep) {
        String r = "";
        int columns = vector.length;
        r = "";
        int i = 0;
        for (int j = 0; j < columns; ++j) {
            if (i == limit - 1) {
                r = r + vector[j] + "\n";
                i = 0;
                continue;
            }
            r = r + vector[j] + sep;
            ++i;
        }
        return r;
    }

    public static String vectorOfDoubleToString(double[] vector, int limit) {
        return TextUtilities.vectorOfDoubleToString(vector, limit, ",", '.', "#.#");
    }

    public static String vectorOfDoubleToString(double[] vector, int limit, String sep, char decimalSeparator, String decimalPattern) {
        String r = "";
        DecimalFormat format = TextUtilities.getDecimalFormat(decimalSeparator, decimalPattern);
        int columns = vector.length;
        r = "";
        int i = 0;
        for (int j = 0; j < columns; ++j) {
            if (i == limit - 1) {
                r = r + format.format(vector[j]) + "\n";
                i = 0;
                continue;
            }
            r = r + format.format(vector[j]) + sep;
            ++i;
        }
        return r;
    }

    public static String tableOfDoubleToString(double[][] table, String sep, char decimalSeparator, String decimalPattern) {
        String r = "";
        DecimalFormat format = TextUtilities.getDecimalFormat(decimalSeparator, decimalPattern);
        int rows = table.length;
        int columns = table[0].length;
        r = rows + " , " + columns + "\n";
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                r = j == columns - 1 ? r + format.format(table[i][j]) + "\n" : r + format.format(table[i][j]) + sep;
            }
        }
        return r;
    }

    public static String cubeOfDoubleToString(double[][][] cube, String sep, char decimalSeparator, String decimalPattern) {
        String r = "";
        int rsize = cube.length;
        int csize = cube[0].length;
        int dsize = cube[0][0].length;
        DecimalFormat format = TextUtilities.getDecimalFormat(decimalSeparator, decimalPattern);
        String sufix = "";
        for (int l = 0; l < dsize; ++l) {
            for (int i = 0; i < rsize; ++i) {
                for (int j = 0; j < csize; ++j) {
                    sufix = j == csize - 1 ? "\n" : sep;
                    r = r + format.format(cube[i][j][l]) + sufix;
                }
            }
            r = r + "\n";
        }
        return r;
    }

    public static String getTimeString(long t1, long t2) {
        return TextUtilities.getTimeString(t2 - t1);
    }

    public static String getTimeString(long t) {
        String r = "";
        double mill = t;
        double seconds = mill / 1000.0;
        double hou = mill / 1000.0 / 60.0 / 60.0;
        int ehou = (int)hou;
        double rhours = hou - (double)ehou;
        double min = rhours * 60.0;
        int emin = (int)min;
        double rmin = min - (double)emin;
        double sec = rmin * 60.0;
        int esec = (int)sec;
        double rsec = sec - (double)esec;
        int mil = (int)Math.ceil(rsec * 1000.0);
        r = "Execution time: " + ehou + " hours, " + emin + " minutes and " + esec + " seconds with " + mil + " thousandths" + " (" + seconds + " seconds)";
        return r;
    }
}

