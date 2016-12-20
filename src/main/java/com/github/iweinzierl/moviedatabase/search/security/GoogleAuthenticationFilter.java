package com.github.iweinzierl.moviedatabase.search.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GoogleAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String accessToken = request.getHeader("X-Authorization");

        if (Strings.isNullOrEmpty(accessToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            GoogleUserDetails user = fetchUserDetails(accessToken);
            SecurityContextHolder.getContext().setAuthentication(new GoogleAuthentication(user));

            filterChain.doFilter(request, response);
        } catch (UnauthorizedException e) {
            LOGGER.warn("Unauthorized access", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            LOGGER.error("Authentication failed", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private GoogleUserDetails fetchUserDetails(String accessToken) throws Exception {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        Plus plus = new Plus.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("Private Movie DataBase")
                .build();

        try {
            Person me = plus.people().get("me").execute();

            return GoogleUserDetails.builder()
                    .email(me.getEmails().get(0).getValue())
                    .name(me.getDisplayName())
                    .id(me.getId())
                    .profileImage(me.getImage().getUrl())
                    .build();
        } catch (GoogleJsonResponseException e) {
            if (e.getDetails().getCode() == 401) {
                throw new UnauthorizedException(e);
            } else {
                throw new AuthorizationNotPossibleException("Http Status: " + e.getStatusCode(), e);
            }
        } catch (IOException e) {
            throw new AuthorizationNotPossibleException(e.getMessage(), e);
        }
    }

    private class UnauthorizedException extends Exception {
        UnauthorizedException(Throwable throwable) {
            super(throwable);
        }
    }

    private class AuthorizationNotPossibleException extends Exception {
        AuthorizationNotPossibleException(String message, Throwable throwable) {
            super(message, throwable);
        }
    }
}
