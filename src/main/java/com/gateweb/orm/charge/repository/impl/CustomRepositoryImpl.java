package com.gateweb.orm.charge.repository.impl;

import com.gateweb.orm.charge.repository.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

@Service
/**
 * Custom Mapping區
 */
public class CustomRepositoryImpl implements CustomRepository {
    @Autowired
    @Qualifier("chargeEntityManager")
    EntityManager chargeEntityManager;

    @Override
    public List<Map<String, Object>> getIasrCountByYearMonth(String yearMonth) {
        List<Map<String, Object>> resultMapList = new ArrayList<>();
        String[] columnArray = new String[]{
                "businessNo"
                , "counter"
        };
        Query q = chargeEntityManager.createNativeQuery(
                "SELECT DISTINCT seller AS business_no\n" +
                        "\t\t,sum(amount) AS counter\n" +
                        "\tFROM invoice_amount_summary_report iasr\n" +
                        "\tWHERE invoice_date LIKE (substring(iasr.invoice_date FROM 0 FOR 7) || '%')\n" +
                        "\tand invoice_date like '" + yearMonth + "%'\n" +
                        "\tGROUP BY seller\n" +
                        "\t\t,substring(iasr.invoice_date FROM 0 FOR 7)\n" +
                        "\t\torder by business_no;");
        q.getResultList().stream().forEach(queryResult -> {
            Map<String, Object> resultMap = mapQueryResult((Object[]) queryResult, columnArray);
            resultMapList.add(resultMap);
        });
        return resultMapList;
    }

    @Override
    public List<Map<String, Object>> getPcscCountByYearMonth(String yearMonth) {
        List<Map<String, Object>> resultMapList = new ArrayList<>();
        String[] columnArray = new String[]{
                "businessNo"
                , "counter"
        };
        Query q = chargeEntityManager.createNativeQuery(
                "SELECT business_no\n" +
                        "\t\t,cnt::BIGINT AS counter\n" +
                        "\tFROM bill_cycle bc\n" +
                        "\tJOIN company c ON (c.company_id = bc.company_id)\n" +
                        "\tWHERE c.business_no IN (\n" +
                        "\t\t\tSELECT DISTINCT seller\n" +
                        "\t\t\tFROM invoice_amount_summary_report iasr\n" +
                        "\t\t\t)\n" +
                        "\t\tAND bc.STATUS = '1'\n" +
                        "\t\t--AND cnt IS NOT null\n" +
                        "\t\tand year_month = '" + yearMonth + "'\n" +
                        "\t\torder by business_no;");
        q.getResultList().stream().forEach(queryResult -> {
            Map<String, Object> resultMap = mapQueryResult((Object[]) queryResult, columnArray);
            resultMapList.add(resultMap);
        });
        return resultMapList;
    }

    @Override
    public List<Map<String, Object>> getOverlapPackageByYearMonth(String yearMonth) {
        String[] columnArray = new String[]{
                "companyId"
                , "yearMonth"
                , "previousPackageId"
                , "currentPackageId"
        };
        List<Map<String, Object>> resultMapList = new ArrayList<>();
        Query q = chargeEntityManager.createNativeQuery(
                "SELECT pm2.company_id ,dc.year_month,min(pm2.package_id) as previous\n" +
                        ",max(pm2.package_id) as current\n" +
                        "FROM package_mode pm2\n" +
                        "JOIN (\n" +
                        "\tSELECT company_id\n" +
                        "\t\t,year_month\n" +
                        "\t\t,count(bc.bill_id) AS counter\n" +
                        "\tFROM bill_cycle bc\n" +
                        "\tWHERE year_month > '" + yearMonth + "'\n" +
                        "\t\tAND bc.STATUS = '1'\n" +
                        "\tGROUP BY company_id\n" +
                        "\t\t,year_month\n" +
                        "\tORDER BY year_month\n" +
                        "\t) dc ON (dc.company_id = pm2.company_id)\n" +
                        "JOIN bill_cycle bc ON (\n" +
                        "\t\tbc.company_id = dc.company_id\n" +
                        "\t\tAND bc.year_month = dc.year_month\n" +
                        "\t\tAND pm2.package_id = bc.package_id\n" +
                        "\t\t)\n" +
                        "join charge_mode_cycle_add cmca on(cmca.addition_id=pm2.addition_id)\n" +
                        "WHERE dc.counter > 1\n" +
                        "group by pm2.company_id,dc.year_month;");
        q.getResultList().stream().forEach(queryResult -> {
            Map<String, Object> resultMap = mapQueryResult((Object[]) queryResult, columnArray);
            resultMapList.add(resultMap);
        });
        return resultMapList;
    }

    private Map<String, Object> mapQueryResult(Object[] objArray, String[] columnArray) {
        Map<String, Object> resultMap = new HashMap<>();
        for (int i = 0; i < objArray.length; i++) {
            resultMap.put(columnArray[i], objArray[i]);
        }
        return resultMap;
    }
}
