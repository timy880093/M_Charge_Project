package com.gateweb.charge.chargePolicy.grade.mapper;

import com.gateweb.orm.charge.entity.NewGrade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface NewGradeMapper {

    @Mappings({
            @Mapping(target = "gradeId", ignore = true),
            @Mapping(target = "creatorId", ignore = true),
            @Mapping(target = "createDate", ignore = true),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "enabled", target = "enabled")
    })
    void updateNewGradeFromVo(NewGrade from, @MappingTarget NewGrade to);

    /**
     * children grade不會更新enabled
     *
     * @param from
     * @param to
     */
    @Mappings({
            @Mapping(target = "gradeId", ignore = true),
            @Mapping(target = "creatorId", ignore = true),
            @Mapping(target = "createDate", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(source = "level", target = "level"),
            @Mapping(source = "cntStart", target = "cntStart"),
            @Mapping(source = "cntEnd", target = "cntEnd")
    })
    void updateChildrenGradeFromVo(NewGrade from, @MappingTarget NewGrade to);

    @Mappings({
            @Mapping(source = "level", target = "level"),
            @Mapping(source = "cntStart", target = "cntStart"),
            @Mapping(source = "cntEnd", target = "cntEnd"),
            @Mapping(source = "name", target = "name"),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "gradeId", ignore = true)
    })
    void updateDetailNewGradeFromVo(NewGrade from, @MappingTarget NewGrade to);
}
