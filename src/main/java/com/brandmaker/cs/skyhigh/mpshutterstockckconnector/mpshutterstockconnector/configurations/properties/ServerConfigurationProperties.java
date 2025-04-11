package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.server")
public class ServerConfigurationProperties {
    
    private String url;
    private String soapUrl;
    private Integer requestTimeout;
    private Integer maxInMemorySize;
    private Integer port;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSoapUrl() {
        return soapUrl;
    }

    public void setSoapUrl(String soapUrl) {
        this.soapUrl = soapUrl;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

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
