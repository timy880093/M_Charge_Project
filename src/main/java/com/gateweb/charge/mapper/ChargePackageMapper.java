package com.gateweb.charge.mapper;

import com.gateweb.orm.charge.entity.ChargePackage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ChargePackageMapper {

    @Mappings({
            @Mapping(target = "packageId", ignore = true),
            @Mapping(target = "createDate", ignore = true),
            @Mapping(target = "creatorId", ignore = true),
            @Mapping(target = "modifierId", ignore = true),
            @Mapping(target = "modifyDate", ignore = true)
    })
    void updateChargePackageFromVo(ChargePackage from, @MappingTarget ChargePackage to);

}
