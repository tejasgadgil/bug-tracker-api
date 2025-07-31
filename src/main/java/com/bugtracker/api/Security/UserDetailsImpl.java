package com.bugtracker.api.Security;

import com.bugtracker.api.Model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority( "ROLE_" + user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Extend logic here if you want to expire accounts
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Extend logic if you want to lock accounts
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Extend logic if needed
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

}
