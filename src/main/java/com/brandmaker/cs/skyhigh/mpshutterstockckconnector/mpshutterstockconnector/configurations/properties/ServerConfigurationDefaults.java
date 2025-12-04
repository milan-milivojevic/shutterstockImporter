package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Provides fallback Media Pool server settings by reading classpath {@code application.properties}
 * or {@code application.yaml} if properties are unavailable.
 */
@Component
public class ServerConfigurationDefaults {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerConfigurationDefaults.class);

	private static final String YAML_RESOURCE = "application.yaml";
	private static final String PROPERTIES_RESOURCE = "application.properties";

	private final Properties properties;

	public ServerConfigurationDefaults() {
		this.properties = loadDefaults();
	}

	public String getUrl() {
		return getProperty("application.server.url");
	}

	public String getSoapUrl() {
		return getProperty("application.server.soap_url");
	}

	public Integer getRequestTimeout() {
		return sanitizePositive(getInteger("application.server.request_timeout"));
	}

	public Integer getMaxInMemorySize() {
		return sanitizePositive(getInteger("application.server.max_in_memory_size"));
	}

	public Integer getPort() {
		return sanitizePositive(getInteger("application.server.port"));
	}

	private String getProperty(String key) {
		return this.properties != null ? this.properties.getProperty(key) : null;
	}

	private Properties loadDefaults() {
		final Properties defaults = new Properties();

		final Resource propertiesResource = new ClassPathResource(PROPERTIES_RESOURCE);
		if (propertiesResource.exists()) {
			try {
				defaults.putAll(PropertiesLoaderUtils.loadProperties(propertiesResource));
			} catch (IOException exception) {
				LOGGER.warn("Failed to load default values from application.properties: {}", exception.getMessage());
			}
		}

		final Resource yamlResource = new ClassPathResource(YAML_RESOURCE);
		if (yamlResource.exists()) {
			final YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
			yamlFactory.setResources(yamlResource);
			final Properties yamlProperties = yamlFactory.getObject();
			if (yamlProperties != null) {
				yamlProperties.forEach((key, value) -> defaults.putIfAbsent(key, value));
			}
		}

		if (defaults.isEmpty()) {
			LOGGER.warn("No default configuration file (application.properties or application.yaml) was found on the classpath.");
		}

		return defaults;
	}

	private Integer getInteger(String key) {
		final String value = getProperty(key);
		if (!StringUtils.hasText(value)) {
			return null;
		}
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	private Integer sanitizePositive(Integer candidate) {
		if (candidate == null || candidate <= 0) {
			return null;
		}
		return candidate;
	}
}