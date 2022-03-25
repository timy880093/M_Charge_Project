package com.gateweb.orm.charge.repository;

import java.util.List;
import java.util.Map;

public interface CustomRepository {
    List<Map<String, Object>> getIasrCountByYearMonth(String yearMonth);

    List<Map<String, Object>> getPcscCountByYearMonth(String yearMonth);

    List<Map<String, Object>> getOverlapPackageByYearMonth(String yearMonth);
}
