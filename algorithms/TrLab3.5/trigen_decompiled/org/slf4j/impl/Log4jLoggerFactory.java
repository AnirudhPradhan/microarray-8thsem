/*
 * Decompiled with CFR 0.152.
 */
package org.slf4j.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.log4j.LogManager;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.impl.Log4jLoggerAdapter;

public class Log4jLoggerFactory
implements ILoggerFactory {
    ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();

    public Logger getLogger(String name) {
        Logger slf4jLogger = (Logger)this.loggerMap.get(name);
        if (slf4jLogger != null) {
            return slf4jLogger;
        }
        org.apache.log4j.Logger log4jLogger = name.equalsIgnoreCase("ROOT") ? LogManager.getRootLogger() : LogManager.getLogger(name);
        Log4jLoggerAdapter newInstance = new Log4jLoggerAdapter(log4jLogger);
        Logger oldInstance = this.loggerMap.putIfAbsent(name, newInstance);
        return oldInstance == null ? newInstance : oldInstance;
    }
}

