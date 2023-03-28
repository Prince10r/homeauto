package com.murphy.homeauto.config;

import com.murphy.homeauto.filter.CustomFilter;
import com.murphy.homeauto.security.HomeAutoBasicAuthenticationEntryPoint;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.sql.DataSource;

@Slf4j
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private DataSource dataSource;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final HomeAutoBasicAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfiguration(HomeAutoBasicAuthenticationEntryPoint authenticationEntryPoint,
            DataSource dataSource) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.dataSource = dataSource;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource);
    }

    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    // http.authorizeRequests()
    // .antMatchers("/securityNone").permitAll()
    // .anyRequest().authenticated()
    // .and()
    // .httpBasic()
    // .authenticationEntryPoint(authenticationEntryPoint)
    // .and().logout().deleteCookies("JSESSIONID").invalidateHttpSession(false)
    // .and()
    // .csrf().disable();
    // ///.ignoringAntMatchers("/login", "/logout");

    // http.addFilterAfter(new CustomFilter(),
    // BasicAuthenticationFilter.class);
    // }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authz) -> {
                    try {
                        authz
                                .requestMatchers("/securityNone").permitAll()
                                .anyRequest().authenticated()
                                .and().httpBasic().authenticationDetailsSource(null)
                                .and()
                                .logout().deleteCookies("JSESSIONID")
                                .invalidateHttpSession(false);
                    } catch (Exception e) {
                        log.error("Failed to intialize Security Filter Chain", e);
                    }
                });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }
}
