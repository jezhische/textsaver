package com.jezh.textsaver.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.jezh.textsaver.security.SecurityConstants.*;

/**
 * NB: each filter in the chain is used both when request is coming to servlet and when the response is coming back
 * to servlet container (i.e. tomcat) to be passed to the client.
 * See <a href = http://otndnld.oracle.co.jp/document/products/as10g/101300/B25221_03/web.1013/b14426/filters.htm>oracle docs</a>.
 * So here I check if the request contains the proper
 * token in its "Authorisation" header, and then add the token to response in the "Authorisation" header.
 * If the token is proper, after request checking
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            // doFilter() fulfills filter work, any other changes of request and response are impossible
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

// setAuthentication(authentication): Set or change the currently authenticated principal, or remove the authentication
// information.
// Parameters: authentication - the new Authentication token, or null.
// The Authentication usually is stored in a thread-local SecurityContext managed by the SecurityContextHolder.
// So here I set the Authentication and store it in the SecurityContext.
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    // An Authentication implementation that is designed for simple presentation of a username and password.
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>()); // principal,
                // credentials, authorities: "The principal and credentials should be set with an Object that provides
                // the respective property via its Object.toString() method. The simplest such Object to use is String".
            }
            return null;
        }
        return null;
    }
}