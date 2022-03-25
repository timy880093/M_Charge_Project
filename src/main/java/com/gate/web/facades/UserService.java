package com.gate.web.facades;

import com.gateweb.charge.exception.InvalidUserException;
import com.gateweb.orm.charge.entity.ChargeUser;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import org.springframework.security.core.Authentication;

import java.util.Optional;

/**
 * Created by simon on 2014/7/11.
 */
public interface UserService extends Service {

    Optional<CallerInfo> getCallerInfoByUserId(Long userId);

    Optional<CallerInfo> getCallerInfoByChargeUser(ChargeUser chargeUser);

    CallerInfo getCallerInfoByAuthentication(Authentication authentication) throws InvalidUserException;
}
