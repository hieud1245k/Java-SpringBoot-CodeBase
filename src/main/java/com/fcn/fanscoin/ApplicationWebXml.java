package com.fcn.fanscoin;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import tech.jhipster.config.DefaultProfileUtil;

public class ApplicationWebXml
        extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        // set a default to use when no profile is configured.
        DefaultProfileUtil.addDefaultProfile(application.application());
        return application.sources(FansCoinApp.class);
    }
}
