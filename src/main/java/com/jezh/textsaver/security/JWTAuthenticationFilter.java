package com.jezh.textsaver.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jezh.textsaver.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.jezh.textsaver.security.SecurityConstants.*;

/**
 * NB: By extending the filter provided within the security framework (like {@link UsernamePasswordAuthenticationFilter} used here),
 * Spring can automatically identify the best place to put it in the security chain. So I don't need explicitly define
 * where in the filter chain I want that filter.
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    @Autowired
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.setFilterProcessesUrl("/users/submit");
    }

    /**
     * Here I parse the user's credentials and issue them to the {@link AuthenticationManager}, that creates
     * {@link Authentication} object
     * @param req
     * @param res
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            AppUser creds = new ObjectMapper()
                    .readValue(req.getInputStream(), AppUser.class);





            System.out.println("************************************************** JWTAuthenticationFilter creds:  " + creds);

//            creds.setEnabled(true);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public void setFilterProcessesUrl(String filterProcessesUrl) {
//        this.setFilterProcessesUrl("/users/submit");
////        super.setFilterProcessesUrl(filterProcessesUrl);
//    }

    /**
     * The method called when a user successfully logs in. Here this method is used to generate a JWT for this user,
     * that than be added in the response "Authorisation" header.
     * @param req
     * @param res
     * @param chain
     * @param auth
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        System.out.println("*********************************** JWTAuthenticationFilter.successfulAuthentication()");

        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
//        res.reset();
//        res.setStatus(200);
        res.sendRedirect("/textsaver/");
    }
}
