package com.sr.Ziply.config;


import com.sr.Ziply.enums.UserRole;
import com.sr.Ziply.repository.UserRepository;
import com.sr.Ziply.service.OurUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;
    private final OurUserDetailService ourUserDetailService;
    private final JwtAuthFilter jwtAuthFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        return http.
               csrf(req->req.disable())
                .authorizeHttpRequests(req->
                        req.requestMatchers("/api/auth/**").permitAll()

                                .requestMatchers("/api/admin/**").hasAuthority(UserRole.ADMIN.name())
                                .requestMatchers("/api/owner/**").hasAuthority(UserRole.RESTAURENT_OWNER.name())
                                .requestMatchers("/api/user/**").hasAuthority(UserRole.CUSTOMER.name())
                                //.requestMatchers("/api/customer/restaurants/**").hasAuthority(UserRole.CUSTOMER.name())
                                .requestMatchers("/api/common/**").authenticated()
                                .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                .authenticationProvider(authenticationProvider()).
                addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(ourUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(getpasswordEncoder());
        return daoAuthenticationProvider;
    }
    @Bean
    public PasswordEncoder getpasswordEncoder(){
    return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    }




