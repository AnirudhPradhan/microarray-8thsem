/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

public abstract class LogXF {
    protected static final Level TRACE = new Level(5000, "TRACE", 7);
    private static final String FQCN = LogXF.class.getName();

    protected LogXF() {
    }

    protected static Boolean valueOf(boolean b2) {
        if (b2) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    protected static Character valueOf(char c2) {
        return new Character(c2);
    }

    protected static Byte valueOf(byte b2) {
        return new Byte(b2);
    }

    protected static Short valueOf(short b2) {
        return new Short(b2);
    }

    protected static Integer valueOf(int b2) {
        return new Integer(b2);
    }

    protected static Long valueOf(long b2) {
        return new Long(b2);
    }

    protected static Float valueOf(float b2) {
        return new Float(b2);
    }

    protected static Double valueOf(double b2) {
        return new Double(b2);
    }

    protected static Object[] toArray(Object param1) {
        return new Object[]{param1};
    }

    protected static Object[] toArray(Object param1, Object param2) {
        return new Object[]{param1, param2};
    }

    protected static Object[] toArray(Object param1, Object param2, Object param3) {
        return new Object[]{param1, param2, param3};
    }

    protected static Object[] toArray(Object param1, Object param2, Object param3, Object param4) {
        return new Object[]{param1, param2, param3, param4};
    }

    public static void entering(Logger logger, String sourceClass, String sourceMethod) {
        if (logger.isDebugEnabled()) {
            logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, sourceClass + "." + sourceMethod + " ENTRY", null));
        }
    }

    public static void entering(Logger logger, String sourceClass, String sourceMethod, String param) {
        if (logger.isDebugEnabled()) {
            String msg = sourceClass + "." + sourceMethod + " ENTRY " + param;
            logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, msg, null));
        }
    }

    public static void entering(Logger logger, String sourceClass, String sourceMethod, Object param) {
        if (logger.isDebugEnabled()) {
            String msg = sourceClass + "." + sourceMethod + " ENTRY ";
            if (param == null) {
                msg = msg + "null";
            } else {
                try {
                    msg = msg + param;
                }
                catch (Throwable ex) {
                    msg = msg + "?";
                }
            }
            logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, msg, null));
        }
    }

    public static void entering(Logger logger, String sourceClass, String sourceMethod, Object[] params) {
        if (logger.isDebugEnabled()) {
            String msg = sourceClass + "." + sourceMethod + " ENTRY ";
            if (params != null && params.length > 0) {
                String delim = "{";
                for (int i = 0; i < params.length; ++i) {
                    try {
                        msg = msg + delim + params[i];
                    }
                    catch (Throwable ex) {
                        msg = msg + delim + "?";
                    }
                    delim = ",";
                }
                msg = msg + "}";
            } else {
                msg = msg + "{}";
            }
            logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, msg, null));
        }
    }

    public static void exiting(Logger logger, String sourceClass, String sourceMethod) {
        if (logger.isDebugEnabled()) {
            logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, sourceClass + "." + sourceMethod + " RETURN", null));
        }
    }

    public static void exiting(Logger logger, String sourceClass, String sourceMethod, String result) {
        if (logger.isDebugEnabled()) {
            logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, sourceClass + "." + sourceMethod + " RETURN " + result, null));
        }
    }

    public static void exiting(Logger logger, String sourceClass, String sourceMethod, Object result) {
        if (logger.isDebugEnabled()) {
            String msg = sourceClass + "." + sourceMethod + " RETURN ";
            if (result == null) {
                msg = msg + "null";
            } else {
                try {
                    msg = msg + result;
                }
                catch (Throwable ex) {
                    msg = msg + "?";
                }
            }
            logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, msg, null));
        }
    }

    public static void throwing(Logger logger, String sourceClass, String sourceMethod, Throwable thrown) {
        if (logger.isDebugEnabled()) {
            logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, sourceClass + "." + sourceMethod + " THROW", thrown));
        }
    }
}

