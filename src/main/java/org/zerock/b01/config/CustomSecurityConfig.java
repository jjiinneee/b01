package org.zerock.b01.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.zerock.b01.security.CustomUserDetailsService;
import org.zerock.b01.security.handler.Custom403Handler;

import javax.sql.DataSource;

@Configuration
@Log4j2
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class CustomSecurityConfig {
  
  private final DataSource dataSource;
  
  private final CustomUserDetailsService customUserDetailsService;
  
  @Bean
  public PasswordEncoder PasswordEncoder(){
    return new BCryptPasswordEncoder();
  }
  
  
  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
    log.info("-----------------config-----------");
    http.formLogin().loginPage("/member/login");
    
    http.csrf().disable();
   
    http.rememberMe()
            .key("12345678")
            .tokenRepository(persistentTokenRepository())
            .userDetailsService(customUserDetailsService)
            .tokenValiditySeconds(60*60*24*30);
//    super.configure(http);
  
    http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    
    http.oauth2Login();
  
    return http.build();
  }
  
  
  @Bean
  public AccessDeniedHandler accessDeniedHandler(){
    return new Custom403Handler();
  }
  
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    
    log.info("------------web configure-------------------");
    
    return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    
  }
  
  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
    repo.setDataSource(dataSource);
    return repo;
  }
  
}