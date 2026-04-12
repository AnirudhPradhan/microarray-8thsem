/*
 * Decompiled with CFR 0.152.
 */
package org.knowm.xchart;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import org.knowm.xchart.internal.Series_AxesChart;

public class CSVExporter {
    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void writeCSVRows(Series_AxesChart series, String path2Dir) {
        File newFile = new File(path2Dir + series.getName() + ".csv");
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(newFile), "UTF8"));
            String csv = CSVExporter.join(series.getXData(), ",") + System.getProperty("line.separator");
            out.write(csv);
            csv = CSVExporter.join(series.getYData(), ",") + System.getProperty("line.separator");
            out.write(csv);
            if (series.getExtraValues() != null) {
                csv = CSVExporter.join(series.getExtraValues(), ",") + System.getProperty("line.separator");
                out.write(csv);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                }
                catch (IOException iOException) {}
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void writeCSVColumns(Series_AxesChart series, String path2Dir) {
        File newFile = new File(path2Dir + series.getName() + ".csv");
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(newFile), "UTF8"));
            Collection<?> xData = series.getXData();
            Collection<? extends Number> yData = series.getYData();
            Collection<? extends Number> errorBarData = series.getExtraValues();
            Iterator<?> itrx = xData.iterator();
            Iterator<? extends Number> itry = yData.iterator();
            Iterator<? extends Number> itrErrorBar = null;
            if (errorBarData != null) {
                itrErrorBar = errorBarData.iterator();
            }
            while (itrx.hasNext()) {
                Number xDataPoint = (Number)itrx.next();
                Number yDataPoint = itry.next();
                Number errorBarValue = null;
                if (itrErrorBar != null) {
                    errorBarValue = itrErrorBar.next();
                }
                StringBuilder sb = new StringBuilder();
                sb.append(xDataPoint + ",");
                sb.append(yDataPoint + ",");
                if (errorBarValue != null) {
                    sb.append(errorBarValue + ",");
                }
                sb.append(System.getProperty("line.separator"));
                out.write(sb.toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                }
                catch (IOException iOException) {}
            }
        }
    }

    private static String join(Collection<? extends Object> collection, String separator) {
        if (collection == null) {
            return null;
        }
        Iterator<? extends Object> iterator = collection.iterator();
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return first == null ? "" : first.toString();
        }
        StringBuffer buf = new StringBuffer(256);
        if (first != null) {
            buf.append(first);
        }
        while (iterator.hasNext()) {
            Object obj;
            if (separator != null) {
                buf.append(separator);
            }
            if ((obj = iterator.next()) == null) continue;
            buf.append(obj);
        }
        return buf.toString();
    }
}

