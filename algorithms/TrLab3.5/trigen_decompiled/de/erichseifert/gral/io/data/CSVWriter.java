/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.io.data;

import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.io.IOCapabilities;
import de.erichseifert.gral.io.data.AbstractDataWriter;
import de.erichseifert.gral.util.Messages;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class CSVWriter
extends AbstractDataWriter {
    public static final String SEPARATOR_CHAR = "separator";

    public CSVWriter(String string) {
        super(string);
        if ("text/tab-separated-values".equals(string)) {
            this.setDefault(SEPARATOR_CHAR, Character.valueOf('\t'));
            return;
        }
        this.setDefault(SEPARATOR_CHAR, Character.valueOf(','));
    }

    @Override
    public void write(DataSource object, OutputStream closeable) throws IOException {
        Character c2 = (Character)this.getSetting(SEPARATOR_CHAR);
        closeable = new OutputStreamWriter((OutputStream)closeable);
        int n = 0;
        int n2 = object.getColumnCount();
        object = object.iterator();
        while (object.hasNext()) {
            Comparable comparable = (Comparable)object.next();
            ((Writer)closeable).write(String.valueOf(comparable));
            int n3 = n % n2;
            if (n3 < n2 - 1) {
                ((OutputStreamWriter)closeable).write(c2.charValue());
            } else {
                ((Writer)closeable).write("\r\n");
            }
            ++n;
        }
        ((OutputStreamWriter)closeable).close();
    }

    static {
        CSVWriter.addCapabilities(new IOCapabilities("CSV", Messages.getString("DataIO.csvDescription"), "text/csv", new String[]{"csv", "txt"}));
        CSVWriter.addCapabilities(new IOCapabilities("TSV", Messages.getString("DataIO.tsvDescription"), "text/tab-separated-values", new String[]{"tsv", "tab", "txt"}));
    }
}

