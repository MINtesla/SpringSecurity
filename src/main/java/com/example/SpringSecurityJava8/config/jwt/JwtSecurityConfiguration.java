package com.example.SpringSecurityJava8.config.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

//@Slf4j
//@Configuration
//@EnableMethodSecurity(jsr250Enabled = true)
public class JwtSecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                .requestMatchers(new AntPathRequestMatcher("/user/**")).hasRole("ADMIN") // Requires Basic Auth
                                //.requestMatchers(new AntPathRequestMatcher("/hello")).hasRole("USER")

                                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()// Requires Basic Auth
                        // Allow all other URLs without authentication
                )
                .httpBasic();

        http.sessionManagement(session->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin(); // this is for h2 consol to frame the application

        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        // this requires  jwt decoder 'org.springframework.security.oauth2.jwt.JwtDecoder' that could not be found.
        return http.build();
    }



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

    @Bean
    public KeyPair keyPair(){
        try{
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");  // key pair intitalise using RSA
            keyPairGenerator.initialize(2048); // bigger key size higher the security - 2048 bit key size
            return keyPairGenerator.generateKeyPair();
        }catch(Exception e){
            //log.error("exception occur while creating a RSA key pair:",e);
            throw new RuntimeException();
        }
    }

    @Bean
    public RSAKey rsaKey(KeyPair keyPair){
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey){
//        var jwkSet = new JWKSet(rsaKey);
//        var jwkSelector = new JWKSource(){
//            @Override
//            public List<JWK> get(JWKSelector jwkSelector, SecurityContext securityContext) throws KeySourceException {
//                return jwkSelector.select(jwkSet);
//            }
//        };
//        return jwkSelector;

        // write above function as lambda function
        return (jwkSelector,securityContext)-> jwkSelector.select(new JWKSet(rsaKey));
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey())
                .build();
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }


}
