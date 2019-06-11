package com.jezh.textsaver.logger;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class LoggerTest {

    @Test
    public void givenLoggerWithDefaultConfig_whenLogToConsole_thanOK()
            throws Exception {
        Logger logger = LogManager.getLogger(getClass());
        Exception e = new RuntimeException("This is only a test!");

        logger.trace("This is a simple message at TRACE level. " +
                "It will be hidden.");
        logger.info("This is a simple message at INFO level. " +
                "It will be hidden.");
        logger.debug("This is a simple message at DEBUG level. " +
                "It will be hidden.");
        logger.warn("This is a simple message at WARN level. " +
                "It will be hidden.");
        logger.error("This is a simple message at ERROR level. " +
                "This is the minimum visible level.", e);
        logger.fatal("This is a simple message at FATAL level. " +
                "It will be hidden.");
    }
}
