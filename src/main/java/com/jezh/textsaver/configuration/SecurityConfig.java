package com.jezh.textsaver.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DataSource dataSource;

    @Value("${spring.queries.user-query}")
    private String userQuery;
    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Autowired
    public SecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder, DataSource dataSource) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery(userQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
                authorizeRequests()
                .antMatchers("/login", "/sign-up", "/sign-in", "/access-denied").permitAll()
//                .antMatchers("/product-images-uploads/**").hasAuthority("ADMIN")
//                .antMatchers("/home").hasAuthority("USER")
                .antMatchers("/**").hasAnyAuthority("ADMIN", "USER")
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                .formLogin()
                .loginPage("/login")
//                .loginProcessingUrl("/sign-in")
                .failureUrl("/login?error=true")
                // controller method GET mapped "/", and THE ADDRESS WILL BE http://localhost:8082/textsaver
                .defaultSuccessUrl("/")// "/sign-in"
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .logout()
                // to use POST "/logout":
//                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied")
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
//  let Spring knows that some static resources can be served skipping the antMatchers defined
                .ignoring()
                .antMatchers( "/bootstrap3.3.7/**", "/css/**", "/js/**", "/img/**", "/css/login.css");
    }
}
