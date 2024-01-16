package com.letsRoll.letsRoll_New.Global.Config;

import com.letsRoll.letsRoll_New.Global.JWT.filter.CustomAuthEntryPoint;
import com.letsRoll.letsRoll_New.Global.JWT.filter.JwtAuthFilter;
import com.letsRoll.letsRoll_New.Global.JWT.filter.JwtExceptionFilter;
import com.letsRoll.letsRoll_New.Global.OAuth.handler.OAuth2LoginFailureHandler;
import com.letsRoll.letsRoll_New.Global.OAuth.handler.OAuth2LoginSuccessHandler;
import com.letsRoll.letsRoll_New.Global.OAuth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
@EnableWebSecurity // security 활성화 어노테이션 - 기본 스프링 필터 체인에 등록
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean //Spring Security 설정 메소드를 빈으로 등록해 컨테이너가 관리하도록 한다
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("filterChain 진입");
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers(new AntPathRequestMatcher(HttpMethod.GET.toString(), "/api/users/**")).permitAll()
//                        .requestMatchers(new AntPathRequestMatcher("/api/++")).hasRole(Role.USER.getRole())
                        .anyRequest().authenticated())
                .httpBasic(AbstractHttpConfigurer::disable) // jwt 토큰(Bearer 방식) 사용하기 위해 httpBasic disable
                .formLogin(AbstractHttpConfigurer::disable) //spring security가 제공하는 FormLogin 형식 disable
                .sessionManagement(auth -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션을 사용하지 않기 때문에 stateless로 설정
                .oauth2Login(auth -> auth
//                        .loginPage("/").permitAll() // 인증이 필요한 URL에 접근하면 /loginForm으로 이동
                                .defaultSuccessUrl("/api/users/login")
                                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))// customUserService 설정
                                .successHandler(oAuth2LoginSuccessHandler) //동의하고 계속하기 눌렀을 떄, Handler 설정
                                .failureHandler(oAuth2LoginFailureHandler) //소셜 로그인 실패시 handler 설정
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // header로부터 전달받는 토큰 검사, valid하면 authentication에 user 등록
                .addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class) //위의 filter에서 exception 발생시 처리하는 filter
                .exceptionHandling(auth -> auth
                        .authenticationEntryPoint(new CustomAuthEntryPoint()));
        return http.build();
    }
}
