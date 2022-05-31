package com.gateweb.charge.repository;

import com.gateweb.charge.config.EnvironmentPropertiesUtils;
import com.gateweb.charge.config.SpringWebMvcConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class PropertySourceTest {

    @Autowired
    EnvironmentPropertiesUtils environmentPropertiesUtils;

    @Test
    public void printEnvironmentProperties() {
        environmentPropertiesUtils.getProperties("application-uat.properties", "env");
        environmentPropertiesUtils.getProperties("application-uat.properties", "mail.propertyFileName");
        environmentPropertiesUtils.getProperties("application-uat.properties", "file.propertyFileName");
        environmentPropertiesUtils.getProperties("application-uat.properties", "einv.db.propertyFileName");
    }


}
