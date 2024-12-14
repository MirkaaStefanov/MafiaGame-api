package com.example.MafiaGame_api.config;



import com.example.MafiaGame_api.handler.JwtAuthenticationEntryPoint;
import com.example.MafiaGame_api.services.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;




@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final ObjectMapper objectMapper;
    public static final String CLIENT_URL = "/api/v1/client/**";
    public static final String PACKAGE_URL = "/api/v1/package/**";
    public static final String PLATE_URL = "/api/v1/plate/**";
    public static final String CARTON_URL = "/api/v1/carton/**";
    public static final String PRODUCT_URL = "/api/v1/product/**";
    public static final String ORDER_URL = "/api/v1/order/**";
    public static final String ORDER_PRODUCT_URL = "/api/v1/orderProduct/**";
    public static final String MONTHLY_ORDER_URL = "/api/v1/monthlyOrder/**";
    public static final String MONTHLY_ORDER_PRODUCT_URL = "/api/v1/monthlyOrderProduct/**";
    public static final String INVOICE_URL = "/api/v1/monthlyOrderProduct/**";
    public static final String CLIENT_USER_URL = "/api/v1/client-user/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint(objectMapper))
                .and()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/api/v1/images/uploadImageForPackage",
                        "/api/v1/images/**",
                        "/api/v1/auth/**",
                        "/api/v1/user/forgot-password",
                        "/api/v1/user/reset-password")
                .permitAll()
                .requestMatchers(HttpMethod.GET,"/api/v1/user/me").authenticated()
                .requestMatchers(HttpMethod.GET,"/api/v1/user/{id}").authenticated()
                .requestMatchers(HttpMethod.PUT,"/api/v1/user/authenticated/{id}").authenticated()
                .requestMatchers(HttpMethod.GET,"/api/v1/user/ifPassMatch").authenticated()
                .requestMatchers(HttpMethod.PUT,"/api/v1/user/change-pass").authenticated()

                .anyRequest()
                .permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

        return http.build();
    }
}
