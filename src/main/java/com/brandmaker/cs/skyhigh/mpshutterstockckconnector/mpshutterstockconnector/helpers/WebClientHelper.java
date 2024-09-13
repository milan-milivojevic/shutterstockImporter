package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.helpers;

import java.util.List;

import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services.AuthService;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.utils.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class WebClientHelper {

    private static final String BEARER = "Bearer ";

    private final WebClient webClient;
    private final AuthService authService;

    private String accessToken;

    public WebClientHelper(final WebClient webClient, final AuthService authService) {
        this.webClient = webClient;
        this.authService = authService;
    }

    /**
     * Send POST request with provided parameters
     *
     * @param endpoint   Endpoint where the request is going to be sent
     * @param body       Request body
     * @param mediaType  Media type
     * @param clazz      Class that shows the response body
     * @param pathParams Path params
     * @return {@link ResponseEntity} of the class
     */
    public <T> ResponseEntity<T> sendPostRequest(final String endpoint, final Object body,
            final MediaType mediaType, final Class<T> clazz, final Object... pathParams) {

        return this.webClient.post().uri(uriBuilder ->
                        uriBuilder.path(endpoint)
                                .build(pathParams)
                )
                .header(HttpHeaders.AUTHORIZATION, BEARER + this.getAccessToken())
                .contentType(mediaType)
                .bodyValue(body)
                .retrieve()
                .toEntity(clazz)
                .block();
    }

    /**
     * Send PATCH request with provided parameters
     *
     * @param endpoint  Endpoint where the request is going to be sent
     * @param body      Request body
     * @param mediaType Media type
     * @param clazz     Class that shows the response body
     * @return {@link ResponseEntity} of the class
     */
    public <T> ResponseEntity<T> sendPatchRequest(final String endpoint, final Object body, final MediaType mediaType,
                                                  final Class<T> clazz) {

        return this.webClient.patch().uri(endpoint)
                .header(HttpHeaders.AUTHORIZATION, BEARER + this.getAccessToken())
                .contentType(mediaType)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .toEntity(clazz)
                .block();
    }

    /**
     * Send POST request in content type {@link MediaType#APPLICATION_FORM_URLENCODED} and get the response
     *
     * @param endpoint Endpoint where the request is going to be sent
     * @param body     Request body
     * @param clazz    Class that shows the response body
     * @return {@link ResponseEntity} of the class
     */
    public <T> ResponseEntity<T> sendPostRequestUrlEncoded(final String endpoint,
                                                           final MultiValueMap<String, String> body, final Class<T> clazz) {

        return this.webClient.post().uri(endpoint)
                .header(HttpHeaders.AUTHORIZATION, BEARER + this.getAccessToken())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(body))
                .retrieve()
                .toEntity(clazz)
                .block();
    }

    /**
     * Send PUT request and get the response
     *
     * @param endpoint Endpoint where the request is going to be sent
     * @param body     Request body
     * @param clazz    Class that shows the response body
     * @param params   Path params
     * @return {@link ResponseEntity} of the class
     */
    public <T> ResponseEntity<T> sendPutRequest(final String endpoint, final Object body, final Class<T> clazz,
                                                final Object... params) {

        return this.webClient.put().uri(uriBuilder ->
                        uriBuilder.path(endpoint)
                                .build(params)
                )
                .header(HttpHeaders.AUTHORIZATION, BEARER + this.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .toEntity(clazz)
                .block();
    }

    /**
     * Send GET request with path parameters and query parameters
     * 
     * @param endpoint    Endpoint where the request is going to be sent
     * @param queryParams Query parameters
     * @param clazz       Class that shows the response type
     * @param pathParams  Path parameters
     * @return {@link ResponseEntity} of class
     */
    public <T> ResponseEntity<T> sendGetRequest(final String endpoint, final MultiValueMap<String, String> queryParams,
                    final Class<T> clazz, final Object... pathParams) {

        final UriComponents uriComponents = UriComponentsBuilder.fromUriString(endpoint)
                        .queryParams(queryParams)
                        .buildAndExpand(pathParams);

        return this.webClient.get().uri(uriComponents.toString())
                .header(HttpHeaders.AUTHORIZATION, BEARER + this.getAccessToken())
                .retrieve()
                .toEntity(clazz)
                .block();
    }

    /**
     * Get list of resources
     *
     * @param endpoint   Endpoint where the request is going to be sent
     * @param clazz      Expected type of the response body
     * @param pathParams Path parameters
     * @return {@link ResponseEntity} of {@link List} entities
     */
    public <T> ResponseEntity<List<T>> sendGetRequest(final String endpoint, final Class<T> clazz,
                                                      final Object... pathParams) {

        final UriComponents uriComponents = UriComponentsBuilder.fromUriString(endpoint)
                .buildAndExpand(pathParams);

        return this.webClient.get().uri(uriComponents.toString())
                .header(HttpHeaders.AUTHORIZATION, BEARER + this.getAccessToken())
                .retrieve()
                .toEntityList(clazz)
                .block();
    }

    /**
     * Get new access token if the token does not exist or token is expired
     *
     * @return Access token
     */
    private String getAccessToken() {

        if (accessToken == null || !JwtUtils.isJwtValidInAuthHeader(accessToken)) {

            accessToken = this.authService.getAccessToken();
            return accessToken;
        }

        return this.accessToken;
    }

}
