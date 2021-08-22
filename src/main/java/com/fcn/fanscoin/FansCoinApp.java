package com.fcn.fanscoin;

import com.fcn.fanscoin.config.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import tech.jhipster.config.DefaultProfileUtil;
import tech.jhipster.config.JHipsterConstants;

import javax.annotation.PostConstruct;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class,
                                ApplicationProperties.class})
public class FansCoinApp {

    private final Environment env;

    public FansCoinApp(final Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
                && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run "
                              + "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
                && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not "
                              + "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }

    public static void main(final String[] args) {
        SpringApplication app = new SpringApplication(FansCoinApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(final Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store"))
                                  .map(key -> "https")
                                  .orElse("http");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional.ofNullable(env.getProperty("server.servlet.context-path"))
                                     .filter(StringUtils::isNotBlank)
                                     .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t"
                         + "Application '{}' is running! Access URLs:\n\t"
                         + "Local: \t\t{}://localhost:{}{}\n\t"
                         + "External: \t{}://{}:{}{}\n\t"
                         + "Profile(s): \t{}\n----------------------------------------------------------",
                 env.getProperty("spring.application.name"),
                 protocol,
                 serverPort,
                 contextPath,
                 protocol,
                 hostAddress,
                 serverPort,
                 contextPath,
                 env.getActiveProfiles()
        );
    }
}
