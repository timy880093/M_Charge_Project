package com.gateweb.charge.security;

import com.gateweb.orm.charge.entity.ChargeUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class ChargeUserPrinciple implements UserDetails {
    ChargeUser user;

    public ChargeUserPrinciple(ChargeUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> result = new ArrayList<GrantedAuthority>();
        if (user != null) {
            GrantedAuthority gau = new SimpleGrantedAuthority(
                    UserRole.valueOfRoleNumber(Long.valueOf(user.getRoleId())).name()
            );
            result.add(gau);
        }
        return result;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getAccount();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public ChargeUser getUserInstance() {
        return user;
    }
}
