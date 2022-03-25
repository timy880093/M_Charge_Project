package com.gateweb.charge.component.annotated;

import com.gateweb.utils.bean.BeanConverterUtils;
import com.gateweb.charge.model.nonMapped.CallerInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@Deprecated
@Component
public class ModifierAndCreatorUtils {
    final BeanConverterUtils beanConverterUtils = new BeanConverterUtils();

    public void signEntityWithCallerInfo(Object obj, CallerInfo callerInfo) {
        try {
            PropertyDescriptor[] propertyDescriptorList = BeanUtils.getPropertyDescriptors(obj.getClass());
            for (PropertyDescriptor propertyDescriptor : propertyDescriptorList) {
                if (propertyDescriptor.getName().toLowerCase().equals("creatorid")) {
                    Optional<Object> valueOptional = Optional.ofNullable(propertyDescriptor.getReadMethod().invoke(obj));
                    if (!valueOptional.isPresent()) {
                        Object value = beanConverterUtils.typeProcessor(
                                propertyDescriptor.getReadMethod().getReturnType()
                                , callerInfo.getUserEntity().getUserId().longValue()
                        );
                        propertyDescriptor.getWriteMethod().invoke(obj, value);
                    }
                }
                if (propertyDescriptor.getName().toLowerCase().equals("modifierid")) {
                    Object value = beanConverterUtils.typeProcessor(
                            propertyDescriptor.getReadMethod().getReturnType()
                            , callerInfo.getUserEntity().getUserId().longValue()
                    );
                    propertyDescriptor.getWriteMethod().invoke(obj, value);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
