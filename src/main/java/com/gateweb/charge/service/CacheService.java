package com.gateweb.charge.service;

import org.springframework.stereotype.Service;

@Service
public interface CacheService {
    void refreshChargeEntity(Object entity);
}
