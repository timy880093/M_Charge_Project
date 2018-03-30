package com.gate.web.facades;

import com.gateweb.charge.model.BillCycleEntity;

import java.util.List;

/**
 * Created by Eason on 3/29/2018.
 */
public interface BillCycleService extends Service{

    List<BillCycleEntity> getAllCompaniesBillCycle(String yearMonth);
}
