/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data;

import de.erichseifert.gral.data.DataChangeEvent;
import de.erichseifert.gral.data.DataSource;

public interface DataListener {
    public void dataAdded(DataSource var1, DataChangeEvent ... var2);

    public void dataUpdated(DataSource var1, DataChangeEvent ... var2);

    public void dataRemoved(DataSource var1, DataChangeEvent ... var2);
}

