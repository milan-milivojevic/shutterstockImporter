package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

	private boolean runInitialImport = false;

	public boolean isRunInitialImport() {
		return runInitialImport;
	}

	public void setRunInitialImport(boolean runInitialImport) {
		this.runInitialImport = runInitialImport;
	}
}
