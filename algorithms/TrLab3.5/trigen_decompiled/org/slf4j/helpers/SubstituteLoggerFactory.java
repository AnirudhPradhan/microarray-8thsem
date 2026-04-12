/*
 * Decompiled with CFR 0.152.
 */
package org.slf4j.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.helpers.SubstituteLogger;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SubstituteLoggerFactory
implements ILoggerFactory {
    final ConcurrentMap<String, SubstituteLogger> loggers = new ConcurrentHashMap<String, SubstituteLogger>();

    @Override
    public Logger getLogger(String name) {
        SubstituteLogger oldLogger;
        SubstituteLogger logger = (SubstituteLogger)this.loggers.get(name);
        if (logger == null && (oldLogger = this.loggers.putIfAbsent(name, logger = new SubstituteLogger(name))) != null) {
            logger = oldLogger;
        }
        return logger;
    }

    public List<String> getLoggerNames() {
        return new ArrayList<String>(this.loggers.keySet());
    }

    public List<SubstituteLogger> getLoggers() {
        return new ArrayList<SubstituteLogger>(this.loggers.values());
    }

    public void clear() {
        this.loggers.clear();
    }
}

