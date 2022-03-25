package com.gateweb.charge.frontEndIntegration.datatablePagination;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.Optional;

@Component
public class PageableUtils {

    public Optional<Sort> sortBuilder(PageInfo pageInfo, Class targetClass) {
        Sort sort = null;
        for (Order order : pageInfo.getOrder()) {
            PropertyDescriptor[] propertyDescriptorList = BeanUtils.getPropertyDescriptors(targetClass);
            for (PropertyDescriptor propertyDescriptor : propertyDescriptorList) {
                if (pageInfo.getColumns().get(order.getColumn()).sortBy == null) {
                    String columnName = pageInfo.getColumns().get(order.getColumn()).getName();
                    if (propertyDescriptor.getName().equals(columnName)) {
                        sort = Sort.by(
                                Sort.Direction.valueOf(order.getDir().toUpperCase())
                                , columnName
                        );
                    }
                } else {
                    int targetColumn = pageInfo.getColumns().get(order.getColumn()).sortBy;
                    String columnName = pageInfo.getColumns().get(targetColumn).getName();
                    if (propertyDescriptor.getName().equals(columnName)) {
                        sort = Sort.by(
                                Sort.Direction.valueOf(order.getDir().toUpperCase())
                                , columnName
                        );
                    }
                }
            }
        }
        return Optional.ofNullable(sort);
    }

    public Pageable pageableBuilder(PageInfo pageInfo, Class targetClass) {
        Optional<Sort> sortOptional = sortBuilder(pageInfo, targetClass);
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getLength());
        if (sortOptional.isPresent()) {
            pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getLength(), sortOptional.get());
        }
        return pageable;
    }


}
