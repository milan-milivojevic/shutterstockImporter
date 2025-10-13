package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

	private boolean runInitialImport = true;
	private boolean runInitialUpdate = false;

	private boolean runVectorImagesUpdate = false;

	public boolean isRunInitialImport() {
		return runInitialImport;
	}

	public void setRunInitialImport(boolean runInitialImport) {
		this.runInitialImport = runInitialImport;
	}

	public boolean isRunInitialUpdate() {
		return runInitialUpdate;
	}

	public void setRunInitialUpdate(boolean runInitialUpdate) {
		this.runInitialUpdate = runInitialUpdate;
	}

	public boolean isRunVectorImagesUpdate() {
		return runVectorImagesUpdate;
	}

	public void setRunVectorImagesUpdate(boolean runVectorImagesUpdate) {
		this.runVectorImagesUpdate = runVectorImagesUpdate;
	}
}
