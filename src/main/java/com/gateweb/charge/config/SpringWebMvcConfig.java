package com.gateweb.charge.config;

import com.gateweb.charge.eventBus.ChargeSystemEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.nio.charset.StandardCharsets;

/**
 * thymeleaf官網的設定檔有用到deprecated的類別，還是以spring的為準
 * https://www.baeldung.com/thymeleaf-in-spring-mvc
 * 因為目前還在用LocalDateTimeUtils傳值，現階段導入有困難，先以棄用spring_web.xml為目標
 *
 * @EnabledWebMvc https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/config/annotation/EnableWebMvc.html
 */
@Configuration
@EnableWebMvc
@PropertySources({
        @PropertySource(value = "classpath:application-${spring.profiles.active}.properties"),
        @PropertySource(name = "charge.localhost.jdbc.properties", value = "classpath:charge.localhost.jdbc.properties"),
        @PropertySource(name = "charge.production.jdbc.properties", value = "classpath:charge.production.jdbc.properties"),
        @PropertySource(name = "charge.localhost.file.properties", value = "classpath:charge.localhost.file.properties"),
        @PropertySource(name = "charge.uat.file.properties", value = "classpath:charge.uat.file.properties"),
        @PropertySource(name = "charge.uat.jdbc.properties", value = "classpath:charge.uat.jdbc.properties"),
        @PropertySource(name = "charge.test.jdbc.properties", value = "classpath:charge.test.jdbc.properties"),
        @PropertySource(name = "mail.uat.properties", value = "classpath:mail.uat.properties"),
        @PropertySource(name = "mail.production.properties", value = "classpath:mail.production.properties"),
        @PropertySource(name = "freeMarker.properties", value = "classpath:freeMarker.properties")
})
@ComponentScan(
        basePackages = {
                "com.gateweb"
                , "com.gate.utils"
                , "com.gate.web"
                , "com.gateweb.bridge.service"
                , "com.gateweb.einv.service"
                , "com.gateweb.charge.chargePolicy.cycle.service"
                , "com.gateweb.charge"
        }
)
public class SpringWebMvcConfig implements WebMvcConfigurer, ApplicationContextAware {

    ApplicationContext applicationContext;

    @Autowired
    ChargeSystemEventListener chargeSystemEventListener;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return messageSource;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/backendAdmin/images/**")
                .addResourceLocations("/backendAdmin/images/");

        registry.addResourceHandler("/backendAdmin/css/**")
                .addResourceLocations("/backendAdmin/css/");

        registry.addResourceHandler("/backendAdmin/js/**")
                .addResourceLocations("/backendAdmin/js/");

        registry.addResourceHandler("/backendAdmin/jqueryPlugin/**")
                .addResourceLocations("/backendAdmin/jqueryPlugin/");

        registry.addResourceHandler("/backendAdmin/template/**")
                .addResourceLocations("/backendAdmin/template/");

        registry.addResourceHandler("/backendAdmin/dist/plugins/**")
                .addResourceLocations("/backendAdmin/dist/plugins/");

        registry.addResourceHandler("/backendAdmin/dist/**")
                .addResourceLocations("/backendAdmin/dist/");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("/images/");

        registry.addResourceHandler("/css/**")
                .addResourceLocations("/css/");
    }

    /**
     * thymeleaf configuration
     */
    @Bean
    public ThymeleafViewResolver htmlViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setContentType("text/html");
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        // SpringTemplateEngine automatically applies SpringStandardDialect and
        // enables Spring's own MessageSource message resolution mechanisms.
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        // Enabling the SpringEL compiler with Spring 4.2.4 or newer can
        // speed up execution in most scenarios, but might be incompatible
        // with specific cases when expressions in one template are reused
        // across different data types, so this flag is "false" by default
        // for safer backwards compatibility.
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        // SpringResourceTemplateResolver automatically integrates with Spring's own
        // resource resolution infrastructure, which is highly recommended.
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix("/thymeleaf");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        // HTML is the default value, added here for the sake of clarity.
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // Template cache is true by default. Set to false if you want
        // templates to be automatically updated when modified.
        templateResolver.setCacheable(false);
        return templateResolver;
    }

}
