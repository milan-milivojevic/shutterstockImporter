package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties;

/**
 * Represents the Media Pool environment that should be used when building search payloads.
 */
public enum MediaPoolEnvironment {
	TEST,
	LIVE;

	public static MediaPoolEnvironment fromUrl(String url) {
		if (url == null || url.isBlank()) {
			throw new IllegalArgumentException("Media Pool server URL must be configured.");
		}

		final String normalized = url.toLowerCase();
		if (normalized.contains("mediaservices-test.zf.com")) {
			return TEST;
		}

		if (normalized.contains("mediaservices.zf.com")) {
			return LIVE;
		}

		throw new IllegalArgumentException("Unsupported Media Pool server URL: " + url);
	}
}