package com.gateweb.charge.config;

import com.gateweb.charge.security.CustomAccessDeniedHandler;
import com.gateweb.charge.security.LoginSuccessHandler;
import com.gateweb.charge.security.WebServletAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan
public class GwServletSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    WebServletAuthenticationProvider webServletAuthenticationProvider;

    @Override
    protected void configure(
            AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(webServletAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**"
                , "/static/**"
                , "/backendAdmin/**/*.jpg"
                , "/backendAdmin/**/*.png"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/j_spring_security_check")
                .permitAll()
                .failureForwardUrl("/login?error=true")
                .successHandler(customAuthenticationSuccessHandler())
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        http.logout()
                .permitAll()
                .logoutUrl("/j_spring_security_logout")
                .logoutSuccessUrl("/login.html?logout=true")
                .invalidateHttpSession(true);

        http.rememberMe()
                .key("uniqueAndSecret").tokenValiditySeconds(86400)
                .rememberMeParameter("remember-me")
                .tokenRepository(new InMemoryTokenRepositoryImpl());

        http.authorizeRequests().antMatchers(
                "/**/j_spring_security_*"
                , "/**/login.jsp"
                , "/**/index.jsp"
        ).permitAll();

        http.authorizeRequests()
                .antMatchers("/backendAdmin/**")
                .authenticated()
                .anyRequest().permitAll();

        //不關的話post request會進不來
        http.csrf().disable();

        http.sessionManagement().maximumSessions(2);
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    /**
     * <bean id="tokenRepository" class="org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl"></bean>
     */
    @Bean(name = "tokenRepository")
    public InMemoryTokenRepositoryImpl tokenRepository() {
        InMemoryTokenRepositoryImpl imtri = new InMemoryTokenRepositoryImpl();
        return imtri;
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new LoginSuccessHandler();
    }

}
