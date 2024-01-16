package com.letsRoll.letsRoll_New.Global.JWT.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsRoll.letsRoll_New.Global.common.BaseResponse;
import com.letsRoll.letsRoll_New.Global.exception.BaseResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("customAuthenticationEntryPoint")
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");

        if (exception == null) {
            setResponse(response, BaseResponseCode.NOT_FOUND_TOKEN);
        } else if (exception.equals(BaseResponseCode.WRONG_TYPE_TOKEN.getCode())) {
            setResponse(response, BaseResponseCode.WRONG_TYPE_TOKEN);
        } else if (exception.equals(BaseResponseCode.EXPIRED_TOKEN.getCode())) {
            setResponse(response, BaseResponseCode.EXPIRED_TOKEN);
        } else if (exception.equals(BaseResponseCode.UNSUPPORTED_TOKEN.getCode())) {
            setResponse(response, BaseResponseCode.UNSUPPORTED_TOKEN);
        } else if (exception.equals(BaseResponseCode.NOT_FOUND_TOKEN.getCode())) {
            setResponse(response, BaseResponseCode.NOT_FOUND_TOKEN);
        } else if (exception.equals(BaseResponseCode.MALFORMED_TOKEN.getCode())) {
            setResponse(response, BaseResponseCode.MALFORMED_TOKEN);
        } else {
            setResponse(response, BaseResponseCode.TOKEN_ERROR);
        }
    }

    private void setResponse(HttpServletResponse response, BaseResponseCode baseResponseCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8"); //한글 출력을 위해 getWriter와 함꼐 사용
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        BaseResponse<Object> baseResponse = new BaseResponse<>(baseResponseCode);
        objectMapper.writeValueAsString(baseResponse);

        response.getWriter().write(objectMapper.writeValueAsString(baseResponse));
    }
}
