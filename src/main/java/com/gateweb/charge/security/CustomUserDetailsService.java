package com.gateweb.charge.security;

import com.gateweb.orm.charge.entity.ChargeUser;
import com.gateweb.orm.charge.repository.ChargeUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    ChargeUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        Optional<ChargeUser> userEntityOpt = userRepository.findByAccount(account);
        if (userEntityOpt.isPresent()) {
            return new ChargeUserPrinciple(userEntityOpt.get());
        } else {
            throw new UsernameNotFoundException(account);
        }
    }
}