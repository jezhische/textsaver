package com.jezh.textsaver.util.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CommonLogger {

    @Bean
    public Logger getLogger() {
        return LogManager.getLogger(this.getClass());
    }
}
