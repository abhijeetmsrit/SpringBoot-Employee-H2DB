package com.abhi.userresource.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okta.jwt.JwtVerificationException;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private static final Log LOG = LogFactory.getLog(JWTAuthorizationFilter.class);
    private String errorMsg;
    private int errorCode;

    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();
    private OKTAJWTProcessor oktaJWT =new OKTAJWTProcessor();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
                    throws ServletException, IOException {
        String jwtToken = null;
        try{
            final String requestTokenHeader = request.getHeader("Authorization");
            jwtToken = requestTokenHeader.substring(7);
            if (requestTokenHeader != null
                    && requestTokenHeader.startsWith("Bearer ")
            ) {
                LOG.info("Token from request = "+jwtToken);
                if((oktaJWT.isvalid(jwtToken))) {
                    LOG.info("Token valid");
                    chain.doFilter(request, response);
                }else {
                    errorMsg = "JWT Token has expired!";
                    errorCode = HttpStatus.FORBIDDEN.value();
                    setErrorResponse(response,errorMsg,errorCode);
                    return;
                }
            }
        }catch(NullPointerException e ){
            errorMsg = "JWT Token not available in the request!";
            errorCode = HttpStatus.FORBIDDEN.value();
            setErrorResponse(response,errorMsg,errorCode);
            return;
        } catch (MalformedJwtException e){
            errorMsg ="JWT Token is bogus!";
            errorCode = HttpStatus.FORBIDDEN.value();
            setErrorResponse(response,errorMsg,errorCode);
            return;
        } catch (JwtVerificationException e) {
            errorMsg = "JWT Token verification failed!";
            errorCode = HttpStatus.FORBIDDEN.value();
            setErrorResponse(response,errorMsg,errorCode);
            return;
        }

    }
    public void setErrorResponse(HttpServletResponse response,
                                 String errorMsg,int errorCode) throws IOException {
        LOG.warn(errorMsg);
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("Message", errorMsg);
        errorDetails.put("HttpStatus",HttpStatus.FORBIDDEN.value());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), errorDetails);
    }
}