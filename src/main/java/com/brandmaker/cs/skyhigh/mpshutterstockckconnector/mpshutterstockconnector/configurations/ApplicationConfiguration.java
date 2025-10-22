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

        final HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(
                        this.properties.getRequestTimeout()
                ));

        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(
                        this.properties.getMaxInMemorySize()
                ))
                .build();

        return WebClient.builder()
                .baseUrl(this.properties.getUrl())
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
