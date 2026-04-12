/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.ui;

import de.erichseifert.gral.io.IOCapabilities;
import de.erichseifert.gral.util.Messages;
import java.io.File;
import java.text.MessageFormat;
import javax.swing.filechooser.FileFilter;

public class DrawableWriterFilter
extends FileFilter {
    private final IOCapabilities a;

    public DrawableWriterFilter(IOCapabilities iOCapabilities) {
        this.a = iOCapabilities;
    }

    @Override
    public boolean accept(File object) {
        if (object == null) {
            return false;
        }
        if (((File)object).isDirectory()) {
            return true;
        }
        int n = ((String)(object = ((File)object).getName())).lastIndexOf(46);
        object = (n <= 0 || n == ((String)object).length() - 1 ? "" : ((String)object).substring(n + 1)).toLowerCase();
        for (String string : this.a.getExtensions()) {
            if (!string.equals(object)) continue;
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return MessageFormat.format(Messages.getString("IO.formatDescription"), this.a.getFormat(), this.a.getName());
    }

    public IOCapabilities getWriterCapabilities() {
        return this.a;
    }
}

