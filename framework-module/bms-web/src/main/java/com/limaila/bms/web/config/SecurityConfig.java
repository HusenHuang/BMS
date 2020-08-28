package com.limaila.bms.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/***
 说明:
 @author MrHuang
 @date 2020/3/23 20:33
 @desc
 ***/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and()
                .csrf().disable()
                .authorizeRequests().antMatchers("/actuator/**").authenticated()
                .anyRequest().permitAll();
    }
}