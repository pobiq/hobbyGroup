package com.zerobase.hobbyGroup.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter authenticationFilter;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic(HttpBasicConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests
                    (
                        authorize -> authorize
                            .requestMatchers("/swagger-ui/**", "/api-docs/swagger-config").permitAll()
                            .requestMatchers("/user/signup", "/user/signin", "/user/auth").permitAll()
                            .requestMatchers("/user/update", "/user/logout", "user/delete").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                            .requestMatchers("/email/**").permitAll()
                            .requestMatchers("/category/list").permitAll()
                            .requestMatchers("/category/create", "/category/update", "/category/delete").hasAnyAuthority("ROLE_ADMIN")
                            .requestMatchers("/groupBoard/latest/list", "/groupBoard/title/list", "/groupBoard/view/list", "/groupBoard/detail").permitAll()
                            .requestMatchers("/groupBoard/create", "/groupBoard/update").hasAnyAuthority("ROLE_USER")
                            .requestMatchers("/groupBoard/delete").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                            .requestMatchers("/applyGroup/create", "/applyGroup/list", "/applyGroup/self/list", "/applyGroup/detail", "/applyGroup/update", "/applyGroup/accept", "/applyGroup/reject").hasAnyAuthority("ROLE_USER")
                            .requestMatchers("/likeGroup/create", "/likeGroup/self/list", "/likeGroup/delete").hasAnyAuthority("ROLE_USER")
                            .requestMatchers("/activityBoard/create/general", "/activityBoard/create/notice", "/activityBoard/latest/list", "/activityBoard/title/list", "/activityBoard/view/list", "/activityBoard/detail", "/activityBoard/update").hasAnyAuthority("ROLE_USER")
                            .requestMatchers("/activityBoard/delete").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                    )
                .addFilterBefore(this.authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }



}
