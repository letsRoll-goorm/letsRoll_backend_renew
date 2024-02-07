package com.letsRoll.letsRoll_New.Member.controller;

import com.letsRoll.letsRoll_New.Member.dto.res.MemberResDto;
import com.letsRoll.letsRoll_New.Member.service.MemberService;
import com.letsRoll.letsRoll_New.Global.common.BaseResponse;
import com.letsRoll.letsRoll_New.User.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @GetMapping("/{projectId}")
    public BaseResponse<List<MemberResDto>> getMemberList(@PathVariable Long projectId) {
        return new BaseResponse<>(memberService.getMemberList(projectId));
    }
    @GetMapping("/{projectId}/member")
    public BaseResponse<MemberResDto> getMember(@PathVariable Long projectId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new BaseResponse<>(memberService.getMember(projectId, user));
    }
}
