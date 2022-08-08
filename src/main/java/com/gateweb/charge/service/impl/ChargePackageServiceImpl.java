package com.gateweb.charge.service.impl;

import com.gateweb.charge.exception.MissingRequiredPropertiesException;
import com.gateweb.charge.mapper.ChargePackageMapper;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.service.ChargePackageService;
import com.gateweb.charge.service.PackageRefService;
import com.gateweb.orm.charge.entity.ChargePackage;
import com.gateweb.orm.charge.entity.Contract;
import com.gateweb.orm.charge.entity.PackageRef;
import com.gateweb.orm.charge.repository.*;
import com.gateweb.utils.bean.BeanConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChargePackageServiceImpl implements ChargePackageService {
    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();

    @Autowired
    ChargePackageViewRepository chargePackageViewRepository;
    @Autowired
    SimpleUserViewRepository simpleUserViewRepository;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ChargePackageRepository chargePackageRepository;
    @Autowired
    PackageRefRepository packageRefRepository;
    @Autowired
    ChargePackageMapper chargePackageMapper;
    @Autowired
    PackageRefService packageRefService;

    @Override
    public void transactionSaveChargePackageByMap(HashMap<String, Object> map, CallerInfo callerInfo) {
        List<PackageRef> packageRefList = new ArrayList<>();
        //根據map產生vo
        ChargePackage chargePackageVo = beanConverterUtils.mapToBean(map, ChargePackage.class);
        chargePackageVo.setModifierId(callerInfo.getUserEntity().getUserId().longValue());
        chargePackageVo.setModifyDate(LocalDateTime.now());
        //根據vo找資料
        Optional<ChargePackage> chargePackageOptional = Optional.empty();
        if (chargePackageVo.getPackageId() != null) {
            chargePackageOptional = chargePackageRepository.findById(chargePackageVo.getPackageId());
        }
        if (chargePackageOptional.isPresent()) {
            //查詢原來的清單
            packageRefList = packageRefRepository.findByFromPackageId(chargePackageOptional.get().getPackageId());
            chargePackageMapper.updateChargePackageFromVo(chargePackageVo, chargePackageOptional.get());
            chargePackageRepository.save(chargePackageOptional.get());
        } else {
            chargePackageOptional = Optional.ofNullable(chargePackageRepository.save(chargePackageVo));
        }
        //看看有沒有子清單
        if (map.containsKey("packageRefList")) {
            List<Map<String, Object>> packageRefMapList = (List<Map<String, Object>>) map.get("packageRefList");
            for (Map packageRefMap : packageRefMapList) {
                Optional<PackageRef> targetPackageRefOptional = Optional.empty();
                if (packageRefMap.containsKey("packageRefId") && packageRefMap.get("packageRefId") instanceof Number) {
                    Long mapPackageRefId = Double.valueOf(String.valueOf(packageRefMap.get("packageRefId"))).longValue();
                    targetPackageRefOptional = packageRefList.stream().filter(packageRef -> {
                        return packageRef.getPackageRefId().equals(mapPackageRefId);
                    }).findAny();
                }
                PackageRef packageRef = packageRefService.mergePackageRefEntityFromMap(packageRefMap, targetPackageRefOptional);
                packageRef.setFromPackageId(chargePackageOptional.get().getPackageId());
                packageRefRepository.save(packageRef);
                if (targetPackageRefOptional.isPresent()) {
                    packageRefList.remove(targetPackageRefOptional.get());
                }
            }
            //刪掉沒有對應到的部份
            packageRefList.stream().forEach(packageRef -> {
                packageRefRepository.delete(packageRef);
            });
        }
    }

    @Override
    public void deleteChargePackage(Long packageId, CallerInfo callerInfo) throws MissingRequiredPropertiesException {
        Optional<ChargePackage> chargePackageOptional = chargePackageRepository.findById(packageId);
        if (!chargePackageOptional.isPresent()) {
            throw new MissingRequiredPropertiesException("ChargePackage");
        } else {
            Collection<Contract> contractList = contractRepository.findByPackageId(chargePackageOptional.get().getPackageId());
            if (contractList.isEmpty()) {
                Collection<PackageRef> packageRefCollection = packageRefRepository.findByFromPackageId(packageId);
                packageRefRepository.deleteAll(packageRefCollection);
                chargePackageRepository.delete(chargePackageOptional.get());
            }
        }
    }

}
