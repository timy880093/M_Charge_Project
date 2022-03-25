package com.gateweb.charge.mapper;

import com.gateweb.orm.charge.entity.PackageRef;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PackageRefMapper {

    @Mappings({
            @Mapping(source = "toChargeRuleId", target = "toChargeRuleId"),
            @Mapping(target = "packageRefId", ignore = true),
            @Mapping(target = "fromPackageId", ignore = true)
    })
    void updatePackageRefToVo(PackageRef from, @MappingTarget PackageRef to);

    @Mappings({
            @Mapping(source = "toChargeRuleId", target = "toChargeRuleId"),
            @Mapping(target = "packageRefId", ignore = true),
            @Mapping(target = "fromPackageId", ignore = true)
    })
    void createPackageRefFromVo(PackageRef from, @MappingTarget PackageRef to);
}
