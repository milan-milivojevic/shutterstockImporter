package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.services;

import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.configurations.properties.AuthenticationProperties;
import com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.constants.Endpoints;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
public class AuthService {

    private final AuthenticationProperties properties;
    private final WebClient webClient;

    public AuthService(final AuthenticationProperties properties, final WebClient webClient) {
        this.properties = properties;
        this.webClient = webClient;
    }

    /**
     * Get access token for the API user
     *
     * @return Access token as {@link String}
     */
    public String getAccessToken() {

        final AuthResponse response = this.webClient.post().uri(Endpoints.REST_SSO_AUTH)
                .headers(httpHeader -> httpHeader.setBasicAuth(this.properties.getUser(), this.properties.getPassword()))
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .block();

        return response.getAccessToken();
    }

    public static class AuthResponse {

        @JsonProperty("access_token")
        private String accessToken;

        public AuthResponse() {

        }

        public AuthResponse(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getAccessToken() {
            return this.accessToken;
        }

        public AuthResponse setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this)
                return true;
            if (!(o instanceof AuthResponse)) {
                return false;
            }
            AuthResponse authResponse = (AuthResponse) o;
            return Objects.equals(accessToken, authResponse.accessToken);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(accessToken);
        }

        @Override
        public String toString() {
            return "{" +
                    " accessToken='" + getAccessToken() + "'" +
                    "}";
        }

    }
}
