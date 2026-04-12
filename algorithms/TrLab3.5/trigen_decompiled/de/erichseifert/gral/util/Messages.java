/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public abstract class Messages {
    private static final ResourceBundle a = ResourceBundle.getBundle("messages");

    private Messages() {
    }

    public static String getString(String string) {
        try {
            return a.getString(string);
        }
        catch (MissingResourceException missingResourceException) {
            return "!" + string + '!';
        }
    }
}

