package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.Contract;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.HashMap;
import java.util.List;

@NoRepositoryBean
public interface ContractRepositoryCustom {

    List<Contract> searchBySearchCondition(HashMap<String, String> parameterMap);
}
