package com.gateweb.charge.chargePolicy.grade.service;

import com.gateweb.orm.charge.entity.RootGradeFetchView;
import com.gateweb.orm.charge.repository.NewGradeRepository;
import com.gateweb.orm.charge.repository.RootGradeFetchViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RootGradeSearchServiceImpl implements RootGradeSearchService {
    @Autowired
    NewGradeRepository newGradeRepository;
    @Autowired
    RootGradeFetchViewRepository rootGradeFetchViewRepository;

    @Override
    public List<RootGradeFetchView> findAllRootGradeFetchView() {
        List<RootGradeFetchView> fetchViewList = new ArrayList<>();
        newGradeRepository.findRootGradeList().stream().forEach(newGrade -> {
            Optional<RootGradeFetchView> rootGradeFetchViewOptional
                    = rootGradeFetchViewRepository.findById(newGrade.getGradeId());
            if (rootGradeFetchViewOptional.isPresent()) {
                fetchViewList.add(rootGradeFetchViewOptional.get());
            }
        });
        return fetchViewList;
    }
}
