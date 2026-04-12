/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.ui;

import de.erichseifert.gral.io.IOCapabilities;
import de.erichseifert.gral.ui.DrawableWriterFilter;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFileChooser;

public class ExportChooser
extends JFileChooser {
    private static final long serialVersionUID = -7885235526259131711L;

    public ExportChooser(boolean bl, List<IOCapabilities> object) {
        this.setAcceptAllFileFilterUsed(!bl);
        Iterator<IOCapabilities> iterator = object.iterator();
        while (iterator.hasNext()) {
            object = iterator.next();
            this.addChoosableFileFilter(new DrawableWriterFilter((IOCapabilities)object));
        }
    }
}

