package com.gateweb.charge.chargePolicy.grade.service;

import com.gateweb.charge.exception.InvalidGradeLevelException;
import com.gateweb.orm.charge.entity.RootGradeFetchView;
import com.gateweb.orm.charge.entity.ChargeRule;
import com.gateweb.orm.charge.entity.Grade;
import com.gateweb.orm.charge.entity.NewGrade;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.charge.chargePolicy.grade.bean.RootGradeStorageBean;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GradeService {

    void saveRootGradeStorageBean(RootGradeStorageBean rootGradeStorageBean);

    RootGradeStorageBean mergeWithDataBase(RootGradeFetchView newView, CallerInfo callerInfo) throws InvalidGradeLevelException;

    void saveOrUpdateGradeByMap(Map<String, Object> map, CallerInfo callerInfo);

    NewGrade mergeChildrenGradeFromMap(Map<String, Object> map, Optional<NewGrade> newGradeOptional);

    NewGrade getGradeByChargeRuleId(Long chargeModeId);

    List<NewGrade> getNewGradeListByChargeMode(ChargeRule chargeRule);

    List<NewGrade> getNewGradeListByChargeModeId(Long chargeModeId);

    List<Grade> getGradeListByChargeId(Integer chargeId);

    void transactionDeleteNewGradeIfExists(Long id);

    LinkedList<NewGrade> getLinkedNewGrade(Long gradeId);
}
