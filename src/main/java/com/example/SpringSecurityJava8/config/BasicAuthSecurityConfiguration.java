package com.example.SpringSecurityJava8.config;

import lombok.var;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

//@Configuration
public class BasicAuthSecurityConfiguration {
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
//        httpSecurity.authorizeHttpRequests(auth ->{
//            auth.anyRequest().authenticated();
//        });
//        httpSecurity.sessionManagement(session->
//            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        // no session has no concept of login logout
//
//        // basic auth for passing username and passowrd in it
//        httpSecurity.httpBasic();
//
//        // disable all csrf related request processing
//        httpSecurity.csrf().disable();
//        return httpSecurity.build();
//    }
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                            .requestMatchers(new AntPathRequestMatcher("/user/**")).hasRole("ADMIN") // Requires Basic Auth
                    .requestMatchers(new AntPathRequestMatcher("/hello")).hasRole("USER")

                            .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()// Requires Basic Auth
                    // Allow all other URLs without authentication
            )
            .httpBasic();

    http.sessionManagement(session->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.csrf().disable();
    http.headers().frameOptions().sameOrigin(); // this is for h2 consol to frame the application
    return http.build();
}

// In memory use credential storing

//    @Bean
//    public UserDetailsService userDetailsService() {
//        // Define a user with encoded password
//        User.UserBuilder userBuilder = User.builder().passwordEncoder(password -> passwordEncoder().encode(password));
//        var user = userBuilder
//                .username("user")
//                .password("user")
//                .roles("USER")
//                .build();
//        var admin = userBuilder
//                .username("admin")
//                .password("admin")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user,admin);
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // this is to execute a script using h2 database in starting of the application only
    @Bean
    public DataSource dataSource(){
    return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
            .build();
    }


    // storing user credential in database
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        // Define a user with encoded password
        User.UserBuilder userBuilder = User.builder().passwordEncoder(password -> passwordEncoder().encode(password));
        var user = userBuilder
                .username("user")
                .password("user")
                .roles("USER")
                .build();
        var admin = userBuilder
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();

        JdbcUserDetailsManager jdbcUserDetailsManager=new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.createUser(user);
        jdbcUserDetailsManager.createUser(admin);
        return jdbcUserDetailsManager;
    }
}
