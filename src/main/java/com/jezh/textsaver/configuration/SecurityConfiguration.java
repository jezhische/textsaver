package com.jezh.textsaver.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
// The configuration creates a Servlet Filter known as the springSecurityFilterChain
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    // data source implementation out of the box by Spring Boot
    @Autowired
    public SecurityConfiguration(BCryptPasswordEncoder bCryptPasswordEncoder, DataSource dataSource) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
// the order of the antMatchers() elements is significant – the more specific rules need to come first, followed by the more general ones
        http
                .csrf().disable()
//  each antMatchers() and anyRequest() is a child of authorizeRequests(), and these are the intercept-url patterns
                .authorizeRequests()
                    .antMatchers("/swagger-ui*", "/v2/api-docs").permitAll()
                    .antMatchers("/", "/login", "/registration").permitAll()
                // NB authority (e.g. hasAnyAuthority) as "ROLE_ADMIN", but role (e.g. hasAnyRole) as "ADMIN"
                    .antMatchers("/doc-data/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                    .antMatchers("/index").permitAll()
//                    .antMatchers("/doc-data/**").access("hasRole('ADMIN') or hasRole('USER')")
                    .anyRequest().authenticated()
// and() means the finish of previous configuration and start the new one (return to HttpSecurity http configuration level)
                .and()
                    .formLogin()
                        .loginPage("/login")

                // for the following line: controller method GET mapped "/admin/home", and THE ADDRESS WILL BE http://localhost:8074/index
//                        .defaultSuccessUrl("/index")

                // and for the following line: controller method POST mapped "/index". But in the same time it must be
                // <form th:action="@{/login}" method="POST"> in the login.html, and THE ADDRESS WILL BE http://localhost:8074/login
                        .loginProcessingUrl("/index")

//.successForwardUrl("/doc-data")
                        .failureUrl("/login?error=true")
                        .usernameParameter("name")
                        .passwordParameter("password")
                .and()
                    .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                .and()
                    .exceptionHandling()
                        .accessDeniedPage("/access-denied")
//                .and()
//                    .httpBasic()
        ;
    }



//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll();
//    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.
//                authorizeRequests()
//                .antMatchers("/").permitAll()
//                .antMatchers("/login").permitAll()
//                .antMatchers("/registration").permitAll()
//                .antMatchers("/doc-data/**").hasAuthority("ROLE_USER").anyRequest()
//                .authenticated().and().csrf().disable().formLogin()
//                .loginPage("/login").failureUrl("/login?error=true")
//                .defaultSuccessUrl("/index")
//                .usernameParameter("name")
//                .passwordParameter("password")
//                .and().logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/").and().exceptionHandling()
//                .accessDeniedPage("/access-denied");
//    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
//  need to let Spring knows that the resources folder can be served skipping the antMatchers defined
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }


}
