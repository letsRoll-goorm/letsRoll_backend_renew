package com.letsRoll.letsRoll_New.Global.JWT.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsRoll.letsRoll_New.Global.exception.BaseResponseCode;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    /**
     * Spring에서 예외를 처리하는 HandlerInterceptor는 filter의 예외를 처리할 수 없음
     * Filter(JwtAuthFilter)에서 던지는 예외를 처리하기 위한 filter
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException exception) {
            log.error(BaseResponseCode.EXPIRED_TOKEN.getMessage());
            request.setAttribute("exception", BaseResponseCode.EXPIRED_TOKEN.getCode());
            filterChain.doFilter(request, response);
        } catch (UnsupportedJwtException exception) {
            log.error(BaseResponseCode.UNSUPPORTED_TOKEN.getMessage());
            request.setAttribute("exception", BaseResponseCode.UNSUPPORTED_TOKEN.getCode());
            filterChain.doFilter(request, response);
        } catch (RequiredTypeException exception) {
            log.error(BaseResponseCode.WRONG_TYPE_TOKEN.getMessage());
            request.setAttribute("exception", BaseResponseCode.WRONG_TYPE_TOKEN.getCode());
            filterChain.doFilter(request, response);
        } catch (NullPointerException exception) {
            log.error(BaseResponseCode.NOT_FOUND_TOKEN.getMessage());
            request.setAttribute("exception", BaseResponseCode.NOT_FOUND_TOKEN.getCode());
            filterChain.doFilter(request, response);
        } catch (MalformedJwtException exception) {
            log.error(BaseResponseCode.MALFORMED_TOKEN.getMessage());
            request.setAttribute("exception", BaseResponseCode.MALFORMED_TOKEN.getCode());
            filterChain.doFilter(request, response);
        }/*catch (ClaimJwtException exception) {
            log.error("Token ClaimJwtException");
        } catch (CompressionException exception) {
            log.error("Token CompressionException");
        }*/  catch (Exception exception) {
            log.error(exception.getLocalizedMessage());
            log.error(BaseResponseCode.TOKEN_ERROR.getMessage());
            request.setAttribute("exception", BaseResponseCode.TOKEN_ERROR.getCode());
            filterChain.doFilter(request, response);
        }
    }
}
