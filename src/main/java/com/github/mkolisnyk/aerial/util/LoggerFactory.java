package com.github.mkolisnyk.aerial.util;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public final class LoggerFactory {

    private LoggerFactory() {
        ConsoleAppender console = new ConsoleAppender(); //create appender
        //configure the appender
        String pattern = "%d [%p|%c|%C{1}] %m%n";
        console.setLayout(new PatternLayout(pattern));
        console.activateOptions();
        //add appender to any Logger (here is root)
        Logger.getRootLogger().addAppender(console);
    }

    public Logger getLog(Class<?> clazz) {
        return Logger.getLogger(clazz);
    }

    public static Logger create(Class<?> clazz) {
        LoggerFactory factory = new LoggerFactory();
        return factory.getLog(clazz);
    }
}
