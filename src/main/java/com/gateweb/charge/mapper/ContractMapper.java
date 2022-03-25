package com.gateweb.charge.mapper;

import com.gateweb.orm.charge.entity.Contract;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ContractMapper {

    @Mappings({
            @Mapping(target = "contractId", ignore = true),
            @Mapping(target = "companyId", ignore = true),
            @Mapping(target = "creatorId", ignore = true),
            @Mapping(target = "createDate", ignore = true),
            @Mapping(target = "status", ignore = true)
    })
    void updateContractFromVo(Contract from, @MappingTarget Contract to);

}
