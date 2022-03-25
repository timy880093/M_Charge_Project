package com.gateweb.charge.mapper;

import com.gateweb.orm.charge.entity.ChargeRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ChargeRuleMapper {

    @Mappings({
            @Mapping(target = "chargeRuleId", ignore = true),
            @Mapping(target = "createDate", ignore = true),
            @Mapping(target = "creatorId", ignore = true),
            @Mapping(target = "modifierId", ignore = true),
            @Mapping(target = "modifyDate", ignore = true),
            @Mapping(source = "calculateCycleType", target = "calculateCycleType"),
            @Mapping(source = "chargeCycleType", target = "chargeCycleType")
    })
    void updateChargeRuleFromVo(ChargeRule from, @MappingTarget ChargeRule to);
}
