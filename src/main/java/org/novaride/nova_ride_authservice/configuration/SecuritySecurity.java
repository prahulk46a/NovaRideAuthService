package org.novaride.nova_ride_authservice.configuration;

import org.novaride.nova_ride_authservice.filters.JwtAuthFilter;
import org.novaride.nova_ride_authservice.services.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class SecuritySecurity implements WebMvcConfigurer {
    @Autowired
    JwtAuthFilter jwtAuthFilter;
    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailServiceImpl();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf->csrf.disable())
                .cors(cors->cors.disable())
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/api/v1/auth/signup/*").permitAll()
                        .requestMatchers("/api/v1/auth/signin/*").permitAll()
                        .requestMatchers("/api/v1/auth/validate").permitAll()
                ).authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    //this method allows to various type of authentication scheme plugable in application-
    //here we provide logic like of autheticate user based on username alone, username+password.
    //processes authentication request
    //Fully authenticated obj with full credential is returned
    //Matching of email and pass is done by this auth provider
    //there are various authentication strategies(LdapAuthenticationProvider,DaoAuthenticationProvider,CustomAuthenticationProvider) but most popular is DaoAuthenticationProvider
    //If we go with custom authentication service then we manually need to check all those password match and all that things to simplify this spring security provides this authProvider.
    //This authProvider does every thing just expect UserDetailsService obj
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    //It is a core interface that spring security uses for the authentication process.
    /*It has only one method authenticate which when implemented in a class that implements
    an Authentication Manager has all the logic for authenticating a user request.*/
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }

}
