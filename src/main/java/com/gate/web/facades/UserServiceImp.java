package com.gate.web.facades;

import com.gateweb.charge.exception.InvalidUserException;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.security.ChargeUserPrinciple;
import com.gateweb.orm.charge.entity.ChargeUser;
import com.gateweb.orm.charge.entity.Company;
import com.gateweb.orm.charge.repository.ChargeUserRepository;
import com.gateweb.orm.charge.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by simon on 2014/7/11.
 */
@Service("userService")
public class UserServiceImp implements UserService {

    @Autowired
    ChargeUserRepository userRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public Optional<CallerInfo> getCallerInfoByUserId(Long userId) {
        CallerInfo result = null;
        Optional<ChargeUser> userEntityOptional = userRepository.findById(userId.intValue());
        Optional<Company> companyOptional = companyRepository.findById(userEntityOptional.get().getCompanyId().intValue());
        if (userEntityOptional.isPresent() && companyOptional.isPresent()) {
            CallerInfo callerInfo = new CallerInfo();
            callerInfo.setCompany(companyOptional.get());
            callerInfo.setUserEntity(userEntityOptional.get());
            result = callerInfo;
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<CallerInfo> getCallerInfoByChargeUser(ChargeUser chargeUser) {
        CallerInfo result = null;
        Optional<Company> companyOptional = companyRepository.findById(chargeUser.getCompanyId().intValue());
        if (companyOptional.isPresent()) {
            CallerInfo callerInfo = new CallerInfo();
            callerInfo.setCompany(companyOptional.get());
            callerInfo.setUserEntity(chargeUser);
            result = callerInfo;
        }
        return Optional.ofNullable(result);
    }

    public CallerInfo getCallerInfoByAuthentication(Authentication authentication) throws InvalidUserException {
        ChargeUserPrinciple chargeUserPrinciple = (ChargeUserPrinciple) authentication.getPrincipal();
        Optional<CallerInfo> callerInfoOptional = getCallerInfoByUserId(chargeUserPrinciple.getUserInstance().getUserId().longValue());
        if (callerInfoOptional.isPresent()) {
            return callerInfoOptional.get();
        } else {
            throw new InvalidUserException();
        }
    }
}
