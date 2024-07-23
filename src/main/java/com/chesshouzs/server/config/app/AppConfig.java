package com.chesshouzs.server.config.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.application")
public class AppConfig {

    private String group; 
    private String artifactId;

    public static final String LOG_SERVICE_PKG_PATH = "execution(* com.chesshouzs.server.service..*.*(..))";

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getArtifactId() {
        return this.artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

}
