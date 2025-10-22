package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * Provides fallback Media Pool server settings by reading the classpath {@code application.yaml}.
 * <p>
 * When the deployment relies on an external {@code application.properties} file and omits specific
 * keys, Spring replaces the whole configuration set. These helpers re-introduce the defaults that
 * are defined in the bundled YAML so the application can still start with sensible values.
 */
@Component
public class ServerConfigurationDefaults {

	private static final String CONFIG_RESOURCE = "application.yaml";

	private final Properties properties;

	public ServerConfigurationDefaults() {
		final YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
		yamlFactory.setResources(new ClassPathResource(CONFIG_RESOURCE));
		this.properties = yamlFactory.getObject();
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