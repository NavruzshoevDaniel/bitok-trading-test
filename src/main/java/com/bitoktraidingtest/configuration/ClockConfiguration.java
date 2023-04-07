package com.bitoktraidingtest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfiguration {

    /**
     * For test friendly
     */
    @Bean
    public Clock clockSecurityBean() {
        return Clock.systemDefaultZone();
    }

}
