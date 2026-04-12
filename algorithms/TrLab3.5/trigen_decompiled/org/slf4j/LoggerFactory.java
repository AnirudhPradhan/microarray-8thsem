/*
 * Decompiled with CFR 0.152.
 */
package org.slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.helpers.NOPLoggerFactory;
import org.slf4j.helpers.SubstituteLogger;
import org.slf4j.helpers.SubstituteLoggerFactory;
import org.slf4j.helpers.Util;
import org.slf4j.impl.StaticLoggerBinder;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class LoggerFactory {
    static final String CODES_PREFIX = "http://www.slf4j.org/codes.html";
    static final String NO_STATICLOGGERBINDER_URL = "http://www.slf4j.org/codes.html#StaticLoggerBinder";
    static final String MULTIPLE_BINDINGS_URL = "http://www.slf4j.org/codes.html#multiple_bindings";
    static final String NULL_LF_URL = "http://www.slf4j.org/codes.html#null_LF";
    static final String VERSION_MISMATCH = "http://www.slf4j.org/codes.html#version_mismatch";
    static final String SUBSTITUTE_LOGGER_URL = "http://www.slf4j.org/codes.html#substituteLogger";
    static final String UNSUCCESSFUL_INIT_URL = "http://www.slf4j.org/codes.html#unsuccessfulInit";
    static final String UNSUCCESSFUL_INIT_MSG = "org.slf4j.LoggerFactory could not be successfully initialized. See also http://www.slf4j.org/codes.html#unsuccessfulInit";
    static final int UNINITIALIZED = 0;
    static final int ONGOING_INITIALIZATION = 1;
    static final int FAILED_INITIALIZATION = 2;
    static final int SUCCESSFUL_INITIALIZATION = 3;
    static final int NOP_FALLBACK_INITIALIZATION = 4;
    static int INITIALIZATION_STATE = 0;
    static SubstituteLoggerFactory TEMP_FACTORY = new SubstituteLoggerFactory();
    static NOPLoggerFactory NOP_FALLBACK_FACTORY = new NOPLoggerFactory();
    private static final String[] API_COMPATIBILITY_LIST = new String[]{"1.6", "1.7"};
    private static String STATIC_LOGGER_BINDER_PATH = "org/slf4j/impl/StaticLoggerBinder.class";

    private LoggerFactory() {
    }

    static void reset() {
        INITIALIZATION_STATE = 0;
        TEMP_FACTORY = new SubstituteLoggerFactory();
    }

    private static final void performInitialization() {
        LoggerFactory.bind();
        if (INITIALIZATION_STATE == 3) {
            LoggerFactory.versionSanityCheck();
        }
    }

    private static boolean messageContainsOrgSlf4jImplStaticLoggerBinder(String msg) {
        if (msg == null) {
            return false;
        }
        if (msg.indexOf("org/slf4j/impl/StaticLoggerBinder") != -1) {
            return true;
        }
        return msg.indexOf("org.slf4j.impl.StaticLoggerBinder") != -1;
    }

    private static final void bind() {
        try {
            Set<URL> staticLoggerBinderPathSet = LoggerFactory.findPossibleStaticLoggerBinderPathSet();
            LoggerFactory.reportMultipleBindingAmbiguity(staticLoggerBinderPathSet);
            StaticLoggerBinder.getSingleton();
            INITIALIZATION_STATE = 3;
            LoggerFactory.reportActualBinding(staticLoggerBinderPathSet);
            LoggerFactory.fixSubstitutedLoggers();
        }
        catch (NoClassDefFoundError ncde) {
            String msg = ncde.getMessage();
            if (LoggerFactory.messageContainsOrgSlf4jImplStaticLoggerBinder(msg)) {
                INITIALIZATION_STATE = 4;
                Util.report("Failed to load class \"org.slf4j.impl.StaticLoggerBinder\".");
                Util.report("Defaulting to no-operation (NOP) logger implementation");
                Util.report("See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.");
            }
            LoggerFactory.failedBinding(ncde);
            throw ncde;
        }
        catch (NoSuchMethodError nsme) {
            String msg = nsme.getMessage();
            if (msg != null && msg.indexOf("org.slf4j.impl.StaticLoggerBinder.getSingleton()") != -1) {
                INITIALIZATION_STATE = 2;
                Util.report("slf4j-api 1.6.x (or later) is incompatible with this binding.");
                Util.report("Your binding is version 1.5.5 or earlier.");
                Util.report("Upgrade your binding to version 1.6.x.");
            }
            throw nsme;
        }
        catch (Exception e) {
            LoggerFactory.failedBinding(e);
            throw new IllegalStateException("Unexpected initialization failure", e);
        }
    }

    static void failedBinding(Throwable t) {
        INITIALIZATION_STATE = 2;
        Util.report("Failed to instantiate SLF4J LoggerFactory", t);
    }

    private static final void fixSubstitutedLoggers() {
        List<SubstituteLogger> loggers = TEMP_FACTORY.getLoggers();
        if (loggers.isEmpty()) {
            return;
        }
        Util.report("The following set of substitute loggers may have been accessed");
        Util.report("during the initialization phase. Logging calls during this");
        Util.report("phase were not honored. However, subsequent logging calls to these");
        Util.report("loggers will work as normally expected.");
        Util.report("See also http://www.slf4j.org/codes.html#substituteLogger");
        for (SubstituteLogger subLogger : loggers) {
            subLogger.setDelegate(LoggerFactory.getLogger(subLogger.getName()));
            Util.report(subLogger.getName());
        }
        TEMP_FACTORY.clear();
    }

    private static final void versionSanityCheck() {
        try {
            String requested = StaticLoggerBinder.REQUESTED_API_VERSION;
            boolean match = false;
            for (int i = 0; i < API_COMPATIBILITY_LIST.length; ++i) {
                if (!requested.startsWith(API_COMPATIBILITY_LIST[i])) continue;
                match = true;
            }
            if (!match) {
                Util.report("The requested version " + requested + " by your slf4j binding is not compatible with " + Arrays.asList(API_COMPATIBILITY_LIST).toString());
                Util.report("See http://www.slf4j.org/codes.html#version_mismatch for further details.");
            }
        }
        catch (NoSuchFieldError nsfe) {
        }
        catch (Throwable e) {
            Util.report("Unexpected problem occured during version sanity check", e);
        }
    }

    private static Set<URL> findPossibleStaticLoggerBinderPathSet() {
        LinkedHashSet<URL> staticLoggerBinderPathSet = new LinkedHashSet<URL>();
        try {
            ClassLoader loggerFactoryClassLoader = LoggerFactory.class.getClassLoader();
            Enumeration<URL> paths = loggerFactoryClassLoader == null ? ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH) : loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
            while (paths.hasMoreElements()) {
                URL path = paths.nextElement();
                staticLoggerBinderPathSet.add(path);
            }
        }
        catch (IOException ioe) {
            Util.report("Error getting resources from path", ioe);
        }
        return staticLoggerBinderPathSet;
    }

    private static boolean isAmbiguousStaticLoggerBinderPathSet(Set<URL> staticLoggerBinderPathSet) {
        return staticLoggerBinderPathSet.size() > 1;
    }

    private static void reportMultipleBindingAmbiguity(Set<URL> staticLoggerBinderPathSet) {
        if (LoggerFactory.isAmbiguousStaticLoggerBinderPathSet(staticLoggerBinderPathSet)) {
            Util.report("Class path contains multiple SLF4J bindings.");
            for (URL path : staticLoggerBinderPathSet) {
                Util.report("Found binding in [" + path + "]");
            }
            Util.report("See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.");
        }
    }

    private static void reportActualBinding(Set<URL> staticLoggerBinderPathSet) {
        if (LoggerFactory.isAmbiguousStaticLoggerBinderPathSet(staticLoggerBinderPathSet)) {
            Util.report("Actual binding is of type [" + StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr() + "]");
        }
    }

    public static Logger getLogger(String name) {
        ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
        return iLoggerFactory.getLogger(name);
    }

    public static Logger getLogger(Class clazz) {
        return LoggerFactory.getLogger(clazz.getName());
    }

    public static ILoggerFactory getILoggerFactory() {
        if (INITIALIZATION_STATE == 0) {
            INITIALIZATION_STATE = 1;
            LoggerFactory.performInitialization();
        }
        switch (INITIALIZATION_STATE) {
            case 3: {
                return StaticLoggerBinder.getSingleton().getLoggerFactory();
            }
            case 4: {
                return NOP_FALLBACK_FACTORY;
            }
            case 2: {
                throw new IllegalStateException(UNSUCCESSFUL_INIT_MSG);
            }
            case 1: {
                return TEMP_FACTORY;
            }
        }
        throw new IllegalStateException("Unreachable code");
    }
}

