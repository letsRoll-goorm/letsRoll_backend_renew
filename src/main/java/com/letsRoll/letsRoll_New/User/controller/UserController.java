package com.letsRoll.letsRoll_New.User.controller;

import com.letsRoll.letsRoll_New.Global.JWT.JwtUtils;
import com.letsRoll.letsRoll_New.Global.common.BaseResponse;
import com.letsRoll.letsRoll_New.User.dto.response.UserTokenRes;
import com.letsRoll.letsRoll_New.User.entity.User;
import com.letsRoll.letsRoll_New.User.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private UserService userService;
    private final JwtUtils jwtUtils;;

        @GetMapping("/index")
    public BaseResponse<String> loginForm(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new BaseResponse<>("authentication "+ user.getUserName() );
    }

    @GetMapping("/token")
    public BaseResponse<UserTokenRes> refreshToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UserTokenRes.toDto(jwtUtils.createToken(user.getSocialId(), user.getEmail()));

        return new BaseResponse<>(UserTokenRes.toDto(jwtUtils.createToken(user.getSocialId(), user.getEmail())));
    }

}

