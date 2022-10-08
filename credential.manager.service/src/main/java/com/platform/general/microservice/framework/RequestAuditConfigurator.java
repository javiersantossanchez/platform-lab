package com.platform.general.microservice.framework;

import com.platform.general.microservice.framework.filter.CustomRequestLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * Class to log the detail of all http calls
 */
@Configuration
public class RequestAuditConfigurator {

    @Bean
    public CustomRequestLoggingFilter requestLoggingFilter() {
        CustomRequestLoggingFilter loggingFilter = new CustomRequestLoggingFilter();
        return loggingFilter;
    }

}