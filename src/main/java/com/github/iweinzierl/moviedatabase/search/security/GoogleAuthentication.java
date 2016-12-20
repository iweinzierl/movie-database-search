package com.github.iweinzierl.moviedatabase.search.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class GoogleAuthentication implements Authentication {

    private final GoogleUserDetails googleUserDetails;

    public GoogleAuthentication(GoogleUserDetails googleUserDetails) {
        this.googleUserDetails = googleUserDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return googleUserDetails;
    }

    @Override
    public Object getPrincipal() {
        return googleUserDetails;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
    }

    @Override
    public String getName() {
        return googleUserDetails.getName();
    }
}
