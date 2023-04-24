package com.gateweb.charge.infrastructure.propertyProvider;

import com.gateweb.orm.charge.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
@PropertySources({
        @PropertySource(value = "classpath:application-${spring.profiles.active}.properties")
})
public abstract class ContextComponent {
    @Autowired
    protected CompanyRepository companyRepository;
    @Autowired
    protected ContextProvider contextProvider;

    protected <T> T getComponent() {
        Object result = contextProvider.getBean(this.getClass());
        return (T) result;
    }
}
