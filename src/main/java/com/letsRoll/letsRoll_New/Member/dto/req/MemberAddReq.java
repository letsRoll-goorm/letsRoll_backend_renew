package com.letsRoll.letsRoll_New.Member.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberAddReq{
    private String nickname;
    private String role;
    private String password;
}
