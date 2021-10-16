package com.primus.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
public class SecurityAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    DefaultAuthenticationProvider defaultAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider((AuthenticationProvider)defaultAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
          http.authorizeRequests()
                  .anyRequest().authenticated().and().httpBasic().and().headers().frameOptions().sameOrigin().and().csrf()
                 .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and().formLogin().loginPage("/bologin.html")
                  .loginProcessingUrl("/bologin.html").permitAll();


    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("https://maxcdn.bootstrapcdn.com/**","https://stackpath.bootstrapcdn.com/**");
    }
}
