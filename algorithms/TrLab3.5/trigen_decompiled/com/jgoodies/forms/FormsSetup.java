/*
 * Decompiled with CFR 0.152.
 */
package com.jgoodies.forms;

import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.factories.DefaultComponentFactory;

public class FormsSetup {
    private static final String DEBUG_TOOL_TIPS_ENABLED_KEY = "FormsSetup.debugToolTipsEnabled";
    private static ComponentFactory componentFactoryDefault;
    private static boolean labelForFeatureEnabledDefault;
    private static boolean opaqueDefault;
    private static boolean debugToolTipsEnabled;

    private FormsSetup() {
    }

    public static ComponentFactory getComponentFactoryDefault() {
        if (componentFactoryDefault == null) {
            componentFactoryDefault = new DefaultComponentFactory();
        }
        return componentFactoryDefault;
    }

    public static void setComponentFactoryDefault(ComponentFactory factory) {
        componentFactoryDefault = factory;
    }

    public static boolean getLabelForFeatureEnabledDefault() {
        return labelForFeatureEnabledDefault;
    }

    public static void setLabelForFeatureEnabledDefault(boolean b2) {
        labelForFeatureEnabledDefault = b2;
    }

    public static boolean getOpaqueDefault() {
        return opaqueDefault;
    }

    public static void setOpaqueDefault(boolean b2) {
        opaqueDefault = b2;
    }

    public static boolean getDebugToolTipsEnabledDefault() {
        return debugToolTipsEnabled;
    }

    public static void setDebugToolTipsEnabled(boolean b2) {
        debugToolTipsEnabled = b2;
    }

    private static boolean getDebugToolTipSystemProperty() {
        try {
            String value = System.getProperty(DEBUG_TOOL_TIPS_ENABLED_KEY);
            return "true".equalsIgnoreCase(value);
        }
        catch (SecurityException e) {
            return false;
        }
    }

    static {
        labelForFeatureEnabledDefault = true;
        opaqueDefault = false;
        debugToolTipsEnabled = FormsSetup.getDebugToolTipSystemProperty();
    }
}

