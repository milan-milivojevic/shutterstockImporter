package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations;

import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties.AuthenticationProperties;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties.ServerConfigurationProperties;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.generated.mediapool.MediaPoolService;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.generated.mediapool.MediaPoolWebServicePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.xml.ws.BindingProvider;
import java.time.Duration;
import java.util.Map;

@Configuration
@EnableScheduling
@EnableRetry
@EnableIntegration
public class ApplicationConfiguration {

    private final ServerConfigurationProperties properties;
    private final AuthenticationProperties authenticationProperties;

    public ApplicationConfiguration(final ServerConfigurationProperties properties, AuthenticationProperties authenticationProperties) {
        this.properties = properties;
        this.authenticationProperties = authenticationProperties;
    }

    @Bean
    WebClient webClient() {
        final String baseUrl = this.properties.getUrl();
        if (!StringUtils.hasText(baseUrl)) {
            throw new IllegalStateException("Property 'application.server.url' must be configured.");
        }

        final Integer requestTimeout = this.properties.getRequestTimeout();
        if (requestTimeout == null || requestTimeout <= 0) {
            throw new IllegalStateException("Property 'application.server.request_timeout' must be configured with a positive value.");
        }

        final Integer maxInMemorySize = this.properties.getMaxInMemorySize();
        if (maxInMemorySize == null || maxInMemorySize <= 0) {
            throw new IllegalStateException("Property 'application.server.max_in_memory_size' must be configured with a positive value.");
        }

        final HttpClient httpClient = HttpClient.create()
          .responseTimeout(Duration.ofSeconds(requestTimeout));

        final ExchangeStrategies strategies = ExchangeStrategies.builder()
          .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(maxInMemorySize))
          .build();

        return WebClient.builder()
          .baseUrl(baseUrl)
          .clientConnector(new ReactorClientHttpConnector(httpClient))
          .exchangeStrategies(strategies)
          .build();
    }

    @Bean
    ObjectMapper objectMapper() {

        return new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    MediaPoolService generatedMediaPoolService() {
        return new MediaPoolService();
    }

    @Bean
    MediaPoolWebServicePort mediaPoolWebServicePort(final MediaPoolService generatedMediaPoolService) {
        final MediaPoolWebServicePort mediaPoolWebServicePort = generatedMediaPoolService.getMediaPoolPort();
        final Map<String, Object> requestContext = ((BindingProvider) mediaPoolWebServicePort).getRequestContext();
        requestContext.put(BindingProvider.USERNAME_PROPERTY, this.authenticationProperties.getUser());
        requestContext.put(BindingProvider.PASSWORD_PROPERTY, this.authenticationProperties.getPassword());
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.properties.getSoapUrl());

        return mediaPoolWebServicePort;
    }

}
