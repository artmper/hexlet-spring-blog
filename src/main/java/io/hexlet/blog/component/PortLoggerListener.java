package io.hexlet.blog.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PortLoggerListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger(PortLoggerListener.class);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Environment env = event.getApplicationContext().getEnvironment();

        String sslEnabled = env.getProperty("server.ssl.enabled");
        String protocol = "true".equals(sslEnabled) ? "https" : "http";

        String port = env.getProperty("local.server.port");
        if (port == null) {
            port = env.getProperty("server.port", "8080");
        }

        String contextPath = env.getProperty("server.servlet.context-path", "");

        log.info("=================================================");
        log.info("Application is running! Access URL:");
        log.info("{}://localhost:{}{}/", protocol, port, contextPath);
        log.info("=================================================");
    }
}
