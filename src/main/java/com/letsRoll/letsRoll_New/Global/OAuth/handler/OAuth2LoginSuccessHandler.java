package com.letsRoll.letsRoll_New.Global.OAuth.handler;

import com.letsRoll.letsRoll_New.Global.JWT.JwtUtils;
import com.letsRoll.letsRoll_New.Global.OAuth.user.CustomOAuth2User;
import com.letsRoll.letsRoll_New.Global.enums.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication) {
        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            /*// User의 Role이 GUEST일 경우 처음 가입한 회원이므로, 회원가입 페이지로 리다이렉트
            if (oAuth2User.getRole().equals(Role.GUEST)) {
                log.info("Role = Guest, 회원가입 페이지로 리다이렉트");
                jwtUtils.sendTokens(response, oAuth2User.getSocialId(), oAuth2User.getEmail());
                response.sendRedirect("http://localhost:8080/api/users/login"); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트
                System.out.println("리다이렉트 된겨?");
            } else {
                log.info("Role = User, loginSuccess 메소드 실행");
                loginSuccess(response, oAuth2User);
            }*/
            if (oAuth2User.getRole().equals(Role.USER)) {
                log.info("Role = User, loginSuccess 메소드 실행");
                loginSuccess(response, oAuth2User);
            }
        } catch (Exception e) {
            log.info(String.valueOf(e));

        }
    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws Exception {
        jwtUtils.sendTokens(response, oAuth2User.getSocialId(), oAuth2User.getEmail());
//        jwtUtils.accessExpiration(oAuth2User.getSocialId(), oAuth2User.getEmail());
    }

}
