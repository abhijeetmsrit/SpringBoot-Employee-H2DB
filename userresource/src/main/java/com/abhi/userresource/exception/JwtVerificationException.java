package com.abhi.userresource.exception;

/**
 * Thrown if a JWT is invalid or expired.
 */
public class JwtVerificationException extends Exception {

    public JwtVerificationException(String message) {
        super(message);
    }

    public JwtVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}