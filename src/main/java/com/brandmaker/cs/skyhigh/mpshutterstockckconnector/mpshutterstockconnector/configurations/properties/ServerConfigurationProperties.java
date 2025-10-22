package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

import static com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties.MediaPoolEnvironment.fromUrl;

@Configuration
@ConfigurationProperties(prefix = "application.server")
public class ServerConfigurationProperties {

    private String url;
    private String soapUrl;
    private Integer requestTimeout;
    private Integer maxInMemorySize;
    private Integer port;

    private ServerConfigurationDefaults defaults;

    @Autowired
    void setDefaults(ServerConfigurationDefaults defaults) {
        this.defaults = defaults;
    }

    @PostConstruct
    void applyDefaults() {
        ensureDefaults(this.defaults);
    }

    /**
     * Makes missing values fall back to the bundled {@code application.yaml} defaults.
     * Exposed for tests so they can mimic the container lifecycle without spinning up Spring.
     */
    public void ensureDefaults(ServerConfigurationDefaults defaults) {
        if (defaults == null) {
            return;
        }

        if (!StringUtils.hasText(this.url)) {
            this.url = defaults.getUrl();
        }
        if (!StringUtils.hasText(this.soapUrl)) {
            this.soapUrl = defaults.getSoapUrl();
        }
        Integer defaultRequestTimeout = defaults.getRequestTimeout();
        if (this.requestTimeout == null || this.requestTimeout <= 0) {
            this.requestTimeout = defaultRequestTimeout;
        }
        Integer defaultMaxInMemorySize = defaults.getMaxInMemorySize();
        if (this.maxInMemorySize == null || this.maxInMemorySize <= 0) {
            this.maxInMemorySize = defaultMaxInMemorySize;
        }
        Integer defaultPort = defaults.getPort();
        if (this.port == null || this.port <= 0) {
            this.port = defaultPort;
        }
    }

    public MediaPoolEnvironment getEnvironment() {
        return fromUrl(url);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) { this.url = url; }

    public String getSoapUrl() {
        return soapUrl;
    }

    public void setSoapUrl(String soapUrl) {
        this.soapUrl = soapUrl;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) { this.requestTimeout = requestTimeout; }

    public Integer getMaxInMemorySize() {
        return maxInMemorySize;
    }

    public void setMaxInMemorySize(Integer maxInMemorySize) {
        this.maxInMemorySize = maxInMemorySize;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
