package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services;

import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties.MediaPoolEnvironment;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties.ServerConfigurationProperties;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.dto.AssetSearchTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class MediaPoolSearchPayloadFactory {

	private final ObjectMapper objectMapper;
	private final ObjectNode updateExistingTemplate;
	private final ObjectNode vectorUpdateTemplate;

	public MediaPoolSearchPayloadFactory(ObjectMapper objectMapper, ServerConfigurationProperties serverConfigurationProperties) {
		this.objectMapper = objectMapper;
		MediaPoolEnvironment environment = serverConfigurationProperties.getEnvironment();
		this.updateExistingTemplate = loadTemplate(environment, "update-existing-assets");
		this.vectorUpdateTemplate = loadTemplate(environment, "update-vector-images");
	}

	public AssetSearchTO buildUpdateExistingAssetsPayload(String stockValue) {
		ObjectNode payload = updateExistingTemplate.deepCopy();
		applyStockValue(payload, stockValue);
		return AssetSearchTO.fromObjectNode(payload);
	}

	public ObjectNode buildVectorUpdatePayload(String stockValue) {
		ObjectNode payload = vectorUpdateTemplate.deepCopy();
		applyStockValue(payload, stockValue);
		return payload;
	}

	private ObjectNode loadTemplate(MediaPoolEnvironment environment, String scenario) {
		String resourcePath = String.format("mediapool/search/%s-%s.json", scenario, environment.name().toLowerCase());
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
			if (inputStream == null) {
				throw new IllegalStateException("Missing Media Pool search template: " + resourcePath);
			}
			JsonNode jsonNode = objectMapper.readTree(inputStream);
			if (!(jsonNode instanceof ObjectNode)) {
				throw new IllegalStateException("Invalid Media Pool search template: " + resourcePath);
			}
			return (ObjectNode) jsonNode;
		} catch (IOException e) {
			throw new IllegalStateException("Failed to load Media Pool search template: " + resourcePath, e);
		}
	}

	private void applyStockValue(ObjectNode payload, String stockValue) {
		if (stockValue == null) {
			return;
		}
		JsonNode criteria = payload.path("criteria");
		JsonNode subsRoot = criteria.path("subs");
		if (!subsRoot.isArray() || subsRoot.size() == 0) {
			return;
		}

		JsonNode orNode = subsRoot.get(0);
		JsonNode orSubs = orNode.path("subs");
		if (orSubs.isArray() && orSubs.size() >= 2) {
			JsonNode matchNode = orSubs.get(0);
			if (matchNode instanceof ObjectNode) {
				((ObjectNode) matchNode).put("value", stockValue);
			}
			JsonNode exactMatchNode = orSubs.get(1);
			if (exactMatchNode instanceof ObjectNode) {
				((ObjectNode) exactMatchNode).put("value", stockValue);
			}
		}
	}
}