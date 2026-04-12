/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;

public class CSVImporter {
    public static XYChart getChartFromCSVDir(String path2Directory, DataOrientation dataOrientation, int width, int height, Styler.ChartTheme chartTheme) {
        XYChart chart = null;
        chart = chartTheme != null ? new XYChart(width, height, chartTheme) : new XYChart(width, height);
        File[] csvFiles = CSVImporter.getAllFiles(path2Directory, ".*.csv");
        for (int i = 0; i < csvFiles.length; ++i) {
            File csvFile = csvFiles[i];
            String[] xAndYData = null;
            xAndYData = dataOrientation == DataOrientation.Rows ? CSVImporter.getSeriesDataFromCSVRows(csvFile) : CSVImporter.getSeriesDataFromCSVColumns(csvFile);
            if (xAndYData[2] == null || xAndYData[2].trim().equalsIgnoreCase("")) {
                chart.addSeries(csvFile.getName().substring(0, csvFile.getName().indexOf(".csv")), CSVImporter.getAxisData(xAndYData[0]), CSVImporter.getAxisData(xAndYData[1]));
                continue;
            }
            chart.addSeries(csvFile.getName().substring(0, csvFile.getName().indexOf(".csv")), CSVImporter.getAxisData(xAndYData[0]), CSVImporter.getAxisData(xAndYData[1]), CSVImporter.getAxisData(xAndYData[2]));
        }
        return chart;
    }

    public static XYChart getChartFromCSVDir(String path2Directory, DataOrientation dataOrientation, int width, int height) {
        return CSVImporter.getChartFromCSVDir(path2Directory, dataOrientation, width, height, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static String[] getSeriesDataFromCSVRows(File csvFile) {
        String[] xAndYData = new String[3];
        BufferedReader bufferedReader = null;
        try {
            int counter = 0;
            String line = null;
            bufferedReader = new BufferedReader(new FileReader(csvFile));
            while ((line = bufferedReader.readLine()) != null) {
                xAndYData[counter++] = line;
            }
        }
        catch (Exception e) {
            System.out.println("Exception while reading csv file: " + e);
        }
        finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return xAndYData;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static String[] getSeriesDataFromCSVColumns(File csvFile) {
        String[] xAndYData = new String[]{"", "", ""};
        BufferedReader bufferedReader = null;
        try {
            String line = null;
            bufferedReader = new BufferedReader(new FileReader(csvFile));
            while ((line = bufferedReader.readLine()) != null) {
                String[] dataArray = line.split(",");
                xAndYData[0] = xAndYData[0] + dataArray[0] + ",";
                xAndYData[1] = xAndYData[1] + dataArray[1] + ",";
                if (dataArray.length <= 2) continue;
                xAndYData[2] = xAndYData[2] + dataArray[2] + ",";
            }
        }
        catch (Exception e) {
            System.out.println("Exception while reading csv file: " + e);
        }
        finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return xAndYData;
    }

    private static List<Number> getAxisData(String stringData) {
        ArrayList<Number> axisData = new ArrayList<Number>();
        String[] stringDataArray = stringData.split(",");
        for (int i = 0; i < stringDataArray.length; ++i) {
            String dataPoint = stringDataArray[i];
            try {
                Double value = Double.parseDouble(dataPoint);
                axisData.add(value);
                continue;
            }
            catch (NumberFormatException e) {
                System.out.println("Error parsing >" + dataPoint + "< !");
                throw e;
            }
        }
        return axisData;
    }

    public static File[] getAllFiles(String dirName, String regex) {
        File[] allFiles = CSVImporter.getAllFiles(dirName);
        ArrayList<File> matchingFiles = new ArrayList<File>();
        for (int i = 0; i < allFiles.length; ++i) {
            if (!allFiles[i].getName().matches(regex)) continue;
            matchingFiles.add(allFiles[i]);
        }
        return matchingFiles.toArray(new File[matchingFiles.size()]);
    }

    public static File[] getAllFiles(String dirName) {
        File dir = new File(dirName);
        File[] files = dir.listFiles();
        if (files != null) {
            ArrayList<File> filteredFiles = new ArrayList<File>();
            for (int i = 0; i < files.length; ++i) {
                if (!files[i].isFile()) continue;
                filteredFiles.add(files[i]);
            }
            return filteredFiles.toArray(new File[filteredFiles.size()]);
        }
        System.out.println(dirName + " does not denote a valid directory!");
        return new File[0];
    }

    public static enum DataOrientation {
        Rows,
        Columns;

    }
}

