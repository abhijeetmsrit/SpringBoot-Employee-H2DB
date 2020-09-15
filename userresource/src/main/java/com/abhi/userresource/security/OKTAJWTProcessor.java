package com.abhi.userresource.security;

import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerificationException;
import com.okta.jwt.JwtVerifiers;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.time.Duration;

public class OKTAJWTProcessor {

    private Boolean istokenValid = false;
    private static final Log LOG = LogFactory.getLog(OKTAJWTProcessor.class);


    public Boolean isvalid(String jwtToken) throws JwtVerificationException {
        String issuerUrl = "https://dev-356528.okta.com/oauth2/ausre6ia7CYSulFuu4x6";
        String audience = "http://35.243.200.1:8080";
        String jwtString = jwtToken;

        // Build the Bearer token parser
        AccessTokenVerifier jwtVerifier = JwtVerifiers.accessTokenVerifierBuilder()
                .setIssuer(issuerUrl)
                .setAudience(audience)
                .setConnectionTimeout(Duration.ofSeconds(1)) // defaults to 1000ms
                .build();

        //Process the token (includes validation)
        Jwt jwt = jwtVerifier.decode(jwtString);

        //Do something with the token
        if((jwt.getClaims().containsValue(issuerUrl))){
            LOG.info(jwt.getTokenValue()); // print the token
            LOG.info(jwt.getClaims().get("invalidKey")); // an invalid key just returns null
            LOG.info(jwt.getClaims().get("groups")); // handle an array value
            LOG.info(jwt.getExpiresAt()); // print the expiration time
            istokenValid=true;
            return istokenValid;
        }
        return istokenValid;
    }
}
