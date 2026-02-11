package com.plat.platboot.auth.config;

import com.plat.platboot.auth.oauth2.PrincipalDetailsService;
import com.plat.platboot.auth.oauth2.PrincipalOAuth2UserService;
import com.plat.platboot.auth.oauth2.formLogin.JsonAuthenticationFilter;
import com.plat.platboot.auth.oauth2.logout.JsonLogoutSuccessHandler;
import com.plat.platboot.auth.oauth2.logout.JwtTokenClearingLogoutHandler;
import com.plat.platboot.auth.oauth2.oauth.OAuth2AuthenticationSuccessHandler;
import com.plat.platdata.auth.jwt.CustomJwtAuthenticationConverter;
import com.plat.platdata.auth.jwt.JwtTokenProvider;
import com.plat.platdata.auth.jwt.SignupTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final BCryptPasswordEncoder encoder;
    private final JwtTokenProvider provider;
    private final SignupTokenProvider signupTokenProvider;
    private final ObjectMapper mapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   PrincipalDetailsService principalDetailsService,
                                                   PrincipalOAuth2UserService principalOAuth2UserService) {
        var authenticationManager = getAuthenticationManager(http, principalDetailsService);
        var filter = jsonAuthenticationFilter(authenticationManager);

        //TODO CSRF 보호 관련 설정 필요, CSRF 토큰보다는 SameSite 고려
        http
            .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)

            .csrf(AbstractHttpConfigurer::disable)

            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/favicon.ico", "/static/**", "/*.js", "/*.css").permitAll()
                .requestMatchers("/api/v1/auth/signup/**", "/api/v1/auth/login", "/api/v1/auth/check-nickname").permitAll()
                .requestMatchers("/api/v1/auth/refresh").authenticated()
                .requestMatchers("/api/v1/posts/**").permitAll()
                .anyRequest().permitAll()
            )

            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .oauth2Login(login -> login
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(principalOAuth2UserService)
                )
                // OAuth2 유저 -> JWT 토큰 발급
                .successHandler(new OAuth2AuthenticationSuccessHandler(provider, signupTokenProvider, mapper))
                .failureHandler((request, response, exception) ->
                    response.sendRedirect("/login?error")
                )
            )

            .oauth2ResourceServer(server -> server
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())
                )
            )

            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .addLogoutHandler(new JwtTokenClearingLogoutHandler(provider))
                .logoutSuccessHandler(new JsonLogoutSuccessHandler(mapper))
            )

            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )

            .authenticationManager(authenticationManager)
        ;
        return http.build();
    }

    AuthenticationManager getAuthenticationManager(HttpSecurity http, PrincipalDetailsService principalDetailsService) {
        var authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder
            .userDetailsService(principalDetailsService)
            .passwordEncoder(encoder);
        return authManagerBuilder.build();
    }

    JsonAuthenticationFilter jsonAuthenticationFilter(AuthenticationManager authenticationManager) {
        var filter = new JsonAuthenticationFilter(mapper, provider);
        filter.setAuthenticationManager(authenticationManager);
        filter.setFilterProcessesUrl("/api/v1/auth/login");
        return filter;
    }

}
