package com.gateweb.charge.security;

import com.gateweb.orm.charge.entity.ChargeUser;
import com.gateweb.orm.charge.repository.ChargeUserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 根據這篇文章
 * https://www.baeldung.com/spring-security-authentication-provider
 */
@Component
public class WebServletAuthenticationProvider implements AuthenticationProvider {
    protected Logger logger = LogManager.getLogger(getClass());
    @Autowired
    ChargeUserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String account = authentication.getName();
        String password = authentication.getCredentials().toString();
        Optional<ChargeUser> userEntityOptional = userRepository.findByAccount(account);
        boolean valid;
        if (userEntityOptional.get().getPassword().contains("bcrypt")) {
            valid = bcryptAuth(
                    password
                    , userEntityOptional.get().getPassword().replace("{bcrypt}", "")
            );
        } else {
            valid = basicAuth(password, userEntityOptional.get().getPassword());
        }
        if (valid) {
            return genToken(userEntityOptional.get());
        } else {
            throw new UsernameNotFoundException(account);
        }
    }

    private boolean basicAuth(String inputPassword, String dbPassword) {
        return inputPassword.equals(dbPassword);
    }

    private boolean bcryptAuth(String inputPassword, String bcryptPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(inputPassword, bcryptPassword);
    }

    private UsernamePasswordAuthenticationToken genToken(ChargeUser chargeUser) {
        ChargeUserPrinciple chargeUserPrinciple = new ChargeUserPrinciple(chargeUser);
        return new UsernamePasswordAuthenticationToken(
                chargeUserPrinciple
                , null
                , chargeUserPrinciple.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
