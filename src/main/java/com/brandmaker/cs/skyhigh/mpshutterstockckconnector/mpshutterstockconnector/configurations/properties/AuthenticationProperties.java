package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.authentication")
public class AuthenticationProperties {

    private String user;
    private String password;

    public String getUser() {
        return this.user;
    }

    public AuthenticationProperties setUser(final String user) {
        this.user = user;
        return this;
    }

    public String getPassword() {
        return this.password;
    }

    public AuthenticationProperties setPassword(final String password) {
        this.password = password;
        return this;
    }
    
}
