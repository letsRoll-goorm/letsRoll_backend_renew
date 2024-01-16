package com.letsRoll.letsRoll_New.Global.JWT.filter;

import com.letsRoll.letsRoll_New.Global.JWT.JwtUtils;
import com.letsRoll.letsRoll_New.Global.enums.Common;
import com.letsRoll.letsRoll_New.Global.exception.BaseException;
import com.letsRoll.letsRoll_New.Global.exception.BaseResponseCode;
import com.letsRoll.letsRoll_New.User.entity.User;
import com.letsRoll.letsRoll_New.User.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.letsRoll.letsRoll_New.Global.JWT.JwtUtils.SPACE;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Value("${jwt.auth-header}")
    private String header;

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String fullToken = request.getHeader(header); //서버에서 클라이언트에게 보낼때는 body로, 그 반대는 header로 토큰 보내기
        String justToken = jwtUtils.parseJustTokenFromFullToken(fullToken);

        if (!StringUtils.hasText(justToken)) {
            doFilter(request, response, filterChain);
            return;
        }

       /* if (jwtUtils.isExpiredToken(accessToken)) {
            log.error("isExpiredToken: Token 만료");
            filterChain.doFilter(request, response);
        }*/
        if (jwtUtils.isValidToken(justToken)) { // 토큰이 유효할 때
            Optional<User> findRefreshTokenUser = userRepository.findByRefreshToken(fullToken);
            System.out.println(justToken);
            if (findRefreshTokenUser.isPresent()) { // header의 토큰이 refresh token일 때
                log.info("refresh");
                User findUser = findRefreshTokenUser.get();
                Authentication authentication = getAuthentication(findUser);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else { // header의 토큰이 access token일 때
                log.info("access");
                User findUser = userRepository.findBySocialIdAndEmailAndStatus(jwtUtils.getSocialIdFromFullToken(jwtUtils.tokenType + SPACE + justToken),
                                jwtUtils.getEmailFromFullToken(jwtUtils.tokenType + SPACE + justToken), Common.ACTIVE_STATUS.getValue())
                        .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_USER));
                Authentication authentication = getAuthentication(findUser);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    public Authentication getAuthentication(User user) {
        return new UsernamePasswordAuthenticationToken(user, "",
                List.of(new SimpleGrantedAuthority(user.getRole().getRole())));
    }
}
