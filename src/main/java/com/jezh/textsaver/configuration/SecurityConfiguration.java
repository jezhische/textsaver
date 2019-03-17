package com.jezh.textsaver.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
//                .cors()
//                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(  "/users/*", "/login", "/users/sign-up", "/users/submit", "/swagger-ui.html").permitAll() // for login.jpg
//                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers("/**").hasAnyAuthority("ADMIN", "USER")
//                .antMatchers("/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
//                requires POST method
//                .loginProcessingUrl("/users/submit")
                .usernameParameter("username")// If not specified then default is username.
                .passwordParameter("password")// If not specified then default is password.
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/")
//                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied")
        ;
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web
//  need to let Spring knows that the resources folder can be served skipping the antMatchers defined
                .ignoring()
                .antMatchers("/bootstrap3.3.7/**", "/css/**", "/images/**", "/js/**", "/webjars/**");
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//        return source;
//    }


}
