package com.gateweb.charge.chargePolicy.grade;

import com.gateweb.orm.charge.entity.RootGradeFetchView;
import com.gateweb.orm.charge.entity.NewGrade;
import com.gateweb.utils.bean.BeanConverterUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RootGradeViewBeanConverter {
    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();

    public RootGradeFetchView convert(Map<String, Object> map) {
        //根據map產生vo
        RootGradeFetchView rootGradeFetchView = beanConverterUtils.mapToBean(map, RootGradeFetchView.class);
        List<NewGrade> childGradeList = new ArrayList<>();
        if (map.containsKey("childrenGradeList")) {
            List<Map<String, Object>> childMapList = (List<Map<String, Object>>) map.get("childrenGradeList");
            childMapList.stream().forEach(childMap -> {
                NewGrade childGrade = beanConverterUtils.mapToBean(childMap, NewGrade.class);
                childGradeList.add(childGrade);
            });
        }
        rootGradeFetchView.setChildren(childGradeList);
        return rootGradeFetchView;
    }

}
