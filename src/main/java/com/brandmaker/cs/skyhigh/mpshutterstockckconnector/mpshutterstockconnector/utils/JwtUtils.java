package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Date;

public class JwtUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    private static final long TOKEN_EXPIRATION_LIMIT = -30L;

    private JwtUtils() {

    }

    /**
     * Validate that JWT is not expired
     *
     * @param accessToken Access token
     * @throws JWTDecodeException If provided token is not valid
     * @return if token contains 2 parts (Bearer + token) and if token is not expired
     */
    public static boolean isJwtValidInAuthHeader(final String accessToken) {

        try {

            return !JWT.decode(accessToken).getExpiresAt().before(
                    Date.from(Instant.now().minusSeconds(TOKEN_EXPIRATION_LIMIT))
            );
        } catch (final JWTDecodeException e) {

            LOGGER.error(
                    "Cannot decode the JWT token {}. Auth header is: {}",
                    e.getMessage(),
                    accessToken
            );
        }

        return false;
    }
}
