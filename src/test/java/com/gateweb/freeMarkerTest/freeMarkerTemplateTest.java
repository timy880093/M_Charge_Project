package com.gateweb.freeMarkerTest;

import com.gateweb.charge.config.SpringWebMvcConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
@Category(freeMarkerTemplateTest.class)
public class freeMarkerTemplateTest {
    @Autowired
    @Qualifier("freemarkerJavaConfiguration")
    public Configuration freemarkerConfiguration;

    @Test
    public void freemarkerTemplateResourceTest() {
        Optional<Template> javaConfigurationTemplate = javaConfiguration();
        Optional<Template> xmlConfigurationTemplate = xmlConfiguration();
//        Assert.isTrue(javaConfigurationTemplate.isPresent(),"javaConfiguration");
//        Assert.isTrue(xmlConfigurationTemplate.isPresent(),"xmlConfiguration");
    }

    public Optional<Template> javaConfiguration() {
        Optional<Template> templateOpt = Optional.empty();
        try {
            templateOpt = Optional.of(
                    freemarkerConfiguration.getTemplate("remainingCountNoticeMail.ftl")
            );
        } catch (Exception e) {
            e.getMessage();
        }
        return templateOpt;
    }

    public Optional<Template> xmlConfiguration() {
        Optional<Template> templateOpt = Optional.empty();
        try {
            templateOpt = Optional.of(freemarkerConfiguration.getTemplate("remainingCountNoticeMail.ftl"));
        } catch (Exception e) {
            e.getMessage();
        }
        return templateOpt;
    }
}
