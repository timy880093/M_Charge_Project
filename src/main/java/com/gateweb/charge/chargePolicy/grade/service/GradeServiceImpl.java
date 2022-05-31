package com.gateweb.charge.chargePolicy.grade.service;

import com.gateweb.charge.chargePolicy.grade.bean.RootGradeStorageBean;
import com.gateweb.charge.chargePolicy.grade.mapper.NewGradeMapper;
import com.gateweb.charge.exception.InvalidGradeLevelException;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import com.gateweb.orm.charge.entity.ChargeRule;
import com.gateweb.orm.charge.entity.Grade;
import com.gateweb.orm.charge.entity.NewGrade;
import com.gateweb.orm.charge.entity.RootGradeFetchView;
import com.gateweb.orm.charge.repository.*;
import com.gateweb.utils.bean.BeanConverterUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GradeServiceImpl implements GradeService {
    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();
    protected final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    GradeRepository gradeRepository;
    @Autowired
    NewGradeRepository newGradeRepository;
    @Autowired
    SimpleUserViewRepository simpleUserViewRepository;
    @Autowired
    NewGradeMapper newGradeMapper;
    @Autowired
    ChargeRuleRepository chargeRuleRepository;
    @Autowired
    RootGradeFetchViewRepository rootGradeFetchViewRepository;

    @Override
    public void saveRootGradeStorageBean(RootGradeStorageBean rootGradeStorageBean) {
        if (rootGradeStorageBean.getRoot().getGradeId() == null) {
            newGradeRepository.save(rootGradeStorageBean.getRoot());
            rootGradeStorageBean.getRoot().setRootId(rootGradeStorageBean.getRoot().getGradeId());
        }
        List<NewGrade> updateGradeList = new ArrayList<>();
        updateGradeList.add(rootGradeStorageBean.getRoot());
        rootGradeStorageBean.getChildList().stream().forEach(child -> {
            child.setRootId(rootGradeStorageBean.getRoot().getGradeId());
            updateGradeList.add(child);
        });
        newGradeRepository.deleteAll(rootGradeStorageBean.getRemoveList());
        newGradeRepository.saveAll(updateGradeList);
    }

    @Override
    public RootGradeStorageBean mergeWithDataBase(RootGradeFetchView newView, CallerInfo callerInfo) throws InvalidGradeLevelException {
        Optional<RootGradeFetchView> oldViewOpt = Optional.empty();
        if (newView.getGradeId() != null) {
            oldViewOpt = rootGradeFetchViewRepository.findById(newView.getGradeId());
        }
        RootGradeStorageBean rootGradeStorageBean = mergeGradeList(oldViewOpt, newView, callerInfo);
        return rootGradeStorageBean;
    }

    public RootGradeStorageBean mergeGradeList(
            Optional<RootGradeFetchView> oldViewOpt, RootGradeFetchView newView, CallerInfo callerInfo) throws InvalidGradeLevelException {
        RootGradeStorageBean rootGradeStorageBean = new RootGradeStorageBean();
        List<NewGrade> resultList = new ArrayList<>();
        List<NewGrade> oldChildList = new ArrayList<>();
        if (oldViewOpt.isPresent()) {
            oldChildList = oldViewOpt.get().getChildren();
        }
        List<NewGrade> newChildList = newView.getChildren();
        HashMap<Long, NewGrade> oldGradeHashMap = new HashMap<>();
        oldChildList.stream().forEach(oldObjDetail -> {
            oldGradeHashMap.put(oldObjDetail.getGradeId(), oldObjDetail);
        });
        Set<Long> overlapIdSet = new HashSet<>();
        newChildList.stream().forEach(newChild -> {
            try {
                NewGrade targetGrade = new NewGrade();
                if (oldGradeHashMap.containsKey(newChild.getGradeId())) {
                    //consider delete root node
                    NewGrade oldChild;
                    if (newChild.getLevel() == 0 && newView.getGradeId() != null) {
                        oldChild = oldGradeHashMap.get(newView.getGradeId());
                    } else {
                        oldChild = oldGradeHashMap.get(newChild.getGradeId());
                    }
                    BeanUtils.copyProperties(targetGrade, oldChild);
                    newGradeMapper.updateDetailNewGradeFromVo(newChild, targetGrade);
                    overlapIdSet.add(targetGrade.getGradeId());
                } else {
                    BeanUtils.copyProperties(targetGrade, newChild);
                    targetGrade.setGradeId(null);
                    targetGrade.setCreateDate(LocalDateTime.now());
                    targetGrade.setCreatorId(callerInfo.getUserEntity().getUserId().longValue());
                }
                targetGrade.setName(newView.getName());
                targetGrade.setEnabled(newView.getEnabled());
                targetGrade.setModifyDate(LocalDateTime.now());
                targetGrade.setModifierId(callerInfo.getUserEntity().getUserId().longValue());
                resultList.add(targetGrade);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        });
        if (!resultList.isEmpty()) {
            rootGradeStorageBean.setRoot(resultList.get(0));
            oldChildList.stream().forEach(oldChild -> {
                if (!overlapIdSet.contains(oldChild.getGradeId())) {
                    rootGradeStorageBean.getRemoveList().add(oldChild);
                }
            });
        } else {
            throw new InvalidGradeLevelException();
        }
        if (resultList.size() > 1) {
            rootGradeStorageBean.setChildList(resultList.subList(1, resultList.size()));
        }
        return rootGradeStorageBean;
    }

    @Override
    public void saveOrUpdateGradeByMap(Map<String, Object> map, CallerInfo callerInfo) {
        //根據map產生vo
        NewGrade newGradeVo = beanConverterUtils.mapToBean(map, NewGrade.class);
        newGradeVo.setModifyDate(LocalDateTime.now());
        newGradeVo.setModifierId(callerInfo.getUserEntity().getUserId().longValue());
        //根據vo找資料
        Optional<NewGrade> gradeOptional = Optional.empty();
        if (newGradeVo.getGradeId() != null) {
            gradeOptional = newGradeRepository.findById(newGradeVo.getGradeId());
        }
        //看看有沒有子清單
        if (map.containsKey("childrenGradeList")) {
            //查詢原來的清單
            List<NewGrade> childGradeList = new ArrayList<>(newGradeRepository.findByRootIdIs(newGradeVo.getGradeId()));
            List<Map<String, Object>> childList = (List<Map<String, Object>>) map.get("childrenGradeList");
            for (Map childGradeMap : childList) {
                Optional<NewGrade> targetChildGradeOptional = Optional.empty();
                if (childGradeMap.containsKey("gradeId") && childGradeMap.get("gradeId") instanceof Number) {
                    Long mapGradeId = Double.valueOf(String.valueOf(childGradeMap.get("gradeId"))).longValue();
                    targetChildGradeOptional = childGradeList.stream().filter(child -> {
                        return child.getGradeId().equals(mapGradeId);
                    }).findAny();
                }
                NewGrade childGrade = mergeChildrenGradeFromMap(childGradeMap, targetChildGradeOptional);
                //設定共用參數
                childGrade.setRootId(newGradeVo.getGradeId());
                childGrade.setName(newGradeVo.getName());

                //第一級是他自己
                if (childGrade.getLevel().equals(0)) {
                    newGradeMapper.updateDetailNewGradeFromVo(childGrade, newGradeVo);
                    targetChildGradeOptional = Optional.ofNullable(newGradeVo);
                }
                //更新資料
                if (targetChildGradeOptional.isPresent()) {
                    targetChildGradeOptional = Optional.of(newGradeRepository.save(targetChildGradeOptional.get()));
                    Long childGradeId = targetChildGradeOptional.get().getGradeId();
                    childGradeList = childGradeList.stream().filter(cg -> {
                        if (cg.getGradeId().equals(childGradeId)) {
                            return false;
                        } else {
                            return true;
                        }
                    }).collect(Collectors.toList());
                } else {
                    childGrade.setGradeId(null);
                    newGradeRepository.save(childGrade);
                }
            }
            //刪掉沒有對應到的部份
            childGradeList.stream().forEach(childGrade -> {
                newGradeRepository.delete(childGrade);
            });
        }
        if (gradeOptional.isPresent()) {
            newGradeMapper.updateNewGradeFromVo(newGradeVo, gradeOptional.get());
            newGradeRepository.save(gradeOptional.get());
        } else {
            newGradeVo.setEnabled(true);
            newGradeVo = newGradeRepository.save(newGradeVo);
            newGradeVo.setRootId(newGradeVo.getGradeId());
            //寫入自己的rootId
            newGradeRepository.save(newGradeVo);
        }
    }

    @Override
    public NewGrade mergeChildrenGradeFromMap(Map<String, Object> map, Optional<NewGrade> newGradeOptional) {
        NewGrade result = null;
        NewGrade newGradeVo = beanConverterUtils.mapToBean(map, NewGrade.class);
        if (newGradeOptional.isPresent()) {
            newGradeMapper.updateChildrenGradeFromVo(newGradeVo, newGradeOptional.get());
            result = newGradeOptional.get();
        } else {
            result = newGradeVo;
        }
        return result;
    }

    @Override
    public NewGrade getGradeByChargeRuleId(Long chargeRuleId) {
        NewGrade result = null;
        Optional<ChargeRule> chargeRuleOptional = chargeRuleRepository.findByChargeRuleId(chargeRuleId);
        if (chargeRuleOptional.isPresent()) {
            Optional<NewGrade> grade = newGradeRepository.findByGradeIdIsAndRootIdIs(chargeRuleOptional.get().getGradeId(), chargeRuleOptional.get().getGradeId());
            if (grade.isPresent()) {
                result = grade.get();
            }
        }
        return result;
    }

    @Override
    public List<NewGrade> getNewGradeListByChargeMode(ChargeRule chargeRule) {
        List<NewGrade> resultList = getNewGradeListByChargeModeId(chargeRule.getChargeRuleId());
        return resultList;
    }

    @Override
    /**
     * 取得某級距方案的級距清單
     */
    public List<NewGrade> getNewGradeListByChargeModeId(Long chargeModeId) {
        List<NewGrade> resultList = new ArrayList<>();
        NewGrade rootGrade = getGradeByChargeRuleId(chargeModeId);
        if (rootGrade != null) {
            resultList = newGradeRepository.findByRootIdOrderByLevelAsc(rootGrade.getGradeId());
        }
        return resultList;
    }

    @Override
    public List<Grade> getGradeListByChargeId(Integer chargeId) {
        List<Grade> resultList = gradeRepository.findByChargeId(chargeId);
        return resultList;
    }

    @Override
    public void transactionDeleteNewGradeIfExists(Long id) {
        Optional<NewGrade> newGradeOptional = newGradeRepository.findByGradeId(id);
        if (newGradeOptional.isPresent()) {
            List<NewGrade> children = newGradeRepository.findByRootIdIsAndGradeIdIsNotOrderByLevel(id, id);
            for (NewGrade child : children) {
                newGradeRepository.delete(child);
            }
            newGradeRepository.delete(newGradeOptional.get());
        }
    }

    @Override
    public LinkedList<NewGrade> getLinkedNewGrade(Long gradeId) {
        LinkedList<NewGrade> gradeLinkedList = new LinkedList<>();
        Optional<NewGrade> newGradeOptional = newGradeRepository.findByGradeId(gradeId);
        if (newGradeOptional.isPresent()) {
            gradeLinkedList.add(newGradeOptional.get());
            List<NewGrade> children = newGradeRepository.findByRootIdIsAndGradeIdIsNotOrderByLevel(gradeId, gradeId);
            for (NewGrade child : children) {
                gradeLinkedList.addLast(child);
            }
        }
        return gradeLinkedList;
    }
}
